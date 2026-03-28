/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core;

import com.sfc.sf2.helpers.StringHelpers;
import java.util.Map;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractEnums {
    
    /**
     * Gets the enum id from a string of a number (e.g. "3" or "$10")
     */
    public static String toEnumString(String data, Map<String, Integer> enumData) {
        try {
            int number = StringHelpers.getValueInt(data);
            return toEnumString(number, enumData);
        }
        catch (NumberFormatException ex) {
            //Not a number
            return data;
        }
    }
    
    /**
     * Gets the enum id from a byte value
     */
    public static String toEnumString(byte data, Map<String, Integer> enumData) {
        return toEnumString((int)data, enumData);
    }
    
    /**
     * Gets the enum id from a short value
     */
    public static String toEnumString(short data, Map<String, Integer> enumData) {
        return toEnumString((int)data, enumData);
    }
    
    /**
     * Gets the enum id from a value
     */
    public static String toEnumString(int data, Map<String, Integer> enumData) {
        int number = data;
        for (Map.Entry<String, Integer> entry : enumData.entrySet()) {
            if (number == entry.getValue())
                return entry.getKey();
        }

        return String.valueOf(data);    //Fallback
    }
    
    /**
     * Gets the byte value of an enum from the enum id
     */
    public static byte toEnumByte(String data, Map<String, Integer> enumData) {
        return (byte)(toEnumInt(data, enumData)&0xFF);
    }
    
    /**
     * Gets the short value of an enum from the enum id
     */
    public static short toEnumShort(String data, Map<String, Integer> enumData) {
        return (short)(toEnumInt(data, enumData)&0xFFFF);
    }
    
    /**
     * Gets the value of an enum from the enum id
     */
    public static int toEnumInt(String data, Map<String, Integer> enumData) {
        try {
            int number = Integer.parseInt(data);
            return number;
        } catch (NumberFormatException ex) {
            //Not a number
        }
        
        String[] split = data.split("\\|");
        int value = -1;
        for (int i = 0; i < split.length; i++) {
            if (enumData.containsKey(split[i]))
                value = (enumData.get(split[i]));
        }
        
        return value;
    }
}
