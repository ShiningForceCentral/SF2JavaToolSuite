/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.models;

import com.sfc.sf2.battle.mapterrain.LandEffect;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author TiMMy
 */
public class LandEffectTableEditor extends AbstractCellEditor implements TableCellEditor {
    
    private final LandEffectCellEditor landEffectEditor = new LandEffectCellEditor();
    
    public void setDefenses(String[] defenses) {
        landEffectEditor.getComboBox().setModel(new DefaultComboBoxModel<>(defenses));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        landEffectEditor.setLandEffect((LandEffect)value);
        return landEffectEditor;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return landEffectEditor.getLandEffect();
    }
    
    @Override
    public boolean stopCellEditing() {
        try {
            landEffectEditor.getSpinner().commitEdit();
        } catch (ParseException ex) { }
        return super.stopCellEditing();
    }
    
    @Override
    public void addCellEditorListener(CellEditorListener l) {
        super.addCellEditorListener(l);
        landEffectEditor.addPropertyChangeListener(this::dataChanged);
    }
    
    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
        landEffectEditor.addPropertyChangeListener(this::dataChanged);
    }
    
    private void dataChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == "dataChange") {
            fireEditingStopped();
        }
    }
}
