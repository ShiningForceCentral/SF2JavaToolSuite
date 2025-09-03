/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class TilesetLayoutPanel extends AbstractLayoutPanel { 
    
    private Tileset tileset;
    
    public TilesetLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = null;
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
    }

    @Override
    protected Dimension getImageDimensions() {
        return tileset.getDimensions(getItemsPerRow());
    }

    @Override
    protected boolean hasData() {
        return tileset != null && tileset.getTiles().length > 0;
    }

    @Override
    protected void drawImage(Graphics graphics) {
        tileset.clearIndexedColorImage(false);
        graphics.drawImage(tileset.getIndexedColorImage(), 0, 0, null);
    }
    
    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
        redraw();
    }
}
