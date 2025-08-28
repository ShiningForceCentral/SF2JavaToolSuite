/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.BinaryDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.core.io.TextFileException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.text.compression.TextDecoder;
import com.sfc.sf2.text.compression.TextEncoder;
import com.sfc.sf2.text.io.AsciiReplaceProcessor;
import com.sfc.sf2.text.io.asm.AsciiTableAsmProcessor;
import com.sfc.sf2.text.io.AsmManager;
import com.sfc.sf2.text.io.TextProcessor;
import com.sfc.sf2.text.io.asm.AllyNamesAsmProcessor;
import com.sfc.sf2.vwfont.FontSymbol;
import com.sfc.sf2.vwfont.VWFontManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author wiz
 */
public class TextManager extends AbstractManager {
    private static final String HUFFMANTREEOFFSETS_FILENAME = "huffmantreeoffsets.bin";
    private static final String HUFFMANTREES_FILENAME = "huffmantrees.bin";
    private static final String TEXTBANK_FILEPREFIX = "textbank";  
    
    private final TilesetManager tilesetManager = new TilesetManager();
    private final VWFontManager fontManager = new VWFontManager();
    private final BinaryDisassemblyProcessor binaryDisassemblyProcessor = new BinaryDisassemblyProcessor();
    private final AsciiTableAsmProcessor asciiAsmProcessor = new AsciiTableAsmProcessor();
    private final AllyNamesAsmProcessor allyNamesAsmProcessor = new AllyNamesAsmProcessor();
    private final AsciiReplaceProcessor asciiReplaceProcessor = new AsciiReplaceProcessor();
    private final TextProcessor textProcessor = new TextProcessor();
    
    private String[] gamescript;
    private Tileset baseTiles;
    private FontSymbol[] fontSymbols;
    private int[] asciiToSymbolMap;
    private HashMap<Character, Character> asciiReplaceMap;
    private String[] allyNames;

    @Override
    public void clearData() {
        tilesetManager.clearData();
        fontManager.clearData();
        
        gamescript = null;
        if (baseTiles != null) {
            baseTiles.clearIndexedColorImage(true);
        }
        if (fontSymbols != null) {
            for (int i = 0; i < fontSymbols.length; i++) {
                fontSymbols[i].clearIndexedColorImage();
            }
        }
        if (asciiReplaceMap != null) {
            asciiReplaceMap.clear();
            asciiReplaceMap = null;
        }
        asciiToSymbolMap = null;
        allyNames = null;
    }
       
