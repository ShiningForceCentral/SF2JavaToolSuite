/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait.io;

import com.sfc.sf2.core.io.AbstractMetadataProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.portrait.Portrait;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class PortraitMetadataProcessor extends AbstractMetadataProcessor<Portrait> {

    @Override
    protected void parseMetaData(BufferedReader reader, Portrait item) throws IOException, MetadataException {
        String data = reader.readLine();
        int eyesCount = Integer.parseInt(data.split(":")[1].trim());
        int[][] eyeTiles = new int[eyesCount][4];
        for (int i = 0; i < eyesCount; i++) {
            data = reader.readLine();
            String[] eyeData = data.split(",");
            for (int d = 0; d < eyeData.length; d++) {
                eyeTiles[i][d] = Integer.parseInt(eyeData[d].trim());
            }
        }
        data = reader.readLine();
        int mouthsCount = Integer.parseInt(data.split(" ")[1].trim());
        int[][] mouthTiles = new int[mouthsCount][4];
        for (int i = 0; i < mouthsCount; i++) {
            data = reader.readLine();
            String[] mouthsData = data.split(",");
            for (int d = 0; d < mouthsData.length; d++) {
                mouthTiles[i][d] = Integer.parseInt(mouthsData[d].trim());
            }
        }
        item.setEyeTiles(eyeTiles);
        item.setMouthTiles(mouthTiles);
    }

    @Override
    protected void packageMetaData(FileWriter writer, Portrait item) throws IOException, MetadataException {
        int[][] eyeTiles = item.getEyeTiles();
        writer.append(String.format("Eyes: %s\n", eyeTiles.length));
        for (int i = 0; i < eyeTiles.length; i++) {
            writer.append(String.format("%d, %d, %d, %d\n", eyeTiles[i][0], eyeTiles[i][1], eyeTiles[i][2], eyeTiles[i][3]));
        }
        int[][] mouthTiles = item.getMouthTiles();
        writer.append(String.format("Mouths: %s\n", mouthTiles.length));
        for (int i = 0; i < mouthTiles.length; i++) {
            writer.append(String.format("%d, %d, %d, %d\n", mouthTiles[i][0], mouthTiles[i][1], mouthTiles[i][2], mouthTiles[i][3]));
        }
    }
}
