/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.stats.ShopInventory;

/**
 *
 * @author TiMMy
 */
public class ShopInventoriesTableModel extends AbstractTableModel<ShopInventory> {

    public ShopInventoriesTableModel() {
        super(new String[] { "Id", "Name", "Items" }, -1);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return col == 0 ? Integer.class : String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    protected ShopInventory createBlankItem(int row) {
        ShopInventory item = new ShopInventory();
        item.setIndex(row);
        item.setName("Shop " + (row + 1));
        return item;
    }

    @Override
    protected ShopInventory cloneItem(ShopInventory item) {
        return item.clone();
    }

    @Override
    protected Object getValue(ShopInventory item, int row, int col) {
        return switch (col) {
            case 0 -> item.getIndex();
            case 1 -> item.getName();
            case 2 -> item.getItems();
            default -> null;
        };
    }

    @Override
    protected ShopInventory setValue(ShopInventory item, int row, int col, Object value) {
        switch (col) {
            case 1 -> item.setName((String) value);
            case 2 -> item.setItems((String) value);
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(ShopInventory item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(ShopInventory item, int col) {
        return Integer.MAX_VALUE;
    }
}
