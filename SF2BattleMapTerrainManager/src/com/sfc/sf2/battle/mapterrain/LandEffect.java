/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain;

/**
 *
 * @author TiMMy
 */
public class LandEffect {
    
    String defense;
    int moveCost;

    public LandEffect(String defense, int moveCost) {
        this.defense = defense;
        this.moveCost = moveCost;
    }

    public String getDefense() {
        return defense;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }

    public int getMoveCost() {
        return moveCost;
    }

    public void setMoveCost(int moveCost) {
        this.moveCost = moveCost;
    }
}
