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
public class LandEffectMovementType {
    
    String movementType;
    LandEffect[] landEffects;

    public LandEffectMovementType(String movementType, LandEffect[] landEffects) {
        this.movementType = movementType;
        this.landEffects = landEffects;
    }

    public String getMovementType() {
        return movementType;
    }

    public LandEffect[] getLandEffects() {
        return landEffects;
    }
}
