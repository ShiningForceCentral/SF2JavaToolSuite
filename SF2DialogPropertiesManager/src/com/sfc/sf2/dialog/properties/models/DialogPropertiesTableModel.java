/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.dialog.properties.DialogProperty;
import com.sfc.sf2.dialog.properties.DialogPropertiesEnums;
import java.awt.image.BufferedImage;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author wiz
 */
public class DialogPropertiesTableModel extends AbstractTableModel<DialogProperty> {
    
    private DialogPropertiesEnums enums;
    private DefaultComboBoxModel mapSpritesModel;
    private DefaultComboBoxModel portraitsModel;
    private DefaultComboBoxModel sfxModel;
    
    private boolean mapSpritesEditable;

    public DialogPropertiesTableModel() {
        super(new String[] { "Id", "Sprite", "Sprite", "Portrait", "Portrait", "SFX" }, 255);
    }

    public DialogPropertiesEnums getEnums() {
        return enums;
    }
 
    public void setEnums(DialogPropertiesEnums enums) {
        this.enums = enums;
        mapSpritesModel = new DefaultComboBoxModel(enums.getMapSprites().keySet().toArray());
        portraitsModel = new DefaultComboBoxModel(enums.getPortraits().keySet().toArray());
        sfxModel = new DefaultComboBoxModel(enums.getSfx().keySet().toArray());
    }

    public void setMapSpritesEditable(boolean mapSpritesEditable) {
        this.mapSpritesEditable = mapSpritesEditable;
    }

    @Override
    public Class getColumnType(int col) {
        return (col == 2 || col == 4) ? BufferedImage.class : col == 0 ? Integer.class : String.class;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            return mapSpritesEditable;
        } else {
            return !(columnIndex == 0 || columnIndex == 2 || columnIndex == 4);
        }
    }

    @Override
    protected DialogProperty createBlankItem(int row) {
        return DialogProperty.emptyDialogPropertiesEntry();
    }

    @Override
    protected DialogProperty cloneItem(DialogProperty item) {
        return item.clone();
    }

    @Override
    protected Object getValue(DialogProperty item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getSpriteName();
            case 2: return enums.getMapSpriteFor(item.getSpriteName());
            case 3: return item.getPortraitName();
            case 4: return enums.getPortraitFor(item.getPortraitName());
            case 5: return item.getSfxName();
            default: return null;
        }
    }

    @Override
    protected DialogProperty setValue(DialogProperty item, int row, int col, Object value) {
        switch (col) {
            case 1: item.setSpriteName((String)value); break;
            case 3: item.setPortraitName((String)value); break;
            case 5: item.setSfxName((String)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(DialogProperty item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(DialogProperty item, int col) {
        switch (col) {
            case 1: return enums.getMapSprites().size()-1;
            case 3: return enums.getPortraits().size()-1;
            case 5: return enums.getSfx().size()-1;
            default: return Integer.MAX_VALUE;
        }
    }
    
    @Override
    public ComboBoxModel getComboBoxModel(int row, int col) {
        switch (col) {
            case 1: return mapSpritesModel;
            case 3: return portraitsModel;
            case 5: return sfxModel;
            default: return null;
        }
    }
}
