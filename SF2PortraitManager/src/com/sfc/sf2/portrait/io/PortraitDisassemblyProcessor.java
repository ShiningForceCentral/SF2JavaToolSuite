/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.palette.CRAMColor;
import com.sfc.sf2.portrait.Portrait;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteDecoder;

/**
 *
 * @author wiz
 */
public class PortraitDisassemblyProcessor extends AbstractDisassemblyProcessor<Portrait, PortraitPackage> {
    
    @Override
    protected Portrait parseDisassemblyData(byte[] data, PortraitPackage pckg) throws DisassemblyException {
        Tile[] tiles;
        if (data.length < 36) {
            throw new DisassemblyException("Portrait file too small (must be a dummy file). Size : " + data.length);
        }
        short eyesTileNumber = BinaryHelpers.getWord(data,0);
        int[][] eyesTiles = new int[eyesTileNumber][4];
        for (int i=0; i < eyesTileNumber; i++) {
            eyesTiles[i][0] = (int)(BinaryHelpers.getByte(data,2+i*4+0)&0xFF);
            eyesTiles[i][1] = (int)(BinaryHelpers.getByte(data,2+i*4+1)&0xFF);
            eyesTiles[i][2] = (int)(BinaryHelpers.getByte(data,2+i*4+2)&0xFF);
            eyesTiles[i][3] = (int)(BinaryHelpers.getByte(data,2+i*4+3)&0xFF);
        }
        
        short mouthTileNumber = BinaryHelpers.getWord(data,2+eyesTileNumber*4);
        int[][] mouthTiles = new int[mouthTileNumber][4];
        for (int i=0; i < mouthTileNumber; i++) {
            mouthTiles[i][0] = (int)(BinaryHelpers.getByte(data,2+eyesTileNumber*4+2+i*4+0)&0xFF);
            mouthTiles[i][1] = (int)(BinaryHelpers.getByte(data,2+eyesTileNumber*4+2+i*4+1)&0xFF);
            mouthTiles[i][2] = (int)(BinaryHelpers.getByte(data,2+eyesTileNumber*4+2+i*4+2)&0xFF);
            mouthTiles[i][3] = (int)(BinaryHelpers.getByte(data,2+eyesTileNumber*4+2+i*4+3)&0xFF);
        } 
        
        int paletteOffset = 2+eyesTileNumber*4+2+mouthTileNumber*4;
        byte[] paletteData = new byte[32];
        System.arraycopy(data, paletteOffset, paletteData, 0, paletteData.length);
        CRAMColor[] colors = PaletteDecoder.decodePalette(paletteData);
        Palette palette = new Palette(pckg.name(), colors, true);
        
        int graphicsOffset = paletteOffset + 32;
        byte[] tileData = new byte[data.length-graphicsOffset];
        System.arraycopy(data, graphicsOffset, tileData, 0, tileData.length);
        tiles = new StackGraphicsDecoder().decode(tileData, palette);
        Tileset tileset = new Tileset(pckg.name(), tiles, Portrait.PORTRAIT_TILES_FULL_WIDTH);
        
        return new Portrait(pckg.index(), pckg.name(), tileset, eyesTiles, mouthTiles);
    }

    @Override
    protected byte[] packageDisassemblyData(Portrait item, PortraitPackage pckg) throws DisassemblyException {
        byte[] eyeTiles = new byte[2+item.getEyeTiles().length*4];
        eyeTiles[0] = 0;
        eyeTiles[1] = (byte)(item.getEyeTiles().length & 0xFF);
        for(int i=0;i<item.getEyeTiles().length;i++){
            eyeTiles[2+i*4+0] = (byte)(item.getEyeTiles()[i][0] & 0xFF);
            eyeTiles[2+i*4+1] = (byte)(item.getEyeTiles()[i][1] & 0xFF);
            eyeTiles[2+i*4+2] = (byte)(item.getEyeTiles()[i][2] & 0xFF);
            eyeTiles[2+i*4+3] = (byte)(item.getEyeTiles()[i][3] & 0xFF);
        }
        
        byte[] mouthTiles = new byte[2+item.getMouthTiles().length*4];
        mouthTiles[0] = 0;
        mouthTiles[1] = (byte)(item.getMouthTiles().length & 0xFF);
        for(int i=0;i<item.getMouthTiles().length;i++){
            mouthTiles[2+i*4+0] = (byte)(item.getMouthTiles()[i][0] & 0xFF);
            mouthTiles[2+i*4+1] = (byte)(item.getMouthTiles()[i][1] & 0xFF);
            mouthTiles[2+i*4+2] = (byte)(item.getMouthTiles()[i][2] & 0xFF);
            mouthTiles[2+i*4+3] = (byte)(item.getMouthTiles()[i][3] & 0xFF);
        }
        
        byte[] paletteBytes = PaletteDecoder.encodePalette(item.getPalette().getColors());
        byte[] tilesetBytes = new StackGraphicsDecoder().encode(item.getTileset().getTiles());
        byte[] newPortraitFileBytes = new byte[eyeTiles.length+mouthTiles.length+paletteBytes.length+tilesetBytes.length];
        System.arraycopy(eyeTiles, 0, newPortraitFileBytes, 0, eyeTiles.length);
        System.arraycopy(mouthTiles, 0, newPortraitFileBytes, eyeTiles.length, mouthTiles.length);
        System.arraycopy(paletteBytes, 0, newPortraitFileBytes, eyeTiles.length+mouthTiles.length, paletteBytes.length);
        System.arraycopy(tilesetBytes, 0, newPortraitFileBytes, eyeTiles.length+mouthTiles.length+paletteBytes.length, tilesetBytes.length);
        Console.logger().finest("Portrait exported : " + newPortraitFileBytes.length + " bytes");      
        return newPortraitFileBytes;
    }
}
