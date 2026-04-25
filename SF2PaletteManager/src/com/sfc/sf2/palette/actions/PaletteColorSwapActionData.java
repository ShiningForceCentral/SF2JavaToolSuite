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
public class PaletteColorSwapActionData implements IActionData<PaletteColorSwapActionData> {
    
    private final Palette palette;
    private final byte[] pixelData;
    private final int fromIndex;
    private final int toIndex;
    
    public PaletteColorSwapActionData(Palette palette, byte[] pixelData, int fromIndex, int toIndex) {
        this.palette = palette;
        this.pixelData = pixelData;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    public Palette palette() {
        return palette;
    }

    public byte[] pixelData() {
        return pixelData;
    }

    public int fromIndex() {
        return fromIndex;
    }

    public int toIndex() {
        return toIndex;
    }
    
    @Override
    public boolean isInvalidated(PaletteColorSwapActionData other) {
        return this.fromIndex == other.fromIndex && this.toIndex == other.toIndex && this.palette.equals(other.palette) && Arrays.equals(this.pixelData, other.pixelData);
    }

    @Override
    public boolean canBeCombined(PaletteColorSwapActionData other) {
        return other.fromIndex == this.toIndex;
    }

    @Override
    public PaletteColorSwapActionData combine(PaletteColorSwapActionData other) {
        return new PaletteColorSwapActionData(other.palette, other.pixelData, this.fromIndex, other.toIndex);
    }

    @Override
    public String toString() {
        return String.format("From %d to %d. Swap Pixels = %b", fromIndex, toIndex, pixelData != null);
    }
}
