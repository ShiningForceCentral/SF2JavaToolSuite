/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.combobox;

import com.sfc.sf2.core.models.StringTableModel;
import javax.swing.AbstractListModel;

/**
 *
 * @author TiMMy
 */
public class ComboTableModel extends StringTableModel {

    AbstractListModel<Object> model;
    
    public void setComboModel(AbstractListModel<Object> model) {
        this.model = model;
    }

    @Override
    protected String createBlankItem(int row) {
        return model.getElementAt(0).toString();
    }
}
