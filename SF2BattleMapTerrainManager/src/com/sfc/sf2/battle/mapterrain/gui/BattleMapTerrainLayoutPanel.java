/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.gui;

import static com.sfc.sf2.battle.mapcoords.BattleMapCoords.BATTLE_SIZE;
import com.sfc.sf2.battle.mapcoords.gui.BattleMapCoordsLayoutPanel;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.actions.TerrainChangeActionData;
import com.sfc.sf2.battle.mapterrain.gui.TerrainKeyPanel.TerrainDrawMode;
import com.sfc.sf2.battle.mapterrain.gui.resources.BattleTerrainIcons;
import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.CumulativeAction;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent.GridMousePressedEvent;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public class BattleMapTerrainLayoutPanel extends BattleMapCoordsLayoutPanel {
    
    private BattleMapTerrain terrain;
    private boolean drawTerrain;
    private TerrainDrawMode terrainDrawMode;
    
    private byte selectedTerrainType = -1;
    
    public BattleMapTerrainLayoutPanel() {
        super();
        mouseInput = new LayoutMouseInput(this, this::onMouseButtonInput, PIXEL_WIDTH, PIXEL_HEIGHT);
    }
    
    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        if (!drawTerrain) return;
        
        byte[] data = terrain.getData();
        int coordsX = battleCoords.getX();
        int coordsY = battleCoords.getY();
        int width = battleCoords.getWidth();
        int height = battleCoords.getHeight();
        
        graphics.setColor(BattleTerrainIcons.TERRAIN_DARKEN);
        graphics.fillRect(coordsX*PIXEL_WIDTH, coordsY*PIXEL_HEIGHT, width*PIXEL_WIDTH, height*PIXEL_HEIGHT);
        switch (terrainDrawMode) {
            case Icons:
                drawTerrainIcons(graphics, data, coordsX, coordsY, width, height);
                break;
            case Colors:
                drawTerrainColors(graphics, data, coordsX, coordsY, width, height);
                break;
            case Numbers:
                drawTerrainText(graphics, data, coordsX, coordsY, width, height);
                break;
        }
    }
    
    private void drawTerrainIcons(Graphics graphics, byte[] data, int coordsX, int coordsY, int width, int height) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                int x = (coordsX+i)*PIXEL_WIDTH;
                int y = (coordsY+j)*PIXEL_HEIGHT;
                int value = data[i+j*48];
                graphics.setColor(BattleTerrainIcons.getBGColor(value));
                graphics.fillRect(x+6, y+6, PIXEL_WIDTH-12, PIXEL_HEIGHT-12);
                graphics.drawImage(BattleTerrainIcons.getTerrainIcon(value).getImage(), x+8, y+8, null);
            }
        }
    }
    
    private void drawTerrainColors(Graphics graphics, byte[] data, int coordsX, int coordsY, int width, int height) {
        graphics.setColor(BattleTerrainIcons.TERRAIN_BG);
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                int x = (coordsX+i)*PIXEL_WIDTH;
                int y = (coordsY+j)*PIXEL_HEIGHT;
                int value = data[i+j*48];
                graphics.setColor(BattleTerrainIcons.getBGColor(value));
                graphics.fillRect(x+6, y+6, PIXEL_WIDTH-12, PIXEL_HEIGHT-12);
                graphics.setColor(BattleTerrainIcons.getTerrainTextColor(value));
                graphics.fillRect(x+8, y+8, PIXEL_WIDTH-16, PIXEL_HEIGHT-16);
            }
        }
    }
    
    private void drawTerrainText(Graphics graphics, byte[] data, int coordsX, int coordsY, int width, int height) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                int x = (coordsX+i)*PIXEL_WIDTH;
                int y = (coordsY+j)*PIXEL_HEIGHT;
                int value = data[i+j*48];
                graphics.setColor(BattleTerrainIcons.getBGColor(value));
                graphics.fillRect(x+6, y+5, PIXEL_WIDTH-12, PIXEL_HEIGHT-11);
                graphics.setColor(BattleTerrainIcons.getTerrainTextColor(value));
                if (value < 0) {
                    graphics.drawString(Integer.toString(value), x+8, y+16);
                } else if (value >= 10) {
                    graphics.drawString(Integer.toString(value), x+4, y+16);
                } else {
                    graphics.drawString(Integer.toString(value), x+9, y+16);
                }
            }
        }
    }

    public BattleMapTerrain getTerrain() {
        return terrain;
    }

    public void setTerrain(BattleMapTerrain terrain) {
        this.terrain = terrain;
        redraw();
    }

    public boolean isDrawTerrain() {
        return drawTerrain;
    }

    public void setDrawTerrain(boolean drawTerrain) {
        if (this.drawTerrain != drawTerrain) {
            this.drawTerrain = drawTerrain;
            redraw();
        }
    }

    public void setTerrainDrawMode(TerrainDrawMode terrainDrawMode) {
        if (this.terrainDrawMode != terrainDrawMode) {
            this.terrainDrawMode = terrainDrawMode;
            redraw();
        }
    }

    public void setSelectedTerrainType(int selectedTerrainType) {
        this.selectedTerrainType = (byte)(selectedTerrainType);
    }
    
    protected void onMouseButtonInput(GridMousePressedEvent evt) {
        if (evt.released()) return;
        if (evt.mouseButton() != MouseEvent.BUTTON1) return;
        if (battleCoords == null) return;
        int x = evt.x();
        int y = evt.y();
        int startX = battleCoords.getX();
        int startY = battleCoords.getY();
        int width = battleCoords.getWidth();
        int height = battleCoords.getHeight();
        if (((x < startX || x > startX+width) || y < startY) || y > startY+height) {
            return;
        }
        x -= startX;
        y -=startY;
        int index = x+y*BATTLE_SIZE;
        if (terrain.getData()[index] != selectedTerrainType) {
            TerrainChangeActionData newValue = new TerrainChangeActionData(terrain, selectedTerrainType, index);
            TerrainChangeActionData oldValue = new TerrainChangeActionData(terrain, terrain.getData()[index], index);
            ActionManager.setAndExecuteAction(new CumulativeAction<TerrainChangeActionData>(this, "Terrain Chainge", this::actionTerrainChanged, newValue, oldValue));
        }
    }
    
    private void actionTerrainChanged(List<TerrainChangeActionData> values) {
        for (TerrainChangeActionData value : values) {
            value.terrainData().getData()[value.index()] = value.terrain();
        }
        redraw();
    }
}
