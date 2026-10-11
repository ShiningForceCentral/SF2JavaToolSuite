/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.spinner;

import com.sfc.sf2.core.models.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author TiMMy
 */
public class SpinnerTableEditor extends AbstractCellEditor implements TableCellEditor, MouseListener {
            
    private final JSpinner spinner = new JSpinner();
    private AbstractTableModel activeModel;
    private int activeRow;
    private int activeColumn;
    private boolean spinnerButtonHeld;
    private boolean dataSilentlyChanged;
    
    @Override public void mouseClicked(MouseEvent e) { spinnerButtonHeld = false; }
    @Override public void mousePressed(MouseEvent e) { setSpinnerHeld(true); }
    @Override public void mouseReleased(MouseEvent e) { setSpinnerHeld(false); }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { setSpinnerHeld(false); }
    
    public SpinnerTableEditor(AbstractSpinnerModel model) {
        super();
        spinner.setModel(model);
        spinner.setUI(new LeftRightSpinnerUI());
        
        for (Component comp : spinner.getComponents()) {
            if (comp instanceof JButton) {
                String name = comp.getName();
                
                if ("Spinner.nextButton".equals(name)) {
                    comp.addMouseListener(this);
                } else if ("Spinner.previousButton".equals(name)) {
                    comp.addMouseListener(this);
                }
            }
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        AbstractTableModel model = (AbstractTableModel)table.getModel();
        SpinnerNumberModel spinnerModel = (SpinnerNumberModel)spinner.getModel();
        if (model != null && spinnerModel != null) {
            spinnerModel.setMinimum(model.getMinValue(row, column));
            spinnerModel.setMaximum(model.getMaxValue(row, column));
            spinnerModel.setStepSize(model.getSpinnerStep(column));
        }
        activeModel = model;
        activeRow = row;
        activeColumn = column;
        spinner.setValue(value);
        return spinner;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }
    
    @Override
    public boolean stopCellEditing() {
        try {
            spinner.commitEdit();
        } catch (ParseException ex) { }
        return super.stopCellEditing();
    }
    
    @Override
    public void addCellEditorListener(CellEditorListener l) {
        super.addCellEditorListener(l);
        spinner.addChangeListener(this::spinnerStateChanged);
    }
    
    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
        spinner.removeChangeListener(this::spinnerStateChanged);
    }
    
    private void setSpinnerHeld(boolean held) {
        if (held) {
            if (!spinnerButtonHeld) {
                spinnerButtonHeld = true;
            }
        } else {
            if (spinnerButtonHeld) {
                spinnerButtonHeld = false;
                spinnerStateChanged(null);
            }
        }
    }
    
    private void spinnerStateChanged(ChangeEvent evt) {
        if (spinnerButtonHeld) {
            dataSilentlyChanged = true;
            activeModel.silentlySetValueAt(spinner.getValue(), activeRow, activeColumn);
            activeModel.fireTableCellUpdated(activeRow, activeColumn);
        } else if (dataSilentlyChanged){
            dataSilentlyChanged = false;
            activeModel.setValueAt(spinner.getValue(), activeRow, activeColumn);
        } else {
            fireEditingStopped();
        }
    }
}
