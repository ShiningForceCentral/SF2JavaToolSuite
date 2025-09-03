/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.compression.MapBlocksetDecoder;
import static com.sfc.sf2.map.block.compression.MapBlocksetDecoder.TILESET_TILES;

/**
 *
 * @author TiMMy
 */
public class MapBlocksetDisassemblyProcessor extends AbstractDisassemblyProcessor<MapBlockset, MapBlockPackage> {
    
    @Override
    protected MapBlockset parseDisassemblyData(byte[] data, MapBlockPackage pckg) throws DisassemblyException {
        if(pckg.tilesets() == null || data == null) {
            throw new DisassemblyException("Cannot import map blockset.");
        }
        for (int t = 0; t < pckg.tilesets().length; t++) {
            if (pckg.tilesets()[t] != null) {
                Tile[] tiles = pckg.tilesets()[t].getTiles();
                for (int i = 0; i < tiles.length; i++) {
                    if (tiles[i] != null ) {
                        tiles[i].setId(t*TILESET_TILES+i);
                    }
                }
            }
        }
        MapBlock[] blocks = new MapBlocksetDecoder().decode(data, pckg.palette(), pckg.tilesets());
        Console.logger().finest("Created MapBlocks with " + blocks.length + " blocks.");
        return new MapBlockset(blocks, 12);
    }

    @Override
    protected byte[] packageDisassemblyData(MapBlockset item, MapBlockPackage pckg) throws DisassemblyException {
        return new MapBlocksetDecoder().encode(item.getBlocks(), pckg.palette(), pckg.tilesets());
    }
}
