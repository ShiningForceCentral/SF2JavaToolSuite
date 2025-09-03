/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.background.settings;

import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;
import com.sfc.sf2.core.settings.AbstractSettings;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class BackgroundSettings implements AbstractSettings {

    private FileFormat exportFileFormat;
        
    public FileFormat getExportFileFormat() {
        return exportFileFormat;
    }
    
    public void setExportFileFormat(FileFormat exportFileFormat) {
        this.exportFileFormat = exportFileFormat;
    }
    
    @Override
    public void initialiseNewUser() {
        exportFileFormat = exportFileFormat.PNG;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("exportMode")) {
            try {
                exportFileFormat = FileFormat.valueOf((String)data.get("exportFileFormat"));
            } catch (Exception e) {
                initialiseNewUser();
            }
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("exportFileFormat", exportFileFormat.toString());
    }
}
