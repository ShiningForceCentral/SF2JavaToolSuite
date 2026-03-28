/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background.io;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.helpers.TileHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteDecoder;

/**
 *
 * @author wiz
 */
public class BackgroundDisassemblyProcessor extends AbstractDisassemblyProcessor<Background, BackgroundPackage> {

    @Override
    protected Background parseDisassemblyData(byte[] data, BackgroundPackage pckg) throws DisassemblyException {
        if (data.length <= 2) {
            throw new DisassemblyException("File ignored because of too small length (must be a dummy file) " + data.length);
        }
        short tileset1Offset = BinaryHelpers.getWord(data, 0);
        short tileset2Offset = (short)(BinaryHelpers.getWord(data, 2)+2);
        short paletteOffset = (short)(BinaryHelpers.getWord(data, 4)+4);
        byte[] tileset1Data = new byte[data.length-tileset1Offset];
        System.arraycopy(data, tileset1Offset, tileset1Data, 0, tileset1Data.length);
        byte[] tileset2Data = new byte[data.length-tileset2Offset];
        System.arraycopy(data, tileset2Offset, tileset2Data, 0, tileset2Data.length);
        byte[] paletteData = new byte[32];
        System.arraycopy(data, paletteOffset, paletteData, 0, paletteData.length);
        Palette palette = new Palette("Background Palette", PaletteDecoder.decodePalette(paletteData), false);
        Tile[] tileset1 = new StackGraphicsDecoder().decode(tileset1Data, palette);
        Tile[] tileset2 = new StackGraphicsDecoder().decode(tileset2Data, palette);
        Tile[] tiles = new Tile[tileset1.length+tileset2.length];
        System.arraycopy(tileset1, 0, tiles, 0, tileset1.length);
        System.arraycopy(tileset2, 0, tiles, tileset1.length, tileset2.length);
        tiles = TileHelpers.reorderTilesSequentially(tiles, 8, 3, 4);
        return new Background(pckg.index(), new Tileset(null, tiles, Background.BG_TILES_WIDTH));
    }
    
    @Override
    protected byte[] packageDisassemblyData(Background item, BackgroundPackage pckg) throws DisassemblyException {
        Tile[] tileset1 = new Tile[192];
        Tile[] tileset2 = new Tile[192];
        Tile[] tiles = item.getTileset().getTiles();
        tiles = TileHelpers.reorderTilesForDisasssembly(tiles, 8, 3, 4);
        System.arraycopy(tiles,0,tileset1,0,192);
        System.arraycopy(tiles,192,tileset2,0,192);
        byte[] newTileset1 = new StackGraphicsDecoder().encode(tileset1);
        byte[] newTileset2 = new StackGraphicsDecoder().encode(tileset2);
        byte[] newBackgroundFileBytes = new byte[2+2+2+32+newTileset1.length+newTileset2.length];
        short tileset2Offset = (short) (newTileset1.length + 6 + 32 - 2);
        newBackgroundFileBytes[0] = 0;
        newBackgroundFileBytes[1] = 0x26;
        newBackgroundFileBytes[2] = (byte)((tileset2Offset>>8)&0xFF);
        newBackgroundFileBytes[3] = (byte)(tileset2Offset&0xFF);
        newBackgroundFileBytes[4] = 0;
        newBackgroundFileBytes[5] = 2;
        byte[] palette = PaletteDecoder.encodePalette(item.getPalette().getColors());
        System.arraycopy(palette, 0, newBackgroundFileBytes, 6, palette.length);
        System.arraycopy(newTileset1, 0, newBackgroundFileBytes, 0x26, newTileset1.length);
        System.arraycopy(newTileset2, 0, newBackgroundFileBytes, 0x26+newTileset1.length, newTileset2.length);
        return newBackgroundFileBytes;
    }
}