    public String[] importDisassembly(Path basePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        //Load huffman trees
        Path offsetsPath = basePath.resolve(HUFFMANTREEOFFSETS_FILENAME);
        Path treesPath = basePath.resolve(HUFFMANTREES_FILENAME);
        byte[] offsets = binaryDisassemblyProcessor.importDisassembly(offsetsPath, null);
        byte[] trees = binaryDisassemblyProcessor.importDisassembly(treesPath, null);
        TextDecoder decoder = new TextDecoder();
        decoder.parseOffsets(offsets);
        decoder.parseTrees(trees);
        //Load banks
        File[] files = FileHelpers.findAllFilesInDirectory(basePath, TEXTBANK_FILEPREFIX, ".bin");
        Console.logger().info(files.length + " Textbanks found.");
        ArrayList<String> textList = new ArrayList<>();
        int failedToLoad = 0;
        for (File file : files) {
            Path bankPath = file.toPath();
            try {
                int index = FileHelpers.getNumberFromFileName(file);
                byte[] data = binaryDisassemblyProcessor.importDisassembly(bankPath, null);
                String[] text = decoder.parseTextbank(data, index);
                textList.addAll(Arrays.asList(text));
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Background could not be imported : " + bankPath + " : " + e);
            }
        }
        gamescript = new String[textList.size()];
        gamescript = textList.toArray(gamescript);
        Console.logger().info((files.length-failedToLoad) + " text banks loaded with " + gamescript.length + " text lines successfully imported from disasm : " + basePath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " text banks failed to import. See logs above");
        }
        Console.logger().finest("EXITING importDisassembly");
        return gamescript;
    }
    
    public void exportDisassembly(Path basePath, String[] text) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        gamescript = text;
        int failedToSave = 0;
        Path bankPath = null;
        int fileCount = 0;
        TextEncoder encoder = new TextEncoder();
        Path offsetsPath = basePath.resolve(HUFFMANTREEOFFSETS_FILENAME);
        Path treesPath = basePath.resolve(HUFFMANTREES_FILENAME);
        encoder.produceTrees(text);
        encoder.produceTextbanks(text);
        byte[] offsetsData = encoder.getNewHuffmantreeOffsetsFileBytes();
        byte[] treesData = encoder.getNewHuffmanTreesFileBytes();
        byte[][] newTextbanks = encoder.getNewTextbanks();
        binaryDisassemblyProcessor.exportDisassembly(offsetsPath, offsetsData, null);
        binaryDisassemblyProcessor.exportDisassembly(treesPath, treesData, null);
        for(int i=0; i < newTextbanks.length; i++) {
            try {
                String index = String.format("%02d", i);
                bankPath = basePath.resolve(TEXTBANK_FILEPREFIX + index + ".bin");
                binaryDisassemblyProcessor.exportDisassembly(bankPath, newTextbanks[i], null);
                fileCount++;
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Text bank could not be exported : " + bankPath + " : " + e);
            }
        }
        Console.logger().info((fileCount - failedToSave) + " text banks successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " text banks failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportDisassembly");       
    }
    
    public String[] importTxt(Path filePath) throws IOException, TextFileException {
        Console.logger().finest("ENTERING importTxt");
        gamescript = textProcessor.importTextData(filePath);
        asciiReplace(gamescript);
        Console.logger().info(gamescript.length + " lines of text successfully imported from : " + filePath);
        Console.logger().finest("EXITING importTxt");
        return gamescript;
    }
    
    public void exportTxt(Path filePath, String[] text) throws IOException, TextFileException {
        Console.logger().finest("ENTERING exportTxt");
        gamescript = text;
        textProcessor.exportTextData(filePath, text);
        Console.logger().info(gamescript.length + " lines of text successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportTxt");
    }
       
    public String[] importAsm(Path path) {
        Console.logger().finest("ENTERING importAsm");
        gamescript = new AsmManager().importAsm(path.toString(), gamescript);
        Console.logger().info(gamescript.length + " lines of text successfully imported from ASM : " + path);
        Console.logger().finest("EXITING importAsm");
        return gamescript;
    }
    
    public Tileset importBaseTiles(Path palettePath, Path tilesetPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importBaseTiles");
        baseTiles = tilesetManager.importDisassembly(palettePath, tilesetPath, TilesetDisassemblyProcessor.TilesetCompression.STACK, 16);
        Console.logger().finest("EXITING importBaseTiles");
        return baseTiles;
    }
    
    public FontSymbol[] importVWFonts(Path vwFontPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importVWFonts");
        fontSymbols = fontManager.importDisassembly(vwFontPath);
        Console.logger().finest("EXITING importVWFonts");
        return fontSymbols;
    }
    
    public int[] importAsciiMap(Path asciiMapPath) throws IOException, FileNotFoundException, AsmException {
        Console.logger().finest("ENTERING importAsciiMap");
        asciiToSymbolMap = asciiAsmProcessor.importAsmData(asciiMapPath);
        Console.logger().finest("EXITING importAsciiMap");
        return asciiToSymbolMap;
    }
    
    public HashMap<Character, Character> importAsciiReplaceMap(Path replacePath) throws IOException, MetadataException {
        Console.logger().finest("ENTERING importAsciiReplaceMap");
        asciiReplaceMap = new HashMap<Character, Character>();
        asciiReplaceProcessor.importMetadata(replacePath, asciiReplaceMap);
        Console.logger().finest("EXITING importAsciiReplaceMap");
        return asciiReplaceMap;
    }
    
    public String[] importAllyNames(Path allyNamesPath) throws IOException, FileNotFoundException, AsmException {
        Console.logger().finest("ENTERING importAllyNames");
        EntriesAsmData data = allyNamesAsmProcessor.importAsmData(allyNamesPath);
        allyNames = new String[data.uniqueEntriesCount()];
        for (int i = 0; i < allyNames.length; i++) {
            allyNames[i] = data.getUniqueEntries(i);
        }
        Console.logger().finest("EXITING importAllyNames");
        return allyNames;
    }
    
    private void asciiReplace(String[] data) {
        if (data == null || asciiReplaceMap == null || asciiReplaceMap.size() == 0) return;
        String replaceFrom = "";
        String replaceTo = "";
        for (Map.Entry<Character, Character> entry : asciiReplaceMap.entrySet()) {
            replaceFrom += entry.getKey();
            replaceTo += entry.getValue();
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = StringUtils.replaceChars(data[i], replaceFrom, replaceTo);
        }
    }
    
    public String[] getGameScript() {
        return gamescript;
    }
    
    public void setGameScript(String[] gamescript) {
        this.gamescript = gamescript;
    }
    
    public Tileset getBaseTiles() {
        return baseTiles;
    }
    
    public FontSymbol[] getFontSymbols() {
        return fontSymbols;
    }
    
    public int[] getAsciiToSymbolMap() {
        return asciiToSymbolMap;
    }
    
    public HashMap<Character, Character> getAsciiReplaceMap() {
        return asciiReplaceMap;
    }
    
    public String[] getAllyNames() {
        return allyNames;
    }
}
