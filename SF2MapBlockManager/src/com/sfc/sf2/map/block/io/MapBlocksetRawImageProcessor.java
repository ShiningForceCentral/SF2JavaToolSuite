/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import static com.sfc.sf2.graphics.Block.TILE_HEIGHT;
import static com.sfc.sf2.graphics.Block.TILE_WIDTH;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.AbstractTilesetRawImageProcessor;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.MapTile;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public class MapBlocksetRawImageProcessor extends AbstractTilesetRawImageProcessor<MapBlockset, MapBlockPackage> {

    private Tileset tileset;
    public Tileset getTileset() { return tileset; }
     
    @Override
    protected MapBlockset parseImageData(WritableRaster raster, IndexColorModel icm, MapBlockPackage pckg) throws RawImageException {
        checkImageDimensions(raster);
        Palette palette = null;
        if (pckg != null && pckg.palette() != null) {
            palette = pckg.palette();
        } else {
            palette = new Palette(Palette.fromICM(icm), true);
        }
        
        tileset = parseTileset(raster, palette);
        int yTileStep = raster.getWidth()/Tile.PIXEL_WIDTH;
        int yBlockStep = yTileStep/3;
        MapBlock[] blocks = new MapBlock[tileset.getTiles().length / Block.TILES_COUNT];
        for (int b = 0; b < blocks.length; b++) {
            MapTile[] mapTiles = new MapTile[MapBlock.TILES_COUNT];
            for (int y = 0; y < TILE_HEIGHT; y++) {
                for (int x = 0; x < TILE_WIDTH; x++) {
                    int tileIndex = (b%yBlockStep)*TILE_WIDTH + b/yBlockStep*TILE_WIDTH*yTileStep;
                    tileIndex += x+y*yTileStep;
                    mapTiles[x+y*TILE_WIDTH] = new MapTile((byte)0, tileIndex, new TileFlags(TileFlags.TILE_FLAG_NONE));
                }
            }
            blocks[b] = new MapBlock(b, mapTiles);
        }
        return new MapBlockset(pckg.name(), blocks, 64);
    }

    @Override
    protected BufferedImage packageImageData(MapBlockset item, MapBlockPackage pckg) throws RawImageException {
        MapBlock[] blocks = item.getBlocks();
        int blocksPerRow = item.getBlocksPerRow();
        BufferedImage image = setupImage(blocks.length, blocksPerRow, 3, 3, pckg.palette().getIcm());
        WritableRaster raster = image.getRaster();
        
        for (int b = 0; b < blocks.length; b++) {
            if (blocks[b] != null) {
                int bX = (b%blocksPerRow)*PIXEL_WIDTH;
                int bY = (b/blocksPerRow)*PIXEL_HEIGHT;
                MapTile[] tiles = blocks[b].getMapTiles();
                for (int t = 0; t < tiles.length; t++) {
                    int tX = (t%Block.TILE_WIDTH)*Tile.PIXEL_WIDTH;
                    int tY = (t/Block.TILE_WIDTH)*Tile.PIXEL_HEIGHT;
                    Tile tile = tiles[t].getTile(pckg.tilesets());
                    if (tile != null) {
                        int[] pixels = tile.getRenderPixels(tiles[t].getTileFlags());
                        raster.setPixels(bX+tX, bY+tY, Tile.PIXEL_WIDTH, Tile.PIXEL_HEIGHT, pixels);
                    }
                }
            }
        }
        return image;
    }
}
