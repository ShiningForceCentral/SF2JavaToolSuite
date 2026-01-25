/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.controls;

import com.sfc.sf2.core.INameable;
import java.beans.BeanProperty;
import javax.swing.ButtonGroup;

/**
 *
 * @author TiMMy
 */
public class NameableButtonGroup extends ButtonGroup implements INameable {
    private String name;

    @Override
    public String getName() {
        return name;
    }

    
    @BeanProperty(preferred = true, visualUpdate = false, description = "The name of the group.")
    public void setName(String name) {
        this.name = name;
    }
}
