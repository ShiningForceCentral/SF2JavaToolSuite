/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.actions;

import static com.sfc.sf2.battle.mapcoords.BattleMapCoords.BATTLE_SIZE;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.core.actions.ICumulativeActionData;

/**
 *
 * @author TiMMy
 */
public class TerrainChangeActionData implements ICumulativeActionData<TerrainChangeActionData> {
    
    private final BattleMapTerrain terrainData;
    private final byte terrain;
    private final int index;

    public TerrainChangeActionData(BattleMapTerrain terrainData, byte terrain, int index) {
        this.terrainData = terrainData;
        this.terrain = terrain;
        this.index = index;
    }

    public BattleMapTerrain terrainData() {
        return terrainData;
    }

    public byte terrain() {
        return terrain;
    }

    public int index() {
        return index;
    }

    @Override
    public boolean isInvalidated(TerrainChangeActionData other) {
        return this.terrainData.equals(other.terrainData) && this.terrain == other.terrain && this.index == other.index;
    }

    @Override
    public boolean canBeCombined(TerrainChangeActionData other) {
        return this.terrainData.equals(other.terrainData) && this.terrain == other.terrain;
    }

    @Override
    public TerrainChangeActionData combine(TerrainChangeActionData other) {
        return other;
    }
    
    @Override
    public String toCumulativeString(int number) {
        return String.format("Set terrain: %s at %d points", BattleMapTerrain.getTerrainName(terrain), number);
    }

    @Override
    public String toString() {
        return String.format("Set terrain: %s at battle point (%d, %d)", BattleMapTerrain.getTerrainName(terrain), index%BATTLE_SIZE, index/BATTLE_SIZE);
    }
}
