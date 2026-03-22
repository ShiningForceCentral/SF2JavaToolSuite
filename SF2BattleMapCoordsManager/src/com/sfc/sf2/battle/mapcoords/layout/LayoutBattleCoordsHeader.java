/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords.layout;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.LayoutCoordsHeader;

/**
 *
 * @author TiMMy
 */
public class LayoutBattleCoordsHeader extends LayoutCoordsHeader {

    private BattleMapCoords coords;
    
    public LayoutBattleCoordsHeader(AbstractLayoutPanel panel, int gridX, int gridY) {
        super(panel, gridX, gridY, false);
    }
    
    public void setBattleMapCoords(BattleMapCoords coords) {
        this.coords = coords;
    }

    @Override
    protected String getNewTitleText(int x, int y, boolean showIndex) {
        if (coords == null) return null;
        if (x < 0 || y < 0) {
            return "Map : ( - ) - Battle : ( - )";
        } else {
            String text = String.format("Map : (%d, %d)", x, y);            
            int bx = x-coords.getX();
            int by = y-coords.getY();
            if (bx < 0 || bx >= coords.getWidth() || by < 0 || by >= coords.getHeight()) {
                text += " - Battle : ( - )";
            } else {
                text += String.format(" - Battle : (%d, %d)", bx, by);
            }
            return text;
        }
    }
}
