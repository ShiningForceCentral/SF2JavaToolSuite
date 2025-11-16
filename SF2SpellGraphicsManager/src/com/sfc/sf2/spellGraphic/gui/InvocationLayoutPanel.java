/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.gui;

import com.sfc.sf2.battlescene.gui.BattleSceneLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
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
public class InvocationLayoutPanel extends BattleSceneLayoutPanel {
    private static int BATTLE_POSITION_OFFSET = 100;
    
    private InvocationGraphic invocationGraphic;
    
    private boolean battlePreviewMode = false;
    private int previousScale;
    
    public InvocationLayoutPanel() {
        super();
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, -1, INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(0, INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT, false, 0, 0, 2);
        setBattlePreviewMode(battlePreviewMode);
    }

    @Override
    protected boolean hasData() {
        return invocationGraphic != null && invocationGraphic.getFrames() != null && invocationGraphic.getFrames().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        if (battlePreviewMode) {
            return new Dimension(500, 500);
        } else {
            int width = INVOCATION_TILE_WIDTH*PIXEL_WIDTH;
            int height = invocationGraphic.getFrames().length*INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT;
            return new Dimension(width, height);
        }
    }

    @Override
    protected void drawImage(Graphics graphics) {
        if (battlePreviewMode) {
            graphics.drawImage(bg.getTileset().getIndexedColorImage(), BATTLE_POSITION_OFFSET+BACKGROUND_BASE_X, BATTLE_POSITION_OFFSET+BACKGROUND_BASE_Y, null);
            graphics.drawImage(ground.getTileset().getIndexedColorImage(), BATTLE_POSITION_OFFSET+GROUND_BASE_X, BATTLE_POSITION_OFFSET+GROUND_BASE_Y, null);
            Tileset[] frames = invocationGraphic.getFrames();
            int x = BATTLE_POSITION_OFFSET+BATTLESPRITE_INVOCATION_BASE_X+invocationGraphic.getPosX();
            int y = BATTLE_POSITION_OFFSET+BATTLESPRITE_INVOCATION_BASE_Y+invocationGraphic.getPosY();
            int maxFrame = frames.length-2;
            graphics.drawImage(frames[maxFrame+0].getIndexedColorImage(), x, y, null);
            graphics.drawImage(frames[maxFrame+1].getIndexedColorImage(), x, y+INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT, null);
        } else {
            for (int f = 0; f < invocationGraphic.getFrames().length; f++) {
                Tile[] frameTiles = invocationGraphic.getFrames()[f].getTiles();
                int yy = f*INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT;
                for (int t = 0; t < frameTiles.length; t++) {
                    int x = (t%INVOCATION_TILE_WIDTH)*PIXEL_WIDTH;
                    int y = yy + t/INVOCATION_TILE_WIDTH*PIXEL_HEIGHT;
                    graphics.drawImage(frameTiles[t].getIndexedColorImage(), x, y, null);
                }
            }
        }
    }

    public boolean isBattlePreviewMode() {
        return battlePreviewMode;
    }
    
    public void setBattlePreviewMode(boolean battlePreview) {
        this.battlePreviewMode = battlePreview;
        if (battlePreview) {
            background.setBgColor(Color.BLACK);
            background.setCheckerPattern(-1);
            grid.setGridDimensions(-1, -1);
            grid.setThickGridDimensions(-1, -1);
            coordsGrid.setEnabled(false);
            previousScale = scale.getScale();
            scale.setScale(1);
        } else {
            background.setBgColor(Color.LIGHT_GRAY);
            background.setCheckerPattern(PIXEL_WIDTH);
            grid.setGridDimensions(PIXEL_WIDTH, PIXEL_HEIGHT);
            grid.setThickGridDimensions(-1, INVOCATION_TILE_HEIGHT*PIXEL_HEIGHT);
            coordsGrid.setEnabled(true);
            scale.setScale(previousScale);
        }
        redraw();
    }

    public InvocationGraphic getInvocationGraphic() {
        return invocationGraphic;
    }

    public void setInvocationGraphic(InvocationGraphic invocationGraphic) {
        this.invocationGraphic = invocationGraphic;
        redraw();
    }
}
