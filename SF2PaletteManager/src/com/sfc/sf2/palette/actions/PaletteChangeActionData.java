/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.palette.Palette;
import java.util.Arrays;

/**
 *
 * @author TiMMy
 */
public class PaletteChangeActionData implements IActionData<PaletteChangeActionData> {
    
    private final Palette palette;
    private final byte[] pixelData;
    
    public PaletteChangeActionData(Palette palette, byte[] pixelData) {
        this.palette = palette;
        this.pixelData = pixelData;
    }

    public Palette palette() {
        return palette;
    }

    public byte[] pixelData() {
        return pixelData;
    }
    
    @Override
    public boolean isInvalidated(PaletteChangeActionData other) {
        return this.palette.equals(other.palette) && Arrays.equals(this.pixelData, other.pixelData);
    }

    @Override
    public boolean canBeCombined(PaletteChangeActionData other) {
        return false;
    }

    @Override
    public PaletteChangeActionData combine(PaletteChangeActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("Palette %s. Swap Pixels = %b", palette == null ? "NULL" : palette.getName(), pixelData == null);
    }
}
