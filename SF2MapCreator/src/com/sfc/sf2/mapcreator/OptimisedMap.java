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
import java.util.Arrays;
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
    private Tile[] orphanTiles;
    private Tileset[] newTilesets;
    
    private HashMap<Integer, Integer> occurences;

    public OptimisedMap(ProtoMap protoMap, Tileset[] tilesets, boolean chestBlocks, boolean canModifyTilesets) {
        map = protoMap.clone();
        generateTilesets(protoMap.getOptimisedTileset(), tilesets, protoMap.getOptimisedBlockset(), chestBlocks, canModifyTilesets);
    }

    public Tile[] getOrphanTiles() {
        return orphanTiles;
    }

    public Tileset[] getNewTilesets() {
        return newTilesets;
    }

    public void generateTilesets(Tileset optimisedTileset, Tileset[] tilesets, MapBlockset blockset, boolean chestBlocks, boolean canModifyTilesets) {
        Console.logger().finest("ENTERING generateTilesets");
        Queue<Tile> orphanTileList = new LinkedList<>();
        HashMap<Integer, TileRemap> tileRemap = new HashMap<>();
        
        Tileset[] newTilesets = new Tileset[tilesets.length];
        //Remap chest tiles
        if (chestBlocks) {
            if (tilesets[4] == null) {
                newTilesets[4] = Tileset.EmptyTilset(optimisedTileset.getPalette(), TILESET_TILES, optimisedTileset.getTilesPerRow());
            } else {
                newTilesets[4] = tilesets[4].clone();
            }
            Tile[] opTiles = optimisedTileset.getTiles();
            Tile[] tiles = newTilesets[4].getTiles();
            if (!Arrays.equals(tiles[CHEST_TILE_REMAPS[1]].getPixels(), opTiles[1].getPixels())) {
                //Tileset 5 is missing chest graphics
                for (int i = 0; i < CHEST_TILE_REMAPS.length; i++) {
                    if (CHEST_TILE_REMAPS[i] >= 0) {
                        Tile chestTile = new Tile(CHEST_TILE_REMAPS[i], opTiles[i].getPixels(), opTiles[i].getPalette());
                        tiles[CHEST_TILE_REMAPS[i]] = chestTile;
                    }
                }
            }
            newTilesets[4].setTiles(tiles);
            //Remap placeholder chest tiles to final chest tiles
            for (int i = 0; i < CHEST_TILE_REMAPS.length; i++) {
                if (CHEST_TILE_REMAPS[i] >= 0) {
                    tileRemap.put(i, new TileRemap((byte)4, CHEST_TILE_REMAPS[i]));
                }
            }
        }
        
        //Match tiles in tilesets
        Tile[] opTiles = optimisedTileset.getTiles();
        for (int i=0; i < opTiles.length; i++) {
            if (chestBlocks && i < CHEST_TILE_REMAPS.length) continue;
            boolean tileFound = false;
            Tile opTile = opTiles[i];
            for (int t=0; t < tilesets.length; t++) {
                if (tilesets[t] == null) continue;
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
        
        //Add new tiles to empty tilesets
        List<Tile> addedTilesList = new ArrayList<>();
        for (int i=0; i < tilesets.length; i++) {
            if (orphanTileList.isEmpty()) break;
            if (tilesets[i] != null) continue;
            if (newTilesets[i] == null) {
                newTilesets[i] = Tileset.EmptyTilset(optimisedTileset.getPalette(), TILESET_TILES, optimisedTileset.getTilesPerRow());
            }
            Tile[] tiles = newTilesets[i].getTiles();
            for (int t=0; t < tiles.length; t++) {
                if (orphanTileList.isEmpty()) {
                    break;
                }
                if (!tiles[t].isTileEmpty()) continue;
                Tile orphan = orphanTileList.remove();
                tiles[t] = orphan;
                tileRemap.put(orphan.getId(), new TileRemap((byte)i, t));
                orphan.setId(t);
                addedTilesList.add(orphan);
            }
        }
        
        //Add new tiles to esisting tilesets (if enabled)
        for (int i=0; i < tilesets.length; i++) {
            if (tilesets[i] == null) continue;
            if (newTilesets[i] == null) {
                newTilesets[i] = tilesets[i].clone();
            }
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
        Tile[] orphanTiles = new Tile[orphanTileList.size()];
        orphanTileList.toArray(orphanTiles);
        this.orphanTiles = orphanTiles;
        Console.logger().finest("EXITING generateTilesets");
    }
    
    //Raw pixel data for chest tiles (open and closed)
    static int[] CHEST_TILE_REMAPS = new int[] { -1,44,45,60,61,46,47,62,63,87,88 };
}
