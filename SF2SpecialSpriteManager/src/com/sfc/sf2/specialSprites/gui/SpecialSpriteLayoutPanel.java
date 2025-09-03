/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.specialSprites.gui;

import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.gui.TilesetLayoutPanel;

/**
 *
 * @author TiMMy
 */
public class SpecialSpriteLayoutPanel extends TilesetLayoutPanel {
    
    @Override
    public void setTileset(Tileset tileset) {
        super.setTileset(tileset);
    }
    
    @Override
    public void setItemsPerRow(int itemsPerRow) {
        if (getItemsPerRow() != itemsPerRow) {
            super.setItemsPerRow(itemsPerRow);
            if (getTileset() != null && BaseLayoutComponent.IsEnabled(grid)) {
                grid.setThickGridDimensions(getTileset().getTilesPerRow()/2*PIXEL_WIDTH, -1);
            }
        }
    }
}
