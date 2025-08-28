/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background;

import com.sfc.sf2.background.io.BackgroundDisassemblyProcessor;
import com.sfc.sf2.background.io.BackgroundPackage;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.palette.PaletteManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author wiz
 */
public class BackgroundManager extends AbstractManager {
    
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetManager tilesetManager = new TilesetManager();    
    private final BackgroundDisassemblyProcessor backgroundDisassemblyProcessor = new BackgroundDisassemblyProcessor();
    
    private Background[] backgrounds;
    
    @Override
    public void clearData() {
        paletteManager.clearData();
        tilesetManager.clearData();
        if (backgrounds != null) {
            for (int i = 0; i < backgrounds.length; i++) {
                backgrounds[i].getTileset().clearIndexedColorImage(true);
            }
            backgrounds = null;
        }
    }
       
    public Background[] importDisassembly(Path filePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        int index = FileHelpers.getNumberFromFileName(filePath.toFile());
        backgrounds = new Background[1];
        backgrounds[0] = backgroundDisassemblyProcessor.importDisassembly(filePath, new BackgroundPackage(index));
        Console.logger().finest("EXITING importDisassembly");
        return backgrounds;
    }
    
    public Background[] importAllDisassemblies(Path basePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importAllDisassemblies");
        File[] files = FileHelpers.findAllFilesInDirectory(basePath, "background", ".bin");
        Console.logger().info(files.length + " Backgrounds found.");
        ArrayList<Background> bgsList = new ArrayList<>();
        int failedToLoad = 0;
        for (File file : files) {
            Path bgPath = file.toPath();
            try {
                int index = FileHelpers.getNumberFromFileName(file);
                Background bg = backgroundDisassemblyProcessor.importDisassembly(bgPath, new BackgroundPackage(index));
                bgsList.add(bg);
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Background could not be imported : " + bgPath + " : " + e);
            }
        }
        backgrounds = new Background[bgsList.size()];
        backgrounds = bgsList.toArray(backgrounds);
        Console.logger().info(backgrounds.length + " backgrounds successfully imported from disasm : " + basePath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " backgrounds failed to import. See logs above");
        }
        Console.logger().finest("EXITING importAllDisassemblies");
        return backgrounds;
    }
    
    public void exportDisassembly(Path path, Background background) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        backgroundDisassemblyProcessor.exportDisassembly(path, background, null);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportAllDisassemblies(Path basePath, Background[] backgrounds) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportAllDisassemblies");
        this.backgrounds = backgrounds;
        int failedToSave = 0;
        Path bgPath = null;
        int fileCount = 0;
        for (Background background : backgrounds) {
            try {
                bgPath = basePath.resolve(String.format("background%02d%s", background.getIndex(), ".bin"));
                backgroundDisassemblyProcessor.exportDisassembly(bgPath, background, null);
                fileCount++;
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Background could not be exported : " + bgPath + " : " + e);
            }
        }
        Console.logger().info((fileCount - failedToSave) + " backgrounds successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " backgrounds failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportAllDisassemblies");
    }
    
    public Background[] importAllImages(Path basePath, FileFormat format) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importAllImages");
        File[] files = FileHelpers.findAllFilesInDirectory(basePath, "background", AbstractRawImageProcessor.GetFileExtensionString(format));
        Console.logger().info(files.length + " background images found.");
        ArrayList<Background> bgsList = new ArrayList<>();
        int failedToLoad = 0;
        for (File file : files) {
            Path bgPath = file.toPath();
            try {
                int index = FileHelpers.getNumberFromFileName(file);
                Tileset tileset = tilesetManager.importImage(bgPath, false);
                Background background = new Background(index, tileset);
                bgsList.add(background);
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Background could not be imported : " + bgPath + " : " + e);
            }
        }
        backgrounds = new Background[bgsList.size()];
        backgrounds = bgsList.toArray(backgrounds);
        Console.logger().info(backgrounds.length + " backgrounds successfully imported from images : " + basePath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " backgrounds failed to import. See logs above");
        }
        Console.logger().finest("EXITING importAllImages");
        return backgrounds;
    }
    
    public void exportAllImages(Path basePath, Background[] backgrounds, FileFormat format) {
        Console.logger().finest("ENTERING exportAllImages");
        this.backgrounds = backgrounds;
        int failedToSave = 0;
        Path filePath = null;
        int fileCount = 0;
        for (Background background : backgrounds) {
            try {
                filePath = basePath.resolve(String.format("background%02d%s", background.getIndex(), AbstractRawImageProcessor.GetFileExtensionString(format)));
                tilesetManager.exportImage(filePath, background.getTileset());
                fileCount++;
            }catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Background could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((fileCount - failedToSave) + " backgrounds successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " backgrounds failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportAllImages");
    }

    public Background[] getBackgrounds() {
        return backgrounds;
    }
}
