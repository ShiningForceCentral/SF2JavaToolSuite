/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.layout.io;

import com.sfc.sf2.graphics.TileFlags;

/**
 *
 * @author TiMMy
 */
public class TileMatchData {
    public int index;
    public TileFlags flags;

    public TileMatchData(int index, TileFlags flags) {
        this.index = index;
        this.flags = flags;
    }
    
    public boolean matchFound() { return index != -1; }
    
    public static TileMatchData NoMatch() {
        return new TileMatchData(-1, null);
    }
}
