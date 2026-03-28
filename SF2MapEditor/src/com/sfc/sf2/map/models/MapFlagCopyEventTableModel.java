/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.MapFlagCopyEvent;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author wiz
 */
public class MapFlagCopyEventTableModel extends AbstractTableModel<MapFlagCopyEvent> {
    
    public MapFlagCopyEventTableModel() {
        super(new String[] { "Index", "Flag", "Flag Info", "Source X", "Source Y", "Source X'", "Source Y'", "Dest X", "Dest Y", "Comment" }, 64);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col == 2 || col == 9 ? String.class : Integer.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2 ? false : super.isCellEditable(row, column);
    }

    @Override
    protected MapFlagCopyEvent createBlankItem(int row) {
        return MapFlagCopyEvent.createEmpty();
    }

    @Override
    protected MapFlagCopyEvent cloneItem(MapFlagCopyEvent item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapFlagCopyEvent item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getFlag();
            case 2: return item.getFlagComment();
            case 3: return item.getSourceStartX();
            case 4: return item.getSourceStartY();
            case 5: return item.getSourceEndX();
            case 6: return item.getSourceEndY();
            case 7: return item.getDestStartX();
            case 8: return item.getDestStartY();
            case 9: return item.getComment();
        }
        return -1;
    }

    @Override
    protected MapFlagCopyEvent setValue(MapFlagCopyEvent item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setFlag((int)value); setValueAt(item.getFlagComment(), row, 2); break;
            case 3: item.setSourceStartX((int)value); break;
            case 4: item.setSourceStartY((int)value); break;
            case 5: item.setSourceEndX((int)value); break;
            case 6: item.setSourceEndY((int)value); break;
            case 7: item.SetDestStartX((int)value); break;
            case 8: item.setDestStartY((int)value); break;
            case 9: item.setComment((String)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapFlagCopyEvent item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapFlagCopyEvent item, int col) {
        switch (col) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return MapLayout.BLOCK_WIDTH-1;
        }
        return Integer.MAX_VALUE;
    }
}
