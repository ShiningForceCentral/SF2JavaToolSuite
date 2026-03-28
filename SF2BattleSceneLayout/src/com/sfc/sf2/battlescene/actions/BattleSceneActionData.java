/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battlescene.actions;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.ground.Ground;

/**
 *
 * @author TiMMy
 */
public class BattleSceneActionData implements IActionData<BattleSceneActionData> {
    
    private Background background;
    private Ground ground;

    public BattleSceneActionData(Background background, Ground ground) {
        this.background = background;
        this.ground = ground;
    }

    public Background background() {
        return background;
    }

    public Ground ground() {
        return ground;
    }

    @Override
    public boolean isInvalidated(BattleSceneActionData other) {
        return background.equals(other.background) && ground.equals(other.ground);
    }

    @Override
    public boolean canBeCombined(BattleSceneActionData other) {
        return isInvalidated(other);
    }

    @Override
    public BattleSceneActionData combine(BattleSceneActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("BG: %d, Ground: %s", background.getIndex(), ground.getName());
    }
}
