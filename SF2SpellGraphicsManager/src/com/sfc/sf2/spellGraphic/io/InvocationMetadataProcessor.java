/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.io;

import com.sfc.sf2.core.io.AbstractMetadataProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.MetadataException;
import com.sfc.sf2.spellGraphic.InvocationGraphic;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
public class InvocationMetadataProcessor extends AbstractMetadataProcessor<InvocationGraphic> {

    @Override
    protected void parseMetaData(BufferedReader reader, InvocationGraphic item) throws IOException, MetadataException {
        String data = reader.readLine();
        data = data.substring(data.indexOf(":")+1).trim();
        item.setUnknown1(Short.parseShort(data));
        data = reader.readLine();
        data = data.substring(data.indexOf(":")+1).trim();
        item.setUnknown2(Short.parseShort(data));
        data = reader.readLine();
        data = data.substring(data.indexOf(":")+1).trim();
        item.setUnknown3(Short.parseShort(data));
    }

    @Override
    protected void packageMetaData(FileWriter writer, InvocationGraphic item) throws IOException, MetadataException {
        writer.append("Unknown 1: " + item.getUnknown1() + "\n");
        writer.append("Unknown 2: " + item.getUnknown2() + "\n");
        writer.append("Unknown 3: " + item.getUnknown3() + "\n");
    }
}
