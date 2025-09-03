/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.spellGraphic.InvocationGraphic;
import static com.sfc.sf2.spellGraphic.InvocationGraphic.INVOCATION_TILE_HEIGHT;
import static com.sfc.sf2.spellGraphic.InvocationGraphic.INVOCATION_TILE_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class InvocationLayoutPanel extends AbstractLayoutPanel {
    
    private InvocationGraphic invocationGraphic;
    
    public InvocationLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, -1, INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(0, INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT, false, 0, 0, 2);
        coordsHeader = null;
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
    }

    @Override
    protected boolean hasData() {
        return invocationGraphic != null && invocationGraphic.getFrames().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        int width = invocationGraphic.getFrameWidth()*PIXEL_WIDTH;
        int height = invocationGraphic.getTotalHeight()*PIXEL_HEIGHT;
        return new Dimension(width, height);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        for(int f = 0; f < invocationGraphic.getFrames().length; f++) {
            Tile[] frameTiles = invocationGraphic.getFrames()[f].getTiles();
            int yy = f*INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT;
            for(int t = 0; t < frameTiles.length; t++) {
                int x = (t%INVOCATION_TILE_WIDTH)*PIXEL_WIDTH;
                int y = yy + t/INVOCATION_TILE_WIDTH*PIXEL_HEIGHT;
                graphics.drawImage(frameTiles[t].getIndexedColorImage(), x, y, null);
            }
        }
        graphics.dispose();
    }

    public InvocationGraphic getInvocationGraphic() {
        return invocationGraphic;
    }

    public void setInvocationGraphic(InvocationGraphic invocationGraphic) {
        this.invocationGraphic = invocationGraphic;
        redraw();
    }
}
