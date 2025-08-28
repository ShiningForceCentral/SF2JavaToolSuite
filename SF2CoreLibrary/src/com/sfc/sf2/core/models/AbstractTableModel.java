/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models;

import com.sfc.sf2.core.gui.controls.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractTableModel<T> extends javax.swing.table.AbstractTableModel {

    protected String[] columns;
    
    private List<T> tableItems = new ArrayList();
    private int rowLimit;
    
    /**
     *
     * @param rowLimit set to -1 for no limit
     */
    public AbstractTableModel(String[] columns, int rowLimit) {
        super();
        this.columns = columns;
        this.rowLimit = rowLimit;
    }

    public T[] getTableData(Class<T[]> type) {
        T[] data = type.cast(Array.newInstance(type.getComponentType(), tableItems.size()));
        for (int i = 0; i < data.length; i++) {
            data[i] = tableItems.get(i);
        }
        return data;
    }

    public void setTableData(T[] tableData) {
        this.tableItems.clear();
        if (tableData != null) {
            for (int i = 0; i < tableData.length; i++) {
                this.tableItems.add(tableData[i]);
            }
        }
        fireTableDataChanged();
    }
    
    public abstract Class<?> getColumnType(int col);
    protected abstract T createBlankItem(int row);
    protected abstract T cloneItem(T item);
    protected abstract Object getValue(T item, int row, int col);    
    protected abstract T setValue(T item, int col, Object value);
    
    
    public T getRow(int row) {
        if (row < 0 || row >= tableItems.size()) {
            return null;
        }
        return tableItems.get(row);
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        if (row < 0 || row >= tableItems.size() || col < 0 || col >= columns.length) {
            return null;
        }
        return getValue(tableItems.get(row), row, col);
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (row < 0 || row >= tableItems.size() || col < 0 || col >= columns.length) {
            return;
        }
        tableItems.set(row, setValue(tableItems.get(row), col, value));
        fireTableCellUpdated(row, col);
    }
    
    public boolean canAddMoreRows() {
        return rowLimit == -1 || tableItems.size() < rowLimit;
    }
    
    public boolean addRow(int row) {
        if (rowLimit > -1 && tableItems.size() >= rowLimit) {
            Console.logger().warning("WARNING Cannot create new row because already at limit");
            return false;
        }
        if (tableItems.isEmpty() || row < 0 || row >= tableItems.size()) {
            tableItems.add(createBlankItem(tableItems.size()));
            fireTableRowsInserted(0, 0);
            return true;
        } else {
            tableItems.add(row+1, createBlankItem(row+1));
            fireTableRowsInserted(row+1, row+1);
            return true;
        }
    }
    
    public SelectionInterval cloneRows(int start, int end) {
        for (int i = end; i >= start; i--) {
            cloneRow(i, end+1);
        }
        fireTableRowsInserted(start, end);
        int dif = end - start;
        start = end+1;
        end = start+dif;
        if (start >= tableItems.size())
            start = tableItems.size()-1;
        if (end >= tableItems.size())
            end = tableItems.size()-1;
        return new SelectionInterval(start, end);
    }
    
    private void cloneRow(int row, int addOffset) {
        if (rowLimit > -1 && tableItems.size() >= rowLimit) {
            Console.logger().warning("WARNING Cannot clone item because already at limit");
            return;
        }
        T item;
        if (tableItems.isEmpty() || row < 0 || row >= tableItems.size()) {
            item = createBlankItem(row);
        } else {
            item = cloneItem(tableItems.get(row));
        }
        tableItems.add(addOffset, item);
    }
    
    public SelectionInterval removeRows(int start, int end) {
        for (int i = end; i >= start; i--) {
            if (isRowLocked(i)) {
                Console.logger().warning("WARNING Cannot delete item because row " + i + " is locked");
            } else {
                removeRow(i);
            }
        }
        fireTableRowsDeleted(start, end);
        if (tableItems.isEmpty()) {
            return new SelectionInterval(-1, -1);
        } else {
            int selection = start;
            if (selection >= tableItems.size())
                selection = tableItems.size()-1;
            return new SelectionInterval(selection, selection);
        }
    }
    
    private void removeRow(int row) {
        if (!tableItems.isEmpty()) {
            if (row >= 0) {
                tableItems.remove(row);
                fireTableRowsDeleted(row, row);
            } else {
                tableItems.remove(tableItems.size()-1);
            }
        }
    }
    
    public SelectionInterval shiftUp(int start, int end) {
        if (start <= 0) {
            Console.logger().warning("WARNING Cannot shift up because selection is already at the top");
            return new SelectionInterval(start, end);
        } else if (isRowLocked(start-1)) {
            Console.logger().warning("WARNING Cannot shift items up row " + (start-1) + " is locked");
            return new SelectionInterval(start, end);
        }
        for (int i = start; i <= end; i++) {
            if (isRowLocked(i)) {
                Console.logger().warning("WARNING Cannot shift items up because row " + i + " is locked");
                return new SelectionInterval(start, end);
            }
        }
        shiftItemsUp(start, end);
        fireTableRowsDeleted(start, end);
        return new SelectionInterval(start-1, end-1);
    }
    
    private void shiftItemsUp(int start, int end) {
        T item = tableItems.remove(start-1);
        tableItems.add(end, item);
    }
    
    public SelectionInterval shiftDown(int start, int end) {
        if (end >= tableItems.size()-1) {
            Console.logger().warning("WARNING Cannot shift down because selection is already at the bottom");
            return new SelectionInterval(start, end);
        } else if (isRowLocked(end+1)) {
            Console.logger().warning("WARNING Cannot shift items up row " + (end+1) + " is locked");
            return new SelectionInterval(start, end);
        }
        for (int i = start; i <= end; i++) {
            if (isRowLocked(i)) {
                Console.logger().warning("WARNING Cannot shift items up because row " + i + " is locked");
                return new SelectionInterval(start, end);
            }
        }
        shiftItemsDown(start, end);
        fireTableRowsDeleted(start, end);
        return new SelectionInterval(start+1, end+1);
    }
    
    private void shiftItemsDown(int start, int end) {
        T item = tableItems.remove(end+1);
        tableItems.add(start, item);
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }
 
    public boolean isRowLocked(int row) {
        return false;
    }
    
    @Override
    public int getRowCount() {
        return tableItems.size();
    }
 
    @Override
    public int getColumnCount() {
        return columns.length;
    }
 
    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return getColumnType(columnIndex);
    }
}
