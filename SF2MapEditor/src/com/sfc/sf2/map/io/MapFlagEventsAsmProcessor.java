/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.io;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.MapStringHelpers;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.MapFlagCopyEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapFlagEventsAsmProcessor extends AbstractAsmProcessor<MapFlagCopyEvent[], EmptyPackage> {

    @Override
    protected MapFlagCopyEvent[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<MapFlagCopyEvent> flagCopiesList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("fbcFlag")) {
                String flagComment = StringHelpers.extractComment(line);
                line = StringHelpers.trimAndRemoveComments(line);
                int flag, sourceX, sourceY, width, height, destX, destY;
                
                flag = StringHelpers.getValueInt(line.substring(line.indexOf(" ")));
                line = reader.readLine();
                String comment = StringHelpers.extractComment(line);
                line = StringHelpers.trimAndRemoveComments(line);
                String[] split = line.substring(line.indexOf(" ")+1).split(",");
                sourceX = StringHelpers.getValueInt(split[0]);
                sourceY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "fbcSize", flagCopiesList.size()+1);
                width = StringHelpers.getValueInt(split[0]);
                height = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "fbcDest", flagCopiesList.size()+1);
                destX = StringHelpers.getValueInt(split[0]);
                destY = StringHelpers.getValueInt(split[1]);
                
                flagCopiesList.add(new MapFlagCopyEvent(flag, sourceX, sourceY, sourceX+width-1, sourceY+height-1, destX, destY, flagComment, comment));
            }
        }
        MapFlagCopyEvent[] flagCopies = new MapFlagCopyEvent[flagCopiesList.size()];
        flagCopies = flagCopiesList.toArray(flagCopies);
        return flagCopies;
    }

    @Override
    protected String getHeaderName(MapFlagCopyEvent[] item, EmptyPackage pckg) {
        return "Map flag events";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapFlagCopyEvent[] item, EmptyPackage pckg) throws IOException, AsmException {
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\t\t\t\tfbcFlag %3d", item[i].getFlag()));
            if (item[i].getComment() != null && item[i].getComment().length() > 0) {
                writer.write(String.format("\t\t\t\t; %s\n", item[i].getFlagComment()));
            } else {
                writer.write('\n');
            }
            writer.write(String.format("\t\t\t\t\tfbcSource %2d, %2d", item[i].getSourceStartX(), item[i].getSourceStartY()));
            if (item[i].getComment() != null && item[i].getComment().length() > 0) {
                writer.write(String.format("\t; %s\n", item[i].getComment()));
            } else {
                writer.write('\n');
            }
            writer.write(String.format("\t\t\t\t\tfbcSize   %2d, %2d\n", item[i].getWidth(), item[i].getHeight()));
            writer.write(String.format("\t\t\t\t\tfbcDest   %2d, %2d\n", item[i].getDestStartX(), item[i].getDestStartY()));
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
