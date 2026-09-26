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
import com.sfc.sf2.stats.ClassDefinition;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class ClassDefsAsmProcessor extends AbstractAsmProcessor<ClassDefinition[], EmptyPackage> {

    @Override
    protected ClassDefinition[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<ClassDefinition> entries = new ArrayList<>();
        ClassDefinition current = null;
        String line;
        while ((line = reader.readLine()) != null) {
            String comment = StringHelpers.extractComment(line);
            String code = StringHelpers.trimAndRemoveComments(line);
            if (code.isEmpty() || (code.endsWith(":") && !code.contains(" "))) {
                continue;
            }
            if (code.startsWith("movetype")) {
                if (current != null) {
                    current.setMoveType(AsmLineJoiner.argsAfterKeyword(code, "movetype"));
                }
            } else if (code.startsWith("mov")) {
                current = new ClassDefinition();
                current.setIndex(entries.size());
                current.setMov(StringHelpers.getValueInt(AsmLineJoiner.argsAfterKeyword(code, "mov")));
                if (comment != null && comment.contains(":")) {
                    current.setIndex(parseIndex(comment, entries.size()));
                    current.setName(parseName(comment));
                } else {
                    current.setName("CLASS" + current.getIndex());
                }
                entries.add(current);
            } else if (current == null) {
                continue;
            } else if (code.startsWith("resistance")) {
                current.setResistance(AsmLineJoiner.argsAfterKeyword(code, "resistance"));
            } else if (code.startsWith("prowess")) {
                current.setProwess(AsmLineJoiner.argsAfterKeyword(code, "prowess"));
            }
        }
        if (entries.isEmpty()) {
            throw new AsmException("No class definitions found.");
        }
        return entries.toArray(ClassDefinition[]::new);
    }

    private static int parseIndex(String comment, int fallback) {
        int colon = comment.indexOf(':');
        try {
            return Integer.parseInt(comment.substring(0, colon).trim());
        } catch (Exception ex) {
            return fallback;
        }
    }

    private static String parseName(String comment) {
        int colon = comment.indexOf(':');
        return comment.substring(colon + 1).trim();
    }

    @Override
    protected String getHeaderName(ClassDefinition[] item, EmptyPackage pckg) {
        return "Class definitions";
    }

    @Override
    protected void packageAsmData(FileWriter writer, ClassDefinition[] item, EmptyPackage pckg) throws IOException, AsmException {
        writer.write("table_ClassDefinitions:\n\n");
        writer.write("; Syntax        mov        0-255\n");
        writer.write(";               resistance [RESISTANCE_]bitfield\n");
        writer.write(";               movetype   [MOVETYPE_UPPER_]enum (or index)\n");
        writer.write(";               prowess    [PROWESS_]bitfield\n\n");
        for (int i = 0; i < item.length; i++) {
            ClassDefinition def = item[i];
            writer.write(String.format("                mov %d                   ; %d: %s\n", def.getMov(), def.getIndex(), nz(def.getName(), "CLASS")));
            writer.write(String.format("                resistance %s\n", nz(def.getResistance(), "NONE")));
            writer.write(String.format("                movetype   %s\n", nz(def.getMoveType(), "REGULAR")));
            writer.write(String.format("                prowess    %s\n", nz(def.getProwess(), "NONE")));
            if (i < item.length - 1) {
                writer.write("\n");
            }
        }
    }

    private static String nz(String value, String fallback) {
        return value == null || value.isEmpty() ? fallback : value;
    }
}
