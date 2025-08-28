/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.io;

import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.helpers.TileHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteDecoder;
import java.util.ArrayList;

/**
 *
 * @author wiz
 */
public class BattleSpriteDisassemblyProcessor extends AbstractDisassemblyProcessor<BattleSprite, EmptyPackage> {
    
    @Override
    protected BattleSprite parseDisassemblyData(byte[] data, EmptyPackage pckg) throws DisassemblyException {
        if (data.length < 42) {
            throw new DisassemblyException("File ignored because of too small length (must be a dummy file) " + data.length);
        }
        int animSpeed = BinaryHelpers.getWord(data, 0);
        byte statusOffsetX = BinaryHelpers.getByte(data, 2);
        byte statusOffsetY = BinaryHelpers.getByte(data, 3);
        int palettesOffset = 4 + BinaryHelpers.getWord(data, 4);
        int firstFrameOffset = 6 + BinaryHelpers.getWord(data, 6);
        
        ArrayList<Palette> paletteList = new ArrayList<>();
        for (int i=0; i*32 + palettesOffset < firstFrameOffset; i++) {
            byte[] paletteData = new byte[32];
            System.arraycopy(data, palettesOffset+i*32, paletteData, 0, paletteData.length);
            Palette palette = new Palette(Integer.toString(i), PaletteDecoder.decodePalette(paletteData), true);
            paletteList.add(palette);
        }
        Palette[] palettes = paletteList.toArray(new Palette[paletteList.size()]);
        
        BattleSpriteType type = null;
        ArrayList<Tileset> frameList = new ArrayList<>();
        for (int i=0; (6+i*2) < palettesOffset; i++) {
            int frameOffset = 6+i*2 + BinaryHelpers.getWord(data, 6+i*2);
            int dataLength = 0;
            if ((6+(i+1)*2) < palettesOffset) {
                dataLength = 6+i*2 + BinaryHelpers.getWord(data, 6+(i+1)*2)+2 - frameOffset;
            } else {
                dataLength = data.length - frameOffset;
            }
            byte[] tileData = new byte[dataLength];
            System.arraycopy(data, frameOffset, tileData, 0, dataLength);
            Tile[] frame = new StackGraphicsDecoder().decode(tileData, paletteList.get(0));
            if (type == null) type = frame.length == 144 ? BattleSpriteType.ALLY : BattleSpriteType.ENEMY;
            frame = TileHelpers.reorderTilesSequentially(frame, type == BattleSpriteType.ALLY ? 3 : 4, 3, 4);
            frameList.add(new Tileset(Integer.toString(i), frame, BattleSprite.getTilesPerRow(type)));
            //Console.logger().finest("Frame "+i+" length="+dataLength+", offset="+frameOffset+", tiles="+frame.length);
        }
        Tileset[] frames = frameList.toArray(new Tileset[frameList.size()]);
        
        return new BattleSprite(type, frames, palettes, animSpeed, statusOffsetX, statusOffsetY);
    }

    @Override
    protected byte[] packageDisassemblyData(BattleSprite item, EmptyPackage pckg) throws DisassemblyException {
        short animSpeed = (short)(item.getAnimSpeed()&0xFFFF);
        byte statusOffsetX = item.getStatusOffsetX();
        byte statusOffsetY = item.getStatusOffsetY();

        Palette[] palettes = item.getPalettes();
        byte[][] paletteBytes = new byte[palettes.length][];

        Tileset[] frames = item.getFrames();

        byte[][] frameBytes = new byte[frames.length][];
        short[] frameOffsets = new short[frames.length];

        short palettesOffset = (short) (frames.length * 2 + 2);

        for (int i=0; i < palettes.length; i++) {
            paletteBytes[i] = PaletteDecoder.encodePalette(palettes[i].getColors());
        }

        int totalSize = 6 + frames.length * 2 + palettes.length * 32;
        for (int i=0; i < frames.length; i++) {
            Tile[] frameTiles = frames[i].getTiles();
            frameTiles = TileHelpers.reorderTilesForDisasssembly(frameTiles, (item.getType() == BattleSpriteType.ALLY) ? 3 : 4, 3, 4);
            frameBytes[i] = new StackGraphicsDecoder().encode(frameTiles);
            if (i==0) {
                frameOffsets[i] = (short)(frames.length * 2 + palettes.length * 32);
                System.out.println("Frame "+i+" length="+frameBytes[i].length+", offset="+frameOffsets[i]);
            } else {
                int target = frameOffsets[i-1] + 6 + (i-1)*2 + frameBytes[i-1].length;
                int offsetLocation = 6 + i*2;
                frameOffsets[i] = (short)((target - offsetLocation)&0xFFFF);
                System.out.println("Frame "+i+" length="+frameBytes[i].length+", offset="+frameOffsets[i]);
            }
            totalSize += frameBytes[i].length;
        }

        byte[] newBattleSpriteFileBytes = new byte[totalSize];

        newBattleSpriteFileBytes[0] = (byte)((animSpeed&0xFF00) >> 8);
        newBattleSpriteFileBytes[1] = (byte)(animSpeed&0xFF); 
        newBattleSpriteFileBytes[2] = statusOffsetX;
        newBattleSpriteFileBytes[3] = statusOffsetY; 
        newBattleSpriteFileBytes[4] = (byte)((palettesOffset&0xFF00) >> 8);
        newBattleSpriteFileBytes[5] = (byte)(palettesOffset&0xFF); 
        for (int i=0; i < frameOffsets.length; i++) {
            newBattleSpriteFileBytes[6+i*2] =  (byte) ((frameOffsets[i]&0xFF00) >> 8);
            newBattleSpriteFileBytes[6+i*2+1] = (byte) (frameOffsets[i]&0xFF); 
        }
        for (int i=0; i < paletteBytes.length; i++) {
            System.arraycopy(paletteBytes[i], 0, newBattleSpriteFileBytes, 6+frameOffsets.length*2+i*32, 32);
        }
        for (int i=0; i < frameBytes.length; i++) {
            System.out.println("Writing frame "+i+" with length="+frameBytes[i].length+" at offset="+(int)(frameOffsets[i]+6+i*2));
            System.arraycopy(frameBytes[i], 0, newBattleSpriteFileBytes, frameOffsets[i]+6+i*2, frameBytes[i].length);
        }
        return newBattleSpriteFileBytes;
    }
}
