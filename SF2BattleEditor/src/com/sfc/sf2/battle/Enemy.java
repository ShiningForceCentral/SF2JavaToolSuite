/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import java.awt.Point;

/**
 *
 * @author wiz
 */
public class Enemy {
        
    private EnemyData enemyData;
    private int x;
    private int y;
    private String ai;
    private String item;
    private String itemFlags;
    private String moveOrder;
    private int moveOrderTarget;
    private int triggerRegion1;
    private int triggerRegion2;
    private String backupMoveOrder;
    private int backupMoveOrderTarget;
    private int unknown;
    private String spawnParams;

    public Enemy(EnemyData enemyData, int x, int y, String ai, String item, String itemFlags, String moveOrder, int moveOrderTarget, int triggerRegion1, int triggerRegion2, String backupMoveOrder, int backupMoveOrderTarget, int unknown, String spawnParams) {
        this.enemyData = enemyData;
        this.x = x;
        this.y = y;
        this.ai = ai;
        this.item = item;
        this.itemFlags = itemFlags;
        this.moveOrder = moveOrder;
        this.moveOrderTarget = moveOrderTarget;
        this.triggerRegion1 = triggerRegion1;
        this.triggerRegion2 = triggerRegion2;
        this.backupMoveOrder = backupMoveOrder;
        this.backupMoveOrderTarget = backupMoveOrderTarget;
        this.unknown = unknown;
        this.spawnParams = spawnParams;
    }

    public EnemyData getEnemyData() {
        return enemyData;
    }

    public void setEnemyData(EnemyData data) {
        this.enemyData = data;
    }

    public Point getPos() {
        return new Point(x, y);
    }

    public void setPos(Point pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemFlags() {
        return itemFlags;
    }

    public void setItemFlags(String itemFlags) {
        this.itemFlags = itemFlags;
    }

    public String getMoveOrder() {
        return moveOrder;
    }

    public void setMoveOrder(String moveOrder) {
        this.moveOrder = moveOrder;
    }

    public int getMoveOrderTarget() {
        return moveOrderTarget;
    }

    public void setMoveOrderTarget(int moveOrderTarget) {
        this.moveOrderTarget = moveOrderTarget;
    }

    public int getTriggerRegion1() {
        return triggerRegion1;
    }

    public void setTriggerRegion1(int triggerRegion1) {
        this.triggerRegion1 = triggerRegion1;
    }

    public int getTriggerRegion2() {
        return triggerRegion2;
    }

    public void setTriggerRegion2(int triggerRegion2) {
        this.triggerRegion2 = triggerRegion2;
    }

    public String getBackupMoveOrder() {
        return backupMoveOrder;
    }

    public void setBackupMoveOrder(String backupMoveOrder) {
        this.backupMoveOrder = backupMoveOrder;
    }

    public int getBackupMoveOrderTarget() {
        return backupMoveOrderTarget;
    }

    public void setBackupMoveOrderTarget(int backupMoveOrderTarget) {
        this.backupMoveOrderTarget = backupMoveOrderTarget;
    }

    public int getUnknown() {
        return unknown;
    }

    public void setUnknown(int byte10) {
        this.unknown = byte10;
    }

    public String getSpawnParams() {
        return spawnParams;
    }

    public void setSpawnParams(String spawnParams) {
        this.spawnParams = spawnParams;
    }

    @Override
    public Enemy clone() {
        return new Enemy(enemyData, x, y, ai, item, itemFlags, moveOrder, moveOrderTarget, triggerRegion1, triggerRegion2, backupMoveOrder, backupMoveOrderTarget, unknown, spawnParams);
    }
    
    public static Enemy emptyEnemy() {
        return new Enemy(null, 0, 0, null, null, null, null, -1, 15, 15, null, -1, 0, null);
    }
}
