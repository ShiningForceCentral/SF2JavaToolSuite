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

    @Override
    public void clearData() {
        if (portrait != null) {
            portrait.clearIndexedColorImage();
            portrait = null;
        }
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
        Portrait[] portraits = new Portrait[entriesData.entriesCount()];
        Path portraitPath = null;
        int failedToLoad = 0;
        for (int i = 0; i < portraits.length; i++) {
            try {
                portraitPath = PathHelpers.getIncbinPath().resolve(entriesData.getPathForUnique(i));
                int index = FileHelpers.getNumberFromFileName(portraitPath.toFile());
                PortraitPackage pckg = new PortraitPackage(index, PathHelpers.filenameFromPath(portraitPath));
                portraits[i] = new PortraitDisassemblyProcessor().importDisassembly(portraitPath, pckg);
            } catch (Exception e) {
                failedToLoad++;
                Console.logger().warning("Portrait could not be imported : " + portraitPath + " : " + e);
            }
        }
        Console.logger().info(portraits.length + " portraits successfully imported from entries file : " + entriesPath);
        Console.logger().info((entriesData.entriesCount() - entriesData.uniqueEntriesCount()) + " duplicate portrait entries found.");
        if (failedToLoad > 0) {
            Console.logger().severe(failedToLoad + " portraits failed to import. See logs above");
        }
        Console.logger().finest("ENTERING importDisassemblyFromEntryFile");
        return portraits;
    }

    public Portrait getPortrait() {
        return portrait;
    }

    public void setPortrait(Portrait portrait) {
        this.portrait = portrait;
    }
}
