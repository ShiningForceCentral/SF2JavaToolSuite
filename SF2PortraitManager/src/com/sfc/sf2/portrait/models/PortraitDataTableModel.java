/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.portrait.models;

import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class PortraitDataTableModel extends AbstractTableModel<int[]>  {

    public PortraitDataTableModel() {
        super(new String[] { "Row", "X", "Y", "X'", "Y'" }, 12);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return Integer.class;
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    protected int[] createBlankItem(int row) {
        return new int[getColumnCount()];
    }

    @Override
    protected int[] cloneItem(int[] item) {
        int[] clone = new int[item.length];
        System.arraycopy(item, 0, clone, 0, clone.length);
        return clone;
    }

    @Override
    protected Object getValue(int[] item, int row, int col) {
        if (col == 0) {
            return row;
        } else {
            return item[col-1];
        }
    }

    @Override
    protected int[] setValue(int[] item, int col, Object value) {
        if (col > 0) {
            item[col-1] = (int)value;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(int[] item, int col) {
        switch (col) {
            case 3:
                return 6;
            default:
                return 0;
        }
    }

    @Override
    protected Comparable<?> getMaxLimit(int[] item, int col) {
        switch (col) {
            case 1:
                return 5;
            default:
                return 7;
        }
    }
}
