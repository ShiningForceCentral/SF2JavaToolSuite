/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator.gui;

import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import static com.sfc.sf2.graphics.Block.TILE_HEIGHT;
import static com.sfc.sf2.graphics.Block.TILE_WIDTH;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.MapBlockHelpers;
import com.sfc.sf2.map.block.MapTile;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import com.sfc.sf2.map.layout.gui.MapLayoutPanel;
import static com.sfc.sf2.mapcreator.OptimisedMap.ORPHAN_TILESET_INDEX;
import com.sfc.sf2.mapcreator.ProtoMap;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class MapCreatorLayoutPanel extends MapLayoutPanel {
    private static final Color NEW_COLOR = new Color(0f, 1f, 1f, 0.5f);
    private static final Color ORPHAN_COLOR = new Color(1f, 0f, 1f, 0.5f);
    
    private ProtoMap protoMap;
    private Tileset addedTiles;
    
    private boolean showAddedTiles;
    private boolean showOrphanTiles;

    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        
        if ((showAddedTiles || showOrphanTiles) && !getShowPriority()) {
            graphics.setColor(MapBlockHelpers.PRIORITY_DARKEN_COLOR);
            graphics.fillRect(0, 0, BLOCK_WIDTH*PIXEL_WIDTH, BLOCK_HEIGHT*PIXEL_HEIGHT);
        }
        
        if (showAddedTiles && addedTiles != null) {
            graphics.setColor(NEW_COLOR);
            drawAddedTiles(graphics, addedTiles.getTiles());
        }
        if (showOrphanTiles) {
            graphics.setColor(ORPHAN_COLOR);
            drawOrphanTiles(graphics);
        }
    }
    
    private void drawAddedTiles(Graphics graphics, Tile[] tiles) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                MapTile[] mapTiles = blocks[x+y*BLOCK_WIDTH].getMapBlock().getMapTiles();
                for (int ty = 0; ty < TILE_HEIGHT; ty++) {
                    for (int tx = 0; tx < TILE_WIDTH; tx++) {
                        MapTile tile = mapTiles[tx+ty*TILE_WIDTH];
                        for (int i = 0; i < tiles.length; i++) {
                            if (tiles[i].getId() == tile.getTileIndex()) {
                                graphics.fillRect(x*PIXEL_WIDTH+tx*Tile.PIXEL_WIDTH, y*PIXEL_HEIGHT+ty*Tile.PIXEL_HEIGHT, Tile.PIXEL_WIDTH, Tile.PIXEL_HEIGHT);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void drawOrphanTiles(Graphics graphics) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                MapTile[] mapTiles = blocks[x+y*BLOCK_WIDTH].getMapBlock().getMapTiles();
                for (int ty = 0; ty < TILE_HEIGHT; ty++) {
                    for (int tx = 0; tx < TILE_WIDTH; tx++) {
                        MapTile tile = mapTiles[tx+ty*TILE_WIDTH];
                        if (tile.getTilesetIndex() == ORPHAN_TILESET_INDEX) {
                            graphics.fillRect(x*PIXEL_WIDTH+tx*Tile.PIXEL_WIDTH, y*PIXEL_HEIGHT+ty*Tile.PIXEL_HEIGHT, Tile.PIXEL_WIDTH, Tile.PIXEL_HEIGHT);
                            break;
                        }
                    }
                }
            }
        }
    }

    public ProtoMap getProtoMap() {
        return protoMap;
    }

    public void setProtoMap(ProtoMap protoMap) {
        this.protoMap = protoMap;
        if (protoMap == null) {
            setMapLayout(null);
        } else {
            setMapLayout(protoMap.getOptimisedLayout());
        }
        this.redraw();
    }

    public void setAddedTiles(Tileset addedTiles) {
        this.addedTiles = addedTiles;
    }

    public void setShowAddedTiles(boolean drawAddedTiles) {
        this.showAddedTiles = drawAddedTiles;
        redraw();
    }

    public void setShowOrphanTiles(boolean drawOrphanTiles) {
        this.showOrphanTiles = drawOrphanTiles;
        redraw();
    }
}
