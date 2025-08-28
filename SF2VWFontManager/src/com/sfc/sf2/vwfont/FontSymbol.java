/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont;

import com.sfc.sf2.palette.CRAMColor;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 *
 * @author TiMMy
 */
public class FontSymbol {
    
    private static final Palette DEFAULT_PALETTE = new Palette(new CRAMColor[] { CRAMColor.WHITE, CRAMColor.BLACK, CRAMColor.LIGHT_GRAY }, true);
    public static final int PIXEL_WIDTH = 16;
    public static final int PIXEL_HEIGHT = 16;
    
    private int id;
    private int width;
    private int[] pixels = new int[PIXEL_WIDTH*PIXEL_HEIGHT];
    private BufferedImage indexedColorImage = null;
    private Palette palette;

    public FontSymbol(int id, int[] pixels, int width) {
        this.id = id;
        this.pixels = pixels;
        this.width = width;
        palette = DEFAULT_PALETTE;
    }
    
    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }
    
    public Palette getPalette() {
        return palette;
    }
    
    public void setpalette(Palette palette) {
        if (this.palette != palette) {
            this.palette = palette;
            clearIndexedColorImage();
        }
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public BufferedImage getIndexColoredImage() {
        if(indexedColorImage == null) {
            indexedColorImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED, palette.getIcm());
            byte[] data = ((DataBufferByte)(indexedColorImage.getRaster().getDataBuffer())).getData();
            for (int j = 0; j < PIXEL_HEIGHT; j++) {
                for (int i = 0; i < PIXEL_WIDTH; i++) {
                    data[i+j*PIXEL_WIDTH] = (byte)pixels[i+j*PIXEL_WIDTH];
                }
            }
        }
        return indexedColorImage;        
    }
    
    public void clearIndexedColorImage() {
        if (indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
        }
    }
    
    private static final int[] EMPTY_PIXELS = new int[PIXEL_WIDTH*PIXEL_HEIGHT];
    public static FontSymbol EmptySymbol() {
        FontSymbol emptySymbol = new FontSymbol(-1, EMPTY_PIXELS, 2);
        return emptySymbol;
    }
}
