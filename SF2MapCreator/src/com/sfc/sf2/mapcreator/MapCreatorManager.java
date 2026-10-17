/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor.TilesetCompression;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.io.MapBlockPackage;
import com.sfc.sf2.map.block.io.MapBlocksetMetaProcessor;
import com.sfc.sf2.map.block.io.MapBlocksetRawImageProcessor;
import com.sfc.sf2.map.block.io.MapTilesetData;
import com.sfc.sf2.map.block.io.MapTilesetsAsmProcessor;
import com.sfc.sf2.map.layout.BlockFlags;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import com.sfc.sf2.map.layout.MapLayoutManager;
import com.sfc.sf2.map.layout.io.MapLayoutMetaProcessor;
import com.sfc.sf2.mapcreator.io.MapTilesetsData;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class MapCreatorManager extends AbstractManager {

    private ProtoMap protoMap;
    private Tileset[] importedTilesets;

    @Override
    public void clearData() {
        if (protoMap != null) {
            protoMap.getLayout().clearIndexedColorImage(true);
            protoMap = null;
        }
        if (importedTilesets != null) {
            for (int i = 0; i < importedTilesets.length; i++) {
                if (importedTilesets[i] != null) {
                    importedTilesets[i].clearIndexedColorImage(true);
                }
            }
            importedTilesets = null;
        }
    }
    
    public ProtoMap importImage(Path filePath, Path flagsPath, Path hptilesPath) throws IOException, RawImageException, MetadataException {
        Console.logger().finest("ENTERING importImage");
        String name = PathHelpers.filenameFromPath(filePath);
        MapBlocksetRawImageProcessor blocksetProcessor = new MapBlocksetRawImageProcessor();
        MapBlockPackage pckg = new MapBlockPackage(name, null, null);
        MapBlockset blockset = blocksetProcessor.importRawImage(filePath, pckg);
        Tileset[] tilesets = new Tileset[] { blocksetProcessor.getTileset() };
        MapLayoutBlock[] layoutBlocks = new MapLayoutBlock[blockset.getBlocks().length];
        for (int i = 0; i < layoutBlocks.length; i++) {
            layoutBlocks[i] = new MapLayoutBlock(blockset.getBlocks()[i], new BlockFlags(0));
        }
        MapLayout layout = new MapLayout(0, tilesets, layoutBlocks);
        try {
            new MapLayoutMetaProcessor().importMetadata(hptilesPath, layout);
        } catch (Exception ex) {
            Console.logger().warning("Map flags could not be imported from: " + flagsPath);
        }
        try {
            new MapBlocksetMetaProcessor().importMetadata(hptilesPath, blockset);
        } catch (Exception ex) {
            Console.logger().warning("HP tiles data could not be imported from: " + flagsPath);
        }
        protoMap = new ProtoMap(name, blockset, layout);
        Console.logger().info("Map image successfully imported from: " + filePath);
        Console.logger().finest("EXITING importImage");
        return protoMap;
    }
    
    public MapTilesetsData importMapData(Path mapFile, Path palettePath, Path tilesetsPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importMapData");
        MapTilesetData mapData = new MapTilesetsAsmProcessor().importAsmData(mapFile, null);
        String paletteName = String.format("mappalette%02d.bin", mapData.paletteIndex());
        palettePath = palettePath.resolve(paletteName);
        Path[] tilesetPaths = new Path[mapData.tilesetIndices().length];
        for (int i = 0; i < tilesetPaths.length; i++) {
            if (mapData.tilesetIndices()[i] == -1) {
                tilesetPaths[i] = null;
            } else {
                tilesetPaths[i] = tilesetsPath.resolve(String.format("maptileset%03d.bin", mapData.tilesetIndices()[i]));
            }
        }
        Console.logger().finest("EXITING importMapData");
        return new MapTilesetsData(palettePath, tilesetPaths);
    }
    
    public Tileset[] importMapPaletteAndTilesets(Path palettePath, Path[] tilesetPaths) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importMapPaletteAndTilesets");
        if (tilesetPaths.length != 5) {
            throw new IOException("importMapPaletteAndTilesets expects exactly 5 tileset paths. Any/all of the paths can be null");
        }
        Palette palette = new PaletteManager().importDisassembly(palettePath, true);
        TilesetManager tilesetManager = new TilesetManager();
        importedTilesets = new Tileset[tilesetPaths.length];
        for (int i = 0; i < tilesetPaths.length; i++) {
            if (tilesetPaths[i] == null) {
                importedTilesets[i] = null;
            } else {
                importedTilesets[i] = tilesetManager.importDisassembly(tilesetPaths[i], palette, TilesetCompression.STACK, 16);
            }
        }
        Console.logger().finest("EXITING importMapPaletteAndTilesets");
        return importedTilesets;
    }
    
    public void importBaseTilesets(Path[] tilesetPaths, boolean chestGraphics, Path targetPaletteFilepath) {
        Console.logger().finest("ENTERING importBaseTilesets");
        /*Palette palette = null;
        Path palettepath = Paths.get(targetPaletteFilepath);
        if(palettepath.toFile().exists() && palettepath.toFile().isFile()){
            palette = com.sfc.sf2.palette.io.DisassemblyManager.importDisassembly(targetPaletteFilepath);
        }else{
            palette = map.getTiles()[0].getPalette();
        }
        
        Tile[] tiles;
        Tileset[] tilesets = new Tileset[5];
        for(int i=0;i<tilesets.length;i++) {
            String tpath = tilesetPaths[i];
            Path path = Paths.get(tpath);
            if (path.toFile().exists()&&!path.toFile().isDirectory()) {
                tiles = com.sfc.sf2.graphics.io.DisassemblyManager.importDisassembly(tpath, palette, GraphicsManager.COMPRESSION_STACK);
                tilesets[i] = new Tileset();
                tilesets[i].setName(tilesetPaths[i].substring(tilesetPaths[i].lastIndexOf("\\"), tilesetPaths[i].lastIndexOf(".")));
                tilesets[i].setTiles(tiles);
            } else {
                if (i!=4 || !chestGraphics) {
                    tilesets[i] = Tileset.EmptyTilset(palette);
                } else {
                    try {
                        InputStream is = ClassLoader.class.getResourceAsStream("basemaptileset5.bin");
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        int nRead;
                        byte[] data = new byte[424];
                        while ((nRead = is.read(data, 0, data.length)) != -1) {
                            buffer.write(data, 0, nRead);
                        }
                        byte[] bm5 = buffer.toByteArray();
                        tiles =  new StackGraphicsDecoder().decodeStackGraphics(bm5, palette);
                        tilesets[i] = new Tileset();
                        tilesets[i].setName("basemaptileset5");
                        tilesets[i].setTiles(tiles);
                    } catch (IOException ex) {
                        Logger.getLogger(MapCreatorManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        map.setPalette(palette);
        map.setTilesets(tilesets);
        lastImportedPalettePath = targetPaletteFilepath;
        lastImportedTilesetsPaths = tilesetPaths;*/
        Console.logger().finest("EXITING importBaseTilesets");
    }
    
    public void exportPalette(Path filePath, Palette palette) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportPalette");
        new PaletteManager().exportDisassembly(filePath, palette);
        Console.logger().finest("EXITING exportPalette");     
    }
    
    public void exportTilesets(Path[] tilesetPaths, Tileset[] tilesets) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportTilesets");
        TilesetManager tilesetManager = new TilesetManager();
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesets[i] == null || tilesets[i].isTilesetEmpty()) {
                System.out.println("Tilset " + (i+1) + " is blank so will not be exported.");
            } else {
                tilesetManager.exportDisassembly(tilesetPaths[i], tilesets[i], TilesetCompression.STACK);
            }
        }
        Console.logger().info("Map tilesets successfully exported.");
        Console.logger().finest("EXITING exportTilesets");     
    }
    
    public void exportDisassembly(Path tilesetsPath, Path blocksPath, Path layoutPath, ProtoMap map) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        //new MapLayoutManager().exportDisassembly(tilesetsPath, blocksPath, layoutPath, map.getOptimizedBlockset(), map.getLayout());
        Console.logger().info("Map tilesets, blockset, and layout successfully exported.");
        Console.logger().finest("EXITING exportDisassembly");
    }

    public void exportHPTiles(Path hpFilePath, MapLayout layout) throws IOException, MetadataException {
        Console.logger().finest("ENTERING exportHPTiles");
        new MapBlocksetMetaProcessor().exportMetadata(hpFilePath, layout.layoutToBlockset());
        Console.logger().info("Map layout successfully exported to image : " + hpFilePath);
        Console.logger().finest("EXITING exportHPTiles");
    }
    
    public void exportPng(Path filePath, MapLayout layout) throws IOException, RawImageException {
        Console.logger().finest("ENTERING exportPng");
        MapBlockPackage pckg = new MapBlockPackage(Integer.toString(layout.getIndex()), layout.getTilesets(), layout.getTilesets()[0].getPalette());
        new MapBlocksetRawImageProcessor().exportRawImage(filePath, layout.layoutToBlockset(), pckg);
        Console.logger().info("Map layout successfully exported to image : " + filePath);
        Console.logger().finest("EXITING exportPng");
    }

    public ProtoMap getProtoMap() {
        return protoMap;
    }

    public Tileset[] getImportedTilesets() {
        return importedTilesets;
    }
}
