/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.dialog.properties.DialogProperty;
import java.util.Arrays;

/**
 *
 * @author TiMMy
 */
public class DialogPropertiesActionData implements IActionData<DialogPropertiesActionData> {
    
    private DialogProperty[] properties;

    public DialogPropertiesActionData(DialogProperty[] properties) {
        this.properties = properties;
    }

    public DialogProperty[] properties() {
        return properties;
    }

    @Override
    public boolean isInvalidated(DialogPropertiesActionData other) {
        return Arrays.equals(this.properties, other.properties);
    }

    @Override
    public boolean canBeCombined(DialogPropertiesActionData other) {
        return isInvalidated(other);
    }

    @Override
    public DialogPropertiesActionData combine(DialogPropertiesActionData other) {
        return other;
    }
    
    @Override
    public String toString() {
        return String.format("Length: %d", properties.length);
    }
}
