/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.io;

import com.sfc.sf2.core.gui.controls.Console;
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
public class TextProcessor extends AbstractTextProcessor<String[], TextPackage> {
    public enum TextID {
        DECIMAL,
        HEX,
        BOTH,
    }
    
    @Override
    protected String[] parseTextData(BufferedReader reader, TextPackage pckg) throws IOException, TextFileException {
        ArrayList<String> linesList = new ArrayList<>();
        String line;
        int idIndex = -1;
        while ((line = reader.readLine()) != null) {
            //Ignore commented lines
            if (line.charAt(0) == ';') {
                continue;
            }
            //Identify file format
            if (idIndex == -1) {
                if (line == null) {
                    Console.logger().severe("ERROR: Could not parse text data format. Defaulting to original format to attempt to parse");
                    idIndex = 5;
                } else {
                    if (line.charAt(9) == '=') {
                        idIndex = 10;
                    } else {
                        idIndex = 5;
                    }
                }
            }
            //remove the IDs
            line = line.substring(idIndex);
            linesList.add(line);
            //Console.logger().finest("Line "+linesList.size()+" : "+line);
        }
        String[] lines = new String[linesList.size()];
        return linesList.toArray(lines);
    }

    @Override
    protected void packageTextData(FileWriter writer, String[] item, TextPackage pckg) throws IOException, TextFileException {
        String header = null;
        String format = null;
        boolean both = false;
        switch (pckg.id()) {
            case DECIMAL:
                header = ";Dec| Lines\n";
                format = "%04d=%s\n";
                both = false;
                break;
            case HEX:
                header = ";Hex| Lines\n";
                format = "%04X=%s\n";
                both = false;
                break;
            case BOTH:
                header = ";Dec| Hex| Lines\n";
                format = "%04d=%04X=%s\n";
                both = true;
                break;
        }
        if (pckg.header()) {
            writer.write(header);
        }
        for (int i = 0; i < item.length; i++) {
            if (both) {
                writer.write(String.format(format, i, i, item[i]));
                //Console.logger().finest("Line "+i+" : "+item[i]);
            } else {
                writer.write(String.format(format, i, item[i]));
                //Console.logger().finest("Line "+i+" : "+item[i]);
            }
        }  
    }
}
