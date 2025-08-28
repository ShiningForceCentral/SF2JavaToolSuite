/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.icon;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.AbstractRawImageProcessor;
import com.sfc.sf2.core.io.AbstractRawImageProcessor.FileFormat;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.graphics.io.TilesetDisassemblyProcessor.TilesetCompression;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.icon.io.IconPackage;
import com.sfc.sf2.icon.io.IconRawImageProcessor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author wiz
 */
public class IconManager extends AbstractManager {
       
    public enum IconExportMode {
        INDIVIDUAL_FILES,
        SINGLE_FILE,
    }
    
    private final PaletteManager paletteManager = new PaletteManager();
    private final TilesetManager tilesetManager = new TilesetManager();
    private final IconRawImageProcessor iconRawImageProcessor = new IconRawImageProcessor();
    private Icon[] icons;

    @Override
    public void clearData() {
        if (icons != null) {
            for (int i = 0; i < icons.length; i++) {
                if (icons[i] != null) {
                    icons[i].clearIndexedColorImage(true);
                    icons[i] = null;
                }
            }
        }
    }
       
    public void importAllDisassemblies(Path paletteFilePath, Path basePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importAllDisassemblies");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        File[] files = FileHelpers.findAllFilesInDirectory(basePath, "icon", ".bin");
        Console.logger().info(files.length + " icon disasms found.");
        ArrayList<Icon> iconsList = new ArrayList<>();
        int failedToLoad = 0;
        for (File file : files) {
            Path iconPath = file.toPath();
            try {
                int index = FileHelpers.getNumberFromFileName(file);
                Tileset tileset = tilesetManager.importDisassembly(iconPath, palette, TilesetCompression.NONE, Icon.ICON_TILE_WIDTH);
                iconsList.add(new Icon(index, tileset));
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Icon could not be imported : " + iconPath + " : " + e);
            }
        }
        icons = new Icon[iconsList.size()];
        icons = iconsList.toArray(icons);
        Console.logger().info(icons.length + " icons successfully imported from disasms : " + basePath);
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " icons failed to import. See logs above");
        }
        Console.logger().finest("EXITING importAllDisassemblies");
    }
    
    public void exportAllDisassemblies(Path basePath, Icon[] icons) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportAllDisassemblies");
        int failedToSave = 0;
        Path filePath = null;
        for (Icon icon : icons) {
            try {
                if (icon != null && icon.getTileset() != null) {
                    int index = icon.getIndex();
                    filePath = basePath.resolve(String.format("icon%03d.bin", index));
                    tilesetManager.exportDisassembly(filePath, icon.getTileset(), TilesetCompression.NONE);
                }
            } catch (Exception e) {
                failedToSave++;
                Console.logger().warning("Icon could not be exported : " + filePath + " : " + e);
            }
        }
        Console.logger().info((icons.length - failedToSave) + " icons successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " icons failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportAllDisassemblies");    
    }
    
    public void importAllImages(Path paletteFilePath, Path basePath, FileFormat format) throws IOException, DisassemblyException, RawImageException {
        Console.logger().finest("ENTERING importAllImages");
        Palette palette = paletteManager.importDisassembly(paletteFilePath, true);
        File singleFile = basePath.resolve("allIcons" + AbstractRawImageProcessor.GetFileExtensionString(format)).toFile();
        if (singleFile.exists()) {
            IconPackage pckg = new IconPackage(-1, palette, 10);
            icons = iconRawImageProcessor.importRawImage(singleFile.toPath(), pckg);
            Console.logger().info(icons.length + " Icons with successfully imported from image : " + singleFile);
        } else {
            File[] files = FileHelpers.findAllFilesInDirectory(basePath, "icon", AbstractRawImageProcessor.GetFileExtensionString(format));
            Console.logger().info(files.length + " icon images found.");
            ArrayList<Icon> iconsList = new ArrayList<>();
            int failedToLoad = 0;
            for (File file : files) {
                Path iconPath = file.toPath();
                try {
                    int index = FileHelpers.getNumberFromFileName(file);
                    IconPackage pckg = new IconPackage(index, palette, 10);
                    Icon[] icons = iconRawImageProcessor.importRawImage(iconPath, pckg);
                    for (int i = 0; i < icons.length; i++) {
                        iconsList.add(icons[i]);
                    }
                } catch (Exception e) {
                    failedToLoad++;
                    Console.logger().warning("Icon could not be imported : " + iconPath + " : " + e);
                }
            }
            icons = new Icon[iconsList.size()];
            icons = iconsList.toArray(icons);
            Console.logger().info(icons.length + " icons with successfully imported from images : " + basePath);
            if (failedToLoad > 0) {
                Console.logger().severe(failedToLoad + " icons failed to import. See logs above");
            }
        }
        Console.logger().finest("EXITING importAllImages");
    }
    
    public void exportAllImages(Path basePath, Icon[] icons, IconExportMode exportMode, int iconsPerRow, FileFormat format) throws IOException, RawImageException {
        Console.logger().finest("ENTERING exportAllImages");
        int failedToSave = 0;
        Path iconPath = null;
        try {
            switch (exportMode) {
                case INDIVIDUAL_FILES:
                for (Icon icon : icons) {
                    if (icon != null && icon.getTileset() != null) {
                        iconPath = basePath.resolve(String.format("icon%03d%s", icon.getIndex(), AbstractRawImageProcessor.GetFileExtensionString(format)));
                        IconPackage pckg = new IconPackage(icon.getIndex(), icon.getPalette(), 1);
                        iconRawImageProcessor.exportRawImage(iconPath, new Icon[] { icon }, pckg);
                    }
                }
                break;
            case SINGLE_FILE:
                iconPath = basePath.resolve(String.format("allIcons%s", AbstractRawImageProcessor.GetFileExtensionString(format)));
                IconPackage pckg = new IconPackage(-1, icons[0].getPalette(), iconsPerRow);
                iconRawImageProcessor.exportRawImage(iconPath, icons, pckg);
                break;
            }
        } catch (Exception e) {
            failedToSave++;
            Console.logger().warning("Icon could not be exported : " + iconPath + " : " + e);
        }
        Console.logger().info((icons.length - failedToSave) + " icons successfully exported.");
        if (failedToSave > 0) {
            Console.logger().severe(failedToSave + " icons failed to export. See logs above");
        }
        Console.logger().finest("EXITING exportAllImages");   
    }

    public Icon[] getIcons() {
        return icons;
    }

    public void setIcons(Icon[] icons) {
        this.icons = icons;
    }
}
