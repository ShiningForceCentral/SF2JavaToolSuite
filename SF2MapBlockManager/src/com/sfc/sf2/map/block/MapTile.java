/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.graphics.Tileset;
import static com.sfc.sf2.helpers.MapBlockHelpers.TILESET_TILES;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class MapTile {
    private byte tilesetIndex;
    private byte tileIndex;
    private TileFlags tileFlags;
    
    public MapTile(int tileData) {
        tileFlags = new TileFlags(TileFlags.TILE_FLAG_NONE);
        if ((tileData&0x8000) != 0) tileFlags.addFlag(TileFlags.TILE_FLAG_PRIORITY);
        if ((tileData&0x0800) != 0) tileFlags.addFlag(TileFlags.TILE_FLAG_HFLIP);
        if ((tileData&0x1000) != 0) tileFlags.addFlag(TileFlags.TILE_FLAG_VFLIP);
        tileData = tileData&0x3FF;
        this.tilesetIndex = (byte)(tileData/TILESET_TILES);
        this.tileIndex = (byte)(tileData%TILESET_TILES);
    }

    public MapTile(byte tilesetIndex, byte tileIndex, TileFlags tileFlags) {
        this.tilesetIndex = tilesetIndex;
        this.tileIndex = tileIndex;
        this.tileFlags = tileFlags;
    }
    
    public int encodeTileID() {
        return tilesetIndex*TILESET_TILES + tileIndex;
    }
    
    public int encodeTileData() {
        int data = encodeTileID();
        if (tileFlags.isPriority()) data |= 0x8000;
        if (tileFlags.isHFlip())    data |= 0x0800;
        if (tileFlags.isVFlip())    data |= 0x1000;
        return data;
    }

    public byte getTilesetIndex() {
        return tilesetIndex;
    }

    public void setTilesetIndex(byte tilesetIndex) {
        this.tilesetIndex = tilesetIndex;
    }

    public byte getTileIndex() {
        return tileIndex;
    }

    public void setTileIndex(byte tileIndex) {
        this.tileIndex = tileIndex;
    }

    public TileFlags getTileFlags() {
        return tileFlags;
    }

    public void setTileFlags(TileFlags tileFlags) {
        this.tileFlags = tileFlags;
    }
    
    public Tile getTile(Tileset[] tilesets) {
        if (tilesets == null || tilesets[tilesetIndex] == null) return null;
        return tilesets[tilesetIndex].getTiles()[tileIndex];
    }
    
    public BufferedImage getIndexedColorImage(Tileset[] tilesets) {
        Tile tile = getTile(tilesets);
        if (tile == null) {
            return null;
        } else {
            return getTile(tilesets).getIndexedColorImage(tileFlags);
        }
    }

    @Override
    public String toString() {
        return String.format("%d: %b, %b, %b", encodeTileID(), tileFlags.isPriority(), tileFlags.isHFlip(), tileFlags.isVFlip());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MapTile)) return super.equals(obj);
        MapTile tile = (MapTile)obj;
        return this.tilesetIndex == tile.tilesetIndex && this.tileIndex == tile.tileIndex && this.tileFlags.equals(tile.tileFlags);
    }
    
    @Override 
    public MapTile clone() {
        return new MapTile(tilesetIndex, tileIndex, new TileFlags(tileFlags.value()));
    }
    
    public static MapTile EmptyMapTile() {
        return new MapTile((byte)-1, (byte)-1, new TileFlags(TileFlags.TILE_FLAG_NONE));
    }
}
