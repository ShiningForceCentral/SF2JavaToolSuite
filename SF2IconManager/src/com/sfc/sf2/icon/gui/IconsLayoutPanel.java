/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.icon.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.icon.Icon;
import java.awt.Dimension;
import java.awt.Graphics;
import static com.sfc.sf2.icon.Icon.ICON_TILE_HEIGHT;
import static com.sfc.sf2.icon.Icon.ICON_TILE_WIDTH;

/**
 *
 * @author TiMMy
 */
public class IconsLayoutPanel extends AbstractLayoutPanel {

    Icon[] icons;
    
    public IconsLayoutPanel() {
        super();
        setGridDimensions(ICON_TILE_WIDTH*PIXEL_WIDTH, ICON_TILE_HEIGHT*PIXEL_HEIGHT);
        setCoordsDimensions(ICON_TILE_WIDTH*PIXEL_WIDTH, ICON_TILE_HEIGHT*PIXEL_HEIGHT, 6);
    }
    
    @Override
    protected boolean hasData() {
        return icons != null && icons.length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        int iconsPerRow = this.tilesPerRow;
        int width = icons.length > iconsPerRow ? iconsPerRow : icons.length;
        int height = icons.length/iconsPerRow;
        if (icons.length%iconsPerRow != 0) {
            height++;
        }
        return new Dimension(width*ICON_TILE_WIDTH*PIXEL_WIDTH, height*ICON_TILE_HEIGHT*PIXEL_HEIGHT);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        int iconsPerRow = this.tilesPerRow;
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
    }
}
