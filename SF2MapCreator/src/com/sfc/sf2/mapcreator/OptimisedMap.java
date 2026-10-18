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
import com.sfc.sf2.map.layout.MapLayoutBlock;
import com.sfc.sf2.palette.Palette;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public final class OptimisedMap {
    ProtoMap map;
    private MapBlockset optimisedBlockset;
    private MapTile[] orphanTiles;
    private HashMap<Integer, Integer> occurences;
    private Tileset[] newTilesets;

    public OptimisedMap(ProtoMap protoMap, Tileset[] newTilesets, boolean chestBlocks, boolean canModifyTilesets) {
        map = protoMap.clone();
        generateOptimisedBlockset(map, chestBlocks);
        //calculateOrphanedTiles(protoMap.getLayout().getTilesets(), newTilesets);
        //generateTilesets(newTilesets, canModifyTilesets);
    }

    public MapBlockset getOptimizedBlockset() {
        return optimisedBlockset;
    }

    public MapTile[] getOrphanTiles() {
        return orphanTiles;
    }

    public Tileset[] getNewTilesets() {
        return newTilesets;
    }
    
    public void generateOptimisedBlockset(ProtoMap map, boolean chestBlocks) {
        Console.logger().finest("ENTERING generateOptimisedBlockset");
        MapBlock[] optimizedBlockset = null;
        MapBlock[] blocks = map.getBlockset().getBlocks();
        List<MapBlock> optimisedBlockList = new ArrayList();
        optimisedBlockList.add(MapBlock.EmptyMapBlock(0));   //First block is blank
        if (chestBlocks) {  //Add chest blocks
            optimisedBlockList.add(MapBlock.createChestMapBlock(false));
            optimisedBlockList.add(MapBlock.createChestMapBlock(true));
        }
        for (int i=0; i < blocks.length; i++) {
            boolean found = false;
            for (int j=0;j<optimisedBlockList.size();j++){
                if(blocks[i].equals(optimisedBlockList.get(j))){
                    found = true;
                    break;
                }
            }
            if (!found) {
                blocks[i].setIndex(optimisedBlockList.size());
                optimisedBlockList.add(blocks[i]);
            }
        }
        optimizedBlockset = new MapBlock[optimisedBlockList.size()];
        optimisedBlockList.toArray(optimizedBlockset);
        this.optimisedBlockset = new MapBlockset(map.getName(), optimizedBlockset, map.getBlockset().getBlocksPerRow());
        
        //Re-assign layout block indexes
        MapLayoutBlock[] layout = map.getLayout().getBlocks();
        MapBlock[] blockset = optimizedBlockset;
        for (int b=0; b < layout.length; b++) {
            for (int i=0;i < blockset.length; i++) {
                if (layout[b].getMapBlock().equals(blockset[i])) {
                    layout[b].getMapBlock().setIndex(i);
                    break;
                }
            }
        }
        Console.logger().finest("EXITING generateOptimisedBlockset");
    }
    
    public void calculateOrphanedTiles(Tileset[] oldTilesets, Tileset[] newTilesets) {
        Console.logger().finest("ENTERING generateOrphanedTiles");
        MapTile[] orphanTiles = null;
        List<MapTile> orphanTileList = new ArrayList();
        MapBlock[] blockset = optimisedBlockset.getBlocks();
        for (int b = 0; b < blockset.length; b++) {
            MapBlock block = blockset[b];
            for (int t = 0; t < block.getMapTiles().length; t++) {
                int targetId = -1;
                MapTile tile = block.getMapTiles()[t];
                int[] pixels = tile.getTile(oldTilesets).getRenderPixels(tile.getTileFlags());
                for (int i = 0; i < newTilesets.length; i++) {
                    Tile[] tiles = newTilesets[i].getTiles();
                    for (int j = 0; j < tiles.length; j++) {
                        byte match = MapTile.tryMatchTile(pixels, tiles[j]);
                        if (match >= 0) {
                            targetId = i*128 + j;
                            //Console.logger().fine(String.format("Found blockset block %d tile %d in tileset i:j, flip: %d", b, t, i, j, match));
                            break;
                        }
                    }
                    if (targetId >= 0) {
                        break;
                    }
                }
                if (targetId < 0) {
                    Console.logger().fine(String.format("  BLOCKSET BLOCK %d TILE %d NOT FOUND IN AVAILABLE TILESETS", b, t));
                    orphanTileList.add(block.getMapTiles()[t]);
                }
            }
        }

        HashMap<Integer, Integer> occurences = new HashMap<>();
        List<MapTile> optimTileList = new ArrayList();
        for (int i = 0; i < orphanTileList.size(); i++) {
            boolean found = false;
            MapTile tile = orphanTileList.get(i);
            int[] pixels = tile.getTile(newTilesets).getRenderPixels(tile.getTileFlags());
            for (int j = 0; j < optimTileList.size(); j++) {
                byte match = MapTile.tryMatchTile(pixels, optimTileList.get(j).getTile(newTilesets));
                if (match >= 0) {
                    int occ = occurences.getOrDefault(j, 0)+1;
                    occurences.put(j, occ);
                    found = true;
                    break;
                }
            }
            if (!found) {
                optimTileList.add(orphanTileList.get(i));
            }
        }
        orphanTiles = new MapTile[optimTileList.size()];
        optimTileList.toArray(orphanTiles);
        this.occurences = occurences;
        this.orphanTiles = orphanTiles;
        Console.logger().finest("EXITING generateOrphanedTiles");
    }

    public void generateTilesets(Tileset[] tilesets, boolean canModifyTilesets) {
        Console.logger().finest("ENTERING generateTilesets");
        Palette palette = map.getLayout().getPalette();

        Tile emptyTile = Tile.EmptyTile(palette);
        Tileset[] newTilesets = new Tileset[5];
        Tileset[] emptyTilesets = new Tileset[5];
        Tileset[] nonEmptyTilesets = new Tileset[5];
        for (int i = 0; i < newTilesets.length; i++) {
            newTilesets[i] = tilesets[i].clone();
        }
        for (int i = 0; i < newTilesets.length; i++) {
            boolean empty = true;
            Tile[] tiles = newTilesets[i].getTiles();
            for (int j = 0; j < tiles.length; j++) {
                if (!tiles[j].equals(emptyTile)) {
                    empty = false;
                    nonEmptyTilesets[i] = newTilesets[i];
                    break;
                }
            }
            if (empty) {
                emptyTilesets[i] = newTilesets[i];
            }
        }

        MapTile[] orphanTiles = this.orphanTiles;
        for (int t = 0; t < orphanTiles.length; t++) {
            MapTile tile = orphanTiles[t];
            boolean assigned = false;
            for (int i = 0; i < emptyTilesets.length; i++) {
                if (emptyTilesets[i] != null) {
                    Tile[] tiles = emptyTilesets[i].getTiles();
                    for (int j = 0; j < tiles.length; j++) {
                        if (!(i == 0 && j == 0) && tiles[j].equals(emptyTile)) {
                            tiles[j] = tile.getTile(tilesets);
                            assigned = true;
                            break;
                        }
                    }
                    if (assigned) {
                        break;
                    } else {
                        emptyTilesets[i] = null;
                    }
                }
            }
            if (!assigned && canModifyTilesets) {
                for (int i = 0; i < nonEmptyTilesets.length; i++) {
                    if (nonEmptyTilesets[nonEmptyTilesets.length - 1 - i] != null) {
                        Tile[] tiles = nonEmptyTilesets[nonEmptyTilesets.length - 1 - i].getTiles();
                        for (int j = 0; j < tiles.length; j++) {
                            if (!(i == 0 && j == 0) && tiles[j].equals(emptyTile)) {
                                tiles[j] = tile.getTile(tilesets);
                                assigned = true;
                                break;
                            }
                        }
                        if (assigned) {
                            break;
                        } else {
                            nonEmptyTilesets[nonEmptyTilesets.length - 1 - i] = null;
                        }
                    }
                }
            }
        }
        
        tilesets = newTilesets;
        //Re-assign tilesets tile indexes
        for (int i = 0; i < tilesets.length; i++) {
            Tile[] tiles = tilesets[i].getTiles();
            for (int j = 0; j < tiles.length; j++) {
                tiles[j].setId(i*TILESET_TILES + j);
            }
        }
        
        //Re-assign block tile indexes
        MapBlock[] blockset = optimisedBlockset.getBlocks();
        tilesets = newTilesets;
        //Console.logger().fine(blockset[3].getMapTiles()[0].getId());
        for (int b = 0; b < blockset.length; b++) {
            MapBlock block = blockset[b];
            for (int t = 0; t < block.getMapTiles().length; t++) {
                MapTile tile = block.getMapTiles()[t];
                int[] pixels = tile.getTile(tilesets).getRenderPixels(tile.getTileFlags());
                boolean found = false;
                for (int i = 0; i < tilesets.length; i++) {
                    Tile[] tiles = tilesets[i].getTiles();
                    for (int j = 0; j < tiles.length; j++) {
                        byte match = MapTile.tryMatchTile(pixels, tiles[j]);
                        if (match >= 0) {
                            tile.setTileIndex(i*TILESET_TILES + j);
                            tile.setTileFlags(new TileFlags(match));
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
            }
        }
        this.newTilesets = newTilesets;
        Console.logger().finest("EXITING generateTilesets");
    }
}
