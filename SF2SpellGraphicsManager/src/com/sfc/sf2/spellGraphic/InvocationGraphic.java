/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author TiMMy
 */
public class InvocationGraphic implements INameable {

    public static int INVOCATION_TILE_WIDTH = 16;
    public static int INVOCATION_TILE_HEIGHT = 8;
    
    private String name;
    private Tileset[] frames;
    private short posX;
    private short posY;
    private short loadMode;
    
    public InvocationGraphic(String name, Tileset[] frames) {
        this(name, frames, (short)0, (short)0, (short)1);
    }

    public InvocationGraphic(String name, Tileset[] frames, short posX, short posY, short loadMode) {
        this.name = name;
        this.frames = frames;
        this.posX = posX;
        this.posY = posY;
        this.loadMode = loadMode;
    }

    @Override
    public String getName() {
        return name;
    }

    public Tileset[] getFrames() {
        return frames;
    }

    public void setFrames(Tileset[] frames) {
        this.frames = frames;
    }
    
    public void clearIndexedColorImage() {
        if (frames == null) return;
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] != null) {
                frames[i].clearIndexedColorImage(true);
            }
        }
    }

    public Palette getPalette() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        return frames[0].getPalette();
    }

    public int getTotalHeight() {
        return frames == null ? 0 : INVOCATION_TILE_HEIGHT*frames.length;
    }

    public short getPosX() {
        return posX;
    }

    public void setPosX(short posX) {
        this.posX = posX;
    }

    public short getPosY() {
        return posY;
    }

    public void setPosY(short posY) {
        this.posY = posY;
    }

    public short getLoadMode() {
        return loadMode;
    }

    public void setLoadMode(short loadMode) {
        this.loadMode = loadMode;
    }
}
