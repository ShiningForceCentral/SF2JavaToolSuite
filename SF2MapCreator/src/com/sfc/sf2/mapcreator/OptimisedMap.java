/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import static com.sfc.sf2.helpers.MapBlockHelpers.TILESET_TILES;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.MapTile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author TiMMy
 */
public final class OptimisedMap {
    
    ProtoMap map;
    private Tile[] addedTiles;
    private MapTile[] orphanTiles;
    private Tileset[] newTilesets;
    
    private HashMap<Integer, Integer> occurences;

    public OptimisedMap(ProtoMap protoMap, Tileset[] tilesets, boolean chestBlocks, boolean canModifyTilesets) {
        map = protoMap.clone();
        generateTilesets(protoMap.getOptimisedTileset(), tilesets, protoMap.getOptimisedBlockset(), chestBlocks, canModifyTilesets);
    }

    public MapTile[] getOrphanTiles() {
        return orphanTiles;
    }

    public Tileset[] getNewTilesets() {
        return newTilesets;
    }

    public void generateTilesets(Tileset optimisedTileset, Tileset[] tilesets, MapBlockset blockset, boolean chestBlocks, boolean canModifyTilesets) {
        Console.logger().finest("ENTERING generateTilesets");
        Queue<Tile> orphanTileList = new LinkedList<>();
        HashMap<Integer, TileRemap> tileRemap = new HashMap<>();
        
        //Match tiles in tilesets
        Tile[] opTiles = optimisedTileset.getTiles();
        for (int i=0; i < opTiles.length; i++) {
            boolean tileFound = false;
            Tile opTile = opTiles[i];
            for (int t=0; t < tilesets.length; t++) {
                Tile[] tiles = tilesets[t].getTiles();
                for (int tt = 0; tt < tiles.length; tt++) {
                    if (opTile.equals(tiles[tt])) {
                        tileRemap.put(i, new TileRemap((byte)t, tt));
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
        
        //Remap chest tiles
        if (chestBlocks) {
            //TODO remap
        }
        
        //Add new tiles to empty tilesets
        Tileset[] newTilesets = new Tileset[tilesets.length];
        List<Tile> addedTilesList = new ArrayList<>();
        for (int i=0; i < tilesets.length; i++) {
            if (orphanTileList.isEmpty()) break;
            if (tilesets[i] != null) continue;
            newTilesets[i] = Tileset.EmptyTilset(optimisedTileset.getPalette(), TILESET_TILES, optimisedTileset.getTilesPerRow());
            Tile[] tiles = newTilesets[i].getTiles();
            for (int t=0; t < tiles.length; t++) {
                if (!orphanTileList.isEmpty()) {
                    Tile orphan = orphanTileList.remove();
                    tiles[t] = orphan;
                    tileRemap.put(orphan.getId(), new TileRemap((byte)i, t));
                    orphan.setId(t);
                    addedTilesList.add(orphan);
                } else {
                    break;
                }
            }
        }
        
        //Add new tiles to esisting tilesets (if enabled)
        for (int i=0; i < tilesets.length; i++) {
            if (tilesets[i] == null) continue;
            newTilesets[i] = tilesets[i].clone();
            if (!canModifyTilesets) continue;
            if (orphanTileList.isEmpty()) continue;
            Tile[] tiles = newTilesets[i].getTiles();
            for (int t=0; t < tiles.length; t++) {
                if (!orphanTileList.isEmpty()) {
                    Tile orphan = orphanTileList.remove();
                    tiles[t] = orphan;
                    tileRemap.put(orphan.getId(), new TileRemap((byte)i, t));
                    orphan.setId(t);
                    addedTilesList.add(orphan);
                } else {
                    break;
                }
            }
        }
        
        //Remap blocks a final time
        if (!tileRemap.isEmpty()) {
            MapBlock[] blocks = blockset.getBlocks();
            for (int i=0; i < blocks.length; i++) {
                MapTile[] tiles = blocks[i].getMapTiles();
                for (int t=0; t < tiles.length; t++) {
                    MapTile tile = tiles[t];
                    if (tile.getTilesetIndex() == 0 && tileRemap.containsKey(tile.getTileIndex())) {
                        TileRemap remap = tileRemap.get(tile.getTileIndex());
                        tile.setTilesetIndex(remap.tileset());
                        tile.setTileIndex(remap.index());
                    }
                }
            }
        }
        
        //Finalise
        this.newTilesets = newTilesets;
        Tile[] addedTiles = new Tile[addedTilesList.size()];
        addedTilesList.toArray(addedTiles);
        this.addedTiles = addedTiles;
        MapTile[] orphanTiles = new MapTile[orphanTileList.size()];
        orphanTileList.toArray(orphanTiles);
        this.orphanTiles = orphanTiles;
        
        Console.logger().finest("EXITING generateTilesets");
    }
}
