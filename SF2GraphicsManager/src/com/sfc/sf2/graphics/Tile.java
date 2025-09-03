/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;

/**
 *
 * @author wiz
 */
public class Tile {
    
    public static final int PIXEL_WIDTH = 8;
    public static final int PIXEL_HEIGHT = 8;
    
    private int id;
    private Palette palette;
    protected int[] pixels = new int[PIXEL_WIDTH*PIXEL_HEIGHT];
    private BufferedImage indexedColorImage = null;
    
    private boolean highPriority = false;
    private boolean hFlip = false;
    private boolean vFlip = false;
        
    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    public boolean ishFlip() {
        return hFlip;
    }

    public void sethFlip(boolean hFlip) {
        this.hFlip = hFlip;
        clearIndexedColorImage();
    }

    public boolean isvFlip() {
        return vFlip;
    }

    public void setvFlip(boolean vFlip) {
        this.vFlip = vFlip;
        clearIndexedColorImage();
    }
    
    public static Tile vFlip(Tile tile) {
        return tile.clone(tile.isHighPriority(), tile.ishFlip(), !tile.isvFlip(), tile.getPalette());
    }
    
    public static Tile hFlip(Tile tile) {
        return tile.clone(tile.isHighPriority(), !tile.ishFlip(), tile.isvFlip(), tile.getPalette());
    }
    
    public static Tile clearFlip(Tile tile) {
        return tile.clone(tile.isHighPriority(), false, false, tile.getPalette());
    }
    
    public static Tile paletteSwap(Tile tile, Palette palette) {
        return tile.clone(tile.isHighPriority(), tile.ishFlip(), tile.isvFlip(), palette);
    }
    
    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        clearIndexedColorImage();
    }

    public IndexColorModel getIcm() {
        if (palette == null) {
            return null;
        } else {
            return palette.getIcm();
        }
    }

    public BufferedImage getIndexedColorImage() {
        if (indexedColorImage == null) {
            IndexColorModel icm = palette.getIcm();
            indexedColorImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED, icm);
            byte[] data = ((DataBufferByte)(indexedColorImage.getRaster().getDataBuffer())).getData();
            drawPixelData(data);
        }
        return indexedColorImage;        
    }
    
    private void drawPixelData(byte[] imageData) {
        if (ishFlip() || isvFlip()) {
            for (int i=0; i < pixels.length; i++) {
                int index = (ishFlip() ? PIXEL_WIDTH-(i%PIXEL_WIDTH)-1 : i%PIXEL_WIDTH) + (isvFlip()? PIXEL_HEIGHT-(i/PIXEL_HEIGHT)-1 : i/PIXEL_HEIGHT)*PIXEL_WIDTH;
                imageData[i] = (byte)(pixels[index]);
            }
        } else {
            for (int i=0; i < pixels.length; i++) {
                imageData[i] = (byte)(pixels[i]);
            }
        }
    }
    
    public void clearIndexedColorImage() {
        if (indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
        }
    }
    
    public void drawIndexedColorPixels(BufferedImage image, int[][] pixels, int x, int y){
    }
    
    public void setPixel(int x, int y, int colorIndex){
        this.pixels[x+y*PIXEL_WIDTH] = colorIndex;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tile " + id +" :\n");
        for(int y=0;y<PIXEL_HEIGHT;y++){
            for(int x=0;x<PIXEL_WIDTH;x++){
                sb.append("\t"+pixels[x+y*PIXEL_HEIGHT]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Tile)) return false;
        Tile tile = (Tile)obj;
        if (tile.id != this.id || tile.vFlip != this.vFlip || tile.hFlip != this.hFlip) {
            return false;
        }
        for (int i=0; i < pixels.length; i++) {
            if (this.pixels[i] != tile.pixels[i]) {
                return false;
            }
        }
        return true;
    }
    
    public boolean equalsWithPriority(Object obj) {
        boolean equals = equals(obj);
        if (equals) {
            Tile tile = (Tile)obj;
            equals = this.highPriority == tile.isHighPriority();
        }
        return equals;
    }
    
    @Override
    public Tile clone() {
        return clone(isHighPriority(), ishFlip(), isvFlip(), getPalette());
    }
    
    public Tile clone(boolean priority, boolean hFlip, boolean vFlip, Palette newPalette) {
        Tile newTile = new Tile();
        newTile.setId(getId());
        newTile.setPalette(newPalette);
        newTile.setPixels(getPixels());
        newTile.setHighPriority(priority);
        newTile.sethFlip(hFlip);
        newTile.setvFlip(vFlip);
        return newTile;
    }
    
    public boolean isTileEmpty() {
        if (pixels == null) {
            return true;
        }
        for (int i=0; i < pixels.length; i++) {
            if (pixels[i] > 0) {
                return false;
            }
        }
        return true;
    }
    
    public static Tile EmptyTile(Palette palette) {
        Tile emptyTile = new Tile();
        emptyTile.setPalette(palette);
        emptyTile.setPixels(new int[PIXEL_WIDTH*PIXEL_HEIGHT]);
        return emptyTile;
    }
}
