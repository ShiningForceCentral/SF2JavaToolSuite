/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation;

import static com.sfc.sf2.helpers.MapBlockHelpers.TILESET_TILES;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class MapAnimationFrame {
    
    private int start;
    private int length;
    private int destTileset;
    private int destTileIndex;
    private int delay;

    public MapAnimationFrame(int start, int length, int dest, int delay) {
        this.start = start;
        this.length = length;
        this.delay = delay;
        
        this.destTileset = dest/TILESET_TILES-2;
        destTileIndex = dest%TILESET_TILES;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDestTileset() {
        return destTileset;
    }

    public void setDestTileset(int destTileset) {
        this.destTileset = destTileset;
    }

    public int getDestTileIndex() {
        return destTileIndex;
    }

    public void setDestTileIndex(int destTileIndex) {
        this.destTileIndex = destTileIndex;
    }
    
    public int getDestValue() {
        return (destTileset+2)*TILESET_TILES+destTileIndex;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MapAnimationFrame)) return super.equals(obj);
        MapAnimationFrame other = (MapAnimationFrame)obj;
        if (this.start != other.start) return false;
        if (this.length != other.length) return false;
        if (this.destTileset != other.destTileset) return false;
        if (this.destTileIndex != other.destTileIndex) return false;
        if (this.delay != other.delay) return false;
        return true;
    }
    
    @Override 
    public MapAnimationFrame clone() {
        MapAnimationFrame clone = new MapAnimationFrame(start, length, getDestValue(), delay);
        return clone;
    }
    
    public static MapAnimationFrame EmptyMapAnimationFrame() {
        return new MapAnimationFrame(0, 32, 768, 20);
    }
}
