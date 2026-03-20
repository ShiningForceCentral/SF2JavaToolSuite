/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import javax.swing.JComboBox;

/**
 *
 * @author TiMMy
 */
public class ComboAction extends BasicAction<Integer> {

    private JComboBox comboBox;
    
    public ComboAction(JComboBox comboBox, int newIndex, int oldIndex) {
        super(comboBox, "Combo Value", null, newIndex, oldIndex);
        this.comboBox = comboBox;
    }
    
    @Override
    public void execute() {
        comboBox.setSelectedIndex(newValue);
    }

    @Override
    public void undo() {
        comboBox.setSelectedIndex(oldValue);
    }

    @Override
    protected String dataToString(Integer data) {
        Object dataObj = comboBox.getModel().getElementAt(data);
        if (dataObj == null) {
            return super.dataToString(data);
        } else {
            return dataObj.toString();
        }
    }
}
