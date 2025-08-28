/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models;

/**
 *
 * @author TiMMy
 */
public class StringTableModel extends AbstractTableModel<String> {

    public StringTableModel() {
        super(new String[] { "Index", "String", "Length" }, -1);
    }
    
    @Override
    public Class<?> getColumnType(int col) {
        switch (col) {
            case 0: return String.class;
            default: return Integer.class;
        }
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1;
    }

    @Override
    protected String createBlankItem(int row) {
        return "New String : " + row;
    }

    @Override
    protected String cloneItem(String item) {
        return new String(item);
    }

    @Override
    protected Object getValue(String item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item;
            case 2: return item.length();
        }
        return null;
    }

    @Override
    protected String setValue(String item, int col, Object value) {
        switch (col) {
            case 1: return (String)value;
        }
        return null;
    }
}
