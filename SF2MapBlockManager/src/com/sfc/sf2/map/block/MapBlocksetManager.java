/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.block.io.MapBlockPackage;
import com.sfc.sf2.map.block.io.MapBlocksetDisassemblyProcessor;
import com.sfc.sf2.map.block.io.MapBlocksetMetaProcessor;
import com.sfc.sf2.map.block.io.MapBlocksetRawImageProcessor;
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
public class MapBlocksetManager extends AbstractManager {    
    private Tileset[] tilesets;
    private MapBlockset mapBlockset;

    @Override
    public void clearData() {
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
       
    public MapBlockset importDisassembly(Path palettePath, Path[] tilesetPaths, Path blocksetPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        Palette palette = new PaletteManager().importDisassembly(palettePath, true);
        tilesets = new Tileset[tilesetPaths.length];
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesetPaths[i] != null) {
                tilesets[i] = new TilesetManager().importDisassembly(tilesetPaths[i], palette, TilesetDisassemblyProcessor.TilesetCompression.STACK, 8);
            }
        }
        MapBlockPackage pckg = new MapBlockPackage(PathHelpers.filenameFromPath(blocksetPath), tilesets, palette);
        mapBlockset = new MapBlocksetDisassemblyProcessor().importDisassembly(blocksetPath, pckg);
        Console.logger().info("Map blocks successfully imported from palette and tilesets : " + blocksetPath);
        Console.logger().finest("EXITING importDisassembly");
        return mapBlockset;
    }
       
    public MapBlockset importDisassemblyFromEntries(Path paletteEntriesPath, Path tilesetEntriesPath, Path tilesetsFilePath, Path blocksetPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        EntriesAsmData paletteData = new EntriesAsmProcessor().importAsmData(paletteEntriesPath, null);
        EntriesAsmData tilesetData = new EntriesAsmProcessor().importAsmData(tilesetEntriesPath, null);
        MapTilesetData mapData = new MapTilesetsAsmProcessor().importAsmData(tilesetsFilePath, null);
        Path palettePath = PathHelpers.getIncbinPath().resolve(paletteData.getPathForEntry(mapData.paletteIndex()));
        Palette palette = new PaletteManager().importDisassembly(palettePath, true);
        tilesets = new Tileset[mapData.tilesetIndices().length];
        TilesetManager tilesetManager = new TilesetManager();
        for (int i = 0; i < tilesets.length; i++) {
            if (mapData.tilesetIndices()[i] != -1) {
                Path tilesetPath = PathHelpers.getIncbinPath().resolve(tilesetData.getPathForEntry(mapData.tilesetIndices()[i]));
                tilesets[i] = tilesetManager.importDisassembly(tilesetPath, palette, TilesetDisassemblyProcessor.TilesetCompression.STACK, 8);
            }
        }
        MapBlockPackage pckg = new MapBlockPackage(PathHelpers.filenameFromPath(blocksetPath), tilesets, palette);
        mapBlockset = new MapBlocksetDisassemblyProcessor().importDisassembly(blocksetPath, pckg);
        Console.logger().info("Map blocks successfully imported from entries paths. Map data : " + tilesetsFilePath);
        Console.logger().finest("EXITING importDisassembly");
        return mapBlockset;
    }
    
    public void importImage(Path filepath, Path hpFilePath) throws IOException, RawImageException, MetadataException {
        Console.logger().finest("ENTERING importImage");
        mapBlockset = new MapBlocksetRawImageProcessor().importRawImage(filepath, null);
        new MapBlocksetMetaProcessor().importMetadata(hpFilePath, mapBlockset);
        Console.logger().info("Map blocks successfully imported from image : " + filepath + " and hpTiles : " + hpFilePath);
        Console.logger().finest("EXITING importImage");
    }
    
    public void exportDisassembly(Path tilesetsFilePath, Path blocksetPath, MapBlockset mapBlockset, Tileset[] mapTilesets) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.mapBlockset = mapBlockset;
        MapBlockPackage pckg = new MapBlockPackage(mapBlockset.getName(), mapTilesets, mapTilesets[0].getPalette());
        new MapBlocksetDisassemblyProcessor().exportDisassembly(blocksetPath, mapBlockset, pckg);
        int paletteIndex = StringHelpers.getNumberFromString(mapTilesets[0].getPalette().getName());
        int[] tilesetIndices = new int[mapTilesets.length];
        for (int i = 0; i < tilesetIndices.length; i++) {
            tilesetIndices[i] = mapTilesets[i] == null ? -1 : StringHelpers.getNumberFromString(mapTilesets[i].getName());
        }
        new MapTilesetsAsmProcessor().exportAsmData(tilesetsFilePath, new MapTilesetData(paletteIndex, tilesetIndices), null);
        Console.logger().info("Map blocks successfully exported to : " + blocksetPath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportImage(Path filepath, Path hpFilePath, int blocksPerRow, MapBlockset mapBlockset, Tileset[] tilesets) throws IOException, RawImageException, MetadataException {
        Console.logger().finest("ENTERING exportImage");
        mapBlockset.setBlocksPerRow(blocksPerRow);
        this.mapBlockset = mapBlockset;
        MapBlockPackage pckg = new MapBlockPackage(mapBlockset.getName(), tilesets, tilesets[0].getPalette());
        new MapBlocksetRawImageProcessor().exportRawImage(filepath, mapBlockset, pckg);
        new MapBlocksetMetaProcessor().exportMetadata(hpFilePath, mapBlockset);
        Console.logger().info("Map blocks successfully exported to image : " + filepath + " and hpTiles : " + hpFilePath);
        Console.logger().finest("EXITING exportImage");
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
