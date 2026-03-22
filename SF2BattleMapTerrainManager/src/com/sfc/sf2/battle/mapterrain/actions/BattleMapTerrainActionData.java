/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.actions;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.LandEffectMovementType;
import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class BattleMapTerrainActionData implements IActionData<BattleMapTerrainActionData> {
    
    private final int battleIndex;
    private final MapLayout layout;
    private final BattleMapCoords coords;
    private final BattleMapTerrain terrain;
    private final LandEffectMovementType[] landEffects;
    private final String sharedTerraininfo;

    public BattleMapTerrainActionData(int battleIndex, MapLayout layout, BattleMapCoords coords, BattleMapTerrain terrain, LandEffectMovementType[] landEffects, String sharedTerraininfo) {
        this.battleIndex = battleIndex;
        this.layout = layout;
        this.coords = coords;
        this.terrain = terrain;
        this.landEffects = landEffects;
        this.sharedTerraininfo = sharedTerraininfo;
    }

    public int battleIndex() {
        return battleIndex;
    }

    public MapLayout layout() {
        return layout;
    }

    public BattleMapCoords coords() {
        return coords;
    }

    public BattleMapTerrain terrain() {
        return terrain;
    }

    public LandEffectMovementType[] landEffects() {
        return landEffects;
    }

    public String sharedTerraininfo() {
        return sharedTerraininfo;
    }

    @Override
    public boolean isInvalidated(BattleMapTerrainActionData other) {
        if (!this.layout.equals(other.layout) || !this.coords.equals(other.coords) || !this.terrain.equals(other.terrain)) return false;
        if (this.landEffects.length != other.landEffects.length) return false;
        for (int i = 0; i < landEffects.length; i++) {
            if (!this.landEffects[i].equals(other.landEffects[i])) return false;
        }
        return true;
    }

    @Override
    public boolean canBeCombined(BattleMapTerrainActionData other) {
        return this.layout.equals(other.layout) && this.coords.equals(other.coords) && this.terrain.equals(other.terrain);
    }

    @Override
    public BattleMapTerrainActionData combine(BattleMapTerrainActionData other) {
        return other;
    }
    
    @Override
    public String toString() {
        if (layout == null || coords == null) {
            return "NULL";
        } else {
            return String.format("Battle %d map: %d. Land Effects: %d", battleIndex, coords.getMapIndex(), landEffects.length);
        }
    }
}
