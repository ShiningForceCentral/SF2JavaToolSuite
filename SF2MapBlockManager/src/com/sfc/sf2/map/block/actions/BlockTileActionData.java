/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.core.actions.ICumulativeActionData;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapTile;

/**
 *
 * @author TiMMy
 */
public class BlockTileActionData implements ICumulativeActionData<BlockTileActionData> {
    private MapBlock block;
    private MapTile tile;
    private int tileIndex;

    public BlockTileActionData(MapBlock block, MapTile tile, int tileIndex) {
        this.block = block;
        this.tile = tile;
        this.tileIndex = tileIndex;
    }

    public MapBlock block() {
        return block;
    }

    public MapTile tile() {
        return tile;
    }

    public int tileIndex() {
        return tileIndex;
    }

    @Override
    public boolean isInvalidated(BlockTileActionData other) {
        return this.block.equals(other.block) && this.tile.equals(other.tile) && this.tileIndex == other.tileIndex;
    }

    @Override
    public boolean canBeCombined(BlockTileActionData other) {
        return this.block.equals(other.block) && this.tile.equals(other.tile);
    }

    @Override
    public BlockTileActionData combine(BlockTileActionData other) {
        return other;
    }

    @Override
    public String toCumulativeString(int number) {
        return String.format("Block(%d) set to Tile(%d) - Indices: %d", block.getIndex(), tile.getTileIndex(), number);
    }
    
    @Override
    public String toString() {
        return String.format("Block(%d) set to Tile(%d) - index: %d", block.getIndex(), tile.getTileIndex(), tileIndex);
    }
}
