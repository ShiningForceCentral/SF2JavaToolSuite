/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.layout;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author TiMMy
 */
public class SpecialScreenLayout implements INameable {
    
    private final String name;
    private Palette[] palettes;
    private Tileset[] tilesets;     //Tileset[0] should be the base tileset
    private LayoutTile[] layout;
    private int currentPalette;
    private int tilesPerRow;
    
    private BufferedImage indexedColorImage = null;
    
    public SpecialScreenLayout(String name, Palette[] palettes, Tileset[] tilesets, LayoutTile[] layout, int tilesPerRow) {
        this.name = name;
        this.palettes = palettes;
        this.tilesets = tilesets;
        this.layout = layout;
        this.currentPalette = 0;
        this.tilesPerRow = tilesPerRow;
    }

    @Override
    public String getName() {
        return name;
    }

    public Palette[] getPalettes() {
        return palettes;
    }

    public void setPalettes(Palette[] palettes) {
        this.palettes = palettes;
        if (currentPalette >= palettes.length) {
            currentPalette = 0;
        }
        clearIndexedColorImage(true);
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }

    public LayoutTile[] getLayout() {
        return layout;
    }

    public void setLayout(LayoutTile[] layout) {
        this.layout = layout;
    }

    public int getCurrentPalette() {
        return currentPalette;
    }

    public void setCurrentPalette(int currentPalette) {
        if (currentPalette < 0 || currentPalette >= palettes.length) {
            currentPalette = 0;
        }
        if (this.currentPalette != currentPalette) {
            clearIndexedColorImage(true);
        }
        this.currentPalette = currentPalette;
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
        int h = layout.length/tilesPerRow;
        if (layout.length%tilesPerRow != 0) {
            h++;
        }
        return new Dimension(w*PIXEL_WIDTH, h*PIXEL_HEIGHT);
    }
    
    public BufferedImage getIndexedColorImage() {
        if (layout == null || layout.length == 0) {
            return null;
        }
        if (indexedColorImage == null) {
            int width = tilesPerRow;
            int height = layout.length/tilesPerRow;
            if (layout.length%tilesPerRow != 0)
                height++;
            Palette palette = currentPalette >= palettes.length ? palettes[0] : palettes[currentPalette];
            indexedColorImage = new BufferedImage(width*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for(int j=0;j<height;j++){
                for(int i=0;i<width;i++){
                    int layoutIndex = i+j*width;
                    if (layoutIndex >= layout.length) {
                        break;
                    }
                    Tile tile = layout[layoutIndex].getTile(tilesets);
                    if (tile != null)
                        tile.setPalette(palette);
                        graphics.drawImage(tile.getIndexedColorImage(layout[layoutIndex].getTileFlags()), i*PIXEL_WIDTH, j*PIXEL_HEIGHT, null);   
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
        if (tilesets != null) {
            for (int i = 0; i < tilesets.length; i++) {
                tilesets[i].clearIndexedColorImage(alsoClearTiles);
            }
        }
    }
    
    public void insertTile(int index, LayoutTile tile, boolean cloneTile) {
        if (index < 0 || index > layout.length) return;
        LayoutTile[] newTiles = new LayoutTile[layout.length+1];
        System.arraycopy(layout, 0, newTiles, 0, index);
        newTiles[index] = tile;
        if (cloneTile) {
            newTiles[index] = tile.clone();
        }
        newTiles[index].setTileIndex(index);
        for (int i = index+1; i < newTiles.length; i++) {
            newTiles[i] = layout[i-1];
            newTiles[i].setTileIndex(i);
        }
        layout = newTiles;
    }
    
    public void removeTile(int index) {
        if (index < 0 || index >= layout.length) return;
        LayoutTile[] newTiles = new LayoutTile[layout.length-1];
        System.arraycopy(layout, 0, newTiles, 0, index);
        for (int i = index; i < newTiles.length; i++) {
            newTiles[i] = layout[i+1];
            newTiles[i].setTileIndex(i);
        }
        layout = newTiles;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpecialScreenLayout)) return super.equals(obj);
        SpecialScreenLayout tileset = (SpecialScreenLayout)obj;
        if (!Arrays.equals(this.layout, tileset.layout)) return false;
        return true;
    }
    
    public SpecialScreenLayout clone() {
        SpecialScreenLayout layout = new SpecialScreenLayout(this.name, this.palettes, this.tilesets, this.layout.clone(), this.tilesPerRow);
        layout.currentPalette = this.currentPalette;
        return layout;
    }
    
    public boolean isLayoutEmpty() {
        if (layout == null || layout.length == 0) {
            return true;
        }
        for (int i = 0; i < layout.length; i++) {
            if (!layout[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
