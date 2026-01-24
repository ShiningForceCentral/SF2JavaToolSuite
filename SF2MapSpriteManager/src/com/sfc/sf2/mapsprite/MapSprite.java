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
import com.sfc.sf2.palette.Palette;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class MapSprite {
    
    private int index;
    private int facingIndex;
    private Block[] frames = new Block[2];
    
    private BufferedImage indexedColorImage = null;
    
    public MapSprite(int index, int facingIndex) {
        this.index = index;
        this.facingIndex = facingIndex;
    }
    
    public MapSprite(int index, int facingIndex, Block firstFrame, Block secondFrame) {
        this.index = index;
        this.facingIndex = facingIndex;
        this.frames[0] = firstFrame;
        this.frames[1] = secondFrame;
    }
    
    public int getIndex() {
        return index;
    }

    public int getFacingIndex() {
        return facingIndex;
    }
    
    public int getCombinedIndex() {
        return index*3 + facingIndex;
    }
    
    public void setCombinedIndex(int index) {
        this.index = index/3;
        this.facingIndex = index%3;
    }
    
    public Block[] getFrames() {
        return frames;
    }
    
    public Block getFrame(boolean first) {
        return first ? frames[0] : frames[1];
    }
    
    public void setFrame(Block tileset, boolean first) {
        if (first) {
            frames[0] = tileset;
        } else {
            frames[1] = tileset;
        }
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
        return 2;
    }
    
    public int getSpritesHeight() {
        return 1;
    }
    
    public BufferedImage getIndexedColorImage() {
        return getIndexedColorImage(1);
    }
    
    public BufferedImage getIndexedColorImage(int scale) {
        if (frames == null || frames.length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            int width = getSpritesWidth();
            int height = getSpritesHeight();
            indexedColorImage = new BufferedImage(width*3*PIXEL_WIDTH*scale, height*3*PIXEL_HEIGHT*scale, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for (int f = 0; f < width; f++) {
                int fx = (f%width)*3*PIXEL_WIDTH * scale;
                int fy = (f/width)*3*PIXEL_HEIGHT * scale;
                Tile[] tiles = frames[f] == null ? null : frames[f].getTiles();
                if (tiles != null) {
                    for(int j=0; j < 3; j++) {
                        for(int i=0; i < 3; i++) {
                            int spriteID = i+j*3;
                            if (tiles[spriteID] != null) {
                                graphics.drawImage(tiles[spriteID].getIndexedColorImage(), fx + i*PIXEL_WIDTH*scale, fy + j*PIXEL_HEIGHT*scale, PIXEL_WIDTH*scale, PIXEL_HEIGHT*scale, null);
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
        if (indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
            if (frames != null) {
                for (int i = 0; i < frames.length; i++) {
                    frames[i].clearIndexedColorImage(alsoClearTiles);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("%03d-%d", index, facingIndex);
    }
    
    public boolean isEmpty() {
        if (frames == null) return true;
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] != null && !frames[i].isEmpty()) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return this == null;
        if (obj == this) return true;
        if (!(obj instanceof MapSprite)) return false;
        MapSprite sprite = (MapSprite)obj;
        for (int i = 0; i < frames.length; i++) {
            if ((frames[i] == null) != (sprite.frames[i] == null)) return false;
            if (frames[i] != null && !frames[i].equals(sprite.frames[i])) return false;
        }
        return true;
    }
}
