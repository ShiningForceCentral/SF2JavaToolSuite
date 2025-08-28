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
 */
public abstract class AbstractTextProcessor<TType extends Object> {
    
    public TType importTextData(Path filePath) throws IOException, TextFileException, FileNotFoundException {
        Console.logger().finest("ENTERING importTextData : " + filePath);
        File textFile = filePath.toFile();
        if (!textFile.exists()) {
            throw new FileNotFoundException("Text file not found : " + filePath);
        }
        BufferedReader reader = new BufferedReader(new FileReader(textFile));
        TType item = parseTextData(reader);
        reader.close();
        Console.logger().finest("EXITING importTextData");
        return item;
    }
    
    protected abstract TType parseTextData(BufferedReader reader) throws IOException, TextFileException;
    
    public void exportTextData(Path filePath, TType item) throws IOException, TextFileException {
        Console.logger().finest("ENTERING exportTextData : " + filePath);
        File textFile = filePath.toFile();
        FileWriter writer = new FileWriter(textFile, false);
        packageTextData(writer, item);
        writer.close();
        Console.logger().finest("EXITING exportTextData");
    }
    
    protected abstract void packageTextData(FileWriter writer, TType item) throws IOException, TextFileException;
}
