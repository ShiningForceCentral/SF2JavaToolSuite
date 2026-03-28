/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain;

import com.sfc.sf2.core.AbstractEnums;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class LandEffectEnums extends AbstractEnums {
    
    private LinkedHashMap<String, Integer> defenses;
    private LinkedHashMap<String, Integer> moveTypes;

    public LandEffectEnums(LinkedHashMap<String, Integer> defenses, LinkedHashMap<String, Integer> moveTypes) {
        this.defenses = defenses;
        this.moveTypes = moveTypes;
    }

    public LinkedHashMap<String, Integer> getDefenses() {
        return defenses;
    }

    public LinkedHashMap<String, Integer> getMoveTypes() {
        return moveTypes;
    }
}
