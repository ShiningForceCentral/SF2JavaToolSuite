/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator;

import com.sfc.sf2.core.INameable;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.MapLayout;
import java.util.Arrays;

/**
 *
 * @author TiMMy
 */
public class ProtoMap implements INameable {
    //Core data
    private String name;
    private MapBlockset blockset;
    private MapLayout layout;

    public ProtoMap(String name, MapBlockset blockset, MapLayout layout) {
        this.name = name;
        this.blockset = blockset;
        this.layout = layout;
    }

    @Override
    public String getName() {
        return name;
    }

    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
    }

    public MapLayout getLayout() {
        return layout;
    }

    public void setLayout(MapLayout layout) {
        this.layout = layout;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProtoMap)) return super.equals(obj);
        ProtoMap protoMap = (ProtoMap)obj;
        if (!this.name.equals(protoMap.name)) return false;
        if (!blockset.equals(protoMap.blockset)) return false;
        if (!layout.equals(protoMap.layout)) return false;
        return true;
    }
    
    public ProtoMap clone() {
        return new ProtoMap(name, blockset.clone(), layout.clone());
    }
}
