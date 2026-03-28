/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation;

/**
 *
 * @author TiMMy
 */
public class SpellAnimationFrame {

    private short frameIndex;
    private short tileIndex;
    private short x;
    private short y;
    private byte w;
    private byte h;
    private boolean foreground;

    public SpellAnimationFrame(short frameIndex, short tileIndex, short x, short y, byte w, byte h, boolean foreground) {
        this.frameIndex = frameIndex;
        this.tileIndex = tileIndex;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.foreground = foreground;
    }
    
    public short getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(short index) {
        this.frameIndex = index;
    }

    public short getTileIndex() {
        return tileIndex;
    }

    public void setTileIndex(short tileIndex) {
        this.tileIndex = tileIndex;
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public byte getW() {
        return w;
    }

    public void setW(byte w) {
        this.w = w;
    }

    public byte getH() {
        return h;
    }

    public void setH(byte h) {
        this.h = h;
    }

    public boolean getForeground() {
        return foreground;
    }

    public void setForeground(boolean foreground) {
        this.foreground = foreground;
    }
    
    public static SpellAnimationFrame createEmpty() {
        return new SpellAnimationFrame((short)0, (short)0, (short)0, (short)0, (byte)1, (byte)1, true);
    }

    @Override
    public SpellAnimationFrame clone() {
        return new SpellAnimationFrame(frameIndex, tileIndex, x, y, w, h, foreground);
    }
}
