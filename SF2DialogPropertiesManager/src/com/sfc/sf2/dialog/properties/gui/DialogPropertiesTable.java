/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties.gui;

import com.sfc.sf2.core.gui.controls.Table;
import com.sfc.sf2.core.models.combobox.ComboBoxTableEditor;
import com.sfc.sf2.core.models.combobox.ComboBoxTableRenderer;
import com.sfc.sf2.core.models.image.BufferedImageTableRenderer;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class DialogPropertiesTable extends Table {
    
    public DialogPropertiesTable() {
        super();        
        ComboBoxTableEditor comboBoxEditor = new ComboBoxTableEditor();
        jTable.setDefaultEditor(String.class, comboBoxEditor);
        ComboBoxTableRenderer comboBoxRenderer = new ComboBoxTableRenderer();
        jTable.setDefaultRenderer(String.class, comboBoxRenderer);
        BufferedImageTableRenderer imageRenderer = new BufferedImageTableRenderer();
        jTable.setDefaultRenderer(BufferedImage.class, imageRenderer);
    }
}
