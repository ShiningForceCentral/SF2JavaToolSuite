/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.palette.CRAMColor;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author TiMMy
 */
public class PaletteHelpers {
    
    /**
     * Inserts colors from one (smaller) palette into a base palette
     * @param base The base palette. Should be 16 colors
     * @param insert The palette to insert to. Should be less than 16 colors
     * @param insertToIndices Which slots in the base palette to copy to
     * @param insertFromIndices Which slots from the insert palette to copy from
     * @return A new combined palette
     */
    public static Palette combinePalettes(Palette base, Palette insert, int[] insertToIndices, int[] insertFromIndices) {
        CRAMColor[] newPalette = new CRAMColor[base.getColorsCount()];
        System.arraycopy(base.getColors(), 0, newPalette, 0, newPalette.length);
        for (int i = 0; i < insertToIndices.length; i++) {
            newPalette[insertToIndices[i]] = insert.getColors()[insertFromIndices[i]];
        }
        return new Palette(newPalette, true);
    }
    
    /**
     *
     * @param palette The base palette to extract from
     * @param extractIndices The indices to extract colors from the palette
     * @return A new palette of same length as extractIndices
     */
    public static Palette extractColors(Palette palette, int[] extractIndices, boolean firstColorTransparent) {
        CRAMColor[] newPalette = new CRAMColor[extractIndices.length];
        for (int i = 0; i < extractIndices.length; i++) {
            newPalette[i] = palette.getColors()[extractIndices[i]];
        }
        return new Palette(newPalette, firstColorTransparent);
    }
}
