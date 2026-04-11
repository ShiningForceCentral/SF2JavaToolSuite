/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.tables;

import com.sfc.sf2.core.gui.controls.Table;
import com.sfc.sf2.core.models.combobox.ComboBoxTableEditor;
import com.sfc.sf2.core.models.combobox.ComboBoxTableRenderer;

/**
 *
 * @author TiMMy
 */
public class EnemyPropertiesTable extends Table {
    
    public EnemyPropertiesTable() {
        super();
        jTable.setDefaultEditor(String.class, new ComboBoxTableEditor());
        jTable.setDefaultRenderer(String.class, new ComboBoxTableRenderer());
    }
}
