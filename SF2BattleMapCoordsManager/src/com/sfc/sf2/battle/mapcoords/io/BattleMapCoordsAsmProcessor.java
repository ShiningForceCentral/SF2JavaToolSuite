/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords.io;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.ListAsmProcessor;

/**
 *
 * @author TiMMy
 */
public class BattleMapCoordsAsmProcessor extends ListAsmProcessor<BattleMapCoords> {

    public BattleMapCoordsAsmProcessor() {
        super(BattleMapCoords[].class, "table_BattleMapCoordinates", "battleMapCoordinates");
    }

    @Override
    protected BattleMapCoords parseItem(int index, String itemData) {
        String[] split = itemData.split(",");
        int map = Integer.parseInt(split[0].trim());
        int x = Integer.parseInt(split[1].trim());
        int y = Integer.parseInt(split[2].trim());
        int width = Integer.parseInt(split[3].trim());
        int height = Integer.parseInt(split[4].trim());
        int trigX = Integer.parseInt(split[5].trim());
        int trigY = Integer.parseInt(split[6].trim());
        return new BattleMapCoords(map, x, y, width, height, trigX, trigY);
    }

    @Override
    protected String getHeaderName(BattleMapCoords[] item, EmptyPackage pckg) {
        //Header plus info about the data
        return "Battle map coords\n\t\t\t\t\t\t\t; Map, X, Y, Width, Height, Trigger X, Trigger Y";
    }

    @Override
    protected String packageItem(int index, BattleMapCoords item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getMapIndex());
        sb.append(", ");
        sb.append(item.getX());
        sb.append(", ");
        sb.append(item.getY());
        sb.append(", ");
        sb.append(item.getWidth());
        sb.append(", ");
        sb.append(item.getHeight());
        sb.append(", ");
        sb.append(item.getTrigX());
        sb.append(", ");
        sb.append(item.getTrigY());
        return sb.toString();
    }
}
