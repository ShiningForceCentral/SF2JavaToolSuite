/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.FileFormat;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.graphics.Block;
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
import java.util.HashMap;
import jdk.jshell.spi.ExecutionControl;

/**
 *
 * @author wiz
 */
public class MapSpriteManager extends AbstractManager {
    
    public enum MapSpriteExportMode {
        INDIVIDUAL_FILES,
        FILE_PER_DIRECTION,
        FILE_PER_CHARACTER,
    }
    
    private final PaletteManager paletteManager = new PaletteManager();
    private final MapSpriteDisassemblyProcessor mapSpriteDisassemblyProcessor = new MapSpriteDisassemblyProcessor();
    private final MapSpriteRawImageProcessor mapSpriteRawImageProcessor = new MapSpriteRawImageProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    
    private MapSpriteEntries mapSprites;
    private MapSprite[] unreferencedMapsprites;
    
    @Override
    public void clearData() {
        paletteManager.clearData();
        if (mapSprites != null) {
            MapSprite[] sprites = mapSprites.getMapSprites();
            for (int i = 0; i < sprites.length; i++) {
                if (sprites[i] != null) {
                    sprites[i].clearIndexedColorImage(true);
                }
            }
            mapSprites = null;
        }
    }

    public MapSprite importDisassembly(Path mapspriteFilePath, Path paletteFilePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        MapSprite sprite = importDisassembly(mapspriteFilePath, palette);
        Console.logger().finest("EXITING importDisassembly");
        return sprite;
    }

