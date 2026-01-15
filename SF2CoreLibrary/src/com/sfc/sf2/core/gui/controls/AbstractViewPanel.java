/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.controls;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.ComboAction;
import com.sfc.sf2.core.actions.SpinnerAction;
import com.sfc.sf2.core.actions.ToggleAction;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;

/**
 *
 * @author TiMMy
 */
public class AbstractViewPanel<T extends AbstractLayoutPanel> extends javax.swing.JPanel {

    protected  T layoutPanel;
    private JComboBox jComboBoxScale;
    private JCheckBox jCheckBoxGrid;
    private JSpinner jSpinnerItemsPerRow;
    private ColorPicker colorPickerBG;
    
    public JComboBox getScaleComboBox() { return jComboBoxScale; }
    public JCheckBox getGridCheckBox() { return jCheckBoxGrid; }
    public JSpinner getItemsPerRowSpinner() { return jSpinnerItemsPerRow; }
    public ColorPicker getBackgroundColorPicker() { return colorPickerBG; }
        
    protected void init(JComboBox jComboBoxScale, JCheckBox jCheckBoxGrid, JSpinner jSpinnerItemsPerRow, ColorPicker colorPickerBG) {
        this.jComboBoxScale = jComboBoxScale;
        this.jCheckBoxGrid = jCheckBoxGrid;
        this.jSpinnerItemsPerRow = jSpinnerItemsPerRow;
        this.colorPickerBG = colorPickerBG;
        
        if (colorPickerBG != null) {
            colorPickerBG.setColor(SettingsManager.getGlobalSettings().getTransparentBGColor());
        }
        if (jComboBoxScale != null) {
            jComboBoxScale.setModel(new DefaultComboBoxModel<>(RenderScaleHelpers.RENDER_SCALE_STRINGS));
            jComboBoxScale.setSelectedIndex(RenderScaleHelpers.DEFAULT_RENDER_SCALE);
        }
    }
    
    public void setLayoutPanel(T layoutPanel) {
        this.layoutPanel = layoutPanel;
        if (jComboBoxScale != null) layoutPanel.setRenderScaleIndex(jComboBoxScale.getSelectedIndex());
        if (jCheckBoxGrid != null) layoutPanel.setShowGrid(jCheckBoxGrid.isSelected());
        if (jSpinnerItemsPerRow != null) layoutPanel.setItemsPerRow((int)jSpinnerItemsPerRow.getValue());
        if (colorPickerBG != null) layoutPanel.setBGColor(colorPickerBG.getColor());
    }

    protected void onItemsPerRowChanged(javax.swing.event.ChangeEvent evt) {
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new SpinnerAction(jSpinnerItemsPerRow, (int)jSpinnerItemsPerRow.getValue(), layoutPanel.getItemsPerRow()));
        }
        if (layoutPanel != null) {
            layoutPanel.setItemsPerRow((int)jSpinnerItemsPerRow.getValue());
        }
    }

    protected void onBGColorChanged(java.awt.event.ActionEvent evt) {
        if (layoutPanel != null) {
            layoutPanel.setBGColor(colorPickerBG.getColor());
        }
        SettingsManager.getGlobalSettings().setTransparentBGColor(colorPickerBG.getColor());
        SettingsManager.saveGlobalSettingsFile();
    }

    protected void onGridChanged(java.awt.event.ItemEvent evt) {
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxGrid, jCheckBoxGrid.isSelected()));
        }
        if (layoutPanel != null) {
            layoutPanel.setShowGrid(jCheckBoxGrid.isSelected());
        }
    }
    
    protected void onScaleChanged(java.awt.event.ActionEvent evt) {
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ComboAction(jComboBoxScale, (int)jComboBoxScale.getSelectedIndex(), layoutPanel.getRenderScaleIndex()));
        }
        if (layoutPanel != null) {
            layoutPanel.setRenderScaleIndex(jComboBoxScale.getSelectedIndex());
        }
    }
}
