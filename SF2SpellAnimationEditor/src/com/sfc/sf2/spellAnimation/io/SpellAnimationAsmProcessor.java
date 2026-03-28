/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation.io;

import com.sfc.sf2.core.io.AbstractAsmProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.spellAnimation.SpellAnimation;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class SpellAnimationAsmProcessor extends AbstractAsmProcessor<SpellAnimation> {
    
    @Override
    protected void parseAsmData(BufferedReader reader, SpellAnimation item) throws DisassemblyException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        //TODO upate to the new format for spell animations
        /*byte frameIndex = -1;
        List<SpellSubAnimation> subAnimations = new ArrayList();
        SpellSubAnimation spellSubAnimation = null;
        List<SpellAnimationFrame> animationFrames = null;
        while(scan.hasNext()) {
            String line = scan.nextLine().trim();
            short newLineVal;
            if (line.length() > 2 && !line.startsWith(";")) {
                isHeader = false;
                if (!line.startsWith("dc.")) {
                    //Store previous animation
                    if (spellSubAnimation != null) {
                        SpellAnimationFrame[] frames = new SpellAnimationFrame[animationFrames.size()];
                        frames = animationFrames.toArray(frames);
                        spellSubAnimation.setFrames(frames);
                    }
                    //Setup next animation
                    frameIndex = 0;
                    spellSubAnimation = new SpellSubAnimation();
                    spellSubAnimation.setName(line.substring(0, line.indexOf(":")).trim());
                    subAnimations.add(spellSubAnimation);
                    animationFrames = new ArrayList();
                    line = line.substring(line.indexOf("dc.")+4).trim();
                    String line2 = scan.nextLine();
                    line2 = line2.substring(line2.indexOf("dc.")+4).trim();
                    newLineVal = CombineBytesToWord(valueOf(line), valueOf(line2));
                } else {
                    frameIndex++;
                    line = line.substring(line.indexOf("dc.")+4).trim();
                    String line2 = scan.nextLine();
                    line2 = line2.substring(line2.indexOf("dc.")+4).trim();
                    newLineVal = CombineBytesToWord(valueOf(line), valueOf(line2));
                }

                SpellAnimationFrame frame = new SpellAnimationFrame();
                frame.setFrameIndex(frameIndex);
                frame.setX(newLineVal);
                frame.setY(getNextWordLine(scan));
                frame.setTileIndex((short)(getNextWordLine(scan) - 0x520));
                int flags = getNextWordLine(scan);
                frame.setW((byte)(((flags >> 8) & 3)+1));
                frame.setH((byte)(((flags >> 10) & 3)+1));
                frame.setForeground((byte)(flags & 0xFF));
                animationFrames.add(frame);

            } else if (isHeader) {
                header += line + "\n";
            }
        }   

        if (spellSubAnimation != null) {
            SpellAnimationFrame[] frames = new SpellAnimationFrame[animationFrames.size()];
            frames = animationFrames.toArray(frames);
            spellSubAnimation.setFrames(frames);
        }
        spellAnimation = new SpellAnimation();
        SpellSubAnimation[] subAnims = new SpellSubAnimation[subAnimations.size()];
        subAnims = subAnimations.toArray(subAnims);
        spellAnimation.setSpellSubAnimations(subAnims);*/
    }

    @Override
    protected void writeAsmHeader(FileWriter writer, SpellAnimation item) throws DisassemblyException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void packageAsmData(FileWriter writer, SpellAnimation item) throws DisassemblyException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        //TODO upate to the new format for spell animations
        /*byte[] animationFileBytes;
        int frameNumber = anim.getFrames().length;
        animationFileBytes = new byte[8 + frameNumber*8];

        animationFileBytes[0] = (byte)(frameNumber);
        animationFileBytes[1] = (byte)(anim.getSpellInitFrame());
        animationFileBytes[2] = (byte)(anim.getSpellAnim());
        animationFileBytes[3] = (byte)(anim.getEndSpellAnim());

        for(int i=0;i<frameNumber;i++){
            animationFileBytes[8+i*8+0] = (byte)(anim.getFrames()[i].getFrameIndex());
            animationFileBytes[8+i*8+1] = (byte)(anim.getFrames()[i].getDuration());
            animationFileBytes[8+i*8+2] = (byte)(anim.getFrames()[i].getX());
            animationFileBytes[8+i*8+3] = (byte)(anim.getFrames()[i].getY());
        }
        Path animFilePath = Paths.get(filepath);
        Files.write(animFilePath,animationFileBytes);
        System.out.println(animationFileBytes.length + " bytes into " + animFilePath);*/
    }
}
