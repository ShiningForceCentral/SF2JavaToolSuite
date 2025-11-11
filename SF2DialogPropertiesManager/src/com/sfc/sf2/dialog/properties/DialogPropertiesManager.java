/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.FileFormat;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.dialog.properties.io.AllyDialogPropertiesAsmProcessor;
import com.sfc.sf2.dialog.properties.io.DialogPropertiesAsmProcessor;
import com.sfc.sf2.dialog.properties.io.DialogPropertiesDisassemblyProcessor;
import com.sfc.sf2.dialog.properties.io.DialogPropertiesEnumsProcessor;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.mapsprite.MapSpriteEntries;
import com.sfc.sf2.mapsprite.MapSpriteManager;
import com.sfc.sf2.portrait.Portrait;
import com.sfc.sf2.portrait.PortraitManager;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wiz
 */
public class DialogPropertiesManager extends AbstractManager {
       
    private final MapSpriteManager mapSpriteManager = new MapSpriteManager();
    private final PortraitManager portraitManager = new PortraitManager();
    private final AllyDialogPropertiesAsmProcessor allyDialogAsmProcessor = new AllyDialogPropertiesAsmProcessor();
    private final DialogPropertiesAsmProcessor dialogAsmProcessor = new DialogPropertiesAsmProcessor();
    private final DialogPropertiesDisassemblyProcessor dialogDisassemblyProcessor = new DialogPropertiesDisassemblyProcessor();
    private final DialogPropertiesEnumsProcessor dialogEnumsProcessor = new DialogPropertiesEnumsProcessor();
    
    private DialogPropertiesEnums dialogEnums;
    private DialogProperty[] dialogProperties;
    private HashMap<Integer, MapSprite> mapsprites;
    private HashMap<Integer, Portrait> portraits;

    @Override
    public void clearData() {
        mapSpriteManager.clearData();
        portraitManager.clearData();
        dialogEnums = null;
        dialogProperties = null;
        if (mapsprites != null) {
            for (Map.Entry<Integer, MapSprite> entry : mapsprites.entrySet()) {
                MapSprite sprite = entry.getValue();
                if (sprite != null) {
                    sprite.clearIndexedColorImage(true);
                }
            }
        }
        if (portraits != null) {
            for (Map.Entry<Integer, Portrait> entry : portraits.entrySet()) {
                Portrait portrait = entry.getValue();
                if (portrait != null) {
                    portrait.clearIndexedColorImage();
                }
            }
        }
    }
       
    public DialogProperty[] importDisassembly(Path filePath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        if (FileFormat.getFormat(filePath) == FileFormat.BIN) {
            dialogProperties = dialogDisassemblyProcessor.importDisassembly(filePath, dialogEnums);
        } else {
            dialogProperties = dialogAsmProcessor.importAsmData(filePath, dialogEnums);
        }
        Console.logger().info("Dialog properties successfully imported from : " + filePath);
        Console.logger().finest("EXITING importDisassembly");
        return dialogProperties;
    }
       
    public DialogProperty[] importAlliesDisassembly(Path filePath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importAlliesDisassembly");
        dialogProperties = allyDialogAsmProcessor.importAsmData(filePath, dialogEnums);
        Console.logger().info("Allies dialog properties successfully imported from : " + filePath);
        Console.logger().finest("EXITING importAlliesDisassembly");
        return dialogProperties;
    }
    
    public void importImagesAndEnums(Path palettePath, Path mapspritesPath, Path portraitsPath, Path sf2EnumsPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importImagesAndEnums");
        if (dialogEnums == null) {
            dialogEnums = dialogEnumsProcessor.importAsmData(sf2EnumsPath, null);
            Console.logger().info("Dialog properties enums successfully imported from : " + sf2EnumsPath);
        }
        if (mapsprites == null) {
            MapSpriteEntries items = mapSpriteManager.importDisassemblyFromEntryFile(palettePath, mapspritesPath);
            mapsprites = new HashMap<>(items.getEntries().length);
            for (int i = 0; i < items.getEntries().length; i++) {
                mapsprites.put(i, items.getMapSprite(i));
            }
            Console.logger().info("Mapsprites successfully imported from : " + mapspritesPath);
        }
        if (portraits == null) {
            Portrait[] items = portraitManager.importDisassemblyFromEntryFile(portraitsPath);
            portraits = new HashMap<>(items.length);
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    portraits.put(items[i].getIndex(), items[i]);
                }
            }
            Console.logger().info("Portraits successfully imported from : " + portraitsPath);
        }
        dialogEnums.setImages(mapsprites, portraits);
        Console.logger().finest("EXITING importImagesAndEnums");
    }
    
    public void exportDisassembly(Path filePath, DialogProperty[] dialogProperties) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.dialogProperties = dialogProperties;
        if (FileFormat.getFormat(filePath) == FileFormat.BIN) {
            dialogDisassemblyProcessor.exportDisassembly(filePath, dialogProperties, dialogEnums);
        } else {
            dialogAsmProcessor.exportAsmData(filePath, dialogProperties, dialogEnums);
        }
        Console.logger().info("Dialog properties successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    } 
    
    public void exportAlliesDisassembly(Path filePath, DialogProperty[] dialogProperties) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportAlliesDisassembly");
        this.dialogProperties = dialogProperties;
        allyDialogAsmProcessor.exportAsmData(filePath, dialogProperties, dialogEnums);
        Console.logger().info("Allies dialog properties successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportAlliesDisassembly");
    } 

    public DialogProperty[] getDialogProperties() {
        return dialogProperties;
    }

    public DialogPropertiesEnums getDialogEnums() {
        return dialogEnums;
    }
}
