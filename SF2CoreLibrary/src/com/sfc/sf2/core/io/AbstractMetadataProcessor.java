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
public abstract class AbstractMetadataProcessor<TType extends Object> {
    
    public TType importMetadata(Path filePath, TType item) throws MetadataException, IOException, FileNotFoundException {
        Console.logger().finest("ENTERING importMetadata : " + filePath);
        File metaFile = filePath.toFile();
        if (!metaFile.exists()) {
            throw new FileNotFoundException("Metadata file not found : " + filePath);
        }
        BufferedReader reader = new BufferedReader(new FileReader(metaFile));
        parseMetaData(reader, item);
        reader.close();
        Console.logger().finest("EXITING importMetadata");
        return item;
    }
    
    protected abstract void parseMetaData(BufferedReader reader, TType item) throws IOException, MetadataException;
    
    public void exportMetadata(Path filePath, TType item) throws IOException, MetadataException {
        Console.logger().finest("ENTERING exportMetadata : " + filePath);
        File metaFile = filePath.toFile();
        FileWriter writer = new FileWriter(metaFile, false);
        packageMetaData(writer, item);
        writer.close();
        Console.logger().finest("EXITING exportMetadata");
    }

    protected abstract void packageMetaData(FileWriter writer, TType item) throws IOException, MetadataException;
}
