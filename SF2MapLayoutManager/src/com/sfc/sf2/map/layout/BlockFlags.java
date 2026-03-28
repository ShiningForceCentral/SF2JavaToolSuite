/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

/**
 *
 * @author TiMMy
 */
public class BlockFlags {
    public static final int MAP_FLAG_MASK_EVENTS = 0x3C00;
    public static final int MAP_FLAG_MASK_EXPLORE = 0xC000;
    
    public static final int MAP_FLAG_STEP = 0x0400;
    public static final int MAP_FLAG_HIDE = 0x0800;
    public static final int MAP_FLAG_SHOW = 0x0C00;
    public static final int MAP_FLAG_WARP = 0x1000;    
    public static final int MAP_FLAG_TRIGGER = 0x1400;
    public static final int MAP_FLAG_CHEST = 0x1800;
    public static final int MAP_FLAG_SEARCH = 0x1C00;
    public static final int MAP_FLAG_LAYER_UP = 0x2000;
    public static final int MAP_FLAG_LAYER_DOWN = 0x2400;
    public static final int MAP_FLAG_TABLE = 0x2800;
    public static final int MAP_FLAG_VASE = 0x2C00;
    public static final int MAP_FLAG_BARREL = 0x3000;
    public static final int MAP_FLAG_SHELF = 0x3400;
    public static final int MAP_FLAG_CARAVAN = 0x3800;
    public static final int MAP_FLAG_RAFT = 0x3C00;
    public static final int MAP_FLAG_STAIRS_RIGHT = 0x4000;
    public static final int MAP_FLAG_STAIRS_LEFT = 0x8000;
    public static final int MAP_FLAG_OBSTRUCTED = 0xC000;
    
    private int flag;

    public BlockFlags(int flag) {
        this.flag = flag;
    }
    
    public int value() {
        return flag;
    }
    
    public void setValue(int value) {
        this.flag = value;
    }

    public int getExplorationFlags() {
        return flag & MAP_FLAG_MASK_EXPLORE;
    }

    public int getEventFlags() {
        return flag & MAP_FLAG_MASK_EVENTS;
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
    
    public boolean isFlagOn(int flag) {
        return (this.flag & flag) != 0;
    }

    @Override
    public String toString() {
        switch (flag) {
            case 0: return "All";
            case MAP_FLAG_STEP: return "Step";
            case MAP_FLAG_HIDE: return "Hide";
            case MAP_FLAG_SHOW: return "Show";
            case MAP_FLAG_WARP: return "Warp";
            case MAP_FLAG_TRIGGER: return "Trigger";
            case MAP_FLAG_CHEST: return "Chest Item";
            case MAP_FLAG_SEARCH: return "Search";
            case MAP_FLAG_LAYER_UP: return "Layer Up";
            case MAP_FLAG_LAYER_DOWN: return "Layer Down";
            case MAP_FLAG_TABLE: return "Table";
            case MAP_FLAG_VASE: return "Vase";
            case MAP_FLAG_BARREL: return "Barrel";
            case MAP_FLAG_SHELF: return "Shelf";
            case MAP_FLAG_CARAVAN: return "Caravan";
            case MAP_FLAG_RAFT: return "Raft";
            case MAP_FLAG_STAIRS_RIGHT: return "Stairs (Right)";
            case MAP_FLAG_STAIRS_LEFT: return "Stairs (Left)";
            case MAP_FLAG_OBSTRUCTED: return "Obstructed";
            default: return "???";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return this == null;
        if (obj == this) return true;
        if ((obj instanceof BlockFlags)) {
            BlockFlags other = (BlockFlags)obj;
            return flag == other.value();
        } else if (obj instanceof Integer) {
            Integer other = (Integer)obj;
            return flag == other;
        }
        return false;
    }
    
    public BlockFlags clone() {
        return new BlockFlags(flag);
    }
}
