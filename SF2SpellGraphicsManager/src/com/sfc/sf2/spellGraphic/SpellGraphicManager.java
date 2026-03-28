/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.TilesetRawImageProcessor;
import com.sfc.sf2.helpers.PaletteHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.palette.io.PalettePackage;
import com.sfc.sf2.spellGraphic.io.SpellDisassemblyProcessor;
import com.sfc.sf2.spellGraphic.io.SpellTilesetPackage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class SpellGraphicManager extends AbstractManager {
    private static final int[] paletteReplaceIndices = new int[] { 9, 13, 14 };
        
    private Palette defaultPalette;
    private Tileset spellTileset;

    @Override
    public void clearData() {
        defaultPalette = null;
        spellTileset = null;
    }
    
    public Tileset importDisassembly(Path filepath, Path defaultPalettePath, int tilesPerRow) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        importDefaultPalette(defaultPalettePath);
        SpellTilesetPackage pckg = new SpellTilesetPackage(PathHelpers.filenameFromPath(filepath), defaultPalette, tilesPerRow);
        spellTileset = new SpellDisassemblyProcessor().importDisassembly(filepath, pckg);
        Console.logger().info("Spell successfully imported from : " + filepath);
        Console.logger().finest("EXITING importDisassembly");
        return spellTileset;
    }
    
    public void exportDisassembly(Path filePath, Tileset spellTileset) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.spellTileset = spellTileset;
        new SpellDisassemblyProcessor().exportDisassembly(filePath, spellTileset, null);
        Console.logger().info("Spell successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public Tileset importImage(Path filePath, Path defaultPalettePath) throws RawImageException, IOException, DisassemblyException {
        Console.logger().finest("ENTERING importImage");
        importDefaultPalette(defaultPalettePath);
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), true);
        spellTileset = new TilesetRawImageProcessor().importRawImage(filePath, pckg);
        Palette palette = spellTileset.getPalette();
        palette = PaletteHelpers.combinePalettes(defaultPalette, palette, paletteReplaceIndices, paletteReplaceIndices);
        Console.logger().info("Spell successfully imported from : " + filePath);
        Console.logger().finest("EXITING importImage");
        return spellTileset;
    }
    
    public void exportImage(Path filePath, Tileset spellTileset, int tilesPerRow) throws RawImageException, IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportImage");
        this.spellTileset = spellTileset;
        spellTileset.setTilesPerRow(tilesPerRow);
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), true);
        new TilesetRawImageProcessor().exportRawImage(filePath, spellTileset, pckg);
        Console.logger().info("Spell successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportImage");
    }
    
    private void importDefaultPalette(Path defaultPalettePath) throws IOException, DisassemblyException {        
        if (defaultPalette == null) {
            defaultPalette = new PaletteManager().importDisassembly(defaultPalettePath, true);
        }
    }

    public Tileset getSpellTileset() {
        return spellTileset;
    }

    public Palette getDefaultPalette() {
        return defaultPalette;
    }
}
