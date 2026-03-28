/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties.io;

import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.dialog.properties.DialogProperty;
import com.sfc.sf2.dialog.properties.DialogPropertiesEnums;
import com.sfc.sf2.helpers.BinaryHelpers;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class DialogPropertiesDisassemblyProcessor extends AbstractDisassemblyProcessor<DialogProperty[], DialogPropertiesEnums> {

    @Override
    protected DialogProperty[] parseDisassemblyData(byte[] data, DialogPropertiesEnums pckg) throws DisassemblyException {
        ArrayList<DialogProperty> entriesList = new ArrayList<>();
        int cursor = 0;
        while ((cursor+4) < data.length && BinaryHelpers.getWord(data, cursor) != -1) {
            int spriteId = BinaryHelpers.getByte(data, cursor) & 0xFF;
            int portraitId = BinaryHelpers.getByte(data, cursor + 1) & 0xFF;
            int sfxId = BinaryHelpers.getByte(data, cursor + 2) & 0xFF;
            String spriteName = DialogPropertiesEnums.toEnumString(spriteId, pckg.getMapSprites());
            String portraitName = DialogPropertiesEnums.toEnumString(portraitId, pckg.getPortraits());
            String sfxName = DialogPropertiesEnums.toEnumString(sfxId, pckg.getSfx());
            
            entriesList.add(new DialogProperty(spriteName, portraitName, sfxName));
            cursor += 4;
        }
        DialogProperty[] entries = new DialogProperty[entriesList.size()];
        entries = entriesList.toArray(entries);
        return entries;
    }

    @Override
    protected byte[] packageDisassemblyData(DialogProperty[] item, DialogPropertiesEnums pckg) throws DisassemblyException {
        byte[] propertiesFileBytes = new byte[item.length*4+2];
        for (int i = 0; i < item.length; i++) {
            propertiesFileBytes[i*4+0] = DialogPropertiesEnums.toEnumByte(item[i].getSpriteName(), pckg.getMapSprites());
            propertiesFileBytes[i*4+1] = DialogPropertiesEnums.toEnumByte(item[i].getPortraitName(), pckg.getPortraits());
            propertiesFileBytes[i*4+2] = DialogPropertiesEnums.toEnumByte(item[i].getSfxName(), pckg.getSfx());
        }
        propertiesFileBytes[item.length * 4] = -1;
        propertiesFileBytes[item.length * 4 + 1] = -1;
        return propertiesFileBytes;
    }
}
