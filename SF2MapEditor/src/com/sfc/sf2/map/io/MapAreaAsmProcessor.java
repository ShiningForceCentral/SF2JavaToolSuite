/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.MapStringHelpers;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapEnums;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapAreaAsmProcessor extends AbstractAsmProcessor<MapArea[], MapEnums> {

    private int importedFileDarknessPatchIndex1 = -1;
    private int importedFileDarknessPatchIndex2 = -1;
    
    @Override
    protected MapArea[] parseAsmData(BufferedReader reader, MapEnums pckg) throws IOException, AsmException {
        importedFileDarknessPatchIndex1 = importedFileDarknessPatchIndex2 = -1;
        ArrayList<MapArea> areasList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("mainLayerStart")) {
                int l1StartX, l1StartY, l1EndX, l1EndY;
                int fg2StartX, fg2StartY, bg2StartX, bg2StartY;
                int l1ParallaxX, l1ParallaxY, l2ParallaxX, l2ParallaxY;
                int l1AutoscrollX, l1AutoscrollY, l2AutoscrollX, l2AutoscrollY;
                int layerType;
                String defaultMusic;
                
                String[] split = line.substring(line.indexOf(" ")).split(",");
                l1StartX = StringHelpers.getValueInt(split[0]);
                l1StartY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "mainLayerEnd", areasList.size()+1);
                l1EndX = StringHelpers.getValueInt(split[0]);
                l1EndY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "scndLayerFgndStart", areasList.size()+1);
                fg2StartX = StringHelpers.getValueInt(split[0]);
                fg2StartY = StringHelpers.getValueInt(split[1]);
                
                line = StringHelpers.trimAndRemoveComments(reader.readLine());
                if (line.startsWith("if (")) {
                    if (importedFileDarknessPatchIndex1 == -1) {
                        importedFileDarknessPatchIndex1 = areasList.size();
                        Console.logger().warning("Darkness patch found at item: " + importedFileDarknessPatchIndex1 + ". Patch will be reapplied on export.");
                    } else {
                        importedFileDarknessPatchIndex2 = areasList.size();
                    }
                    reader.readLine();
                    reader.readLine();
                    line = StringHelpers.trimAndRemoveComments(reader.readLine());
                }
                if (!line.startsWith("scndLayerBgndStart")) {
                    throw new AsmException("ERROR Map data line cannot be parsed. Cannot find 'scndLayerBgndStart' in line " + areasList.size()+1);
                }
                split = line.substring(line.indexOf(" ")+1).trim().split(",");
                bg2StartX = StringHelpers.getValueInt(split[0]);
                bg2StartY = StringHelpers.getValueInt(split[1]);
                if (importedFileDarknessPatchIndex1 == areasList.size() || importedFileDarknessPatchIndex2 == areasList.size()) {
                    reader.readLine();
                }
                
                split = MapStringHelpers.getNextLineMulti(reader, "mainLayerParallax", areasList.size()+1);
                l1ParallaxX = StringHelpers.getValueInt(split[0]);
                l1ParallaxY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "scndLayerParallax", areasList.size()+1);
                l2ParallaxX = StringHelpers.getValueInt(split[0]);
                l2ParallaxY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "mainLayerAutoscroll", areasList.size()+1);
                l1AutoscrollX = StringHelpers.getValueInt(split[0]);
                l1AutoscrollY = StringHelpers.getValueInt(split[1]);
                
                split = MapStringHelpers.getNextLineMulti(reader, "scndLayerAutoscroll", areasList.size()+1);
                l2AutoscrollX = StringHelpers.getValueInt(split[0]);
                l2AutoscrollY = StringHelpers.getValueInt(split[1]);
                
                layerType = StringHelpers.getValueInt(MapStringHelpers.getNextLineSingle(reader, "mainLayerType", areasList.size()+1));
                defaultMusic = MapStringHelpers.getNextLineSingle(reader, "areaDefaultMusic", areasList.size()+1);
                if (defaultMusic.startsWith("MUSIC_")) defaultMusic = defaultMusic.substring(6);
                defaultMusic = MapEnums.toEnumString(defaultMusic, pckg.getMusic());
                
                areasList.add(new MapArea(l1StartX, l1StartY, l1EndX, l1EndY, fg2StartX, fg2StartY, bg2StartX, bg2StartY, l1ParallaxX, l1ParallaxY, l2ParallaxX, l2ParallaxY, l1AutoscrollX, l1AutoscrollY, l2AutoscrollX, l2AutoscrollY, layerType, defaultMusic));
            }
        }
        MapArea[] areas = new MapArea[areasList.size()];
        areas = areasList.toArray(areas);
        return areas;
    }

    @Override
    protected String getHeaderName(MapArea[] item, MapEnums pckg) {
        return "Map areas";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapArea[] item, MapEnums pckg) throws IOException, AsmException {
        writer.write("\n");
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\t\t\t\t\tmainLayerStart      %3d, %3d\n", item[i].getLayer1StartX(), item[i].getLayer1StartY()));
            writer.write(String.format("\t\t\t\t\tmainLayerEnd        %3d, %3d\n", item[i].getLayer1EndX(), item[i].getLayer1EndY()));
            writer.write(String.format("\t\t\t\t\tscndLayerFgndStart  %3d, %3d\n", item[i].getForegroundLayer2StartX(), item[i].getForegroundLayer2StartY()));
            if (importedFileDarknessPatchIndex1 == i || importedFileDarknessPatchIndex2 == i) {
                //Darkness patch
                Console.logger().warning("Darkness patch being reapplied to item: " + i);
                writer.append("\t\t\t\tif (STANDARD_BUILD&NO_DARKNESS_IN_CAVES=1)\n");
                writer.append("\t\t\t\t\tscndLayerBgndStart    0,  33\n");
                writer.append("\t\t\t\telse\n");
                writer.append(String.format("\t\t\t\t\tscndLayerBgndStart  %3d, %3d\n", item[i].getBackgroundLayer2StartX(), item[i].getBackgroundLayer2StartY()));
                writer.append("\t\t\t\tendif\n\n");
            } else {
                writer.write(String.format("\t\t\t\t\tscndLayerBgndStart  %3d, %3d\n", item[i].getBackgroundLayer2StartX(), item[i].getBackgroundLayer2StartY()));
            }
            writer.write(String.format("\t\t\t\t\tmainLayerParallax   %3d, %3d\n", item[i].getLayer1ParallaxX(), item[i].getLayer1ParallaxY()));
            writer.write(String.format("\t\t\t\t\tscndLayerParallax   %3d, %3d\n", item[i].getLayer2ParallaxX(), item[i].getLayer2ParallaxY()));
            writer.write(String.format("\t\t\t\t\tmainLayerAutoscroll %3d, %3d\n", item[i].getLayer1AutoscrollX(), item[i].getLayer1AutoscrollY()));
            writer.write(String.format("\t\t\t\t\tscndLayerAutoscroll %3d, %3d\n", item[i].getLayer2AutoscrollX(), item[i].getLayer2AutoscrollY()));
            writer.write(String.format("\t\t\t\t\tmainLayerType       %3d\n", item[i].getLayerType()));
            writer.write(String.format("\t\t\t\t\tareaDefaultMusic    MUSIC_%s\n", item[i].getDefaultMusic()));
            writer.write("\n");
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
