/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.helpers;

import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import static com.sfc.sf2.battle.mapcoords.BattleMapCoords.BATTLE_SIZE;
import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.map.block.MapTile;
import com.sfc.sf2.map.layout.MapLayout;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.MapLayoutBlock;

/**
 *
 * @author TiMMy
 */
public class BattleMapTerrainHelpers {
    private static int[] CHECK_PIXEL_INDICES = new int[] {19,20,21,22,27,28,29,30,35,36,37,38,43,44,45,46};
        
    public static BattleMapTerrain resetTerrain(BattleMapTerrain terrain) {
        if (terrain == null) return terrain;
        terrain = terrain.clone();
        byte[] data = terrain.getData();
        for (int i = 0; i < data.length; i++) {
            data[i] = 1; //Plains
        }
        return terrain;
    }
    
    public static BattleMapTerrain autoGenerateTerrain(BattleMapTerrain terrain, BattleMapCoords battleCoords, MapLayout layout) {
        if (terrain == null || layout == null) return terrain;
        terrain = terrain.clone();
        MapLayoutBlock[] blocks = layout.getBlocks();
        byte[] data = terrain.getData();
        byte[] colorCounts = new byte[16];
        byte highestColor;
        byte nextHighestColor;
        
        for (int j = 0; j < battleCoords.getHeight(); j++) {
            for (int i = 0; i < battleCoords.getWidth(); i++) {
                //Check edges
                if (i == 0 || i >= battleCoords.getWidth()-1 || j == 0 || j >= battleCoords.getHeight()-1)
                {
                    data[i+j*BATTLE_SIZE] = -1; //Obstructed
                    continue;
                }
                
                //Read map pixel data
                int x = battleCoords.getX()+i;
                int y = battleCoords.getY()+j;
                MapLayoutBlock block = blocks[x + y*BLOCK_WIDTH];
                MapTile tile = block.getMapBlock().getMapTiles()[4];    //Center tile
                int[] pixels = tile.getTile(layout.getTilesets()).getRenderPixels(tile.getTileFlags());
                
                //Reset colorCounts
                for (byte c = 0; c < colorCounts.length; c++) {
                    colorCounts[c] = 0;
                }
                //Increment color counts
                for (int p = 0; p < CHECK_PIXEL_INDICES.length; p++) {
                    colorCounts[pixels[CHECK_PIXEL_INDICES[p]]]++;
                }
                //Find highest color
                highestColor = 0;
                nextHighestColor = 0;
                int highestAmount = 0;
                for (byte c = 0; c < colorCounts.length; c++) {
                    if (colorCounts[c] > highestAmount) {
                        highestAmount = colorCounts[c];
                        highestColor = c;
                    }
                }
                highestAmount = 0;
                for (byte c = 0; c < colorCounts.length; c++) {
                    if (c == highestColor) continue;
                    if (colorCounts[c] > highestAmount) {
                        highestAmount = colorCounts[c];
                        nextHighestColor = c;
                    }
                }
                
                //Check for color profiles
                //"Sky/Wall", "Plains", "Path", "Overgrowth", "Forest", "Hills", "Desert", "Mountain", "Water", "Add1", "Add2", "Add3", "Add4", "Add5", "Add6", "Add7" };
                byte type;
                switch (highestColor) {
                    case 1, 2 -> type = 8;              //Water
                    case 3, 4 -> {
                        if (nextHighestColor == 5)
                            type = 0;                   //High cliffs (sky)
                        else
                            type = 1;                   //Indoor ground (plains)
                    }
                    case 5 -> {
                        if (nextHighestColor == 3)
                             type = 2;                  //Path (bridge)
                        else if (nextHighestColor == 13)
                             type = 6;                  //Sand
                        else
                             type = 5;                  //Hills
                    }
                    case 6 -> type = 4;                 //Forest
                    case 7 -> {
                        if (nextHighestColor == 6)
                            type = 3;                   //Overgrowth
                        else if (nextHighestColor == 14)
                            type = 1;                   //Plains
                        else
                            type = 2;                   //Path
                    }
                    case 13 -> type = 6;                //Sand
                    case 9, 10 -> {
                        if (nextHighestColor == 11 && colorCounts[nextHighestColor] > 4)
                            type = 7;                   //Mountain
                        else
                            type = 0;                   //Wall
                    }
                    case 8, 11, 15 ->{
                        if (highestColor == 8 && nextHighestColor == 6)
                            type = 4;                   //Forest
                        else
                            type = 7;                   //Mountain
                    }
                    default -> type = -1;               //Fallback Obstructed
                }
                data[i+j*BATTLE_SIZE] = type;
            }
        }
        return terrain;
    }
}
