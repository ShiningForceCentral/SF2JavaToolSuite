/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.ICumulativeActionData;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class MapBlockActionData implements ICumulativeActionData<MapBlockActionData> {

    private final MapLayout layout;
    private final MapBlock block;
    private final int layoutIndex;

    public MapBlockActionData(MapLayout layout, MapBlock block, int layoutIndex) {
        this.layout = layout;
        this.block = block;
        this.layoutIndex = layoutIndex;
    }

    public MapLayout layout() {
        return layout;
    }

    public MapBlock block() {
        return block;
    }

    public int layoutIndex() {
        return layoutIndex;
    }
    
    @Override
    public boolean isInvalidated(MapBlockActionData other) {
        return this.layout.equals(other.layout) && this.block.equals(other.block) && this.layoutIndex == other.layoutIndex;
    }

    @Override
    public boolean canBeCombined(MapBlockActionData other) {
        return this.layout.equals(other.layout) && this.block.equals(other.block);
    }

    @Override
    public MapBlockActionData combine(MapBlockActionData other) {
        return other;
    }

    @Override
    public String toCumulativeString(int number) {
        return String.format("Setting block: %d to %d blocks", block.getIndex(), number);
    }

    @Override
    public String toString() {
        int x = layoutIndex % MapLayout.BLOCK_WIDTH;
        int y = layoutIndex / MapLayout.BLOCK_HEIGHT;
        return String.format("Setting block: %d to block at x: %d, y: %d", block.getIndex(), x, y);
    }
}
