/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.map.layout.MapLayoutBlock;

/**
 *
 * @author TiMMy
 */
public class MapCopyBlocksActionData implements IActionData<MapCopyBlocksActionData> {

    private final int x;
    private final int y;
    private final MapLayoutBlock[][] copyData;

    public MapCopyBlocksActionData(int x, int y, MapLayoutBlock[][] copyData) {
        this.x = x;
        this.y = y;
        this.copyData = copyData;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public MapLayoutBlock[][] copyData() {
        return copyData;
    }
    
    @Override
    public boolean isInvalidated(MapCopyBlocksActionData other) {
        if (this.x != other.x || this.y != other.y) return false;
        if (this.copyData.length != other.copyData.length || this.copyData[0].length != other.copyData[0].length) return false;
        for (int j = 0; j < copyData.length; j++) {
            for (int i = 0; i < copyData[0].length; i++) {
                if (!this.copyData[i][j].equals(other.copyData[i][j])) return false;
            }
        }
        return true;
    }

    @Override
    public boolean canBeCombined(MapCopyBlocksActionData other) {
        return false;
    }

    @Override
    public MapCopyBlocksActionData combine(MapCopyBlocksActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("Copy tiles x: %d, y: %d, w: %d, h: %d", x, y, copyData.length, copyData[0].length);
    }
}
