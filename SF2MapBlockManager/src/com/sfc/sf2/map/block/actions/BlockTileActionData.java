/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapTile;

/**
 *
 * @author TiMMy
 */
public class BlockTileActionData implements IActionData<BlockTileActionData> {
    private MapBlock block;
    private int index;
    private MapTile tile;

    public BlockTileActionData(MapBlock block, int index, MapTile tile) {
        this.block = block;
        this.index = index;
        this.tile = tile;
    }

    public MapBlock block() {
        return block;
    }

    public int index() {
        return index;
    }

    public MapTile tile() {
        return tile;
    }

    @Override
    public boolean isInvalidated(BlockTileActionData other) {
        return this.block.equals(other.block) && this.index == other.index && this.tile.equals(other.tile);
    }

    @Override
    public boolean canBeCombined(BlockTileActionData other) {
        return this.block.equals(other.block) && this.index == other.index;
    }

    @Override
    public BlockTileActionData combine(BlockTileActionData other) {
        return other;
    }
    
    @Override
    public String toString() {
        return String.format("Block: %s Index: {%s} - Tile: {%s}", index, block.getIndex(), tile.getTileIndex());
    }
}
