/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords.models;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author wiz
 */
public class BattleMapCoordsTableModel extends AbstractTableModel<BattleMapCoords> {

    public BattleMapCoordsTableModel() {
        super(new String[] { "Id", "Map", "X", "Y", "Width", "Height", "Trig. X", "Trig. Y" }, 255); //Arbitrary large number
    }
    
    @Override
    public Class<?> getColumnType(int col) {
        return Integer.class;
    }

    @Override
    protected BattleMapCoords createBlankItem(int row) {
        return BattleMapCoords.EmptyBattleMapCoords();
    }

    @Override
    protected BattleMapCoords cloneItem(BattleMapCoords item) {
        return item.clone();
    }

    @Override
    protected Object getValue(BattleMapCoords item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getMapIndex();
            case 2: return item.getX();
            case 3: return item.getY();
            case 4: return item.getWidth();
            case 5: return item.getHeight();
            case 6: return item.getTrigX();
            case 7: return item.getTrigY();
            default: return -1;
        }
    }

    @Override
    protected BattleMapCoords setValue(BattleMapCoords item, int row, int col, Object value) {
        int newVal = (int)value;
        if (col >= 6 && newVal >= MapLayout.BLOCK_WIDTH) {
            //For triggers, valid values are 0-63. 255 is a n/a sentinel
            int oldVal = (int)getValue(item, row, col);
            if (oldVal <= newVal) newVal = 255;
            else if (oldVal > newVal) newVal = MapLayout.BLOCK_WIDTH-1;
        }
        switch (col) {
            case 1: item.setMapIndex(newVal); break;
            case 2: item.setX(newVal); break;
            case 3: item.setY(newVal); break;
            case 4: item.setWidth(newVal); break;
            case 5: item.setHeight(newVal); break;
            case 6: item.setTrigX(newVal); break;
            case 7: item.setTrigY(newVal); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(BattleMapCoords item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(BattleMapCoords item, int col) {
        if (col == 2 || col == 3) {
            return BattleMapCoords.BATTLE_SIZE;
        } else if (col <= 1 || col >= 6) {
            return 255;
        } else {
            return MapLayout.BLOCK_WIDTH-1;
        }
    }
}
