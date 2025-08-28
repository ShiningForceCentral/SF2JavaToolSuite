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
        super(new String[] { "X", "Y", "X'", "Y'" }, 12);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return Integer.class;
    }

    @Override
    protected int[] createBlankItem(int row) {
        return new int[columns.length];
    }

    @Override
    protected int[] cloneItem(int[] item) {
        int[] clone = new int[item.length];
        System.arraycopy(item, 0, clone, 0, clone.length);
        return clone;
    }

    @Override
    protected Object getValue(int[] item, int row, int col) {
        return item[col];
    }

    @Override
    protected int[] setValue(int[] item, int col, Object value) {
        item[col] = (int)value;
        return item;
    }
}
