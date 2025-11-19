/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords;

import com.sfc.sf2.battle.mapcoords.io.BattleMapCoordsAsmProcessor;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.MapLayoutManager;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class BattleMapCoordsManager extends AbstractManager {

    private final MapLayoutManager mapLayoutManager = new MapLayoutManager();
    private final BattleMapCoordsAsmProcessor coordsAsmProcessor = new BattleMapCoordsAsmProcessor();
    
    private BattleMapCoords[] coords;
    private MapLayout battleMapLayout;

    @Override
    public void clearData() {
        mapLayoutManager.clearData();
        if (battleMapLayout != null) {
            battleMapLayout.clearIndexedColorImage(true);
            battleMapLayout = null;
        }
        coords = null;
    }
    
    public BattleMapCoords[] importDisassembly(Path battleMapCoordsPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        coords = coordsAsmProcessor.importAsmData(battleMapCoordsPath, null);
        Console.logger().info("Battle map coords successfully imported from : " + battleMapCoordsPath);
        Console.logger().finest("EXITING importDisassembly");
        return coords;
    }
    
    public void exportDisassembly(Path battleMapCoordsPath, BattleMapCoords[] coords) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.coords = coords;
        coordsAsmProcessor.exportAsmData(battleMapCoordsPath, coords, null);
        Console.logger().info("Battle coords successfully exported to : " + battleMapCoordsPath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public MapLayout importMapFromEntries(Path paletteEntriesPath, Path tilesetsEntriesPath, Path mapEntriesPath, int mapId) throws IOException, AsmException, DisassemblyException {
        mapLayoutManager.ImportMapEntries(mapEntriesPath);
        battleMapLayout = mapLayoutManager.importDisassemblyFromMapEntries(paletteEntriesPath, tilesetsEntriesPath, mapEntriesPath, mapId);
        return battleMapLayout;
    }

    public BattleMapCoords[] getCoords() {
        return coords;
    }

    public void setCoords(BattleMapCoords[] coords) {
        this.coords = coords;
    }

    public MapLayout getMapLayout() {
        return battleMapLayout;
    }

    public void setMapLayout(MapLayout battleMapLayout) {
        this.battleMapLayout = battleMapLayout;
    }
}
