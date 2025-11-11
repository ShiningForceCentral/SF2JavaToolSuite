/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout.io;

import com.sfc.sf2.core.io.AbstractMetadataProcessor;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.map.layout.MapLayout;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class MapLayoutMetaProcessor extends AbstractMetadataProcessor<MapLayout> {
        
    @Override
    protected void parseMetaData(BufferedReader reader, MapLayout item) throws IOException, MetadataException {
        int lineIndex = 0;
        int cursor = 0;
        String line;
        MapLayoutBlock[] blocks = item.getBlocks();
        while ((line = reader.readLine()) != null) {
            while (cursor < line.length()) {
                int blockIndex = lineIndex*BLOCK_WIDTH + cursor/2;
                int value = Integer.parseInt(line.substring(cursor, cursor+2), 16);
                blocks[blockIndex].setFlags(value<<8);
                cursor++;
            }
            cursor = 0;
            lineIndex++;
        }
    }

    @Override
    protected void packageMetaData(FileWriter writer, MapLayout item) throws IOException, MetadataException {
        MapLayoutBlock[] blocks = item.getBlocks();
        for (int b = 0; b < blocks.length; b++) {
            if (b > 0 && (b%BLOCK_WIDTH) == 0) {
                writer.write("\n");
            }
            writer.write(String.format("%02X", blocks[b].getFlags()>>8));
        }
    }
}
