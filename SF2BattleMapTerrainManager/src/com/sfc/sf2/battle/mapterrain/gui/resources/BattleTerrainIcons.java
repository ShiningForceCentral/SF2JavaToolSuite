/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.gui.resources;

import com.sfc.sf2.core.settings.CoreSettings;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.helpers.PathHelpers;
import java.awt.Color;
import java.io.File;
import java.nio.file.Path;
import javax.swing.ImageIcon;

/**
 *
 * @author TiMMy
 */
public class BattleTerrainIcons {
    private static final ClassLoader loader = BattleTerrainIcons.class.getClassLoader();
    
    public static final Color TERRAIN_DARKEN = new Color(0, 0, 0, 50);
    public static final Color TERRAIN_BG = Color.BLACK;
    public static final Color TERRAIN_BG_ADDITIONAL = Color.WHITE;
    
    private static final ImageIcon[] terrainIcons = new ImageIcon[17];
    private static final Color[] terrainTextColors = new Color[] {
        //Base
        new Color(0xFF3333),    //Obstructed
        Color.DARK_GRAY,        //Wall
        Color.WHITE,            //Plains
        new Color(0xFFBB66),    //Path
        new Color(0x008800),    //Grass
        new Color(0x22DD22),    //Forest
        new Color(0xFF7744),    //Hills
        new Color(0xFFFF66),    //Desert
        Color.GRAY,             //Mountain
        new Color(0x6666FF),    //Water
        //Additional
        new Color(0xBB0000),    //Additional 1
        new Color(0x008800),    //Additional 2
        new Color(0x0000BB),    //Additional 3
        new Color(0xBBBB00),    //Additional 4
        new Color(0x00BBBB),    //Additional 5
        new Color(0xBB00BB),    //Additional 6
        new Color(0x000000),    //Additional 7
    };
    
    public static Color getBGColor(int terrainType) {
        return terrainType < 9 ? TERRAIN_BG : TERRAIN_BG_ADDITIONAL;
    }
    
    public static ImageIcon getTerrainIcon(int terrainType) {
        if (!((CoreSettings)SettingsManager.getSettingsStore("core")).arePathsValid()) return null;
        if (terrainIcons[0] == null) {
            //Base
            terrainIcons[0] = new ImageIcon(loader.getResource("terrain/icons/XX_Obstructed.png"));
            terrainIcons[1] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/00.png"), "terrain/icons/00_Wall.png");
            terrainIcons[2] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/01.png"), "terrain/icons/01_Plains.png");
            terrainIcons[3] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/02.png"), "terrain/icons/02_Path.png");
            terrainIcons[4] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/03.png"), "terrain/icons/03_Grass.png");
            terrainIcons[5] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/04.png"), "terrain/icons/04_Forest.png");
            terrainIcons[6] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/05.png"), "terrain/icons/05_Hills.png");
            terrainIcons[7] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/06.png"), "terrain/icons/06_Desert.png");
            terrainIcons[8] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/07.png"), "terrain/icons/07_Mountain.png");
            terrainIcons[9] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/08.png"), "terrain/icons/08_Water.png");
            //Additional
            terrainIcons[10] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/09.png"), "terrain/icons/09_Add_1.png");
            terrainIcons[11] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/10.png"), "terrain/icons/10_Add_2.png");
            terrainIcons[12] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/11.png"), "terrain/icons/11_Add_3.png");
            terrainIcons[13] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/12.png"), "terrain/icons/12_Add_4.png");
            terrainIcons[14] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/13.png"), "terrain/icons/13_Add_5.png");
            terrainIcons[15] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/14.png"), "terrain/icons/14_Add_6.png");
            terrainIcons[16] = loadCustomOrInternal(PathHelpers.getBasePath().resolve("terrain_icons/15.png"), "terrain/icons/15_Add_7.png");
        }
        terrainType++;
        if (terrainType < 0 || terrainType >= terrainTextColors.length) {
            return terrainIcons[0];
        } else {
            return terrainIcons[terrainType];
        }
    }
    
    private static ImageIcon loadCustomOrInternal(Path customPath, String resourcePath) {
        File file = customPath.toFile();
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }
        return new ImageIcon(loader.getResource(resourcePath));
    }
    
    public static Color getTerrainTextColor(int terrainType) {
        terrainType++;
        if (terrainType < 0 || terrainType >= terrainTextColors.length) {
            return Color.WHITE;
        } else {
            return terrainTextColors[terrainType];
        }
    }
}
