/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.io;

import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.compression.BattleTerrainStackDecoder;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.EmptyPackage;

/**
 *
 * @author TiMMy
 */
public class BattleTerrainDisassemblyProcessor extends AbstractDisassemblyProcessor<BattleMapTerrain, EmptyPackage> {
    
    @Override
    protected BattleMapTerrain parseDisassemblyData(byte[] data, EmptyPackage pckg) throws DisassemblyException {
        byte[] decodedData = new BattleTerrainStackDecoder().decode(data);
        return new BattleMapTerrain(decodedData);
    }
    
    @Override
    protected byte[] packageDisassemblyData(BattleMapTerrain item, EmptyPackage pckg) throws DisassemblyException {
        byte[] terrainBytes = new BattleTerrainStackDecoder().encode(item.getData());
        return terrainBytes;
    }
}
