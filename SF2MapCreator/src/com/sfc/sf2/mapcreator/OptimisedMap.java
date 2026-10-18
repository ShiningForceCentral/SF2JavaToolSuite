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
    private Tileset optimisedOrigTileset;
    private MapBlockset optimisedBlockset;
    private MapLayout optimisedLayout;
    private MapTile[] orphanTiles;
    private HashMap<Integer, Integer> occurences;
    private Tileset[] optimisedTilesets;

    public OptimisedMap(ProtoMap protoMap, Tileset[] newTilesets, boolean chestBlocks, boolean canModifyTilesets) {
        map = protoMap.clone();
        generateOptimisedTileset(map.getLayout().getTilesets()[0], map.getBlockset(), chestBlocks);
        this.optimisedTilesets = new Tileset[] { optimisedOrigTileset }; //TEMP
        generateOptimisedBlockset(optimisedBlockset, map.getLayout(), chestBlocks);
        //calculateOrphanedTiles(protoMap.getLayout().getTilesets(), newTilesets);
        //generateTilesets(newTilesets, canModifyTilesets);
    }

    public MapBlockset getOptimizedBlockset() {
        return optimisedBlockset;
    }

    public MapTile[] getOrphanTiles() {
        return orphanTiles;
    }

    public Tileset[] getOptimisedTilesets() {
        return optimisedTilesets;
    }

    public MapLayout getOptimisedLayout() {
        return optimisedLayout;
    }
    
    public void generateOptimisedTileset(Tileset tileset, MapBlockset blockset, boolean chestBlocks) {
        Console.logger().finest("ENTERING generateOptimisedTileset");
        Tile[] origTiles = tileset.getTiles();
        //Remove duplicate tiles and remap tiles
        HashMap<Integer, Integer> tilesRemap = new HashMap<>();
        List<Tile> optimisedTilesList = new ArrayList();
        optimisedTilesList.add(Tile.EmptyTile(tileset.getPalette()));   //First block is blank
        if (chestBlocks) {  //Add chest tiles
            for (int i = 0; i < CHEST_TILE_PIXELS.length; i++) {
                optimisedTilesList.add(new Tile(i+1, CHEST_TILE_PIXELS[i], tileset.getPalette()));
            }
        }
        for (int i=0; i < origTiles.length; i++) {
            boolean found = false;
            int[] pixels = origTiles[i].getRenderPixels();
            for (int j=0; j < optimisedTilesList.size(); j++) {
                byte match = MapTile.tryMatchTile(pixels, optimisedTilesList.get(j));
                if (match >= 0) {
                    tilesRemap.put(i, j);
                    found = true;
                    break;
                }
            }
            if (!found) {
                optimisedTilesList.add(origTiles[i].clone());
                tilesRemap.put(i, optimisedTilesList.size()-1);
            }
        }
        Tile[] optimisedTileset = new Tile[optimisedTilesList.size()];
        optimisedTilesList.toArray(optimisedTileset);
        this.optimisedOrigTileset = new Tileset(tileset.getName(), optimisedTileset, tileset.getTilesPerRow());
        
        //Re-assign blockset tile indices and check for flips
        Tileset[] tilesets = { tileset };
        MapBlock[] origBlocks = blockset.getBlocks();
        MapBlock[] optimisedBlocks = new MapBlock[origBlocks.length];
        for (int b=0; b < origBlocks.length; b++) {
            MapBlock block = origBlocks[b].clone();
            MapTile[] tiles = block.getMapTiles();
            for (int t=0; t < tiles.length; t++) {
                int ti = tiles[t].getTileIndex();
                ti = tilesRemap.get(ti);
                byte match = origBlocks[b].getMapTiles()[t].tryMatchTile(tilesets, optimisedTileset[ti]);
                if (match > 0) {
                    tiles[t].setTileFlags(new TileFlags(match));
                }
                tiles[t].setTileIndex(ti);
            }
            optimisedBlocks[b] = block;
        }
        this.optimisedBlockset = new MapBlockset(blockset.getName(), optimisedBlocks, blockset.getBlocksPerRow());
        Console.logger().finest("EXITING generateOptimisedTileset");
    }
    
    public void generateOptimisedBlockset(MapBlockset blockset, MapLayout mapLayout, boolean chestBlocks) {
        Console.logger().finest("ENTERING generateOptimisedBlockset");
        MapBlock[] blocks = blockset.getBlocks();
        //Add default blocks
        HashMap<Integer, Integer> blocksRemap = new HashMap<>();
        List<MapBlock> optimisedBlockList = new ArrayList();
        optimisedBlockList.add(MapBlock.EmptyMapBlock(0));   //First block is blank
        if (chestBlocks) {  //Add chest blocks
            optimisedBlockList.add(createChestMapBlock(false));
            optimisedBlockList.add(createChestMapBlock(true));
        }
        //Remove duplicate blocks
        for (int i=0; i < blocks.length; i++) {
            boolean found = false;
            for (int j=0; j < optimisedBlockList.size(); j++) {
                if (blocks[i].areTilesEqual(optimisedBlockList.get(j))) {
                    blocksRemap.put(i, j);
                    found = true;
                    break;
                }
            }
            if (!found) {
                optimisedBlockList.add(blocks[i].clone());
                blocksRemap.put(i, optimisedBlockList.size()-1);
            }
        }
        MapBlock[] optimisedBlocks = new MapBlock[optimisedBlockList.size()];
        optimisedBlockList.toArray(optimisedBlocks);
        this.optimisedBlockset.setBlocks(optimisedBlocks);

        //Re-assign layout block indexes
        MapLayoutBlock[] layoutBlocks = mapLayout.getBlocks().clone();
        for (int b=0; b < layoutBlocks.length; b++) {
            int bi = layoutBlocks[b].getMapBlock().getIndex();
            bi = blocksRemap.get(bi);
            layoutBlocks[b].getMapBlock().setIndex(bi);
        }
        optimisedLayout = new MapLayout(0, mapLayout.getTilesets(), layoutBlocks);
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
                int[] pixels;
                if (tile.getTilesetIndex() > 0) {
                    pixels = tile.getTile(newTilesets).getRenderPixels(tile.getTileFlags());    //For injected chest graphics
                } else {
                    pixels = tile.getTile(oldTilesets).getRenderPixels(tile.getTileFlags());
                }
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
        this.optimisedTilesets = newTilesets;
        Console.logger().finest("EXITING generateTilesets");
    }
    
    public MapBlock createChestMapBlock(boolean isOpen) {
        byte tileset = 0;
        int openOffset = isOpen ? 0 : 4;
        MapTile[] tiles = new MapTile[9];
        tiles[0] = new MapTile(tileset, (byte)(1+openOffset), new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[1] = new MapTile(tileset, (byte)(2+openOffset), new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[2] = new MapTile(tileset, (byte)(1+openOffset), new TileFlags(TileFlags.TILE_FLAG_HFLIP));
        tiles[3] = new MapTile(tileset, (byte)(3+openOffset), new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[4] = new MapTile(tileset, (byte)(4+openOffset), new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[5] = new MapTile(tileset, (byte)(3+openOffset), new TileFlags(TileFlags.TILE_FLAG_HFLIP));
        tiles[6] = new MapTile(tileset, (byte)9, new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[7] = new MapTile(tileset, (byte)10, new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[8] = new MapTile(tileset, (byte)9, new TileFlags(TileFlags.TILE_FLAG_HFLIP));
        MapBlock block = new MapBlock(isOpen ? 2 : 1, tiles);
        return block;
    }

    //Raw pixel data for chest tiles (open and closed)
    static byte[][] CHEST_TILE_PIXELS = new byte[][] {
        new byte[] { 8,9,10,9,8,8,8,8,8,9,11,9,4,5,13,13,8,9,15,9,4,5,13,13,8,9,11,9,4,5,13,13,8,9,11,9,4,5,5,5,8,9,11,9,3,4,4,4,8,9,10,9,3,3,3,3,8,9,10,9,9,9,9,9 },
        new byte[] { 8,8,9,10,10,9,8,8,5,4,9,11,11,9,4,5,5,4,9,15,15,9,4,5,5,4,9,11,11,9,4,5,5,4,9,11,11,9,4,5,4,3,9,11,11,9,3,4,3,3,9,10,10,9,3,3,9,9,9,15,11,9,9,9 },
        new byte[] { 8,9,10,11,11,11,11,11,8,9,10,10,10,10,10,10,8,9,9,9,9,9,9,9,8,9,8,8,8,8,8,8,8,9,9,8,8,8,8,8,8,9,10,8,8,8,8,8,8,9,11,8,8,8,8,8,8,9,15,8,8,8,8,8 },
        new byte[] { 11,11,11,9,9,11,11,11,10,10,10,10,10,10,10,10,9,9,9,9,9,9,9,9,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,9,9,8,8,8 },
        new byte[] { 8,9,9,9,8,8,8,8,8,9,10,9,9,9,9,9,8,9,11,9,4,5,5,5,8,9,15,9,4,5,13,13,8,9,15,9,4,5,13,13,8,9,15,9,4,5,13,13,8,9,15,9,4,5,13,13,8,9,11,9,4,5,13,13 },
        new byte[] { 8,8,9,9,9,9,8,8,9,9,9,10,10,9,9,9,5,4,9,11,11,9,4,5,5,4,9,15,15,9,4,5,5,4,9,15,15,9,4,5,5,4,9,15,15,9,4,5,5,4,9,15,15,9,4,5,5,4,9,11,11,9,4,5 },
        new byte[] { 8,9,11,9,4,5,5,5,8,9,11,9,4,4,5,5,8,9,10,9,3,4,4,4,8,9,10,9,3,3,3,3,8,9,10,9,9,9,9,9,8,9,10,11,11,11,11,11,8,9,10,10,10,10,10,10,8,8,8,8,8,8,8,8 },
        new byte[] { 5,4,9,11,11,9,4,5,4,4,9,11,11,9,3,4,4,3,9,10,10,9,3,4,3,3,9,10,10,9,3,3,9,9,9,10,10,9,9,9,11,11,11,9,9,11,11,11,10,10,10,11,11,10,10,10,8,8,8,10,10,8,8,8 },
        new byte[] { 8,9,11,11,10,9,10,10,8,9,11,8,8,8,8,8,8,9,10,8,3,4,4,5,8,9,9,8,3,3,4,4,8,9,9,8,3,3,3,3,8,9,9,8,8,9,8,8,8,9,10,9,9,8,9,9,8,8,8,8,8,8,8,8 },
        new byte[] { 10,9,11,15,15,11,9,10,8,9,11,8,8,11,9,8,4,9,10,11,11,10,9,4,4,8,9,9,9,9,8,4,3,3,3,3,3,3,3,3,8,8,8,8,8,8,8,8,9,9,10,10,10,10,9,9,8,8,8,8,8,8,8,8 }
    };
}
