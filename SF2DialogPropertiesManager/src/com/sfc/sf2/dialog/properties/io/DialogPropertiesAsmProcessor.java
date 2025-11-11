/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties.io;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.dialog.properties.DialogProperty;
import com.sfc.sf2.dialog.properties.DialogPropertiesEnums;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class DialogPropertiesAsmProcessor extends AbstractAsmProcessor<DialogProperty[], DialogPropertiesEnums> {

    private boolean importedFileIsStandard = false;
    private int importedFileElisFixIndex = -1;
    
    @Override
    protected DialogProperty[] parseAsmData(BufferedReader reader, DialogPropertiesEnums pckg) throws IOException, AsmException {
        ArrayList<DialogProperty> entriesList = new ArrayList<>();
        String line;
        importedFileElisFixIndex = -1;
        String mapSprite;
        String portrait;
        String sfx;
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == ';') continue;
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("mapsprite")) {
                String value = line.substring(line.indexOf(' ')).trim();
                if (value.contains("$") || value.matches("[0-9]+")) {
                    int val = StringHelpers.getValueInt(value);
                    mapSprite = DialogPropertiesEnums.toEnumString(val, pckg.getMapSprites());
                } else {
                    mapSprite = value;
                }
                
                line = StringHelpers.trimAndRemoveComments(reader.readLine());
                if (!line.startsWith("portrait")) {
                    throw new AsmException("Dialog Properties asm file formatted incorrectly. Could not find \"portrait\" entry for : " + mapSprite);
                }
                value = line.substring(line.indexOf(' ')).trim();
                if (value.contains("$") || value.matches("[0-9]+")) {
                    int val = StringHelpers.getValueInt(value);
                    portrait = DialogPropertiesEnums.toEnumString(val, pckg.getPortraits());
                } else {
                    portrait = value;
                }
                
                line = StringHelpers.trimAndRemoveComments(reader.readLine());
                if (line.startsWith("if (")) {
                    importedFileElisFixIndex = entriesList.size();
                    line = StringHelpers.trimAndRemoveComments(reader.readLine());
                    reader.readLine();
                    reader.readLine();
                    reader.readLine();
                    Console.logger().warning("Elis fix found at item: " + importedFileElisFixIndex + ". Fix will be reapplied on export.");
                }
                if (!line.startsWith("speechSfx")) {
                    throw new AsmException("Dialog Properties asm file formatted incorrectly. Could not find \"speechSfx\" entry for : " + mapSprite);
                }
                value = line.substring(line.indexOf(' ')).trim();
                if (value.contains("$") || value.matches("[0-9]+")) {
                    int val = StringHelpers.getValueInt(value);
                    sfx = DialogPropertiesEnums.toEnumString(val, pckg.getSfx());
                } else {
                    sfx = value;
                }

                entriesList.add(new DialogProperty(mapSprite, portrait, sfx));
                mapSprite = portrait = sfx = null;
            } else if (line.startsWith("tableEnd")) {
                importedFileIsStandard = !line.endsWith(".w");
            }
        }
        DialogProperty[] entries = new DialogProperty[entriesList.size()];
        entries = entriesList.toArray(entries);
        return entries;
    }

    @Override
    protected String getHeaderName(DialogProperty[] item, DialogPropertiesEnums pckg) {
        return "Sprite dialog properties";
    }

    @Override
    protected void packageAsmData(FileWriter writer, DialogProperty[] item, DialogPropertiesEnums pckg) throws IOException, AsmException {
        writer.write("table_MapspriteDialogueProperties:\n\n");
        
        writer.write("; Syntax\t\tmapsprite [MAPSPRITE_]enum (or index)\n");
        writer.write(";\t\t\t\tportrait  [PORTRAIT_]enum (or index)\n");
        writer.write(";\t\t\t\tspeechSfx [SFX_]enum (or index)\n\n");
        
        for (int i=0; i < item.length; i++) {
            writer.append(String.format("\t\t\t\tmapsprite %s\n", item[i].getSpriteName()));
            writer.append(String.format("\t\t\t\tportrait  %s\n", item[i].getPortraitName()));
            if (i == importedFileElisFixIndex) {
                //Elis fix
                Console.logger().warning("Elis fix being reapplied to item: " + importedFileElisFixIndex);
                writer.append("\t\t\tif (FIX_ELIS_SPEECH_SFX=1)\n");
                writer.append(String.format("\t\t\t\tspeechSfx %s\n", item[i].getSfxName()));
                writer.append("\t\t\telse\n");
                writer.append("\t\t\t\tspeechSfx DIALOG_BLEEP_6\n");
                writer.append("\t\t\tendif\n\n");
            } else {
                writer.append(String.format("\t\t\t\tspeechSfx %s\n\n", item[i].getSfxName()));
            }
        }
        if (importedFileIsStandard) {
            writer.append("\t\t\t\ttableEnd\n");
        } else {
            writer.append("\t\t\t\ttableEnd.w\n");
        }
    }
}
