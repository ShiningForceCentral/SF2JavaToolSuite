/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.portrait.settings;

import com.sfc.sf2.core.settings.AbstractSettings;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class PortraitSettings implements AbstractSettings {

    private int zoom;
    
    public int getZoom() {
        return zoom;
    }
    
    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
    
    @Override
    public void initialiseNewUser() {
        zoom = 2;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("zoom")) {
            zoom = Integer.parseInt(data.get("zoom"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("zoom", Integer.toString(zoom));
    }
    
}
