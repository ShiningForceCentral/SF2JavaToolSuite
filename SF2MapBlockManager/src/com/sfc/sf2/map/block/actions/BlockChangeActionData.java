/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.map.block.MapBlock;

/**
 *
 * @author TiMMy
 */
public class BlockChangeActionData implements IActionData<BlockChangeActionData> {
    
    private MapBlock block;
    private int index;

    public BlockChangeActionData(MapBlock block, int index) {
        this.block = block;
        this.index = index;
    }

    public MapBlock block() {
        return block;
    }

    public int index() {
        return index;
    }

    @Override
    public boolean isInvalidated(BlockChangeActionData other) {
        return this.block.equals(other.block) && this.index == other.index;
    }

    @Override
    public boolean canBeCombined(BlockChangeActionData other) {
        return this.block.equals(other.block);
    }

    @Override
    public BlockChangeActionData combine(BlockChangeActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("Block(%d): %s", index, block.getIndex());
    }
}
