/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.TileFlags;
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
    public enum TilesetFillMode {
        NewOnly,
        NewFirst,
        ExistingFirst,
    }
    
    private Tileset addedTiles;
    private Tileset orphanTiles;
    private Tileset[] newTilesets;

    public OptimisedMap(ProtoMap protoMap, Tileset[] tilesets, boolean chestBlocks, TilesetFillMode fillMode, boolean[] modifyTilesets) {
        generateTilesets(protoMap.getOptimisedTileset(), tilesets, protoMap.getOptimisedBlockset(), chestBlocks, fillMode, modifyTilesets);
    }

    public Tileset getAddedTiles() {
        return addedTiles;
    }

    public Tileset getOrphanTiles() {
        return orphanTiles;
    }

    public Tileset[] getNewTilesets() {
        return newTilesets;
    }

    public void generateTilesets(Tileset optimisedTileset, Tileset[] tilesets, MapBlockset blockset, boolean chestBlocks, TilesetFillMode fillMode, boolean[] modifyTilesets) {
        Console.logger().finest("ENTERING generateTilesets");
        List<Tile> addedTilesList = new ArrayList<>();
        Queue<Tile> orphanTilesList = new LinkedList<>();
        HashMap<Integer, TileRemap> tileRemap = new HashMap<>();
        
        Tileset[] newTilesets = new Tileset[tilesets.length];
        boolean[] tilesetWasEmpty = new boolean[tilesets.length];
        for (int i = 0; i < tilesets.length; i++) {
            tilesetWasEmpty[i] = tilesets[i] == null;
            if (!tilesetWasEmpty[i]) {
                newTilesets[i] = tilesets[i].clone();
            }
        }
        
        //Remap chest tiles
        if (chestBlocks) {
            if (newTilesets[4] == null) {
                newTilesets[4] = Tileset.EmptyTilset(optimisedTileset.getPalette(), TILESET_TILES, optimisedTileset.getTilesPerRow());
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
                    tileRemap.put(i, new TileRemap((byte)4, CHEST_TILE_REMAPS[i], TileFlags.TILE_FLAG_NONE));
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
                    byte match = MapTile.tryMatchTile(opTile.getRenderPixels(), tiles[tt]);
                    if (match >= 0) {
                        tileRemap.put(i, new TileRemap((byte)t, tt, match));
                        tileFound = true;
                        break;
                    }
                }
                if (tileFound) {
                    break;
                }
            }
            if (!tileFound) {
                orphanTilesList.add(opTile);
            }
        }
        
        //Fill existing and/or new tilesets
        switch (fillMode) {
            case NewOnly:
                fillNewTilesets(optimisedTileset, newTilesets, tilesets, orphanTilesList, addedTilesList, tileRemap, tilesetWasEmpty);
                break;
            case NewFirst:
                fillNewTilesets(optimisedTileset, newTilesets, tilesets, orphanTilesList, addedTilesList, tileRemap, tilesetWasEmpty);
                addToExistingTilesets(newTilesets, tilesets, orphanTilesList, addedTilesList, tileRemap, modifyTilesets, tilesetWasEmpty);
                break;
            case ExistingFirst:
                addToExistingTilesets(newTilesets, tilesets, orphanTilesList, addedTilesList, tileRemap, modifyTilesets, tilesetWasEmpty);
                fillNewTilesets(optimisedTileset, newTilesets, tilesets, orphanTilesList, addedTilesList, tileRemap, tilesetWasEmpty);
                break;
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
                        tile.getTileFlags().toggleFlag(remap.flag());
                    }
                }
            }
        }
        
        //Finalise
        this.newTilesets = newTilesets;
        Tile[] addedTiles = new Tile[addedTilesList.size()];
        addedTilesList.toArray(addedTiles);
        this.addedTiles = new Tileset("Added", addedTiles, 10);
        Tile[] orphanTiles = new Tile[orphanTilesList.size()];
        orphanTilesList.toArray(orphanTiles);
        this.orphanTiles = new Tileset("Orphaned", orphanTiles, 10);
        Console.logger().finest("EXITING generateTilesets");
    }
    
    private void fillNewTilesets(Tileset optimisedTileset, Tileset[] newTilesets, Tileset[] tilesets, Queue<Tile> orphanTilesList, List<Tile> addedTilesList, HashMap<Integer, TileRemap> tileRemap, boolean[] tilesetWasEmpty) {
        for (int i=0; i < tilesets.length; i++) {
            if (orphanTilesList.isEmpty()) break;
            if (!tilesetWasEmpty[i]) continue;
            if (newTilesets[i] == null) {
                newTilesets[i] = Tileset.EmptyTilset(optimisedTileset.getPalette(), TILESET_TILES, optimisedTileset.getTilesPerRow());
            }
            Tile[] tiles = newTilesets[i].getTiles();
            for (int t=0; t < tiles.length; t++) {
                if (orphanTilesList.isEmpty()) {
                    break;
                }
                if (!tiles[t].isTileEmpty()) continue;
                Tile orphan = orphanTilesList.remove();
                addedTilesList.add(orphan.clone());
                tiles[t] = orphan;
                tileRemap.put(orphan.getId(), new TileRemap((byte)i, t, TileFlags.TILE_FLAG_NONE));
                orphan.setId(t);
            }
        }
    }
    
    private void addToExistingTilesets(Tileset[] newTilesets, Tileset[] tilesets, Queue<Tile> orphanTilesList, List<Tile> addedTilesList, HashMap<Integer, TileRemap> tileRemap, boolean[] modifyTilesets, boolean[] tilesetWasEmpty) {
        for (int i=0; i < tilesets.length; i++) {
            if (tilesetWasEmpty[i]) continue;
            if (newTilesets[i] == null) {
                newTilesets[i] = tilesets[i].clone();
            }
            if (!modifyTilesets[i]) continue;
            if (orphanTilesList.isEmpty()) continue;
            Tile[] tiles = newTilesets[i].getTiles();
            for (int t=0; t < tiles.length; t++) {
                if (orphanTilesList.isEmpty()) {
                    break;
                }
                if (tiles[t].isTileEmpty()) {
                    Tile orphan = orphanTilesList.remove();
                    addedTilesList.add(orphan.clone());
                    tiles[t] = orphan;
                    tileRemap.put(orphan.getId(), new TileRemap((byte)i, t, TileFlags.TILE_FLAG_NONE));
                    orphan.setId(t);
                }
            }
        }
    }
    
    //Raw pixel data for chest tiles (open and closed)
    static int[] CHEST_TILE_REMAPS = new int[] { -1,44,45,60,61,46,47,62,63,78,79 };
}
