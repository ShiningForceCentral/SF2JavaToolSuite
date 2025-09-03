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
    
    private int itemsPerRow;
    
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

    public int getItemsPerRow() {
        return itemsPerRow;
    }

    public void setItemsPerRow(int itemsPerRow) {
        this.itemsPerRow = itemsPerRow;
    }
    
    @Override
    public void initialiseNewUser() {
        exportMode = IconExportMode.INDIVIDUAL_FILES;
        exportFileFormat = exportFileFormat.PNG;
        itemsPerRow = 10;
    }

    @Override
    public void decodeSettings(HashMap<String, String> data) {
        try {
            if (data.containsKey("exportMode")) {
                exportMode = IconExportMode.valueOf(data.get("exportMode"));
            }
            if (data.containsKey("exportFileFormat")) {
                exportFileFormat = FileFormat.valueOf(data.get("exportFileFormat"));
            }
        } catch (Exception e) {
            initialiseNewUser();
        }
        if (data.containsKey("itemsPerRow")) {
            itemsPerRow = Integer.parseInt(data.get("itemsPerRow"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("exportMode", exportMode.toString());
        data.put("exportFileFormat", exportFileFormat.toString());
        data.put("itemsPerRow", Integer.toString(itemsPerRow));
    }
}
