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
import com.sfc.sf2.map.MapCopyEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapStepEventsAsmProcessor extends AbstractAsmProcessor<MapCopyEvent[], EmptyPackage> {

    @Override
    protected MapCopyEvent[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<MapCopyEvent> stepCopiesList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("sbc")) {
                String comment = StringHelpers.extractComment(line);
                line = StringHelpers.trimAndRemoveComments(line);
                int triggerX, triggerY, sourceX, sourceY, width, height, destX, destY;
                
                String[] split = line.substring(line.indexOf(" ")).split(",");
                triggerX = StringHelpers.getValueInt(split[0]);
                triggerY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "sbcSource", stepCopiesList.size()+1);
                sourceX = StringHelpers.getValueInt(split[0]);
                sourceY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "sbcSize", stepCopiesList.size()+1);
                width = StringHelpers.getValueInt(split[0]);
                height = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "sbcDest", stepCopiesList.size()+1);
                destX = StringHelpers.getValueInt(split[0]);
                destY = StringHelpers.getValueInt(split[1]);
                
                stepCopiesList.add(new MapCopyEvent(triggerX, triggerY, sourceX, sourceY, sourceX+width-1, sourceY+height-1, destX, destY, comment));
            }
        }
        MapCopyEvent[] stepCopies = new MapCopyEvent[stepCopiesList.size()];
        stepCopies = stepCopiesList.toArray(stepCopies);
        return stepCopies;
    }

    @Override
    protected String getHeaderName(MapCopyEvent[] item, EmptyPackage pckg) {
        return "Map step events";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapCopyEvent[] item, EmptyPackage pckg) throws IOException, AsmException {
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\t\t\t\tsbc %2d, %2d", item[i].getTriggerX(), item[i].getTriggerY()));
            if (item[i].getComment() != null && item[i].getComment().length() > 0) {
                writer.write(String.format("\t; %s\n", item[i].getComment()));
            } else {
                writer.write('\n');
            }
            writer.write(String.format("\t\t\t\t\tsbcSource %2d, %2d\n", item[i].getSourceStartX(), item[i].getSourceStartY()));
            writer.write(String.format("\t\t\t\t\tsbcSize   %2d, %2d\n", item[i].getWidth(), item[i].getHeight()));
            writer.write(String.format("\t\t\t\t\tsbcDest   %2d, %2d\n", item[i].getDestStartX(), item[i].getDestStartY()));
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
