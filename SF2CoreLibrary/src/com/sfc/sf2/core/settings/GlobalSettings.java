/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import com.sfc.sf2.helpers.ColorHelpers;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class GlobalSettings implements AbstractSettings {
    
    private boolean darkTheme;
    private Color transparentBGColor;
    
    public boolean getIsDarkTheme() {
        return darkTheme;
    }
    
    public void setIsDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }
    
    public Color getTransparentBGColor() {
        return transparentBGColor;
    }
    
    public void setTransparentBGColor(Color transparentBGColor) {
        this.transparentBGColor = transparentBGColor;
    }

    @Override
    public void initialiseNewUser() {
        darkTheme = false;
        transparentBGColor = new Color(200, 0, 200);
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("darkTheme")) {
            darkTheme = Boolean.parseBoolean(data.get("darkTheme"));
        }
        if (data.containsKey("transparentBGColor")) {
            transparentBGColor = ColorHelpers.parseColorString(data.get("transparentBGColor"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("darkTheme", Boolean.toString(darkTheme));
        data.put("transparentBGColor", ColorHelpers.toHexString(transparentBGColor));
    }
}
