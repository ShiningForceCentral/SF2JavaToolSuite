/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlock;
import static com.sfc.sf2.map.block.MapBlock.TILES_COUNT;
import com.sfc.sf2.map.block.MapTile;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class MapBlockHelpers {
    public static final int TILESET_TILES = 128;
    public static final int MAP_TILESETS_TILES = TILESET_TILES*5;
    
    public static final Color PRIORITY_COLOR = new Color(255, 255, 0, 100);
    public static final Color PRIORITY_DARKEN_COLOR = new Color(0, 0, 0, 100);
    
    public static void drawTilePriorities(Graphics graphics, MapBlock[] blocks, Tileset[] tilesets, int blocksPerRow) {
        for (int b=0; b < blocks.length; b++) {
            drawTilePriorities(graphics, blocks[b], tilesets, (b%blocksPerRow)*Block.PIXEL_WIDTH, (b/blocksPerRow)*Block.PIXEL_HEIGHT);
        }
    }
    
    public static void drawTilePriorities(Graphics graphics, MapBlock mapBlock, Tileset[] tilesets, int x, int y) {
        MapTile[] tiles = mapBlock.getMapTiles();
        for (int t=0; t < TILES_COUNT; t++) {
            if (tiles[t].getTileFlags().isPriority()) {
                graphics.setColor(PRIORITY_COLOR);
            } else {
                graphics.setColor(PRIORITY_DARKEN_COLOR);
            }
            graphics.fillRect(x+(t%Block.TILE_WIDTH)*Tile.PIXEL_WIDTH, y+(t/Block.TILE_WIDTH)*Tile.PIXEL_HEIGHT, Tile.PIXEL_WIDTH, Tile.PIXEL_HEIGHT);
        }
    }
}
