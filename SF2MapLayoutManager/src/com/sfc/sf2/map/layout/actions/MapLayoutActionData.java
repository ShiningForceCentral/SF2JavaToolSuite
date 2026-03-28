/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class MapLayoutActionData implements IActionData<MapLayoutActionData> {
    
    private final MapLayout layout;
    private final MapBlockset blockset;
    private final String sharedBlockInfo;

    public MapLayoutActionData(MapLayout layout, MapBlockset blockset, String sharedBlockInfo) {
        this.layout = layout;
        this.blockset = blockset;
        this.sharedBlockInfo = sharedBlockInfo;
    }

    public MapLayout layout() {
        return layout;
    }

    public MapBlockset blockset() {
        return blockset;
    }

    public String sharedBlockInfo() {
        return sharedBlockInfo;
    }

    @Override
    public boolean isInvalidated(MapLayoutActionData other) {
        if (other == null) return false;
        if (!this.layout.equals(other.layout)) return false;
        if (this.blockset == null) {
            return other.blockset == null;
        } else {
            return this.blockset.equals(other.blockset);
        }
    }

    @Override
    public boolean canBeCombined(MapLayoutActionData other) {
        return isInvalidated(other);
    }

    @Override
    public MapLayoutActionData combine(MapLayoutActionData other) {
        return other;
    }

    @Override
    public String toString() {
        if (layout == null) {
            return "NULL";
        } else {
            return String.format("Layout: map%d", layout.getIndex());
        }
    }
}
