/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import com.sfc.sf2.map.block.MapBlock;

/**
 *
 * @author TiMMy
 */
public class MapLayoutBlock {
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
    
    private MapBlock mapBlock;
    private int flags;
    
    public MapLayoutBlock(MapBlock mapBlock, int flags) {
        this.mapBlock = mapBlock;
        this.flags = flags;
    }

    public MapBlock getMapBlock() {
        return mapBlock;
    }

    public void setMapBlock(MapBlock mapBlock) {
        this.mapBlock = mapBlock;
    }

    public int getFlags() {
        return flags;
    }

    public int getExplorationFlags() {
        return flags & MAP_FLAG_MASK_EXPLORE;
    }

    public int getEventFlags() {
        return flags & MAP_FLAG_MASK_EVENTS;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!equalsIgnoreTiles(obj)) return false;
        if (obj == null) return false;
        MapLayoutBlock block = (MapLayoutBlock)obj;
        if (!this.mapBlock.equals(block.mapBlock)) return false;
        return this.flags == block.flags;
    }
    
    public boolean equalsIgnoreTiles(Object obj) {
        if (obj == null) return this == null;
        if (obj == this) return true;
        if (!(obj instanceof MapLayoutBlock)) return false;
        MapLayoutBlock block = (MapLayoutBlock)obj;
        if (!this.mapBlock.equalsIgnoreTiles(block.mapBlock)) return false;
        return this.flags == block.flags;
    }
    
    @Override 
    public MapLayoutBlock clone() {
        MapLayoutBlock clone = new MapLayoutBlock(mapBlock, flags);
        return clone;
    }
    
    public static MapLayoutBlock EmptyMapLayoutBlock() {
        return new MapLayoutBlock(null, 0);
    }
}
