/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.gui;

import com.sfc.sf2.battle.mapterrain.LandEffect;
import com.sfc.sf2.battle.mapterrain.LandEffectEnums;
import com.sfc.sf2.battle.mapterrain.models.LandEffectTableEditor;
import com.sfc.sf2.battle.mapterrain.models.LandEffectTableRenderer;
import com.sfc.sf2.core.gui.controls.Table;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author TiMMy
 */
public class LandEffectTable extends Table {

    private final LandEffectTableEditor landEffectEditor;
    private final LandEffectTableRenderer landEffectRenderer;

    public LandEffectTable() {
        super();
        setButtonsVisible(false);
        setMinColumnWidth(-1, 60);
        jTable.getColumnModel().getColumn(0).setMinWidth(70);
        jTable.setMinimumSize(new Dimension(jTable.getColumnCount()*60, Integer.MAX_VALUE));
        jTable.setRowHeight(70);
        
        landEffectEditor = new LandEffectTableEditor();
        jTable.setDefaultEditor(LandEffect.class, landEffectEditor);
        landEffectRenderer = new LandEffectTableRenderer();
        jTable.setDefaultRenderer(LandEffect.class, landEffectRenderer);
    }
        
    public void setLandEffectData(LandEffectEnums landEffectEnums) {     
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.LEFT);
        jTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        
        if (landEffectEnums != null) {
            String[] defenses = new String[landEffectEnums.getDefenses().size()];
            defenses = landEffectEnums.getDefenses().keySet().toArray(defenses);
            landEffectEditor.setDefenses(defenses);
            landEffectRenderer.setDefenses(defenses);
        }
    }
}
