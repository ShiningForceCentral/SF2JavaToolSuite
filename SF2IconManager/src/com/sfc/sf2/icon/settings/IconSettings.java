/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.sfc.sf2.icon.settings;

import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;
import com.sfc.sf2.core.settings.AbstractSettings;
import com.sfc.sf2.icon.IconManager.IconExportMode;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class IconSettings implements AbstractSettings {

    private IconExportMode exportMode;
    private FileFormat exportFileFormat;
    
    public IconExportMode getExportMode() {
        return exportMode;
    }
    
    public void setExportMode(IconExportMode exportMode) {
        this.exportMode = exportMode;
    }
    
    public FileFormat getExportFileFormat() {
        return exportFileFormat;
    }
    
    public void setExportFileFormat(FileFormat exportFileFormat) {
        this.exportFileFormat = exportFileFormat;
    }
    
    @Override
    public void initialiseNewUser() {
        exportMode = IconExportMode.INDIVIDUAL_FILES;
        exportFileFormat = exportFileFormat.PNG;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("exportMode")) {
            try {
                exportMode = IconExportMode.valueOf((String)data.get("exportMode"));
                exportFileFormat = FileFormat.valueOf((String)data.get("exportFileFormat"));
            } catch (Exception e) {
                initialiseNewUser();
            }
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("exportMode", exportMode.toString());
        data.put("exportFileFormat", exportFileFormat.toString());
    }
}
