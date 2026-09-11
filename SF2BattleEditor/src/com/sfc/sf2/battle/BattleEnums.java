/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle;

import com.sfc.sf2.core.AbstractEnums;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class BattleEnums extends AbstractEnums {
    private final LinkedHashMap<String, Integer> battles;

    public BattleEnums(LinkedHashMap<String, Integer> battles) {
        this.battles = battles;
    }

    public LinkedHashMap<String, Integer> getBattles() {
        return battles;
    }

    @Override
    public String toString() {
        return String.format("Battles: %d", battles.size());
    }
}
