/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class MapSprite {
    
    private int index;
    private Block[] frames = new Block[6];    //2x up, left, down frames
    
    private BufferedImage indexedColorImage = null;
    
    public MapSprite(int index) {
        this.index = index;
    }
    
    public MapSprite(int index, Block[] frames) {
        this.index = index;
        this.frames = frames;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public Block[] getFrames() {
        return frames;
    }
    
    public Block getFrame(int[] indices) {
        return getFrame(indices[1], indices[2]);
    }
    
    public Block getFrame(int facingIndex, int frameIndex) {
        return frames[facingIndex*2 + frameIndex];
    }
    
    public void addFrame(Block tileset, int[] indices) {
        addFrame(tileset, indices[1], indices[2]);
    }
    
    public void addFrame(Block tileset, int facingIndex, int frameIndex) {
        frames[facingIndex*2 + frameIndex] = tileset;
    }
    
    public Palette getPalette() {
        if (frames == null) {
            return null;
        }
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] != null) return frames[i].getPalette();
        }
        return null;
    }
    
    public int getSpritesWidth() {
        return 6;
    }
    
    public int getSpritesHeight() {
        return 1;
    }
    
    public BufferedImage getIndexedColorImage() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            int width = getSpritesWidth();
            int height = getSpritesHeight();
            indexedColorImage = new BufferedImage(width*3*PIXEL_WIDTH, height*3*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for (int f = 0; f < width; f++) {
                int fx = (f%width)*3*PIXEL_WIDTH;
                int fy = (f/width)*3*PIXEL_HEIGHT;
                Tile[] tiles = frames[f] == null ? null : frames[f].getTiles();
                if (tiles != null) {
                    for(int j=0; j < 3; j++) {
                        for(int i=0; i < 3; i++) {
                            int spriteID = i+j*3;
                            if (tiles[spriteID] != null) {
                                graphics.drawImage(tiles[spriteID].getIndexedColorImage(), fx + i*PIXEL_WIDTH, fy + j*PIXEL_HEIGHT, null);
                            }
                        }
                    }
                }
            }
            graphics.dispose();
        }
        return indexedColorImage;
    }
    
    public void clearIndexedColorImage(boolean alsoClearTiles) {
        indexedColorImage.flush();
        indexedColorImage = null;
        for (int i = 0; i < frames.length; i++) {
            frames[i].clearIndexedColorImage(alsoClearTiles);
        }
    }
}
