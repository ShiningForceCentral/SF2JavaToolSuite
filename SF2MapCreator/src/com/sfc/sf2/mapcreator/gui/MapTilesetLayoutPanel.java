/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.MapBlockHelpers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class MapTilesetLayoutPanel extends AbstractLayoutPanel {
    private Tileset[] tilesets;
    int renderHeight;
    
    public MapTilesetLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/2);
        scale = new LayoutScale();
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, -1, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, true, 0, PIXEL_WIDTH, 0);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
    }

    @Override
    protected Dimension getImageDimensions() {
        if (tilesets == null) {
            return new Dimension();
        } else {
            Dimension d;
            if (tilesets[0] == null) {
                d = tilesets[4].getDimensions(getItemsPerRow());
            } else {
                d = tilesets[0].getDimensions(getItemsPerRow());
            }
            d.height *= tilesets.length;
            return d;
        }
    }

    @Override
    protected boolean hasData() {
        //Also check last tileset, because it contains chest graphics
        return tilesets != null && ((tilesets[0] != null && tilesets[0].getTiles().length > 0) || (tilesets[4] != null && tilesets[4].getTiles().length > 0));
    }

    @Override
    protected void drawImage(Graphics graphics) {
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesets[i] != null) {
                tilesets[i].clearIndexedColorImage(false);
                graphics.drawImage(tilesets[i].getIndexedColorImage(), 0, renderHeight*i, null);
            }
        }
    }
    
    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
        redraw();
    }

    @Override
    public void setItemsPerRow(int itemsPerRow) {
        super.setItemsPerRow(itemsPerRow);
        int renderWidth = itemsPerRow*PIXEL_WIDTH;
        renderHeight = MapBlockHelpers.TILESET_TILES/itemsPerRow*PIXEL_HEIGHT;
        grid.setThickGridDimensions(-1, renderHeight);
        coordsGrid.setDisplaySizes(renderWidth, renderHeight);
        coordsHeader.setGrid(renderWidth, renderHeight);
    }

    @Override
    public void setShowGrid(boolean showGrid) {
        if (grid != null) {
            if (showGrid) {
                grid.setGridDimensions(PIXEL_WIDTH, PIXEL_HEIGHT);
            } else {
                grid.setGridDimensions(-1, -1);
            }
            redraw();
        }
    }
}
