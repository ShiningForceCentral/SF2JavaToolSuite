/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation.io;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.spellAnimation.SpellAnimation;
import com.sfc.sf2.spellAnimation.SpellAnimationFrame;
import com.sfc.sf2.spellAnimation.SpellSubAnimation;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Parses SF2DISASM vdpSpell spell-animation data
 * (e.g. boltanimdata.asm).
 *
 * @author TiMMy
 */
public class SpellAnimationAsmProcessor extends AbstractAsmProcessor<SpellAnimation, EmptyPackage> {

    @Override
    protected SpellAnimation parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<SpellSubAnimation> subAnimations = new ArrayList<>();
        SpellSubAnimation currentSub = null;
        ArrayList<SpellAnimationFrame> currentFrames = null;
        short frameIndex = 0;
        String line;

        while ((line = reader.readLine()) != null) {
            String code = StringHelpers.trimAndRemoveComments(line);
            if (code.isEmpty()) {
                String trimmed = line.trim();
                if (trimmed.startsWith(";") && currentFrames != null && !currentFrames.isEmpty()) {
                    frameIndex++;
                }
                continue;
            }

            if (!code.startsWith("vdpSpell") && code.contains(":")) {
                flushSubAnimation(subAnimations, currentSub, currentFrames);
                String name = code.substring(0, code.indexOf(':')).trim();
                currentSub = new SpellSubAnimation();
                currentSub.setName(name);
                currentFrames = new ArrayList<>();
                frameIndex = 0;
                continue;
            }

            if (code.startsWith("vdpSpell")) {
                if (currentSub == null) {
                    currentSub = new SpellSubAnimation();
                    currentSub.setName("SpellAnim");
                    currentFrames = new ArrayList<>();
                    frameIndex = 0;
                }
                currentFrames.add(parseVdpSpell(code, frameIndex));
            }
        }

        flushSubAnimation(subAnimations, currentSub, currentFrames);
        if (subAnimations.isEmpty()) {
            throw new AsmException("Spell animation ASM contained no vdpSpell data.");
        }

        SpellAnimation animation = new SpellAnimation();
        SpellSubAnimation[] subs = new SpellSubAnimation[subAnimations.size()];
        animation.setSpellSubAnimations(subAnimations.toArray(subs));
        return animation;
    }

    private static void flushSubAnimation(ArrayList<SpellSubAnimation> dest, SpellSubAnimation currentSub,
            ArrayList<SpellAnimationFrame> currentFrames) {
        if (currentSub == null || currentFrames == null) {
            return;
        }
        SpellAnimationFrame[] frames = new SpellAnimationFrame[currentFrames.size()];
        currentSub.setFrames(currentFrames.toArray(frames));
        dest.add(currentSub);
    }

    private static SpellAnimationFrame parseVdpSpell(String code, short frameIndex) throws AsmException {
        String args = code.substring("vdpSpell".length()).trim();
        String[] parts = args.split("\\s*,\\s*");
        if (parts.length < 4) {
            throw new AsmException("Invalid vdpSpell line: " + code);
        }
        short x = (short) StringHelpers.getValueInt(parts[0]);
        short y = (short) StringHelpers.getValueInt(parts[1]);
        short tileIndex = parseTileIndex(parts[2]);
        byte w = 1;
        byte h = 1;
        boolean hFlip = false;
        boolean foreground = false;
        String[] flagTokens = parts[3].split("\\|");
        for (String token : flagTokens) {
            token = token.trim();
            if (token.isEmpty()) {
                continue;
            }
            if (token.length() > 1 && (token.charAt(0) == 'V' || token.charAt(0) == 'v') && Character.isDigit(token.charAt(1))) {
                h = Byte.parseByte(token.substring(1));
            } else if (token.length() > 1 && (token.charAt(0) == 'H' || token.charAt(0) == 'h') && Character.isDigit(token.charAt(1))) {
                w = Byte.parseByte(token.substring(1));
            } else {
                int raw = StringHelpers.getValueInt(token);
                hFlip = (raw & 1) != 0;
                foreground = raw >= 32;
            }
        }
        return new SpellAnimationFrame(frameIndex, tileIndex, x, y, w, h, foreground, hFlip);
    }

    private static short parseTileIndex(String token) {
        token = token.trim();
        if (token.regionMatches(true, 0, "SPELLTILE", 0, "SPELLTILE".length())) {
            return (short) StringHelpers.getNumberFromString(token.substring("SPELLTILE".length()));
        }
        return (short) StringHelpers.getValueInt(token);
    }

    @Override
    protected String getHeaderName(SpellAnimation item, EmptyPackage pckg) {
        return "Spell animation data";
    }

    @Override
    protected void packageAsmData(FileWriter writer, SpellAnimation item, EmptyPackage pckg) throws IOException, AsmException {
        SpellSubAnimation[] subs = item.getSpellSubAnimations();
        if (subs == null) {
            throw new AsmException("Spell animation has no sub-animations to export.");
        }
        for (int s = 0; s < subs.length; s++) {
            SpellSubAnimation sub = subs[s];
            String name = sub.getName() == null || sub.getName().isEmpty() ? ("SpellAnim_" + (char) ('A' + s)) : sub.getName();
            writer.write(name);
            writer.write(":\n");
            SpellAnimationFrame[] frames = sub.getFrames();
            if (frames == null) {
                continue;
            }
            short lastFrame = -1;
            for (int i = 0; i < frames.length; i++) {
                SpellAnimationFrame frame = frames[i];
                if (lastFrame != -1 && frame.getFrameIndex() != lastFrame) {
                    writer.write("                ;\n");
                }
                lastFrame = frame.getFrameIndex();
                writer.write(String.format("                vdpSpell %d, %d, %s, V%d|H%d|%d\n",
                        frame.getX(),
                        frame.getY(),
                        formatTile(frame.getTileIndex()),
                        frame.getH(),
                        frame.getW(),
                        formatFlags(frame)));
            }
            if (s < subs.length - 1) {
                writer.write("\n");
            }
        }
    }

    private static String formatTile(short tileIndex) {
        if (tileIndex <= 0) {
            return "0";
        }
        return "SPELLTILE" + tileIndex;
    }

    private static int formatFlags(SpellAnimationFrame frame) {
        if (!frame.getForeground() && frame.getTileIndex() <= 0) {
            return 0;
        }
        return frame.getHFlip() ? 33 : 32;
    }
}
