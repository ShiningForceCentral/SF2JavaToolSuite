/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette.helpers;

import com.sfc.sf2.palette.IPaletteGraphic;

/**
 *
 * @author TiMMy
 */
public class PaletteGraphicsHelpers {
    /**
     *
     * Swap the color index for colors matching from and to
     */
    public static void swapColorIndices(IPaletteGraphic graphics, byte from, byte to) {
        byte[] pixels = graphics.getPixels();
        pixels = swapColorIndices(pixels, from, to);
        graphics.setPixels(pixels);
    }
    
    /**
     *
     * Swap the color index for colors matching from and to
     */
    public static byte[] swapColorIndices(byte[] pixels, byte from, byte to) {
        if (pixels == null || pixels.length == 0) return null;
        
        byte[] newData = pixels.clone();
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == from) {
                newData[i] = to;
            } else if (pixels[i] == to) {
                newData[i] = from;
            } else {
                newData[i] = pixels[i];
            }
        }
        return newData;
    }
}
