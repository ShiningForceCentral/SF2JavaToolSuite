/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.ICumulativeActionData;
import com.sfc.sf2.map.layout.BlockFlags;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class MapFlagActionData implements ICumulativeActionData<MapFlagActionData> {
    
    private final MapLayout mapLayout;
    private final BlockFlags mapFlag;
    private final int layoutIndex;
    private final BlockFlags blockNewFlag;

    public MapFlagActionData(MapLayout mapLayout, BlockFlags mapFlag, int layoutIndex, BlockFlags blockNewFlag) {
        this.mapLayout = mapLayout;
        this.mapFlag = mapFlag;
        this.layoutIndex = layoutIndex;
        this.blockNewFlag = blockNewFlag;
    }

    public MapLayout layout() {
        return mapLayout;
    }

    public BlockFlags mapFlag() {
        return mapFlag;
    }

    public int layoutIndex() {
        return layoutIndex;
    }

    public BlockFlags blockNewFlag() {
        return blockNewFlag;
    }

    @Override
    public boolean isInvalidated(MapFlagActionData other) {
        return this.mapLayout.equals(other.mapLayout) && this.mapFlag.equals(other.mapFlag) && this.layoutIndex == other.layoutIndex && this.blockNewFlag.equals(other.blockNewFlag);
    }

    @Override
    public boolean canBeCombined(MapFlagActionData other) {
        return this.mapLayout.equals(other.mapLayout) && this.mapFlag.equals(other.mapFlag);
    }

    @Override
    public MapFlagActionData combine(MapFlagActionData other) {
        return other;
    }

    @Override
    public String toCumulativeString(int number) {
        return String.format("Set %s flag - Indices: %d", mapFlag.toString(), number);
    }

    @Override
    public String toString() {
        return String.format("Set %s flag - index: %d", mapFlag.toString(), layoutIndex);
    }
}
