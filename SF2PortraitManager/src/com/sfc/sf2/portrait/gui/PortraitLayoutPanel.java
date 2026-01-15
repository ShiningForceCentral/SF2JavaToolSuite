/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent.GridMousePressedEvent;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.portrait.Portrait;
import static com.sfc.sf2.portrait.Portrait.PORTRAIT_TILES_FULL_WIDTH;
import static com.sfc.sf2.portrait.Portrait.PORTRAIT_TILES_HEIGHT;
import static com.sfc.sf2.portrait.Portrait.PORTRAIT_TILES_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author wiz
 */
public class PortraitLayoutPanel extends AbstractLayoutPanel {
    
    private Portrait portrait;
    
    private boolean blinking = false;
    private boolean speaking = false;
    
    private int selectedEyeTile = -1;
    private int selectedMouthTile = -1;
    private int regionIndicator;
    
    private ActionListener eyesChangedListener;
    private ActionListener mouthChangedListener;
        
    public PortraitLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/2);
        scale = new LayoutScale();
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, PORTRAIT_TILES_WIDTH*PIXEL_WIDTH, -1);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, false);
        coordsHeader = null;
        mouseInput = new LayoutMouseInput(this, this::onMouseButtonInput, PIXEL_WIDTH, PIXEL_HEIGHT);
        scroller = null;
    }

    @Override
    protected boolean hasData() {
        return portrait != null && portrait.getTileset() != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        int width = PORTRAIT_TILES_FULL_WIDTH*PIXEL_WIDTH;
        int height = PORTRAIT_TILES_HEIGHT*PIXEL_HEIGHT;
        return new Dimension(width, height);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        portrait.clearIndexedColorImage();
        graphics.drawImage(portrait.getIndexedColorImage(true, blinking, speaking), 0, 0, null);
        graphics.setColor(Color.YELLOW);
        if (selectedEyeTile >= 0 && selectedEyeTile < portrait.getEyeTiles().length) {
            int[] item = portrait.getEyeTiles()[selectedEyeTile];
            graphics.drawRect(item[0]*PIXEL_WIDTH, item[1]*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            graphics.drawRect(item[2]*PIXEL_WIDTH, item[3]*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        }
        if (selectedMouthTile >= 0 && selectedMouthTile < portrait.getMouthTiles().length) {
            int[] item = portrait.getMouthTiles()[selectedMouthTile];
            graphics.drawRect(item[0]*PIXEL_WIDTH, item[1]*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            graphics.drawRect(item[2]*PIXEL_WIDTH, item[3]*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        }
    }
    
    public Portrait getPortrait() {
        return portrait;
    }

    public void setPortrait(Portrait portrait) {
        this.portrait = portrait;
        redraw();
    }

    public int getSelectedEyeTile() {
        return selectedEyeTile;
    }

    public void setSelectedEyeTile(int selectedEyeTile) {
        this.selectedEyeTile = selectedEyeTile;
        this.selectedMouthTile = -1;
        redraw();
    }

    public int getSelectedMouthTile() {
        return selectedMouthTile;
    }

    public void setSelectedMouthTile(int selectedMouthTile) {
        this.selectedMouthTile = selectedMouthTile;
        this.selectedEyeTile = -1;
        redraw();
    }

    public boolean getBlinking() {
        return blinking;
    }

    public void setBlinking(boolean blinking) {
        if (this.blinking != blinking) {
            this.blinking = blinking;
            redraw();
        }
    }

    public boolean getSpeaking() {
        return speaking;
    }

    public void setSpeaking(boolean speaking) {
        if (this.speaking != speaking) {
            this.speaking = speaking;
            redraw();
        }
    }
    
    public void setEyesChangedListener(ActionListener eyesChangedListener) {
        this.eyesChangedListener = eyesChangedListener;
    }
    
    public void SetMouthChangedListener(ActionListener mouthChangedListener) {
        this.mouthChangedListener = mouthChangedListener;
    }
    
    private void onMouseButtonInput(GridMousePressedEvent evt) {
        if (evt.released()) {
            regionIndicator = 0;
            return;
        }
        int x = evt.x();
        int y = evt.y();
        if (x < 0 || x >= PORTRAIT_TILES_FULL_WIDTH || y < 0 || y >= PORTRAIT_TILES_HEIGHT) return;
        if (x < 6 && regionIndicator >= 1 || x >= 6 && regionIndicator <= -1) return; //Cannot drag between left/right regions
        regionIndicator = x < 6 ? -1 : 1;
        if (selectedEyeTile >= 0) {
            int[] item = portrait.getEyeTiles()[selectedEyeTile].clone();
            if (x < 6) {
                item[0] = x;
                item[1] = y;
            } else {
                item[2] = x;
                item[3] = y;
            }
            portrait.getEyeTiles()[selectedEyeTile] = item;
            eyesChangedListener.actionPerformed(new ActionEvent(item, selectedEyeTile, null));
            redraw();
            revalidate();
            repaint();

        } else if (selectedMouthTile >= 0) {
            int[] item = portrait.getMouthTiles()[selectedMouthTile].clone();
            if (x < 6) {
                item[0] = x;
                item[1] = y;
            } else {
                item[2] = x;
                item[3] = y;
            }
            portrait.getMouthTiles()[selectedMouthTile] = item;
            mouthChangedListener.actionPerformed(new ActionEvent(item, selectedMouthTile, null));
            redraw();
            revalidate();
            repaint();
        }
        //Console.logger().finest("Portrait press "+e.mouseButton()+" -- "+e.x()+" - "+e.y());
    }
}
