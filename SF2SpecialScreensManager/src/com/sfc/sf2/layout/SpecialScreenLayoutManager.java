/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.layout;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor.TilesetCompression;
import com.sfc.sf2.graphics.io.TilesetPackage;
import com.sfc.sf2.layout.io.TilesetLayoutDisassemblyProcessor;
import com.sfc.sf2.layout.io.SpecialScreenLayoutPackage;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.layout.io.LayoutTilesetRawImageProcessor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.palette.io.PaletteDisassemblyProcessor;
import com.sfc.sf2.palette.io.PalettePackage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class SpecialScreenLayoutManager extends AbstractManager {
    
    private SpecialScreenLayout layout;   //Uses MapBlocks because these are just tilesets where tiles can be flipped and given draw priority flags
    
    @Override
    public void clearData() {
        if (layout != null) {
            layout.clearIndexedColorImage(true);
            layout = null;
        }
    }
       
    public SpecialScreenLayout importDisassembly(Path baseTilesetFilePath, Path paletteFilePath, Path tilesetFilePath, Path layoutFilePath)
            throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyWithLayout");
        //Palette
        Palette[] palettes = new PaletteManager().importDisassembliesPacked(paletteFilePath, 32, true);
        //Tilesets
        Tileset[] tilesets = new Tileset[2];
        TilesetPackage baseTilesPckg = new TilesetPackage(PathHelpers.filenameFromPath(baseTilesetFilePath), TilesetCompression.STACK, palettes[0], 16);
        tilesets[0] = new TilesetDisassemblyProcessor().importDisassembly(baseTilesetFilePath, baseTilesPckg);
        TilesetPackage tilesPckg = new TilesetPackage(PathHelpers.filenameFromPath(tilesetFilePath), TilesetCompression.STACK, palettes[0], 16);
        tilesets[1] = new TilesetDisassemblyProcessor().importDisassembly(tilesetFilePath, tilesPckg);
        //Layout
        SpecialScreenLayoutPackage pckg = new SpecialScreenLayoutPackage(PathHelpers.filenameFromPath(layoutFilePath), palettes, tilesets, 32);
        layout = new TilesetLayoutDisassemblyProcessor().importDisassembly(layoutFilePath, pckg);
        Console.logger().info("Special Screen Layout successfully imported from : " + layoutFilePath);
        Console.logger().finest("EXITING importDisassemblyWithLayout");
        return layout;
    }
    
    public void exportPalette(SpecialScreenLayout layout, Path palettePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportPalette");
        Tileset[] tilesets = layout.getTilesets();
        PalettePackage palettePckg = new PalettePackage(tilesets[1].getPalette());
        new PaletteDisassemblyProcessor().exportDisassembly(palettePath, tilesets[1].getPalette(), palettePckg);
        Console.logger().info("Layout palette successfully exported to : " + palettePath);
        Console.logger().finest("EXITING exportPalette");
    }
    
    public void exportTileset(SpecialScreenLayout layout, Path tilesetPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportTileset");
        Tileset[] tilesets = layout.getTilesets();
        TilesetPackage tilesetPckg = new TilesetPackage(tilesets[1].getName(), TilesetCompression.STACK, tilesets[1].getPalette(), tilesets[1].getTilesPerRow());
        new TilesetDisassemblyProcessor().exportDisassembly(tilesetPath, tilesets[1], tilesetPckg);
        Console.logger().info("Layout tileset successfully exported to : " + tilesetPath);
        Console.logger().finest("EXITING exportTileset");
    }
    
    public void exportLayout(SpecialScreenLayout layout, Path layoutPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportLayout");
        SpecialScreenLayoutPackage pckg = new SpecialScreenLayoutPackage(layout.getName(), layout.getPalettes(), layout.getTilesets(), layout.getTilesPerRow());
        new TilesetLayoutDisassemblyProcessor().exportDisassembly(layoutPath, layout, pckg);
        Console.logger().info("Layout successfully exported to : " + layoutPath);
        Console.logger().finest("EXITING exportLayout");
    }
    
    public SpecialScreenLayout importImage(Path baseTilesetFilePath, Path filePath, boolean firstColorTransparent) throws IOException, RawImageException, DisassemblyException {
        Console.logger().finest("ENTERING importImage");
        Tileset[] baseTilesets = new Tileset[1];
        TilesetPackage baseTilesPckg = new TilesetPackage(PathHelpers.filenameFromPath(baseTilesetFilePath), TilesetCompression.STACK, null, 16);
        baseTilesets[0] = new TilesetDisassemblyProcessor().importDisassembly(baseTilesetFilePath, baseTilesPckg);
        SpecialScreenLayoutPackage pckg = new SpecialScreenLayoutPackage(PathHelpers.filenameFromPath(filePath), null, baseTilesets, 32);
        layout = new LayoutTilesetRawImageProcessor().importRawImage(filePath, pckg);
        Console.logger().info("Layout image successfully imported from : " + filePath);
        Console.logger().finest("EXITING importImage");
        return layout;
    }
    
    public void exportImage(Path filePath, SpecialScreenLayout layout) throws IOException, RawImageException {
        Console.logger().finest("ENTERING exportImage");
        this.layout = layout;
        SpecialScreenLayoutPackage pckg = new SpecialScreenLayoutPackage(PathHelpers.filenameFromPath(filePath), layout.getPalettes(), layout.getTilesets(), layout.getTilesPerRow());
        new LayoutTilesetRawImageProcessor().exportRawImage(filePath, layout, pckg);
        Console.logger().info("Layout image successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportImage");
    }

    public SpecialScreenLayout getLayout() {
        return layout;
    }

    public void setLayout(SpecialScreenLayout layout) {
        this.layout = layout;
    }
}
