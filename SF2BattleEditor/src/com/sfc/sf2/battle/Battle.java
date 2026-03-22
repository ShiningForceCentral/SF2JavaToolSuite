/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author wiz
 */
public class Battle {
    
    private int index;
    private MapLayout layout;
    private BattleMapCoords mapCoords;
    private BattleMapTerrain terrain;
    private BattleSpriteset spriteset;

    public Battle(int index, MapLayout layout, BattleMapCoords mapCoords, BattleMapTerrain terrain, BattleSpriteset spriteset) {
        this.index = index;
        this.layout = layout;
        this.mapCoords = mapCoords;
        this.terrain = terrain;
        this.spriteset = spriteset;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MapLayout getMapLayout() {
        return layout;
    }

    public void setMapLayout(MapLayout layout) {
        this.layout = layout;
    }
    
    public BattleMapCoords getBattleCoords() {
        return mapCoords;
    }

    public void setMapCoords(BattleMapCoords mapCoords) {
        this.mapCoords = mapCoords;
    }

    public BattleMapTerrain getTerrain() {
        return terrain;
    }

    public void setTerrain(BattleMapTerrain terrain) {
        this.terrain = terrain;
    }

    public BattleSpriteset getSpriteset() {
        return spriteset;
    }

    public void setSpriteset(BattleSpriteset spriteset) {
        this.spriteset = spriteset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return this == null;
        if (obj == this) return true;
        if (!(obj instanceof Battle)) return false;
        Battle other = (Battle)obj;
        return this.index == other.index && this.layout.equals(other.layout) && this.mapCoords.equals(other.mapCoords) && this.terrain.equals(other.terrain) && this.spriteset.equals(other.spriteset);
    }
}
