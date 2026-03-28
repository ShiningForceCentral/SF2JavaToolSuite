/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class MapEntriesAsmProcessor extends AbstractAsmProcessor<MapEntryData[], EmptyPackage> {

    @Override
    protected MapEntryData[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        HashMap<Integer, String[]> mapComponents = new HashMap<>();
        HashMap<String, String> mapPaths = new HashMap<>();
        String line;
        int numIndex = 6;
        int largest = 0;
        int currentMapId = -1;
        //Read all data
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == ';') continue;
            if (line.startsWith("pt_MapData")) {
                line = line.substring(11);
            }
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("dc.l")) {
                //found a map component
                int startIndex = line.indexOf('.')+3;
                line = line.substring(startIndex);
                if (line.charAt(0) == '$') continue; //map item is empty (i.e. has no animation)
                int mapNum;
                if (currentMapId == -1) {
                    mapNum = StringHelpers.getNumberFromString(line.substring(3));
                } else {
                    mapNum = currentMapId;
                }
                if (!mapComponents.containsKey(mapNum)) {
                    mapComponents.put(mapNum, new String[20]);  //20 is arbitrary number larger than current number of map components
                    if (mapNum > largest) largest = mapNum;
                }
                String[] existingComponents = mapComponents.get(mapNum);
                for (int i = 0; i < existingComponents.length; i++) {
                    if (existingComponents[i] == null) {
                        existingComponents[i] = line;
                        break;
                    }
                }
            } else if (line.startsWith("Map")) {
                //Is path data
                int colonIndex = line.indexOf(':');
                int includeIndex = line.indexOf("include")+7;
                if (includeIndex <= 7) includeIndex = line.indexOf("incbin")+6;
                String pathID = line.substring(0, colonIndex).trim();
                String Path = line.substring(includeIndex).trim().replace("\"", "");
                mapPaths.put(pathID, Path);
                if (colonIndex < 7) {
                    //First identifier before map components
                    currentMapId = StringHelpers.getNumberFromString(line.substring(3, numIndex));
                }
            }
        }
        if (largest == 0) {
            throw new AsmException("Map entries data was not found.");
        }
        //process data
        MapEntryData[] maps = new MapEntryData[largest+1];
        MapEntryData currentMap;
        for (int m = 0; m < maps.length; m++) {
            String[] components = mapComponents.get(m);
            if (components == null) {
                Console.logger().warning("WARNING Map '" + m + "' undefined. Skipping.");
                continue;
            }
            currentMap = new MapEntryData(m);
            maps[m] = currentMap;
            for (int c = 0; c < components.length; c++) {
                if (components[c] == null) {
                    break;   //End of this map
                } else {
                    String path = null;
                    if (mapPaths.containsKey(components[c])) {
                        path = mapPaths.get(components[c]);
                        parseMapComponentData(currentMap, components[c], path);
                    } else {
                        Console.logger().warning("Path not found for map component '" + components[c] + "'. Skipping.");
                    }
                }
            }
        }
        return maps;
    }
    
    private void parseMapComponentData(MapEntryData currentMap, String component, String path) {
        String identifier;
        int underscoreIndex = component.indexOf('_');
        if (underscoreIndex == -1) {
            identifier = "Tilesets";
        } else {
            identifier = component.substring(underscoreIndex+1);
        }
        switch (identifier) {
            case "Tilesets":
                currentMap.setTilesetsPath(path);
                break;
            case "Blocks":
                currentMap.setBlocksPath(path);
                break;
            case "Layout":
                currentMap.setLayoutPath(path);
                break;
            case "Areas":
                currentMap.setAreasPath(path);
                break;
            case "FlagEvents":
                currentMap.setFlagEventsPath(path);
                break;
            case "StepEvents":
                currentMap.setStepEventsPath(path);
                break;
            case "RoofEvents":
                currentMap.setRoofEventsPath(path);
                break;
            case "WarpEvents":
                currentMap.setWarpEventsPath(path);
                break;
            case "ChestItems":
                currentMap.setChestItemsPath(path);
                break;
            case "OtherItems":
                currentMap.setOtherItemsPath(path);
                break;
            case "Animations":
                currentMap.setAnimationsPath(path);
                break;
            default:
                Console.logger().warning("WARNING Identifier '" + identifier + "' unknown in map entries.asm.");
                break;
        }
    }

    @Override
    protected String getHeaderName(MapEntryData[] item, EmptyPackage pckg) {
        return "Map entries";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapEntryData[] item, EmptyPackage pckg) throws IOException, AsmException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
