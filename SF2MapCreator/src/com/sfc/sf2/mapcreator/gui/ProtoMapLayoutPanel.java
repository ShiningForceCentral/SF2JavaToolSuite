/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator.gui;

import com.sfc.sf2.map.layout.gui.MapLayoutPanel;
import com.sfc.sf2.mapcreator.ProtoMap;

/**
 *
 * @author TiMMy
 */
public class ProtoMapLayoutPanel extends MapLayoutPanel {
    
    private ProtoMap protoMap;

    public ProtoMap getProtoMap() {
        return protoMap;
    }

    public void setProtoMap(ProtoMap protoMap) {
        this.protoMap = protoMap;
        if (protoMap == null) {
            setMapLayout(null);
        } else {
            setMapLayout(protoMap.getLayout());
        }
        this.redraw();
    }
}
