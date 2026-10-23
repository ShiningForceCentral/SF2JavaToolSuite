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
import com.sfc.sf2.map.layout.MapLayout;
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
    private static final int TILESETS_COUNT = 5;        //5 for map tilesets and 1 orphan placeholder
    public static final byte CHEST_TILESET_INDEX = 4;   //Chest graphics go in the 5th tileset
    public static final byte ORPHAN_TILESET_INDEX = 5;  //The 6th tileset is placeholder for orphaned tiles (does not get exported)
    
    public enum TilesetFillMode {
        NewOnly,
        NewFirst,
        ExistingFirst,
    }
    
    private Tileset addedTiles;
    private Tileset orphanTiles;
    private Tileset[] newTilesets;
    private Tileset[] newTilesetsWithOrphans;
    private MapBlockset newBlockset;
    private MapLayout newLayout;

    public OptimisedMap(ProtoMap protoMap, Tileset[] tilesets, boolean chestBlocks, TilesetFillMode fillMode, boolean[] modifyTilesets) {
        generateTilesets(protoMap.getOptimisedTileset(), tilesets, protoMap.getOptimisedBlockset(), protoMap.getOptimisedLayout(), chestBlocks, fillMode, modifyTilesets);
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

    public Tileset[] getNewTilesetsWithOrphans() {
        return newTilesetsWithOrphans;
    }

    public void setNewTilesetsWithOrphans(Tileset[] newTilesetsWithOrphans) {
        this.newTilesetsWithOrphans = newTilesetsWithOrphans;
    }

    public MapBlockset getNewBlockset() {
        return newBlockset;
    }

    public MapLayout getNewLayout() {
        return newLayout;
    }

    public void generateTilesets(Tileset optimisedTileset, Tileset[] tilesets, MapBlockset blockset, MapLayout layout, boolean chestBlocks, TilesetFillMode fillMode, boolean[] modifyTilesets) {
        Console.logger().finest("ENTERING generateTilesets");
        List<Tile> addedTilesList = new ArrayList<>();
        Queue<Tile> orphanTilesList = new LinkedList<>();
        HashMap<Integer, TileRemap> tileRemap = new HashMap<>();
        
        Tileset[] newTilesets = new Tileset[TILESETS_COUNT];
        boolean[] tilesetWasEmpty = new boolean[tilesets.length];
        for (int i = 0; i < tilesets.length; i++) {
            tilesetWasEmpty[i] = tilesets[i] == null;
            if (!tilesetWasEmpty[i]) {
                newTilesets[i] = tilesets[i].clone();
            }
        }
        
        //Remap chest tiles
        if (chestBlocks) {
            if (newTilesets[CHEST_TILESET_INDEX] == null) {
                newTilesets[CHEST_TILESET_INDEX] = Tileset.EmptyTilset(optimisedTileset.getPalette(), TILESET_TILES, optimisedTileset.getTilesPerRow());
            }
            Tile[] opTiles = optimisedTileset.getTiles();
            Tile[] tiles = newTilesets[CHEST_TILESET_INDEX].getTiles();
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
                    tileRemap.put(i, new TileRemap(CHEST_TILESET_INDEX, CHEST_TILE_REMAPS[i], TileFlags.TILE_FLAG_NONE));
                }
            }
        }
        
        //Match tiles in tilesets
        Tile[] opTiles = optimisedTileset.getTiles();
        for (int i=0; i < opTiles.length; i++) {
            if (chestBlocks && i < CHEST_TILE_REMAPS.length) continue;
            Tile opTile = opTiles[i];
            int[] pixels = opTile.getRenderPixels();
            boolean tileFound = false;
            for (int t=0; t < tilesets.length; t++) {
                if (tilesets[t] == null) continue;
                Tile[] tiles = tilesets[t].getTiles();
                for (int tt = 0; tt < tiles.length; tt++) {
                    byte match = MapTile.tryMatchTile(pixels, tiles[tt]);
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
                //Add to orphaned tiles and also remap orphaned tiles to placeholder 6th tileset
                orphanTilesList.add(opTile);
                tileRemap.put(i, new TileRemap(ORPHAN_TILESET_INDEX, i, TileFlags.TILE_FLAG_NONE));
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
        MapBlockset newBlockset = blockset.clone();
        if (!tileRemap.isEmpty()) {
            MapBlock[] blocks = blockset.getBlocks();
            for (int i=0; i < blocks.length; i++) {
                blocks[i].setIndex(i);
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
        Tile[] addedTiles = new Tile[addedTilesList.size()];
        addedTilesList.toArray(addedTiles);
        this.addedTiles = new Tileset("Added", addedTiles, 10);
        Tile[] orphanTiles = new Tile[orphanTilesList.size()];
        orphanTilesList.toArray(orphanTiles);
        this.orphanTiles = new Tileset("Orphaned", orphanTiles, 10);
        newTilesetsWithOrphans = new Tileset[TILESETS_COUNT+1];
        newTilesetsWithOrphans[ORPHAN_TILESET_INDEX] = this.orphanTiles;
        for (int i = 0; i < TILESETS_COUNT; i++) {
            newTilesetsWithOrphans[i] = newTilesets[i];
        }
        
        this.newBlockset = newBlockset;
        this.newLayout = new MapLayout(0, newTilesets, layout.getBlocks());
        this.newTilesets = newTilesets;
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
    
    /**
     * Returns a blockset that no longer points to tileset 6 (for orphan tiles). Required to export map data without an invalid 6 tilesets
     */
    public MapBlockset getCleanedBlockset() {
        MapBlock[] oldBlocks = newBlockset.getBlocks();
        MapBlock[] cleanedBlocks = new MapBlock[oldBlocks.length];
        for (int i = 0; i < cleanedBlocks.length; i++) {
            cleanedBlocks[i] = oldBlocks[i].clone();
            MapTile[] tiles = cleanedBlocks[i].getMapTiles();
            for (int t = 0; t < tiles.length; t++) {
                if (tiles[t].getTilesetIndex() == ORPHAN_TILESET_INDEX) {
                    tiles[t] = MapTile.EmptyMapTile();
                }
            }
        }
        return new MapBlockset(newBlockset.getName(), cleanedBlocks, newBlockset.getBlocksPerRow());
    }
    
    //Raw pixel data for chest tiles (open and closed)
    static int[] CHEST_TILE_REMAPS = new int[] { -1,44,45,60,61,46,47,62,63,78,79 };
}
