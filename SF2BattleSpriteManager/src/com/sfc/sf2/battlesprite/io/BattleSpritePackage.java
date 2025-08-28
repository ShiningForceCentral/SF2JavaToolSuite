/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.io;

import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;

/**
 *
 * @author TiMMy
 */
public class BattleSpritePackage
{
    private boolean usePaletteFromImage;
    public boolean usePaletteFromImage() { return usePaletteFromImage; }
    private int exportPalette;
    public int exportPalette() { return exportPalette; }
    private FileFormat format;
    public FileFormat format() { return format; }
    
    public BattleSpritePackage(boolean usePaletteFromImage) {
        this.usePaletteFromImage = usePaletteFromImage;
        this.exportPalette = -1;
        this.format = FileFormat.UNKNOWN;
    }
    
    public BattleSpritePackage(int exportPalette, FileFormat format) {
        this.usePaletteFromImage = false;
        this.exportPalette = exportPalette;
        this.format = format;
    }
}
