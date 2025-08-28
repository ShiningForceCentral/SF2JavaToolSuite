/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.io;

import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.vwfont.FontSymbol;
import static com.sfc.sf2.vwfont.FontSymbol.PIXEL_HEIGHT;
import static com.sfc.sf2.vwfont.FontSymbol.PIXEL_WIDTH;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author TiMMy
 */
public class VWFontRawImageProcessor extends AbstractRawImageProcessor<FontSymbol, FontSymbolPackage> {
    
    @Override
    protected FontSymbol parseImageData(WritableRaster raster, IndexColorModel icm, FontSymbolPackage pckg) throws RawImageException {
        int symbolWidth = 0;
        int[] symbolPixels = new int[PIXEL_WIDTH*PIXEL_HEIGHT];
        int[] pixels = new int[PIXEL_WIDTH*PIXEL_HEIGHT];
        raster.getPixels(0, 0, PIXEL_WIDTH, PIXEL_HEIGHT, pixels);
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == 2) {   //Width marker
                symbolWidth = i%PIXEL_WIDTH;
                symbolPixels[i] = 0;
            } else {
                symbolPixels[i] = pixels[i];
            }
        }
        return new FontSymbol(pckg.id(), symbolPixels, symbolWidth);
    }

    @Override
    protected BufferedImage packageImageData(FontSymbol item, FontSymbolPackage pckg) throws RawImageException {
        BufferedImage image = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_BINARY, item.getPalette().getIcm());
        WritableRaster raster = image.getRaster();
        int[] data = item.getPixels();
        data[item.getWidth()] = 2;
        raster.setPixels(0, 0, PIXEL_WIDTH, PIXEL_HEIGHT, data);
        return image;
    }
}
