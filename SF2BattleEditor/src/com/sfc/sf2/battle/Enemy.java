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
    public boolean equals(Object obj) {
        if (!(obj instanceof Enemy)) return super.equals(obj);
        Enemy other = (Enemy)obj;
        if (this.enemyData == null ? other.enemyData != null : !this.enemyData.equals(other.enemyData)) return false;
        if (this.x != other.x) return false;
        if (this.y != other.y) return false;
        if (this.ai == null ? other.ai != null : !this.ai.equals(other.ai)) return false;
        if (this.item == null ? other.item != null : !this.item.equals(other.item)) return false;
        if (this.itemFlags == null ? other.itemFlags != null : !this.itemFlags.equals(other.itemFlags)) return false;
        if (this.moveOrder == null ? other.moveOrder != null : !this.moveOrder.equals(other.moveOrder)) return false;
        if (this.moveOrderTarget != other.moveOrderTarget) return false;
        if (this.triggerRegion1 != other.triggerRegion1) return false;
        if (this.triggerRegion2 != other.triggerRegion2) return false;
        if (this.backupMoveOrder == null ? other.backupMoveOrder != null : !this.backupMoveOrder.equals(other.backupMoveOrder)) return false;
        if (this.backupMoveOrderTarget != other.backupMoveOrderTarget) return false;
        if (this.unknown != other.unknown) return false;
        if (this.spawnParams == null ? other.spawnParams != null : !this.spawnParams.equals(other.spawnParams)) return false;
        return true;
    }

    @Override
    public Enemy clone() {
        return new Enemy(enemyData, x, y, ai, item, itemFlags, moveOrder, moveOrderTarget, triggerRegion1, triggerRegion2, backupMoveOrder, backupMoveOrderTarget, unknown, spawnParams);
    }
    
    public static Enemy emptyEnemy(EnemyData[] enemyData, EnemyEnums enemyEnums) {
        String ai = (String)enemyEnums.getCommandSets().keySet().toArray()[0];
        Object[] itemsArray = enemyEnums.getItems().keySet().toArray();
        String item = (String)itemsArray[itemsArray.length-1];  //NOTHING
        Object[] ordersArray = enemyEnums.getOrders().keySet().toArray();
        String order = (String)ordersArray[ordersArray.length-1];  //NONE
        String spawn = (String)enemyEnums.getSpawnParams().keySet().toArray()[0];
        return new Enemy(enemyData[0], 2, 2, ai, item, null, order, 0, 15, 15, order, 0, 0, spawn);
    }
}
