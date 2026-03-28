/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation.gui;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationController;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.spellAnimation.SpellAnimation;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.spellAnimation.SpellAnimationFrame;
import com.sfc.sf2.spellAnimation.SpellSubAnimation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class SpellAnimationLayoutPanel extends AbstractLayoutPanel implements AnimationController {
    
    private static final Dimension IMAGE_SIZE = new Dimension(256, 224);
        
    private static final int SPELL_BASE_X = 136;
    private static final int SPELL_BASE_Y = 100;
    private static final int SPELL_MIRROR_X = 50;
    private static final int SPELL_MIRROR_Y = 75;
    private static final int BACKGROUND_BASE_X = 0;
    private static final int BACKGROUND_BASE_Y = 56;
    private static final int GROUND_BASE_X = 136;
    private static final int GROUND_BASE_Y = 140;
    
    private SpellAnimation spellAnimation;
    private SpellSubAnimation subAnimation;
    private Background bg;
    private Ground ground;
    
    private int currentSubAnimation = 0;
    
    public SpellAnimationLayoutPanel() {
        super();
        background = new LayoutBackground(Color.BLACK);
        scale = new LayoutScale();
        grid = null;
        coordsGrid = null;
        coordsHeader = null;
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
        animator = new LayoutAnimator(this);
    }

    @Override
    protected boolean hasData() {
        return spellAnimation != null && subAnimation != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return IMAGE_SIZE;
    }

    @Override
    protected void drawImage(Graphics graphics) {        
        if (bg != null)
            graphics.drawImage(bg.getTileset().getIndexedColorImage(), BACKGROUND_BASE_X, BACKGROUND_BASE_Y, null);
        if (ground != null)
            graphics.drawImage(ground.getTileset().getIndexedColorImage(), GROUND_BASE_X, GROUND_BASE_Y, null);
        drawAnimationFrame(graphics, animator.getFrame());
    }
    
    private void drawAnimationFrame(Graphics g, int index) {
        if (subAnimation == null || index < 0 || index >= subAnimation.getFrames().length) return;
        boolean mirror = false;
        int posX = mirror ? SPELL_MIRROR_X : SPELL_BASE_X;
        int posY = mirror ? SPELL_MIRROR_Y : SPELL_BASE_Y;
        for (int f = 0; f < subAnimation.getFrames().length; f++) {
            SpellAnimationFrame frame = subAnimation.getFrames()[index];
        
            int startIndex = frame.getTileIndex();
            if (startIndex >= 0 && startIndex < spellAnimation.getSpellGraphic().getTiles().length) {
                int x = frame.getX();
                int y = frame.getY();
                for (int j = 0; j < frame.getH(); j++) {
                    for (int i = 0; i < frame.getW(); i++) {
                        Tile tile = spellAnimation.getSpellGraphic().getTiles()[startIndex + i + j * PIXEL_WIDTH];
                        g.drawImage(tile.getIndexedColorImage(), posX+x+i*PIXEL_WIDTH, posY+y+j*PIXEL_HEIGHT, null);
                    }
                }
            }
        }
    }

    @Override
    public int getAnimationFrameSpeed(int currentAnimFrame) {
        if (hasData()) {
            return 1;
        } else {
            animator.stopAnimation();
            return 0;
        }
    }

    public void setBackground(Background bg) {
        this.bg = bg;
        redraw();
    }

    public void setGround(Ground ground) {
        this.ground = ground;
        redraw();
    }

    public SpellAnimation getSpellAnimation() {
        return spellAnimation;
    }

    public void setSpellAnimation(SpellAnimation spellAnimation) {
        this.spellAnimation = spellAnimation;
        currentSubAnimation = 0;
        animator.setFrame(0);
        redraw();
    }

    public int getSubAnimationIndex() {
        return currentSubAnimation;
    }

    public void setSubAnimationIndex(int subAnimationIndex) {
        this.currentSubAnimation = subAnimationIndex;
        if (spellAnimation == null || subAnimationIndex >= spellAnimation.getSpellSubAnimations().length) {
            subAnimation = null;
        } else {
            subAnimation = spellAnimation.getSpellSubAnimations()[subAnimationIndex];
        }
        redraw();
    }
}
