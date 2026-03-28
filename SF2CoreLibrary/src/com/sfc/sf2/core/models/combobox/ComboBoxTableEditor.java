/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.combobox;

import com.sfc.sf2.core.models.AbstractTableModel;
import java.awt.Component;
import java.awt.Dimension;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author TiMMy
 */
public class ComboBoxTableEditor extends AbstractCellEditor implements TableCellEditor, PopupMenuListener {

    private final JComboBox comboBox = new JComboBox();

    public ComboBoxTableEditor(ComboBoxModel model) {
        this();
        setData(model);
    }
    
    public ComboBoxTableEditor() {
        super();
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 28));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
    }
    
    public void setData(ComboBoxModel model) {
        comboBox.setModel(model);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (table.getModel() instanceof AbstractTableModel) {
            ComboBoxModel model = ((AbstractTableModel)table.getModel()).getComboBoxModel(row, column);
            if (model != null) {
                model.setSelectedItem(value);
                comboBox.setModel(model);
            }
        }
        comboBox.setSelectedItem(value);
        return comboBox;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }
    
    @Override
    public boolean stopCellEditing() {
        comboBox.setPopupVisible(false);
        return super.stopCellEditing();
    }
    
    @Override
    public void addCellEditorListener(CellEditorListener l) {
        super.addCellEditorListener(l);
        comboBox.addPopupMenuListener(this);
    }
    
    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
        comboBox.removePopupMenuListener(this);
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        fireEditingStopped();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        fireEditingCanceled();
    }
}
