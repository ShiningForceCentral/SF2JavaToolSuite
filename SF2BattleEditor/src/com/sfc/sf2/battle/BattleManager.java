/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import com.sfc.sf2.battle.io.BattleSpritesetAsmProcessor;
import com.sfc.sf2.battle.io.BattleSpritesetEntriesAsmProcessor;
import com.sfc.sf2.battle.io.BattleSpritesetPackage;
import com.sfc.sf2.battle.io.EnemyEnumsAsmProcessor;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.battle.mapcoords.io.BattleMapCoordsAsmProcessor;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrainManager;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.core.io.asm.EntriesAsmData;
import com.sfc.sf2.core.io.asm.EntriesAsmProcessor;
import com.sfc.sf2.core.io.asm.SF2EnumsAsmData;
import com.sfc.sf2.core.io.asm.SF2EnumsAsmProcessor;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.mapsprite.MapSpriteManager;
import com.sfc.sf2.mapsprite.io.EnemyMapspriteAsmProcessor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.specialSprites.SpecialSpriteManager;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author wiz
 */
public class BattleManager extends AbstractManager {
    private final PaletteManager paletteManager = new PaletteManager();
    private final BattleMapTerrainManager mapTerrainManager = new BattleMapTerrainManager();
    private final MapSpriteManager mapspriteManager = new MapSpriteManager();
    private final SpecialSpriteManager specialSpriteManager = new SpecialSpriteManager();
    private final BattleSpritesetAsmProcessor battleSpritesetAsmProcessor = new BattleSpritesetAsmProcessor();
    private final BattleSpritesetEntriesAsmProcessor battleSpritesetEntriesProcessor = new BattleSpritesetEntriesAsmProcessor();
    private final BattleMapCoordsAsmProcessor battleCoordsAsmProcessor = new BattleMapCoordsAsmProcessor();
    private final EnemyEnumsAsmProcessor enemyEnumsAsmProcessor = new EnemyEnumsAsmProcessor();
    private final EntriesAsmProcessor entriesAsmProcessor = new EntriesAsmProcessor();
    private final EnemyMapspriteAsmProcessor enemyMapspritesAsmProcessor = new EnemyMapspriteAsmProcessor();
    
    private Battle battle;
    private EnemyData[] enemyData;
    private EnemyEnums enemyEnums;

    @Override
    public void clearData() {
        mapTerrainManager.clearData();
        mapspriteManager.clearData();
        specialSpriteManager.clearData();
        battle = null;
        enemyData = null;
        enemyEnums = null;
    }
        
