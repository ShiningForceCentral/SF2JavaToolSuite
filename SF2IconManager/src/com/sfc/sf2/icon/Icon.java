/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.icon;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author TiMMy
 */
public class Icon {
    public static final int ICON_TILE_WIDTH = 2;
    public static final int ICON_TILE_HEIGHT = 3;
    
    private int index;
    private Tileset tileset;
    
    public Icon(int index, Tileset tileset) {
        this.index = index;
        this.tileset = tileset;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public Tileset getTileset() {
        return tileset;
    }
    
    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }
    
    public Palette getPalette() {
        if (tileset == null) {
            return null;
        }
        return tileset.getPalette();
    }
    
    public void clearIndexedColorImage(boolean alsoClearTiles) {
        if (tileset != null) {
            tileset.clearIndexedColorImage(alsoClearTiles);
        }
    }
}
