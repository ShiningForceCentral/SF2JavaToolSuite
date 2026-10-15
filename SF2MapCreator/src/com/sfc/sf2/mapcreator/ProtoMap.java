/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.MapTile;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class ProtoMap implements INameable {
    //Core data
    private String name;
    private MapBlockset blockset;
    private MapLayout layout;
    
    //Optimisation data
    private MapBlockset optimizedBlockset;
    private Tileset[] newTilesets;
    private MapTile[] orphanTiles;

    public ProtoMap(String name, MapBlockset blockset, MapLayout layout) {
        this.name = name;
        this.blockset = blockset;
        this.layout = layout;
    }

    @Override
    public String getName() {
        return name;
    }

    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
    }

    public MapLayout getLayout() {
        return layout;
    }

    public void setLayout(MapLayout layout) {
        this.layout = layout;
    }

    public Tileset[] getNewTilesets() {
        return newTilesets;
    }

    public void setNewTilesets(Tileset[] newTilesets) {
        this.newTilesets = newTilesets;
    }

    public MapTile[] getOrphanTiles() {
        return orphanTiles;
    }

    public void setOrphanTiles(MapTile[] orphanTiles) {
        this.orphanTiles = orphanTiles;
    }

    public MapBlockset getOptimizedBlockset() {
        return optimizedBlockset;
    }

    public void setOptimizedBlockset(MapBlockset optimizedBlockset) {
        this.optimizedBlockset = optimizedBlockset;
    }
    
    public void generateOrphanedTiles() {
        /*MapTile[] orphanTiles = null;
        List<MapTile> orphanTileList = new ArrayList();
        MapBlock[] blockset = getOptimizedBlockset();
        for (int b = 0; b < blockset.length; b++) {
            MapBlock block = blockset[b];
            for (int t = 0; t < block.getMapTiles().length; t++) {
                int targetId = -1;
                for (int i = 0; i < layout.getTilesets().length; i++) {
                    MapTile[] tiles = blockset[i].getMapTiles();
                    if (targetId >= 0) {
                        break;
                    }
                    MapTile tile = block.getMapTiles()[t];
                    int[] pixels = tile.getTile(layout.getTilesets()).getRenderPixels(tile.getTileFlags());
                    for (int j = 0; j < tiles.length; j++) {
                        Tile testTile = tiles[j].getTile(layout.getTilesets());
                        if (Arrays.equals(pixels, testTile.getRenderPixels())) {
                            targetId = i * 128 + j;
                            System.out.println("Found blockset block " + b + " tile " + t + " in tileset " + i + " : " + j + " (no flips)");
                            break;
                        }
                        if (Arrays.equals(pixels, testTile.getRenderPixels(new TileFlags(TileFlags.TILE_FLAG_VFLIP)))) {
                            targetId = i * 128 + j;
                            System.out.println("Found blockset block " + b + " tile " + t + " in tileset " + i + " : " + j + " (V flip)");
                            break;
                        }
                        if (Arrays.equals(pixels, testTile.getRenderPixels(new TileFlags(TileFlags.TILE_FLAG_HFLIP)))) {
                            targetId = i * 128 + j;
                            System.out.println("Found blockset block " + b + " tile " + t + " in tileset " + i + " : " + j + " (H flip)");
                            break;
                        }
                        if (Arrays.equals(pixels, testTile.getRenderPixels(new TileFlags(TileFlags.TILE_FLAG_BOTHFLIP)))) {
                            targetId = i * 128 + j;
                            System.out.println("Found blockset block " + b + " tile " + t + " in tileset " + i + " : " + j + " (V+H flips)");
                            break;
                        }
                    }
                }
                if (targetId < 0) {
                    System.out.println("  BLOCKSET BLOCK " + b + " TILE " + t + " NOT FOUND IN AVAILABLE TILESETS");
                    orphanTileList.add(block.getMapTiles()[t]);
                }
            }
        }

        List<MapTile> optimTileList = new ArrayList();
        for (int i = 0; i < orphanTileList.size(); i++) {
            boolean found = false;
            MapTile tile = orphanTileList.get(i);
            int[] pixels = tile.getTile(layout.getTilesets()).getRenderPixels(tile.getTileFlags());
            for (int j = 0; j < optimTileList.size(); j++) {
                Tile testTile = optimTileList.get(j).getTile(layout.getTilesets());
                if (Arrays.equals(pixels, testTile.getRenderPixels())) {
                    found = true;
                    optimTileList.get(j).setOccurrences(optimTileList.get(j).getOccurrences() + 1);
                    break;
                }
                if (Arrays.equals(pixels, testTile.getRenderPixels(new TileFlags(TileFlags.TILE_FLAG_VFLIP)))) {
                    found = true;
                    optimTileList.get(j).setOccurrences(optimTileList.get(j).getOccurrences() + 1);
                    break;
                }
                if (Arrays.equals(pixels, testTile.getRenderPixels(new TileFlags(TileFlags.TILE_FLAG_HFLIP)))) {
                    found = true;
                    optimTileList.get(j).setOccurrences(optimTileList.get(j).getOccurrences() + 1);
                    break;
                }
                if (Arrays.equals(pixels, testTile.getRenderPixels(new TileFlags(TileFlags.TILE_FLAG_BOTHFLIP)))) {
                    found = true;
                    optimTileList.get(j).setOccurrences(optimTileList.get(j).getOccurrences() + 1);
                    break;
                }
            }
            if (!found) {
                orphanTileList.get(i).setOccurrences(0);
                optimTileList.add(orphanTileList.get(i));
            }
        }
        orphanTiles = new MapTile[optimTileList.size()];
        optimTileList.toArray(orphanTiles);
        this.orphanTiles = orphanTiles;*/
    }

    public void generateTilesets() {
        /*Palette palette = map.getLayout().getPalette();

        Tile emptyTile = Tile.EmptyTile(palette);
        Tileset[] newTilesets = new Tileset[5];
        Tileset[] emptyTilesets = new Tileset[5];
        Tileset[] nonEmptyTilesets = new Tileset[5];
        map.setNewTilesets(newTilesets);
        for (int i = 0; i < newTilesets.length; i++) {
            newTilesets[i] = map.getTilesets()[i].clone();
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

        for (int t = 0; t < map.getOrphanTiles().length; t++) {
            Tile tile = map.getOrphanTiles()[t];
            boolean assigned = false;
            for (int i = 0; i < emptyTilesets.length; i++) {
                if (emptyTilesets[i] != null) {
                    Tile[] tiles = emptyTilesets[i].getTiles();
                    for (int j = 0; j < tiles.length; j++) {
                        if (!(i == 0 && j == 0) && tiles[j].equals(emptyTile)) {
                            tiles[j] = tile;
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
            if (!assigned) {
                for (int i = 0; i < nonEmptyTilesets.length; i++) {
                    if (nonEmptyTilesets[nonEmptyTilesets.length - 1 - i] != null) {
                        Tile[] tiles = nonEmptyTilesets[nonEmptyTilesets.length - 1 - i].getTiles();
                        for (int j = 0; j < tiles.length; j++) {
                            if (!(i == 0 && j == 0) && tiles[j].equals(emptyTile)) {
                                tiles[j] = tile;
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

        MapBlock[] blockset = map.getOptimizedBlockset();
        Tileset[] tilesets = map.getNewTilesets();
        //Re-assign tilesets tile indexes
        tilesets = map.getNewTilesets();
        for (int i = 0; i < tilesets.length; i++) {
            Tile[] tiles = tilesets[i].getTiles();
            for (int j = 0; j < tiles.length; j++) {
                tiles[j].setId(i * 128 + j);
            }
        }
        //Re-assign block tile indexes
        blockset = map.getOptimizedBlockset();
        tilesets = map.getNewTilesets();
        System.out.println(blockset[3].getTiles()[0].getId());
        for (int b = 0; b < blockset.length; b++) {
            MapBlock block = blockset[b];
            for (int t = 0; t < block.getTiles().length; t++) {
                Tile tile = block.getTiles()[t];
                boolean found = false;
                for (int i = 0; i < tilesets.length; i++) {
                    Tile[] tiles = tilesets[i].getTiles();
                    for (int j = 0; j < tiles.length; j++) {
                        if (tile.equals(tiles[j])) {
                            tile.setId(i * 128 + j);
                            tile.sethFlip(false);
                            tile.setvFlip(false);
                            found = true;
                            break;
                        }
                        if (tile.equals(Tile.hFlip(tiles[j]))) {
                            tile.setId(i * 128 + j);
                            tile.sethFlip(true);
                            tile.setvFlip(false);
                            found = true;
                            break;
                        }
                        if (tile.equals(Tile.vFlip(tiles[j]))) {
                            tile.setId(i * 128 + j);
                            tile.sethFlip(false);
                            tile.setvFlip(true);
                            found = true;
                            break;
                        }
                        if (tile.equals(Tile.hFlip(Tile.vFlip(tiles[j])))) {
                            tile.setId(i * 128 + j);
                            tile.sethFlip(true);
                            tile.setvFlip(true);
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
            }
        }*/
    }
    
    public void generateOptimisedBlockset() {
        /*MapBlock[] blocks = map.getBlocks();
        MapBlock[] optimizedBlockset = null;
        List<MapBlock> blockList = new ArrayList();
        blockList.add(createVoidMapBlock());
        blockList.add(createClosedChestMapBlock());
        blockList.add(createOpenChestMapBlock());
        for(int i=0;i<blocks.length;i++){
            boolean found = false;
            for(int j=0;j<blockList.size();j++){
                if(blocks[i].equals(blockList.get(j))){
                    found = true;
                    break;
                }
            }
            if(!found){
                blocks[i].setIndex(blockList.size());
                blockList.add(blocks[i]);
            }
        }
        optimizedBlockset = new MapBlock[blockList.size()];
        blockList.toArray(optimizedBlockset);
        map.setOptimizedBlockset(optimizedBlockset);
        /* Re-assign layout block indexes */
        /*
        MapBlock[] layout = map.getLayout().getBlocks();
        MapBlock[] blockset = map.getOptimizedBlockset();
        for(int b=0;b<layout.length;b++){
            for(int i=0;i<blockset.length;i++){
                if(layout[b].equals(blockset[i])){
                    layout[b].setIndex(i);
                    break;
                }
            }
        }*/
    }
}
