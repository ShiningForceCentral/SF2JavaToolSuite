/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractBasicPanel;
import com.sfc.sf2.graphics.Tile;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class TileSlotPanel extends AbstractBasicPanel {
    
    Tile tile;

    @Override
    protected boolean hasData() {
        return tile != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return getSize();
    }

    @Override
    protected void paintImage(Graphics graphics) {
        graphics.drawImage(tile.getIndexedColorImage(), 0, 0, this.getWidth(), this.getHeight(), null);
    }
    
    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
        redraw();
        this.repaint();
    }
}
