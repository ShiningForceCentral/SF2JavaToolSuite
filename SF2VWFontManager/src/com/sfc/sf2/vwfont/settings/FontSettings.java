/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.vwfont.settings;

import com.sfc.sf2.core.io.FileFormat;
import com.sfc.sf2.core.settings.AbstractSettings;
import com.sfc.sf2.helpers.ColorHelpers;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class FontSettings implements AbstractSettings {
    
    private Color transparentBGColor;
    private FileFormat exportFileFormat;
    
    public void setTransparentBGColor(Color transparentBGColor) {
        this.transparentBGColor = transparentBGColor;
    }
    
    public Color getTransparentBGColor() {
        return transparentBGColor;
    }
    
    public FileFormat getExportFileFormat() {
        return exportFileFormat;
    }
    
    public void setExportFileFormat(FileFormat exportFileFormat) {
        this.exportFileFormat = exportFileFormat;
    }

    @Override
    public void initialiseNewUser() {
        transparentBGColor = new Color(200, 200, 255);
        exportFileFormat = exportFileFormat.PNG;
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        try {
            if (data.containsKey("exportFileFormat")) {
                exportFileFormat = FileFormat.valueOf(data.get("exportFileFormat"));
            }
        } catch (Exception e) {
            initialiseNewUser();
        }
        if (data.containsKey("transparentBGColor")) {
            transparentBGColor = ColorHelpers.parseColorString(data.get("transparentBGColor"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("transparentBGColor", ColorHelpers.toHexString(transparentBGColor));
        data.put("exportFileFormat", exportFileFormat.toString());
    }
}
