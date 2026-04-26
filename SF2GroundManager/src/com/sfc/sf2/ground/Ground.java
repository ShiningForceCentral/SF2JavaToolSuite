/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.IPaletteGraphic;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author wiz
 */
public class Ground implements INameable, IPaletteGraphic {
    public static final int GROUND_TILES_PER_ROW = 12;
    
    private Tileset tileset;
    
    public Ground(Tileset tileset) {
        this.tileset = tileset;
    }

    @Override
    public String getName() {
        return tileset.getName();
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }
    
    @Override
    public Palette getPalette() {
        if (tileset == null) {
            return null;
        }
        return tileset.getPalette();
    }

    @Override
    public void setPalette(Palette palette) {
        if (tileset == null) return;
        tileset.setPalette(palette);
    }

    @Override
    public byte[] getPixels() {
        if (tileset == null) return null;
        return tileset.getPixels();
    }

    @Override
    public void setPixels(byte[] pixels) {
        if (tileset == null) return;
        tileset.setPixels(pixels);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ground)) return super.equals(obj);
        Ground other = (Ground)obj;
        return this.tileset.equals(other.tileset);
    }
}
