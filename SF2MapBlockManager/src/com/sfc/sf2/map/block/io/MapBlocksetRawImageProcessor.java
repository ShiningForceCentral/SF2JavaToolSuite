/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
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
public class MapBlocksetRawImageProcessor extends AbstractRawImageProcessor<MapBlockset, MapBlockPackage> {
        
    /**
     * Checks image dimensions to ensure that the image is multiple of {@code Block} pixel width/height.
     */
    protected void checkImageDimensions(WritableRaster raster) throws RawImageException {
        int imageWidth = raster.getWidth();
        int imageHeight = raster.getHeight();
        if (imageWidth%PIXEL_WIDTH != 0 || imageHeight%PIXEL_HEIGHT != 0) {
            throw new RawImageException("Warning : image dimensions are not a multiple of " + PIXEL_WIDTH + " (pixels per tile).");
        }
    }
    
    /**
     * Checks image dimensions to ensure that the image is multiple of {@code Block} pixel width/height as well as a multiple for the {@link Blockset} width/height.
     * @param blockWidthOfBlockset How many block wide is the blockset
     * @param blockHeightOfBlockset How many block in height is the blockset
     */
    /*protected void checkImageDimensions(WritableRaster raster, int blockWidthOfBlockset, int blockHeightOfBlockset) throws RawImageException {
        checkImageDimensions(raster);
        if (raster.getWidth()%(blockWidthOfBlockset*PIXEL_WIDTH) != 0) {
            throw new RawImageException("Warning : image dimensions are wrong size. Width is " + raster.getWidth() + " but expected a multiple of " + (blockWidthOfBlockset*PIXEL_WIDTH));
        }
        if (raster.getHeight()%(blockHeightOfBlockset*PIXEL_HEIGHT) != 0) {
            throw new RawImageException("Warning : image dimensions are wrong size. Height is " + raster.getHeight() + " but expected a multiple of " + (blockHeightOfBlockset*PIXEL_HEIGHT));
        }
    }*/
    
    @Override
    protected MapBlockset parseImageData(WritableRaster raster, IndexColorModel icm, MapBlockPackage pckg) throws RawImageException {
        throw new RawImageException("Importing blockset image not implemented");
        /*checkImageDimensions(raster);
        Palette palette = null;
        if (pckg != null && pckg.palette() != null) {
            palette = pckg.palette();
        } else {
            palette = new Palette(Palette.fromICM(icm), true);
        }
        return parseBlockset(raster, palette, pckg.tilesets());*/
    }
    
    /**
     * Parses the image (raster) and outputs a {@link Blockset} from all pixels.
     */
    protected MapBlockset parseBlockset(WritableRaster raster, Palette palette, Tileset[] tilesets) {
        return parseBlockset(raster, 0, 0, raster.getWidth()/PIXEL_WIDTH, raster.getHeight()/PIXEL_HEIGHT, palette, tilesets);
    }
    
    /**
     * Parses a segment of the image (raster) and outputs a {@link Blockset}.
     * @param blockX How many tiles across is the top-left tile of the  {@code Blockset}
     * @param blockY How many tiles down is the top-left tile of the {@code Blockset}
     * @param blocksPerRow How many tiles in a row for the {@code Blockset} (not for the image)
     * @param blocksPerColumn How many tiles in a column for the {@code Blockset} (not for the image)
     */
    protected MapBlockset parseBlockset(WritableRaster raster, int blockX, int blockY, int blocksPerRow, int blocksPerColumn, Palette palette, Tileset[] tilesets) {
        /*MapBlock[] blocks = new MapBlock[blocksPerRow*blocksPerColumn];
        int blockId = 0;
        int tileId = 0;
        //Console.logger().finest("Building blockset from coordinates "+blockX+":"+blockY+":"+(blockX+blocksPerRow)+":"+(blockY+blocksPerColumn));
        for (int b = 0; b < blocks.length; b++) {
            int bX = (blockX + b%blocksPerRow)*PIXEL_WIDTH;
            int bY = (blockY + b/blocksPerRow)*PIXEL_HEIGHT;
            MapTile[] tiles = new MapTile[Block.TILES_COUNT];
            for (int t = 0; t < tiles.length; t++) {
                int[] pixels = new int[Tile.PIXEL_WIDTH*Tile.PIXEL_HEIGHT];
                int tX = (t%Block.TILE_WIDTH)*Tile.PIXEL_WIDTH;
                int tY = (t/Block.TILE_WIDTH)*Tile.PIXEL_HEIGHT;
                //Console.logger().finest("Building tile from coordinates "+tX+":"+tY);
                raster.getPixels(bX+tX, bY+tY, Tile.PIXEL_WIDTH, Tile.PIXEL_HEIGHT, pixels);
                MapTile tile = new MapTile(tileId, pixels, palette, false, false, false); //NOTE System cannot know if tile is flipped or now
                Console.logger().finest(tile.toString());
                //tiles[tileId] = tile;   
                tileId++;
            }
            blocks[blockId] = new MapBlock(blockId, 0, tiles);
            blockId++;
        }
        return new MapBlockset(blocks, blocksPerRow);*/
        return null;
    }

