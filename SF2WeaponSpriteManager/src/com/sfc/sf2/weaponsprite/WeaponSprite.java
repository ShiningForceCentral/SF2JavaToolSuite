/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class WeaponSprite {
    
    public static final int WEAPONSPRITE_FRAMES_LENGTH = 4;
    public static final int FRAME_TILE_WIDTH = 8;
    public static final int FRAME_TILE_HEIGHT = 8;
    
    private int index;
    
    private Tileset[] frames;
    
    public WeaponSprite(int index, Tileset[] frames) {
        this.index = index;
        this.frames = frames;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Tileset[] getFrames() {
        return frames;
    }

    public void setFrames(Tileset[] frames) {
        this.frames = frames;
    }
    
    /*public Dimension getDimensions() {
        int w = tilesPerRow;
        int h = tiles.length/tilesPerRow;
        if (tiles.length%tilesPerRow != 0) {
            h++;
        }
        return new Dimension(w*PIXEL_WIDTH, h*PIXEL_HEIGHT);
    }*/

    public Palette getPalette() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        return frames[0].getPalette();
    }
    
    public void setPalette(Palette palette) {
        if (frames == null || frames.length == 0) {
            return;
        }
        for (int f = 0; f < frames.length; f++) {
            Tile[] tiles = frames[f].getTiles();
            for (int t = 0; t < tiles.length; t++) {
                tiles[t].setPalette(palette);
            }
        }
        clearIndexedColorImage(false);
    }
    
    public void clearIndexedColorImage(boolean alsoClearTiles) {
        if (frames == null || frames.length == 0) {
            return;
        }
        for (int i = 0; i < frames.length; i++) {
            frames[i].clearIndexedColorImage(alsoClearTiles);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WeaponSprite)) return false;
        WeaponSprite other = (WeaponSprite)obj;
        return this.index == other.index && Arrays.equals(this.frames, other.frames);
    }
}
