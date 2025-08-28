/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.text.io;

import com.sfc.sf2.core.io.AbstractMetadataProcessor;
import com.sfc.sf2.core.io.MetadataException;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class AsciiReplaceProcessor extends AbstractMetadataProcessor<HashMap<Character, Character>> {

    @Override
    protected void parseMetaData(BufferedReader reader, HashMap<Character, Character> item) throws IOException, MetadataException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == ';') continue;
            String[] split = line.split(" ");
            char key = parseChar(split[0]);
            char val = parseChar(split[1]);
            item.put(key, val);
        }
    }
    
    char parseChar(String string) {
        if (string.length() == 1) {
            return string.charAt(0);
        } else {
            return (char)Integer.parseInt(string.substring(1), 16);
        }
    }

    @Override
    protected void packageMetaData(FileWriter writer, HashMap<Character, Character> item) throws IOException, MetadataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
