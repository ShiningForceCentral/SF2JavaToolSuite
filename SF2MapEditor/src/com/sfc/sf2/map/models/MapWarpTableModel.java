/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.helpers.Direction;
import com.sfc.sf2.helpers.NumHelpers;
import com.sfc.sf2.map.MapEnums;
import com.sfc.sf2.map.MapWarpEvent;
import com.sfc.sf2.map.layout.MapLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
/**
 *
 * @author TiMMy
 */
public class MapWarpTableModel extends AbstractTableModel<MapWarpEvent> {
    
    private DefaultComboBoxModel directionsModel;
    private DefaultComboBoxModel mapsModel;
    
    public MapWarpTableModel() {
        super(new String[] { "Index", "Trigger X", "Trigger Y", "Scroll Dir.", "Dest Map", "Dest X", "Dest Y", "Facing", "Comment" }, 64);
    }
 
    public void setEnums(MapEnums enums) {
        directionsModel = new DefaultComboBoxModel(Direction.values());
        mapsModel = new DefaultComboBoxModel(enums.getMaps().keySet().toArray());
    }

    @Override
    public Class<?> getColumnType(int col) {
        switch (col) {
            case 3:
            case 4:
            case 7:
            case 8:
                return String.class;
            default:
                return Integer.class;
        }
    }

    @Override
    protected MapWarpEvent createBlankItem(int row) {
        return MapWarpEvent.createEmpty();
    }

    @Override
    protected MapWarpEvent cloneItem(MapWarpEvent item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapWarpEvent item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getTriggerX();
            case 2: return item.getTriggerY();
            case 3: return item.getScrollDirection();
            case 4: return item.getDestMap();
            case 5: return item.getDestX();
            case 6: return item.getDestY();
            case 7: return item.getFacing();
            case 8: return item.getComment();
        }
        return null;
    }

    @Override
    protected MapWarpEvent setValue(MapWarpEvent item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setTriggerX(NumHelpers.getValueWithValidGap((int)value, item.getTriggerX(), MapLayout.BLOCK_WIDTH-1, 0xFF)); break;
            case 2: item.setTriggerY(NumHelpers.getValueWithValidGap((int)value, item.getTriggerY(), MapLayout.BLOCK_WIDTH-1, 0xFF)); break;
            case 3: item.setScrollDirection((Direction)value); break;
            case 4: item.setDestMap((String)value); break;
            case 5: item.setDestX((int)value); break;
            case 6: item.setDestY((int)value); break;
            case 7: item.setFacing((Direction)value); break;
            case 8: item.setComment((String)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapWarpEvent item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapWarpEvent item, int col) {
        return col <= 2 ? 0xFF : MapLayout.BLOCK_WIDTH-1;
    }
    
    @Override
    public ComboBoxModel getComboBoxModel(int row, int col) {
        switch (col) {
            case 4: return mapsModel;
            case 3:
            case 7: return directionsModel;
            default: return null;
        }
    }
}
