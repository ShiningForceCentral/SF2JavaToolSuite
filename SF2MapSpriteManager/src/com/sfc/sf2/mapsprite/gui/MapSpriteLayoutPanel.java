/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.mapsprite.MapSprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class MapSpriteLayoutPanel extends AbstractLayoutPanel {
        
    private MapSprite[] mapsprites;
    
    public MapSpriteLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, Block.PIXEL_WIDTH, Block.PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(Block.PIXEL_WIDTH, Block.PIXEL_HEIGHT, false, 0, PIXEL_WIDTH, 1);
        coordsHeader = new LayoutCoordsHeader(this, Block.PIXEL_WIDTH, Block.PIXEL_HEIGHT);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
    }

    @Override
    protected boolean hasData() {
        return mapsprites != null && mapsprites.length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        if (mapsprites.length == 1) {
            return new Dimension(mapsprites[0].getSpritesWidth()*3*PIXEL_WIDTH, mapsprites[0].getSpritesHeight()*3*PIXEL_HEIGHT);
        } else {
            int w = 6*3*PIXEL_WIDTH;    //6 sprites per mapsprite (2x up, 2x left, 2x down)
            int h = mapsprites.length*3*PIXEL_HEIGHT;
            return new Dimension(w, h);
        }
    }

    @Override
    protected void drawImage(Graphics graphics) {
        for (int i = 0; i < mapsprites.length; i++) {
            if (mapsprites[i] != null) {
                graphics.drawImage(mapsprites[i].getIndexedColorImage(), 0, i*3*PIXEL_HEIGHT, null);
            }
        }
    }

    public MapSprite[] getMapSprites() {
        return mapsprites;
    }

    public void setMapSprites(MapSprite[] mapsprites) {
        this.mapsprites = mapsprites;
    }
}
