/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import static com.sfc.sf2.weaponsprite.WeaponSprite.FRAME_TILE_HEIGHT;
import static com.sfc.sf2.weaponsprite.WeaponSprite.FRAME_TILE_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class WeaponSpriteLayoutPanel extends AbstractLayoutPanel {
    
    private WeaponSprite weaponsprite;
    private Palette palette;
    
    public WeaponSpriteLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, -1, FRAME_TILE_HEIGHT*PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(0, FRAME_TILE_HEIGHT*PIXEL_HEIGHT, false, 0, 0, 2);
        coordsHeader = null;
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
        setItemsPerRow(FRAME_TILE_WIDTH);
    }
    
    @Override
    protected boolean hasData() {
        return weaponsprite != null && palette != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return new Dimension(getItemsPerRow()*PIXEL_WIDTH, weaponsprite.getFrames().length*FRAME_TILE_HEIGHT*PIXEL_HEIGHT);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        
        int frameHeight = FRAME_TILE_HEIGHT*PIXEL_HEIGHT;
        Tileset[] frames = weaponsprite.getFrames();
        for(int f = 0; f < weaponsprite.getFrames().length; f++) {
            frames[f].setPalette(palette);
            graphics.drawImage(frames[f].getIndexedColorImage(), 0, f*frameHeight, null);
        }
    }
    
    public WeaponSprite getWeaponSprite() {
        return weaponsprite;
    }

    public void setWeaponSprite(WeaponSprite weaponsprite) {
        this.weaponsprite = weaponsprite;
        redraw();
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        redraw();
    }
}
