/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlockset;

/**
 *
 * @author TiMMy
 */
public class MapBlocksetActionData implements IActionData<MapBlocksetActionData> {
    
    private MapBlockset blockset;
    private Tileset[] tilesets;

    public MapBlocksetActionData(MapBlockset blockset, Tileset[] tilesets) {
        this.blockset = blockset;
        this.tilesets = tilesets;
    }

    public MapBlockset blockset() {
        return blockset;
    }

    public Tileset[] tilesets() {
        return tilesets;
    }

    @Override
    public boolean isInvalidated(MapBlocksetActionData other) {
        if (!this.blockset.equals(other.blockset)) return false;
        for (int i = 0; i < tilesets.length; i++) {
            if (!this.tilesets[i].equals(other.tilesets[i])) return false;
        }
        return true;
    }

    @Override
    public boolean canBeCombined(MapBlocksetActionData other) {
        return this.blockset.equals(other.blockset);
    }

    @Override
    public MapBlocksetActionData combine(MapBlocksetActionData other) {
        return other;
    }

    @Override
    public String toString() {
        StringBuilder tilesetIDs = new StringBuilder();
        if (tilesets == null) {
            tilesetIDs.append("NULL");
        } else {
            for (int i = 0; i < tilesets.length; i++) {
                if (tilesets[i] != null) {
                    if (tilesetIDs.length() > 0)
                        tilesetIDs.append(", ");
                    tilesetIDs.append(tilesets[i].getName());
                }
            }
        }
        return String.format("Blockset: %s - Tilesets: {%s}", blockset == null ? "NULL" : blockset.getName(), tilesetIDs.toString());
    }
}
