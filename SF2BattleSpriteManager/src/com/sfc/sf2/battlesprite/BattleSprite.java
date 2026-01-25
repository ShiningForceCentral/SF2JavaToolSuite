/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author wiz
 */
public class BattleSprite implements INameable {
    public static final int BATTLE_SPRITE_TILE_HEIGHT = 12;
    
    public enum BattleSpriteType {
        ALLY,
        ENEMY,
    }
    
    private String name;
    private BattleSpriteType type;
    private Palette[] palettes;
    private int currentPaletteIndex;
    private Tileset[] frames;
    
    private int animSpeed;
    private byte statusOffsetX;
    private byte statusOffsetY;
    
    public BattleSprite(String name, BattleSpriteType type, Tileset[] frames, Palette[] palettes) {
        this(name, type, frames, palettes, 0, (byte)0, (byte)0);
    }
    
    public BattleSprite(String name, BattleSpriteType type, Tileset[] frames, Palette[] palettes, int animSpeed, byte statusOffsetX, byte statusOffsetY) {
        this.name = name;
        this.type = type;
        this.frames = frames;
        this.palettes = palettes;
        setCurrentPaletteIndex(0);
        this.animSpeed = animSpeed;
        this.statusOffsetX = statusOffsetX;
        this.statusOffsetY = statusOffsetY;
    }

    public String getName() {
        return name;
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

    public Palette getCurrentPalette() {
        if (currentPaletteIndex < 0 || currentPaletteIndex >= palettes.length) return null;
        return palettes[currentPaletteIndex];
    }

    public int getCurrentPaletteIndex() {
        return currentPaletteIndex;
    }

    public void setCurrentPaletteIndex(int currentPalette) {
        if (currentPalette < 0 || currentPalette >= palettes.length) return;
        if (frames == null || frames.length == 0) return;
        this.currentPaletteIndex = currentPalette;
        for (int i = 0; i < frames.length; i++) {
            frames[i].setPalette(palettes[currentPalette]);
            frames[i].clearIndexedColorImage(true);
        }
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
    
    public void clearIndexedColorImage(boolean alsoClearTiles) {
        if (frames == null || frames.length == 0) {
            return;
        }
        for (int i = 0; i < frames.length; i++) {
            frames[i].clearIndexedColorImage(alsoClearTiles);
        }
    }
}
