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
    
    private MapBlock mapBlock;
    private BlockFlags flags;
    
    public MapLayoutBlock(MapBlock mapBlock, BlockFlags flags) {
        this.mapBlock = mapBlock;
        this.flags = flags;
    }

    public MapBlock getMapBlock() {
        return mapBlock;
    }

    public void setMapBlock(MapBlock mapBlock) {
        this.mapBlock = mapBlock;
    }

    public BlockFlags getFlags() {
        return flags;
    }

    public void setFlags(BlockFlags flags) {
        this.flags = flags;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!equalsIgnoreTiles(obj)) return false;
        if (obj == null) return false;
        MapLayoutBlock block = (MapLayoutBlock)obj;
        if (!this.mapBlock.equals(block.mapBlock)) return false;
        return this.flags.equals(block.flags);
    }
    
    public boolean equalsIgnoreTiles(Object obj) {
        if (!(obj instanceof MapLayoutBlock)) return super.equals(obj);
        MapLayoutBlock block = (MapLayoutBlock)obj;
        if (!this.mapBlock.equalsIgnoreTiles(block.mapBlock)) return false;
        return this.flags.equals(block.flags);
    }
    
    @Override 
    public MapLayoutBlock clone() {
        MapLayoutBlock clone = new MapLayoutBlock(mapBlock, flags);
        return clone;
    }
    
    public static MapLayoutBlock EmptyMapLayoutBlock() {
        return new MapLayoutBlock(null, new BlockFlags(0));
    }
}
