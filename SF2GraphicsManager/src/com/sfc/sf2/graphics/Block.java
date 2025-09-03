/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import com.sfc.sf2.palette.Palette;
import java.awt.Dimension;

/**
 *
 * @author TiMMy
 */
public class Block extends Tileset {
    public static final int TILE_WIDTH = 3;
    public static final int TILE_HEIGHT = 3;
    public static final int TILES_COUNT = TILE_WIDTH*TILE_HEIGHT;
    public static final int PIXEL_WIDTH = TILE_WIDTH*Tile.PIXEL_WIDTH;
    public static final int PIXEL_HEIGHT = TILE_HEIGHT*Tile.PIXEL_HEIGHT;
    
    protected int index;
    
    public Block(int index, Tile[] tiles) {
        super(String.format("Block-%02d", index), tiles, TILE_WIDTH);
        this.index = index;
    }
    
    public Block(int index, Tileset tileset) {
        super(tileset.getName(), tileset.getTiles(), tileset.tilesPerRow);
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    @Override 
    public Block clone() {
        return new Block(index, tiles.clone());
    }
    
    public boolean isEmpty() {
        if (tiles == null || tiles.length == 0) {
            return true;
        }
        for (int i = 0; i < tiles.length; i++) {
            if (!tiles[i].isTileEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static Block EmptyBlock(int index, Palette palette) {
        return new Block(index, EmptyTilset(palette, TILE_WIDTH));
    }
}
