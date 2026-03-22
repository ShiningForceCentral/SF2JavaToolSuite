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

    private MapLayout layout;
    private BattleMapCoords[] allCoords;
    private BattleMapCoords coords;
    private BattleMapTerrain terrain;
    private String sharedTerrainInfo;
    private LandEffectEnums landEffectEnums;
    private LandEffectMovementType[] landEffects;

    @Override
    public void clearData() {
        if (layout != null) {
            layout.clearIndexedColorImage(true);
            layout = null;
        }
        allCoords = null;
        coords = null;
        terrain = null;
        sharedTerrainInfo = null;
        landEffectEnums = null;
        landEffects = null;
    }
    
    public BattleMapTerrain importDisassembly(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, Path terrainEntriesPath, Path battleMapCoordsPath, int battleIndex) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        sharedTerrainInfo = null;
        BattleMapCoordsManager mapCoordsManager = new BattleMapCoordsManager();
        allCoords = mapCoordsManager.importDisassembly(battleMapCoordsPath);
        this.coords = allCoords[battleIndex];
        EntriesAsmData terrainEntries = new EntriesAsmProcessor().importAsmData(terrainEntriesPath, null);
        int mapId = coords.getMapIndex();
        layout = mapCoordsManager.importMapFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
        if (battleIndex < terrainEntries.entriesCount()) {
            Path path = PathHelpers.getIncbinPath().resolve(terrainEntries.getPathForEntry(battleIndex));
            terrain = new BattleTerrainDisassemblyProcessor().importDisassembly(path, null);
            checkForSharedTerrain(terrainEntries, battleIndex);
        }
        Console.logger().info("Terrain data " + battleIndex + " successfully imported from : " + terrainEntriesPath);
        Console.logger().finest("EXITING importDisassembly");
        return terrain;
    }
    
    public LandEffectMovementType[] importLandEffects(Path enumsPath, Path landEffectPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importLandEffects");
        landEffectEnums = new LandEffectEnumsAsmProcessor().importAsmData(enumsPath, null);
        landEffects = new LandEffectAsmProcessor().importAsmData(landEffectPath, landEffectEnums);
        Console.logger().info("Land effects data successfully imported from : " + landEffectPath);
        Console.logger().finest("EXITING importLandEffects");
        return landEffects;
    }
    
    public void exportDisassembly(Path battleMapTerrainPath, BattleMapTerrain terrain) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.terrain = terrain;
        new BattleTerrainDisassemblyProcessor().exportDisassembly(battleMapTerrainPath, terrain, null);
        Console.logger().info("Terrain data successfully exported to : " + battleMapTerrainPath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void exportLandEffects(Path landEffectPath, LandEffectMovementType[] landEffects) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportLandEffects");
        this.landEffects = landEffects;
        new LandEffectAsmProcessor().exportAsmData(landEffectPath, landEffects, landEffectEnums);
        Console.logger().info("Land effects successfully exported to : " + landEffectPath);
        Console.logger().finest("EXITING exportLandEffects");
    }
    
    public MapLayout importMap(Path paletteEntriesPath, Path tilesetsEntriesPath, Path mapEntriesPath, int mapId) throws IOException, AsmException, DisassemblyException {
        return new BattleMapCoordsManager().importMapFromEntries(paletteEntriesPath, tilesetsEntriesPath, mapEntriesPath, mapId);
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
        return layout;
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
