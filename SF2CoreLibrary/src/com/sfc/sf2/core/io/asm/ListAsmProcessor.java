/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Simplifies processing ASM files that list data
 * <br>Example
 * <pre>
 * ASM File path\to\file.asm :
 * ; 0x000000..0x000000 : List Example
 * table_listName:
 *      listItem item00DataA item00DataB
 *      listItem item01DataA item01DataB
 * </pre> 
 * @author TiMMy
 */
public abstract class ListAsmProcessor<TItem> extends AbstractAsmProcessor<TItem[], EmptyPackage> {

    private final Class<TItem[]> collectionClass;
    private final String listNameIdentifier;
    private final String listItemIdentifier;

    /**
     * @param collectionClass The array class for the collection conversion (Java does not allow generic array creation)
     * @param listNameIdentifier The String that indicates the start of the list
     * @param listItemIdentifier The String that indicates that the line of the file is an item of the list
     */
    public ListAsmProcessor(Class<TItem[]> collectionClass, String listNameIdentifier, String listItemIdentifier) {
        if (!listNameIdentifier.endsWith(":")) {
            listNameIdentifier += ":";
        }
        this.collectionClass = collectionClass;
        this.listNameIdentifier = listNameIdentifier;
        this.listItemIdentifier = listItemIdentifier;
    }
    
    @Override
    protected TItem[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<TItem> itemsList = new ArrayList<>();
        String line;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(listNameIdentifier)) {   //Found start of list
                line = line.substring(line.indexOf(':')+1);
                do {
                    if (line.length() == 0 || line.charAt(0) == ';') {
                        continue;   //empty line
                    }
                    line = StringHelpers.trimAndRemoveComments(line);
                    if (line.startsWith(listItemIdentifier)) {
                        line = line.substring(listItemIdentifier.length()).trim();
                        if (line.length() > 0) {
                            TItem item = parseItem(index, line);
                            itemsList.add(item);
                        } else {
                            Console.logger().warning("WARNING " + listItemIdentifier + " number " + index + " is blank so it will be skipped.");
                        }
                        index++;
                    }
                } while((line = reader.readLine()) != null);
                break;  //End of list
            }
        }
        if (itemsList.size() == 0) {
            throw new AsmException("Ascii table data was not found.");
        }
        
        TItem[] items = collectionClass.cast(Array.newInstance(collectionClass.getComponentType(), itemsList.size()));
        for (int i = 0; i < items.length; i++) {
            items[i] = itemsList.get(i);
        }
        return items;
    }
    
    protected abstract TItem parseItem(int index, String itemData);

    @Override
    protected void packageAsmData(FileWriter writer, TItem[] item, EmptyPackage pckg) throws IOException, AsmException {
        writer.write(listNameIdentifier);
        writer.write("\n");
        for (int i = 0; i < item.length; i++) {
            writer.write("\t\t\t");
            writer.write(listItemIdentifier);
            writer.write(" ");
            writer.write(packageItem(i, item[i]));
            writer.write("\n");
        }
    }
    
    protected abstract String packageItem(int index, TItem item);
}
