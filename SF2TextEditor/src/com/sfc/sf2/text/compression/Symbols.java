/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.compression;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author TiMMy
 */
public class Symbols {
    // <editor-fold defaultstate="collapsed" desc="TAGS_SYMBOL_TABLE">  
    private static final String[] TAGS_SYMBOL_TABLE = {
        "{DICT}",
        "{N}",
        "{D2}",
        "{#}",
        "{NAME}",
        "{LEADER}",
        "{ITEM}",
        "{SPELL}",
        "{CLASS}",
        "{W2}",
        "{D1}",
        "{D3}",
        "{W1}",
        "{CLEAR}",
        "{NAME",
        "{COLOR",
        "(START/EOL)",
        "\\UNUSED",
    };
    // </editor-fold>
    
    private static final int[] asciiToSymbolTable = new int[256];
    private static final int[] symbolToAsciiTable = new int[256];
    private static final String[] symbolToStringTable = new String[256];
    private static final HashMap<String, Integer> stringToSymbolMap = new HashMap<>();
    
    private static HashMap<Character, Character> replaceMap;
    private static HashMap<Character, Character> reverseReplaceMap;
    
    public static int charToSymbol(char c) {
        if (reverseReplaceMap != null && reverseReplaceMap.containsKey(c)) {
            return asciiToSymbol((int)reverseReplaceMap.get(c));
        } else {
            return asciiToSymbol((int)c);
        }
    }
    
    public static int asciiToSymbol(int ascii) {
        if (ascii < 0 || ascii >= asciiToSymbolTable.length) {
            return 0;
        } else {
            return asciiToSymbolTable[ascii];
        }
    }
    
    public static int symbolToAscii(int symbol) {
        if (symbol < 0 || symbol >= symbolToAsciiTable.length) {
            return 0;
        } else {
            return symbolToAsciiTable[symbol];
        }
    }
    
    public static String asciiToString(int ascii) {
        return symbolToString(asciiToSymbol(ascii));
    }
    
    public static String symbolToString(int symbol) {
        if (symbol < 0 || symbol >= symbolToStringTable.length) {
            return null;
        } else {
            return symbolToStringTable[symbol];
        }
    }
    
    public static int stringToSymbol(String string) {        
        if (stringToSymbolMap.containsKey(string)) {
            return stringToSymbolMap.get(string);
        } else {
            return 1;
        }
    }
    
    public static int stringToAscii(String string) {
        return symbolToAscii(stringToSymbol(string));
    }
    
    public static int count() {
        return symbolToStringTable.length;
    }
    
    public static void setImportedTable(int[] importedTable) {
        if (importedTable == null || importedTable.length == 0) return;
        for (int i = 0; i < asciiToSymbolTable.length; i++) {
            asciiToSymbolTable[i] = -1;
            symbolToAsciiTable[i] = -1;
            symbolToStringTable[i] = null;
        }
        stringToSymbolMap.clear();
        putSymbol(0, 0, "\\UNUSED");    //Hidden
        putSymbol(32, 1, " ");          //Space
        char c;
        for (int i = 0; i < importedTable.length; i++) {
            if (i >= 238) {
                putSymbol(i, i, TAGS_SYMBOL_TABLE[i-238]);  //For tags, use default table
            } else if (importedTable[i] != 1) {
                c = (char)i;
                if (replaceMap != null && replaceMap.containsKey(c)) {
                    c = replaceMap.get(c);
                }
                putSymbol(i, importedTable[i], Character.toString(c));
            }
        }
    }
    
    private static void putSymbol(int ascii, int symbol, String string) {
        asciiToSymbolTable[ascii] = symbol;
        if (!stringToSymbolMap.containsKey(string)) {
            stringToSymbolMap.put(string, symbol);
        }
        if (symbolToAsciiTable[symbol] == -1) {   //If data is not already set for this symbol
            symbolToAsciiTable[symbol] = ascii;
            symbolToStringTable[symbol] = string;
        }
    }
    
    public static void setReplaceMap(HashMap<Character, Character> replaceMap) {
        Symbols.replaceMap = replaceMap;
        reverseReplaceMap = new HashMap<>(replaceMap.size());
        for (Map.Entry<Character, Character> entry : replaceMap.entrySet()) {
            reverseReplaceMap.put(entry.getValue(), entry.getKey());
        }
    }
}
