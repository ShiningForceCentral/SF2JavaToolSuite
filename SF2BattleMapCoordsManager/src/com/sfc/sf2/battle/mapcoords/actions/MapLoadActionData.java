/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class MapLoadActionData implements IActionData<MapLoadActionData> {
    private final int coordsIndex;
    private final MapLayout layout;

    public MapLoadActionData(int coordsIndex, MapLayout layout) {
        this.coordsIndex = coordsIndex;
        this.layout = layout;
    }

    public int coordsIndex() {
        return coordsIndex;
    }

    public MapLayout layout() {
        return layout;
    }

    @Override
    public boolean isInvalidated(MapLoadActionData other) {
        return this.coordsIndex == other.coordsIndex && this.layout.equals(other.layout);
    }

    @Override
    public boolean canBeCombined(MapLoadActionData other) {
        return false;
    }

    @Override
    public MapLoadActionData combine(MapLoadActionData other) {
        return other;
    }
    
    @Override
    public String toString() {
        return String.format("Load map index %d", coordsIndex);
    }
}
