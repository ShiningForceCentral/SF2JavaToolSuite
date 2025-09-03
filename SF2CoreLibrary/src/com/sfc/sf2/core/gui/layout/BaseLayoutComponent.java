/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

/**
 * Layout components was a way to extend the LayoutPanels without making simbple panels too complex
 * Layout components are semi-decoupled (i.e. The AbstractLayoutPanel defines the logic of how they are used but any component may be null)
 * @author TiMMy
 */
public abstract class BaseLayoutComponent {
    
    boolean isEnabled = true;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
    
    public static boolean IsEnabled(BaseLayoutComponent component) {
        return component != null && component.isEnabled;
    }
}
