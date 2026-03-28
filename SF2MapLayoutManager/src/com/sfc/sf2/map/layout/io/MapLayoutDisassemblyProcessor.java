/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import com.sfc.sf2.map.layout.compression.MapLayoutDecoder;

/**
 *
 * @author wiz
 */
public class MapLayoutDisassemblyProcessor extends AbstractDisassemblyProcessor<MapLayout, MapLayoutPackage> {

    @Override
    protected MapLayout parseDisassemblyData(byte[] data, MapLayoutPackage pckg) throws DisassemblyException {
        MapLayoutBlock[] layoutBlockset = new MapLayoutDecoder().decode(data, pckg.blockset());
        return new MapLayout(pckg.index(), pckg.tilesets(), layoutBlockset);
    }

    @Override
    protected byte[] packageDisassemblyData(MapLayout item, MapLayoutPackage pckg) throws DisassemblyException {
        MapLayoutDecoder decoder = new MapLayoutDecoder();
        return decoder.encode(item, pckg.blockset());
    }
}
