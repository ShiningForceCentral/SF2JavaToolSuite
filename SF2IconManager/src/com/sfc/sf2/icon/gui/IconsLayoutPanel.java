/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.icon.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.icon.Icon;
import java.awt.Dimension;
import java.awt.Graphics;
import static com.sfc.sf2.icon.Icon.ICON_TILE_HEIGHT;
import static com.sfc.sf2.icon.Icon.ICON_TILE_WIDTH;
import java.awt.Color;

/**
 *
 * @author TiMMy
 */
public class IconsLayoutPanel extends AbstractLayoutPanel {

    Icon[] icons;
    
    public IconsLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/2);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(ICON_TILE_WIDTH*PIXEL_WIDTH, ICON_TILE_HEIGHT*PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(ICON_TILE_WIDTH*PIXEL_WIDTH, ICON_TILE_HEIGHT*PIXEL_HEIGHT, true);
        coordsHeader = new LayoutCoordsHeader(this, ICON_TILE_WIDTH*PIXEL_WIDTH, ICON_TILE_HEIGHT*PIXEL_HEIGHT, true);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
    }
    
    @Override
    protected boolean hasData() {
        return icons != null && icons.length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        int iconsPerRow = this.getItemsPerRow();
        int width = icons.length > iconsPerRow ? iconsPerRow : icons.length;
        int height = icons.length/iconsPerRow;
        if (icons.length%iconsPerRow != 0) {
            height++;
        }
        return new Dimension(width*ICON_TILE_WIDTH*PIXEL_WIDTH, height*ICON_TILE_HEIGHT*PIXEL_HEIGHT);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        int iconsPerRow = this.getItemsPerRow();
        int width = icons.length > iconsPerRow ? iconsPerRow : icons.length;
        for (int i = 0; i < icons.length; i++) {
            int x = (i%width)*ICON_TILE_WIDTH*PIXEL_WIDTH;
            int y = (i/width)*ICON_TILE_HEIGHT*PIXEL_HEIGHT;
            graphics.drawImage(icons[i].getTileset().getIndexedColorImage(), x, y, this);
        }
    }
    
    public Icon[] getIcons() {
        return icons;
    }
    
    public void setIcons(Icon[] icons) {
        this.icons = icons;
        redraw();
    }
}
