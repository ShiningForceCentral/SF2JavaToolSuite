/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author TiMMy
 */
public class MapBlockPackage {
    
    private Tileset[] tilesets;
    public Tileset[] tilesets() { return tilesets; }
    private Palette palette;
    public Palette palette() { return palette; }
    
    private int animTilesetStart;
    public int animTilesetStart() { return animTilesetStart; }
    private int animTilesetLength;
    public int animTilesetLength() { return animTilesetLength; }
    private int animTilesetDest;
    public int animTilesetDest() { return animTilesetDest; }
    
    public MapBlockPackage(Tileset[] tilesets, Palette palette) {
        this.tilesets = tilesets;
        this.palette = palette;
    }
    
    public MapBlockPackage(Tileset[] tilesets, Palette palette, int animTilesetStart, int animTilesetLength, int animTilesetDest) {
        this.tilesets = tilesets;
        this.palette = palette;
        this.animTilesetStart = animTilesetStart;
        this.animTilesetLength = animTilesetLength;
        this.animTilesetDest = animTilesetDest;
    }
}
