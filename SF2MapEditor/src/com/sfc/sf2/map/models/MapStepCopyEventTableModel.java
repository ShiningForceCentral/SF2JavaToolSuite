/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.MapCopyEvent;
import com.sfc.sf2.map.layout.MapLayout;

/**
 *
 * @author TiMMy
 */
public class MapStepCopyEventTableModel extends AbstractTableModel<MapCopyEvent> {
    
    public MapStepCopyEventTableModel() {
        super(new String[] { "Index", "Trigger X", "Trigger Y", "Source X", "Source Y", "Source X'", "Source Y'", "Dest X", "Dest Y", "Comment" }, 64);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col == 9 ? String.class : Integer.class;
    }

    @Override
    protected MapCopyEvent createBlankItem(int row) {
        return MapCopyEvent.createEmpty();
    }

    @Override
    protected MapCopyEvent cloneItem(MapCopyEvent item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapCopyEvent item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getTriggerX();
            case 2: return item.getTriggerY();
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
    protected MapCopyEvent setValue(MapCopyEvent item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setTriggerX((int)value); break;
            case 2: item.setTriggerY((int)value); break;
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
    protected Comparable<?> getMinLimit(MapCopyEvent item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapCopyEvent item, int col) {
        return MapLayout.BLOCK_WIDTH-1;
    }
}
