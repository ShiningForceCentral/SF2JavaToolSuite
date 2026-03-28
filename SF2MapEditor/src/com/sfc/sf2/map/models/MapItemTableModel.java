/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.MapEnums;
import com.sfc.sf2.map.MapItem;
import com.sfc.sf2.map.layout.MapLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author TiMMy
 */
public class MapItemTableModel extends AbstractTableModel<MapItem> {
    
    private DefaultComboBoxModel itemsModel;
    
    public MapItemTableModel() {
        super(new String[] { "Index", "X", "Y", "Flag", "Flag Info", "Item", "Comment" }, 64);
    }
 
    public void setEnums(MapEnums enums) {
        itemsModel = new DefaultComboBoxModel(enums.getItems().keySet().toArray());
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col >= 4 ? String.class : Integer.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 4 ? false : super.isCellEditable(row, column);
    }

    @Override
    protected MapItem createBlankItem(int row) {
        return MapItem.createEmpty();
    }

    @Override
    protected MapItem cloneItem(MapItem item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapItem item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getX();
            case 2: return item.getY();
            case 3: return item.getFlag();
            case 4: return item.getFlagInfo();
            case 5: return item.getItem();
            case 6: return item.getComment();
        }
        return -1;
    }

    @Override
    protected MapItem setValue(MapItem item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setX((int)value); break;
            case 2: item.setY((int)value); break;
            case 3: item.setFlag((int)value); setValueAt(item.getFlagInfo(), row, 4); break;
            case 5: item.setItem((String)value); break;
            case 6: item.setComment((String)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapItem item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapItem item, int col) {
        switch (col) {
            case 1:
            case 2:
                return MapLayout.BLOCK_WIDTH-1;
        }
        return Integer.MAX_VALUE;
    }
    
    @Override
    public ComboBoxModel getComboBoxModel(int row, int col) {
        return col == 5 ? itemsModel : null;
    }
}
