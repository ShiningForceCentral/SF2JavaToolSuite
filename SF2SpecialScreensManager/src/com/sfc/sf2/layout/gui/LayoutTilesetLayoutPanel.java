/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.layout.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.layout.SpecialScreenLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class LayoutTilesetLayoutPanel extends AbstractLayoutPanel { 
    
    private SpecialScreenLayout layout;
    
    public LayoutTilesetLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH);
        scale = new LayoutScale();
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, false, 0, PIXEL_WIDTH, 0);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
    }

    @Override
    protected Dimension getImageDimensions() {
        return layout.getDimensions(getItemsPerRow());
    }

    @Override
    protected boolean hasData() {
        return layout != null && layout.getLayout().length > 0 && layout.getTilesets() != null;
    }

    @Override
    protected void drawImage(Graphics graphics) {
        layout.clearIndexedColorImage(false);
        graphics.drawImage(layout.getIndexedColorImage(), 0, 0, null);
    }

    public SpecialScreenLayout getLayoutTileset() {
        return layout;
    }

    public void setLayoutTileset(SpecialScreenLayout layout) {
        this.layout = layout;
        redraw();
    }
}
