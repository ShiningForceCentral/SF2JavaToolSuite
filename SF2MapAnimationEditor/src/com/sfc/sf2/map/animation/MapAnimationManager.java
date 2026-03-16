/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.animation.io.MapAnimationAsmProcessor;
import com.sfc.sf2.map.animation.io.MapAnimationPackage;
import com.sfc.sf2.map.block.MapBlockset;
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
public class MapAnimationManager extends AbstractManager {

    private MapEntryData[] mapEntries;
    private MapLayout layout;
    private MapBlockset blockset;
    private String sharedBlockInfo;
    private MapAnimation animation;
    private String sharedAnimationInfo;
    
    @Override
    public void clearData() {
        mapEntries = null;
        layout = null;
        blockset = null;
        animation = null;
        sharedAnimationInfo = null;
    }
    
    public MapAnimation importDisassembly(Path animationsPath, Path tilesetsEntriesPath, MapLayout layout) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        if (animationsPath != null && animationsPath.toFile().exists()) {
            animation = new MapAnimationAsmProcessor().importAsmData(animationsPath, new MapAnimationPackage(layout.getTilesets()));
            importAnimationTileset(animation, layout.getPalette(), tilesetsEntriesPath, animation.getTilesetId());
            layout.setTilesets(animation.getModifiedTilesets());
            Console.logger().info("Map layout and animation succesfully imported for : " + animationsPath);
        } else {
            animation = new MapAnimation(-1, 0, new MapAnimationFrame[0], layout.getTilesets());
            animation.setAnimationTileset(null);
            getMapLayout().setTilesets(animation.getOriginalTilesets());
            Console.logger().warning("WARNING Map has no animation.");
        }
        //checkForSharedAnimations(terrainEntries, 0);
        Console.logger().finest("EXITING importDisassembly");
        return animation;
    }
    
    public MapAnimation importDisassemblyFromMapData(Path palettesEntriesPath, Path tilesetsEntriesPath, Path tilesetsFilePath, Path blocksPath, Path layoutPath, Path animationsPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromMapData");
        sharedAnimationInfo = null;
        MapLayoutManager mapLayoutManager = new MapLayoutManager();
        layout = mapLayoutManager.importDisassembly(palettesEntriesPath, tilesetsEntriesPath, tilesetsFilePath, blocksPath, layoutPath);
        blockset = mapLayoutManager.getMapBlockset();
        sharedBlockInfo = mapLayoutManager.getSharedBlockInfo();
        animation = importDisassembly(animationsPath, tilesetsEntriesPath, layout);
        Console.logger().finest("EXITING importDisassemblyFromMapData");
        return animation;
    }
    
    public MapAnimation importDisassemblyFromRawFiles(Path palettePath, Path[] tilesetsFilePath, Path blocksetPath, Path layoutPath, Path animationPath, Path tilesetEntriesPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromRawFiles");
        sharedAnimationInfo = null;
        MapLayoutManager mapLayoutManager = new MapLayoutManager();
        layout = new MapLayoutManager().importDisassemblyFromRawFiles(palettePath, tilesetsFilePath, blocksetPath, layoutPath);
        blockset = mapLayoutManager.getMapBlockset();
        sharedBlockInfo = mapLayoutManager.getSharedBlockInfo();
        animation = importDisassembly(animationPath, tilesetEntriesPath, layout);
        Console.logger().finest("EXITING importDisassemblyFromRawFiles");
        return animation;
    }
    
    public MapAnimation importDisassemblyFromEntries(Path palettesEntriesPath, Path tilesetsEntriesPath, Path mapEntriesPath, int mapIndex) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyFromEntries");
        sharedAnimationInfo = null;
        MapEntryData[] mapEntries = ImportMapEntries(mapEntriesPath);
        MapLayoutManager mapLayoutManager = new MapLayoutManager();
        layout = new MapLayoutManager().importDisassemblyFromMapEntries(palettesEntriesPath, tilesetsEntriesPath,mapEntriesPath, mapIndex);
        blockset = mapLayoutManager.getMapBlockset();
        sharedBlockInfo = mapLayoutManager.getSharedBlockInfo();
        MapEntryData mapEntry = (mapIndex >= 0 && mapIndex < mapEntries.length) ? mapEntries[mapIndex] : null;
        if (mapEntry.getAnimationsPath() == null) {
            importDisassembly(null, tilesetsEntriesPath, layout);
        } else {
            importDisassembly(PathHelpers.getIncbinPath().resolve(mapEntry.getAnimationsPath()), tilesetsEntriesPath, layout);
            checkForSharedAnimations(mapEntries, mapIndex, mapEntry.getAnimationsPath());
        }
        Console.logger().finest("EXITING importDisassemblyFromEntries");
        return animation;
    }
    
    public Tileset importAnimationTileset(MapAnimation animation, Palette palette, Path tilesetEntriesPath, int tilesetId) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importTileset");
        this.animation = animation;
        EntriesAsmData tilesetData = new EntriesAsmProcessor().importAsmData(tilesetEntriesPath, null);
        if (tilesetId < 0 || tilesetId >= tilesetData.entriesCount()) {
            animation.setAnimationTileset(null);
            Console.logger().warning("WARNING Map index out of range : " + tilesetId);
            return null;
        }
        Path tilesetPath = PathHelpers.getIncbinPath().resolve(tilesetData.getPathForEntry(tilesetId));
        Tileset tileset = new TilesetManager().importDisassembly(tilesetPath, palette, TilesetDisassemblyProcessor.TilesetCompression.STACK, 8);
        animation.setAnimationTileset(tileset);
        Console.logger().info("Tileset succesfully imported for : " + tilesetPath);
        Console.logger().finest("EXITING importTileset");
        return tileset;
    }
    
    public void exportDisassembly(Path animationsPath, MapAnimation animation) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        if (!this.animation.equals(animation)) {
            this.animation.clearData();
        }
        this.animation = animation;
        new MapAnimationAsmProcessor().exportAsmData(animationsPath, animation, null);
        Console.logger().info("Map animation succesfully exported to : " + animationsPath);
        Console.logger().finest("EXITING exportDisassembly");  
    }
    
    public void exportDisassemblyFromMapEntries(Path mapEntriesPath, int mapId, MapAnimation animation) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassemblyFromMapEntries");
        this.animation = animation;
        MapEntryData[] mapEntries = ImportMapEntries(mapEntriesPath);
        if (mapId < 0 || mapId >= mapEntries.length || mapEntries[mapId] == null) {
            throw new DisassemblyException("Cannot export map " + mapId + ". Map entry was not found or map entries are corrupted.");
        }
        MapEntryData mapEntry = mapEntries[mapId];
        Path animationPath = mapEntry.getLayoutPath() == null ? null : PathHelpers.getIncbinPath().resolve(mapEntry.getAnimationsPath());
        new MapAnimationAsmProcessor().exportAsmData(animationPath, animation, null);
        Console.logger().info("Map animation successfully exported from entries to : " + animationPath);
        Console.logger().finest("EXITING exportDisassemblyFromMapEntries");   
    }
    
    public MapEntryData[] ImportMapEntries(Path mapEntriesPath) throws IOException, AsmException {
        mapEntries = new MapLayoutManager().ImportMapEntries(mapEntriesPath);
        return mapEntries;
    }
    
    public void checkForSharedAnimations(MapEntryData[] mapEntries, int mapId, String path) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < mapEntries.length; i++) {
            if (path.equals(mapEntries[i].getAnimationsPath())) {
                sb.append(String.format("- Map%02d\n", i));
                count++;
            }
        }
        if (count <= 1) {
            sharedAnimationInfo = null;
            Console.logger().finest("Animation not shared with other maps");
        } else {
            sharedAnimationInfo = sb.toString();
            Console.logger().finest(String.format("Animation shared between %d maps", count));
        }
    }

    public MapAnimation getMapAnimation() {
        return animation;
    }

    public MapLayout getMapLayout() {
        return layout;
    }
    
    public MapBlockset getMapBlockset() {
        return blockset;
    }
    
    public Tileset[] getMapTilesets() {
        if (getMapLayout() == null) {
            return null;
        } else {
            return getMapLayout().getTilesets();
        }
    }

    public String getSharedBlockInfo() {
        return sharedBlockInfo;
    }

    public String getSharedAnimationInfo() {
        return sharedAnimationInfo;
    }
}
