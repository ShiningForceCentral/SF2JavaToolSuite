/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.layout.io;

import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.PIXEL_COUNT;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.AbstractTilesetRawImageProcessor;
import com.sfc.sf2.layout.LayoutTile;
import com.sfc.sf2.layout.SpecialScreenLayout;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.io.PalettePackage;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public class LayoutTilesetRawImageProcessor extends AbstractRawImageProcessor<SpecialScreenLayout, PalettePackage> {
    
    @Override
    protected SpecialScreenLayout parseImageData(WritableRaster raster, IndexColorModel icm, PalettePackage pckg) throws RawImageException {
        //checkImageDimensions(raster);
        Palette palette = pckg.preLoadedPalette();
        if (palette == null) {
            palette = new Palette(pckg.name(), Palette.fromICM(icm), pckg.firstColorTransparent());
        }
        //Console.logger().finest("Tiles per row : " + raster.getWidth()/PIXEL_WIDTH);
        //Tileset tileset = parseTileset(raster, palette);
        //tileset.setName(pckg.name());
        return null;//tileset;
    }

    @Override
    protected BufferedImage packageImageData(SpecialScreenLayout item, PalettePackage pckg) throws RawImageException {
        BufferedImage image = setupImage(item.getLayout().length, item.getTilesPerRow(), 1, 1, item.getTilesets()[1].getPalette().getIcm());
        writeTileset(image.getRaster(), item);
        return image;
    }
    
    private BufferedImage setupImage(int itemsCount, int itemsPerRow, int itemTileWidth, int itemTileHeight, IndexColorModel icm) {
        int imageWidth = itemsPerRow*itemTileWidth;
        int imageHeight = (itemsCount/itemsPerRow)*itemTileHeight;
        if (itemsCount % itemsPerRow != 0) {
            imageHeight += itemTileHeight;
        }
        return new BufferedImage(imageWidth*PIXEL_WIDTH, imageHeight*PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED, icm);
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
