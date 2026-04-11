/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */

package com.sfc.sf2.helpers;

import com.sfc.sf2.graphics.Tile;

/**
 *
 * @author TiMMy
 */
public class TileHelpers {
    
    /**
     *
     * For tiles imported from disassembly
     * @return
     */
    public static Tile[] reorderTilesSequentially(Tile[] tiles, int blockColumnCount, int blockRowCount, int tilesPerBlock) {
        /* Disassembly tiles are stored in 4x4 chunks (top-bottom, left-right)
            1  5  9 13 33 37                  
            2  6 10 14 34  .                  
            3  7 11 15 35  .                  
            4  8 12 16 36  .                  
           17 21 25 29                  . 125
           18 22 26 30                  . 126
           19 23 27 31                  . 127
           20 24 28 32                124 128
        */
        int blockTotalTiles = tilesPerBlock*tilesPerBlock;
        Tile[] newTiles = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            int bc = (i/tilesPerBlock) % blockColumnCount;
            int br = i/(blockColumnCount*blockTotalTiles);
            int tc = i%tilesPerBlock;
            int tr = (i/(tilesPerBlock*blockColumnCount)) % tilesPerBlock;
            newTiles[i] = tiles[bc*(blockTotalTiles*blockRowCount) + br*blockTotalTiles + tc*tilesPerBlock + tr];
            newTiles[i].setId(i);
        }
        return newTiles;
    }
    
    /**
     *
     * For tiles being exported back to disassembly
     * @return
     */
    public static Tile[] reorderTilesForDisasssembly(Tile[] tiles, int blockColumnCount, int blockRowCount, int tilesPerBlock) {
        int blockTotalTiles = tilesPerBlock*tilesPerBlock;
        Tile[] newTiles = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            int bc = (i/tilesPerBlock) % blockColumnCount;
            int br = i/(blockColumnCount*blockTotalTiles);
            int tc = i%tilesPerBlock;
            int tr = (i/(tilesPerBlock*blockColumnCount)) % tilesPerBlock;
            int id = bc*(blockTotalTiles*blockRowCount) + br*blockTotalTiles + tc*tilesPerBlock + tr;
            newTiles[id] = tiles[i];
            newTiles[id].setId(i);
        }
        return newTiles;
    }
}
