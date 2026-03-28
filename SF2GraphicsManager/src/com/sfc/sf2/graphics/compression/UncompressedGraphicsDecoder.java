/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.compression;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.graphics.Tile;
import static com.sfc.sf2.graphics.Tile.*;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author wiz
 */
public class UncompressedGraphicsDecoder extends AbstractGraphicsDecoder {
    
    @Override
    public Tile[] decode(byte[] input, Palette palette) {
        Console.logger().finest("ENTERING decode");
        Console.logger().finest("Data length = " + input.length + ", -> expecting " + input.length/32 + " tiles to parse.");
        Tile[] tiles = new Tile[input.length/32];
        for(int i=0;i<tiles.length;i++){
            byte[] pixels = new byte[PIXEL_COUNT];
            for(int y=0;y<PIXEL_HEIGHT;y++){
                for(int x=0;x<PIXEL_WIDTH;x+=2){
                    byte currentByte = input[i*32+(y*8+x)/2];
                    byte firstPixel = (byte)((currentByte & 0xF0)/16);
                    byte secondPixel = (byte)(currentByte & 0x0F);
                    pixels[x+y*PIXEL_WIDTH] = firstPixel;
                    pixels[x+1+y*PIXEL_WIDTH] = secondPixel;
                }
            }
            Tile tile = new Tile(i, pixels, palette);
            //Console.logger().finest(tile.toString());
            tiles[i] = tile;
        }
        Console.logger().finest("EXITING decode");
        return tiles;
    }
    
    @Override
    public byte[] encode(Tile[] tiles) {
        Console.logger().finest("ENTERING encode");
        Console.logger().finest("Tiles length = " + tiles.length + ", -> expecting " + tiles.length*32 + " byte output.");
        byte[] output = new byte[tiles.length*32];
        for(int i=0;i<tiles.length;i++){
            byte[] pixels = tiles[i].getPixels();
            for(int y=0;y<PIXEL_HEIGHT;y++){
                for(int x=0;x<PIXEL_WIDTH;x+=2){
                    byte first = pixels[x+y*PIXEL_WIDTH];
                    byte second = pixels[x+1+y*PIXEL_WIDTH];
                    output[(i*64+y*8+x)/2] = (byte)(first*16 | second);
                }
            }
        }
        Console.logger().finest("EXITING encode");
        return output;
    }
}
