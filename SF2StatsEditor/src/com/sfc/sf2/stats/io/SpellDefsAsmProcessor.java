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
import com.sfc.sf2.stats.SpellDefinition;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class SpellDefsAsmProcessor extends AbstractAsmProcessor<SpellDefinition[], EmptyPackage> {

    @Override
    protected SpellDefinition[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<SpellDefinition> entries = new ArrayList<>();
        SpellDefinition current = null;
        String line;
        while ((line = reader.readLine()) != null) {
            String code = StringHelpers.trimAndRemoveComments(line);
            if (code.isEmpty() || code.endsWith(":")) {
                continue;
            }
            if (code.startsWith("entry")) {
                current = new SpellDefinition();
                current.setEntry(AsmLineJoiner.argsAfterKeyword(code, "entry"));
                entries.add(current);
            } else if (current == null) {
                continue;
            } else if (code.startsWith("mpCost")) {
                current.setMpCost(StringHelpers.getValueInt(AsmLineJoiner.argsAfterKeyword(code, "mpCost")));
            } else if (code.startsWith("animation")) {
                current.setAnimation(AsmLineJoiner.argsAfterKeyword(code, "animation"));
            } else if (code.startsWith("properties")) {
                current.setProperties(AsmLineJoiner.argsAfterKeyword(code, "properties"));
            } else if (code.startsWith("range")) {
                String[] parts = AsmLineJoiner.splitCsv(AsmLineJoiner.argsAfterKeyword(code, "range"));
                current.setRangeMin(parts.length > 0 ? StringHelpers.getValueInt(parts[0]) : 0);
                current.setRangeMax(parts.length > 1 ? StringHelpers.getValueInt(parts[1]) : 0);
            } else if (code.startsWith("radius")) {
                current.setRadius(StringHelpers.getValueInt(AsmLineJoiner.argsAfterKeyword(code, "radius")));
            } else if (code.startsWith("power")) {
                current.setPower(StringHelpers.getValueInt(AsmLineJoiner.argsAfterKeyword(code, "power")));
            }
        }
        if (entries.isEmpty()) {
            throw new AsmException("No spell entries found.");
        }
        return entries.toArray(SpellDefinition[]::new);
    }

    @Override
    protected String getHeaderName(SpellDefinition[] item, EmptyPackage pckg) {
        return "Spell definitions";
    }

    @Override
    protected void packageAsmData(FileWriter writer, SpellDefinition[] item, EmptyPackage pckg) throws IOException, AsmException {
        writer.write("table_SpellDefinitions:\n\n");
        writer.write("; Syntax        entry      [SPELL_]enum[|level]\n");
        writer.write(";               mpCost     0-255\n");
        writer.write(";               animation  [SPELLANIMATION_]enum[|variation]\n");
        writer.write(";               properties [SPELLPROPS_]bitfield\n");
        writer.write(";               range      min, max 0-3\n");
        writer.write(";               radius     0-2\n");
        writer.write(";               power      0-255\n\n");
        for (int i = 0; i < item.length; i++) {
            SpellDefinition spell = item[i];
            writer.write(String.format("                entry      %s\n", nz(spell.getEntry(), "HEAL")));
            writer.write(String.format("                mpCost     %d\n", spell.getMpCost()));
            writer.write(String.format("                animation  %s\n", nz(spell.getAnimation(), "NONE")));
            writer.write(String.format("                properties %s\n", nz(spell.getProperties(), "TYPE_ATTACK")));
            writer.write(String.format("                range      %d, %d\n", spell.getRangeMin(), spell.getRangeMax()));
            writer.write(String.format("                radius     %d\n", spell.getRadius()));
            writer.write(String.format("                power      %d\n", spell.getPower()));
            if (i < item.length - 1) {
                writer.write("\n");
            }
        }
    }

    private static String nz(String value, String fallback) {
        return value == null || value.isEmpty() ? fallback : value;
    }
}
