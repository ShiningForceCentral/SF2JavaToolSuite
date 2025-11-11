/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.battle.mapcoords.BattleMapCoordsManager;
import com.sfc.sf2.battle.mapterrain.io.BattleTerrainDisassemblyProcessor;
import com.sfc.sf2.battle.mapterrain.io.LandEffectAsmProcessor;
import com.sfc.sf2.battle.mapterrain.io.LandEffectEnumsAsmProcessor;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.layout.MapLayout;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class BattleMapTerrainManager extends AbstractManager {

    private final BattleMapCoordsManager mapCoordsManager = new BattleMapCoordsManager();
    private final BattleTerrainDisassemblyProcessor terrainDisassemblyProcessor = new BattleTerrainDisassemblyProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    private final LandEffectEnumsAsmProcessor landEffectEnumsAsmProcessor = new LandEffectEnumsAsmProcessor();
    private final LandEffectAsmProcessor landEffectAsmProcessor = new LandEffectAsmProcessor();

    private BattleMapCoords[] allCoords;
    private BattleMapCoords coords;
    private BattleMapTerrain terrain;
    private String sharedTerrainInfo;
    private LandEffectEnums landEffectEnums;
    private LandEffectMovementType[] landEffects;

    @Override
    public void clearData() {
        mapCoordsManager.clearData();
        coords = null;
        terrain = null;
        sharedTerrainInfo = null;
    }
    
    public BattleMapTerrain importDisassembly(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, Path terrainEntriesPath, Path battleMapCoordsPath, int battleIndex) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        sharedTerrainInfo = null;
        allCoords = mapCoordsManager.importDisassembly(battleMapCoordsPath);
        this.coords = allCoords[battleIndex];
        EntriesAsmData terrainEntries = entriesAsmProcessor.importAsmData(terrainEntriesPath, null);
        int mapId = coords.getMap();
        mapCoordsManager.importMapFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
        if (battleIndex < terrainEntries.entriesCount()) {
            Path path = PathHelpers.getIncbinPath().resolve(terrainEntries.getPathForEntry(battleIndex));
            terrain = terrainDisassemblyProcessor.importDisassembly(path, null);
            checkForSharedTerrain(terrainEntries, battleIndex);
        }
        Console.logger().info("Terrain data " + battleIndex + " successfully imported from : " + terrainEntriesPath);
        Console.logger().finest("EXITING importDisassembly");
        return terrain;
    }
    
    public LandEffectMovementType[] importLandEffects(Path enumsPath, Path landEffectPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importLandEffects");
        landEffectEnums = landEffectEnumsAsmProcessor.importAsmData(enumsPath, null);
        landEffects = landEffectAsmProcessor.importAsmData(landEffectPath, landEffectEnums);
        Console.logger().info("Land effects data successfully imported from : " + landEffectPath);
        Console.logger().finest("EXITING importLandEffects");
        return landEffects;
    }
    
    public void exportDisassembly(Path battleMapTerrainPath, BattleMapTerrain terrain) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.terrain = terrain;
        terrainDisassemblyProcessor.exportDisassembly(battleMapTerrainPath, terrain, null);
        Console.logger().info("Terrain data successfully exported to : " + battleMapTerrainPath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportLandEffects(Path landEffectPath, LandEffectMovementType[] landEffects) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportLandEffects");
        this.landEffects = landEffects;
        landEffectAsmProcessor.exportAsmData(landEffectPath, landEffects, landEffectEnums);
        Console.logger().info("Land effects successfully exported to : " + landEffectPath);
        Console.logger().finest("EXITING exportLandEffects");
    }
    
    public MapLayout importMap(Path paletteEntriesPath, Path tilesetsEntriesPath, Path mapEntriesPath, int mapId) throws IOException, AsmException, DisassemblyException {
        return mapCoordsManager.importMapFromEntries(paletteEntriesPath, tilesetsEntriesPath, mapEntriesPath, mapId);
    }
    
    private void checkForSharedTerrain(EntriesAsmData terrainEntries, int index) {
        if (terrainEntries.isEntryShared(terrainEntries.getEntryValue(index))) {
            int[] sharedEntries = terrainEntries.getSharedEntries(terrainEntries.getEntryValue(index));
            if (sharedEntries != null && sharedEntries.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < sharedEntries.length; i++) {
                    sb.append("- Battle ");
                    sb.append(sharedEntries[i]);
                    sb.append("\n");
                }
                sharedTerrainInfo = sb.toString();
            }
        }
    }

    public BattleMapTerrain getTerrain() {
        return terrain;
    }

    public MapLayout getMapLayout() {
        return mapCoordsManager.getMapLayout();
    }

    public BattleMapCoords getCoords() {
        return coords;
    }

    public BattleMapCoords[] getAllCoords() {
        return allCoords;
    }

    public String getSharedTerrainInfo() {
        return sharedTerrainInfo;
    }

    public LandEffectEnums getLandEffectEnums() {
        return landEffectEnums;
    }

    public LandEffectMovementType[] getLandEffects() {
        return landEffects;
    }
}
