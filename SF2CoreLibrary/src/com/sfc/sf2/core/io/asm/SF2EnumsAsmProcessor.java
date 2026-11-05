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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;


/**
 *
 * @author TiMMy
 */
public abstract class SF2EnumsAsmProcessor<TData extends AbstractEnums> extends AbstractAsmProcessor<TData, EmptyPackage> {
    
    private final String[] enumsCategories;
    private final Path[] separateEnumsFilePaths;
    
    private LinkedHashMap<String, Integer>[] datasets;
    private int categoriesRead = 0;
    
    /**
     * Imports defined categories from ./disasm/SF2Enums.asm.<br>
     * If category does not exist in SF2Enums.asm then system will search in ./disasm/enums/ folder for [category]-standard.asm or [category].asm
     * @param categories Defines the 'header' for each category of data to be parsed. The 'header' is in the form of "; enum [category]"
     */
    public SF2EnumsAsmProcessor(String[] categories) {
        this.enumsCategories = categories;
        separateEnumsFilePaths = null;
    }
    
    /**
     * Imports defined categories from ./disasm/SF2Enums.asm or from individual files in ./disasm/enums/ folder
     * @param categories Defines the 'header' for each category of data to be parsed. The 'header' is in the form of "; enum [category]"
     * @param separateFilePaths For each path that is not null, imports the enums data from that file, instead of inside SF2Enums.asm
     */
    public SF2EnumsAsmProcessor(String[] categories, Path[] separateFilePaths) {
        this.enumsCategories = categories;
        this.separateEnumsFilePaths = separateFilePaths;
    }
    
    /**
     * Imports defined categories from individual files in ./disasm/enums/ folder
     * @param separateFilePaths For each path that is not null, imports the enums data from that file, instead of inside SF2Enums.asm
     */
    public SF2EnumsAsmProcessor(Path[] separateFilePaths) {
        this.enumsCategories = null;
        this.separateEnumsFilePaths = separateFilePaths;
    }

    @Override
    public TData importAsmData(Path filePath, EmptyPackage pckg) throws IOException, AsmException, FileNotFoundException {
        datasets = new LinkedHashMap[enumsCategories.length];
        categoriesRead = 0;
        //Import from separate files
        Path enumsFolder = filePath.getParent().resolve("enums");
        for (int i = 0; i < enumsCategories.length; i++) {
            Path separateFile;
            if (separateEnumsFilePaths == null || separateEnumsFilePaths[i] == null) {
                separateFile = enumsFolder.resolve(String.format("%s-standard.asm", enumsCategories[i].toLowerCase()));
                if (!separateFile.toFile().exists()) {
                    //No standard file so try master
                    separateFile = enumsFolder.resolve(String.format("%s.asm", enumsCategories[i]).toLowerCase());
                }
            } else {
                separateFile = separateEnumsFilePaths[i];
            }
            if (separateFile != null && separateFile.toFile().exists()) {
                //Found a separate file so import from that
                super.importAsmData(separateFile, pckg);
            }
        }
        if (categoriesRead < enumsCategories.length) {
            //Import from SF2Enums (if/when necessary)
            super.importAsmData(filePath, pckg);
        }
        
        TData enums = createEnumsData(datasets);
        datasets = null;
        return enums;
    }

    @Override
    protected TData parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("; enum")) {
                line = line.substring(7).trim();
                for (int i = 0; i < enumsCategories.length; i++) {
                    if (line.equals(enumsCategories[i])) {
                        parseCategory(reader, i);
                        categoriesRead++;
                        break;
                    }
                }
                if (categoriesRead >= enumsCategories.length) {
                    break; //No need to read rest of the file
                }
            }
        }
        return null;
    }
    
    private void parseCategory(BufferedReader reader, int categoryIndex) throws IOException {
        if (datasets[categoryIndex] == null) {
            datasets[categoryIndex] = new LinkedHashMap<>();
        }
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("; ---")) {
                return;  //Category end
            } else if (line.length() > 0 && line.charAt(0) != ';' && line.charAt(0) != ' ') {
                line = StringHelpers.trimAndRemoveComments(line);
                parseLine(categoryIndex, line, datasets[categoryIndex]);
            }
        }
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
