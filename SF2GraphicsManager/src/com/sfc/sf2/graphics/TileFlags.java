/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.graphics;

/**
 *
 * @author TiMMy
 */
public class TileFlags {
    public static final byte TILE_FLAG_NONE = 0x0;
    public static final byte TILE_FLAG_HFLIP = 0x1;
    public static final byte TILE_FLAG_VFLIP = 0x2;
    public static final byte TILE_FLAG_BOTHFLIP = 0x3;
    public static final byte TILE_FLAG_PRIORITY = 0x4;
    
    private byte flag;

    public TileFlags(byte flag) {
        this.flag = flag;
    }
    
    public byte value() {
        return flag;
    }
    
    public void addFlag(int flag) {
        this.flag |= flag;
    }
    
    public void removeFlag(int flag) {
        this.flag &= ~flag;
    }
    
    public void toggleFlag(int flag) {
        this.flag ^= flag;
    }
    
    public void setFlag(int flag, boolean on) {
        if (on) {
            addFlag(flag);
        } else {
            removeFlag(flag);
        }
    }
    
    public boolean isHFlip() {
        return (flag & TILE_FLAG_HFLIP) != 0;
    }
    
    public boolean isVFlip() {
        return (flag & TILE_FLAG_VFLIP) != 0;
    }
    
    public boolean isBothFlip() {
        return (flag & TILE_FLAG_BOTHFLIP) != 0;
    }
    
    public boolean isPriority() {
        return (flag & TILE_FLAG_PRIORITY) != 0;
    }

    @Override
    public String toString() {
        switch (flag) {
            case TileFlags.TILE_FLAG_HFLIP: return "HFlip";
            case TileFlags.TILE_FLAG_VFLIP: return "VFlip";
            case TileFlags.TILE_FLAG_BOTHFLIP: return "BothFlip";
            case TileFlags.TILE_FLAG_PRIORITY: return "Priority";
            default: return "All";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return this == null;
        if (obj == this) return true;
        if ((obj instanceof TileFlags)) {
            TileFlags other = (TileFlags)obj;
            return flag == other.value();
        } else if (obj instanceof Byte) {
            Byte other = (Byte)obj;
            return flag == other;
        } else if (obj instanceof Integer) {
            Integer other = (Integer)obj;
            return flag == other;
        }
        return false;
    }
    
    public TileFlags clone() {
        return new TileFlags(flag);
    }
}
