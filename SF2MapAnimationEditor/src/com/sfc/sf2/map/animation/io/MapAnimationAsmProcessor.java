/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.animation.io;

import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class MapAnimationAsmProcessor extends AbstractAsmProcessor<MapAnimation, MapAnimationPackage> {

    @Override
    protected MapAnimation parseAsmData(BufferedReader reader, MapAnimationPackage pckg) throws IOException, AsmException {
        ArrayList<MapAnimationFrame> framesList = new ArrayList<>();
        String line;
        int tileset = -1;
        int length = -1;
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("mapAnimation")) {
                String[] split = line.split("\\s+");
                tileset = StringHelpers.getValueInt(split[1].replace(",", "").trim());
                length = StringHelpers.getValueInt(split[2].trim());
                while ((line = reader.readLine()) != null) {
                    line = StringHelpers.trimAndRemoveComments(line);
                    if (line.startsWith("endWord")) {
                        break;  //End of file
                    } else if (!line.startsWith("mapAnimEntry")) {
                        continue;
                    }
                    split = line.split("\\s+");
                    int frameStart = StringHelpers.getValueInt(split[1].replace(",", "").trim());
                    int frameLength = StringHelpers.getValueInt(split[2].replace(",", "").trim());
                    int frameDest = StringHelpers.getValueInt(split[3].replace(",", "").trim());
                    int frameDelay = StringHelpers.getValueInt(split[4].trim());
                    framesList.add(new MapAnimationFrame(frameStart, frameLength, frameDest, frameDelay));
                }
            }
        }
        
        if (tileset == -1 || length == -1) {
            throw new AsmException("Map animation entry invalid. Could not find \"mapAnimation\" line or line is corrupted.");
        }
        MapAnimationFrame[] frames = new MapAnimationFrame[framesList.size()];
        frames = framesList.toArray(frames);
        return new MapAnimation(tileset, length, frames, pckg.tilesets());
    }

    @Override
    protected String getHeaderName(MapAnimation item, MapAnimationPackage pckg) {
        return "";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapAnimation item, MapAnimationPackage pckg) throws IOException, AsmException {
        writer.write(String.format("\t\t\t\tmapAnimation %2d, %2d\n", item.getTilesetId(), item.getLength()));
        MapAnimationFrame[] frames = item.getFrames();
        for (int i = 0; i < frames.length; i++) {
            writer.write(String.format("\t\t\t\t\tmapAnimEntry %2d, %2d, $%3X, %2d\n", frames[i].getStart(), frames[i].getLength(), frames[i].getDestValue(), frames[i].getDelay()));
        }
        writer.write("\t\t\t\tendWord\n");
    }
}
