/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class Portrait implements INameable {
    
    public static final int PORTRAIT_TILES_FULL_WIDTH = 8;
    public static final int PORTRAIT_TILES_WIDTH = 6;
    public static final int PORTRAIT_TILES_HEIGHT = 8;
    
    private int index;
    private String name;
    private Tileset tileset;
    private BufferedImage indexedColorImage = null;
    
    private int[][] eyeTiles;    
    private int[][] mouthTiles;
    
    public Portrait(int index, String name, Tileset tileset) {
        this(index, name, tileset, null, null);
    }
    
    public Portrait(int index, String name, Tileset tileset, int[][] eyeTiles, int[][] mouthTiles) {
        this.index = index;
        this.name = name;
        this.tileset = tileset;
        this.eyeTiles = eyeTiles;
        this.mouthTiles = mouthTiles;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
        clearIndexedColorImage();
    }
    
    public int[][] getEyeTiles() {
        return eyeTiles;
    }

    public void setEyeTiles(int[][] eyeTiles) {
        this.eyeTiles = eyeTiles;
    }

    public int[][] getMouthTiles() {
        return mouthTiles;
    }

    public void setMouthTiles(int[][] mouthTiles) {
        this.mouthTiles = mouthTiles;
    }
    
    public Palette getPalette() {
        if (tileset == null) {
            return null;
        }
        return tileset.getPalette();
    }
    
    public BufferedImage getIndexedColorImage(boolean showfullImage, boolean blinking, boolean speaking) {
        if (tileset == null || tileset.getTiles().length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            Tile[] tiles = tileset.getTiles();
            int width = showfullImage ? PORTRAIT_TILES_FULL_WIDTH : PORTRAIT_TILES_WIDTH;
            indexedColorImage = new BufferedImage(width*PIXEL_WIDTH, PORTRAIT_TILES_HEIGHT*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for(int j=0;j<PORTRAIT_TILES_HEIGHT;j++){
                for(int i=0;i<width;i++){
                    int tileID = i+j*8;
                    if (blinking) {
                        int[][] eyeTableData = getEyeTiles();
                        for (int b = 0; b < eyeTableData.length; b++) {
                            if (eyeTableData[b][0] == i && eyeTableData[b][1] == j) {
                                tileID = eyeTableData[b][2]+eyeTableData[b][3]*8;
                                break;
                            }
                        }
                    }
                    if (speaking) {
                        int[][] mouthTableData = getMouthTiles();
                        for (int m = 0; m < mouthTableData.length; m++) {
                            if (mouthTableData[m][0] == i && mouthTableData[m][1] == j) {
                                tileID = mouthTableData[m][2]+mouthTableData[m][3]*8;
                                break;
                            }
                        }
                    }
                    graphics.drawImage(tiles[tileID].getIndexedColorImage(), i*PIXEL_WIDTH, j*PIXEL_HEIGHT, null);
                }
            }
            graphics.dispose();
        }
        return indexedColorImage;
    }
    
    public void clearIndexedColorImage() {
        indexedColorImage = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Portrait)) return super.equals(obj);
        Portrait other = (Portrait)obj;
        if (this.index != other.index || !this.tileset.equals(other.tileset)) return false;
        if (!Arrays.deepEquals(this.eyeTiles, other.eyeTiles)) return false;
        if (!Arrays.deepEquals(this.mouthTiles, other.mouthTiles)) return false;
        return true;
    }
}
