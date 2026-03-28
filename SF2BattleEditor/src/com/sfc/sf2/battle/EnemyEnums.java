/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import com.sfc.sf2.core.AbstractEnums;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author TiMMy
 */
public class EnemyEnums extends AbstractEnums {
        
    private final LinkedHashMap<String, Integer> enemies;
    private final LinkedHashMap<String, Integer> mapSprites;
    private final LinkedHashMap<String, Integer> aiCommandSets;
    private final LinkedHashMap<String, Integer> aiOrders;
    private final LinkedHashMap<String, Integer> spawnParams;
    private final LinkedHashMap<String, Integer> items;
    private final LinkedHashMap<String, Integer> itemFlags;
    
    private final int specialSpritesStart;
    private final int specialSpritesEnd;

    public EnemyEnums(LinkedHashMap<String, Integer> enemies, LinkedHashMap<String, Integer> mapSprites, LinkedHashMap<String, Integer> aiCommandSets, LinkedHashMap<String, Integer> aiOrders,
                LinkedHashMap<String, Integer> spawnParams, LinkedHashMap<String, Integer> items, LinkedHashMap<String, Integer> itemFlags, int specialSpritesStart, int specialSpritesEnd) {
        this.enemies = enemies;
        this.mapSprites = mapSprites;
        this.aiCommandSets = aiCommandSets;
        this.aiOrders = aiOrders;
        this.spawnParams = spawnParams;
        this.items = items;
        this.itemFlags = itemFlags;
        this.specialSpritesStart = specialSpritesStart;
        this.specialSpritesEnd = specialSpritesEnd;
    }

    public LinkedHashMap<String, Integer> getEnemies() {
        return enemies;
    }

    public LinkedHashMap<String, Integer> getMapSprites() {
        return mapSprites;
    }
    
    public LinkedHashMap<String, Integer> getCommandSets() {
        return aiCommandSets;
    }

    public LinkedHashMap<String, Integer> getOrders() {
        return aiOrders;
    }

    public LinkedHashMap<String, Integer> getSpawnParams() {
        return spawnParams;
    }

    public LinkedHashMap<String, Integer> getItems() {
        return items;
    }

    public LinkedHashMap<String, Integer> getItemFlags() {
        return itemFlags;
    }

    public int getSpecialSpritesStart() {
        return specialSpritesStart;
    }

    public int getSpecialSpritesEnd() {
        return specialSpritesEnd;
    }
    
    //Helper functions
    //Items
    public static String itemNumToItemString(short data, Map<String, Integer> items) {
        data = (short)(data&0xFFF);
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            if ((data == entry.getValue())){
                return entry.getKey();
            }
        }
        return Short.toString(data);    //Fallback
    }
    
    public static String itemNumToItemFlagsString(short data, Map<String, Integer> items) {
        if (data >= 0x1000) {  //If flags
            String s = "";
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                if (entry.getValue() > 0x1000 && (data&entry.getValue()) != 0){
                    if (s.length() > 0) s += "|";
                    s += entry.getKey();
                    data = (short)(data&(~entry.getValue()));
                }
            }
            return s;
        } else {
            return null;
        }
    }
    
    public static short itemStringToNum(String item, Map<String, Integer> items) {
        if (item == null || item.length() == 0) return 0;
        String[] split = item.split("\\|");
        short value = 0;
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
            
            if (items.containsKey(split[i])) {
                    value += items.get(split[i]);
            }
        }
        return value;
    }
    
    //Orders
    public static String stringToAiOrderString(String data, Map<String, Integer> orders) {
        try {
            byte number = Byte.parseByte(data);
            return aiOrderNumToString(number, orders);
        } catch (NumberFormatException ex) {
            //Not a number
            return toEnumString(data, orders);
        }
    }
    
    public static String aiOrderNumToString(byte data, Map<String, Integer> orders) {        
        byte value = data;
        
        if (data != 0xFF) {
            value = (byte)(data&0xF0);
        }
        
        for (Map.Entry<String, Integer> entry : orders.entrySet()) {
            if ((value == entry.getValue())) {
                return entry.getKey();
            }
        }
        return Byte.toString(data); //Fallback
    }
    
    public static byte aiOrderStringToNum(String data, Map<String, Integer> orders) {
        String[] split = data.split("\\|");
        if (split.length < 2)
            return toEnumByte(split[0], orders);
        
        byte value = (byte)(Byte.valueOf(split[1])&0x0F);
        if (orders.containsKey(split[0])){
            if (orders.get(split[0]) == 0xFF)
                value = (byte)0xFF;
            else
                value += (byte)(orders.get(split[0])&0xF0);
        }
        return value;
    }
}
