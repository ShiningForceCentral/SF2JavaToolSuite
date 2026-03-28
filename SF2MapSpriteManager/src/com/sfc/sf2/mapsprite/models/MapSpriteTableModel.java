/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.mapsprite.MapSprite;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class MapSpriteTableModel extends AbstractTableModel<MapSprite> {

    public MapSpriteTableModel() {
        super(new String[] { "Id", "Mapsprite" }, -1);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col == 0 ? String.class : BufferedImage.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    protected MapSprite createBlankItem(int row) {
        return null;
    }

    @Override
    protected MapSprite cloneItem(MapSprite item) {
        return null;
    }

    @Override
    protected Object getValue(MapSprite item, int row, int col) {
        switch (col) {
            case 0: return String.format("%03d-%d", item.getIndex(), item.getFacingIndex());
            case 1: return item.getIndexedColorImage(2);
            default: return null;
        }
    }

    @Override
    protected MapSprite setValue(MapSprite item, int row, int col, Object value) {
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapSprite item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapSprite item, int col) {
        return Integer.MAX_VALUE;
    }
    
}
