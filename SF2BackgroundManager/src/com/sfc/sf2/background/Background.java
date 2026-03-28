/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author wiz
 */
public class Background {
    public static final int BG_TILES_WIDTH = 32;
    public static final int BG_TILES_HEIGHT = 12;
    
    private int index;
    
    private Tileset tileset;
    
    public Background(int index, Tileset tileset) {
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Background)) return super.equals(obj);
        Background other = (Background)obj;
        return this.tileset.equals(other.tileset);
    }
}
