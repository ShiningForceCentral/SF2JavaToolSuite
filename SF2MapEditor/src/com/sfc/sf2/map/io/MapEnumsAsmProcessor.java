/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.io;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.SF2EnumsAsmProcessor;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.MapEnums;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class MapEnumsAsmProcessor extends SF2EnumsAsmProcessor<MapEnums> {

    private int itemNothingValue = 0;
    boolean foundItemNothing;

    public MapEnumsAsmProcessor() {
        super(new String[] { "Items (bitfield)", "Maps", "Music" });
    }

    @Override
    protected MapEnums parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        foundItemNothing = false;
        return super.parseAsmData(reader, pckg);
    }
    
    @Override
    protected void parseLine(int categoryIndex, String line, LinkedHashMap<String, Integer> asmData) {
        switch (categoryIndex) {
            case 0:
                if (foundItemNothing) return;
                if (line.startsWith("ITEM_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String item = line.substring(0, line.indexOf(":"));
                    int value = 0;
                    if (item.equals("NOTHING")) {
                        foundItemNothing = true;
                        value = itemNothingValue;
                        asmData.put(item, value);
                        return;
                    } else {
                        int commentIndex = line.indexOf(";");
                        if (commentIndex > -1) {
                            line = line.substring(0, commentIndex);
                        }
                        value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                        asmData.put(item, value);
                    }
                } else if (line.startsWith("item")) {
                    //Get the ITEM_NOTHING
                    if (line.startsWith("itemNothing")) {
                        itemNothingValue = StringHelpers.getValueInt(line.substring(line.indexOf("=") + 1));
                    }
                }
                break;
            case 1:
                if (line.startsWith("MAP_")) {
                    line = line.substring(line.indexOf('_') + 1);
                    String map = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(map, value);
                }
            case 2:
                if (line.startsWith("MUSIC_")) {
                    line = line.substring(line.indexOf('_') + 1);
                    String music = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(music, value);
                }
        }
    }

    @Override
    protected MapEnums createEnumsData(LinkedHashMap<String, Integer>[] dataSets) {
        return new MapEnums(dataSets[0], dataSets[1], dataSets[2]);
    }
}
