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
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class BattleSpritesetAsmProcessor extends AbstractAsmProcessor<BattleSpriteset, BattleSpritesetPackage> {

    private static final String MACRO_LIST_START = "BattleSpriteset";
    private static final String MACRO_DCB = "dc.b";
    private static final String MACRO_ALLIES = "allyCombatant";
    private static final String MACRO_ENEMIES = "enemyCombatant";
    private static final String MACRO_ENEMY_LINE2 = "combatantAiAndItem";
    private static final String MACRO_ENEMY_LINE3 = "combatantBehavior";

    @Override
    protected BattleSpriteset parseAsmData(BufferedReader reader, BattleSpritesetPackage pckg) throws IOException, AsmException {
        ArrayList<Ally> allyList = new ArrayList();
        ArrayList<Enemy> enemyList = new ArrayList();
        ArrayList<AIRegion> aiRegionList = new ArrayList();
        ArrayList<AIPoint> aiPointList = new ArrayList();

        boolean inHeader = true;
        boolean parsedSizes = false;
        String line;
        while ((line = reader.readLine()) != null) {
            if (parsedSizes && line.trim().startsWith(MACRO_DCB)) {
                String[] params = line.trim().substring(MACRO_DCB.length()).trim().split(",");
                if (params.length == 2) {
                    //Is an AI Point
                    int x = 0, y = 0;
                    if (params.length == 2) {
                        x = StringHelpers.getValueInt(params[0].trim());
                        y = StringHelpers.getValueInt(params[1].trim());
                        aiPointList.add(new AIPoint(x, y));
                    }
                } else if (params.length == 1) {
                    //Is an AI Region
                    int type = 0;
                    //Line 1
                    type = StringHelpers.getValueInt(params[0].trim());
                    //Line 2 (Ignore)
                    Point[] points = new Point[4];
                    reader.readLine();
                    for (int i = 0; i < points.length; i++) {
                    //Lines 3,4,5,6
                        if ((line = reader.readLine()) != null) {
                            params = line.trim().substring(MACRO_DCB.length()).trim().split(",");
                            points[i] = new Point();
                            points[i].x = StringHelpers.getValueInt(params[0].trim());
                            points[i].y = StringHelpers.getValueInt(params[1].trim());
                        }
                    }
                    //Line 7, 8 & 9 (Ignore)
                    //NOTE: Some files erroniously separate line 8 with an empty line (between 7 & 8) so skipping line 9 cathes both cases
                    reader.readLine();
                    reader.readLine();
                    reader.readLine();

                    aiRegionList.add(new AIRegion(type, points));
                }
            } else if (line.trim().startsWith(MACRO_ALLIES)) {
                inHeader = false;
                parsedSizes = true;
                /*
                index (0 to $B), X, Y
                Unused, Unused, Unused
                Unused, Unused, Unused, Unused, Unused, Unused, Unused
                 */
                String[] params = line.trim().substring(MACRO_ALLIES.length()).trim().split(",");
                int index = StringHelpers.getValueInt(params[0].trim());
                int x = StringHelpers.getValueInt(params[1].trim());
                int y = StringHelpers.getValueInt(params[2].trim());
                while (allyList.size() < index) { allyList.add(null); }
                allyList.add(new Ally(x, y));

                //AI and behaviour lines not relevant for allies, so skip them
                reader.readLine();
                reader.readLine();
            } else if (line.trim().startsWith(MACRO_ENEMIES)) {
                inHeader = false;
                parsedSizes = true;
                /*
                index, X, Y
                aiType, extraItem
                moveOrder1, region1, moveOrder2, region2, unknown, spawnParams
                 */
                String[] params = line.trim().substring(MACRO_ENEMIES.length()).trim().split(",");
                int x = 0, y = 0, target1 = -1, target2 = -1, region1 = 15, region2 = 15, unknownParam = 0;
                String name, aiCommand = null, item = null, itemFlags = null, moveOrder1 = null, moveOrder2 = null, spawnParams = null;

                //Line 1
                name = params[0].trim();
                x = StringHelpers.getValueInt(params[1].trim());
                y = StringHelpers.getValueInt(params[2].trim());

                //Line 2
                if ((line = reader.readLine()) != null) {
                    params = line.trim().substring(MACRO_ENEMY_LINE2.length()).trim().split(",");
                    aiCommand = params[0].trim();
                    params[1] = params[1].trim();
                    int flagsIndex = params[1].indexOf('|');
                    if (flagsIndex >= 0) {
                        item = params[1].substring(0, flagsIndex);
                        itemFlags = params[1].substring(flagsIndex+1);
                    } else {
                        item = params[1];
                        itemFlags = null;
                    }
                }

                //Line 3
                if ((line = reader.readLine()) != null) {
                    params = line.trim().substring(MACRO_ENEMY_LINE3.length()).trim().split(",");
                    params[0] = params[0].trim();
                    int orderIndex = params[0].indexOf('|');
                    if (orderIndex >= 0) {
                        moveOrder1 = params[0].substring(0, orderIndex);
                        target1 = StringHelpers.getValueInt(params[0].substring(orderIndex+1));
                    } else {
                        moveOrder1 = params[0];
                        target1 = 0;
                    }
                    region1 = StringHelpers.getValueInt(params[1].trim());
                    region2 = StringHelpers.getValueInt(params[3].trim());
                    params[2] = params[2].trim();
                    orderIndex = params[2].indexOf('|');
                    if (orderIndex >= 0) {
                        moveOrder2 = params[2].substring(0, orderIndex);
                        target2 = StringHelpers.getValueInt(params[2].substring(orderIndex+1));
                    } else {
                        moveOrder2 = params[2];
                        target2 = 0;
                    }
                    unknownParam = StringHelpers.getValueInt(params[4].trim());
                    spawnParams = params[5].trim();
                }

                EnemyData enemyData = null;
                if (pckg.enemyEnums().getEnemies().containsKey(name)) {
                    int index = pckg.enemyEnums().getEnemies().get(name);
                    if (index >= 0 && index < pckg.enemyData().length)
                    enemyData = pckg.enemyData()[index];
                }
                if (enemyData == null) {
                    EnemyData placeholder = new EnemyData(-1, name, null, false);
                    enemyData = placeholder;
                }
                enemyList.add(new Enemy(enemyData, x, y, aiCommand, item, itemFlags, moveOrder1, target1, region1, region2, moveOrder2, target2, unknownParam, spawnParams));
            } else if (inHeader) {
                if (line.trim().startsWith(MACRO_LIST_START)) {
                    inHeader = false;
                }
            }
        }

        Ally[] allies = new Ally[allyList.size()];
        allies = allyList.toArray(allies);
        allyList.clear();

        Enemy[] enemies = new Enemy[enemyList.size()];
        enemies = enemyList.toArray(enemies);
        enemyList.clear();

        AIRegion[] aiRegions = new AIRegion[aiRegionList.size()];
        aiRegions = aiRegionList.toArray(aiRegions);
        aiRegionList.clear();

        AIPoint[] aiPoints = new AIPoint[aiPointList.size()];
        aiPoints = aiPointList.toArray(aiPoints);
        aiPointList.clear();
        
        return new BattleSpriteset(pckg.index(), allies, enemies, aiRegions, aiPoints);
    }

    @Override
    protected String getHeaderName(BattleSpriteset item, BattleSpritesetPackage pckg) {
        return String.format("Battle Spritesets %02d", item.getIndex());
    }

    @Override
    protected void packageAsmData(FileWriter writer, BattleSpriteset item, BattleSpritesetPackage pckg) throws IOException, AsmException {
        Ally[] allies = item.getAllies();
        Enemy[] enemies = item.getEnemies();
        AIRegion[] aiRegions = item.getAiRegions();
        AIPoint[] aiPoints = item.getAiPoints();

        writer.write(String.format("BattleSpriteset%02d:\n", pckg.index()));
        //Sizes
        writer.write("\t\t\t\t; # Allies\n");
        writer.write(String.format("\t\t\t\t%s %d\n", MACRO_DCB, allies.length));
        writer.write("\t\t\t\t; # Enemies\n");
        writer.write(String.format("\t\t\t\t%s %d\n", MACRO_DCB, enemies.length));
        writer.write("\t\t\t\t; # AI Regions\n");
        writer.write(String.format("\t\t\t\t%s %d\n", MACRO_DCB, aiRegions.length));
        writer.write("\t\t\t\t; # AI Points\n");
        writer.write(String.format("\t\t\t\t%s %d\n", MACRO_DCB, aiPoints.length));
        writer.write("\n");

        //Allies
        writer.write("\t\t\t\t; Allies\n");
        for (int i=0; i < allies.length; i++) {
            Ally ally = allies[i];
            writer.write(String.format("\t\t\t\t%s %d, %d, %d\n", MACRO_ALLIES, i, ally.getX(), ally.getY()));
            writer.write(String.format("\t\t\t\t%s HEALER1, NOTHING\n", MACRO_ENEMY_LINE2));
            writer.write(String.format("\t\t\t\t%s NONE, 15, NONE, 15, 0, STARTING\n\n", MACRO_ENEMY_LINE3));
        }
        writer.write("\n");

        //Enemies
        writer.write("\t\t\t\t; Enemies\n");
        for (int i=0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];

            String name = enemy.getEnemyData().getName();
            String command = enemy.getAi();
            String itemData = enemy.getItem();
            if (!itemData.equals("NOTHING")) itemData = itemData+"|"+enemy.getItemFlags();
            String moveOrder1 = enemy.getMoveOrder();
            if (!moveOrder1.equals("NONE")) moveOrder1 = moveOrder1+"|"+enemy.getMoveOrderTarget();
            String moveOrder2 = enemy.getBackupMoveOrder();
            if (!moveOrder2.equals("NONE")) moveOrder2 = moveOrder1+"|"+enemy.getBackupMoveOrderTarget();
            String spawnParams = enemy.getSpawnParams();

            writer.write(String.format("\t\t\t\t%s %s, %d, %d\n", MACRO_ENEMIES, name, enemy.getX(), enemy.getY()));
            writer.write(String.format("\t\t\t\t%s %s, %s\n", MACRO_ENEMY_LINE2, command, itemData));
            writer.write(String.format("\t\t\t\t%s %s, %d, %s, %d, %d, %s\n\n", MACRO_ENEMY_LINE3, moveOrder1, enemy.getTriggerRegion1(), moveOrder2, enemy.getTriggerRegion2(), enemy.getUnknown(), spawnParams));
        }
        writer.write("\n");

        //Regions
        writer.write("\t\t\t\t; AI Regions\n");
        for (int i=0; i < aiRegions.length; i++) {
            AIRegion region = aiRegions[i];
            writer.write(String.format("\t\t\t\t%s %d\n", MACRO_DCB, region.getType()));
            writer.write(String.format("\t\t\t\t%s 0\n", MACRO_DCB));
            Point[] points = region.getPoints();
            for (int p = 0; p < points.length; p++) {
                writer.write(String.format("\t\t\t\t%s %d, %d\n", MACRO_DCB, points[p].x, points[p].y));
            }
            writer.write(String.format("\t\t\t\t%s 0\n", MACRO_DCB));
            writer.write(String.format("\t\t\t\t%s 0\n\n", MACRO_DCB));
        }
        writer.write("\n");

        if (aiPoints != null && aiPoints.length > 0) {
            //AI Points
            writer.write("\t\t\t\t; AI Points\n");
            for (int i=0; i < aiPoints.length; i++) {
                AIPoint point = aiPoints[i];
                writer.write(String.format("\t\t\t\t%s %d, %d\n", MACRO_DCB, point.getX(), point.getY()));
            }
            writer.write("\n");
        }
    }
}
