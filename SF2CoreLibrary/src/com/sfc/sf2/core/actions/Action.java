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

    private Object owner;
    private String operation;
    
    private final IActionable<T> action;
    private final IActionable<T> undoAction;
    protected T newValue;
    protected T oldValue;
    
    public Action(Object owner, String operation, IActionable<T> action, T newValue, T oldValue) {
        this(owner, operation, action, newValue, null, oldValue);
    }
    
    public Action(Object owner, String operation, IActionable<T> redoAction, T redoValue, IActionable<T> undoAction, T undoValue) {
        this.action = redoAction;
        this.undoAction = undoAction;
        this.newValue = redoValue;
        this.oldValue = undoValue;
        this.owner = owner;
        this.operation = operation;
    }
    
    public void execute() {
        if (action != null) {
            action.setActionData(newValue);
        }
    }
    
    public void undo() {
        if (undoAction != null) {
            undoAction.setActionData(oldValue);
        } else if (action != null) {
            action.setActionData(oldValue);
        }
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (this.getClass() != action.getClass()) return false;
        Action<T> other = (Action<T>)action;
        if (this.owner != other.owner) return false;
        return this.action == other.action && this.undoAction == other.undoAction;
    }

    @Override
    public void combine(IAction action) {
        this.newValue = ((Action<T>)action).newValue;
    }

    @Override
    public boolean isInvalidated() {
        return newValue.equals(oldValue);
    }
    
    public void dispose() { }
    
    public Object[] toTableData() {
        return new Object[] { owner.getClass().toString(), operation, newValue.toString(), oldValue.toString() };
    }
}
