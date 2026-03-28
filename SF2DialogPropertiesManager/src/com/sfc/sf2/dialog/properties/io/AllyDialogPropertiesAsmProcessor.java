/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties.io;

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
public class AllyDialogPropertiesAsmProcessor extends AbstractAsmProcessor<DialogProperty[], DialogPropertiesEnums> {

    @Override
    protected DialogProperty[] parseAsmData(BufferedReader reader, DialogPropertiesEnums pckg) throws IOException, AsmException {
        ArrayList<DialogProperty> entriesList = new ArrayList<>();
        String line;
        String mapSprite;
        String portrait;
        String sfx;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == ';') continue;
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("allyPortraitAndSfx ")) {
                String[] split = line.split("\\s+");
                split[1] = split[1].replace(",", "");
                
                    mapSprite = null;
                    portrait = split[1];
                    sfx = split[2];
                    
                    String mapspriteSuffix = (index % 3 == 0 ? "_BASE" : (index % 3 == 1 ? "_PROMO" : "_SPECIAL"));
                    String mapspritePrefix;
                    if (split[1].contains("_")) {
                        mapspritePrefix = split[1].substring(0, split[1].indexOf('_'));
                    } else {
                        mapspritePrefix = split[1];                            
                    }
                    if (pckg.getMapSprites().containsKey(mapspritePrefix+mapspriteSuffix)) {
                        mapSprite = mapspritePrefix+mapspriteSuffix;
                    } else if (pckg.getMapSprites().containsKey(mapspritePrefix+"_SPECIAL")) {
                        mapSprite = mapspritePrefix+"_SPECIAL";
                    } else if (pckg.getMapSprites().containsKey(mapspritePrefix+"_PROMO")) {
                        mapSprite = mapspritePrefix+"_PROMO";
                    } else if (pckg.getMapSprites().containsKey(mapspritePrefix+"_BASE")) {
                        mapSprite = mapspritePrefix+"_BASE";
                    } else if (portrait.equals("BOWIE_PAINTING")) {
                        mapSprite = "BLANK";    //Fallback if sprites have been changed but "BOWIE_PAINTING" portrait still exists (otherwise it would have found "BOWIE_BASE"
                    } else {
                        throw new AsmException("Cannot find mapsprite for Ally Dialog Properties entry : " + line);
                    }  
                
                entriesList.add(new DialogProperty(mapSprite, portrait, sfx));
                mapSprite = portrait = sfx = null;
                index++;
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
        writer.write("allyPortraitAndSfx: macro\n");
        writer.write("\tdefineShorthand.b PORTRAIT_,\\1\n");
        writer.write("\tdefineShorthand.b SFX_,\\2\n");
        writer.write("\tendm\n\n");
        
        writer.write("; 3 entries per ally for each class type, 2 bytes per entry.\n\n");
        writer.write("table_AllyDialogueProperties:\n\n");
        writer.write("; Syntax\t\tallyPortraitAndSfx [PORTRAIT_]enum (or index), [SFX_]enum (or index)\n");
        writer.write(";\t\t\t\tallyPortraitAndSfx [PORTRAIT_]enum (or index), [SFX_]enum (or index)\n");
        writer.write(";\t\t\t\tallyPortraitAndSfx [PORTRAIT_]enum (or index), [SFX_]enum (or index)\n\n");
        
        int allyIndex;
        int idx;
        for (int i=0; i < item.length; i++) {
            allyIndex = i / 3;
            idx = i % 3;
            if (idx == 0) {
                //First line for ally
                String name = item[i].getPortraitName();
                if (name.equals("BOWIE_PAINTING")) {
                    name = "";
                } else if (name.contains("_")) {
                    name = name.substring(0, name.indexOf("_"));
                }
                if (allyIndex == 30) {
                    //Special check for expanded force
                    writer.write("\t\t\tif (EXPANDED_FORCE_MEMBERS=1)\n");
                }
                writer.write(String.format("; %d: %s\n", allyIndex, name));
            }
            writer.write(String.format("\t\t\t\tallyPortraitAndSfx %s, %s\n", item[i].getPortraitName(), item[i].getSfxName()));
            if (idx == 2) {
                //Last line for ally
                writer.write("\n");
            }
        }
        if (item.length / 3 < 32) {
            for (int i = item.length / 3; i < 32; i++) {
                writer.write(String.format("; %d: \n", i));
                for (int a = 0; a < 3; a++) {
                    writer.write("\t\t\t\tallyPortraitAndSfx BOWIE_PAINTING, DIALOG_BLEEP_5\n");
                }
                writer.write("\n");
            }
        }
        //Close the special check for expanded force
        writer.write("\t\t\tendif\n");
    }
}
