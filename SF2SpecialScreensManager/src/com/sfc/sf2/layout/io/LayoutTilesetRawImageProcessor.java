/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.layout.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_COUNT;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.AbstractTilesetRawImageProcessor;
import com.sfc.sf2.helpers.TileHelpers;
import com.sfc.sf2.layout.LayoutTile;
import com.sfc.sf2.layout.SpecialScreenLayout;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public class LayoutTilesetRawImageProcessor extends AbstractTilesetRawImageProcessor<SpecialScreenLayout, SpecialScreenLayoutPackage> {
        
    private static HashMap<String, TileMatchData> tilesHashSet;
    
    @Override
    protected SpecialScreenLayout parseImageData(WritableRaster raster, IndexColorModel icm, SpecialScreenLayoutPackage pckg) throws RawImageException {
        checkImageDimensions(raster);
        Palette palette = pckg.palettes()[0];
        if (palette == null) {
            palette = new Palette(pckg.name(), Palette.fromICM(icm), pckg.palettes()[0].isFirstColorTransparent());
        }
        if (palette.getColorsCount() % 16 != 0) {
            Console.logger().warning("Layout tilesets should be 16, 32, 48, or 64 colors.");
        }
        Console.logger().finest("Tiles per row : " + raster.getWidth()/PIXEL_WIDTH);
        return parseImage(raster, pckg);
    }
    
    private SpecialScreenLayout parseImage(WritableRaster raster, SpecialScreenLayoutPackage pckg) {
        tilesHashSet = new HashMap<String, TileMatchData>();
        List<Tile> baseTiles = Arrays.asList(pckg.tilesets()[0].getTiles());
        ArrayList<Tile> newTiles = new ArrayList<>();
        int tilesPerRow = raster.getWidth()/PIXEL_WIDTH;
        int tilesPerColumn = raster.getHeight()/PIXEL_HEIGHT;
        LayoutTile[] layoutTiles = new LayoutTile[tilesPerRow*tilesPerColumn];
        //Console.logger().finest("Building tileset from coordinates "+tileX+":"+tileY+":"+(tileX+tilesPerRow)+":"+(tileY+tilesPerColumn));
        for (int t = 0; t < layoutTiles.length; t++) {
            int[] rasterPixels = new int[PIXEL_COUNT];
            int x = (t%tilesPerRow)*PIXEL_WIDTH;
            int y = (t/tilesPerRow)*PIXEL_HEIGHT;
            //Console.logger().finest("Building tile from coordinates "+x+":"+y);
            raster.getPixels(x, y, PIXEL_WIDTH, PIXEL_HEIGHT, rasterPixels);
            byte[] pixels = new byte[PIXEL_COUNT];
            for (int i = 0; i < PIXEL_COUNT; i++) {
                pixels[i] = (byte)rasterPixels[i];
            }
            
            String hash = getHash(pixels);
            Tile tile = null;
            if (tilesHashSet.containsKey(hash)) {
                TileMatchData data = tilesHashSet.get(hash);
                layoutTiles[t] = new LayoutTile(data.index, 0, data.flags.clone());
            }
            if (tile == null) {
                TileMatchData match = findMatchingTile(pixels, baseTiles, 0); //Check base tiles
                if (!match.matchFound()) {
                    match = findMatchingTile(pixels, newTiles, baseTiles.size());    //Check new tiles
                }
                if (!match.matchFound()) {
                    //This does not belong to either tileset
                    newTiles.add(new Tile(baseTiles.size()+newTiles.size(), pixels, null));
                }
                tilesHashSet.put(hash, match);
                layoutTiles[t] = new LayoutTile(match.index, 0, match.flags);
            }
        }
        tilesHashSet.clear();
        tilesHashSet = null;
        
        return new Tileset(null, tiles, tilesPerRow);
    }
    
    private String getHash(byte[] pixels) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pixels.length; i++) {
            sb.append(String.format("%02x", pixels[i]));
        }
        return sb.toString();
    }
    
    private TileMatchData findMatchingTile(byte[] pixels, List<Tile> compare, int indexOffset) {
        byte[] flipH = TileHelpers.flipTile(pixels, TileFlags.TILE_FLAG_HFLIP);
        byte[] flipV = TileHelpers.flipTile(pixels, TileFlags.TILE_FLAG_VFLIP);
        byte[] flipB = TileHelpers.flipTile(pixels, TileFlags.TILE_FLAG_BOTHFLIP);
        
        for(int i = 0; i<compare.size(); i++) {
            byte comp = comparePixelsAnyOrientation(compare.get(i).getPixels(), pixels, flipH, flipV, flipB);
            if (comp != -1) {
                return new TileMatchData(i+indexOffset, new TileFlags(comp));
            }
        }
        return TileMatchData.NoMatch();
    }
    
    private static byte comparePixelsAnyOrientation(byte[] compare, byte[] noFlip, byte[] flipH, byte[] flipV, byte[] flipB) {
        if (Arrays.equals(noFlip, compare)) return TileFlags.TILE_FLAG_NONE;
        if (Arrays.equals(flipH, compare)) return TileFlags.TILE_FLAG_HFLIP;
        if (Arrays.equals(flipV, compare)) return TileFlags.TILE_FLAG_VFLIP;
        if (Arrays.equals(flipB, compare)) return TileFlags.TILE_FLAG_BOTHFLIP;
        return -1;
    }

    @Override
    protected BufferedImage packageImageData(SpecialScreenLayout item, SpecialScreenLayoutPackage pckg) throws RawImageException {
        BufferedImage image = setupImage(item.getLayout().length, item.getTilesPerRow(), 1, 1, item.getTilesets()[1].getPalette().getIcm(), BufferedImage.TYPE_BYTE_INDEXED);
        writeTileset(image.getRaster(), item);
        return image;
    }
    
    private void writeTileset(WritableRaster raster, SpecialScreenLayout layout) {
        Tileset[] tilesets = layout.getTilesets();
        LayoutTile[] tiles = layout.getLayout();
        int tilesPerRow = layout.getTilesPerRow();
        for (int t = 0; t < tiles.length; t++) {
            Tile tile = tiles[t].getTile(tilesets);
            if (tile != null) {
                int x = (t%tilesPerRow)*PIXEL_WIDTH;
                int y = (t/tilesPerRow)*PIXEL_HEIGHT;
                int[] pixels = tile.getRenderPixels();
                raster.setPixels(x, y, PIXEL_WIDTH, PIXEL_HEIGHT, pixels);
            }
        }
    }
}
