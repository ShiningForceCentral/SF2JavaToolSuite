/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.stats.SpellDefinition;

/**
 *
 * @author TiMMy
 */
public class SpellDefsTableModel extends AbstractTableModel<SpellDefinition> {

    public SpellDefsTableModel() {
        super(new String[] { "Entry", "MP", "Animation", "Properties", "Range min", "Range max", "Radius", "Power" }, -1);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return switch (col) {
            case 1, 4, 5, 6, 7 -> Integer.class;
            default -> String.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    protected SpellDefinition createBlankItem(int row) {
        return new SpellDefinition();
    }

    @Override
    protected SpellDefinition cloneItem(SpellDefinition item) {
        return item.clone();
    }

    @Override
    protected Object getValue(SpellDefinition item, int row, int col) {
        return switch (col) {
            case 0 -> item.getEntry();
            case 1 -> item.getMpCost();
            case 2 -> item.getAnimation();
            case 3 -> item.getProperties();
            case 4 -> item.getRangeMin();
            case 5 -> item.getRangeMax();
            case 6 -> item.getRadius();
            case 7 -> item.getPower();
            default -> null;
        };
    }

    @Override
    protected SpellDefinition setValue(SpellDefinition item, int row, int col, Object value) {
        switch (col) {
            case 0 -> item.setEntry((String) value);
            case 1 -> item.setMpCost(asInt(value));
            case 2 -> item.setAnimation((String) value);
            case 3 -> item.setProperties((String) value);
            case 4 -> item.setRangeMin(asInt(value));
            case 5 -> item.setRangeMax(asInt(value));
            case 6 -> item.setRadius(asInt(value));
            case 7 -> item.setPower(asInt(value));
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(SpellDefinition item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(SpellDefinition item, int col) {
        return 255;
    }

    private static int asInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString());
    }
}