    @Override
    protected BufferedImage packageImageData(MapBlockset item, MapBlockPackage pckg) throws RawImageException {
        BufferedImage image = setupImage(item, item.getBlocksPerRow(), pckg.palette());
        WritableRaster raster = image.getRaster();
        writeTileset(raster, item, pckg.tilesets());
        return image;
    }
    
    /**
     * Creates a new {@code BufferedImage} based on a {@link Tileset} array and the number of {@code tilesPerRow} for the image
     */
    protected BufferedImage setupImage(MapBlockset blockset, int blocksPerRow, Palette palette) {
        
        int width = blocksPerRow*PIXEL_WIDTH;
        int height = (blockset.getBlocks().length/blocksPerRow);
        if (blockset.getBlocks().length % blocksPerRow != 0) {
            height++;
        }
        height *= PIXEL_HEIGHT;
        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY, palette.getIcm());
    }
    
    /**
     * Writes the {@link Blockset} to the image from the top-left corner
     */
    protected void writeTileset(WritableRaster raster, MapBlockset blockset, Tileset[] tilesets) {
        writeTileset(raster, blockset, tilesets, 0, blockset.getBlocks().length, 0, 0, blockset.getBlocksPerRow());
    }
    
    /**
     * Writes the {@link Blockset} array to part of the image.
     * <br>Good for drawing part of a {@code Blockset}.
     * @param blocksetIndexStart The index of the first {@code Tile} in the {@code Tileset} to draw
     * @param blocksetIndexEnd The index of the first {@code Tile} in the {@code Tileset} to draw
     * @param blockX How many {@code Tiles} from the left to start drawing the {@code Tileset}
     * @param blockY How many {@code Tiles} from the top to start drawing the {@code Tileset}
     * @param blocksPerRow How many {@code Tiles} in a row in the image
     */
    protected void writeTileset(WritableRaster raster, MapBlockset blockset, Tileset[] tilesets, int blocksetIndexStart, int blocksetIndexEnd, int blockX, int blockY, int blocksPerRow) {
        MapBlock[] blocks = blockset.getBlocks();
        for (int b = blocksetIndexStart; b < blocksetIndexEnd; b++) {
            if (blocks[b] != null) {
                int bX = (blockX + b%blocksPerRow)*PIXEL_WIDTH;
                int bY = (blockY + b/blocksPerRow)*PIXEL_HEIGHT;
                MapTile[] tiles = blocks[b].getMapTiles();
                for (int t = 0; t < tiles.length; t++) {
                    int tX = (t%Block.TILE_WIDTH)*Tile.PIXEL_WIDTH;
                    int tY = (t/Block.TILE_WIDTH)*Tile.PIXEL_HEIGHT;
                    int[] pixels = tiles[t].getTile(tilesets).getRenderPixels(tiles[t].getTileFlags());
                    raster.setPixels(bX+tX, bY+tY, Tile.PIXEL_WIDTH, Tile.PIXEL_HEIGHT, pixels);
                }
            }
        }
    }
}
