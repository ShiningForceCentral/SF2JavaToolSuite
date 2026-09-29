/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats.io;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.stats.AllyClassStats;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class AllyStatsAsmProcessor extends AbstractAsmProcessor<AllyClassStats[], EmptyPackage> {

    private int allyIndex;
    private String allyLabel;

    public void setAllyIdentity(int allyIndex, String allyLabel) {
        this.allyIndex = allyIndex;
        this.allyLabel = allyLabel;
    }

    @Override
    protected AllyClassStats[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<AllyClassStats> entries = new ArrayList<>();
        AllyClassStats current = null;
        String line;
        while ((line = reader.readLine()) != null) {
            String code = StringHelpers.trimAndRemoveComments(line);
            if (code.isEmpty()) {
                continue;
            }
            if (code.endsWith(":") && !code.contains(" ")) {
                allyLabel = code.substring(0, code.length() - 1).trim();
                continue;
            }
            if (code.startsWith("forClass")) {
                current = new AllyClassStats();
                current.setAllyIndex(allyIndex);
                current.setAllyLabel(allyLabel);
                current.setClassName(AsmLineJoiner.argsAfterKeyword(code, "forClass"));
                entries.add(current);
            } else if (current == null) {
                continue;
            } else if (code.startsWith("hpGrowth")) {
                applyGrowth(current, AsmLineJoiner.argsAfterKeyword(code, "hpGrowth"), 0);
            } else if (code.startsWith("mpGrowth")) {
                applyGrowth(current, AsmLineJoiner.argsAfterKeyword(code, "mpGrowth"), 1);
            } else if (code.startsWith("attGrowth")) {
                applyGrowth(current, AsmLineJoiner.argsAfterKeyword(code, "attGrowth"), 2);
            } else if (code.startsWith("defGrowth")) {
                applyGrowth(current, AsmLineJoiner.argsAfterKeyword(code, "defGrowth"), 3);
            } else if (code.startsWith("agiGrowth")) {
                applyGrowth(current, AsmLineJoiner.argsAfterKeyword(code, "agiGrowth"), 4);
            } else if (code.startsWith("spellList")) {
                String joined = AsmLineJoiner.joinContinuations(reader, code);
                current.setUseFirstSpellList(false);
                current.setSpellList(AsmLineJoiner.argsAfterKeyword(joined, "spellList"));
            } else if (code.startsWith("useFirstSpellList")) {
                current.setUseFirstSpellList(true);
                current.setSpellList("");
            }
        }
        if (entries.isEmpty()) {
            throw new AsmException("No forClass blocks found in ally stats file.");
        }
        return entries.toArray(AllyClassStats[]::new);
    }

    private static void applyGrowth(AllyClassStats current, String args, int kind) {
        String[] parts = AsmLineJoiner.splitCsv(args);
        int start = parts.length > 0 ? StringHelpers.getValueInt(parts[0]) : 0;
        int projected = parts.length > 1 ? StringHelpers.getValueInt(parts[1]) : 0;
        String curve = parts.length > 2 ? parts[2] : "LINEAR";
        switch (kind) {
            case 0 -> { current.setHpStart(start); current.setHpProjected(projected); current.setHpCurve(curve); }
            case 1 -> { current.setMpStart(start); current.setMpProjected(projected); current.setMpCurve(curve); }
            case 2 -> { current.setAttStart(start); current.setAttProjected(projected); current.setAttCurve(curve); }
            case 3 -> { current.setDefStart(start); current.setDefProjected(projected); current.setDefCurve(curve); }
            case 4 -> { current.setAgiStart(start); current.setAgiProjected(projected); current.setAgiCurve(curve); }
        }
    }

    @Override
    protected String getHeaderName(AllyClassStats[] item, EmptyPackage pckg) {
        return "Ally stats " + String.format("%02d", allyIndex);
    }

    @Override
    protected void packageAsmData(FileWriter writer, AllyClassStats[] item, EmptyPackage pckg) throws IOException, AsmException {
        String label = (item.length > 0 && item[0].getAllyLabel() != null && !item[0].getAllyLabel().isEmpty())
                ? item[0].getAllyLabel()
                : String.format("AllyStats%02d", allyIndex);
        writer.write(label + ":\n");
        writer.write("; Syntax        forClass  [CLASS_]enum\n");
        writer.write(";               hpGrowth  start, projected, [GROWTHCURVE_]enum\n");
        writer.write(";               mpGrowth  start, projected, [GROWTHCURVE_]enum\n");
        writer.write(";               attGrowth start, projected, [GROWTHCURVE_]enum\n");
        writer.write(";               defGrowth start, projected, [GROWTHCURVE_]enum\n");
        writer.write(";               agiGrowth start, projected, [GROWTHCURVE_]enum\n");
        writer.write(";               spellList parameter, [SPELL_]enum[|level],..\n");
        writer.write(";                    *or* useFirstSpellList\n\n");
        for (int i = 0; i < item.length; i++) {
            AllyClassStats entry = item[i];
            writer.write(String.format("                forClass  %s\n", emptyToNone(entry.getClassName())));
            writer.write(String.format("                hpGrowth  %d, %d, %s\n", entry.getHpStart(), entry.getHpProjected(), emptyToLinear(entry.getHpCurve())));
            writer.write(String.format("                mpGrowth  %d, %d, %s\n", entry.getMpStart(), entry.getMpProjected(), emptyToLinear(entry.getMpCurve())));
            writer.write(String.format("                attGrowth %d, %d, %s\n", entry.getAttStart(), entry.getAttProjected(), emptyToLinear(entry.getAttCurve())));
            writer.write(String.format("                defGrowth %d, %d, %s\n", entry.getDefStart(), entry.getDefProjected(), emptyToLinear(entry.getDefCurve())));
            writer.write(String.format("                agiGrowth %d, %d, %s\n", entry.getAgiStart(), entry.getAgiProjected(), emptyToLinear(entry.getAgiCurve())));
            if (entry.isUseFirstSpellList()) {
                writer.write("                useFirstSpellList\n");
            } else {
                String spells = entry.getSpellList() == null ? "" : entry.getSpellList().trim();
                if (spells.isEmpty()) {
                    writer.write("                spellList\n");
                } else {
                    writer.write("                spellList &\n");
                    String[] parts = AsmLineJoiner.splitCsv(spells);
                    for (int p = 0; p < parts.length; p += 2) {
                        String level = parts[p];
                        String spell = (p + 1 < parts.length) ? parts[p + 1] : "NOTHING";
                        boolean last = p + 2 >= parts.length;
                        writer.write("                    " + level + ", " + spell + (last ? "\n" : ", &\n"));
                    }
                }
            }
            if (i < item.length - 1) {
                writer.write("\n");
            }
        }
    }

    private static String emptyToNone(String value) {
        return value == null || value.isEmpty() ? "NONE" : value;
    }

    private static String emptyToLinear(String value) {
        return value == null || value.isEmpty() ? "LINEAR" : value;
    }
}
