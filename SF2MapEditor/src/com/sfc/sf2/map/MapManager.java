/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationManager;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.MapBlocksetManager;
import com.sfc.sf2.map.io.*;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutManager;
import com.sfc.sf2.map.layout.io.MapEntryData;
import com.sfc.sf2.palette.Palette;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapManager extends AbstractManager {
            
    private Map map;
    private MapEnums mapEnums;
    
    private String sharedBlockInfo;
    private String sharedAnimInfo;

    @Override
    public void clearData() {
        map = null;
        mapEnums = null;
        
        sharedBlockInfo = null;
        sharedAnimInfo = null;
    }
    
    private Map importDisassembly(String name, MapBlockset blockset, MapLayout layout, MapAnimation animation, Path areasPath, Path flagsPath, Path stepsPath, Path roofsPath,
            Path warpsPath, Path chestItemsPath, Path otherItemsPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        MapArea[] areas = null;
        MapFlagCopyEvent[] flagCopies = null;
        MapCopyEvent[] stepCopies = null;
        MapCopyEvent[] roofCopies = null;
        MapWarpEvent[] warps = null;
        MapItem[] chestItems = null;
        MapItem[] otherItems = null;
        if (areasPath != null) areas = new MapAreaAsmProcessor().importAsmData(areasPath, mapEnums);
        if (flagsPath != null) flagCopies = new MapFlagEventsAsmProcessor().importAsmData(flagsPath, null);
        if (stepsPath != null) stepCopies = new MapStepEventsAsmProcessor().importAsmData(stepsPath, null);
        if (roofsPath != null) roofCopies = new MapRoofEventsAsmProcessor().importAsmData(roofsPath, null);
        if (warpsPath != null) warps = new MapWarpEventsAsmProcessor().importAsmData(warpsPath, null);
        if (chestItemsPath != null) chestItems = new MapItemAsmProcessor().importAsmData(chestItemsPath, null);
        if (otherItemsPath != null) otherItems = new MapItemAsmProcessor().importAsmData(otherItemsPath, null);
        map = new Map(name, blockset, layout, areas, flagCopies, stepCopies, roofCopies, warps, chestItems, otherItems, animation);
        Console.logger().finest("EXITING importDisassembly");
        return map;
    }
    
    public Map importDisassemblyFromData(Path palettesEntriesPath, Path tilesetsEntriesPath, Path tilesetsFilePath, Path blocksPath, Path layoutPath, Path areasPath,
            Path flagsPath, Path stepsPath, Path roofsPath, Path warpsPath, Path chestItemsPath, Path otherItemsPath, Path animationPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromData");
        String name = PathHelpers.filenameFromPath(layoutPath.getParent());
        MapLayoutManager mapLayoutManager = new MapLayoutManager();
        mapLayoutManager.importDisassembly(palettesEntriesPath, tilesetsEntriesPath, tilesetsFilePath, blocksPath, layoutPath);
        MapLayout layout = mapLayoutManager.getMapLayout();
        MapBlockset blockset = mapLayoutManager.getMapBlockset();
        sharedBlockInfo = mapLayoutManager.getSharedBlockInfo();
        MapAnimationManager mapAnimationManager = new MapAnimationManager();
        MapAnimation animation = mapAnimationManager.importDisassembly(animationPath, tilesetsEntriesPath, layout);
        sharedAnimInfo = mapAnimationManager.getSharedAnimationInfo();
        importDisassembly(name, blockset, layout, animation, areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath);
        Console.logger().info("Map successfully imported from data for : " + blocksPath.getParent());
        Console.logger().finest("EXITING importDisassemblyFromData");
        return map;
    }
    
    public Map importDisassemblyFromRawFiles(Path palettePath, Path[] tilesetsFilePath, Path blocksetPath, Path layoutPath, Path areasPath,
            Path flagsPath, Path stepsPath, Path roofsPath, Path warpsPath, Path chestItemsPath, Path otherItemsPath, Path animationPath, Path tilesetEntriesPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromRawFiles");
        String name = PathHelpers.filenameFromPath(layoutPath.getParent());
        MapLayoutManager mapLayoutManager = new MapLayoutManager();
        mapLayoutManager.importDisassemblyFromRawFiles(palettePath, tilesetsFilePath, blocksetPath, layoutPath);
        MapLayout layout = mapLayoutManager.getMapLayout();
        MapBlockset blockset = mapLayoutManager.getMapBlockset();
        sharedBlockInfo = mapLayoutManager.getSharedBlockInfo();
        MapAnimationManager mapAnimationManager = new MapAnimationManager();
        MapAnimation animation = mapAnimationManager.importDisassembly(animationPath, tilesetEntriesPath, layout);
        sharedAnimInfo = mapAnimationManager.getSharedAnimationInfo();
        importDisassembly(name, blockset, layout, animation, areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath);
        Console.logger().finest("EXITING importDisassemblyFromRawFiles");
        return map;
    }
    
    public Map importDisassemblyFromEntries(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, int mapId) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromEntries");
        MapLayoutManager mapLayoutManager = new MapLayoutManager();
        mapLayoutManager.importDisassemblyFromMapEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
        MapLayout layout = mapLayoutManager.getMapLayout();
        MapBlockset blockset = mapLayoutManager.getMapBlockset();
        sharedBlockInfo = mapLayoutManager.getSharedBlockInfo();
        
        MapEntryData[] mapEntries = mapLayoutManager.getMapEntries();
        MapEntryData mapEntry = mapEntries[mapId];
        MapAnimation animation = null;
        if (mapEntry.getAnimationsPath() != null) {
            Path animationPath = PathHelpers.getIncbinPath().resolve(mapEntry.getAnimationsPath());
            MapAnimationManager mapAnimationManager = new MapAnimationManager();
            animation = mapAnimationManager.importDisassembly(animationPath, tilesetEntriesPath, layout);
            mapAnimationManager.checkForSharedAnimations(mapEntries, mapId, mapEntry.getAnimationsPath());
            sharedAnimInfo = mapAnimationManager.getSharedAnimationInfo();
        }
        String name = String.format("map%02d", mapId);
        Path areasPath = null, flagsPath = null, stepsPath = null, roofsPath = null, warpsPath = null, chestItemsPath = null, otherItemsPath = null;
        if (mapEntry.getAreasPath() != null) areasPath = PathHelpers.getIncbinPath().resolve(mapEntry.getAreasPath());
        if (mapEntry.getFlagEventsPath() != null) flagsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getFlagEventsPath());
        if (mapEntry.getStepEventsPath() != null) stepsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getStepEventsPath());
        if (mapEntry.getRoofEventsPath() != null) roofsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getRoofEventsPath());
        if (mapEntry.getWarpEventsPath() != null) warpsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getWarpEventsPath());
        if (mapEntry.getChestItemsPath() != null) chestItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getChestItemsPath());
        if (mapEntry.getOtherItemsPath() != null) otherItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getOtherItemsPath());
        importDisassembly(name, blockset, layout, animation, areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath);
        Console.logger().info("Map successfully imported from entries for : " + mapId);
        Console.logger().finest("EXITING importDisassemblyFromEntries");
        return map;
    }
    
    public void ImportMapEnums(Path sf2enumsPath) throws IOException, AsmException {
        if (mapEnums == null) {
            mapEnums = new MapEnumsAsmProcessor().importAsmData(sf2enumsPath, null);
            Console.logger().info("Map enums successfully loaded from : " + sf2enumsPath);
        }
    }
    
    public Tileset importAnimationTileset(MapAnimation animation, Palette palette, Path tilesetEntriesPath, int tilesetId) throws IOException, AsmException, DisassemblyException {
        return new MapAnimationManager().importAnimationTileset(animation, palette, tilesetEntriesPath, tilesetId);
    }
    
    private void exportDisassembly(Path areasPath, Path flagsPath, Path stepsPath, Path roofsPath, Path warpsPath, Path chestItemsPath, Path otherItemsPath, Map map) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        if (areasPath != null) new MapAreaAsmProcessor().exportAsmData(areasPath, map.getAreas(), mapEnums);
        if (flagsPath != null) new MapFlagEventsAsmProcessor().exportAsmData(flagsPath, map.getFlagCopies(), null);
        if (stepsPath != null) new MapStepEventsAsmProcessor().exportAsmData(stepsPath, map.getStepCopies(), null);
        if (roofsPath != null) new MapRoofEventsAsmProcessor().exportAsmData(roofsPath, map.getRoofCopies(), null);
        if (warpsPath != null) new MapWarpEventsAsmProcessor().exportAsmData(warpsPath, map.getWarps(), null);
        if (chestItemsPath != null) new MapItemAsmProcessor().exportAsmData(chestItemsPath, map.getChestItems(), new MapItemPackage(true));
        if (otherItemsPath != null) new MapItemAsmProcessor().exportAsmData(otherItemsPath, map.getOtherItems(), new MapItemPackage(false));
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportDisassemblyFromData(Path tilesetsFilePath, Path blocksPath, Path layoutPath, Path areasPath, Path flagsPath, Path stepsPath, Path roofsPath, Path warpsPath,
            Path chestItemsPath, Path otherItemsPath, Path animationPath, Map map) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassemblyFromData");
        this.map = map;
        new MapLayoutManager().exportDisassembly(tilesetsFilePath, blocksPath, layoutPath, map.getBlockset(), map.getLayout());
        if (map.getAnimation() != null) {
            new MapAnimationManager().exportDisassembly(animationPath, map.getAnimation());
        }
        exportDisassembly(areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath, map);
        Console.logger().info("Map successfully exported from data for : " + blocksPath.getParent());
        Console.logger().finest("EXITING exportDisassemblyFromData");
    }
    
    public void exportDisassemblyFromMapEntries(Path mapEntriesPath, int mapId, Map map) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.map = map;
        MapLayoutManager mapLayoutManager = new MapLayoutManager();
        MapEntryData[] mapEntries = mapLayoutManager.ImportMapEntries(mapEntriesPath);
        if (mapId < 0 || mapId >= mapEntries.length || mapEntries[mapId] == null) {
            throw new DisassemblyException("Cannot export map " + mapId + ". Map entry was not found or map entries are corrupted.");
        }
        MapEntryData mapEntry = mapEntries[mapId];
        mapLayoutManager.exportDisassemblyFromMapEntries(mapEntriesPath, mapId, map.getBlockset(), map.getLayout());
        if (map.getAnimation() != null) {
            new MapAnimationManager().exportDisassemblyFromMapEntries(mapEntriesPath, mapId, map.getAnimation());
        }
        Path areasPath = null, flagsPath = null, stepsPath = null, roofsPath = null, warpsPath = null, chestItemsPath = null, otherItemsPath = null;
        if (mapEntry.getAreasPath() != null) areasPath = PathHelpers.getIncbinPath().resolve(mapEntry.getAreasPath());
        if (mapEntry.getFlagEventsPath() != null) flagsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getFlagEventsPath());
        if (mapEntry.getStepEventsPath() != null) stepsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getStepEventsPath());
        if (mapEntry.getRoofEventsPath() != null) roofsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getRoofEventsPath());
        if (mapEntry.getWarpEventsPath() != null) warpsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getWarpEventsPath());
        if (mapEntry.getChestItemsPath() != null) chestItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getChestItemsPath());
        if (mapEntry.getOtherItemsPath() != null) otherItemsPath = PathHelpers.getIncbinPath().resolve(mapEntry.getOtherItemsPath());
        exportDisassembly(areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath, map);
        Console.logger().info("Map successfully exported from entries for : " + mapId);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportMapBlocksetImage(Path filepath, Path hpFilepath, int blocksPerRow, MapBlockset mapblockset, Tileset[] tilesets) throws IOException, RawImageException, MetadataException {
        new MapBlocksetManager().exportImage(filepath, hpFilepath, blocksPerRow, mapblockset, tilesets);
    }
    
    public void exportMapLayoutImage(Path filepath, Path flagsPath, Path hpTilesPath, MapLayout layout) throws IOException, RawImageException, MetadataException {
        new MapLayoutManager().exportImage(filepath, flagsPath, hpTilesPath, layout);
    }

    public Map getMap() {
        return map;
    }

    public MapEnums getMapEnums() {
        return mapEnums;
    }
    
    public String getSharedBlockInfo() {
        return sharedBlockInfo;
    }
    
    public String getSharedAnimationInfo() {
        return sharedAnimInfo;
    }
}
