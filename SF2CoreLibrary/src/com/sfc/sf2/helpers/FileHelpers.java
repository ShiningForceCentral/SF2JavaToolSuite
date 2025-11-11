/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.core.io.FileFormat;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class FileHelpers {

    public static File getTempFile(String prefix) throws IOException {
        return File.createTempFile(prefix, Long.toString(System.currentTimeMillis()), Path.of(System.getenv("APPDATA")).resolve("SF2").toFile());
    }
    
    public static File[] findAllFilesInDirectory(Path folder, String filePrefix, FileFormat fileFormat) {
        return findAllFilesInDirectory(folder.toFile(), filePrefix, fileFormat);
    }
    
    public static File[] findAllFilesInDirectory(File folder, String filePrefix, FileFormat fileFormat) {
        return folder.listFiles(fileFormat.getFileFilter(filePrefix));
    }
    
    public static String getExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        } else {
            return name.substring(dotIndex+1);
        }
    }
    
    public static int getNumberFromFileName(File file) {
        return StringHelpers.getNumberFromString(file.getName());
    }
}
