/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain;

/**
 *
 * @author wiz
 */
public class BattleMapTerrain {
    
    public static String TERRAIN_OBTRUCTED = "Obstructed";
    public static String[] TERRAIN_NAMES = new String[] { "Sky/Wall", "Plains", "Path", "Overgrowth", "Forest", "Hills", "Desert", "Mountain", "Water", "Add1", "Add2", "Add3", "Add4", "Add5", "Add6", "Add7" };
    
    private byte[] data;

    public BattleMapTerrain(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
    public static String getTerrainName(int index) {
        return getTerrainName((byte)index);
    }
    
    public static String getTerrainName(byte index) {
        if (index == -1) {
            return TERRAIN_OBTRUCTED;
        } else {
            return TERRAIN_NAMES[index];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return this == null;
        if (obj == this) return true;
        if (!(obj instanceof BattleMapTerrain)) return false;
        BattleMapTerrain other = (BattleMapTerrain)obj;
        if (this.data.length != other.data.length) return false;
        for (int i=0; i < this.data.length; i++) {
            if (this.data[i] != other.data[i]) {
                return false;
            }
        }
        return true;
    }
}
