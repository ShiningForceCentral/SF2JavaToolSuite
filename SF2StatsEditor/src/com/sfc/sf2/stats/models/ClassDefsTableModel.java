/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.stats.ClassDefinition;

/**
 *
 * @author TiMMy
 */
public class ClassDefsTableModel extends AbstractTableModel<ClassDefinition> {

    public ClassDefsTableModel() {
        super(new String[] { "Id", "Name", "MOV", "Resistance", "Move type", "Prowess" }, -1);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return (col == 0 || col == 2) ? Integer.class : String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    protected ClassDefinition createBlankItem(int row) {
        ClassDefinition item = new ClassDefinition();
        item.setIndex(row);
        item.setName("CLASS" + row);
        return item;
    }

    @Override
    protected ClassDefinition cloneItem(ClassDefinition item) {
        return item.clone();
    }

    @Override
    protected Object getValue(ClassDefinition item, int row, int col) {
        return switch (col) {
            case 0 -> item.getIndex();
            case 1 -> item.getName();
            case 2 -> item.getMov();
            case 3 -> item.getResistance();
            case 4 -> item.getMoveType();
            case 5 -> item.getProwess();
            default -> null;
        };
    }

    @Override
    protected ClassDefinition setValue(ClassDefinition item, int row, int col, Object value) {
        switch (col) {
            case 1 -> item.setName((String) value);
            case 2 -> item.setMov(asInt(value));
            case 3 -> item.setResistance((String) value);
            case 4 -> item.setMoveType((String) value);
            case 5 -> item.setProwess((String) value);
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(ClassDefinition item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(ClassDefinition item, int col) {
        return 255;
    }

    private static int asInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString());
    }
}
