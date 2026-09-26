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
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.palette.io.PalettePackage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

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
       
    public Tileset importDisassemblyWithLayout(Path baseTilesetFilePath, Path palette1FilePath, int palette1Offset, Path palette2FilePath, int palette2Offset, Path palette3FilePath, int palette3Offset, Path palette4FilePath, int palette4Offset,
            Path tileset1FilePath, int tileset1Offset, Path tileset2FilePath, int tileset2Offset, Path layoutFilePath, TilesetCompression compression, int tilesPerRow)
            throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassemblyWithLayout");
        Path[] palettePaths = new Path[] { palette1FilePath, palette2FilePath, palette3FilePath, palette4FilePath };
        int[] offsets = new int[] { palette1Offset, palette2Offset, palette3Offset, palette4Offset };
        int[] lengths = new int[] { 32, 32, 32, 32 };
        Palette[] palettes = new PaletteManager().importDisassemblyFromPartials(palettePaths, offsets, lengths, true);
        TilesetDisassemblyProcessor processor = new TilesetDisassemblyProcessor();
        Tile[] baseTiles = processor.importDisassembly(baseTilesetFilePath, new TilesetPackage(PathHelpers.filenameFromPath(baseTilesetFilePath), TilesetCompression.STACK, palettes[0], tilesPerRow)).getTiles();
        Tile[] tileset1 = processor.importDisassembly(tileset1FilePath, new TilesetPackage(PathHelpers.filenameFromPath(tileset1FilePath), compression, palettes[0], tilesPerRow)).getTiles();
        Tile[] tileset2 = processor.importDisassembly(tileset2FilePath, new TilesetPackage(PathHelpers.filenameFromPath(tileset2FilePath), compression, palettes[0], tilesPerRow)).getTiles();
        Tile[] vRamTiles = new Tile[0x800];
        System.arraycopy(baseTiles, 0, vRamTiles, 0, Math.min(baseTiles.length, vRamTiles.length));
        if (tileset1Offset >= 0 && tileset1Offset < vRamTiles.length) {
            System.arraycopy(tileset1, 0, vRamTiles, tileset1Offset, Math.min(tileset1.length, vRamTiles.length - tileset1Offset));
        }
        if (tileset2Offset >= 0 && tileset2Offset < vRamTiles.length) {
            System.arraycopy(tileset2, 0, vRamTiles, tileset2Offset, Math.min(tileset2.length, vRamTiles.length - tileset2Offset));
        }
        byte[] data = Files.readAllBytes(layoutFilePath);
        Tile[] layoutTiles = new Tile[data.length / 2];
        for (int i = 0; i < layoutTiles.length; i++) {
            int layoutValue = BinaryHelpers.getWord(data, i * 2) & 0xFFFF;
            int palette = (layoutValue & 0x6000) >> 13;
            int vFlip = (layoutValue & 0x1000) >> 12;
            int hFlip = (layoutValue & 0x0800) >> 11;
            int tileId = layoutValue & 0x7FF;
            Tile outputTile = null;
            if (tileId >= 0 && tileId < vRamTiles.length) {
                outputTile = vRamTiles[tileId];
            }
            if (outputTile == null) {
                Console.logger().finest("Layout tile " + i + " : wrong tile id " + tileId);
                outputTile = baseTiles.length > 0 ? baseTiles[0] : Tile.EmptyTile(palettes[0]);
            } else {
                if (palette != 0 && palette < palettes.length && palettes[palette] != null) {
                    outputTile = Tile.paletteSwap(outputTile, palettes[palette]);
                }
                if (vFlip != 0) {
                    outputTile = Tile.vFlip(outputTile);
                }
                if (hFlip != 0) {
                    outputTile = Tile.hFlip(outputTile);
                }
            }
            layoutTiles[i] = outputTile;
        }
        tileset = new Tileset(PathHelpers.filenameFromPath(baseTilesetFilePath), layoutTiles, tilesPerRow);
        Console.logger().info("Tileset with layout successfully imported from : " + layoutFilePath);
        Console.logger().finest("EXITING importDisassemblyWithLayout");
        return tileset;
    }

    public void exportTilesAndLayout(Path palettePath, Path tilesPath, Tileset tileset, Path layoutPath, int graphicsOffset, TilesetCompression compression)
            throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportTilesAndLayout");
        this.tileset = tileset;
        new PaletteManager().exportDisassembly(palettePath, tileset.getPalette());
        Tile[] tiles = tileset.getTiles();
        byte[] layout = new byte[tiles.length * 2];
        ArrayList<Tile> uniqueTiles = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            int tilesetIndex = -1;
            for (int j = 0; j < uniqueTiles.size(); j++) {
                if (Arrays.equals(uniqueTiles.get(j).getPixels(), tile.getPixels())) {
                    tilesetIndex = j + graphicsOffset;
                    break;
                }
            }
            if (tilesetIndex == -1) {
                uniqueTiles.add(tile);
                tilesetIndex = uniqueTiles.size() - 1 + graphicsOffset;
            }
            BinaryHelpers.setWord(layout, i * 2, (short) (tilesetIndex & 0x7FF));
        }
        Tile[] outputTiles = uniqueTiles.toArray(Tile[]::new);
        Tileset uniqueTileset = new Tileset(PathHelpers.filenameFromPath(tilesPath), outputTiles, tileset.getTilesPerRow());
        TilesetPackage pckg = new TilesetPackage(uniqueTileset.getName(), compression, tileset.getPalette(), uniqueTileset.getTilesPerRow());
        new TilesetDisassemblyProcessor().exportDisassembly(tilesPath, uniqueTileset, pckg);
        Files.write(layoutPath, layout);
        Console.logger().info("Tileset and layout successfully exported to : " + tilesPath + " / " + layoutPath);
        Console.logger().finest("EXITING exportTilesAndLayout");
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }
}
