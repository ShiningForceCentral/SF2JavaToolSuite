/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.palette.Palette;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class Tileset {
    
    private String name;
    protected Tile[] tiles;
    protected int tilesPerRow;
    
    private BufferedImage indexedColorImage = null;
    
    public Tileset(String name, Tile[] tiles, int tilesPerRow) {
        this.name = name;
        this.tiles = tiles;
        this.tilesPerRow = tilesPerRow;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Tile[] getTiles() {
        return tiles;
    }
    
    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }
    
    public void setTilesPerRow(int tilesPerRow) {
        if (this.tilesPerRow != tilesPerRow)
            clearIndexedColorImage(false);
        this.tilesPerRow = tilesPerRow;
    }
    
    public Dimension getDimensions(int tilesPerRow) {
        this.setTilesPerRow(tilesPerRow);
        return getDimensions();
    }
    
    public Dimension getDimensions() {
        int w = tilesPerRow;
        int h = tiles.length/tilesPerRow;
        if (tiles.length%tilesPerRow != 0) {
            h++;
        }
        return new Dimension(w*PIXEL_WIDTH, h*PIXEL_HEIGHT);
    }

    public Palette getPalette() {
        if (tiles == null || tiles.length == 0) {
            return null;
        }
        return tiles[0].getPalette();
    }

    public void setPalette(Palette palette) {
        if (tiles == null || tiles.length == 0) {
            return;
        }
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].setPalette(palette);
        }
        clearIndexedColorImage(false);
    }
    
    public BufferedImage getIndexedColorImage() {
        if (tiles == null || tiles.length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            int width = tilesPerRow;
            int height = tiles.length/tilesPerRow;
            if (tiles.length%tilesPerRow != 0)
                height++;
            indexedColorImage = new BufferedImage(width*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for(int j=0;j<height;j++){
                for(int i=0;i<width;i++){
                    int tileID = i+j*width;
                    if (tileID >= tiles.length) {
                        break;
                    } else {
                        graphics.drawImage(tiles[tileID].getIndexedColorImage(), i*PIXEL_WIDTH, j*PIXEL_HEIGHT, null);
                    }
                }
            }
            graphics.dispose();
        }
        return indexedColorImage;
    }
    
    public void clearIndexedColorImage(boolean alsoClearTiles) {
        if (this.indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
        }
        
        if (alsoClearTiles) {
            for (int i = 0; i < tiles.length; i++) {
                tiles[i].clearIndexedColorImage();
            }
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Tileset)) return false;
        Tileset tileset = (Tileset)obj;
        for (int i=0; i < this.tiles.length; i++) {
            if (!this.tiles[i].equals(tileset.getTiles()[i])) {
                return false;
            }
        }
        return true;
    }
    
    public Tileset clone() {
        return new Tileset(this.name, this.tiles.clone(), this.tilesPerRow);
    }
    
    public boolean isTilesetEmpty() {
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
    
    public static Tileset EmptyTilset(Palette palette, int tilesPerRow) {
        Tile emptyTile = Tile.EmptyTile(palette);
        Tile[] tiles = new Tile[128];
        for(int i=0; i < tiles.length; i++) {
            tiles[i] = emptyTile;
        }
        Tileset emptyTileset = new Tileset("EMPTY", tiles, tilesPerRow);
        return emptyTileset;
    }
}
