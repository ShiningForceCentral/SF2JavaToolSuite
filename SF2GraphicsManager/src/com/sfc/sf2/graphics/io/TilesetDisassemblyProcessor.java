/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compression.UncompressedGraphicsDecoder;
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public class TilesetDisassemblyProcessor extends AbstractDisassemblyProcessor<Tileset, TilesetPackage> {

    public enum TilesetCompression {
        NONE,
        BASIC,
        STACK,
    }
    
    @Override
    protected Tileset parseDisassemblyData(byte[] data, TilesetPackage pckg) throws DisassemblyException {
        Tile[] tiles = null;
        switch (pckg.compression()) {
            case NONE:
                tiles = new UncompressedGraphicsDecoder().decode(data, pckg.palette());
                break;
            case BASIC:
                tiles = new BasicGraphicsDecoder().decode(data, pckg.palette());
                break;
            case STACK:
                tiles = new StackGraphicsDecoder().decode(data, pckg.palette());
                break;
            default:
                throw new DisassemblyException("Compression mode not recognosed. Compression : " + pckg.compression());
        }
        if (tiles == null || tiles.length == 0) {
            throw new DisassemblyException("Tileset not loaded. Tiles are empty.");
        }
        return new Tileset(pckg.name(), tiles, pckg.tilesPerRow());
    }

    @Override
    protected byte[] packageDisassemblyData(Tileset item, TilesetPackage pckg) throws DisassemblyException {
        byte[] bytes = null;
        switch(pckg.compression()){
            case NONE:
                bytes = new UncompressedGraphicsDecoder().encode(item.getTiles());
                break;
            case BASIC:
                bytes = new BasicGraphicsDecoder().encode(item.getTiles());
                break;
            case STACK:
                bytes = new StackGraphicsDecoder().encode(item.getTiles());
                break;
            default:
                throw new DisassemblyException("Compression mode not recognosed. Compression : " + pckg.compression());
        }
        if (bytes == null || bytes.length == 0) {
            throw new DisassemblyException("Tileset not loaded. Tiles are empty.");
        }
        return bytes;
    }

    //TODO update to new format. TODO Should this be in TilsetManager
    public Tileset importDisassemblyWithLayout(Path baseTilesetPath, Palette[] palettes, Path tileset1FilePath, int tileset1Offset, Path tileset2FilePath, int tileset2Offset, TilesetCompression compression, int tilesPerRow, Path layoutPath)
            throws IOException, DisassemblyException {
        TilesetPackage basePckg = new TilesetPackage(PathHelpers.filenameFromPath(baseTilesetPath), TilesetCompression.STACK, palettes[0], tilesPerRow);
        Tile[] baseTiles = this.importDisassembly(baseTilesetPath, basePckg).getTiles();
        TilesetPackage tilesetPckg1 = new TilesetPackage(PathHelpers.filenameFromPath(tileset1FilePath), compression, palettes[0], tilesPerRow);
        TilesetPackage tilesetPckg2 = new TilesetPackage(PathHelpers.filenameFromPath(tileset2FilePath), compression, palettes[0], tilesPerRow);
        Tile[] tileset1 = this.importDisassembly(tileset1FilePath, tilesetPckg1).getTiles();
        Tile[] tileset2 = this.importDisassembly(tileset2FilePath, tilesetPckg2).getTiles();
        Tile[] vRamTiles = new Tile[0x800];
        Tile[] tiles = null;
        System.arraycopy(baseTiles, 0, vRamTiles, 0, baseTiles.length);
        System.arraycopy(tileset1, 0, vRamTiles, tileset1Offset, tileset1.length);
        System.arraycopy(tileset2, 0, vRamTiles, tileset2Offset, tileset2.length);
        byte[] data = Files.readAllBytes(layoutPath);
        Tile[] layoutTiles = new Tile[data.length/2];
        for(int i=0;i<layoutTiles.length;i++){
            int layoutValue = BinaryHelpers.getWord(data,i*2);
            int priority = (layoutValue&0x8000)>>15;
            int palette = (layoutValue&0x6000)>>13;
            int vFlip = (layoutValue&0x1000)>>12;
            int hFlip = (layoutValue&0x0800)>>11;
            int tileId = (layoutValue&0x7FF);
            if(tileId>=0&&tileId<vRamTiles.length){
                Tile outputTile = vRamTiles[tileId];
                outputTile.setHighPriority(priority!=0);
                if(outputTile!=null&&palette!=0){
                    outputTile = Tile.paletteSwap(outputTile,palettes[palette]);
                }
                if(outputTile!=null&&vFlip!=0){
                    outputTile = Tile.vFlip(outputTile);
                }
                if(outputTile!=null&&hFlip!=0){
                    outputTile = Tile.hFlip(outputTile);
                }
                layoutTiles[i] = outputTile;
            }
            if(layoutTiles[i]==null){
                Console.logger().finest("Layout tile "+i+" : wrong tile id "+tileId);
                layoutTiles[i] = baseTiles[0];
            }
        }
        tiles = layoutTiles;

        Tileset tileset = new Tileset(basePckg.name(), tiles, tilesPerRow);
        return tileset;
    }

    //TODO update to new format. TODO Should this be in TilsetManager
    public void exportTilesAndLayout(Tileset tileset, Path tilesPath, Path layoutPath, int graphicsOffset, TilesetCompression compression, Palette palette)
            throws IOException, DisassemblyException {
        int tilesetIndex = -1;
        Tile[] tiles = tileset.getTiles();
        byte[] layout = new byte[tiles.length*2];
        List<Tile> tilesList = new ArrayList();
        for(int i=0;i<tiles.length;i++){
            Tile tile = tiles[i];
            for(int j=0;j<tilesList.size();j++){
                Tile t = tilesList.get(j);
                if(t.equals(tile)){
                    tilesetIndex = j + graphicsOffset;
                    break;
                }
            }
            if(tilesetIndex==-1){
                tilesList.add(tile);
                tilesetIndex = tilesList.size()-1+graphicsOffset;
            }
            layout[i*2] = (byte)(((tilesetIndex&0x700)>>8)&0xFF);
            layout[i*2+1] = (byte)(tilesetIndex&0xFF);
            tilesetIndex=-1;
        }

        Tile[] outputTiles = new Tile[tilesList.size()];
        outputTiles = tilesList.toArray(outputTiles);

        Tileset newTileset = new Tileset(PathHelpers.filenameFromPath(tilesPath), outputTiles, 0);
        TilesetPackage pckg = new TilesetPackage(newTileset.getName(), compression, palette, 0);
        this.exportDisassembly(tilesPath, newTileset, pckg);
        Files.write(layoutPath, layout);
    }
}
