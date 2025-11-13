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
import javax.swing.ComboBoxModel;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractTableModel<T> extends javax.swing.table.AbstractTableModel {

    private String[] columns;
    
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
    protected abstract T setValue(T item, int row, int col, Object value);
    protected abstract Comparable<?> getMinLimit(T item, int col);
    protected abstract Comparable<?> getMaxLimit(T item, int col);
    
    public Number getSpinnerStep(int col) {
        return 1;
    }
    
    public Comparable<?> getMinValue(int row, int col) {
        if (col < 0 || col >= columns.length) {
            return null;
        }
        return getMinLimit(tableItems.get(row), col);
    }
    
    public Comparable<?> getMaxValue(int row, int col) {
        if (col < 0 || col >= columns.length) {
            return null;
        }
        return getMaxLimit(tableItems.get(row), col);
    }
    
    public ComboBoxModel getComboBoxModel(int row, int col) {
        return null;
    }
    
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
        tableItems.set(row, setValue(tableItems.get(row), row, col, value));
        fireTableCellUpdated(row, col);
    }
    
    public boolean canAddMoreRows() {
        return rowLimit == -1 || tableItems.size() < rowLimit;
    }
    
    public void validateAddRows(SelectionInterval[] selection) {
        int totalToAdd = 0;
        for (int i = 0; i < selection.length; i++) {
            int start = selection[i].start(), end = selection[i].end(), count = selection[i].count();
            if (start < 0) {
                start = 0;
            }
            int offset = (tableItems.size()+totalToAdd+count)-rowLimit;
            if (offset > 0) {
                end -= offset;
                count = end-start+1;
                Console.logger().warning("WARNING Table limit reached, can only add " + (end-start+1) + " items");
                if (end < start) {
                    start = end = -1;   //Cannot add any
                    count = 0;
                }
            } else {
                offset = 0;
            }
            if (start >= 0 && end >= 0) {
                totalToAdd += end-start+1;
            }
            if (end != selection[i].end()) {
                if (count > 0 && selection[i].data() != null) {
                    Object[] data = new Object[count];
                    System.arraycopy(selection[i].data(), 0, data, 0, count);
                    selection[i] = new SelectionInterval(start, data, offset);
                } else {
                    selection[i] = new SelectionInterval(start, end, offset);
                }
            }
        }
    }
    
    public SelectionInterval addRows(SelectionInterval selection) {
        int start = selection.start(), end = selection.end(), offset = selection.offset(), count = selection.count();
        Object[] data = selection.data();
        for (int i = 0; i < count; i++) {
            addRow(end+offset+i+1, (data == null || data[i] == null) ? null : (T)data[i]);
        }
        fireTableRowsInserted(start, end);
        
        int dif = end-start;
        start = end+1+offset;
        end = start+dif;
        if (start >= tableItems.size())
            start = tableItems.size()-1;
        if (end >= tableItems.size())
            end = tableItems.size()-1;
        return new SelectionInterval(start, end, 0);
    }
    
    private void addRow(int row, T item) {
        if (rowLimit > -1 && tableItems.size() >= rowLimit) {
            Console.logger().warning("WARNING Cannot create new row because already at limit");
            return;
        }
        if (item == null) {
            item = createBlankItem(tableItems.size());
        }
        if (tableItems.isEmpty() || row < 0 || row >= tableItems.size()) {
            tableItems.add(item);
        } else {
            tableItems.add(row, item);
        }
    }
    
    public void validateRemoveRows(SelectionInterval[] selection) {
        int totalToRemove = 0;
        for (int i = 0; i < selection.length; i++) {
            int start = selection[i].start(), end = selection[i].end();
            if (end < start) {
                int temp = start;
                start = end;
                end = temp;
            }
            if (start < 0) {
                start = 0;
            }
            int offset = (tableItems.size()-totalToRemove-(end-start+1));
            if (offset < 0) {
                end -= offset;
                Console.logger().warning("WARNING Table limit reached, can only remove " + (end-start+1) + " items");
                if (end < start) {
                    start = end = -1;   //Cannot add any
                }
            } else {
                offset = 0;
            }
            if (start >= 0 && end >= 0) {
                totalToRemove += end-start+1;
            }
            selection[i] = new SelectionInterval(start, end, offset);
        }
    }
    
    public SelectionInterval removeRows(SelectionInterval selection) {
        int start = selection.start(), end = selection.end();
        for (int i = end; i >= start; i--) {
            if (isRowLocked(i)) {
                Console.logger().warning("WARNING Cannot delete item because row " + i + " is locked");
            } else {
                removeRow(i);
            }
        }
        fireTableRowsDeleted(start, end);
        if (tableItems.isEmpty()) {
            return new SelectionInterval(-1, -1, 0);
        } else {
            int selected = start;
            if (selected >= tableItems.size())
                selected = tableItems.size()-1;
            return new SelectionInterval(selected, selected, 0);
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
            return new SelectionInterval(start, end, 0);
        } else if (isRowLocked(start-1)) {
            Console.logger().warning("WARNING Cannot shift items up row " + (start-1) + " is locked");
            return new SelectionInterval(start, end, 0);
        }
        for (int i = start; i <= end; i++) {
            if (isRowLocked(i)) {
                Console.logger().warning("WARNING Cannot shift items up because row " + i + " is locked");
                return new SelectionInterval(start, end, 0);
            }
        }
        shiftItemsUp(start, end);
        fireTableRowsDeleted(start, end);
        return new SelectionInterval(start-1, end-1, 0);
    }
    
    private void shiftItemsUp(int start, int end) {
        T item = tableItems.remove(start-1);
        tableItems.add(end, item);
    }
    
    public SelectionInterval shiftDown(int start, int end) {
        if (end >= tableItems.size()-1) {
            Console.logger().warning("WARNING Cannot shift down because selection is already at the bottom");
            return new SelectionInterval(start, end, 0);
        } else if (isRowLocked(end+1)) {
            Console.logger().warning("WARNING Cannot shift items up row " + (end+1) + " is locked");
            return new SelectionInterval(start, end, 0);
        }
        for (int i = start; i <= end; i++) {
            if (isRowLocked(i)) {
                Console.logger().warning("WARNING Cannot shift items up because row " + i + " is locked");
                return new SelectionInterval(start, end, 0);
            }
        }
        shiftItemsDown(start, end);
        fireTableRowsDeleted(start, end);
        return new SelectionInterval(start+1, end+1, 0);
    }
    
    private void shiftItemsDown(int start, int end) {
        T item = tableItems.remove(end+1);
        tableItems.add(start, item);
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
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
