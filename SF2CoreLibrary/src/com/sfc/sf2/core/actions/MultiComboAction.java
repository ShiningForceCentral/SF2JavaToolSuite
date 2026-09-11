/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.gui.controls.MultiComboBox;

/**
 *
 * @author TiMMy
 */
public class MultiComboAction extends BasicAction<String[]> {

    private MultiComboBox comboBox;
    
    public MultiComboAction(MultiComboBox comboBox, String[] newIndex, String[] oldIndex) {
        super(comboBox, "MultiCombo Value", null, newIndex, oldIndex);
        this.comboBox = comboBox;
    }
    
    @Override
    public void execute() {
        if (!comboBox.isEnabled()) return;
        comboBox.setSelectedItem(newValue);
        comboBox.requestFocus();
        super.execute();
    }

    @Override
    public void undo() {
        if (!comboBox.isEnabled()) return;
        comboBox.setSelectedItem(oldValue);
        comboBox.requestFocus();
        super.undo();
    }

    @Override
    protected String dataToString(String[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);
            if (i < data.length-1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
