/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.map.block.MapBlock;

/**
 *
 * @author TiMMy
 */
public class ActionBlockChange {
    
    private MapBlock block;
    private int index;

    public ActionBlockChange(MapBlock block, int index) {
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
    public String toString() {
        return String.format("Block(%d): %s", index, block == null ? "NULL" : block.getIndex());
    }
}
