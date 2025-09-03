/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.gui;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import com.sfc.sf2.core.gui.AnimatedLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationLayoutPanel extends AnimatedLayoutPanel {
    
    private static final Dimension IMAGE_SIZE = new Dimension(256, 224);
    
    private static final int BACKGROUND_BASE_X = 0;
    private static final int BACKGROUND_BASE_Y = 56;
    private static final int GROUND_BASE_X = 136;
    private static final int GROUND_BASE_Y = 140;
    private static final int BATTLESPRITE_ALLY_BASE_X = 136;
    private static final int BATTLESPRITE_ALLY_BASE_Y = 64;
    private static final int BATTLESPRITE_ENEMY_BASE_X = 0;
    private static final int BATTLESPRITE_ENEMY_BASE_Y = 56;
    private static final int WEAPONSPRITE_BASE_X = 136;
    private static final int WEAPONSPRITE_BASE_Y = 64;
        
    private Background bg;
    private Ground ground;
    private BattleSprite battlesprite;
    private WeaponSprite weaponsprite;
    private BattleSpriteAnimation animation;
    
    private int currentAnimationFrame = 0;
    private boolean hideWeapon = false;
    
    public BattleSpriteAnimationLayoutPanel() {
        super();
        background = new LayoutBackground(Color.BLACK);
        scale = new LayoutScale(1);
        grid = null;
        coordsGrid = null;
        coordsHeader = null;
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
    }

    @Override
    protected boolean hasData() {
        return battlesprite != null && animation != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return IMAGE_SIZE;
    }

    @Override
    protected void drawImage(Graphics graphics) {
        BattleSpriteAnimationFrame animFrame = animation.getFrames()[currentAnimationFrame];
        Tileset spriteFrame = null;
        spriteFrame = battlesprite.getFrames()[animFrame.getBattleSpriteIndex()];   
        graphics.drawImage(bg.getTileset().getIndexedColorImage(), BACKGROUND_BASE_X, BACKGROUND_BASE_Y, null);
        graphics.drawImage(ground.getTileset().getIndexedColorImage(), GROUND_BASE_X, GROUND_BASE_Y, null);
        if (battlesprite.getType() == BattleSpriteType.ENEMY) {
            drawBattleSpriteFrame(graphics, spriteFrame, BATTLESPRITE_ENEMY_BASE_X+animFrame.getX(), BATTLESPRITE_ENEMY_BASE_Y+animFrame.getY());
        } else {
            boolean showWeapon = !hideWeapon && weaponsprite != null;
            boolean weaponBehind = animFrame.getWeaponBehind();
            if (showWeapon && weaponBehind) {
                drawWeapon(graphics, animFrame);
            }
            drawBattleSpriteFrame(graphics, spriteFrame, BATTLESPRITE_ALLY_BASE_X+animFrame.getX(), BATTLESPRITE_ALLY_BASE_Y+animFrame.getY());
            if (showWeapon && !weaponBehind) {
                drawWeapon(graphics, animFrame);
            }
        }
    }
    
    private void drawWeapon(Graphics graphics, BattleSpriteAnimationFrame frame) {
        int x = WEAPONSPRITE_BASE_X+frame.getX()+frame.getWeaponX();
        int y = WEAPONSPRITE_BASE_Y+frame.getY()+frame.getWeaponY();
        int w = WeaponSprite.FRAME_TILE_WIDTH*PIXEL_WIDTH;
        int h = WeaponSprite.FRAME_TILE_HEIGHT*PIXEL_HEIGHT;
        if (frame.getWeaponFlipH()) {
            x += WeaponSprite.FRAME_TILE_WIDTH*PIXEL_WIDTH;
            w *= -1;
        }
        if (frame.getWeaponFlipV()) {
            y += WeaponSprite.FRAME_TILE_HEIGHT*PIXEL_HEIGHT;
            h *= -1;
        }
        graphics.drawImage(weaponsprite.getFrames()[frame.getWeaponFrame()].getIndexedColorImage(), x, y, w, h, null);
    }
    
    private void drawBattleSpriteFrame(Graphics graphics, Tileset frame, int xOffset, int yOffset) {
        graphics.drawImage(frame.getIndexedColorImage(), xOffset, yOffset, null);
    }

    @Override
    protected void animationFrameUpdated(int frame) {
        super.animationFrameUpdated(frame);
        setFrame(frame);
    }
    
    @Override
    protected int getFrameSpeed(int frame) {
        if (hasData()) {
            return animation.getFrames()[frame].getDuration();
        } else {
            stopAnimation();
            return 0;
        }
    }

    public void setBackground(Background background) {
        this.bg = background;
        redraw();
    }

    public void setGround(Ground ground) {
        this.ground = ground;
        redraw();
    }

    public void setBattlesprite(BattleSprite battlesprite) {
        this.battlesprite = battlesprite;
        redraw();
    }

    public void setWeaponsprite(WeaponSprite weaponsprite) {
        this.weaponsprite = weaponsprite;
        redraw();
    }
    
    public BattleSpriteAnimation getAnimation() {
        return animation;
    }

    public void setAnimation(BattleSpriteAnimation animation) {
        this.animation = animation;
    }
    
    public int getFrame() {
        return currentAnimationFrame;
    }

    public void setFrame(int currentFrame) {
        if (animation == null || currentFrame < 0) return;
        if (this.currentAnimationFrame != currentFrame) {
            currentAnimationFrame = currentFrame;
            redraw();
        }
    }

    public boolean isHideWeapon() {
        return hideWeapon;
    }

    public void setHideWeapon(boolean hideWeapon) {
        this.hideWeapon = hideWeapon;
        redraw();
    }
}
