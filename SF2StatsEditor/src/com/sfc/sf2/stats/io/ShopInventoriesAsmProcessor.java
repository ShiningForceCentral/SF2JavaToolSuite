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
import com.sfc.sf2.stats.ShopInventory;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class ShopInventoriesAsmProcessor extends AbstractAsmProcessor<ShopInventory[], EmptyPackage> {

    @Override
    protected ShopInventory[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<ShopInventory> entries = new ArrayList<>();
        String pendingName = null;
        String line;
        while ((line = reader.readLine()) != null) {
            String comment = StringHelpers.extractComment(line);
            String code = StringHelpers.trimAndRemoveComments(line);
            if (code.isEmpty()) {
                if (comment != null && !comment.isEmpty() && !comment.startsWith("Syntax") && !comment.startsWith("Note")) {
                    pendingName = comment;
                }
                continue;
            }
            if (code.endsWith(":") && !code.contains(" ")) {
                continue;
            }
            if (code.startsWith("shopInventory")) {
                String joined = AsmLineJoiner.joinContinuations(reader, code);
                ShopInventory shop = new ShopInventory();
                shop.setIndex(entries.size());
                shop.setName(pendingName == null ? ("Shop " + (entries.size() + 1)) : pendingName);
                shop.setItems(AsmLineJoiner.argsAfterKeyword(joined, "shopInventory"));
                entries.add(shop);
                pendingName = null;
            }
        }
        if (entries.isEmpty()) {
            throw new AsmException("No shop inventories found.");
        }
        return entries.toArray(ShopInventory[]::new);
    }

    @Override
    protected String getHeaderName(ShopInventory[] item, EmptyPackage pckg) {
        return "Shop inventories";
    }

    @Override
    protected void packageAsmData(FileWriter writer, ShopInventory[] item, EmptyPackage pckg) throws IOException, AsmException {
        writer.write("list_ShopInventories:\n\n");
        writer.write("; Syntax        shopInventory [ITEM_]enum,..[ITEM_]enum\n\n");
        for (int i = 0; i < item.length; i++) {
            writer.write("                ; " + (item[i].getName() == null ? ("Shop " + (i + 1)) : item[i].getName()) + "\n");
            AsmLineJoiner.writeContinuedCsv(writer, "shopInventory", item[i].getItems());
            if (i < item.length - 1) {
                writer.write("\n");
            }
        }
    }
}
