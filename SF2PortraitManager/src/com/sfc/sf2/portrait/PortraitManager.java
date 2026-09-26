/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.portrait.io.PortraitDisassemblyProcessor;
import com.sfc.sf2.portrait.io.PortraitMetadataProcessor;
import com.sfc.sf2.portrait.io.PortraitPackage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class PortraitManager extends AbstractManager {
    
    private Portrait portrait;
    private EntriesAsmData lastEntriesData;

    @Override
    public void clearData() {
        if (portrait != null) {
            portrait.clearIndexedColorImage();
            portrait = null;
        }
        lastEntriesData = null;
    }
    
    public void importDisassembly(Path filePath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        PortraitPackage pckg = new PortraitPackage(FileHelpers.getNumberFromFileName(filePath.toFile()), PathHelpers.filenameFromPath(filePath));
        portrait = new PortraitDisassemblyProcessor().importDisassembly(filePath, pckg);
        Console.logger().info("Portrait successfully imported from : " + filePath);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void exportDisassembly(Path filePath, Portrait portrait) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.portrait = portrait;
        PortraitPackage pckg = new PortraitPackage(portrait.getIndex(), PathHelpers.filenameFromPath(filePath));
        new PortraitDisassemblyProcessor().exportDisassembly(filePath, portrait, pckg);
        Console.logger().info("Portrait successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void importImage(Path portraitPath, Path metadataPath) throws IOException, MetadataException, RawImageException {
        Console.logger().finest("ENTERING importImage");
        Tileset tileset = new TilesetManager().importImage(portraitPath, true);
        int index = FileHelpers.getNumberFromFileName(portraitPath.toFile());
        portrait = new Portrait(index, tileset.getName(), tileset);
        Console.logger().info("Portrait successfully imported from : " + portraitPath);
        try {
            new PortraitMetadataProcessor().importMetadata(metadataPath, portrait);
            Console.logger().info("Portrait metadata successfully imported from : " + metadataPath);
        } catch (Exception e) {
            Console.logger().info("ERROR Portrait metadata could not be imported : " + metadataPath + "\nImage still loaded.");
        }
        Console.logger().finest("EXITING importImage");
    }
    
    public void exportImage(Path portraitPath, Path metadataPath, Portrait portrait) throws IOException, MetadataException, RawImageException {
        Console.logger().finest("ENTERING exportImage");
        this.portrait = portrait;
        new TilesetManager().exportImage(portraitPath, portrait.getTileset());
        Console.logger().info("Portrait successfully exported to : " + portraitPath);
        new PortraitMetadataProcessor().exportMetadata(metadataPath, portrait);
        Console.logger().info("Portrait metadata successfully exported to : " + metadataPath);
        Console.logger().finest("EXITING exportImage");  
    }
       
    //TODO update to new format
    public Portrait[] importDisassemblyFromEntryFile(Path entriesPath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importDisassemblyFromEntryFile");
        EntriesAsmData entriesData = new EntriesAsmProcessor().importAsmData(entriesPath, null);
        Portrait[] portraits = new Portrait[entriesData.uniqueEntriesCount()];
        Path portraitPath = null;
        int failedToLoad = 0;
        for (int i = 0; i < portraits.length; i++) {
            try {
                Path uniquePath = entriesData.getPathForUnique(i);
                if (uniquePath == null) {
                    failedToLoad++;
                    continue;
                }
                portraitPath = PathHelpers.getIncbinPath().resolve(uniquePath);
                int index = FileHelpers.getNumberFromFileName(portraitPath.toFile());
                PortraitPackage pckg = new PortraitPackage(index, PathHelpers.filenameFromPath(portraitPath));
                portraits[i] = new PortraitDisassemblyProcessor().importDisassembly(portraitPath, pckg);
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Portrait could not be imported : " + portraitPath + " : " + e);
            }
        }
        Console.logger().info(portraits.length + " unique portraits imported from entries file : " + entriesPath);
        Console.logger().info((entriesData.entriesCount() - entriesData.uniqueEntriesCount()) + " duplicate portrait entries found.");
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " portraits failed to import. See logs above");
        }
        Console.logger().finest("EXITING importDisassemblyFromEntryFile");
        lastEntriesData = entriesData;
        return portraits;
    }

    public void exportDisassemblyToEntryFile(Path entriesPath, Portrait[] portraits) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassemblyToEntryFile");
        EntriesAsmData entriesData = lastEntriesData;
        if (entriesData == null) {
            entriesData = new EntriesAsmData();
            entriesData.setHeadername("Portraits");
            entriesData.setPointerListName("pt_Portraits");
            entriesData.setIsDoubleList(true);
            for (int i = 0; i < portraits.length; i++) {
                if (portraits[i] == null) {
                    continue;
                }
                String entry = formatPortraitEntry(portraits[i]);
                Path relative = Path.of("data/graphics/portraits/" + String.format("portrait%02d.bin", portraits[i].getIndex()));
                entriesData.addEntry(entry);
                entriesData.addPath(entry, relative);
            }
        }

        PortraitDisassemblyProcessor processor = new PortraitDisassemblyProcessor();
        int exported = 0;
        for (int i = 0; i < entriesData.uniqueEntriesCount(); i++) {
            Path uniquePath = entriesData.getPathForUnique(i);
            if (uniquePath == null) {
                continue;
            }
            Portrait portrait = findPortraitForEntry(portraits, entriesData, i);
            if (portrait == null) {
                Console.logger().warning("No portrait data for unique entry " + entriesData.getUniqueEntries(i));
                continue;
            }
            Path portraitPath = uniquePath.isAbsolute() ? uniquePath : PathHelpers.getIncbinPath().resolve(uniquePath);
            if (!PathHelpers.createPathIfRequred(portraitPath)) {
                continue;
            }
            PortraitPackage pckg = new PortraitPackage(portrait.getIndex(), PathHelpers.filenameFromPath(portraitPath));
            processor.exportDisassembly(portraitPath, portrait, pckg);
            exported++;
        }
        new EntriesAsmProcessor().exportAsmData(entriesPath, entriesData, null);
        Console.logger().info(exported + " unique portraits exported with entries file : " + entriesPath);
        Console.logger().finest("EXITING exportDisassemblyToEntryFile");
    }

    private static String formatPortraitEntry(Portrait portrait) {
        if (portrait.getName() != null && !portrait.getName().isEmpty() && !portrait.getName().contains(".")) {
            String name = portrait.getName();
            if (name.startsWith("Portrait") || name.startsWith("portrait")) {
                return name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }
        return String.format("Portrait%02d", portrait.getIndex());
    }

    private static Portrait findPortraitForEntry(Portrait[] portraits, EntriesAsmData entriesData, int uniqueIndex) {
        Path uniquePath = entriesData.getPathForUnique(uniqueIndex);
        String uniqueName = entriesData.getUniqueEntries(uniqueIndex);
        for (int i = 0; i < portraits.length; i++) {
            Portrait portrait = portraits[i];
            if (portrait == null) {
                continue;
            }
            if (uniqueName != null && uniqueName.equalsIgnoreCase(portrait.getName())) {
                return portrait;
            }
            if (uniquePath != null) {
                String fileName = PathHelpers.filenameFromPath(uniquePath);
                if (fileName.equalsIgnoreCase(portrait.getName()) || fileName.equalsIgnoreCase("portrait" + String.format("%02d", portrait.getIndex()))) {
                    return portrait;
                }
            }
            if (portrait.getIndex() == uniqueIndex) {
                return portrait;
            }
        }
        if (uniqueIndex >= 0 && uniqueIndex < portraits.length) {
            return portraits[uniqueIndex];
        }
        return null;
    }

    public Portrait getPortrait() {
        return portrait;
    }

    public void setPortrait(Portrait portrait) {
        this.portrait = portrait;
    }
}
