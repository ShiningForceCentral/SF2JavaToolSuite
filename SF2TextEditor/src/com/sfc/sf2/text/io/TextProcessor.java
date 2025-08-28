/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.io;

import com.sfc.sf2.core.io.AbstractTextProcessor;
import com.sfc.sf2.core.io.TextFileException;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author wiz
 */
public class TextProcessor extends AbstractTextProcessor<String[]> {
    
    @Override
    protected String[] parseTextData(BufferedReader reader) throws IOException, TextFileException {
        ArrayList<String> linesList = new ArrayList<>();
        String line;
        boolean newFormat = false;
        while ((line = reader.readLine()) != null) {
            //Ignore commented lines
            if (line.charAt(0) == ';') {
                newFormat = true;
                continue;
            }
            //remove the IDs
            int index = newFormat ? 8 : 4;
            if (line.charAt(index) != '=') {
                index = line.lastIndexOf('=');
            }
            line = line.substring(index+1);
            linesList.add(line);
            //Console.logger().finest("Line "+linesList.size()+" : "+line);
        }
        String[] lines = new String[linesList.size()];
        return linesList.toArray(lines);
    }

    @Override
    protected void packageTextData(FileWriter writer, String[] item) throws IOException, TextFileException {
        writer.write(";Dec| Hex| Lines\n");
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("%04d=%04X=%s\n", i, i, item[i]));
            //Console.logger().finest("Line "+i+" : "+item[i]);
        }  
    }
}
