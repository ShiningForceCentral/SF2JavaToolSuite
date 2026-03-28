/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation;

import com.sfc.sf2.graphics.Tileset;
import static com.sfc.sf2.helpers.MapBlockHelpers.TILESET_TILES;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class MapAnimation {
    
    private int tilesetId;
    private int length;
    private Tileset animationTileset;
    private MapAnimationFrame[] frames;
    private final Tileset[] originalTilesets;

    private final Tileset[] modifiedTilesets;
    private final boolean[] modified;

    public MapAnimation(int tilesetId, int length, MapAnimationFrame[] frames, Tileset[] originalTilesets) {
        this.tilesetId = tilesetId;
        this.length = length;
        this.frames = frames;
        this.originalTilesets = originalTilesets;

        this.modifiedTilesets = new Tileset[originalTilesets.length];
        this.modified = new boolean[originalTilesets.length];
    }

    public int getTilesetId() {
        return tilesetId;
    }

    public void setTilesetId(int tilesetId) {
        this.tilesetId = tilesetId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Tileset getAnimationTileset() {
        return animationTileset;
    }

    public void setAnimationTileset(Tileset animationTileset) {
        this.animationTileset = animationTileset;
        generateModifiedTilesets();
    }

    public Tileset[] getOriginalTilesets() {
        return originalTilesets;
    }

    public Tileset[] getModifiedTilesets() {
        return modifiedTilesets;
    }

    public MapAnimationFrame[] getFrames() {
        return frames;
    }

    public void setFrames(MapAnimationFrame[] frames) {
        this.frames = frames;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MapAnimation)) return super.equals(obj);
        MapAnimation other = (MapAnimation)obj;
        if (this.tilesetId != other.tilesetId) return false;
        if (this.length != other.length) return false;
        if (!Arrays.equals(this.frames, other.frames)) return false;
        if (!Arrays.equals(this.originalTilesets, other.originalTilesets)) return false;
        return true;
    }
    
    public Tileset[] generateModifiedTilesets() {
        //Clear old
        for (int i = 0; i < modifiedTilesets.length; i++) {
            if (modified[i] && modifiedTilesets[i] != null) {
                modifiedTilesets[i].clearIndexedColorImage(true);
                modifiedTilesets[i] = null;
            }
            modified[i] = false;
        }
        //Fill with default
        for (int i = 0; i < modifiedTilesets.length; i++) {
            if (modifiedTilesets[i] == null) {
                modifiedTilesets[i] = originalTilesets[i];
            }
        }
        return modifiedTilesets;
    }
    
    public void generateModifiedTileset(int frame) {
        int tileset = frames[frame].getDestTileset();
        if (modified[tileset] && modifiedTilesets[tileset] != null) {
            modifiedTilesets[tileset].clearIndexedColorImage(true);
            modifiedTilesets[tileset] = null;
        }
        if (modifiedTilesets[tileset] == null) {
            modifiedTilesets[tileset] = originalTilesets[tileset].clone();
        }
    }
    
    public void updateTileset(int frame) {
        int tileset = frames[frame].getDestTileset();
        if (!modified[tileset]) {
            modifiedTilesets[tileset] = originalTilesets[tileset].clone();
        }
        int dest = frames[frame].getDestTileIndex();
        int start = frames[frame].getStart();
        int length = frames[frame].getLength();
        if (start+length > TILESET_TILES) {
            length -= start+length-TILESET_TILES;
        }
        if (dest+length > TILESET_TILES) {
            length -= dest+length-TILESET_TILES;
        }
        System.arraycopy(animationTileset.getTiles(), start, modifiedTilesets[tileset].getTiles(), dest, length);
        modified[tileset] = true;
    }
    
    public void clearData() {
        for (int i = 0; i < modifiedTilesets.length; i++) {
            if (modifiedTilesets[i] != null) {
                modifiedTilesets[i].clearIndexedColorImage(true);
                modifiedTilesets[i] = null;
            }
            modified[i] = false;
        }
        for (int i = 0; i < originalTilesets.length; i++) {
            if (originalTilesets[i] != null) {
                originalTilesets[i].clearIndexedColorImage(true);
                originalTilesets[i] = null;
            }
        }
    }
}