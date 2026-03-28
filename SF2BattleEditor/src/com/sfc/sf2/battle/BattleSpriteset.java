/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import java.awt.Point;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class BattleSpriteset {
    private int index;
    private Ally[] allies;
    private Enemy[] enemies;
    private AIRegion[] aiRegions;
    private AIPoint[] aiPoints;

    public BattleSpriteset(int index, Ally[] allies, Enemy[] enemies, AIRegion[] aiRegions, AIPoint[] aiPoints) {
        this.index = index;
        this.allies = allies;
        this.enemies = enemies;
        this.aiRegions = aiRegions;
        this.aiPoints = aiPoints;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Ally[] getAllies() {
        return allies;
    }

    public void setAllies(Ally[] allies) {
        this.allies = allies;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
    }

    public AIRegion[] getAiRegions() {
        return aiRegions;
    }

    public void setAiRegions(AIRegion[] aiRegions) {
        this.aiRegions = aiRegions;
    }

    public AIPoint[] getAiPoints() {
        return aiPoints;
    }

    public void setAiPoints(AIPoint[] aiPoints) {
        this.aiPoints = aiPoints;
    }
    
    public void shiftPositions(int xShift, int yShift) {
        for (int i = 0; i < allies.length; i++) {
            allies[i].setX(allies[i].getX()+xShift);
            allies[i].setY(allies[i].getY()+yShift);
        }
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].setX(enemies[i].getX()+xShift);
            enemies[i].setY(enemies[i].getY()+yShift);
        }
        for (int i = 0; i < aiRegions.length; i++) {
            Point[] points = aiRegions[i].getPoints();
            for (int p = 0; p < points.length; p++) {
                points[p].x += xShift;
                points[p].y += yShift;
            }
        }
        for (int i = 0; i < aiPoints.length; i++) {
            aiPoints[i].setX(aiPoints[i].getX()+xShift);
            aiPoints[i].setY(aiPoints[i].getY()+yShift);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BattleSpriteset)) return super.equals(obj);
        BattleSpriteset other = (BattleSpriteset)obj;
        if (this.index != other.index) return false;
        if (!Arrays.equals(this.allies, other.allies)) return false;
        if (!Arrays.equals(this.enemies, other.enemies)) return false;
        if (!Arrays.equals(this.aiRegions, other.aiRegions)) return false;
        if (!Arrays.equals(this.aiPoints, other.aiPoints)) return false;
        return true;
    }
}
