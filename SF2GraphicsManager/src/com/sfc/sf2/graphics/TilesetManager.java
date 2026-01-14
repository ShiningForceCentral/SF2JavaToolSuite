/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor.TilesetCompression;
import com.sfc.sf2.graphics.io.TilesetPackage;
import com.sfc.sf2.graphics.io.TilesetRawImageProcessor;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.palette.io.PalettePackage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class TilesetManager extends AbstractManager {
    
    private Tileset tileset;
    
    @Override
    public void clearData() {
        if (tileset != null) {
            tileset.clearIndexedColorImage(true);
            tileset = null;
        }
    }
       
    public Tileset importDisassembly(Path graphicsFilePath, Palette palette, TilesetCompression compression, int tilesPerRow) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        TilesetPackage pckg = new TilesetPackage(PathHelpers.filenameFromPath(graphicsFilePath), compression, palette, tilesPerRow);
        tileset = new TilesetDisassemblyProcessor().importDisassembly(graphicsFilePath, pckg);
        Console.logger().info("Tileset successfully imported from : " + graphicsFilePath);
        Console.logger().finest("EXITING importDisassembly");
        return tileset;
    }
       
    public Tileset importDisassembly(Path paletteFilePath, Path graphicsFilePath, TilesetCompression compression, int tilesPerRow) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        Palette palette = new PaletteManager().importDisassembly(paletteFilePath, true);
        TilesetPackage pckg = new TilesetPackage(PathHelpers.filenameFromPath(graphicsFilePath), compression, palette, tilesPerRow);
        tileset = new TilesetDisassemblyProcessor().importDisassembly(graphicsFilePath, pckg);
        Console.logger().info("Tileset successfully imported from : " + graphicsFilePath);
        Console.logger().finest("EXITING importDisassembly");
        return tileset;
    }
    
    public void exportDisassembly(Path graphicsFilePath, Tileset tileset, TilesetCompression compression) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.tileset = tileset;
        TilesetPackage pckg = new TilesetPackage(PathHelpers.filenameFromPath(graphicsFilePath), compression, tileset.getPalette(), tileset.getTilesPerRow());
        new TilesetDisassemblyProcessor().exportDisassembly(graphicsFilePath, tileset, pckg);
        Console.logger().info("Tileset successfully exported to : " + graphicsFilePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public Tileset importImage(Path filePath, boolean firstColorTransparent) throws IOException, RawImageException {
        Console.logger().finest("ENTERING importImage");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        tileset = new TilesetRawImageProcessor().importRawImage(filePath, pckg);
        Console.logger().info("Tileset successfully imported from : " + filePath);
        new PaletteManager().setPalette(tileset.getPalette());
        Console.logger().finest("EXITING importImage");
        return tileset;
    }
    
    public void exportImage(Path filePath, Tileset tileset) throws IOException, RawImageException {
        Console.logger().finest("ENTERING exportImage");
        this.tileset = tileset;
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), true);
        new TilesetRawImageProcessor().exportRawImage(filePath, tileset, pckg);
        Console.logger().info("Tileset successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportImage");
    }
       
    /*public void importDisassemblyWithLayout(Path baseTilesetFilePath,Path palette1FilePath, int palette1Offset, Path palette2FilePath, int palette2Offset, Path palette3FilePath, int palette3Offset, Path palette4FilePath, int palette4Offset,
            Path tileset1FilePath, int tileset1Offset, Path tileset2FilePath, int tileset2Offset, Path layoutFilePath, TilesetCompression compression, int tilesPerRow)
            throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyWithLayout");
        Path[] palettePaths = new Path[] { palette1FilePath, palette2FilePath, palette3FilePath, palette4FilePath };
        int[] offsets = new int[] { palette1Offset, palette2Offset, palette3Offset, palette4Offset };
        int[] lengths = new int[] { 32, 32, 32, 32 };
        Palette[] palettes = paletteManager.importDisassemblyFromPartials(palettePaths, offsets, lengths, true);
        tileset = tilesetDisassemblyProcessor.importDisassemblyWithLayout(baseTilesetFilePath, palettes, tileset1FilePath, tileset1Offset, tileset2FilePath, tileset2Offset, compression, tilesPerRow, layoutFilePath);
        Console.logger().finest("EXITING importDisassemblyWithLayout");
    }
    
    public void exportTilesAndLayout(Path palettePath, Path tilesPath, Tileset tileset, Path layoutPath, int graphicsOffset, TilesetCompression compression, int palette)
            throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportTilesAndLayout");
        paletteManager.exportDisassembly(palettePath, tileset.getPalette());
        tilesetDisassemblyProcessor.exportTilesAndLayout(tileset, tilesPath, layoutPath, graphicsOffset, compression, tileset.getPalette());
        Console.logger().finest("EXITING exportTilesAndLayout");
    }*/

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }
}
