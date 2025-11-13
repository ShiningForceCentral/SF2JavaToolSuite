/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

/**
 *
 * @author TiMMy
 */
public class Action<T extends Object> implements IAction {

    protected final IActionable<T> action;
    protected final IActionable<T> undoAction;
    protected final T newValue;
    protected final T oldValue;
    
    public Action(IActionable<T> action, T newValue, T oldValue) {
        this(action, newValue, null, oldValue);
    }
    
    public Action(IActionable<T> redoAction, T redoValue, IActionable<T> undoAction, T undoValue) {
        this.action = redoAction;
        this.undoAction = undoAction;
        this.newValue = redoValue;
        this.oldValue = undoValue;
    }
    
    public void execute() {
        action.setActionData(newValue);
    }
    
    public void undo() {
        if (undoAction != null) {
            undoAction.setActionData(oldValue);
        } else {
            action.setActionData(oldValue);
        }
    }
    
    public void dispose() { }
    
    public Object[] toTableData() {
        return new Object[] { action.getClass().toString(), newValue.toString(), oldValue.toString(), undoAction.getClass().toString() };
    }
}
