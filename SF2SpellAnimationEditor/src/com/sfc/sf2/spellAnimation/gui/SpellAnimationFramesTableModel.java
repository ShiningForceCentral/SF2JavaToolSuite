/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation.gui;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.spellAnimation.SpellAnimationFrame;

/**
 *
 * @author TiMMy
 */
public class SpellAnimationFramesTableModel extends AbstractTableModel<SpellAnimationFrame> {

    public SpellAnimationFramesTableModel() {
        super(new String[] { "Index", "Tile Index", "X", "Y", "Tiles Width", "Tiles Height", "Foreground" }, 255);
    }

    @Override
    public Class getColumnType(int col) {
        switch (col) {
            case 0: return Integer.class;
            case 4:
            case 5: return Byte.class;
            case 6: return Boolean.class;
            default: return Short.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    protected SpellAnimationFrame createBlankItem(int row) {
        return SpellAnimationFrame.createEmpty();
    }

    @Override
    protected SpellAnimationFrame cloneItem(SpellAnimationFrame item) {
        return item.clone();
    }

    @Override
    protected Object getValue(SpellAnimationFrame item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getTileIndex();
            case 2: return item.getX();
            case 3: return item.getY();
            case 4: return item.getW();
            case 5: return item.getH();
            case 6: return item.getForeground();
        }
        return 0;
    }

    @Override
    protected SpellAnimationFrame setValue(SpellAnimationFrame item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setTileIndex((short)value); break;
            case 2: item.setX((short)value); break;
            case 3: item.setY((short)value); break;
            case 4: item.setW((byte)value); break;
            case 5: item.setH((byte)value); break;
            case 6: item.setForeground((boolean)value); break;
        }
        return item;
    }

    @Override
    protected Comparable getMinLimit(SpellAnimationFrame item, int col) {
        return 0;
    }

    @Override
    protected Comparable getMaxLimit(SpellAnimationFrame item, int col) {
        switch (col) {
            case 4:
            case 5: return Byte.MAX_VALUE;
            case 6: return 1;
        }
        return Short.MAX_VALUE;
    }
}
