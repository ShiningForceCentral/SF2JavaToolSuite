/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.icon.io;

import com.sfc.sf2.core.io.RawImageException;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.AbstractTilesetRawImageProcessor;
import com.sfc.sf2.icon.Icon;
import static com.sfc.sf2.icon.Icon.ICON_TILE_HEIGHT;
import static com.sfc.sf2.icon.Icon.ICON_TILE_WIDTH;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class IconRawImageProcessor extends AbstractTilesetRawImageProcessor<Icon[], IconPackage> {

    @Override
    protected Icon[] parseImageData(WritableRaster raster, IndexColorModel icm, IconPackage pckg) throws RawImageException {
        checkImageDimensions(raster);
        Palette palette = pckg.palette();
        if (palette == null) {
            palette = new Palette(Palette.fromICM(icm), true);
        }
        int tilesWidth = raster.getWidth() / (ICON_TILE_WIDTH*PIXEL_WIDTH);
        int tilesHeight = raster.getHeight()/ (ICON_TILE_HEIGHT*PIXEL_HEIGHT);
        if (tilesWidth < 1 || tilesHeight < 1) {
            throw new RawImageException("Warning : image dimensions are too small.");
        } else if (tilesWidth == 1 && tilesHeight == 1) {
            //Single icon
            Icon[] icons = new Icon[1];
            Tileset tileset = parseTileset(raster, palette);
            icons[0] = new Icon(pckg.index(), tileset);
            return icons;
        } else {
            //Multiple icons
            ArrayList<Icon> iconList = new ArrayList<>();
            for (int j = 0; j < tilesHeight; j++) {
                for (int i = 0; i < tilesWidth; i++) {
                    Tileset tileset = parseTileset(raster, i*ICON_TILE_WIDTH, j*ICON_TILE_HEIGHT, ICON_TILE_WIDTH, ICON_TILE_HEIGHT, palette);
                    if (!tileset.isTilesetEmpty()) {
                        iconList.add(new Icon(i+j*tilesWidth, tileset));
                    }
                }
            }
            Icon[] icons = new Icon[iconList.size()];
            icons = iconList.toArray(icons);
            return icons;
        }
    }

    @Override
    protected BufferedImage packageImageData(Icon item[], IconPackage pckg) throws RawImageException {
        if (item.length == 1) {
            //Single icon
            BufferedImage image = setupImage(item[0].getTileset());
            writeTileset(image.getRaster(), item[0].getTileset());
            return image;
        } else {
            //Mutiple icons
            int iconsPerRow = pckg.iconsPerRow();
            BufferedImage image = setupImage(item.length, iconsPerRow, ICON_TILE_WIDTH, ICON_TILE_HEIGHT, pckg.palette().getIcm());
            WritableRaster raster = image.getRaster();
            for (int i = 0; i < item.length; i++) {
                writeTileset(raster, item[i].getTileset(), (i%iconsPerRow)*ICON_TILE_WIDTH, (i/iconsPerRow)*ICON_TILE_HEIGHT, ICON_TILE_WIDTH);
            }
            writeTileset(raster, item[0].getTileset());
            return image;
        }
    }
    
}
