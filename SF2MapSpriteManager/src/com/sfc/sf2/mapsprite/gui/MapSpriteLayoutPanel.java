/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.mapsprite.MapSpriteEntries;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class MapSpriteLayoutPanel extends AbstractLayoutPanel {
    private static Color LABEL_BG = Color.YELLOW;
    private static Font LABEL_FONT = new Font(Font.DIALOG, 0, 9);
        
    private MapSpriteEntries mapsprites;
    private boolean drawReferenceLabels;
    
    public MapSpriteLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, Tile.PIXEL_WIDTH);
        scale = new LayoutScale();
        grid = new LayoutGrid(Tile.PIXEL_WIDTH, Tile.PIXEL_HEIGHT, PIXEL_WIDTH*2, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH*2, PIXEL_HEIGHT, false, 0, Tile.PIXEL_WIDTH, 1);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH*2, PIXEL_HEIGHT, false);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
        setItemsPerRow(3);
    }

    @Override
    protected boolean hasData() {
        return mapsprites != null && mapsprites.getMapSpritesArray().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        if (mapsprites.getMapSpritesArray().length == 1) {
            if (mapsprites.getMapSpritesArray()[0] == null) {
                return new Dimension();
            } else {
                return new Dimension(mapsprites.getMapSpritesArray()[0].getSpritesWidth()*PIXEL_WIDTH, mapsprites.getMapSpritesArray()[0].getSpritesHeight()*PIXEL_HEIGHT);
            }
        } else {
            int w = 6*PIXEL_WIDTH;    //6 sprites per mapsprite (2x up, 2x left, 2x down)
            int h = mapsprites.getEntriesArray().length/3*PIXEL_HEIGHT;
            if (mapsprites.getEntriesArray().length%3 != 0)
                h += PIXEL_HEIGHT;
            return new Dimension(w, h);
        }
    }

    @Override
    protected void drawImage(Graphics graphics) {
        if (drawReferenceLabels) {
            graphics.setFont(LABEL_FONT);
        }
        int[] entries = mapsprites.getEntriesArray();
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] >= 0) {
                MapSprite sprite = mapsprites.getMapSprite(i);
                if (sprite != null) {
                    graphics.drawImage(sprite.getIndexedColorImage(), (i%3)*2*PIXEL_WIDTH, (i/3)*PIXEL_HEIGHT, null);
                }
                if (drawReferenceLabels && mapsprites.isDuplicateEntry(i)) {
                    graphics.setColor(LABEL_BG);
                    graphics.drawRect(((i%3)*2)*PIXEL_WIDTH, (i/3)*PIXEL_HEIGHT, 2*PIXEL_WIDTH-1, PIXEL_HEIGHT-1);
                    graphics.fillRect(((i%3)*2+1)*PIXEL_WIDTH, (i/3)*PIXEL_HEIGHT+14, PIXEL_WIDTH, 9);
                    graphics.setColor(Color.BLACK);
                    graphics.drawString(String.format("%03d-%01d", entries[i]/3, entries[i]%3), ((i%3)*2+1)*PIXEL_WIDTH+1, (i/3)*PIXEL_HEIGHT+22);
                }
            }
        }
    }

    public MapSpriteEntries getMapSprites() {
        return mapsprites;
    }

    public void setMapSprites(MapSpriteEntries mapsprites) {
        this.mapsprites = mapsprites;
        redraw();
    }

    public void setDrawReferenceLabels(boolean drawReferenceLabels) {
        this.drawReferenceLabels = drawReferenceLabels;
        redraw();
    }
}
