/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.gui;

import com.sfc.sf2.battlesprite.BattleSprite;
import static com.sfc.sf2.battlesprite.BattleSprite.BATTLE_SPRITE_TILE_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import com.sfc.sf2.core.gui.AnimatedLayoutPanel;
import com.sfc.sf2.graphics.Tileset;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author wiz
 */
public class BattleSpriteLayoutPanel extends AnimatedLayoutPanel {
    
    public BattleSpriteLayoutPanel() {
        super();
        setGridDimensions(8, 8, -1, BATTLE_SPRITE_TILE_HEIGHT*PIXEL_HEIGHT);
    }
    
    private BattleSprite battleSprite;
    private int currentPalette;
    
    private boolean showStatusMarker = false;
    
    @Override
    protected boolean hasData() {
        return battleSprite != null && (currentPalette >= 0 && currentPalette < battleSprite.getPalettes().length);
    }

    @Override
    protected Dimension getImageDimensions() {
        int width = battleSprite.getTilesPerRow()*PIXEL_WIDTH;
        int height = 0;
        if (isAnimating()) {
            height = BATTLE_SPRITE_TILE_HEIGHT*PIXEL_HEIGHT;
        } else {
            height = battleSprite.getFrames().length*BATTLE_SPRITE_TILE_HEIGHT*PIXEL_HEIGHT;
        }
        return new Dimension(width, height);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        Graphics2D g2 = (Graphics2D)graphics;
        if (isAnimating()) {
            drawAnimPreview(g2);
        } else {
            drawBattleSprites(g2);
        }
    }
    
    public void drawAnimPreview(Graphics2D graphics) {
        Tileset tileset = battleSprite.getFrames()[getCurrentAnimationFrame()];
        graphics.drawImage(tileset.getIndexedColorImage(), 0, 0, null);
    }
    
    public void drawBattleSprites(Graphics2D graphics) {
        int frames = battleSprite.getFrames().length;
        for(int f = 0; f < frames; f++) {
            Tileset tileset = battleSprite.getFrames()[f];
            int yOffset = f*BATTLE_SPRITE_TILE_HEIGHT*PIXEL_HEIGHT;
            graphics.drawImage(tileset.getIndexedColorImage(), 0, yOffset, null);
            if (showStatusMarker) {
                drawStatusIcon(graphics, yOffset);
            }
        }
    }
    
    private void drawStatusIcon(Graphics2D graphics, int yOffset) {
        int x = battleSprite.getStatusOffsetX();
        int y = battleSprite.getStatusOffsetY() + yOffset;
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(5));
        graphics.drawLine(x-5, y-5, x+5, y+5);
        graphics.drawLine(x-5, y+5, x+5, y-5);
        
        graphics.setColor(Color.YELLOW);
        graphics.setStroke(new BasicStroke(3));
        graphics.drawLine(x-5, y-5, x+5, y+5);
        graphics.drawLine(x-5, y+5, x+5, y-5);
        graphics.setColor(Color.WHITE);
    }
    
    public BattleSprite getBattleSprite() {
        return battleSprite;
    }

    public void setBattleSprite(BattleSprite battleSprite) {
        this.battleSprite = battleSprite;
    }
    
    public int getCurrentPalette() {
        return currentPalette;
    }

    public void setCurrentPalette(int currentPalette) {
        this.currentPalette = currentPalette;
        if (battleSprite != null) {
            battleSprite.setRenderPalette(battleSprite.getPalettes()[currentPalette]);
            redraw();
        }
    }

    public void setShowStatusMarker(boolean showStatusMarker) {
        this.showStatusMarker = showStatusMarker;
        redraw();
    }
}
