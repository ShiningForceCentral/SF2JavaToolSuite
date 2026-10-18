/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.MapTile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public final class OptimisedMap {
    
    ProtoMap map;
    private MapTile[] orphanTiles;
    private Tileset[] optimisedTilesets;
    
    private HashMap<Integer, Integer> occurences;

    public OptimisedMap(ProtoMap protoMap, boolean chestBlocks, boolean canModifyTilesets) {
        map = protoMap.clone();
        /*generateTilesets(optimisedOrigTileset, newTilesets, optimisedBlockset, canModifyTilesets);*/
    }

    public MapTile[] getOrphanTiles() {
        return orphanTiles;
    }

    public Tileset[] getOptimisedTilesets() {
        return optimisedTilesets;
    }

    public void generateTilesets(Tileset optimisedTileset, Tileset[] tilesets, MapBlockset blockset, boolean canModifyTilesets) {
        Console.logger().finest("ENTERING generateTilesets");
        List<Tile> orphanTileList = new ArrayList();
        HashMap<Integer, TileRemap> tileRemap = new HashMap<>();
        Tile[] opTiles = optimisedTileset.getTiles();
        for (int i=0; i < opTiles.length; i++) {
            boolean tileFound = false;
            Tile opTile = opTiles[i];
            for (int t=0; t < tilesets.length; t++) {
                Tile[] tiles = tilesets[t].getTiles();
                for (int tt = 0; tt < tiles.length; tt++) {
                    if (opTile.equals(tiles[tt])) {
                        
                        tileFound = true;
                        break;
                    }
                }
                if (tileFound) {
                    break;
                }
            }
            if (!tileFound) {
                orphanTileList.add(opTile);
            }
        }
        
        Console.logger().finest("EXITING generateTilesets");
    }
}
