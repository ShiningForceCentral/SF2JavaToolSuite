/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapTile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class TileSlotPanel extends AbstractLayoutPanel {
    
    MapTile tile;
    Tileset[] tilesets;
    
    public TileSlotPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/2);
        scale = new LayoutScale(4);
        grid = null;
        coordsGrid = null;
        coordsHeader = null;
        mouseInput = null;
        scroller = null;
    }

    @Override
    protected boolean hasData() {
        return tile != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return new Dimension(PIXEL_WIDTH, PIXEL_HEIGHT);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        graphics.drawImage(tile.getIndexedColorImage(tilesets), 0, 0, null);
    }
    
    public MapTile getTile() {
        return tile;
    }

    public void setTile(MapTile tile) {
        this.tile = tile;
        redraw();
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }
}
