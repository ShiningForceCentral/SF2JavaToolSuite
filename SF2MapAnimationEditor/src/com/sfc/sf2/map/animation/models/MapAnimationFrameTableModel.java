/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.map.animation.MapAnimationFrame;

/**
 *
 * @author TiMMy
 */
public class MapAnimationFrameTableModel extends AbstractTableModel<MapAnimationFrame> {

    public MapAnimationFrameTableModel() {
        super(new String[] { "Index", "Start", "Length", "Dest Tileset", "Dest Index", "Delay" }, 100);
    }

    @Override
    public Class getColumnType(int col) {
        return Integer.class;
    }

    @Override
    protected MapAnimationFrame createBlankItem(int row) {
        return MapAnimationFrame.EmptyMapAnimationFrame();
    }

    @Override
    protected MapAnimationFrame cloneItem(MapAnimationFrame item) {
        return item.clone();
    }

    @Override
    protected Object getValue(MapAnimationFrame item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getStart();
            case 2: return item.getLength();
            case 3: return item.getDestTileset();
            case 4: return item.getDestTileIndex();
            case 5: return item.getDelay();
        }
        return -1;
    }

    @Override
    protected MapAnimationFrame setValue(MapAnimationFrame item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setStart((int)value); break;
            case 2: item.setLength((int)value); break;
            case 3: item.setDestTileset((int)value); break;
            case 4: item.setDestTileIndex((int)value); break;
            case 5: item.setDelay((int)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(MapAnimationFrame item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(MapAnimationFrame item, int col) {
        switch (col) {
            case 2: return 127;
            case 3: return 4;
            case 4: return 127;
            default: return Integer.MAX_VALUE;
        }
    }
}
