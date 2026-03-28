/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.ListAsmProcessor;

/**
 *
 * @author TiMMy
 */
public class EnemyMapspriteAsmProcessor extends ListAsmProcessor<String> {

    public EnemyMapspriteAsmProcessor() {
        super(String[].class, "table_EnemyMapsprites", "mapsprite");
    }

    @Override
    protected String parseItem(int index, String itemData) {
        return itemData;
    }

    @Override
    protected String packageItem(int index, String item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected String getHeaderName(String[] item, EmptyPackage pckg) {
        return "Enemy map sprite indexes table";
    }
}
