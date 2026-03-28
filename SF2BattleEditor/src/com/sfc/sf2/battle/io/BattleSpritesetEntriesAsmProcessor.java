/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.io;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class BattleSpritesetEntriesAsmProcessor extends AbstractAsmProcessor<EntriesAsmData, EmptyPackage> {

    @Override
    protected EntriesAsmData parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        EntriesAsmData entries = new EntriesAsmData();
        String line;
        int pathsCount = 0;
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("dc.l")) {
                entries.addEntry(line.substring(5));
            } else if (line.startsWith("include")) {
                entries.addPath(entries.getEntry(pathsCount), Path.of(line.substring(8).replace("\"", "")));
                pathsCount++;
            }
        }
        return entries;
    }

    @Override
    protected String getHeaderName(EntriesAsmData item, EmptyPackage pckg) {
        return "Battle Spritesets";
    }

    @Override
    protected void packageAsmData(FileWriter writer, EntriesAsmData item, EmptyPackage pckg) throws IOException, AsmException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
