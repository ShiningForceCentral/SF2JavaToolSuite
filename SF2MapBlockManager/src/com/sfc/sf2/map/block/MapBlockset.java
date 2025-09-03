/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.palette.Palette;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class MapBlockset {
    
    protected MapBlock[] blocks;
    protected int blocksPerRow;
    
    private BufferedImage indexedColorImage = null;
    
    public MapBlockset(MapBlock[] blocks, int blocksPerRow) {
        this.blocks = blocks;
        this.blocksPerRow = blocksPerRow;
    }
    
    public MapBlock[] getBlocks() {
        return blocks;
    }
    
    public void setBlocks(MapBlock[] blocks) {
        this.blocks = blocks;
        clearIndexedColorImage(false);
    }
    
    public int getBlocksPerRow() {
        return blocksPerRow;
    }
    
    public void setBlocksPerRow(int blocksPerRow) {
        if (this.blocksPerRow != blocksPerRow)
            clearIndexedColorImage(false);
        this.blocksPerRow = blocksPerRow;
    }
    
    public Dimension getDimensions(int blocksPerRow) {
        this.setBlocksPerRow(blocksPerRow);
        return getDimensions();
    }
    
    public Dimension getDimensions() {
        int w = blocksPerRow;
        int h = blocks.length/blocksPerRow;
        if (blocks.length%blocksPerRow != 0) {
            h++;
        }
        return new Dimension(w*PIXEL_WIDTH, h*PIXEL_HEIGHT);
    }

    public Palette getPalette() {
        if (blocks == null || blocks.length == 0) {
            return null;
        }
        return blocks[0].getPalette();
    }

    public void setPalette(Palette palette) {
        if (blocks == null || blocks.length == 0) {
            return;
        }
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setPalette(palette);
        }
        clearIndexedColorImage(false);
    }
    
    public BufferedImage getIndexedColorImage() {
        if (blocks == null || blocks.length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            int width = blocksPerRow;
            int height = blocks.length/blocksPerRow;
            if (blocks.length%blocksPerRow != 0)
                height++;
            indexedColorImage = new BufferedImage(width*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for(int j=0;j<height;j++){
                for(int i=0;i<width;i++){
                    int tileID = i+j*width;
                    if (tileID >= blocks.length) {
                        break;
                    } else if (blocks[tileID] != null) {
                        graphics.drawImage(blocks[tileID].getIndexedColorImage(), i*PIXEL_WIDTH, j*PIXEL_HEIGHT, null);
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
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].clearIndexedColorImage(alsoClearTiles);
            }
        }
    }
    
    public void insertBlock(int index, MapBlock block) {
        if (index < 0 || index > blocks.length) return;
        MapBlock[] newBlocks = new MapBlock[blocks.length+1];
        System.arraycopy(blocks, 0, newBlocks, 0, index);
        newBlocks[index] = block.clone();
        newBlocks[index].setIndex(index);
        for (int i = index+1; i < newBlocks.length; i++) {
            newBlocks[i] = blocks[i-1];
            newBlocks[i].setIndex(i);
        }
        setBlocks(newBlocks);
    }
    
    public void removeBlock(int index) {
        if (index < 0 || index >= blocks.length) return;
        MapBlock[] newBlocks = new MapBlock[blocks.length-1];
        System.arraycopy(blocks, 0, newBlocks, 0, index);
        for (int i = index; i < newBlocks.length; i++) {
            newBlocks[i] = blocks[i+1];
            newBlocks[i].setIndex(i);
        }
        setBlocks(newBlocks);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof MapBlockset)) return false;
        MapBlockset blockset = (MapBlockset)obj;
        for (int i=0; i < this.blocks.length; i++) {
            if (!this.blocks[i].equalsWithPriority(blockset.getBlocks()[i])) {
                return false;
            }
        }
        return true;
    }
    
    public MapBlockset clone() {
        return new MapBlockset(this.blocks.clone(), this.blocksPerRow);
    }
    
    public boolean isBlocksetEmpty() {
        if (blocks == null || blocks.length == 0) {
            return true;
        }
        for (int i = 0; i < blocks.length; i++) {
            if (!blocks[i].isTilesetEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static MapBlockset EmptyMapBlockset(Palette palette, int blocksPerRow) {
        MapBlock emptyBlock = MapBlock.EmptyMapBlock(-1, 0, palette);
        MapBlock[] blocks = new MapBlock[128];
        for(int i=0; i < blocks.length; i++) {
            blocks[i] = emptyBlock;
        }
        return new MapBlockset(blocks, blocksPerRow);
    }
}
