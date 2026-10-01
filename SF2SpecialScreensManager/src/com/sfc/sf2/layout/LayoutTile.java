/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.layout;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class LayoutTile {
    private int tileIndex;
    private int paletteIndex;
    private TileFlags tileFlags;
    
    public LayoutTile(int tileIndex, int paletteIndex) {
        this.tileIndex = tileIndex;
        this.paletteIndex = paletteIndex;
        tileFlags = new TileFlags(TileFlags.TILE_FLAG_NONE);
    }

    public LayoutTile(int tileIndex, int paletteIndex, TileFlags tileFlags) {
        this.tileIndex = tileIndex;
        this.paletteIndex = paletteIndex;
        this.tileFlags = tileFlags;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public void setTileIndex(int tileIndex) {
        this.tileIndex = tileIndex;
    }

    public int getPaletteIndex() {
        return paletteIndex;
    }

    public void setPaletteIndex(int paletteIndex) {
        this.paletteIndex = paletteIndex;
    }

    public TileFlags getTileFlags() {
        return tileFlags;
    }

    public void setTileFlags(TileFlags tileFlags) {
        this.tileFlags = tileFlags;
    }
    
    public Tile getTile(Tileset[] tilesets) {
        int tileset = 0;
        int index = tileIndex;
        if (tilesets == null || tilesets.length == 0) {
            return null;
        }
        
        while (tileset < tilesets.length && tilesets[tileset] != null && index > tilesets[tileset].getTiles().length) {
            index -= tilesets[tileset].getTiles().length;
            tileset++;
        }
        if (tileset < tilesets.length && index < tilesets[tileset].getTiles().length) {
            return tilesets[tileset].getTiles()[index];
        } else {
            return tilesets[0].getTiles()[0];
        }
    }
    
    public BufferedImage getIndexedColorImage(Palette[] palettes, Tileset[] tilesets) {
        Tile tile = getTile(tilesets);
        if (tile == null) {
            return null;
        } else {
            tile.setPalette(palettes[paletteIndex]);
            return tile.getIndexedColorImage(tileFlags);
        }
    }
    
    public boolean isEmpty() {
        return tileIndex <= 0;
    }

    @Override
    public String toString() {
        return String.format("%d: %b, %b, %b", tileIndex, tileFlags.isPriority(), tileFlags.isHFlip(), tileFlags.isVFlip());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LayoutTile)) return super.equals(obj);
        LayoutTile tile = (LayoutTile)obj;
        return this.tileIndex == tile.tileIndex && this.tileFlags.equals(tile.tileFlags);
    }
    
    @Override 
    public LayoutTile clone() {
        return new LayoutTile(tileIndex, paletteIndex, new TileFlags(tileFlags.value()));
    }
    
    public static LayoutTile EmptyLayoutTile() {
        return new LayoutTile(0, 0, new TileFlags(TileFlags.TILE_FLAG_NONE));
    }
}
