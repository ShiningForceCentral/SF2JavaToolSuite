/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block;

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
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.block.io.MapBlocksetDisassemblyProcessor;
import com.sfc.sf2.map.block.io.MapBlockPackage;
import com.sfc.sf2.map.block.io.MapTilesetData;
import com.sfc.sf2.map.block.io.MapTilesetsAsmProcessor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class MapBlockManager extends AbstractManager {
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetManager tilesetManager = new TilesetManager();
    private final MapBlocksetDisassemblyProcessor blocksetDisassemblyProcessor = new MapBlocksetDisassemblyProcessor();
    private final MapTilesetsAsmProcessor mapilesetsAsmProcessor = new MapTilesetsAsmProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    
    private Tileset[] tilesets;
    private MapBlockset mapBlockset;

    @Override
    public void clearData() {
        paletteManager.clearData();
        tilesetManager.clearData();
        if (tilesets != null) {
            for (int i = 0; i < tilesets.length; i++) {
                if (tilesets[i] != null) {
                    tilesets[i].clearIndexedColorImage(true);
                }
            }
            tilesets = null;
        }
        if (mapBlockset != null) {
            mapBlockset.clearIndexedColorImage(true);
            mapBlockset = null;
        }
    }
       
    public void importDisassembly(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapTilesetsFilePath, Path blocksPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        EntriesAsmData paletteData = entriesAsmProcessor.importAsmData(paletteEntriesPath);
        EntriesAsmData tilesetData = entriesAsmProcessor.importAsmData(tilesetEntriesPath);
        MapTilesetData mapData = mapilesetsAsmProcessor.importAsmData(mapTilesetsFilePath);
        Path palettePath = PathHelpers.getIncbinPath().resolve(paletteData.getPathForEntry(mapData.paletteIndex()));
        Palette palette = paletteManager.importDisassembly(palettePath, true);
        tilesets = new Tileset[mapData.tilesetIndices().length];
        for (int i = 0; i < tilesets.length; i++) {
            if (mapData.tilesetIndices()[i] != -1) {
                Path tilesetPath = PathHelpers.getIncbinPath().resolve(tilesetData.getPathForEntry(mapData.tilesetIndices()[i]));
                tilesets[i] = tilesetManager.importDisassembly(tilesetPath, palette, TilesetDisassemblyProcessor.TilesetCompression.STACK, 8);
            }
        }
        MapBlockPackage pckg = new MapBlockPackage(tilesets, palette);
        mapBlockset = blocksetDisassemblyProcessor.importDisassembly(blocksPath, pckg);
        Console.logger().info("Map blocks successfully imported from entries paths. Map data : " + mapTilesetsFilePath);
        Console.logger().finest("EXITING importDisassembly");
    }
       
    public void importDisassembly(Path palettePath, Path[] tilesetPaths, Path blocksPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        Palette palette = paletteManager.importDisassembly(palettePath, true);
        tilesets = new Tileset[tilesetPaths.length];
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesetPaths[i] != null) {
                tilesets[i] = tilesetManager.importDisassembly(tilesetPaths[i], palette, TilesetDisassemblyProcessor.TilesetCompression.STACK, 8);
            }
        }
        MapBlockPackage pckg = new MapBlockPackage(tilesets, palette);
        mapBlockset = blocksetDisassemblyProcessor.importDisassembly(blocksPath, pckg);
        Console.logger().info("Map blocks successfully imported from palette and tilesets : " + blocksPath);
        Console.logger().finest("EXITING importDisassembly");
    }
       
    public void importDisassembly(Path palettePath, Path[] tilesetPaths, Path blocksPath, Path animTilesetPath, int animTilesetStart, int animTilesetLength, int animTilesetDest) {
        Console.logger().finest("ENTERING importDisassembly");
        //blocks = disassemblyManager.importDisassembly(palettePath, tilesetPaths, blocksPath, animTilesetPath, animTilesetStart, animTilesetLength, animTilesetDest);
        //tilesets = disassemblyManager.getTilesets();
        //graphicsManager.setTiles(tiles);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void exportDisassembly(Path mapTilesetsFilePath, Path blocksPath, MapBlockset mapBlockset, Tileset[] tilesets) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.mapBlockset = mapBlockset;
        MapBlockPackage pckg = new MapBlockPackage(tilesets, mapBlockset.getPalette());
        blocksetDisassemblyProcessor.exportDisassembly(blocksPath, mapBlockset, pckg);
        int paletteIndex = StringHelpers.getNumberFromString(mapBlockset.getPalette().getName());
        int[] tilesetIndices = new int[tilesets.length];
        for (int i = 0; i < tilesetIndices.length; i++) {
            tilesetIndices[i] = tilesets[i] == null ? -1 : StringHelpers.getNumberFromString(tilesets[i].getName());
        }
        mapilesetsAsmProcessor.exportAsmData(mapTilesetsFilePath, new MapTilesetData(paletteIndex, tilesetIndices));
        Console.logger().info("Map blocks successfully exported to : " + blocksPath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportDisassembly(Path blocksPath, MapBlockset mapBlockset, Tileset[] tilesets) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.mapBlockset = mapBlockset;
        MapBlockPackage pckg = new MapBlockPackage(tilesets, mapBlockset.getPalette());
        blocksetDisassemblyProcessor.exportDisassembly(blocksPath, mapBlockset, pckg);
        Console.logger().info("Map blocks successfully exported to : " + blocksPath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void importImage(Path filepath, Path hpFilePath){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportGif() - Exporting GIF ...");
        blocks = RawImageManager.importImage(filepath);
        MetaManager.importBlockHpTilesFile(hpFilePath, blocks, RawImageManager.getImportedImageBlockWidth());
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportGif() - GIF exported.");*/
    }
    
    public void exportImage(Path filepath, Path hpFilePath, int blocksPerRow){
        /*System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportPng() - Exporting PNG ...");
        RawImageManager.exportRawImage(blocks, filepath, blocksPerRow, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        MetaManager.exportBlockHpTilesFile(blocks, blocksPerRow, hpFilePath);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportPng() - PNG exported.");*/
    }

    public MapBlockset getMapBlockset() {
        return mapBlockset;
    }

    public void setMapBlockset(MapBlockset mapBlockset) {
        this.mapBlockset = mapBlockset;
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }
}
