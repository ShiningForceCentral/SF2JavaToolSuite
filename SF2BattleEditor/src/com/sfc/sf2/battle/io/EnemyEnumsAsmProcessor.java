/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.io;

import com.sfc.sf2.battle.EnemyEnums;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.SF2EnumsAsmProcessor;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class EnemyEnumsAsmProcessor extends SF2EnumsAsmProcessor<EnemyEnums> {

    private int itemEquippedValue = 0;
    private int itemNothingValue = 0;
    
    private int specialSpritesStart = -1;
    private int specialSpritesEnd = -1;
    
    private boolean foundItemFlags;
    private LinkedHashMap<String, Integer> itemFlags;

    public EnemyEnumsAsmProcessor() {
        super(new String[] { "Enemies", "Mapsprites", "AiCommandsets", "AiOrders", "SpawnSettings", "Items (bitfield)", "Mapsprites_Properties" });
    }

    @Override
    protected EnemyEnums parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        foundItemFlags = false;
        itemFlags = new LinkedHashMap<>();
        return super.parseAsmData(reader, pckg);
    }
    
    @Override
    protected void parseLine(int categoryIndex, String line, LinkedHashMap<String, Integer> asmData) {
        switch (categoryIndex) {
            case 0:
                if (line.startsWith("ENEMY_")) {
                    line = line.substring(line.indexOf('_') + 1);
                    String name = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(name, value);
                }
                break;
            case 1:
                if (line.startsWith("MAPSPRITE_")) {
                    line = line.substring(line.indexOf('_') + 1);
                    String name = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(name, value);
                }
                break;
            case 2:
                if (line.startsWith("AICOMMANDSET_")) {
                    line = line.substring(line.indexOf('_') + 1);
                    String command = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(command, value);
                }
                break;
            case 3:
                if (line.startsWith("AIORDER_")) {
                    line = line.substring(line.indexOf('_') + 1);
                    String order = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(order, value);
                }
                break;
            case 4:
                if (line.startsWith("SPAWN_")) {
                    line = line.substring(line.indexOf('_') + 1);
                    String spawn = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(spawn, value);
                }
                break;
            case 5:
                if (line.startsWith("ITEM_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String item = line.substring(0, line.indexOf(":"));
                    int value = 0;
                    if (item.equals("NOTHING")) {
                        foundItemFlags = true;
                        value = itemNothingValue;
                        asmData.put(item, value);
                        itemFlags.put(item, value);
                    } else if (item.equals("EQUIPPED")) {
                        value = itemEquippedValue;
                        itemFlags.put(item, value);
                    } else {
                        int commentIndex = line.indexOf(";");
                        if (commentIndex > -1) {
                            line = line.substring(0, commentIndex);
                        }
                        value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                        if (foundItemFlags) {
                            itemFlags.put(item, value);
                        } else {
                            asmData.put(item, value);
                        }
                    }
                } else if (line.startsWith("item")) {
                    //Get the ITEM_NOTHING & ITEM_EQUIPPED
                    //TODO .asm will store as string so the value doesn't matter. .bin will store value which will only support expanded item values
                    if (line.startsWith("itemNothing")) {
                        itemNothingValue = StringHelpers.getValueInt(line.substring(line.indexOf("=") + 1));
                    } else if (line.startsWith("itemEquipped")) {
                        itemEquippedValue = StringHelpers.getValueInt(line.substring(line.indexOf("=") + 1));
                    }
                }
                break;
            case 6:
                if (line.startsWith("MAPSPRITES_SPECIALS_")) {
                    line = line.substring(line.lastIndexOf('_') + 1);
                    String id = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    if (id.equals("START")) {
                        specialSpritesStart = value;
                    } else if (id.equals("END")) {
                        specialSpritesEnd = value;
                    }
                }
                break;
        }
    }

    @Override
    protected EnemyEnums createEnumsData(LinkedHashMap<String, Integer>[] dataSets) {
        return new EnemyEnums(dataSets[0], dataSets[1], dataSets[2], dataSets[3], dataSets[4], dataSets[5], itemFlags, specialSpritesStart, specialSpritesEnd);
    }
}
