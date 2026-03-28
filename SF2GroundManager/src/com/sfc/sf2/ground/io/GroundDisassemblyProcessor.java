/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.compression.StackGraphicsDecoder;
import com.sfc.sf2.ground.Ground;
import static com.sfc.sf2.ground.Ground.GROUND_TILES_PER_ROW;
import com.sfc.sf2.helpers.TileHelpers;


/**
 *
 * @author wiz
 */
public class GroundDisassemblyProcessor extends AbstractDisassemblyProcessor<Ground, GroundPackage> {

    @Override
    protected Ground parseDisassemblyData(byte[] data, GroundPackage pckg) throws DisassemblyException {
        Tile[] tiles = new StackGraphicsDecoder().decode(data, pckg.palette());
        tiles = TileHelpers.reorderTilesSequentially(tiles, 3, 1, 4);
        return new Ground(new Tileset(pckg.name(), tiles, GROUND_TILES_PER_ROW));
    }
    
    @Override
    protected byte[] packageDisassemblyData(Ground item, GroundPackage pckg) throws DisassemblyException {
        Tile[] tiles = TileHelpers.reorderTilesForDisasssembly(item.getTileset().getTiles(), 3, 1, 4);
        return new StackGraphicsDecoder().encode(tiles);
    }
}
