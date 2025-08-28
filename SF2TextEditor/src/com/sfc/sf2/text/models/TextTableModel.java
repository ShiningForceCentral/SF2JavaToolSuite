/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.models;

import com.sfc.sf2.core.models.AbstractTableModel;


/**
 *
 * @author TiMMy
 */
public class TextTableModel extends AbstractTableModel<String> {

    public TextTableModel() {
        super(new String[] { "Id", "Hex Id", "Line" }, -1);
    }
    
    @Override
    public Class<?> getColumnType(int col) {
        return String.class;
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2;
    }
 
    @Override
    public boolean isRowLocked(int row) {
        return row == 0;
    }

    @Override
    protected String createBlankItem(int row) {
        return "Line " + row;
    }

    @Override
    protected String cloneItem(String item) {
        return new String(item);
    }

    @Override
    protected Object getValue(String item, int row, int col) {
        switch (col) {
            case 0: return String.format("%04d", row);
            case 1: return String.format("$%04X", row);
            case 2: return item;
        }
        return null;
    }

    @Override
    protected String setValue(String item, int col, Object value) {
        switch (col) {
            case 2: return (String)value;
            default: break;
        }
        return item;
    }
    
}
