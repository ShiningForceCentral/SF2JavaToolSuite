/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battlescene;

import com.sfc.sf2.battlescene.actions.ActionBattleScene;
import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;

/**
 *
 * @author TiMMy
 */
public class BattleSceneAction extends Action<ActionBattleScene> {
    
    public BattleSceneAction(Object owner, String operation, IActionable<ActionBattleScene> action, ActionBattleScene newValue, ActionBattleScene oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean isInvalidated() {
        if ((newValue == null) != (oldValue == null)) return false;
        if (newValue == null) return true;
        return newValue.background().getIndex() == oldValue.background().getIndex() && newValue.ground().getName().equals(oldValue.ground().getName());
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        BattleSceneAction other = (BattleSceneAction)action;
        return newValue.background().getIndex() == other.newValue.background().getIndex() && newValue.ground().getName().equals(other.newValue.ground().getName());
    }
    
}
