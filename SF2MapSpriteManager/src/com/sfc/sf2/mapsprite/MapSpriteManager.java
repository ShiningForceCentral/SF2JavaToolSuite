/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.mapsprite.io.MapSpriteDisassemblyProcessor;
import com.sfc.sf2.mapsprite.io.MapSpritePackage;
import com.sfc.sf2.mapsprite.io.MapSpriteRawImageProcessor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import jdk.jshell.spi.ExecutionControl;

/**
 *
 * @author wiz
 */
public class MapSpriteManager extends AbstractManager {
    
    public enum MapSpriteExportMode {
        INDIVIDUAL_FILES,
        FILE_PER_DIRECTION,
        FILE_PER_SET,
    }
    
    private final PaletteManager paletteManager = new PaletteManager();
    private final MapSpriteDisassemblyProcessor mapSpriteDisassemblyProcessor = new MapSpriteDisassemblyProcessor();
    private final MapSpriteRawImageProcessor mapSpriteRawImageProcessor = new MapSpriteRawImageProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    
    private MapSprite[] mapSprites;
    
    @Override
    public void clearData() {
        paletteManager.clearData();
        if (mapSprites != null) {
            for (int i = 0; i < mapSprites.length; i++) {
                mapSprites[i].clearIndexedColorImage(true);
            }
            mapSprites = null;
        }
    }

    public MapSprite[] importDisassembly(Path paletteFilePath, Path graphicsFilePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        mapSprites = new MapSprite[1];
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        int[] indices = getIndicesFromFilename(graphicsFilePath.getFileName());
        MapSpritePackage pckg = new MapSpritePackage(graphicsFilePath.getFileName().toString(), indices, palette, null);
        Block[] frames = mapSpriteDisassemblyProcessor.importDisassembly(graphicsFilePath, pckg);
        MapSprite newSprite = new MapSprite(indices[0]);
        newSprite.addFrame(frames[0], indices[1], 0);
        newSprite.addFrame(frames[1], indices[1], 1);
        mapSprites[0] = newSprite;
        Console.logger().info("Mapsprite successfully imported from : " + graphicsFilePath);
        Console.logger().finest("EXITING importDisassembly");
        return mapSprites;
    }
        
