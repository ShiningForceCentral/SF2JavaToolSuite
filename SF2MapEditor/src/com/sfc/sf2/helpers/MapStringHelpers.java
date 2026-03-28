/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.core.io.asm.AsmException;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class MapStringHelpers {
    
    public static String getNextLineSingle(BufferedReader reader, String identifier, int count) throws IOException, AsmException {
        String line = reader.readLine();
        line = StringHelpers.trimAndRemoveComments(line);
        if (!line.startsWith(identifier)) {
            throw new AsmException("ERROR Map data line cannot be parsed. Cannot find '" + identifier + "' in line " + count);
        }
        return line.substring(line.indexOf(" ")+1).trim();
    }
    
    public static String[] getNextLineMulti(BufferedReader reader, String identifier, int count) throws IOException, AsmException {
        return getNextLineSingle(reader, identifier, count).split(",");
    }
}
