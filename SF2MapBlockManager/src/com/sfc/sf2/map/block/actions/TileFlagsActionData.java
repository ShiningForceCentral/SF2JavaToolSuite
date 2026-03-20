/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.core.actions.ICumulativeActionData;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapTile;

/**
 *
 * @author TiMMy
 */
public class TileFlagsActionData implements ICumulativeActionData<TileFlagsActionData> {
    
    private final MapBlock block;
    private final TileFlags flag;
    private final boolean flagOn;
    private final int tileIndex;

    public TileFlagsActionData(MapBlock block, TileFlags flag, boolean flagOn, int tileIndex) {
        this.block = block;
        this.flag = flag;
        this.flagOn = flagOn;
        this.tileIndex = tileIndex;
    }

    public MapBlock block() {
        return block;
    }

    public TileFlags flag() {
        return flag;
    }

    public boolean flagOn() {
        return flagOn;
    }

    public int tileIndex() {
        return tileIndex;
    }

    @Override
    public boolean isInvalidated(TileFlagsActionData other) {
        return this.block.equals(other.block) && this.flag.equals(other.flag) && this.flagOn == other.flagOn && this.tileIndex == other.tileIndex;
    }

    @Override
    public boolean canBeCombined(TileFlagsActionData other) {
        return this.block.equals(other.block) && this.flag.equals(other.flag) && this.flagOn == other.flagOn;
    }

    @Override
    public TileFlagsActionData combine(TileFlagsActionData other) {
        return other;
    }

    @Override
    public String toCumulativeString(int number) {
        return String.format("Block(%d) set Flag: %s (%s) - indices: %d", block.getIndex(), flag.toString(), (flagOn ? "On" : "Off"), number);
    }

    @Override
    public String toString() {
        return String.format("Block(%d) set Flag: %s (%s) - index: %d", block.getIndex(), flag.toString(), (flagOn ? "On" : "Off"), tileIndex);
    }
}
