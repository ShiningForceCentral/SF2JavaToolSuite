/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.MapTile;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public class ProtoMap implements INameable {
    //Core data
    private String name;
    private Tileset rawTileset;
    private MapBlockset rawBlockset;
    private MapLayout rawLayout;
    //Optimised data
    private Tileset optimisedTileset;
    private MapBlockset optimisedBlockset;
    private MapLayout optimisedLayout;

    public ProtoMap(String name, Tileset tileset, MapBlockset blockset, MapLayout layout, boolean chestBlocks) {
        this.name = name;
        this.rawTileset = tileset;
        this.rawBlockset = blockset;
        this.rawLayout = layout;
        optimiseTileset(tileset, blockset, chestBlocks);
        optimiseLayout(blockset, layout, chestBlocks);
    }

    private ProtoMap(String name, Tileset rawTileset, MapBlockset rawBlockset, MapLayout layout, Tileset optimisedTileset, MapBlockset optimisedBlockset, MapLayout optimisedLayout) {
        this.name = name;
        this.rawTileset = rawTileset;
        this.rawBlockset = rawBlockset;
        this.rawLayout = layout;
        this.optimisedTileset = optimisedTileset;
        this.optimisedBlockset = optimisedBlockset;
        this.optimisedLayout = optimisedLayout;
    }

    @Override
    public String getName() {
        return name;
    }

    public Tileset getRawTileset() {
        return rawTileset;
    }

    public MapBlockset getRawBlockset() {
        return rawBlockset;
    }

    public MapLayout getRawLayout() {
        return rawLayout;
    }

    public Tileset getOptimisedTileset() {
        return optimisedTileset;
    }

    public MapBlockset getOptimisedBlockset() {
        return optimisedBlockset;
    }

    public MapLayout getOptimisedLayout() {
        return optimisedLayout;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProtoMap)) return super.equals(obj);
        ProtoMap protoMap = (ProtoMap)obj;
        if (!this.name.equals(protoMap.name)) return false;
        if (!rawBlockset.equals(protoMap.rawBlockset)) return false;
        if (!rawLayout.equals(protoMap.rawLayout)) return false;
        return true;
    }
    
    public ProtoMap clone() {
        return new ProtoMap(name, rawTileset.clone(), rawBlockset.clone(), rawLayout.clone(), optimisedTileset.clone(), optimisedBlockset.clone(), optimisedLayout.clone());
    }
    
    public void optimiseTileset(Tileset tileset, MapBlockset blockset, boolean chestBlocks) {
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
                int index = optimisedTilesList.size();
                Tile optimisedTile = origTiles[i].clone();
                optimisedTile.setId(index);
                optimisedTilesList.add(optimisedTile);
                tilesRemap.put(i, index);
            }
        }
        Tile[] optimisedTileset = new Tile[optimisedTilesList.size()];
        optimisedTilesList.toArray(optimisedTileset);
        this.optimisedTileset = new Tileset(tileset.getName(), optimisedTileset, tileset.getTilesPerRow());
        
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
                    if (origBlocks[b].getMapTiles()[t].getTileFlags().isPriority()) {
                        match |= TileFlags.TILE_FLAG_PRIORITY;
                    }
                    tiles[t].setTileFlags(new TileFlags(match));
                }
                tiles[t].setTileIndex(ti);
            }
            optimisedBlocks[b] = block;
        }
        this.optimisedBlockset = new MapBlockset(blockset.getName(), optimisedBlocks, blockset.getBlocksPerRow());
        Console.logger().finest("EXITING generateOptimisedTileset");
    }
    
    public void optimiseLayout(MapBlockset blockset, MapLayout mapLayout, boolean chestBlocks) {
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
        optimisedLayout = new MapLayout(0, new Tileset[] { optimisedTileset }, layoutBlocks);
        Console.logger().finest("EXITING generateOptimisedBlockset");
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
