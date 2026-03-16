/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.actions;

import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class ActionMapLayoutData {
    
    private final MapLayout layout;
    private final MapBlockset blockset;
    private final String sharedBlockInfo;

    public ActionMapLayoutData(MapLayout layout, MapBlockset blockset, String sharedBlockInfo) {
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
    public boolean equals(Object obj) {
        if (obj instanceof ActionMapLayoutData) {
            ActionMapLayoutData other = (ActionMapLayoutData)obj;
            if (!layout.equals(other.layout)) return false;
            return blockset.equals(other.blockset);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public String toString() {
        if (layout == null) {
            return String.format("Layout: NULL");
        } else {
            return String.format("Layout: map%d", layout.getIndex());
        }
    }
}
