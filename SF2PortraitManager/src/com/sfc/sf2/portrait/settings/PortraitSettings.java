/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.portrait.settings;

import com.sfc.sf2.core.settings.AbstractSettings;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class PortraitSettings implements AbstractSettings {

    private int renderScaleIndex;
    
    public int getRenderScaleIndex() {
        return renderScaleIndex;
    }
    
    public void setRenderScaleIndex(int renderScaleIndex) {
        this.renderScaleIndex = renderScaleIndex;
    }
    
    @Override
    public void initialiseNewUser() {
        renderScaleIndex = RenderScaleHelpers.DEFAULT_RENDER_SCALE_INDEX;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("renderScale")) {
            renderScaleIndex = RenderScaleHelpers.stringToIndex(data.get("renderScale"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("renderScale", RenderScaleHelpers.indexToString(renderScaleIndex));
    }    
}
