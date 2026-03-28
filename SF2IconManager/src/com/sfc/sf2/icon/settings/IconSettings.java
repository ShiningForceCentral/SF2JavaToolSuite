/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.sfc.sf2.icon.settings;

import com.sfc.sf2.graphics.settings.ExportSettings;
import com.sfc.sf2.icon.IconManager.IconExportMode;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class IconSettings extends ExportSettings {

    private IconExportMode exportMode;
    
    public IconExportMode getExportMode() {
        return exportMode;
    }
    
    public void setExportMode(IconExportMode exportMode) {
        this.exportMode = exportMode;
    }
    
    @Override
    public void initialiseNewUser() {
        super.initialiseNewUser();
        exportMode = IconExportMode.INDIVIDUAL_FILES;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        super.decodeSettings(data);
        try {
            if (data.containsKey("exportMode")) {
                exportMode = IconExportMode.valueOf(data.get("exportMode"));
            }
        } catch (Exception e) { }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        super.encodeSettings(data);
        data.put("exportMode", exportMode.toString());
    }
}
