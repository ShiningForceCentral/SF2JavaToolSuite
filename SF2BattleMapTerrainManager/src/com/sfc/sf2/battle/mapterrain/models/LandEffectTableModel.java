/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.models;

import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.LandEffect;
import com.sfc.sf2.battle.mapterrain.LandEffectMovementType;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class LandEffectTableModel extends AbstractTableModel<LandEffectMovementType> {

    public LandEffectTableModel() {
        super(getLabels(), 16);
    }
    
    public static String[] getLabels() {
        String[] baseLabels = BattleMapTerrain.TERRAIN_NAMES;
        String[] labels = new String[baseLabels.length+1];
        labels[0] = "Terrain type";
        for (int i = 0; i < baseLabels.length; i++) {
            labels[i+1] = baseLabels[i];
        }
        return labels;
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col == 0 ? Integer.class : LandEffect.class;
    }

    @Override
    protected LandEffectMovementType createBlankItem(int row) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected LandEffectMovementType cloneItem(LandEffectMovementType item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected Object getValue(LandEffectMovementType item, int row, int col) {
        switch (col) {
            case 0: return item.getMovementType();
            default: return item.getLandEffects()[col-1];
        }
    }

    @Override
    protected LandEffectMovementType setValue(LandEffectMovementType item, int row, int col, Object value) {
        if (col > 0) {
            item.getLandEffects()[col-1] = (LandEffect)value;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(LandEffectMovementType item, int col) {
        return Integer.MIN_VALUE;
    }

    @Override
    protected Comparable<?> getMaxLimit(LandEffectMovementType item, int col) {
        return Integer.MAX_VALUE;
    }
}
