/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import com.sfc.sf2.helpers.PathHelpers;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class CoreSettings implements AbstractSettings {

    private boolean prioritiseLocalPath;
    private String localBasePath;
    private String localIncbinPath;
    
    private String basePath;
    private String incbinPath;
    
    
    public boolean areLocalPathsValid() {
        return prioritiseLocalPath && localBasePath != null && localBasePath.length() > 0;
    }
    
    public boolean arePathsValid() {
        if (areLocalPathsValid()) {
            return true;
        } else {
            return basePath != null && basePath.length() > 0;
        }
    }
        
    public String getActiveBasePath() {
        if (prioritiseLocalPath && localBasePath != null) {
            return localBasePath;
        } else {
            return basePath;
        }
    }
    
    public String getActiveIncbinPath() {
        if (prioritiseLocalPath && localIncbinPath != null) {
            return localIncbinPath;
        } else {
            return incbinPath;
        }
    }

    public boolean isPrioritiseLocalPath() {
        return prioritiseLocalPath;
    }

    public void setPrioritiseLocalPath(boolean prioritiseLocalPath) {
        this.prioritiseLocalPath = prioritiseLocalPath;
    }

    public String getBasePath() {
        return basePath;
    }
    
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getIncbinPath() {
        return incbinPath;
    }
    
    public void setIncbinPath(String incbinPath) {
        this.incbinPath = incbinPath;
    }

    @Override
    public void initialiseNewUser() {
        prioritiseLocalPath = true;
        String appPath = PathHelpers.getApplicationpath().toString();
        int incbinIndex = appPath.indexOf("\\disasm\\data");
        if (incbinIndex >= 0) {    //In SF2DISASM
            localIncbinPath = incbinPath = appPath.substring(0, incbinIndex+8);
            localBasePath = basePath = appPath;
        } else {    //A dev build?
            localBasePath = localIncbinPath = basePath = incbinPath = null;
        }
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("prioritiseLocalPath")) {
            prioritiseLocalPath = Boolean.parseBoolean(data.get("prioritiseLocalPath"));
        }
        if (data.containsKey("basePath")) {
            basePath = data.get("basePath");
        }
        if (data.containsKey("incbinPath")) {
            incbinPath = data.get("incbinPath");
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("prioritiseLocalPath", Boolean.toString(prioritiseLocalPath));
        if (basePath != null) {
            data.put("basePath", basePath);
            data.put("incbinPath", incbinPath);
        }
    }
}
