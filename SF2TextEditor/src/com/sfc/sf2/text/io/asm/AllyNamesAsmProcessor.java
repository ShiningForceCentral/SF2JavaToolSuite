/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.text.io.asm;

import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;

/**
 *
 * @author TiMMy
 */
public class AllyNamesAsmProcessor extends EntriesAsmProcessor {
    public AllyNamesAsmProcessor() {
        listPrefix = "table_";
        listItemPrefix = "allyName";
        listPathPrefix = null;
    }
}
