/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

import com.sfc.sf2.core.gui.controls.Console;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 * @param <TType> The type of data being loaded
 */
public abstract class AbstractAsmProcessor<TType extends Object> {
    
    public TType importAsmData(Path filePath, TType item) throws DisassemblyException, IOException, FileNotFoundException {
        Console.logger().finest("ENTERING importAsmData : " + filePath);
        File asmFile = filePath.toFile();
        if (!asmFile.exists()) {
            throw new FileNotFoundException("ASM data file not found : " + filePath);
        }
        BufferedReader reader = new BufferedReader(new FileReader(asmFile));
        parseAsmData(reader, item);
        reader.close();
        Console.logger().finest("EXITING importAsmData");
        return item;
    }
    
    protected abstract void parseAsmData(BufferedReader reader, TType item) throws DisassemblyException, IOException;
    
    public void exportAsmData(Path filePath, TType item) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportAsmData : " + filePath);
        File asmFile = filePath.toFile();
        FileWriter writer = new FileWriter(asmFile, false);
        writeAsmHeader(writer, item);
        packageAsmData(writer, item);
        writer.close();
        Console.logger().finest("EXITING exportAsmData");
    }

    protected abstract void writeAsmHeader(FileWriter writer, TType item) throws DisassemblyException, IOException;
    protected abstract void packageAsmData(FileWriter writer, TType item) throws DisassemblyException, IOException;
}
