/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.models;

import com.sfc.sf2.battle.AIPoint;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author wiz
 */
public class AIPointPropertiesTableModel extends AbstractTableModel<AIPoint> {
        
    public AIPointPropertiesTableModel() {
        super(new String[] {"Id", "X", "Y"} , 64);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return Integer.class;
    }

    @Override
    protected AIPoint createBlankItem(int row) {
        return AIPoint.emptyAIPoint();
    }

    @Override
    protected AIPoint cloneItem(AIPoint item) {
        return item.clone();
    }

    @Override
    protected Object getValue(AIPoint item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getX();
            case 2: return item.getY();
            default: return 0;
        }
    }

    @Override
    protected AIPoint setValue(AIPoint item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setX((int)value); break;
            case 2: item.setY((int)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(AIPoint item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(AIPoint item, int col) {
        return 64;
    }
}
