/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.core.gui.controls.Console;

/**
 *
 * @author TiMMy
 */
public class StringHelpers {
    
    public static String trimAndRemoveComments(String line) {
        int commentIndex = line.lastIndexOf(';');
        if (commentIndex < 0) {
            return line.trim();
        } else {
            return line.substring(0, commentIndex).trim();
        }
    }
    public static String extractComment(String line) {
        int commentIndex = line.lastIndexOf(';');
        if (commentIndex < 0) {
            return null;
        } else {
            return line.substring(commentIndex+1).trim();
        }
    }
    
    public static int getNumberFromString(String s) {
        try {
            s = s.replaceAll("[^0-9]", "");
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            Console.logger().severe("String not formatted with numbers :" + s);
            return -1;
        }
    }
    
    public static byte getValueByte(String string) {
        return (byte)getValueInt(string);
    }
    
    public static int getValueInt(String string) {
        string = string.trim();
        string = string.replace("#", "");   //Remove symbol for immediate value
        if (string.charAt(0) == '$') {
            //Is Hex value
            return Integer.parseInt(string.substring(1), 16);
        } else {
            //Is Decimal value
            return Integer.parseInt(string);
        }
    }
}
