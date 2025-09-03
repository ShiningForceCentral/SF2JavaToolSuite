/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

import com.sfc.sf2.map.block.MapBlock;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author TiMMy
 */
public class MapBlockMetaProcessor {
        
    public static void importBlockHpTilesFile(String filepath, MapBlock[] blocks, int blocksPerRow) {
        /*try {
            Path path = Paths.get(filepath);
            if(path.toFile().exists() && !path.toFile().isDirectory()){
                int lineIndex = 0;
                int cursor = 0;
                int tilesPerRow = blocksPerRow*3;
                Scanner scan = new Scanner(path);
                while(scan.hasNext()){
                    String line = scan.nextLine();
                    while(cursor<line.length()){
                        int globalX = cursor;
                        int globalY = lineIndex;
                        int blockIndex = (globalX/3) + (globalY/3)*blocksPerRow;
                        int tileIndexInBlock = (globalX % 3) + (globalY % 3)*3;
                        if (line.charAt(cursor) != ' ') {
                            blocks[blockIndex].getTiles()[tileIndexInBlock].setHighPriority(line.charAt(cursor)=='H');
                        }
                        cursor++;
                    }
                    cursor=0;
                    lineIndex++;
                }
            }
            System.out.println("Block HP Tiles file exported : " + path.toString());
        } catch (Exception ex) {
            Logger.getLogger(RawImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    public static void exportBlockHpTilesFile(MapBlock[] blocks, int blocksPerRow, String filepath) {
        /*try {
            LOG.entering(LOG.getName(),"export Block HP Tiles");
            File outputfile = new File(filepath);
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile));
            StringBuilder sb = new StringBuilder();
            int tilesPerRow = blocksPerRow*3;
            int rows = blocks.length*9/tilesPerRow;
            if (blocks.length%blocksPerRow != 0) rows++;
            int tiles = rows * tilesPerRow;
            for (int t = 0; t < tiles; t++) {
                if (t > 0 && (t%tilesPerRow) == 0) {
                    sb.append("\n");
                }
                int globalX = t % tilesPerRow;
                int globalY = t / tilesPerRow;
                int blockIndex = (globalX/3) + (globalY/3)*blocksPerRow;
                int tileIndexInBlock = (globalX % 3) + (globalY % 3)*3;
                if (blockIndex < blocks.length) {
                    sb.append(blocks[blockIndex].getTiles()[tileIndexInBlock].isHighPriority()?"H":"L");
                } else {
                    sb.append(' ');
                }
            }
            bw.write(sb.toString());
            bw.close();
            System.out.println("Block HP Tiles file exported : " + outputfile.getAbsolutePath());
        } catch (Exception ex) {
            Logger.getLogger(RawImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
