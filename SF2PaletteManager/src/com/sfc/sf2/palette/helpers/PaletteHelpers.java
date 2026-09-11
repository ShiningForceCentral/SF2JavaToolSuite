/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette.helpers;

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
     * Extracts colors from a palette into a smaller palette
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
    
    /**
     * Returns a ne palette with colors swapped
     * @param palette The base palette to extract from
     * @param from From index
     * @param to To index
     * @return A new palette of same length as extractIndices
     */
    public static Palette swapColors(Palette palette, int from, int to) {
        return new Palette(palette.getName(), swapColors(palette.getColors(), from, to), palette.isFirstColorTransparent(), false);
    }
    
    /**
     * Returns a ne palette with colors swapped
     * @param palette The base palette to extract from
     * @param from From index
     * @param to To index
     * @return A new palette of same length as extractIndices
     */
    public static CRAMColor[] swapColors(CRAMColor[] colors, int from, int to) {
        CRAMColor[] newColors = colors.clone();
        newColors[from] = colors[to];
        newColors[to] = colors[from];
        return newColors;
    }
}
