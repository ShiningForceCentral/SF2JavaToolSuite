/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.actions;

import com.sfc.sf2.battle.Battle;
import com.sfc.sf2.battle.EnemyData;
import com.sfc.sf2.battle.EnemyEnums;
import com.sfc.sf2.battle.mapterrain.LandEffectEnums;
import com.sfc.sf2.battle.mapterrain.LandEffectMovementType;
import com.sfc.sf2.core.actions.IActionData;

/**
 *
 * @author TiMMy
 */
public class BattleActionData implements IActionData<BattleActionData> {
    
    private final Battle battle;
    private final String sharedTerrainInfo;
    private final LandEffectEnums landEffectEnums;
    private final LandEffectMovementType[] landEffects;
    private final EnemyData[] enemyData;
    private final EnemyEnums enemyEnums;

    public BattleActionData(Battle battle, String sharedTerrainInfo, LandEffectEnums landEffectEnums, LandEffectMovementType[] landEffects, EnemyData[] enemyData, EnemyEnums enemyEnums) {
        this.battle = battle;
        this.sharedTerrainInfo = sharedTerrainInfo;
        this.landEffectEnums = landEffectEnums;
        this.landEffects = landEffects;
        this.enemyData = enemyData;
        this.enemyEnums = enemyEnums;
    }

    public Battle battle() {
        return battle;
    }

    public String sharedTerrainInfo() {
        return sharedTerrainInfo;
    }

    public LandEffectEnums landEffectEnums() {
        return landEffectEnums;
    }

    public LandEffectMovementType[] landEffects() {
        return landEffects;
    }

    public EnemyData[] enemyData() {
        return enemyData;
    }

    public EnemyEnums enemyEnums() {
        return enemyEnums;
    }

    @Override
    public boolean isInvalidated(BattleActionData other) {
        if (!this.battle.equals(other.battle) || !this.landEffectEnums.equals(other.landEffectEnums) || !this.enemyEnums.equals(other.enemyEnums)) return false;
        if (this.landEffects.length != other.landEffects.length) return false;
        for (int i = 0; i < landEffects.length; i++) {
            if (!this.landEffects[i].equals(other.landEffects[i])) return false;
        }
        if (this.enemyData.length != other.enemyData.length) return false;
        for (int i = 0; i < enemyData.length; i++) {
            if (!this.enemyData[i].equals(other.enemyData[i])) return false;
        }
        return true;
    }

    @Override
    public boolean canBeCombined(BattleActionData other) {
        return isInvalidated(other);
    }

    @Override
    public BattleActionData combine(BattleActionData other) {
        return other;
    }
    
    @Override
    public String toString() {
        if (battle == null) {
            return "NULL";
        } else {
            return String.format("Battle: %d. Land Effects: %d. Enemy Data: %d", battle.getIndex(), landEffects.length, enemyData.length);
        }
    }
}
