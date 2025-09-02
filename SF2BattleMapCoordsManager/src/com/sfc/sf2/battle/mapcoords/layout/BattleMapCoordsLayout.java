/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapcoords.layout;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.map.layout.MapLayout;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.gui.StaticMapLayoutPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author wiz
 */
public class BattleMapCoordsLayout extends StaticMapLayoutPanel {
    
    protected BattleMapCoords battleCoords;
    protected boolean showBattleCoords = true;
    
    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.YELLOW);
        int width = battleCoords.getWidth();
        int height = battleCoords.getHeight();
        g2.drawRect(battleCoords.getX()*PIXEL_WIDTH, battleCoords.getY()*PIXEL_HEIGHT, width*PIXEL_WIDTH, height*PIXEL_HEIGHT);
        g2.setColor(Color.WHITE);
        int trigX = battleCoords.getTrigX();
        int trigY = battleCoords.getTrigY();
        if (trigX < BLOCK_WIDTH || trigY < BLOCK_HEIGHT) {
            width = 1;
            height = 1;
            if (trigX >= BLOCK_WIDTH) {
                trigX = 0;
                width = BLOCK_WIDTH;
            } else if (trigY >= BLOCK_HEIGHT) {
                trigY = 0;
                height = BLOCK_HEIGHT;
            }
            g2.drawRect(trigX*PIXEL_WIDTH, trigY*PIXEL_HEIGHT, width*PIXEL_WIDTH, height*PIXEL_HEIGHT);
        }
    }

    public BattleMapCoords getBattleCoords() {
        return battleCoords;
    }

    public void setBattleCoords(BattleMapCoords battleCoords) {
        this.battleCoords = battleCoords;
        redraw();
    }

    public boolean getShowBattleCoords() {
        return showBattleCoords;
    }

    public void setShowBattleCoords(boolean showBattleCoords) {
        this.showBattleCoords = showBattleCoords;
        redraw();
    }
}