    public Battle importDisassembly(Path paletteEntriesPath, Path tilesetEntriesPath, Path mapEntriesPath, Path terrainEntriesPath, Path battleMapCoordsPath, Path spritesetEntriesPath, int battleIndex) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        BattleMapTerrain terrain = mapTerrainManager.importDisassembly(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, terrainEntriesPath, battleMapCoordsPath, battleIndex);
        BattleMapCoords coords = mapTerrainManager.getCoords();
        EntriesAsmData spritesetEntries = battleSpritesetEntriesProcessor.importAsmData(spritesetEntriesPath, null);
        Path spritesetPath = PathHelpers.getIncbinPath().resolve(spritesetEntries.getPathForEntry(battleIndex));
        BattleSpritesetPackage pckg = new BattleSpritesetPackage(battleIndex, enemyData, enemyEnums);
        BattleSpriteset spriteset = battleSpritesetAsmProcessor.importAsmData(spritesetPath, pckg);
        battle = new Battle(battleIndex, coords, terrain, spriteset);
        Console.logger().info("Battle " + battleIndex + " and spritesets imported from : " + spritesetEntriesPath);
        Console.logger().finest("EXITING importDisassembly");
        return battle;
    }
    
    public void exportDisassembly(Path mapcoordsPath, Path terrainPath, Path spritesetPath, Battle battle) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.battle = battle;
        BattleMapCoords[] allCoords = mapTerrainManager.getAllCoords();
        allCoords[battle.getIndex()] = battle.getMapCoords();
        battleCoordsAsmProcessor.exportAsmData(mapcoordsPath, allCoords, null);
        mapTerrainManager.exportDisassembly(terrainPath, battle.getTerrain());
        BattleSpritesetPackage pckg = new BattleSpritesetPackage(battle.getIndex(), enemyData, enemyEnums);
        battleSpritesetAsmProcessor.exportAsmData(spritesetPath, battle.getSpriteset(), pckg);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void importMapspriteData(Path basePalettePath, Path mapspriteEntriesPath, Path enemyMapspritesPath, Path mapspriteEnumsPath) throws IOException, AsmException, DisassemblyException {
        Console.logger().finest("ENTERING importEnemyData");
        if (enemyEnums == null) {
            enemyEnums = enemyEnumsAsmProcessor.importAsmData(mapspriteEnumsPath, null);
            Palette palette = paletteManager.importDisassembly(basePalettePath, true);
            EntriesAsmData mapspriteEntries = entriesAsmProcessor.importAsmData(mapspriteEntriesPath, null);
            String[] enemyMapsprites = (String[])enemyMapspritesAsmProcessor.importAsmData(enemyMapspritesPath, null);
            enemyData = processEnemyData(enemyEnums, mapspriteEntries, enemyMapsprites, enemyEnums.getMapSprites(), palette);
            Console.logger().info("Mapsprite data loaded from " + mapspriteEntriesPath + " and " + mapspriteEnumsPath);
        } else {
            Console.logger().warning("Mapsprite data already loaded.");
        }
        Console.logger().finest("EXITING importEnemyData");
    }
    
    private EnemyData[] processEnemyData(EnemyEnums enemyEnums, EntriesAsmData mapspriteEntries, String[] enemyMapsprites, LinkedHashMap<String, Integer> mapspriteEnumsData, Palette palette) throws IOException, DisassemblyException {
        ArrayList<EnemyData> enemyDataList = new ArrayList(enemyEnums.getEnemies().size());
        LinkedHashMap<String, Integer> enemies = enemyEnums.getEnemies();
        for (Map.Entry<String, Integer> entry : enemies.entrySet()) {
            String shortName = entry.getKey().substring(6);
            Tileset loadedSprite = null;
            boolean isSpecialSprite = false;
            String mapSprite = "MAPSPRITE_" + enemyMapsprites[entry.getValue()];
            if (mapspriteEnumsData.containsKey(mapSprite)) {
                Path mapspritePath = null;
                try {
                    int mapSpriteIndex = mapspriteEnumsData.get(mapSprite);
                    if (mapSpriteIndex > 1010) {    //Special sprite
                        //TODO Handle correctly once: https://github.com/ShiningForceCentral/SF2DISASM/issues/57 is resolvedv
                        String specialName = shortName.toLowerCase();
                        if (specialName.indexOf('_') != -1) specialName = specialName.substring(0, specialName.indexOf('_'));
                        mapspritePath = Path.of(String.format("data/graphics/specialsprites/%s.bin", specialName));
                        if (mapspritePath != null) {
                            mapspritePath = PathHelpers.getIncbinPath().resolve(mapspritePath);
                            loadedSprite = specialSpriteManager.importDisassembly(mapspritePath, 2, 4, 3, null);
                            isSpecialSprite = true;
                        }
                    } else {
                        mapspritePath = mapspriteEntries.getPathForEntry(mapSpriteIndex*3+2);
                        if (mapspritePath != null) {
                            mapspritePath = PathHelpers.getIncbinPath().resolve(mapspritePath);
                            MapSprite[] sprite = mapspriteManager.importDisassembly(mapspritePath, palette);
                            loadedSprite = sprite[0].getFrame(2, 1);
                        }
                    }
                } catch (Exception e) {
                    Console.logger().log(Level.SEVERE, null, e);
                    Console.logger().severe("ERROR Could not import mapsprite : " + mapspritePath);
                }
            } else {
                Console.logger().severe("ERROR Could not find mapsprite : " + mapSprite);
            }
            enemyDataList.add(new EnemyData(entry.getValue(), shortName, loadedSprite, isSpecialSprite));
        }
        enemyData = new EnemyData[enemyDataList.size()];
        enemyData = enemyDataList.toArray(enemyData);
        return enemyData;
    }
    
    public MapLayout loadNewMap(Path paletteEntriesPath, Path tilesetEntriesPath, int mapIndex) throws IOException, AsmException, DisassemblyException {
        return mapTerrainManager.importMap(paletteEntriesPath, tilesetEntriesPath, mapIndex);
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public BattleMapCoords getBattleCoords() {
        return this.battle.getMapCoords();
    }

    public MapLayout getMapLayout() {
        return mapTerrainManager.getMapLayout();
    }

    public EnemyData[] getEnemyData() {
        return enemyData;
    }    

    public EnemyEnums getEnemyEnums() {
        return enemyEnums;
    }
}
