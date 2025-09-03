/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.io;

import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.core.io.AbstractMetadataProcessor;
import com.sfc.sf2.core.io.MetadataException;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class BattleSpriteMetadataProcessor extends AbstractMetadataProcessor<BattleSprite> {

    @Override
    protected void parseMetaData(BufferedReader reader, BattleSprite item) throws MetadataException, IOException {
        String data = reader.readLine();
        data = data.substring(data.indexOf(":")+1).trim();
        item.setAnimSpeed(Short.parseShort(data));
        data = reader.readLine();
        data = data.substring(data.indexOf(":")+1).trim();
        String[] statusOffset = data.split(",");
        item.setStatusOffsetX(Byte.parseByte(statusOffset[0].trim()));
        item.setStatusOffsetY(Byte.parseByte(statusOffset[1].trim()));
    }

    @Override
    protected void packageMetaData(FileWriter writer, BattleSprite item) throws MetadataException, IOException {
        writer.append(String.format("Anim Speed: %d\n", item.getAnimSpeed()));
        writer.append(String.format("Status Offset: %d, %d\n", item.getStatusOffsetX(), item.getStatusOffsetY()));
    }
    
}
