/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.stats.ItemDefinition;

/**
 *
 * @author TiMMy
 */
public class ItemDefsTableModel extends AbstractTableModel<ItemDefinition> {

    public ItemDefsTableModel() {
        super(new String[] {
            "Id", "Name", "Equip flags", "Range min", "Range max", "Price", "Type", "Use spell",
            "Effect 1", "Param 1", "Effect 2", "Param 2", "Effect 3", "Param 3"
        }, -1);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return switch (col) {
            case 0, 3, 4, 5, 9, 11, 13 -> Integer.class;
            default -> String.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    protected ItemDefinition createBlankItem(int row) {
        ItemDefinition item = new ItemDefinition();
        item.setIndex(row);
        item.setName("Item " + row);
        return item;
    }

    @Override
    protected ItemDefinition cloneItem(ItemDefinition item) {
        return item.clone();
    }

    @Override
    protected Object getValue(ItemDefinition item, int row, int col) {
        return switch (col) {
            case 0 -> item.getIndex();
            case 1 -> item.getName();
            case 2 -> item.getEquipFlags();
            case 3 -> item.getRangeMin();
            case 4 -> item.getRangeMax();
            case 5 -> item.getPrice();
            case 6 -> item.getItemType();
            case 7 -> item.getUseSpell();
            case 8 -> item.getEffect1();
            case 9 -> item.getEffectParam1();
            case 10 -> item.getEffect2();
            case 11 -> item.getEffectParam2();
            case 12 -> item.getEffect3();
            case 13 -> item.getEffectParam3();
            default -> null;
        };
    }

    @Override
    protected ItemDefinition setValue(ItemDefinition item, int row, int col, Object value) {
        switch (col) {
            case 1 -> item.setName((String) value);
            case 2 -> item.setEquipFlags((String) value);
            case 3 -> item.setRangeMin(asInt(value));
            case 4 -> item.setRangeMax(asInt(value));
            case 5 -> item.setPrice(asInt(value));
            case 6 -> item.setItemType((String) value);
            case 7 -> item.setUseSpell((String) value);
            case 8 -> item.setEffect1((String) value);
            case 9 -> item.setEffectParam1(asInt(value));
            case 10 -> item.setEffect2((String) value);
            case 11 -> item.setEffectParam2(asInt(value));
            case 12 -> item.setEffect3((String) value);
            case 13 -> item.setEffectParam3(asInt(value));
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(ItemDefinition item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(ItemDefinition item, int col) {
        return col == 5 ? 65535 : 255;
    }

    private static int asInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString());
    }
}
