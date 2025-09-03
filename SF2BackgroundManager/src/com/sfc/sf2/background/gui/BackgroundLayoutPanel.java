/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background.gui;

import com.sfc.sf2.background.Background;
import static com.sfc.sf2.background.Background.BG_TILES_HEIGHT;
import static com.sfc.sf2.background.Background.BG_TILES_WIDTH;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.LayoutCoordsGridDisplay;
import com.sfc.sf2.core.gui.layout.LayoutGrid;
import com.sfc.sf2.core.gui.layout.LayoutScale;
import com.sfc.sf2.core.gui.layout.LayoutScrollNormaliser;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class BackgroundLayoutPanel extends AbstractLayoutPanel {
    
    private Background[] backgrounds;
    
    public BackgroundLayoutPanel() {
        background = null;
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, -1, BG_TILES_HEIGHT*PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(0, BG_TILES_HEIGHT*PIXEL_HEIGHT, false, 0, PIXEL_WIDTH, 2);
        coordsHeader = null;
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
        setItemsPerRow(BG_TILES_WIDTH);
    }

    @Override
    protected boolean hasData() {
        return backgrounds != null && backgrounds.length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        return  new Dimension(BG_TILES_WIDTH*PIXEL_WIDTH, BG_TILES_HEIGHT*backgrounds.length*PIXEL_HEIGHT);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        for(int b = 0; b < backgrounds.length; b++) {
            graphics.drawImage(backgrounds[b].getTileset().getIndexedColorImage(), 0, b*BG_TILES_HEIGHT*PIXEL_HEIGHT, null);
        }
    }
    
    public Background[] getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(Background[] backgrounds) {
        this.backgrounds = backgrounds;
        redraw();
    }
}
