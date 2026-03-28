/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

import com.sfc.sf2.core.AbstractEnums;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;


/**
 *
 * @author TiMMy
 */
public abstract class SF2EnumsAsmProcessor<TData extends AbstractEnums> extends AbstractAsmProcessor<TData, EmptyPackage> {
    
    private final String[] categories;
    
    public SF2EnumsAsmProcessor(String[] categories) {
        this.categories = categories;
    }

    @Override
    protected TData parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        LinkedHashMap<String, Integer>[] datasets = new LinkedHashMap[categories.length];
        String line;
        int categoriesRead = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("; enum")) {
                line = line.substring(7).trim();
                for (int i = 0; i < categories.length; i++) {
                    if (line.equals(categories[i])) {
                        //Found a category
                        if (datasets[i] == null) {
                            datasets[i] = new LinkedHashMap<>();
                        }
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("; ---")) {
                                categoriesRead++;
                                break;  //Category end
                            } else if (line.length() > 0 && line.charAt(0) != ';' && line.charAt(0) != ' ') {
                                line = StringHelpers.trimAndRemoveComments(line);
                                parseLine(i, line, datasets[i]);
                            }
                        }
                    }
                }
                if (categoriesRead >= categories.length) {
                    break;  //No need to read rest of the file
                }
            }
        }
        return createEnumsData(datasets);
    }
    
    protected abstract void parseLine(int categoryIndex, String line, LinkedHashMap<String, Integer> asmData);
    protected abstract TData createEnumsData(LinkedHashMap<String, Integer>[] dataSets);

    protected void defaultParseLine(int categoryIndex, String line, LinkedHashMap<String, Integer> asmData) {
        int colonIndex = line.indexOf(':');
        String id = line.substring(line.indexOf("_")+1, colonIndex);
        int value = StringHelpers.getValueInt(line.substring(colonIndex+1).replace("equ", ""));
        if (!asmData.containsKey(id)) {
            asmData.put(id, value);
        }
    }
    
    @Override
    protected String getHeaderName(TData item, EmptyPackage pckg) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void packageAsmData(FileWriter writer, TData item, EmptyPackage pckg) throws IOException, AsmException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
