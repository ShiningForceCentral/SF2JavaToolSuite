/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.AbstractTilesetRawImageProcessor;
import com.sfc.sf2.mapsprite.MapSpriteManager;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public class MapSpriteRawImageProcessor extends AbstractTilesetRawImageProcessor<Block[], MapSpritePackage> {

    @Override
    protected Block[] parseImageData(WritableRaster raster, IndexColorModel icm, MapSpritePackage pckg) throws RawImageException {
        checkImageDimensions(raster);
        Palette palette = pckg.palette();
        if (palette == null) {
            palette = new Palette(pckg.name(), Palette.fromICM(icm), true);
        }
        Tileset[] tilesets = parseTileset(raster, Block.PIXEL_WIDTH, Block.PIXEL_HEIGHT, palette);
        Block[] frames = new Block[tilesets.length];
        for (int i = 0; i < tilesets.length; i++) {
            int index = pckg.indices()[0]*6 + (pckg.indices()[1] == -1 ? 0 : pckg.indices()[1]*2) + (pckg.indices()[2] == -1 ? 0 : pckg.indices()[2]);
            frames[i] = new Block(index, tilesets[i]);
        }        
        return frames;
    }

    @Override
    protected BufferedImage packageImageData(Block[] item, MapSpritePackage pckg) throws RawImageException {
        MapSpriteManager.MapSpriteExportMode exportMode = pckg.exportMode();
        BufferedImage image = null;
        if (exportMode == exportMode.INDIVIDUAL_FILES) {
            image = setupImage(item[0]);
            writeTileset(image.getRaster(), item[0]);
        } else {
            image = setupImage(item, 2);
            writeTileset(image.getRaster(), item, 2);
        }
        return image;
    }
    
}
