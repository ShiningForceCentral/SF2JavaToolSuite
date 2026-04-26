/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.IPaletteGraphic;
import com.sfc.sf2.palette.Palette;
import java.awt.Point;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class BattleSprite implements INameable, IPaletteGraphic {
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
        if (palettes == null || currentPaletteIndex < 0 || currentPaletteIndex >= palettes.length) return null;
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

    @Override
    public Palette getPalette() {
        return getCurrentPalette();
    }

    @Override
    public void setPalette(Palette palette) {
        if (palettes == null || currentPaletteIndex < 0 || currentPaletteIndex >= palettes.length) return;
        palettes[currentPaletteIndex] = palette;
        setCurrentPaletteIndex(currentPaletteIndex);    //Force refresh frames
    }

    @Override
    public byte[] getPixels() {
        if (frames == null || frames.length == 0) return null;
        int pixelsPerFrame = getTilesPerRow()*BATTLE_SPRITE_TILE_HEIGHT*Tile.PIXEL_COUNT;
        byte[] pixels = new byte[frames.length*pixelsPerFrame];
        for (int i = 0; i < frames.length; i++) {
            byte[] framePixels = frames[i].getPixels();
            System.arraycopy(framePixels, 0, pixels, i*pixelsPerFrame, pixelsPerFrame);
        }
        return pixels;
    }

    @Override
    public void setPixels(byte[] pixels) {
        if (frames == null || frames.length == 0) return;
        int pixelsPerFrame = getTilesPerRow()*BATTLE_SPRITE_TILE_HEIGHT*Tile.PIXEL_COUNT;
        if (pixels.length != frames.length*pixelsPerFrame) {
            throw new IllegalStateException(String.format("ERROR: BattleSprite %s frames count changed from %d to %d when editing palette.", name, frames.length, pixels.length/pixelsPerFrame));
        }
        
        for (int i = 0; i < frames.length; i++) {
            byte[] framePixels = new byte[pixelsPerFrame];
            System.arraycopy(pixels, i*pixelsPerFrame, framePixels, 0, pixelsPerFrame);
            frames[i].setPixels(framePixels);
        }
        clearIndexedColorImage(false);
    }

    public int getAnimSpeed() {
        return animSpeed;
    }

    public void setAnimSpeed(int animSpeed) {
        this.animSpeed = animSpeed;
    }

    public Point getStatusOffsetPos() {
        return new Point(statusOffsetX, statusOffsetY);
    }

    public void setStatusOffsetPos(Point point) {
        this.statusOffsetX = (byte)point.x;
        this.statusOffsetY = (byte)point.y;
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
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BattleSprite)) return super.equals(obj);
        BattleSprite other = (BattleSprite)obj;
        if (!this.name.equals(other.name)) return false;
        if (!this.type.equals(other.type)) return false;
        if (!Arrays.equals(this.palettes, other.palettes)) return false;
        if (this.currentPaletteIndex != other.currentPaletteIndex) return false;
        if (!Arrays.equals(this.frames, other.frames)) return false;
        if (this.animSpeed != other.animSpeed) return false;
        if (this.statusOffsetX != other.statusOffsetX) return false;
        if (this.statusOffsetY != other.statusOffsetY) return false;
        return true;
    }
}
