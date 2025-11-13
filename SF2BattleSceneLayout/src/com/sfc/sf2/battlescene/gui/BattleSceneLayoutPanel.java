/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlescene.gui;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.ground.Ground;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author wiz
 */
public class BattleSceneLayoutPanel extends AbstractLayoutPanel {
    
    private static final Dimension IMAGE_SIZE = new Dimension(256, 224);
    
    protected static final int BACKGROUND_BASE_X = 0;
    protected static final int BACKGROUND_BASE_Y = 56;
    protected static final int GROUND_BASE_X = 136;
    protected static final int GROUND_BASE_Y = 140;
    protected static final int BATTLESPRITE_ALLY_BASE_X = 136;
    protected static final int BATTLESPRITE_ALLY_BASE_Y = 64;
    protected static final int BATTLESPRITE_ENEMY_BASE_X = 0;
    protected static final int BATTLESPRITE_ENEMY_BASE_Y = 56;
    protected static final int BATTLESPRITE_INVOCATION_BASE_X = 120;
    protected static final int BATTLESPRITE_INVOCATION_BASE_Y = 40;
    protected static final int WEAPONSPRITE_BASE_X = 136;
    protected static final int WEAPONSPRITE_BASE_Y = 64;
    
    private Background bg;
    private Ground ground;
    
    private boolean showPositions = false;
    
    public BattleSceneLayoutPanel() {
        super();
        background = new LayoutBackground(Color.BLACK);
        scale = new LayoutScale(1);
        grid = null;
        coordsGrid = null;
        coordsHeader = null;
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
        animator = null;
    }

    @Override
    protected boolean hasData() {
        return bg != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return IMAGE_SIZE;
    }

    @Override
    protected void drawImage(Graphics graphics) {
        graphics.drawImage(bg.getTileset().getIndexedColorImage(), BACKGROUND_BASE_X, BACKGROUND_BASE_Y, null);
        graphics.drawImage(ground.getTileset().getIndexedColorImage(), GROUND_BASE_X, GROUND_BASE_Y, null);
        if (!showPositions) return;
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.GRAY);
        g2.drawRect(WEAPONSPRITE_BASE_X+1, WEAPONSPRITE_BASE_Y+1, 8*PIXEL_WIDTH, 8*PIXEL_HEIGHT);
        g2.setColor(Color.WHITE);
        g2.drawRect(BATTLESPRITE_INVOCATION_BASE_X, BATTLESPRITE_INVOCATION_BASE_Y, 16*PIXEL_WIDTH, 16*PIXEL_HEIGHT);
        g2.setColor(Color.YELLOW);
        g2.drawRect(BATTLESPRITE_ALLY_BASE_X, BATTLESPRITE_ALLY_BASE_Y, 12*PIXEL_WIDTH, 12*PIXEL_HEIGHT);
        g2.setColor(Color.RED);
        g2.drawRect(BATTLESPRITE_ENEMY_BASE_X, BATTLESPRITE_ENEMY_BASE_Y, 16*PIXEL_WIDTH, 12*PIXEL_HEIGHT);
    }

    public void setBackground(Background background) {
        this.bg = background;
        redraw();
    }

    public void setGround(Ground ground) {
        this.ground = ground;
        redraw();
    }

    public boolean shouldShowPositions() {
        return showPositions;
    }

    public void setShowPositions(boolean showPositions) {
        this.showPositions = showPositions;
        redraw();
    }
}
