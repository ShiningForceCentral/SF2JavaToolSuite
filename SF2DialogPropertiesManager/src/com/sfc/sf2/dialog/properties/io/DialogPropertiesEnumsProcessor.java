/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties.io;

import com.sfc.sf2.core.io.asm.SF2EnumsAsmProcessor;
import com.sfc.sf2.dialog.properties.DialogPropertiesEnums;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class DialogPropertiesEnumsProcessor extends SF2EnumsAsmProcessor<DialogPropertiesEnums> {

    public DialogPropertiesEnumsProcessor() {
        super(new String[] { "Mapsprites", "Portraits", "Sfx" });
    }

    @Override
    protected void parseLine(int categoryIndex, String line, LinkedHashMap<String, Integer> asmData) {
        defaultParseLine(categoryIndex, line, asmData);
    }

    @Override
    protected DialogPropertiesEnums createEnumsData(LinkedHashMap<String, Integer>[] dataSets) {
        return new DialogPropertiesEnums(dataSets[0], dataSets[1], dataSets[2]);
    }
}
