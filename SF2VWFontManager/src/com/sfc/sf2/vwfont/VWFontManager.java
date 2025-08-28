/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.vwfont.io.FontSymbolPackage;
import com.sfc.sf2.vwfont.io.VWFontDisassemblyProcessor;
import com.sfc.sf2.vwfont.io.VWFontRawImageProcessor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author wiz
 */
public class VWFontManager extends AbstractManager {
    private final VWFontDisassemblyProcessor fontDisassemblyProcessor = new VWFontDisassemblyProcessor();
    private final VWFontRawImageProcessor fontRawImageProcessor = new VWFontRawImageProcessor();
    
    public FontSymbol[] symbols;

    @Override
    public void clearData() {
        if (symbols != null) {
            for (int i = 0; i < symbols.length; i++) {
                symbols[i].clearIndexedColorImage();
            }
            symbols = null;
        }
    }
       
    public FontSymbol[] importDisassembly(Path fontFilePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        symbols = fontDisassemblyProcessor.importDisassembly(fontFilePath, null);
        Console.logger().info("VW fonts successfully imported from : " + fontFilePath);
        Console.logger().finest("EXITING importDisassembly");
        return symbols;
    }
    
    public void exportDisassembly(Path fontFilePath, FontSymbol[] fontSymbols) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.symbols = fontSymbols;
        fontDisassemblyProcessor.exportDisassembly(fontFilePath, symbols, null);
        Console.logger().info("VW fonts successfully exported to : " + fontFilePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public FontSymbol[] importAllImages(Path basePath, FileFormat format) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importAllImages");
        File[] files = FileHelpers.findAllFilesInDirectory(basePath, "symbol", AbstractRawImageProcessor.GetFileExtensionString(format));
        Console.logger().info(files.length + " font symbol images found.");
        ArrayList<FontSymbol> symbolsList = new ArrayList<>();
        int failedToLoad = 0;
        //Symbol indexes are offset by 1 because engine is expecting a hidden value at 000. Old format exported to 000 so check for old format by finding 000
        boolean oldFormat = false;
        Path filePath = basePath.resolve("symbol000" + AbstractRawImageProcessor.GetFileExtensionString(format));
        File index000 = filePath.toFile();
        if (index000.exists()) {
            oldFormat = true;
        }
        //Now load
        for (File file : files) {
            Path symbolPath = file.toPath();
            try {
                int index = FileHelpers.getNumberFromFileName(file);
                if (!oldFormat) index -= 1;  //Offset by 1 because engine is expecting a hidden value at 000
                FontSymbol symbol = fontRawImageProcessor.importRawImage(symbolPath, new FontSymbolPackage(index));
                symbolsList.add(symbol);
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Font symbol could not be imported : " + symbolPath + " : " + e);
            }
        }
        symbols = new FontSymbol[symbolsList.size()];
        symbols = symbolsList.toArray(symbols);
        Console.logger().info(symbols.length + " font symbols successfully imported from images : " + basePath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " font symbols failed to import. See logs above");
        }
        Console.logger().finest("EXITING importAllImages");
        return symbols;
    }
    
    public void exportAllImages(Path basePath, FontSymbol[] symbols, FileFormat format) {
        Console.logger().finest("ENTERING exportAllImages");
        this.symbols = symbols;
        int failedToSave = 0;
        Path filePath = null;
        int fileCount = 0;
        //Remove index 000 (for preexisting exports)
        filePath = basePath.resolve("symbol000" + AbstractRawImageProcessor.GetFileExtensionString(format));
        File index000 = filePath.toFile();
        if (index000.exists()) {
            index000.delete();
        }
        //Now save
        for (FontSymbol symbol : symbols) {
            try {
                int id = symbol.getId()+1;  //Offset by 1 because engine is expecting a hidden value at 000
                filePath = basePath.resolve(String.format("symbol%03d%s", id, AbstractRawImageProcessor.GetFileExtensionString(format)));
                fontRawImageProcessor.exportRawImage(filePath, symbol, null);
                fileCount++;
            }catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Font symbol could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((fileCount - failedToSave) + " font symbols successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " font symbols failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportAllImages");    
    }
    
    public FontSymbol[] getFontSymbols() {
        return symbols;
    }
}
