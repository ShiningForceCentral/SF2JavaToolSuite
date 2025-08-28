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
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
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
    private final TilesetManager tilesetManager = new TilesetManager();
    private final PortraitDisassemblyProcessor portraitDisassemblyProcessor = new PortraitDisassemblyProcessor();
    private final PortraitMetadataProcessor portraitMetadataProcessor = new PortraitMetadataProcessor();
    
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
        PortraitPackage pckg = new PortraitPackage(PathHelpers.filenameFromPath(filePath));
        portrait = portraitDisassemblyProcessor.importDisassembly(filePath, pckg);
        Console.logger().info("Portrait successfully imported from : " + filePath);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void exportDisassembly(Path filePath, Portrait portrait) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.portrait = portrait;
        PortraitPackage pckg = new PortraitPackage(PathHelpers.filenameFromPath(filePath));
        portraitDisassemblyProcessor.exportDisassembly(filePath, portrait, pckg);
        Console.logger().info("Portrait successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void importImage(Path portraitPath, Path metadataPath) throws IOException, MetadataException, RawImageException {
        Console.logger().finest("ENTERING importImage");
        Tileset tileset = tilesetManager.importImage(portraitPath, true);
        portrait = new Portrait(tileset.getName(), tileset);
        Console.logger().info("Portrait successfully imported from : " + portraitPath);
        try {
            portraitMetadataProcessor.importMetadata(metadataPath, portrait);
            Console.logger().info("Portrait metadata successfully imported from : " + metadataPath);
        } catch (Exception e) {
            Console.logger().info("ERROR Portrait metadata could not be imported : " + metadataPath + "\nImage still loaded.");
        }
        Console.logger().finest("EXITING importImage");
    }
    
    public void exportImage(Path portraitPath, Path metadataPath, Portrait portrait) throws IOException, MetadataException, RawImageException {
        Console.logger().finest("ENTERING exportImage");
        this.portrait = portrait;
        tilesetManager.exportImage(portraitPath, portrait.getTileset());
        Console.logger().info("Portrait successfully exported to : " + portraitPath);
        portraitMetadataProcessor.exportMetadata(metadataPath, portrait);
        Console.logger().info("Portrait metadata successfully exported to : " + metadataPath);
        Console.logger().finest("EXITING exportImage");  
    }
       
    //TODO update to new format
    /*public Portrait[] importAllDisassembly(String basePath){
        Console.logger().finest("ENTERING importAllDisassembly");
        Portrait[] portraits = PortraitDisassemblyProcessor.importAllDisassembly(basePath);
        Console.logger().finest("ENTERING importAllDisassembly");
        return portraits;
    } 
       
    //TODO update to new format
    public Portrait[] importDisassemblyFromEntryFile(String basePath, String entryPath){
        Console.logger().finest("ENTERING importDisassemblyFromEntryFile");
        Portrait[] portraits = PortraitDisassemblyProcessor.importDisassemblyFromEntryFile(basePath, entryPath);
        Console.logger().finest("ENTERING importDisassemblyFromEntryFile");
        return portraits;
    }*/
    
    //TODO update to new format
    /*public static Portrait[] importAllDisassembly(String filepath){
        System.out.println("com.sfc.sf2.portrait.io.DisassemblyManager.importAllDisassembly() - Importing ALL disassembly files ...");
        Portrait[] portraits = null;
        List<Portrait> portraitList = new ArrayList();
        
        String dir = filepath.substring(0, filepath.lastIndexOf(System.getProperty("file.separator")));
        File directory = new File(dir);
        File[] files = directory.listFiles();
        for(File f : files){        
            if(f.getName().endsWith(".bin")){
                System.out.println("Importing "+f.getAbsolutePath()+" ...");
                Portrait portrait = importDisassembly(f.getAbsolutePath());
                portraitList.add(portrait);
            }
        }
        portraits = new Portrait[portraitList.size()];
        portraits = portraitList.toArray(portraits);
        
        System.out.println("com.sfc.sf2.portrait.io.DisassemblyManager.importAllDisassembly() - ALL Disassembly files imported.");
        return portraits;
    }
    
    //TODO update to new format
    public static Portrait[] importDisassemblyFromEntryFile(String basepath, String entriesPath){
        System.out.println("com.sfc.sf2.portrait.io.DisassemblyManager.importAllDisassembly() - Importing ALL disassembly files ...");
        Portrait[] portraits = null;
        List<Portrait> portraitList = new ArrayList();
        try{
            File entryFile = new File(entriesPath);
            Scanner scan = new Scanner(entryFile);
            List<String> filepaths = new ArrayList();
            while(scan.hasNext()){
                String line = scan.nextLine();
                if(line.contains("dc.l")){
                    String pointer = line.substring(line.indexOf("dc.l")+5).trim();
                    String filepath = null;
                    Scanner filescan = new Scanner(entryFile);
                    while(filescan.hasNext()){
                        String pathline = filescan.nextLine();
                        if(pathline.startsWith(pointer)){
                            filepath = pathline.substring(pathline.indexOf("\"")+1, pathline.lastIndexOf("\""));
                        }
                    }
                    filepaths.add(filepath);
                }
            }            
            for(int i=0;i<filepaths.size();i++){
                String filePath = basepath + filepaths.get(i);
                System.out.println("Importing "+filePath+" ...");
                Portrait portrait = importDisassembly(filePath);
                portraitList.add(portrait);
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.mapsprite.io.PngManager.importPng() - Error while parsing graphics data : "+e);
        }      
        portraits = new Portrait[portraitList.size()];
        portraits = portraitList.toArray(portraits);   
        System.out.println("com.sfc.sf2.portrait.io.DisassemblyManager.importAllDisassembly() - ALL Disassembly files imported.");
        return portraits;
    }*/

    public Portrait getPortrait() {
        return portrait;
    }

    public void setPortrait(Portrait portrait) {
        this.portrait = portrait;
    }
}
