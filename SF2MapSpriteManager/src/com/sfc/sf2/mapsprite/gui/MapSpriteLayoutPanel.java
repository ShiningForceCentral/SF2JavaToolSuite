/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.mapsprite.MapSprite;
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
        setGridDimensions(8, 8, 48, 24);
        setCoordsDimensions(24, 24, 6);
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
    protected void buildImage(Graphics graphics) {
        for (int i = 0; i < mapsprites.length; i++) {
            if (mapsprites[i] != null) {
                graphics.drawImage(mapsprites[i].getIndexedColorImage(), 0, i*3*PIXEL_HEIGHT, null);
            }
        }
    }

    public MapSprite[] getMapSprite() {
        return mapsprites;
    }

    public void setMapSprite(MapSprite[] mapsprites) {
        this.mapsprites = mapsprites;
    }
}