    public MapSprite importDisassembly(Path mapspriteFilePath, Palette palette) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        mapSprites = new MapSpriteEntries(1);
        int[] indices = getIndicesFromFilename(mapspriteFilePath.getFileName());
        MapSpritePackage pckg = new MapSpritePackage(mapspriteFilePath.getFileName().toString(), indices, palette, null);
        Block[] frames = mapSpriteDisassemblyProcessor.importDisassembly(mapspriteFilePath, pckg);
        MapSprite newSprite = new MapSprite(indices[0], indices[1]);
        newSprite.setFrame(frames[0], true);
        newSprite.setFrame(frames[1], false);
        mapSprites.addUniqueEntry(0, newSprite);
        Console.logger().info("Mapsprite successfully imported from : " + mapspriteFilePath);
        Console.logger().finest("EXITING importDisassembly");
        return newSprite;
    }

    public MapSpriteEntries importDisassemblyFromEntryFile(Path paletteFilePath, Path entriesPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromEntryFile");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        EntriesAsmData entriesData = entriesAsmProcessor.importAsmData(entriesPath, null);
        Console.logger().info("Mapsprites entries successfully imported. Entries found : " + entriesData.entriesCount());
        int entriesMax = getIndicesFromFilename(entriesData.getUniqueEntries(entriesData.uniqueEntriesCount()-1), "_")[0];
        if (entriesMax < entriesData.entriesCount()/3) {
            entriesMax = entriesData.entriesCount()/3;
        }
        unreferencedMapsprites = null;
        mapSprites = new MapSpriteEntries(entriesMax*3);
        int frameCount = 0;
        int failedToLoad = 0;
        int[] indices = new int[3];
        indices[2] = -1;
        for (int i = 0; i < entriesData.entriesCount(); i++) {
            Path tilesetPath = null;
            try {
                indices[0] = i/3;
                indices[1] = i%3;
                int[] loadIndices = getIndicesFromFilename(entriesData.getEntry(i), "_");
                int index = indices[0]*3 + indices[1];
                int loadedIndex = loadIndices[0]*3 + loadIndices[1];
                if (index == loadedIndex) {
                    //Is unique
                    tilesetPath = PathHelpers.getIncbinPath().resolve(entriesData.getPathForEntry(index));
                    MapSpritePackage pckg = new MapSpritePackage(tilesetPath.getFileName().toString(), indices, palette, null);
                    Block[] frames = mapSpriteDisassemblyProcessor.importDisassembly(tilesetPath, pckg);
                    frameCount+=2;
                    MapSprite sprite;
                    if (mapSprites.hasData(index)) {
                        sprite = mapSprites.getMapSprite(i);
                    } else if (frames == null) {
                        Console.logger().warning("WARNING Mapsprite entry is empty, must be a placeholder. Mapsprite " + tilesetPath);
                        sprite = null;
                        mapSprites.addUniqueEntry(index, sprite);
                    } else {
                        sprite = new MapSprite(indices[0], indices[1], frames[0], frames[1]);
                        mapSprites.addUniqueEntry(index, sprite);
                    }
                } else {
                    //Is duplicate
                    mapSprites.addDuplicateEntry(index, loadedIndex);
                }
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Mapsprite could not be imported : " + tilesetPath + " : " + e);
            }
        }
        Console.logger().info(mapSprites.getMapSprites().length + " mapsprites with " + frameCount + " frames successfully imported from images : " + entriesPath);
        Console.logger().info((entriesData.entriesCount() - entriesData.uniqueEntriesCount()) + " duplicate mapsprite entries found.");
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " mapsprites failed to import. See logs above");
        }
        Console.logger().finest("EXITING importDisassemblyFromEntryFile");
        return mapSprites;
    }

    public MapSpriteEntries importAllDisassemblies(Path mapspritesPath, Path entriesPath, Path paletteFilePath) throws IOException, AsmException, DisassemblyException {
        HashMap<Integer, MapSprite> loadedSprites = importSprites(mapspritesPath, paletteFilePath, FileFormat.BIN);
        parseEntries(entriesPath, loadedSprites);
        return mapSprites; 
    }
    
    public MapSpriteEntries importAllImages(Path paletteFilePath, Path imagesPath, Path entriesPath, FileFormat format) throws IOException, AsmException, DisassemblyException {
        HashMap<Integer, MapSprite> loadedSprites = importSprites(imagesPath, paletteFilePath, format);
        parseEntries(entriesPath, loadedSprites);
        return mapSprites;
    }
    
    public HashMap<Integer, MapSprite> importSprites(Path itemsPath, Path paletteFilePath, FileFormat format) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importSprites");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        File[] files = FileHelpers.findAllFilesInDirectory(itemsPath, "mapsprite", format);
        Console.logger().finest(files.length + " files found.");
        HashMap<Integer, MapSprite> mapSprites = new HashMap<>();
        int frameCount = 0;
        int failedToLoad = 0;
        for (File file : files) {
            Path tilesetPath = file.toPath();
            try {
                int[] indices = getIndicesFromFilename(tilesetPath.getFileName());
                Block[] frames = null;
                MapSpritePackage pckg = new MapSpritePackage(tilesetPath.getFileName().toString(), indices, palette, null);
                if (format == FileFormat.BIN) {
                    frames = mapSpriteDisassemblyProcessor.importDisassembly(tilesetPath, pckg);
                } else {
                    frames = mapSpriteRawImageProcessor.importRawImage(tilesetPath, pckg);
                }
                if (frames != null) {
                    frameCount += frames.length;
                }
                int index;
                MapSprite sprite;
                if (indices[1] == -1) {
                    //6 sprites
                    for (int i = 0; i < 3; i++) {
                        index = indices[0]*3+i;
                        if (mapSprites.containsKey(index)) {
                            sprite = mapSprites.get(index);
                        } else {
                            sprite = new MapSprite(indices[0], i);
                            mapSprites.put(index, sprite);
                        }
                        if (frames != null) {
                            sprite.setFrame(frames[i*2+0], true);
                            sprite.setFrame(frames[i*2+1], false);
                        }
                    }
                } else if (indices[2] == -1) {
                    //2 sprites
                    index = indices[0]*3+indices[1];
                    if (mapSprites.containsKey(index)) {
                        sprite = mapSprites.get(index);
                    } else {
                        sprite = new MapSprite(indices[0], indices[1]);
                        mapSprites.put(index, sprite);
                    }
                    if (frames != null) {
                        sprite.setFrame(frames[0], true);
                        sprite.setFrame(frames[1], false);
                    }
                } else {
                    //1 sprite
                    index = indices[0]*3+indices[1];
                    if (mapSprites.containsKey(index)) {
                        sprite = mapSprites.get(index);
                    } else {
                        sprite = new MapSprite(indices[0], indices[1]);
                        mapSprites.put(index, sprite);
                    }
                    if (frames != null) {
                        sprite.setFrame(frames[0], indices[2] == 0);
                    }
                }
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Mapsprite could not be imported : " + tilesetPath + " : " + e);
            }
        }
        Console.logger().info(mapSprites.size() + " mapsprites with " + frameCount + " frames successfully imported from : " + itemsPath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " mapsprites failed to import. See logs above");
        }
        Console.logger().finest("EXITING importSprites");
        return mapSprites;
    }
    
    public void parseEntries(Path entriesPath, HashMap<Integer, MapSprite> loadedMapSprites) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING parseEntries");
        EntriesAsmData entriesData = entriesAsmProcessor.importAsmData(entriesPath, null);
        Console.logger().finest("Mapsprites entries successfully imported. Entries found : " + entriesData.entriesCount());
        int entriesMax = getIndicesFromFilename(entriesData.getUniqueEntries(entriesData.uniqueEntriesCount()-1), "_")[0];
        if (entriesMax < entriesData.entriesCount()/3) {
            entriesMax = entriesData.entriesCount()/3;
        }
        mapSprites = new MapSpriteEntries(entriesMax*3);
        int unfoundEntries = 0;
        for (int i = 0; i < entriesData.entriesCount(); i++) {
            int[] loadIndices = getIndicesFromFilename(entriesData.getEntry(i), "_");
            int loadedIndex = loadIndices[0]*3 + loadIndices[1];
            if (i == loadedIndex) {
                //Is unique
                if (loadedMapSprites.containsKey(i)) {
                    mapSprites.addUniqueEntry(i, loadedMapSprites.get(i));
                    loadedMapSprites.remove(i);
                } else {
                    unfoundEntries++;
                    Console.logger().warning(String.format("WARNING mapSprite could not be found for entry: %03d-%d", (i/3), (i%3)));
                    mapSprites.addUniqueEntry(i, null);
                }
            } else {
                //Is duplicate
                mapSprites.addDuplicateEntry(i, loadedIndex);
            }
        }
        
        unreferencedMapsprites = new MapSprite[loadedMapSprites.size()];
        unreferencedMapsprites = loadedMapSprites.values().toArray(unreferencedMapsprites);
        int loadedCount = entriesData.uniqueEntriesCount()-unfoundEntries-unreferencedMapsprites.length;
        Console.logger().info(String.format("Mapsprite entries parsed. %d mapSprites matched to entries. %d entries without matching sprites. %d mapSprites unreferenced by entries.", loadedCount, unfoundEntries, unreferencedMapsprites.length));
        Console.logger().finest("EXITING parseEntries");
    }
    
    public void exportAllDisassemblies(Path basePath, MapSpriteEntries mapSprites) {
        Console.logger().finest("ENTERING exportDisassembly");
        this.mapSprites = mapSprites;
        int failedToSave = 0;
        Path filePath = null;
        Block[] frames = new Block[2];
        int totalEntries = mapSprites.getEntries().length;
        for (int i=0; i < totalEntries; i++) {
            if (mapSprites.isDuplicateEntry(i)) continue;
            try {
                int index = mapSprites.getEntries()[i];
                int facing = index%3;
                index /= 3;
                filePath = basePath.resolve(String.format("mapsprite%03d-%d.bin", index, facing));
                MapSprite mapSprite = mapSprites.getMapSprite(i);
                if (mapSprite == null) {
                    //Data was a 2 byte placeholder (e.g. like Mapsprite237_0)
                    mapSpriteDisassemblyProcessor.exportDisassembly(filePath, null, null);
                    break;
                } else {
                    frames[0] = mapSprite.getFrame(true);
                    frames[1] = mapSprite.getFrame(false);
                    if (frames[0] == null) frames[0] = Block.EmptyBlock(i, null);
                    if (frames[1] == null) frames[1] = Block.EmptyBlock(i, null);
                    mapSpriteDisassemblyProcessor.exportDisassembly(filePath, frames, null);
                }
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Mapsprite could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((mapSprites.getMapSprites().length - failedToSave) + " mapsprites successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " mapsprites failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportAllImages(Path basePath, MapSpriteEntries mapSprites, MapSpriteExportMode exportMode, FileFormat format) {
        Console.logger().finest("ENTERING exportImage");
        this.mapSprites = mapSprites;
        int failedToSave = 0;
        Path filePath = null;
        int files = 0;
        int step = exportMode == exportMode.FILE_PER_CHARACTER ? 3 : 1;
        MapSprite mapSprite = null;
        Palette palette = null;
        for (int m = 0; m < mapSprites.getEntries().length; m += step) {
            try {
                switch (exportMode) {
                    case INDIVIDUAL_FILES:
                        if (mapSprites.isDuplicateEntry(m)) continue;
                        mapSprite = mapSprites.getMapSprite(m);
                        palette = mapSprite == null ? null : mapSprite.getPalette();
                        for (int i = 0; i < 2; i++) {
                            Block[] frames = new Block[1];
                            frames[0] = mapSprite == null ? null : mapSprite.getFrame(i == 0);
                            if (frames[0] != null) {
                                files++;
                                int[] indices = new int[] { mapSprite.getIndex(), mapSprite.getFacingIndex(), i%2 };
                                filePath = basePath.resolve(String.format("mapsprite%03d-%d-%d%s", indices[0], indices[1], indices[2], format.getExt()));
                                MapSpritePackage pckg = new MapSpritePackage(null, indices, palette, exportMode);
                                mapSpriteRawImageProcessor.exportRawImage(filePath, frames, pckg);
                            }
                        }
                    break;
                    case FILE_PER_DIRECTION:
                        if (mapSprites.isDuplicateEntry(m)) continue;
                        mapSprite = mapSprites.getMapSprite(m);
                        palette = mapSprite == null ? null : mapSprite.getPalette();
                        Block[] frames = new Block[2];
                        frames[0] = mapSprite == null ? null : mapSprite.getFrame(true);
                        frames[1] = mapSprite == null ? null : mapSprite.getFrame(false);
                        if (frames[0] != null && frames[1] != null) {
                            files++;
                            int[] indices = new int[] { mapSprite.getIndex(), mapSprite.getFacingIndex(), -1 };
                            filePath = basePath.resolve(String.format("mapsprite%03d-%d%s", indices[0], indices[1], format.getExt()));
                            MapSpritePackage pckg = new MapSpritePackage(null, indices, palette, exportMode);
                            mapSpriteRawImageProcessor.exportRawImage(filePath, frames, pckg);
                        }
                        break;
                    case FILE_PER_CHARACTER:
                        files++;
                        frames = new Block[6];
                        palette = null;
                        boolean allEmpty = true;
                        int index = -1;
                        for (int i = 0; i < 3; i++) {
                            if (mapSprites.isDuplicateEntry(m+i)) {
                                frames[i*2+0] = null;
                                frames[i*2+1] = null;
                            } else {
                                mapSprite = mapSprites.getMapSprite(m+i);
                                if (mapSprite == null) {
                                    frames[i*2+0] = null;
                                    frames[i*2+1] = null;
                                } else {
                                    frames[i*2+0] = mapSprite.getFrame(true);
                                    frames[i*2+1] = mapSprite.getFrame(false);
                                    allEmpty = false;
                                    if (index == -1) {
                                        index = mapSprite.getIndex();
                                    }
                                    if (palette == null) {
                                        palette = mapSprite.getPalette();
                                    }
                                }
                            }
                        }
                        if (allEmpty) continue;
                        int[] indices = new int[] { index, -1, -1 };
                        filePath = basePath.resolve(String.format("mapsprite%03d%s", indices[0], format.getExt()));
                        MapSpritePackage pckg = new MapSpritePackage(null, indices, palette, exportMode);
                        mapSpriteRawImageProcessor.exportRawImage(filePath, frames, pckg);
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
    
    public void exportEntries(Path entriesPath, MapSpriteEntries mapSprites) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportEntries");
        this.mapSprites = mapSprites;
        EntriesAsmData asmData = new EntriesAsmData();
        asmData.setHeadername("Map sprites");
        asmData.setPointerListName("pt_Mapsprites");
        asmData.setIsDoubleList(true);
        Path entryPath = PathHelpers.getIncbinPath().relativize(PathHelpers.getBasePath());
        for (int i = 0; i < mapSprites.getEntries().length; i++) {
            int index = mapSprites.getEntries()[i];
            int facingIndex = index%3;
            index /= 3;
            boolean isEmpty = mapSprites.getMapSprite(i) == null || mapSprites.getMapSprite(i).isEmpty();
            if (mapSprites.isDuplicateEntry(i)) {
                String entry = String.format("Mapsprite%03d_%d", index, facingIndex);
                asmData.addEntry(entry);
            } else {
                String entry = String.format("Mapsprite%03d_%d", index, facingIndex);
                String path = String.format("mapsprite%03d-%d.bin", index, facingIndex);
                asmData.addPath(entry, entryPath.resolve(path));
            }
        }
        entriesAsmProcessor.exportAsmData(entriesPath, asmData, null);
        Console.logger().info("Mapsprites entries successfully exported to : " + entriesPath + ". Entries : " + asmData.entriesCount());
        Console.logger().finest("EXITING exportEntries");
    }
    
    public MapSpriteEntries getMapSprites() {
        return mapSprites;
    }

    public MapSprite[] getUnreferencedMapsprites() {
        return unreferencedMapsprites;
    }
    
    private int[] getIndicesFromFilename(Path filename) {
        return getIndicesFromFilename(filename.toString(), "-");
    }
    
    private int[] getIndicesFromFilename(String name, String splitter) {
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            name = name.substring(0, dotIndex);
        }
        String[] split = name.substring(9).split(splitter);
        int[] indices = new int[3];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i < split.length ? Integer.parseInt(split[i]) : -1;
        }
        return indices;
    }
}
