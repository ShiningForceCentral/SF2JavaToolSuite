/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.io;

import com.sfc.sf2.core.io.AbstractMetadataProcessor;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class MapBlocksetMetaProcessor extends AbstractMetadataProcessor<MapBlockset> {
        
    @Override
    protected void parseMetaData(BufferedReader reader, MapBlockset item) throws IOException, MetadataException {
        int lineIndex = 0;
        int cursor = 0;
        int blocksPerRow = item.getBlocksPerRow();
        String line;
        while ((line = reader.readLine()) != null) {
            while (cursor < line.length()) {
                int globalX = cursor;
                int globalY = lineIndex;
                int blockIndex = (globalX/Block.TILE_WIDTH) + (globalY/Block.TILE_HEIGHT)*blocksPerRow;
                int tileIndexInBlock = (globalX%Block.TILE_WIDTH) + (globalY%Block.TILE_HEIGHT)*Block.TILE_WIDTH;
                if (line.charAt(cursor) != ' ') {
                    item.getBlocks()[blockIndex].getMapTiles()[tileIndexInBlock].getTileFlags().setFlag(TileFlags.TILE_FLAG_PRIORITY, line.charAt(cursor)=='H');
                }
                cursor++;
            }
            cursor = 0;
            lineIndex++;
        }
    }

    @Override
    protected void packageMetaData(FileWriter writer, MapBlockset item) throws IOException, MetadataException {
        int blocksPerRow = item.getBlocksPerRow();
        int tilesPerRow = blocksPerRow*Block.TILE_WIDTH;
        MapBlock[] blocks = item.getBlocks();
        int rows = blocks.length*Block.TILES_COUNT/tilesPerRow;
        if (blocks.length%blocksPerRow != 0) rows++;
        int tiles = rows * tilesPerRow;
        for (int t = 0; t < tiles; t++) {
            if (t > 0 && (t%tilesPerRow) == 0) {
                writer.write("\n");
            }
            int globalX = t % tilesPerRow;
            int globalY = t / tilesPerRow;
            int blockIndex = (globalX/3) + (globalY/3)*blocksPerRow;
            int tileIndexInBlock = (globalX % 3) + (globalY % 3)*3;
            if (blockIndex < blocks.length) {
                writer.write(blocks[blockIndex].getMapTiles()[tileIndexInBlock].getTileFlags().isPriority()?"H":"L");
            } else {
                writer.write(' ');
            }
        }
    }
}
