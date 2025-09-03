/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compression.BasicGraphicsDecoder;
import com.sfc.sf2.helpers.TileHelpers;

/**
 *
 * @author TiMMy
 */
public class MapSpriteDisassemblyProcessor extends AbstractDisassemblyProcessor<Block[], MapSpritePackage> {
    
    @Override
    protected Block[] parseDisassemblyData(byte[] data, MapSpritePackage pckg) throws DisassemblyException {
        Tile[] tiles = new BasicGraphicsDecoder().decode(data, pckg.palette());
        if (tiles == null || tiles.length == 0) {
            throw new DisassemblyException("Mapsprite tileset not loaded, tiles are empty. Mapsprite : " + pckg.name());
        } else if (tiles.length != 18) {
            System.out.println("Mapsprite tileset not loaded, expected 18 tiles but found " + tiles.length + ". Mapsprite : " + pckg.name());
        }
        Tile[] tiles_frame1 = new Tile[9];
        Tile[] tiles_frame2 = new Tile[9];
        System.arraycopy(tiles, 0, tiles_frame1, 0, 9);
        System.arraycopy(tiles, 9, tiles_frame2, 0, 9);
        tiles_frame1 = TileHelpers.reorderTilesSequentially(tiles_frame1, 1, 1, Block.TILE_WIDTH);
        tiles_frame2 = TileHelpers.reorderTilesSequentially(tiles_frame2, 1, 1, Block.TILE_WIDTH);
        int index = pckg.indices()[0]*6+pckg.indices()[1]*2;
        Block[] blocks = new Block[2];
        blocks[0] = new Block(index, tiles_frame1);
        blocks[1] = new Block(index+1, tiles_frame2);
        return blocks;
    }

    @Override
    protected byte[] packageDisassemblyData(Block[] item, MapSpritePackage pckg) throws DisassemblyException {
        Tile[] tiles = new Tile[Block.TILES_COUNT*item.length];
        for (int i = 0; i < item.length; i++) {
            if (tiles != null) {
                Tile[] frame = item[i].getTiles();
                frame = TileHelpers.reorderTilesForDisasssembly(frame, 1, 1, Block.TILE_WIDTH);
                System.arraycopy(frame, 0, tiles, i*Block.TILES_COUNT, Block.TILES_COUNT);
            }
        }
        
        byte[] bytes = new BasicGraphicsDecoder().encode(tiles);
        if (bytes == null || bytes.length == 0) {
            throw new DisassemblyException("Tileset not exported. Tiles are empty.");
        }
        return bytes;
    }
}
