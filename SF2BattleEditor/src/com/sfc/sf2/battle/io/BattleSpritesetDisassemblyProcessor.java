/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.io;

import com.sfc.sf2.battle.AIPoint;
import com.sfc.sf2.battle.AIRegion;
import com.sfc.sf2.battle.Ally;
import com.sfc.sf2.battle.BattleSpriteset;
import com.sfc.sf2.battle.Enemy;
import com.sfc.sf2.battle.EnemyData;
import com.sfc.sf2.battle.EnemyEnums;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.helpers.BinaryHelpers;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class BattleSpritesetDisassemblyProcessor extends AbstractDisassemblyProcessor<BattleSpriteset, BattleSpritesetPackage> {

    @Override
    protected BattleSpriteset parseDisassemblyData(byte[] data, BattleSpritesetPackage pckg) throws DisassemblyException {            
        int alliesNumber = data[0];
        int enemiesNumber = data[1];
        int aiRegionsNumber = data[2];
        int aiPointsNumber = data[3];

        ArrayList<Ally> allyList = new ArrayList();
        for (int i=0; i < alliesNumber; i++) {
            int dataPointer = 4+i*12;
            int index = data[dataPointer+0];
            int x = data[dataPointer+1];
            int y = data[dataPointer+2];
            while (allyList.size() < index) { allyList.add(null); }
            allyList.add(new Ally(x, y));
        }
        Ally[] allies = new Ally[allyList.size()];
        allies = allyList.toArray(allies);
        allyList.clear();

        ArrayList<Enemy> enemyList = new ArrayList();
        for (int i=0; i < enemiesNumber; i++) {
            int dataPointer = 4+alliesNumber*12+i*12;
            int index = data[dataPointer+0]&0xFF;
            EnemyData enemyData = null;
            if (index < pckg.enemyData().length && pckg.enemyData()[index] != null)
                enemyData = pckg.enemyData()[index];
            int x = data[dataPointer+1];
            int y = data[dataPointer+2];
            String ai = EnemyEnums.toEnumString(data[dataPointer+3], pckg.enemyEnums().getCommandSets());
            String item = EnemyEnums.itemNumToItemString(BinaryHelpers.getWord(data,dataPointer+4), pckg.enemyEnums().getItems());
            String itemFlags = EnemyEnums.itemNumToItemFlagsString(BinaryHelpers.getWord(data,dataPointer+4), pckg.enemyEnums().getItemFlags());
            String order1 = EnemyEnums.aiOrderNumToString(data[dataPointer+6], pckg.enemyEnums().getOrders());
            int target1 = (byte)(data[dataPointer+6]&0x0F);
            int region1 = data[dataPointer+7];
            String order2 = EnemyEnums.aiOrderNumToString(data[dataPointer+8], pckg.enemyEnums().getOrders());
            int target2 = (byte)(data[dataPointer+8]&0x0F);
            int region2 = data[dataPointer+9];
            int byte10 = data[dataPointer+10];
            String spawn = EnemyEnums.toEnumString(data[dataPointer+11], pckg.enemyEnums().getSpawnParams());
            while (enemyList.size() < index) { enemyList.add(null); }
            enemyList.add(new Enemy(enemyData, x, y, ai, item, itemFlags, order1, target1, region1, region2, order2, target2, byte10, spawn));
        }
        Enemy[] enemies = new Enemy[enemyList.size()];
        enemies = enemyList.toArray(enemies);
        enemyList.clear();

        ArrayList<AIRegion> aiRegionList = new ArrayList();
        for (int i=0; i < aiRegionsNumber; i++) {
            int dataPointer = 4+alliesNumber*12+enemiesNumber*12+i*12*12;
            int type = data[dataPointer+0];
            Point[] points = new Point[4];
            for (int p = 0; p < points.length; p++) {
                points[p].x = data[dataPointer+2+p*2];
                points[p].y = data[dataPointer+3+p*2];
            }
            aiRegionList.add(new AIRegion(type, points));
        }
        AIRegion[] aiRegions = new AIRegion[aiRegionList.size()];
        aiRegions = aiRegionList.toArray(aiRegions);
        aiRegionList.clear();

        ArrayList<AIPoint> aiPointList = new ArrayList();
        for (int i=0; i < aiPointsNumber; i++) {
            int dataPointer = 4+alliesNumber*12+enemiesNumber*12+aiRegionsNumber*12+i*2;
            int x = data[dataPointer+0];
            int y = data[dataPointer+1];
            aiPointList.add(new AIPoint(x, y));
        }
        AIPoint[] aiPoints = new AIPoint[aiPointList.size()];
        aiPoints = aiPointList.toArray(aiPoints);
        aiPointList.clear();

        return new BattleSpriteset(pckg.index(), allies, enemies, aiRegions, aiPoints);
    }

    @Override
    protected byte[] packageDisassemblyData(BattleSpriteset item, BattleSpritesetPackage pckg) throws DisassemblyException {
        Ally[] allies = item.getAllies();
        Enemy[] enemies = item.getEnemies();
        AIRegion[] aiRegions = item.getAiRegions();
        AIPoint[] aiPoints = item.getAiPoints();
        
        int alliesNumber = allies.length;
        int enemiesNumber = enemies.length;
        int aiRegionsNumber = aiRegions.length;
        int aiPointsNumber = aiPoints.length;
        
        byte[] spritesetBytes = new byte[4+alliesNumber*12+enemiesNumber*12+aiRegionsNumber*12+aiPointsNumber*2];
        
        spritesetBytes[0] = (byte)alliesNumber;
        spritesetBytes[1] = (byte)enemiesNumber;
        spritesetBytes[2] = (byte)aiRegionsNumber;
        spritesetBytes[3] = (byte)aiPointsNumber;
        
        for (int i=0; i < alliesNumber; i++) {
            Ally ally = allies[i];
            spritesetBytes[4+i*12+0] = (byte)i;
            spritesetBytes[4+i*12+1] = (byte)ally.getX();
            spritesetBytes[4+i*12+2] = (byte)ally.getY();
        }
        
        for (int i=0; i < enemiesNumber; i++) {
            Enemy enemy = enemies[i];
            short itemData = EnemyEnums.itemStringToNum(enemy.getItem(), pckg.enemyEnums().getItems());
            itemData += EnemyEnums.itemStringToNum(enemy.getItemFlags(), pckg.enemyEnums().getItemFlags());
            spritesetBytes[4+alliesNumber*12+i*12+0] = (byte)(enemy.getEnemyData().getID()&0xFF);
            spritesetBytes[4+alliesNumber*12+i*12+1] = (byte)enemy.getX();
            spritesetBytes[4+alliesNumber*12+i*12+2] = (byte)enemy.getY();
            spritesetBytes[4+alliesNumber*12+i*12+3] = EnemyEnums.toEnumByte(enemy.getAi(), pckg.enemyEnums().getCommandSets());
            spritesetBytes[4+alliesNumber*12+i*12+4] = (byte)(itemData>>8);
            spritesetBytes[4+alliesNumber*12+i*12+5] = (byte)(itemData&0xFF);
            spritesetBytes[4+alliesNumber*12+i*12+6] = (byte)(EnemyEnums.aiOrderStringToNum(enemy.getMoveOrder(), pckg.enemyEnums().getOrders()) + enemy.getMoveOrderTarget());
            spritesetBytes[4+alliesNumber*12+i*12+7] = (byte)enemy.getTriggerRegion1();
            spritesetBytes[4+alliesNumber*12+i*12+8] = (byte)(EnemyEnums.aiOrderStringToNum(enemy.getBackupMoveOrder(), pckg.enemyEnums().getOrders()) + enemy.getBackupMoveOrderTarget());
            spritesetBytes[4+alliesNumber*12+i*12+9] = (byte)enemy.getTriggerRegion2();
            spritesetBytes[4+alliesNumber*12+i*12+10] = (byte)enemy.getUnknown();
            spritesetBytes[4+alliesNumber*12+i*12+11] = EnemyEnums.toEnumByte(enemy.getSpawnParams(), pckg.enemyEnums().getSpawnParams());
        }
        
        for (int i=0; i < aiRegionsNumber; i++) {
            AIRegion aiRegion = aiRegions[i];
            spritesetBytes[4+alliesNumber*12+enemiesNumber*12+i*12+0] = (byte)aiRegion.getType();
            Point[] points = aiRegion.getPoints();
            for (int p = 0; p < points.length; p++) {
                spritesetBytes[4+alliesNumber*12+enemiesNumber*12+i*12+2+p*2] = (byte)points[p].x;
                spritesetBytes[4+alliesNumber*12+enemiesNumber*12+i*12+3+p*2] = (byte)points[p].y;
            }
        }
        
        for (int i=0; i < aiPointsNumber; i++) {
            AIPoint aiPoint = aiPoints[i];
            spritesetBytes[4+alliesNumber*12+enemiesNumber*12+aiRegionsNumber*12+i*2+0] = (byte)aiPoint.getX();
            spritesetBytes[4+alliesNumber*12+enemiesNumber*12+aiRegionsNumber*12+i*2+1] = (byte)aiPoint.getY();
        }

        return spritesetBytes;
    }
}
