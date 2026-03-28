/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapEnums;
import com.sfc.sf2.map.layout.MapLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author TiMMy
 */
public class MapAreaTableModel extends AbstractTableModel<MapArea> {
    
    private DefaultComboBoxModel musicModel;
    
    public MapAreaTableModel() {
        super(new String[] { "Index", "L1 X", "L1 Y", "L1 X'", "L1 Y'", "L2 FX", "L2 FY", "L2 BX", "L2 BY",
            "L1 PX", "L1 PY", "L2 PX", "L2 PY", "L1 SX", "L1 SY", "L2 SX", "L2 SY", "L1 Type", "Music" }, 64);
    }
 
    public void setEnums(MapEnums enums) {
        musicModel = new DefaultComboBoxModel(enums.getMusic().keySet().toArray());
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col == 18 ? String.class : Integer.class;
    }

    @Override
    protected MapArea createBlankItem(int row) {
        return MapArea.createEmpty();
    }

    @Override
    protected MapArea cloneItem(MapArea item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapArea item, int row, int col) {
        switch (col) {
            case  0: return row;
            case  1: return item.getLayer1StartX();
            case  2: return item.getLayer1StartY();
            case  3: return item.getLayer1EndX();
            case  4: return item.getLayer1EndY();
            case  5: return item.getForegroundLayer2StartX();
            case  6: return item.getForegroundLayer2StartY();
            case  7: return item.getBackgroundLayer2StartX();
            case  8: return item.getBackgroundLayer2StartY();
            case  9: return item.getLayer1ParallaxX();
            case 10: return item.getLayer1ParallaxY();
            case 11: return item.getLayer2ParallaxX();
            case 12: return item.getLayer2ParallaxY();
            case 13: return item.getLayer1AutoscrollX();
            case 14: return item.getLayer1AutoscrollY();
            case 15: return item.getLayer2AutoscrollX();
            case 16: return item.getLayer2AutoscrollY();
            case 17: return item.getLayerType();
            case 18: return item.getDefaultMusic();
        }
        return -1;
    }

    @Override
    protected MapArea setValue(MapArea item, int row, int col, Object value) {
        switch (col) {
            case  1: item.setLayer1StartX((int)value); break;
            case  2: item.setLayer1StartY((int)value); break;
            case  3: item.setLayer1EndX((int)value); break;
            case  4: item.setLayer1EndY((int)value); break;
            case  5: item.setForegroundLayer2StartX((int)value); break;
            case  6: item.setForegroundLayer2StartY((int)value); break;
            case  7: item.setBackgroundLayer2StartX((int)value); break;
            case  8: item.setBackgroundLayer2StartY((int)value); break;
            case  9: item.setLayer1ParallaxX((int)value); break;
            case 10: item.setLayer1ParallaxY((int)value); break;
            case 11: item.setLayer2ParallaxX((int)value); break;
            case 12: item.setLayer2ParallaxY((int)value); break;
            case 13: item.setLayer1AutoscrollX((int)value); break;
            case 14: item.setLayer1AutoscrollY((int)value); break;
            case 15: item.setLayer2AutoscrollX((int)value); break;
            case 16: item.setLayer2AutoscrollY((int)value); break;
            case 17: item.setLayerType((int)value); break;
            case 18: item.setDefaultMusic((String)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapArea item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapArea item, int col) {
        switch (col) {
            case  9:
            case 10:
            case 11:
            case 12:
                return 0x100;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                return 0xFF;
            default:
                return MapLayout.BLOCK_WIDTH-1;
        }
    }
    
    @Override
    public ComboBoxModel getComboBoxModel(int row, int col) {
        return col == 18 ? musicModel : null;
    }
}
