/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.compression.MapBlocksetDecoder;

/**
 *
 * @author TiMMy
 */
public class MapBlocksetDisassemblyProcessor extends AbstractDisassemblyProcessor<MapBlockset, MapBlockPackage> {
    
    @Override
    protected MapBlockset parseDisassemblyData(byte[] data, MapBlockPackage pckg) throws DisassemblyException {
        if (data == null) {
            throw new DisassemblyException("Cannot import map blockset. No data.");
        }
        MapBlock[] blocks = new MapBlocksetDecoder().decode(data);
        Console.logger().finest("Created MapBlocks with " + blocks.length + " blocks.");
        return new MapBlockset(pckg.name(), blocks, 12);
    }

    @Override
    protected byte[] packageDisassemblyData(MapBlockset item, MapBlockPackage pckg) throws DisassemblyException {
        return new MapBlocksetDecoder().encode(item.getBlocks(), pckg.tilesets());
    }
}
