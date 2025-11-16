/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.helpers.TileHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteDecoder;
import com.sfc.sf2.spellGraphic.InvocationGraphic;

/**
 *
 * @author TiMMy
 */
public class InvocationDisassemblyProcessor extends AbstractDisassemblyProcessor<InvocationGraphic, InvocationPackage> {
    
    @Override
    protected InvocationGraphic parseDisassemblyData(byte[] data, InvocationPackage pckg) throws DisassemblyException {
        if(data.length < 42) {
            throw new DisassemblyException("Invocation ignored because of too small length (must be a dummy file).");
        }
        //First 6 bytes = ??? (or 3 words)
        //4th word = paletteOffset
        //Next bytes until paletteOffset = offsets for each frame (each frame 128 x 64)(makes half of full sprite)
        short posX = BinaryHelpers.getWord(data, 0);
        short posY = BinaryHelpers.getWord(data, 2);
        short loadMode = BinaryHelpers.getWord(data, 4);

        int palettesOffset = BinaryHelpers.getWord(data, 6) + 6;
        byte[] paletteData = new byte[32];
        System.arraycopy(data, palettesOffset, paletteData, 0, paletteData.length);
        Palette palette = new Palette(pckg.name(), PaletteDecoder.decodePalette(paletteData), true);

        int[] frameOffsets = new int[(palettesOffset-8) / 2];
        for (int i = 0; i < frameOffsets.length; i++) {
            frameOffsets[i] = BinaryHelpers.getWord(data, 8 + i*2) + (8 + i*2);
        }
        Tileset[] frameList = new Tileset[frameOffsets.length];
        for (int i = 0; i < frameOffsets.length; i++) {
            int frameOffset = BinaryHelpers.getWord(data, 8 + i*2) + (8 + i*2);
            int dataLength = (i == frameOffsets.length-1) ? data.length-frameOffsets[i] : frameOffsets[i+1] - frameOffsets[i];
            byte[] tileData = new byte[dataLength];
            System.arraycopy(data, frameOffset, tileData, 0, dataLength);
            Tile[] frame = new StackGraphicsDecoder().decode(tileData, palette);
            frame = TileHelpers.reorderTilesSequentially(frame, 4, 2, 4);
            frameList[i] = new Tileset(Integer.toString(i), frame, InvocationGraphic.INVOCATION_TILE_WIDTH);
            Console.logger().finest("Frame "+i+" length="+dataLength+", offset="+frameOffset+", tiles="+frameList[i].getTiles().length);
        }
        return new InvocationGraphic(frameList, posX, posY, loadMode);
    }
    
    @Override
    protected byte[] packageDisassemblyData(InvocationGraphic item, InvocationPackage pckg) throws DisassemblyException {
        short posX = item.getPosX();
        short posY = item.getPosY();
        short loadMode = item.getLoadMode();

        Palette palette = item.getPalette();
        byte[] paletteBytes;
        paletteBytes = PaletteDecoder.encodePalette(palette.getColors());
        short paletteOffset = (short)(item.getFrames().length*2 + 2);

        Tileset[] frames = item.getFrames();
        byte[][] frameBytes = new byte[frames.length][];
        short[] frameOffsets = new short[frames.length];
        int totalFramesSize = 0;
        for (int i = 0; i < frames.length; i++) {
            Tile[] tiles = TileHelpers.reorderTilesForDisasssembly(frames[i].getTiles(), 4, 2, 4);
            frameBytes[i] = new StackGraphicsDecoder().encode(tiles);
            if (i == 0) {
                frameOffsets[i] = (short)(frames.length*2 + 32);
            } else {
                int target = frameOffsets[i-1] + 6 + (i-1)*2 + frameBytes[i-1].length;
                int offsetLocation = 6 + i*2;
                frameOffsets[i] = (short)((target - offsetLocation)&0xFFFF);
            }
            Console.logger().finest("Frame "+i+" length="+frameBytes[i].length+", offset="+frameOffsets[i]);
            totalFramesSize += frameBytes[i].length;
        }

        int totalSize = 8 + frames.length * 2 + palette.getColors().length * 32 + totalFramesSize;
        byte[] newInvocationBytes = new byte[totalSize];
        BinaryHelpers.setWord(newInvocationBytes, 0, posX);
        BinaryHelpers.setWord(newInvocationBytes, 2, posY);
        BinaryHelpers.setWord(newInvocationBytes, 4, loadMode);
        BinaryHelpers.setWord(newInvocationBytes, 6, paletteOffset);
        for(int i=0;i<frameOffsets.length;i++){
            BinaryHelpers.setWord(newInvocationBytes, 8 + i*2, frameOffsets[i]);
        }
        System.arraycopy(paletteBytes, 0, newInvocationBytes, 6 + paletteOffset, 32);
        for (int i = 0; i < frameBytes.length; i++) {
            Console.logger().finest("Writing frame "+i+" with length="+frameBytes[i].length+" at offset="+(int)(frameOffsets[i]+8 + i*2));
            System.arraycopy(frameBytes[i], 0, newInvocationBytes, frameOffsets[i]+8 + i*2, frameBytes[i].length);
        }
        return newInvocationBytes;
    }
}
