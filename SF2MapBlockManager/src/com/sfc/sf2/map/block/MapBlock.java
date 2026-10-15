/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.graphics.Tileset;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class MapBlock {
    public static final int TILES_COUNT = Block.TILES_COUNT;
       
    private int index;
    private MapTile[] mapTiles;
    
    private BufferedImage indexedColorImage = null;
    
    public MapBlock(int index) {
        this.index = index;
        
        MapTile[] mapTiles = new MapTile[TILES_COUNT];
        for (int i = 0; i < TILES_COUNT; i++) {
            mapTiles[i] = MapTile.EmptyMapTile();
        }
        setMapTiles(mapTiles);
    }
    
    public MapBlock(int index, MapTile[] mapTiles) {
        this.index = index;
        setMapTiles(mapTiles);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MapTile[] getMapTiles() {
        return mapTiles;
    }

    public void setMapTiles(MapTile[] mapTiles) {
        if (mapTiles == null || mapTiles.length != TILES_COUNT) {
            throw new IndexOutOfBoundsException("MapBlock mapTiles must be exactly" + TILES_COUNT + " in size.");
        }
        this.mapTiles = mapTiles;
    }
    
    public BufferedImage getIndexedColorImage(Tileset[] tilesets) {
        if (indexedColorImage == null) {
            indexedColorImage = new BufferedImage(Block.TILE_WIDTH*PIXEL_WIDTH, Block.TILE_HEIGHT*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = indexedColorImage.getGraphics();
            for (int i=0; i < TILES_COUNT; i++) {
                graphics.drawImage(mapTiles[i].getIndexedColorImage(tilesets), (i%Block.TILE_WIDTH)*PIXEL_WIDTH, (i/Block.TILE_WIDTH)*PIXEL_HEIGHT, null);
            }
            graphics.dispose();
        }
        return indexedColorImage;
    }
    
    public void clearIndexedColorImage() {
        if (this.indexedColorImage != null) {
            indexedColorImage.flush();
            indexedColorImage = null;
        }
    }
    
    public boolean isEmpty() {
        for (int i = 0; i < mapTiles.length; i++) {
            if (mapTiles[i].getTilesetIndex()!= -1) return false;
            if (mapTiles[i].getTileIndex() != -1) return false;
        }
        return true;
    }
    
    public boolean isAllPriority() {
        for (int i = 0; i < mapTiles.length; i++) {
            if (!mapTiles[i].getTileFlags().isPriority()) return false;
        }
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!equalsIgnoreTiles(obj)) return false;
        if (obj == null) return false;
        MapBlock block = (MapBlock)obj;
        return Arrays.equals(this.mapTiles, block.mapTiles);
    }
    
    public boolean equalsIgnoreTiles(Object obj) {
        if (!(obj instanceof MapBlock)) return super.equals(obj);
        MapBlock block = (MapBlock)obj;
        return this.index == block.index;
    }
    
    @Override 
    public MapBlock clone() {
        MapBlock clone = new MapBlock(index, mapTiles.clone());
        return clone;
    }
    
    public static MapBlock EmptyMapBlock(int index) {
        return new MapBlock(index);
    }
    
    public MapBlock createChestMapBlock(boolean isOpen) {
        byte tileset = 4;
        int openOffset = isOpen ? 2 : 0;
        MapTile[] tiles = new MapTile[9];
        tiles[0] = new MapTile(tileset, (byte)(46+openOffset), new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[1] = new MapTile(tileset, (byte)(47+openOffset), new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[2] = new MapTile(tileset, (byte)(46+openOffset), new TileFlags(TileFlags.TILE_FLAG_HFLIP));
        tiles[3] = new MapTile(tileset, (byte)(62+openOffset), new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[4] = new MapTile(tileset, (byte)(63+openOffset), new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[5] = new MapTile(tileset, (byte)(62+openOffset), new TileFlags(TileFlags.TILE_FLAG_HFLIP));
        tiles[6] = new MapTile(tileset, (byte)78, new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[7] = new MapTile(tileset, (byte)79, new TileFlags(TileFlags.TILE_FLAG_NONE));
        tiles[8] = new MapTile(tileset, (byte)78, new TileFlags(TileFlags.TILE_FLAG_HFLIP));
        MapBlock block = new MapBlock(1, tiles);
        return block;
    }
}
