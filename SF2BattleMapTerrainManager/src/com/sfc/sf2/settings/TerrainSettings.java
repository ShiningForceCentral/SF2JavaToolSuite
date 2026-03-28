/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.settings;

import com.sfc.sf2.battle.mapterrain.gui.TerrainKeyPanel.TerrainDrawMode;
import com.sfc.sf2.core.settings.AbstractSettings;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class TerrainSettings implements AbstractSettings {
    
    private TerrainDrawMode terrainDrawMode;

    public TerrainDrawMode getTerrainDrawMode() {
        return terrainDrawMode;
    }

    public void setTerrainDrawMode(TerrainDrawMode terrainDrawMode) {
        this.terrainDrawMode = terrainDrawMode;
    }

    @Override
    public void initialiseNewUser() {
        terrainDrawMode = TerrainDrawMode.Icons;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("terrainDrawMode")) {
            try {
                terrainDrawMode = TerrainDrawMode.valueOf((String)data.get("terrainDrawMode"));
            } catch (Exception e) {
                initialiseNewUser();
            }
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("terrainDrawMode", terrainDrawMode.toString());
    }
}
