/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.models;

import com.sfc.sf2.battle.mapterrain.LandEffect;
import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author TiMMy
 */
public class LandEffectTableRenderer implements TableCellRenderer {

    private final LandEffectCellEditor landEffectEditor = new LandEffectCellEditor();
    
    public void setDefenses(String[] defenses) {
        landEffectEditor.getComboBox().setModel(new DefaultComboBoxModel<>(defenses));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //if (table.isCellEditable(row, column)) {
        landEffectEditor.setLandEffect((LandEffect)value);
        return landEffectEditor;
    }
}
