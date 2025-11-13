/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.gui;

import com.sfc.sf2.battlescene.gui.BattleSceneLayoutPanel;
import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationController;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationLayoutPanel extends BattleSceneLayoutPanel implements AnimationController {
    
    private static final Dimension IMAGE_SIZE = new Dimension(256, 224);
        
    private BattleSpriteAnimation animation;
    private BattleSprite battlesprite;
    private WeaponSprite weaponsprite;
    
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
        animator = new LayoutAnimator(this);
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
        super.drawImage(graphics);
        
        BattleSpriteAnimationFrame animFrame = animation.getFrames()[animator.getFrame()];
        Tileset spriteFrame = null;
        spriteFrame = battlesprite.getFrames()[animFrame.getBattleSpriteIndex()];   
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
    public int getAnimationFrameSpeed(int currentAnimFrame) {
        if (hasData()) {
            return animation.getFrames()[currentAnimFrame].getDuration();
        } else {
            animator.stopAnimation();
            return 0;
        }
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

    public boolean isHideWeapon() {
        return hideWeapon;
    }

    public void setHideWeapon(boolean hideWeapon) {
        this.hideWeapon = hideWeapon;
        redraw();
    }
}
