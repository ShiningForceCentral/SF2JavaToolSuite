/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compression.UncompressedGraphicsDecoder;

/**
 *
 * @author TiMMy
 */
public class TilesetDisassemblyProcessor extends AbstractDisassemblyProcessor<Tileset, TilesetPackage> {

    public enum TilesetCompression {
        NONE,
        BASIC,
        STACK,
    }
    
    @Override
    protected Tileset parseDisassemblyData(byte[] data, TilesetPackage pckg) throws DisassemblyException {
        Tile[] tiles = null;
        switch (pckg.compression()) {
            case NONE:
                tiles = new UncompressedGraphicsDecoder().decode(data, pckg.palette());
                break;
            case BASIC:
                tiles = new BasicGraphicsDecoder().decode(data, pckg.palette());
                break;
            case STACK:
                tiles = new StackGraphicsDecoder().decode(data, pckg.palette());
                break;
            default:
                throw new DisassemblyException("Compression mode not recognosed. Compression : " + pckg.compression());
        }
        if (tiles == null || tiles.length == 0) {
            throw new DisassemblyException("Tileset not loaded. Tiles are empty.");
        }
        return new Tileset(pckg.name(), tiles, pckg.tilesPerRow());
    }

    @Override
    protected byte[] packageDisassemblyData(Tileset item, TilesetPackage pckg) throws DisassemblyException {
        byte[] bytes = null;
        switch(pckg.compression()){
            case NONE:
                bytes = new UncompressedGraphicsDecoder().encode(item.getTiles());
                break;
            case BASIC:
                bytes = new BasicGraphicsDecoder().encode(item.getTiles());
                break;
            case STACK:
                bytes = new StackGraphicsDecoder().encode(item.getTiles());
                break;
            default:
                throw new DisassemblyException("Compression mode not recognosed. Compression : " + pckg.compression());
        }
        if (bytes == null || bytes.length == 0) {
            throw new DisassemblyException("Tileset not loaded. Tiles are empty.");
        }
        return bytes;
    }
}
