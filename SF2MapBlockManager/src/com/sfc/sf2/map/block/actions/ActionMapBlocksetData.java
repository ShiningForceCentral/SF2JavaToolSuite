/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlockset;

/**
 *
 * @author TiMMy
 */
public class ActionMapBlocksetData {
    
    private MapBlockset blockset;
    private Tileset[] tilesets;

    public ActionMapBlocksetData(MapBlockset blockset, Tileset[] tilesets) {
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
    public boolean equals(Object obj) {
        if (obj instanceof ActionMapBlocksetData) {
            ActionMapBlocksetData other = (ActionMapBlocksetData)obj;
            if (blockset != other.blockset) return false;
            if (tilesets == null && other.tilesets == null) return true;
            if (tilesets != other.tilesets || tilesets == null || other.tilesets == null) return false;
            if (tilesets.length != other.tilesets.length) return false;
            for (int i = 0; i < tilesets.length; i++) {
                if (tilesets[i] != other.tilesets[i]) return false;
            }
            return true;
        } else {
            return super.equals(obj);
        }
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
