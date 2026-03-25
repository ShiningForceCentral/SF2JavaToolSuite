/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

/**
 * Defines a {@code BasicAction} that is only combined if identical data is stacked (if invalidated)
 * @author TiMMy
 */
public class NonCombinableAction<T extends Object> extends BasicAction<T> {

    public NonCombinableAction(Object owner, String operation, IActionable<T> action, T newValue, T oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    public NonCombinableAction(Object owner, String operation, IActionable<T> redoAction, T redoValue, IActionable<T> undoAction, T undoValue) {
        super(owner, operation, redoAction, redoValue, undoAction, undoValue);
    }

    @Override
    public boolean canBeCombined(IAction other) {
        if (!(other instanceof NonCombinableAction)) return false;
        NonCombinableAction otherA = (NonCombinableAction)other;
        return newValue.equals(otherA.newValue);
    }
}
