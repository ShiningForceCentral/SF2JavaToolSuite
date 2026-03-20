/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.map.block.MapTile;

/**
 *
 * @author TiMMy
 */
public class TileFlagsActionData implements IActionData<TileFlagsActionData> {
    
    private MapTile tile;
    private int index;
    private TileFlags flags;

    public TileFlagsActionData(MapTile tile, int index, TileFlags flags) {
        this.tile = tile;
        this.index = index;
        this.flags = flags;
    }

    public MapTile tile() {
        return tile;
    }

    public int index() {
        return index;
    }

    public TileFlags flags() {
        return flags;
    }

    @Override
    public boolean isInvalidated(TileFlagsActionData other) {
        return this.tile.equals(other.tile) && this.index == other.index && this.flags.equals(other.flags);
    }

    @Override
    public boolean canBeCombined(TileFlagsActionData other) {
        return this.tile.equals(other.tile) && this.index == other.index;
    }

    @Override
    public TileFlagsActionData combine(TileFlagsActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("Tile(%s) - H: {%s}, V: {%s}, P: {%s}", index, flags.isHFlip(), flags.isVFlip(), flags.isPriority());
    }
}
