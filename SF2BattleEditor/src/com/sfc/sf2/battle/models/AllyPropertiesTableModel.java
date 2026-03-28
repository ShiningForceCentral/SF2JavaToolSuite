/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.models;

import com.sfc.sf2.battle.Ally;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class AllyPropertiesTableModel extends AbstractTableModel<Ally> {
        
    public AllyPropertiesTableModel() {
        super(new String[] {"Id", "X", "Y"} , 64);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return Integer.class;
    }

    @Override
    protected Ally createBlankItem(int row) {
        return Ally.emptyAlly();
    }

    @Override
    protected Ally cloneItem(Ally item) {
        return item.clone();
    }

    @Override
    protected Object getValue(Ally item, int row, int col) {
        switch (col) {
            case 0: return row+1;
            case 1: return item.getX();
            case 2: return item.getY();
            default: return 0;
        }
    }

    @Override
    protected Ally setValue(Ally item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setX((int)value); break;
            case 2: item.setY((int)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(Ally item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(Ally item, int col) {
        return 64;
    }
}
