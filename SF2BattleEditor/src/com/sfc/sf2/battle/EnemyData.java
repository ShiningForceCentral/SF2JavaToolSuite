/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import com.sfc.sf2.graphics.Tileset;

/**
 *
 * @author TiMMy
 */
public class EnemyData {
    private int id;
    private String name;
    private Tileset mapSprite;
    private boolean isSpecialSprite;

    public EnemyData(int id, String name, Tileset mapSprite, boolean isSpecialSprite) {
        this.id = id;
        this.name = name;
        this.mapSprite = mapSprite;
        this.isSpecialSprite = isSpecialSprite;
    }

    public int getID() {
        return id;
    }
        
    public String getName() {
        return name;
    }

    public Tileset getMapSprite() {
        return mapSprite;
    }

    public boolean isIsSpecialSprite() {
        return isSpecialSprite;
    }
}
