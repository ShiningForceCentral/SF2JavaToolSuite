/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.palette.Palette;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class MapLayout {
    
    public static final int BLOCK_WIDTH = 64;
    public static final int BLOCK_HEIGHT = 64;
    public static final int BLOCK_COUNT = BLOCK_WIDTH*BLOCK_HEIGHT;
    
    private int index;
     
    private Tileset[] tilesets;
    protected MapLayoutBlock[] blocks;
    
    private BufferedImage indexedColorImage = null;

    public MapLayout(int index, Tileset[] tilesets, MapLayoutBlock[] layoutBlocks) {
        this.index = index;
        this.tilesets = tilesets;
        this.blocks = layoutBlocks;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }
    
    public MapLayoutBlock[] getBlocks() {
        return blocks;
    }
    
    public void setBlocks(MapLayoutBlock[] blocks) {
        this.blocks = blocks;
        clearIndexedColorImage(false);
    }
    
    public BufferedImage getIndexedColorImage(Tileset[] tilesets) {
        if (blocks == null || blocks.length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            indexedColorImage = new BufferedImage(BLOCK_WIDTH*PIXEL_WIDTH, BLOCK_HEIGHT*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for(int j=0;j<BLOCK_HEIGHT;j++){
                for(int i=0;i<BLOCK_WIDTH;i++){
                    int tileID = i+j*BLOCK_WIDTH;
                    if (blocks[tileID] != null && blocks[tileID].getMapBlock() != null) {
                        graphics.drawImage(blocks[tileID].getMapBlock().getIndexedColorImage(tilesets), i*PIXEL_WIDTH, j*PIXEL_HEIGHT, null);
                    }
                }
            }
            graphics.dispose();
        }
        return indexedColorImage;
    }
    
    public void clearIndexedColorImage(boolean alsoClearBlocks) {
        if (this.indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
        }
        
        if (alsoClearBlocks) {
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].getMapBlock().clearIndexedColorImage();
            }
        }
    }
    
    public MapBlockset layoutToBlockset() {
        MapBlock[] blocks = new MapBlock[BLOCK_COUNT];
        for (int i = 0; i < BLOCK_COUNT; i++) {
            blocks[i] = this.blocks[i].getMapBlock();
        }
        return new MapBlockset(blocks, BLOCK_WIDTH);
    }

    public Palette getPalette() {
        if (tilesets == null) return null;
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesets[i] != null) {
                return tilesets[i].getPalette();
            }
        }
        return null;
    }
}
