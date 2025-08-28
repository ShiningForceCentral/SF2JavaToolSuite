/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractTilesetRawImageProcessor<TType extends Object, TPackage extends Object> extends AbstractRawImageProcessor<TType, TPackage> {
    
    /**
     * Checks image dimensions to ensure that the image is multiple of {@code Tile} pixel width/height.
     */
    protected void checkImageDimensions(WritableRaster raster) throws RawImageException {
        int imageWidth = raster.getWidth();
        int imageHeight = raster.getHeight();
        if (imageWidth%PIXEL_WIDTH != 0 || imageHeight%PIXEL_HEIGHT != 0) {
            throw new RawImageException("Warning : image dimensions are not a multiple of 8 (pixels per tile).");
        }
    }
    
    /**
     * Checks image dimensions to ensure that the image is multiple of {@code Tile} pixel width/height as well as a multiple for the {@link Tileset} width/height.
     * @param tileWidthOfTileset How many tiles wide is the tileset
     * @param tileHeightOfTileset How many tiles in height is the tileset
     */
    protected void checkImageDimensions(WritableRaster raster, int tileWidthOfTileset, int tileHeightOfTileset) throws RawImageException {
        checkImageDimensions(raster);
        if (raster.getWidth()%(tileWidthOfTileset*PIXEL_WIDTH) != 0) {
            throw new RawImageException("Warning : image dimensions are wrong size. Width is " + raster.getWidth() + " but expected a multiple of " + (tileWidthOfTileset*PIXEL_WIDTH));
        }
        if (raster.getHeight()%(tileHeightOfTileset*PIXEL_HEIGHT) != 0) {
            throw new RawImageException("Warning : image dimensions are wrong size. Height is " + raster.getHeight() + " but expected a multiple of " + (tileHeightOfTileset*PIXEL_HEIGHT));
        }
    }
    
    /**
     * Parses the image (raster) and outputs a {@link Tileset} from all pixels.
     */
    protected Tileset parseTileset(WritableRaster raster, Palette palette) {
        return parseTileset(raster, 0, 0, raster.getWidth()/PIXEL_WIDTH, raster.getHeight()/PIXEL_HEIGHT, palette);
    }
    
    /**
     * Parses the image (raster) and outputs multiple {@link Tileset}s of size <i>tileWidthOfTileset</i> x <i>tileHeightOfTileset</i>.
     * <br>Assumes that each tileset is the same dimensions.
     * @param tileWidthOfTileset How many tiles in width is each {@code Tileset}
     * @param tileHeightOfTileset How many tiles in height is each {@code Tileset}
     */
    protected Tileset[] parseTileset(WritableRaster raster, int tileWidthOfTileset, int tileHeightOfTileset, Palette palette) {
        int tilesPerRow = raster.getWidth()/PIXEL_WIDTH;
        int tilesPerColumn = raster.getHeight()/PIXEL_HEIGHT;
        int tilesetsPerRow = tilesPerRow/tileWidthOfTileset;
        int tilesetsPerColumn = tilesPerColumn/tileHeightOfTileset;
        
        int tilesetCount = (tilesetsPerRow*tilesetsPerColumn);
        Tileset[] tilesets = new Tileset[tilesetCount];
        for (int j = 0; j < tilesetsPerColumn; j++) {
            for (int i = 0; i < tilesetsPerRow; i++) {
                tilesets[i+j*tilesetsPerRow] = parseTileset(raster, i*tileWidthOfTileset, j*tileHeightOfTileset, tileWidthOfTileset, tileHeightOfTileset, palette);
            }
        }
        return tilesets;
    }
    
    /**
     * Parses a segment of the image (raster) and outputs a {@link Tileset}.
     * @param tileX How many tiles across is the top-left tile of the  {@code Tileset}
     * @param tileY How many tiles down is the top-left tile of the {@code Tileset}
     * @param tilesPerRow How many tiles in a row for the {@code Tileset} (not for the image)
     * @param tilesPerColumn How many tiles in a column for the {@code Tileset} (not for the image)
     */
    protected Tileset parseTileset(WritableRaster raster, int tileX, int tileY, int tilesPerRow, int tilesPerColumn, Palette palette) {
        Tile[] tiles = new Tile[tilesPerRow*tilesPerColumn];
        int tileId = 0;
        //Console.logger().finest("Building tileset from coordinates "+tileX+":"+tileY+":"+(tileX+tilesPerRow)+":"+(tileY+tilesPerColumn));
        for (int t = 0; t < tiles.length; t++) {
            int[] pixels = new int[PIXEL_WIDTH*PIXEL_WIDTH];
            int x = (tileX + t%tilesPerRow)*PIXEL_WIDTH;
            int y = (tileY + t/tilesPerRow)*PIXEL_HEIGHT;
            //Console.logger().finest("Building tile from coordinates "+x+":"+y);
            Tile tile = new Tile();
            tile.setId(tileId);
            tile.setPalette(palette);
            raster.getPixels(x, y, PIXEL_WIDTH, PIXEL_HEIGHT, pixels);
            tile.setPixels(pixels);
            //Console.logger().finest(tile.toString());
            tiles[tileId] = tile;   
            tileId++;
        }
        return new Tileset(null, tiles, tilesPerRow);
    }
    
    /**
     * Creates a new {@code BufferedImage} based on the {@code tilesPerRow} and {@link Palette} of the {@link Tileset}
     */
    protected BufferedImage setupImage(Tileset tileset) {
        return setupImage(tileset.getTiles().length, tileset.getTilesPerRow(), 1, 1, tileset.getPalette().getIcm());
    }
    
    /**
     * Creates a new {@code BufferedImage} based on the {@code tilesPerRow} and {@link Palette} of the {@link Tileset}
     * @param itemsCount How many {@code Tiles} in total
     * @param itemsPerRow How many {@code Tiles} per row for the image
     * @param itemTileWidth How many {@code Tiles} high is the {@link Tileset}
     */
    protected BufferedImage setupImage(int itemsCount, int itemsPerRow, int itemTileWidth, int itemTileHeight, IndexColorModel icm) {
        int imageWidth = itemsPerRow*itemTileWidth;
        int imageHeight = (itemsCount/itemsPerRow)*itemTileHeight;
        if (itemsCount % itemsPerRow != 0) {
            imageHeight += itemTileHeight;
        }
        return new BufferedImage(imageWidth*PIXEL_WIDTH, imageHeight*PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_BINARY, icm);
    }
    
    /**
     * Creates a new {@code BufferedImage} based on a {@link Tileset} array and the number of {@code tilesPerRow} for the image
     */
    protected BufferedImage setupImage(Tileset[] tilesets, int tilesetsPerRow) {
        Palette palette = null;
        int tilesetTilesWidth = 1;
        int tilesetTilesheight = 1;
        for (Tileset tileset : tilesets) {
            if (tileset != null) {
                palette = tileset.getPalette();
                tilesetTilesWidth = tileset.getTilesPerRow();
                tilesetTilesheight = (tileset.getTiles().length/tilesetTilesWidth);
                if (tileset.getTiles().length % tilesetTilesWidth != 0) {
                    tilesetTilesheight++;
                }
                break;
            }
        }
        
        int width = tilesetsPerRow*tilesetTilesWidth;
        int height = (tilesets.length/tilesetsPerRow);
        if (tilesets.length % tilesetsPerRow != 0) {
            height++;
        }
        height *= tilesetTilesheight;
        return new BufferedImage(width*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_BINARY, palette.getIcm());
    }
    
    /**
     * Writes the {@link Tileset} to the image from the top-left corner
     */
    protected void writeTileset(WritableRaster raster, Tileset tileset) {
        writeTileset(raster, tileset, 0, tileset.getTiles().length, 0, 0, tileset.getTilesPerRow());
    }
    
    /**
     * Writes the {@link Tileset} array to the image in order; left-to-right, top-to-bottom
     * @param tilesetsPerRow How many {@code Tilesets} in a row in the image
     */
    protected void writeTileset(WritableRaster raster, Tileset[] tilesets, int tilesetsPerRow) {
        int tilesetTilesWidth = 1;
        int tilesetTilesheight = 1;
        for (Tileset tileset : tilesets) {
            if (tileset != null) {
                tilesetTilesWidth = tileset.getTilesPerRow();
                tilesetTilesheight = (tileset.getTiles().length/tilesetTilesWidth);
                if (tileset.getTiles().length % tilesetTilesWidth != 0) {
                    tilesetTilesheight++;
                }
                break;
            }
        }
        
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesets[i] != null) {
                int x = (i % tilesetsPerRow)*tilesetTilesWidth;
                int y = (i/tilesetsPerRow)*tilesetTilesheight;
                writeTileset(raster, tilesets[i], 0, tilesets[i].getTiles().length, x, y, tilesets[i].getTilesPerRow());
            }
        }
    }
    
    /**
     * Writes the {@link Tileset} array to part of the image.
     * <br>Good for drawing classes that contain {@code Tilesets} (i.e. where an array cannot simply be passed).
     * @param tileX How many {@code Tiles} from the left to start drawing the {@code Tileset}
     * @param tileY How many {@code Tiles} from the top to start drawing the {@code Tileset}
     * @param tilesPerRow How many {@code Tiles} in a row in the image
     */
    protected void writeTileset(WritableRaster raster, Tileset tileset, int tileX, int tileY, int tilesPerRow) {
        writeTileset(raster, tileset, 0, tileset.getTiles().length, tileX, tileY, tilesPerRow);
    }
    
    /**
     * Writes the {@link Tileset} array to part of the image.
     * <br>Good for drawing part of a {@code Tileset}.
     * @param tilesetIndexStart The index of the first {@code Tile} in the {@code Tileset} to draw
     * @param tilesetIndexEnd The index of the first {@code Tile} in the {@code Tileset} to draw
     * @param tileX How many {@code Tiles} from the left to start drawing the {@code Tileset}
     * @param tileY How many {@code Tiles} from the top to start drawing the {@code Tileset}
     * @param tilesPerRow How many {@code Tiles} in a row in the image
     */
    protected void writeTileset(WritableRaster raster, Tileset tileset, int tilesetIndexStart, int tilesetIndexEnd, int tileX, int tileY, int tilesPerRow) {
        Tile[] tiles = tileset.getTiles();
        for (int t = tilesetIndexStart; t < tilesetIndexEnd; t++) {
            if (tiles[t] != null) {
                int x = (tileX + t%tilesPerRow)*PIXEL_WIDTH;
                int y = (tileY + t/tilesPerRow)*PIXEL_HEIGHT;
                raster.setPixels(x, y, PIXEL_WIDTH, PIXEL_HEIGHT, tiles[t].getPixels());
            }
        }
    }
}
