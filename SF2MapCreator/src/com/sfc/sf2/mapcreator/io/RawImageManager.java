/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator.io;

import com.sfc.sf2.mapcreator.ProtoMap;
import com.sfc.sf2.map.layout.MapLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TiMMy
 */
public class RawImageManager {
    
    public static final int MAP_PIXEL_WIDTH = 64*3*8;
    public static final int MAP_PIXEL_HEIGHT = 64*3*8;
    /*
    public static Map importMapFromRawImage(String filepath, String flagsPath, String hptilesPath) {
        System.out.println("com.sfc.sf2.map.io.RawImageManager.importImage() - Importing Image files ...");
        Map map = null;
        try {
            map = new Map();
            MapLayout ml = new MapLayout();
            map.setLayout(ml);
            ml.setBlocks(com.sfc.sf2.map.block.io.RawImageManager.importImage(filepath));
            map.setBlocks(ml.getBlocks());
            map.setTiles(com.sfc.sf2.map.block.io.RawImageManager.getImportedTiles());
            com.sfc.sf2.map.block.io.MetaManager.importBlockHpTilesFile(hptilesPath, ml.getBlocks(), 64);
            loadFlags(ml, flagsPath);
        } catch(Exception e) {
             System.err.println("com.sfc.sf2.map.io.RawImageManager.importImage() - Error while parsing graphics data : "+e);
        }
        System.out.println("com.sfc.sf2.map.io.RawImageManager.importImage() - Image files imported.");
        return map;
    }
    
    public static void loadFlags(MapLayout layout, String flagsPath) {
        Path fpath = Paths.get(flagsPath);
        if (fpath.toFile().exists() && !fpath.toFile().isDirectory()) {
            try {
                int blockIndex = 0;
                int cursor = 0;
                Scanner scan = new Scanner(fpath);
                while(scan.hasNext()){
                    String line = scan.nextLine();
                    while(cursor<line.length()-1){
                        String flags = (cursor<line.length()-2)?line.substring(cursor, cursor+2):line.substring(cursor);
                        int flagsValue = Integer.parseInt(flags, 16) << 8;
                        layout.getBlocks()[blockIndex].setFlags(flagsValue);
                        cursor+=2;
                        blockIndex++;
                    }
                    cursor=0;
                }
            } catch (IOException ex) {
                Logger.getLogger(RawImageManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    */
}
