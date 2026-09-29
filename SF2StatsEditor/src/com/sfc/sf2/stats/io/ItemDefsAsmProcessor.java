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
import com.sfc.sf2.stats.ItemDefinition;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class ItemDefsAsmProcessor extends AbstractAsmProcessor<ItemDefinition[], EmptyPackage> {

    @Override
    protected ItemDefinition[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<ItemDefinition> entries = new ArrayList<>();
        ItemDefinition current = null;
        String pendingName = null;
        int pendingIndex = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String comment = StringHelpers.extractComment(line);
            String code = StringHelpers.trimAndRemoveComments(line);
            if (code.isEmpty()) {
                if (comment != null && comment.contains(":")) {
                    pendingName = parseItemName(comment);
                    pendingIndex = parseItemIndex(comment, entries.size());
                }
                continue;
            }
            if (code.endsWith(":") && !code.contains(" ")) {
                continue;
            }
            if (code.startsWith("equipFlags")) {
                current = new ItemDefinition();
                current.setIndex(pendingIndex);
                current.setName(pendingName == null ? ("Item " + pendingIndex) : pendingName);
                current.setEquipFlags(AsmLineJoiner.argsAfterKeyword(code, "equipFlags"));
                entries.add(current);
                pendingName = null;
                pendingIndex = entries.size();
            } else if (current == null) {
                continue;
            } else if (code.startsWith("range")) {
                String[] parts = AsmLineJoiner.splitCsv(AsmLineJoiner.argsAfterKeyword(code, "range"));
                current.setRangeMin(parts.length > 0 ? StringHelpers.getValueInt(parts[0]) : 0);
                current.setRangeMax(parts.length > 1 ? StringHelpers.getValueInt(parts[1]) : 0);
            } else if (code.startsWith("price")) {
                current.setPrice(StringHelpers.getValueInt(AsmLineJoiner.argsAfterKeyword(code, "price")));
            } else if (code.startsWith("itemType")) {
                current.setItemType(AsmLineJoiner.argsAfterKeyword(code, "itemType"));
            } else if (code.startsWith("useSpell")) {
                current.setUseSpell(AsmLineJoiner.argsAfterKeyword(code, "useSpell"));
            } else if (code.startsWith("equipEffects")) {
                String joined = AsmLineJoiner.joinContinuations(reader, code);
                String[] parts = AsmLineJoiner.splitCsv(AsmLineJoiner.argsAfterKeyword(joined, "equipEffects"));
                if (parts.length > 0) current.setEffect1(parts[0]);
                if (parts.length > 1) current.setEffectParam1(parseLooseInt(parts[1]));
                if (parts.length > 2) current.setEffect2(parts[2]);
                if (parts.length > 3) current.setEffectParam2(parseLooseInt(parts[3]));
                if (parts.length > 4) current.setEffect3(parts[4]);
                if (parts.length > 5) current.setEffectParam3(parseLooseInt(parts[5]));
            }
        }
        if (entries.isEmpty()) {
            throw new AsmException("No item definitions found.");
        }
        return entries.toArray(ItemDefinition[]::new);
    }

    private static String parseItemName(String comment) {
        int colon = comment.indexOf(':');
        if (colon < 0) {
            return comment.trim();
        }
        return comment.substring(colon + 1).trim();
    }

    private static int parseItemIndex(String comment, int fallback) {
        int colon = comment.indexOf(':');
        if (colon <= 0) {
            return fallback;
        }
        try {
            return Integer.parseInt(comment.substring(0, colon).trim());
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private static int parseLooseInt(String value) {
        try {
            return StringHelpers.getValueInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }

    @Override
    protected String getHeaderName(ItemDefinition[] item, EmptyPackage pckg) {
        return "Item definitions";
    }

    @Override
    protected void packageAsmData(FileWriter writer, ItemDefinition[] item, EmptyPackage pckg) throws IOException, AsmException {
        writer.write("table_ItemDefinitions:\n\n");
        writer.write("; Syntax        equipFlags   [EQUIPFLAG_]bitfield\n");
        writer.write(";               range        min, max 0-3\n");
        writer.write(";               price        0-65535\n");
        writer.write(";               itemType     [ITEMTYPE_]bitfield\n");
        writer.write(";               useSpell     [SPELL_]enum[|[SPELL_]level]\n");
        writer.write(";               equipEffects [EQUIPEFFECT_]enum, parameter, &\n\n");
        for (int i = 0; i < item.length; i++) {
            ItemDefinition def = item[i];
            writer.write(String.format("                ; %d: %s\n", def.getIndex(), nz(def.getName(), "Item")));
            writer.write(String.format("                equipFlags   %s\n", nz(def.getEquipFlags(), "NONE")));
            writer.write(String.format("                range        %d, %d\n", def.getRangeMin(), def.getRangeMax()));
            writer.write(String.format("                price        %d\n", def.getPrice()));
            writer.write(String.format("                itemType     %s\n", nz(def.getItemType(), "NONE")));
            writer.write(String.format("                useSpell     %s\n", nz(def.getUseSpell(), "NOTHING")));
            writer.write(String.format("                equipEffects %s, %d, &\n", nz(def.getEffect1(), "NONE"), def.getEffectParam1()));
            writer.write(String.format("                             %s, %d, &\n", nz(def.getEffect2(), "NONE"), def.getEffectParam2()));
            writer.write(String.format("                             %s, %d\n", nz(def.getEffect3(), "NONE"), def.getEffectParam3()));
            if (i < item.length - 1) {
                writer.write("\n");
            }
        }
    }

    private static String nz(String value, String fallback) {
        return value == null || value.isEmpty() ? fallback : value;
    }
}
