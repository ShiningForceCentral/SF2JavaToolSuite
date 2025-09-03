/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.map.block.MapBlock;

/**
 *
 * @author TiMMy
 */
public class MapBlockRawImageProcessor {
    
    private static int importedImageBlockWidth = 64;
    private static Tile[] importedTiles;
    
    public static MapBlock[] importImage(String filepath) {
        MapBlock[] blocks = null;
        /*try{
            Tile[] tiles = com.sfc.sf2.graphics.io.RawImageManager.importImage(filepath);
            importedTiles = tiles;
            if(tiles == null || tiles.length == 0){
                throw new IOException("Image not found or image corrupted :" + filepath);
            }
            
            int tilesPerRow = com.sfc.sf2.graphics.io.RawImageManager.getImportedImageTileWidth();
            importedImageBlockWidth = tilesPerRow / 3;
            Tile[][] blockTiles = new Tile[tiles.length/9][9];
            for (int t = 0; t < tiles.length; t++) {
                int globalX = t % tilesPerRow;
                int globalY = t / tilesPerRow;
                int blockIndex = (globalX/3) + (globalY/3)*importedImageBlockWidth;
                int tileIndexInBlock = (globalX % 3) + (globalY % 3)*3;
                blockTiles[blockIndex][tileIndexInBlock] = tiles[t];
            }
            blocks = new MapBlock[blockTiles.length];
            for (int i = 0; i < blockTiles.length; i++) {
                MapBlock block = new MapBlock();
                block.setIndex(i);
                block.setTiles(blockTiles[i]);
                blocks[i] = block;
            }
            
        }catch(Exception e){
             LOG.throwing(LOG.getName(), "importBlocksImage", e);
        }*/    
        return blocks;
    }
    
    public static void exportRawImage(MapBlock[] blocks, String filepath, int blocksPerRow, int fileFormat){
        /*try {
            LOG.entering(LOG.getName(),"exportBlocksImage");
            int tilesPerRow = blocksPerRow*3;
            int rows = blocks.length*9/tilesPerRow;
            if (blocks.length%blocksPerRow != 0) rows++;
            Tile[] tiles = new Tile[rows * tilesPerRow];
            for (int t = 0; t < tiles.length; t++) {
                int globalX = t % tilesPerRow;
                int globalY = t / tilesPerRow;
                int blockIndex = (globalX/3) + (globalY/3)*blocksPerRow;
                int tileIndexInBlock = (globalX % 3) + (globalY % 3)*3;
                if (blockIndex < blocks.length) {
                    tiles[t] = blocks[blockIndex].getTiles()[tileIndexInBlock];
                }
            }
            com.sfc.sf2.graphics.io.RawImageManager.exportImage(tiles, filepath, tilesPerRow, fileFormat);
        } catch (Exception ex) {
            LOG.throwing(LOG.getName(),"exportBlocksImage", ex);
        }*/
    }
    
    public static int getImportedImageBlockWidth() {
        return importedImageBlockWidth;
    }
    
    public static Tile[] getImportedTiles() {
        return importedTiles;
    }
}
