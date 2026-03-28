/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.io;

import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.MapItem;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapItemAsmProcessor extends AbstractAsmProcessor<MapItem[], MapItemPackage> {

    @Override
    protected MapItem[] parseAsmData(BufferedReader reader, MapItemPackage pckg) throws IOException, AsmException {
        ArrayList<MapItem> itemsList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("mapItem")) {
                String comment = StringHelpers.extractComment(line);
                line = StringHelpers.trimAndRemoveComments(line);
                String[] split = line.substring(line.indexOf(' ')).split(",");
                int x = StringHelpers.getValueInt(split[0]);
                int y = StringHelpers.getValueInt(split[1]);
                int flag = StringHelpers.getValueInt(split[2]);
                String item = split[3].trim();
                itemsList.add(new MapItem(x, y, flag, item, comment));
            }
        }
        MapItem[] items = new MapItem[itemsList.size()];
        items = itemsList.toArray(items);
        return items;
    }

    @Override
    protected String getHeaderName(MapItem[] item, MapItemPackage pckg) {
        return pckg.isChestItem() ? "Chest items" : "Other items";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapItem[] item, MapItemPackage pckg) throws IOException, AsmException {
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\t\t\t\tmapItem %2d, %2d, %3d, %s", item[i].getX(), item[i].getY(), item[i].getFlag(), item[i].getItem()));
            if (item[i].getComment() != null && item[i].getComment().length() > 0) {
                writer.write(String.format("\t; %s\n", item[i].getComment()));
            } else {
                writer.write('\n');
            }
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
