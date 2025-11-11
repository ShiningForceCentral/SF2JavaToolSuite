/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.MapBlocksetManager;
import com.sfc.sf2.map.layout.compression.MapLayoutDecoder;
import com.sfc.sf2.map.layout.io.MapEntriesAsmProcessor;
import com.sfc.sf2.map.layout.io.MapEntryData;
import com.sfc.sf2.map.layout.io.MapLayoutDisassemblyProcessor;
import com.sfc.sf2.map.layout.io.MapLayoutMetaProcessor;
import com.sfc.sf2.map.layout.io.MapLayoutPackage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapLayoutManager extends AbstractManager {
    private final MapBlocksetManager mapBlocksetManager = new MapBlocksetManager();
    private final MapLayoutDisassemblyProcessor layoutDisassemblyProcessor = new MapLayoutDisassemblyProcessor();
    private final MapEntriesAsmProcessor mapEntriesAsmProcessor = new MapEntriesAsmProcessor();
    private final MapLayoutMetaProcessor mapLayoutMetaProcessor = new MapLayoutMetaProcessor();
    
    private MapBlockset blockset;
    private MapLayout layout;
    private MapEntryData[] mapEntries = null;
    private String sharedBlockInfo;

    @Override
    public void clearData() {
        mapBlocksetManager.clearData();
        if (blockset != null) {
            blockset.clearIndexedColorImage(true);
            blockset = null;
        }
        if (layout != null) {
            layout.clearIndexedColorImage(true);
            layout = null;
        }
        mapEntries = null;
        sharedBlockInfo = null;
    }
    
    public MapLayout importDisassembly(Path paletteEntriesPath, Path tilesetEntriesPath, Path tilesetsFilePath, Path blocksetPath, Path layoutPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        blockset = mapBlocksetManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, tilesetsFilePath, blocksetPath);
        int mapId = FileHelpers.getNumberFromFileName(layoutPath.getParent().toFile());
        MapLayoutPackage pckg = new MapLayoutPackage(mapId, blockset, mapBlocksetManager.getTilesets());
        layout = layoutDisassemblyProcessor.importDisassembly(layoutPath, pckg);
        layout.setTilesets(mapBlocksetManager.getTilesets());
        Console.logger().info("Map layout successfully imported for : " + layoutPath);
        Console.logger().finest("EXITING importDisassembly");
        return layout;
    }
       
    public MapLayout importDisassemblyFromRawFiles(Path palettePath, Path[] tilesetsFilePath, Path blocksetPath, Path layoutPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromRawFiles");
        blockset = mapBlocksetManager.importDisassembly(palettePath, tilesetsFilePath, blocksetPath);
        int mapId = FileHelpers.getNumberFromFileName(layoutPath.getParent().toFile());
        MapLayoutPackage pckg = new MapLayoutPackage(mapId, blockset, mapBlocksetManager.getTilesets());
        layout = layoutDisassemblyProcessor.importDisassembly(layoutPath, pckg);
        layout.setTilesets(mapBlocksetManager.getTilesets());
        Console.logger().info("Map layout successfully imported from palette and tilesets for : " + layoutPath);
        Console.logger().finest("EXITING importDisassemblyFromRawFiles");
        return layout;
    }
    
    public MapLayout importDisassemblyFromMapEntries(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, int mapId) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromMapEntries");
        mapEntries = ImportMapEntries(mapEntriesPath);
        if (mapId < 0 || mapId >= mapEntries.length || mapEntries[mapId] == null) {
            throw new DisassemblyException("Cannot import map " + mapId + ". Map entry was not found or map entries are corrupted.");
        }
        MapEntryData mapEntry = mapEntries[mapId];
        Path layoutPath = mapEntry.getLayoutPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getLayoutPath());
        Path blocksetPath = mapEntry.getBlocksPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getBlocksPath());
        Path tilesetsPath = mapEntry.getTilesetsPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getTilesetsPath());
        layout = importDisassembly(paletteEntriesPath, tilesetEntriesPath, tilesetsPath, blocksetPath, layoutPath);
        checkForSharedBlocks(mapEntries, mapEntry.getMapId(), mapEntry.getBlocksPath(), mapEntry.getLayoutPath());
        Console.logger().info("Map layout successfully imported from entries for : " + layoutPath);
        Console.logger().finest("EXITING importDisassemblyFromMapEntries");
        return layout;
    }
    
    public MapEntryData[] ImportMapEntries(Path mapEntriesPath) throws IOException, AsmException {
        if (mapEntries == null) {
            Console.logger().finest("ENTERING importDisassembly");
            mapEntries = mapEntriesAsmProcessor.importAsmData(mapEntriesPath, null);
            Console.logger().info("Map entries successfully imported from : " + mapEntriesPath);
            Console.logger().finest("EXITING importDisassembly");
        }
        return mapEntries;
    }
    
    public void exportDisassembly(Path tilesetsPath, Path blocksetPath, Path layoutPath, MapBlockset mapBlockset, MapLayout mapLayout) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        layout = mapLayout;
        blockset = mapBlockset;
        new MapLayoutDecoder().optimiseBlockset(blockset, mapLayout); //Blockset must be optimised before saving either blockset or layout
        mapBlocksetManager.exportDisassembly(tilesetsPath, blocksetPath, blockset, mapBlocksetManager.getTilesets());
        MapLayoutPackage pckg = new MapLayoutPackage(layout.getIndex(), blockset, mapLayout.getTilesets());
        layoutDisassemblyProcessor.exportDisassembly(layoutPath, layout, pckg);
        Console.logger().info("Map layout successfully exported to : " + layoutPath);
        Console.logger().finest("EXITING exportDisassembly");   
    }
    
    public void exportDisassemblyFromMapEntries(Path mapEntriesPath, int mapId, MapBlockset mapBlockset, MapLayout mapLayout) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        ImportMapEntries(mapEntriesPath);
        if (mapId < 0 || mapId >= mapEntries.length || mapEntries[mapId] == null) {
            throw new DisassemblyException("Cannot export map " + mapId + ". Map entry was not found or map entries are corrupted.");
        }
        layout = mapLayout;
        blockset = mapBlockset;
        new MapLayoutDecoder().optimiseBlockset(blockset, mapLayout); //Blockset must be optimised before saving either blockset or layout
        MapEntryData mapEntry = mapEntries[mapId];
        Path layoutPath = mapEntry.getLayoutPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getLayoutPath());
        Path blocksetPath = mapEntry.getBlocksPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getBlocksPath());
        Path tilesetsPath = mapEntry.getTilesetsPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getTilesetsPath());
        mapBlocksetManager.exportDisassembly(tilesetsPath, blocksetPath, blockset, mapLayout.getTilesets());
        MapLayoutPackage pckg = new MapLayoutPackage(layout.getIndex(), blockset, mapLayout.getTilesets());
        layoutDisassemblyProcessor.exportDisassembly(layoutPath, layout, pckg);
        Console.logger().info("Map layout successfully exported from entries to : " + layoutPath);
        Console.logger().finest("EXITING exportDisassembly");   
    }
    
    public void exportImage(Path filepath, Path flagsPath, Path hpFilePath, MapLayout layout) throws IOException, RawImageException, MetadataException {
        Console.logger().finest("ENTERING exportImage");
        mapBlocksetManager.exportImage(filepath, hpFilePath, MapLayout.BLOCK_WIDTH, layout.layoutToBlockset(), layout.getTilesets());
        mapLayoutMetaProcessor.exportMetadata(flagsPath, layout);
        Console.logger().info("Map layout successfully exported to image : " + filepath + ", flags : " + flagsPath + ", and hpTiles : " + hpFilePath);
        Console.logger().finest("EXITING exportImage");
    }
    
    //TODO Add support for creating new map entries
    /*public int createNewMapEntry(Path mapDirectories, Path mapEntriesPath) {
        int mapId = 0;
        while (mapId < mapEntries.length && !mapEntries[mapId].IsEmpty()) {
            mapId++;
        }
        File mapDirectory = mapDirectories.resolve(String.format("map%02d", mapId)).toFile();
        if (!mapDirectory.exists()) {
            mapDirectory.mkdirs();
        }
        if (mapId >= mapEntries.length) {
            MapEntryData[] newEntries = new MapEntryData[mapEntries.length];
            System.arraycopy(mapEntries, 0, newEntries, 0, mapEntries.length);
            newEntries[mapId] = new MapEntryData(mapId);
            mapEntries = newEntries;
        }
        MapEntryData newMap = mapEntries[mapId];
        Path filePath = PathHelpers.getIncbinPath().relativize(mapDirectory.toPath());
        newMap.setTilesetsPath(filePath.resolve("00-tilesets.asm").toString());
        newMap.setBlocksPath(filePath.resolve("0-blocks.bin").toString());
        newMap.setLayoutPath(filePath.resolve("1-layout.bin").toString());
        
        return mapId;
    }*/
    
    public boolean doesMapEntryExist(int mapID) {
        return mapEntries != null && mapID >= 0 && mapID < mapEntries.length && mapEntries[mapID] != null;
    }
    
    private void checkForSharedBlocks(MapEntryData[] mapEntries, int mapIndex, String blocksPath, String layoutPath) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        boolean countedCurrent;
        for (int i = 0; i < mapEntries.length; i++) {
            countedCurrent = false;
            if (blocksPath.equals(mapEntries[i].getBlocksPath())) {
                sb.append(String.format("- Map%02d - Blocks\n", i));
                count++;
                countedCurrent = true;
            }
            if (layoutPath.equals(mapEntries[i].getLayoutPath())) {
                sb.append(String.format("- Map%02d - Layout\n", i));
                if (!countedCurrent) {
                    count++;
                }
            }
        }
        if (count <= 1) {
            sharedBlockInfo = null;
            Console.logger().finest("Blocks and layout not shared with other maps");
        } else {
            sharedBlockInfo = sb.toString();
            Console.logger().finest(String.format("Blocks and layout shared between %d maps", count));
        }
    }
    
    public MapEntryData[] getMapEntries() {
        return mapEntries;
    }

    public MapLayout getMapLayout() {
        return layout;
    }

    public MapBlockset getMapBlockset() {
        return blockset;
    }

    public String getSharedBlockInfo() {
        return sharedBlockInfo;
    }
}
