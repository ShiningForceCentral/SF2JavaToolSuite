/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * For entries files Handles both 'single-list' entries (example 1) and 'double-list' entries (example 2)
 * <br>Example 1 (single-list)
 * <pre>
 * ASM File path\to\file.asm :
 * ; 0x000000..0x000000 : Single List Example
 * item00: incbin "data/graphics/.../itemFile00.bin"
 * item01: incbin "data/graphics/.../itemFile01.bin"
 * </pre> 
 * <br>Example format
 * <pre>
 * ASM File path\to\file.asm :
 * ; 0x000000..0x000000 : Double List Example
 * pt_items:
 *      dc.l item00
 *      dc.l item01
 * item00: incbin "data/graphics/.../itemFile00.bin"
 * item01: incbin "data/graphics/.../itemFile01.bin"
 * </pre> 
 * 
 * @author TiMMy
 */
public class EntriesAsmProcessor extends AbstractAsmProcessor<EntriesAsmData, EmptyPackage> {

    protected String listPrefix = "pt_";
    protected String listItemPrefix = "dc.l";
    protected String listPathPrefix = "incbin";
    
    @Override
    protected EntriesAsmData parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        EntriesAsmData data = new EntriesAsmData();
        String line;
        boolean foundStart = false;
        boolean isDoubleList = false;
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == ';') {
                if (!foundStart && line.startsWith("; 0x")) {
                    data.setHeadername(line.substring(line.indexOf(':')+1).trim());
                }
                continue;
            } else if (line.length() > 5 && !foundStart) {
                foundStart = true;
                if (line.startsWith(listPrefix)) {
                    int colonIndex = line.indexOf(':');
                    data.setPointerListName(line.substring(0, colonIndex));
                    line = line.substring(colonIndex+1);
                }
            }

            if (foundStart) {
                line = StringHelpers.trimAndRemoveComments(line);
                int pathPrefixIndex = listPathPrefix == null ? -1 : line.indexOf(listPathPrefix);
                if (pathPrefixIndex > 0) {          //Is item:path pair
                    String entry = line.substring(0, line.indexOf(':'));
                    Path path = Path.of(line.substring(pathPrefixIndex+listPathPrefix.length()+2, line.length()-1));
                    data.addPath(entry, path);
                }
                if (line.startsWith(listItemPrefix)) {   //Is entry
                    String entry = line.substring(listItemPrefix.length()+1).replace("\"", "");
                    data.addEntry(entry);
                }
            }
        }
        data.setIsDoubleList(isDoubleList);
        return data;
    }
    
    @Override
    protected String getHeaderName(EntriesAsmData item, EmptyPackage pckg) {
        return item.getHeaderName();
    }

    @Override
    protected void packageAsmData(FileWriter writer, EntriesAsmData item, EmptyPackage pckg) throws IOException, AsmException {
        writer.write(item.getPointerListName());
        writer.write(":\n");
        if (item.getIsDoubleList()) {
            //Write list of entries first
            for (int i = 0; i < item.entriesCount(); i++) {
                writer.write(String.format("\t\t\t%s %s\n", listItemPrefix, item.getEntry(i)));
            }
        }
        //Write list of item:path pairs
        for (int i = 0; i < item.uniqueEntriesCount(); i++) {
            String path = item.getPathForUnique(i).toString().replace('\\', '/');
            writer.write(String.format("%s: %s \"%s\"\n", item.getUniqueEntries(i), listPathPrefix, path));
        }
    }
}
