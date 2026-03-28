/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.Direction;
import com.sfc.sf2.helpers.MapStringHelpers;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.MapWarpEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapWarpEventsAsmProcessor extends AbstractAsmProcessor<MapWarpEvent[], EmptyPackage> {

    @Override
    protected MapWarpEvent[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<MapWarpEvent> warpsList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("mWarp")) {
                String comment = StringHelpers.extractComment(line);
                line = StringHelpers.trimAndRemoveComments(line);
                int triggerX, triggerY, destX, destY;
                String warpType, destMap;
                Direction scrollDirection, facing;
                
                String[] split = line.substring(line.indexOf(' ')).split(",");
                triggerX = StringHelpers.getValueInt(split[0]);
                triggerY = StringHelpers.getValueInt(split[1]);
                
                line = reader.readLine().trim();
                split = line.split("\\s+");
                switch (split[0].trim()) {
                    default:
                        Console.logger().warning("WARNING Unknown warp type '" + split[0] + "' detected at warp event " + (warpsList.size()+1));
                        //Then fallthrough and treat as a no-scroll warp
                    case "warpNoScroll":
                        warpType = "warpNoScroll";
                        scrollDirection = Direction.DOWN;
                        break;
                    case "warpScroll":
                        warpType = "warpScroll";
                        scrollDirection = Enum.valueOf(Direction.class, split[1]);
                        break;
                }
                
                destMap = MapStringHelpers.getNextLineSingle(reader, "warpMap", warpsList.size()+1);
                destMap = destMap.replace("MAP_", "");
                
                split = MapStringHelpers.getNextLineMulti(reader, "warpDest", warpsList.size()+1);
                destX = StringHelpers.getValueInt(split[0]);
                destY = StringHelpers.getValueInt(split[1]);
                
                String face = MapStringHelpers.getNextLineSingle(reader, "warpFacing", warpsList.size()+1);
                facing = Enum.valueOf(Direction.class, face);
                
                warpsList.add(new MapWarpEvent(triggerX, triggerY, warpType, scrollDirection, destMap, destX, destY, facing, comment));
            }
        }
        MapWarpEvent[] warps = new MapWarpEvent[warpsList.size()];
        warps = warpsList.toArray(warps);
        return warps;
    }

    @Override
    protected String getHeaderName(MapWarpEvent[] item, EmptyPackage pckg) {
        return "Map warp events";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapWarpEvent[] item, EmptyPackage pckg) throws IOException, AsmException {
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\t\t\t\tmWarp %3d, %3d", item[i].getTriggerX(), item[i].getTriggerY()));
            if (item[i].getComment() != null && item[i].getComment().length() > 0) {
                writer.write(String.format("\t; %s\n", item[i].getComment()));
            } else {
                writer.write('\n');
            }
            if (item[i].getWarpType() == "warpNoScroll") {
                writer.write(String.format("\t\t\t\t\t%s\n", item[i].getWarpType()));
            } else {
                writer.write(String.format("\t\t\t\t\t%s %s\n", item[i].getWarpType(), item[i].getScrollDirection()));
            }
            writer.write(String.format("\t\t\t\t\twarpMap    MAP_%s\n", item[i].getDestMap()));
            writer.write(String.format("\t\t\t\t\twarpDest   %2d, %2d\n", item[i].getDestX(), item[i].getDestY()));
            writer.write(String.format("\t\t\t\t\twarpFacing %s\n", item[i].getFacing()));
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