    //TODO update to new format
    public MapSprite[] importDisassemblyFromEntryFile(Path paletteFilePath, Path entriesPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromEntryFile");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        EntriesAsmData entriesData = entriesAsmProcessor.importAsmData(entriesPath);
        Console.logger().info("Mapsprites entries successfully imported. Entries found : " + entriesData.entriesCount());
        ArrayList<MapSprite> spritesList = new ArrayList<>();
        MapSprite lastMapSprite = null;
        int frameCount = 0;
        int failedToLoad = 0;
        for (int i = 0; i < entriesData.entriesCount(); i++) {
            Path tilesetPath = PathHelpers.getIncbinPath().resolve(entriesData.getPathForEntry(i));
            try {
                int[] indices = getIndicesFromFilename(tilesetPath.getFileName());
                MapSpritePackage pckg = new MapSpritePackage(tilesetPath.getFileName().toString(), indices, palette, null);
                Block[] frames = mapSpriteDisassemblyProcessor.importDisassembly(tilesetPath, pckg);
                frameCount+=2;
                if (lastMapSprite == null || lastMapSprite.getIndex() != indices[0]) {
                    lastMapSprite = new MapSprite(indices[0]);
                    spritesList.add(lastMapSprite);
                }
                lastMapSprite.addFrame(frames[0], indices[1], 0);
                lastMapSprite.addFrame(frames[1], indices[1], 1);
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Mapsprite could not be imported : " + tilesetPath + " : " + e);
            }
        }
        mapSprites = new MapSprite[spritesList.size()];
        mapSprites = spritesList.toArray(mapSprites);
        Console.logger().info(mapSprites.length + " mapsprites with " + frameCount + " frames successfully imported from entries file : " + entriesPath);
        Console.logger().info((entriesData.entriesCount() - entriesData.uniqueEntriesCount()) + " duplicate mapsprite entries found.");
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " mapsprites failed to import. See logs above");
        }
        Console.logger().finest("EXITING importDisassemblyFromEntryFile");
        return mapSprites;
    }
    
    public void exportAllDisassemblies(Path basePath, MapSprite[] mapSprites) {
        Console.logger().finest("ENTERING exportDisassembly");
        this.mapSprites = mapSprites;
        int failedToSave = 0;
        Path filePath = null;
        Block[] frames = new Block[2];
        for (MapSprite mapSprite : mapSprites) {
            try {
                int index = mapSprite.getIndex();
                for (int i = 0; i < 3; i++) {   //For each facing direction
                    filePath = basePath.resolve(String.format("mapsprite%03d-%d.bin", index, i));
                    frames[0] = mapSprite.getFrame(i, 0);
                    frames[1] = mapSprite.getFrame(i, 1);
                    if (frames[0] != null || frames[1] != null) {
                        mapSpriteDisassemblyProcessor.exportDisassembly(filePath, frames, null);
                    }
                }
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Mapsprite could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((mapSprites.length - failedToSave) + " mapsprites successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " mapsprites failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public MapSprite[] importAllImages(Path paletteFilePath, Path basePath, FileFormat format) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importImage");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        File[] files = FileHelpers.findAllFilesInDirectory(basePath, "mapsprite", AbstractRawImageProcessor.GetFileExtensionString(format));
        Console.logger().info(files.length + " mapsprite images found.");
        ArrayList<MapSprite> spritesList = new ArrayList<>();
        MapSprite lastMapSprite = null;
        int frameCount = 0;
        int failedToLoad = 0;
        for (File file : files) {
            Path tilesetPath = file.toPath();
            try {
                int[] indices = getIndicesFromFilename(tilesetPath.getFileName());
                MapSpritePackage pckg = new MapSpritePackage(tilesetPath.getFileName().toString(), indices, palette, null);
                Block[] frames = mapSpriteRawImageProcessor.importRawImage(tilesetPath, pckg);
                frameCount+=frames.length;
                if (lastMapSprite == null || lastMapSprite.getIndex() != indices[0]) {
                    lastMapSprite = new MapSprite(indices[0]);
                    spritesList.add(lastMapSprite);
                }
                for (int i = 0; i < frames.length; i++) {
                    if (indices[1] == -1) {
                        lastMapSprite.addFrame(frames[i], i/2, i%2);
                    } else if (indices[2] == -1) {
                        lastMapSprite.addFrame(frames[i], indices[1], i%2);
                    } else {
                        lastMapSprite.addFrame(frames[i], indices[1], indices[2]);
                    }
                }
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Mapsprite could not be imported : " + tilesetPath + " : " + e);
            }
        }
        mapSprites = new MapSprite[spritesList.size()];
        mapSprites = spritesList.toArray(mapSprites);
        Console.logger().info(mapSprites.length + " mapsprites with " + frameCount + " frames successfully imported from images : " + basePath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " mapsprites failed to import. See logs above");
        }
        Console.logger().finest("EXITING importImage");
        return mapSprites;
    }
    
    public void exportAllImages(Path basePath, MapSprite[] mapSprites, MapSpriteExportMode exportMode, FileFormat format) {
        Console.logger().finest("ENTERING exportImage");
        this.mapSprites = mapSprites;
        int failedToSave = 0;
        Path filePath = null;
        int files = 0;
        for (MapSprite mapSprite : mapSprites) {
            try {
                int index = mapSprite.getIndex();
                MapSpritePackage pckg = new MapSpritePackage(null, new int[] { index }, mapSprite.getPalette(), exportMode);
                switch (exportMode) {
                    case INDIVIDUAL_FILES:
                        for (int i = 0; i < 6; i++) {
                            Block[] frames = new Block[1];
                            frames[0] = mapSprite.getFrame(i/2, i%2);
                            if (frames[0] != null) {
                                files++;
                                filePath = basePath.resolve(String.format("mapsprite%03d-%d-%d%s", index, i/2, i%2, AbstractRawImageProcessor.GetFileExtensionString(format)));
                                mapSpriteRawImageProcessor.exportRawImage(filePath, frames, pckg);
                            }
                        }
                    break;
                    case FILE_PER_DIRECTION:
                        for (int i = 0; i < 3; i++) {
                            Block[] frames = new Block[2];
                            frames[0] = mapSprite.getFrame(i, 0);
                            frames[1] = mapSprite.getFrame(i, 1);
                            if (frames[0] != null && frames[1] != null) {
                                files++;
                                filePath = basePath.resolve(String.format("mapsprite%03d-%d%s", index, i, AbstractRawImageProcessor.GetFileExtensionString(format)));
                                mapSpriteRawImageProcessor.exportRawImage(filePath, frames, pckg);
                            }
                        }
                        break;
                    case FILE_PER_SET:
                        files++;
                        filePath = basePath.resolve(String.format("mapsprite%03d%s", index, AbstractRawImageProcessor.GetFileExtensionString(format)));
                        mapSpriteRawImageProcessor.exportRawImage(filePath, mapSprite.getFrames(), pckg);
                        break;
                    default:
                        throw new ExecutionControl.NotImplementedException("Export format " + exportMode + "not supported.");
                }
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Mapsprite could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((files - failedToSave) + " mapsprites successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " mapsprites failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportImage");
    }
    
    public MapSprite[] getMapSprites() {
        return mapSprites;
    }
    
    private int[] getIndicesFromFilename(Path filename) {
        String name = filename.toString();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            name = name.substring(0, dotIndex);
        }
        String[] split = name.substring(9).split("-");
        int[] indices = new int[3];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i < split.length ? Integer.parseInt(split[i]) : -1;
        }
        return indices;
    }
}
