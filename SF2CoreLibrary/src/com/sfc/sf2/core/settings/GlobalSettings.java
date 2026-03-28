/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class GlobalSettings implements AbstractSettings {
    
    private boolean darkTheme;
    private int logLevel;
    
    public boolean getIsDarkTheme() {
        return darkTheme;
    }
    
    public void setIsDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }
    
    public int getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void initialiseNewUser() {
        darkTheme = false;
        logLevel = 1;
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("darkTheme")) {
            darkTheme = Boolean.parseBoolean(data.get("darkTheme"));
        }
        if (data.containsKey("logLevel")) {
            logLevel = Integer.parseInt(data.get("logLevel"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("darkTheme", Boolean.toString(darkTheme));
        data.put("logLevel", Integer.toString(logLevel));
    }
}
