/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author wiz
 */
public class BattleSprite {
    public static final int BATTLE_SPRITE_TILE_HEIGHT = 12;
    
    public enum BattleSpriteType {
        ALLY,
        ENEMY,
    }
    
    private BattleSpriteType type;    
    private Palette[] palettes;
    private Tileset[] frames;
    
    private int animSpeed;
    private byte statusOffsetX;
    private byte statusOffsetY;
    
    public BattleSprite(BattleSpriteType type, Tileset[] frames, Palette[] palettes) {
        this.type = type;
        this.frames = frames;
        this.palettes = palettes;
        this.animSpeed = 0;
        this.statusOffsetX = 0;
        this.statusOffsetY = 0;
    }
    
    public BattleSprite(BattleSpriteType type, Tileset[] frames, Palette[] palettes, int animSpeed, byte statusOffsetX, byte statusOffsetY) {
        this.type = type;
        this.frames = frames;
        this.palettes = palettes;
        this.animSpeed = animSpeed;
        this.statusOffsetX = statusOffsetX;
        this.statusOffsetY = statusOffsetY;
    }

    public BattleSpriteType getType() {
        return type;
    }

    public void setType(BattleSpriteType type) {
        this.type = type;
    }

    public int getTilesPerRow() {
        return getTilesPerRow(type);
    }
    
    public static int getTilesPerRow(BattleSpriteType type) {
        return type == BattleSpriteType.ALLY ? 12 : 16;
    }

    public Tileset[] getFrames() {
        return frames;
    }

    public void setFrames(Tileset[] frames) {
        this.frames = frames;
    }

    public Palette[] getPalettes() {
        return palettes;
    }

    public void setPalettes(Palette[] palettes) {
        this.palettes = palettes;
    }

    public int getAnimSpeed() {
        return animSpeed;
    }

    public void setAnimSpeed(int animSpeed) {
        this.animSpeed = animSpeed;
    }

    public byte getStatusOffsetX() {
        return statusOffsetX;
    }

    public void setStatusOffsetX(byte statusOffsetX) {
        this.statusOffsetX = statusOffsetX;
    }

    public byte getStatusOffsetY() {
        return statusOffsetY;
    }

    public void setStatusOffsetY(byte statusOffsetY) {
        this.statusOffsetY = statusOffsetY;
    }
    
    public void setRenderPalette(Palette palette) {
        if (frames == null || frames.length == 0) {
            return;
        }
        for (int i = 0; i < frames.length; i++) {
            frames[i].setPalette(palette);
            frames[i].clearIndexedColorImage(true);
        }
    }
    
    public void clearIndexedColorImage(boolean alsoClearTiles) {
        if (frames == null || frames.length == 0) {
            return;
        }
        for (int i = 0; i < frames.length; i++) {
            frames[i].clearIndexedColorImage(alsoClearTiles);
        }
    }
}
