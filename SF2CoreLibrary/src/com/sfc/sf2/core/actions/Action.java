/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.INameable;
import javax.swing.JComponent;

/**
 *
 * @author TiMMy
 */
public class Action<T extends Object> implements IAction {

    private Object owner;
    protected String operation;
    
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
        if (!this.owner.equals(other.owner)) return false;
        if (!this.operation.equals(other.operation)) return false;
        if (this.action == null || other.action == null) return this.action == other.action;
        if (this.undoAction == null || other.undoAction == null) return this.undoAction == other.undoAction;
        return this.action.getClass() == other.action.getClass() && this.undoAction.getClass() == other.undoAction.getClass();
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
        String name = null;
        if (owner != null) {
            if (owner instanceof JComponent) {
                JComponent component = (JComponent)owner;
                name = component.getName();
            }
            if (name == null || name.isEmpty()) {
                name = owner.getClass().toString();
            }
        }
        return new Object[] { name, operation, dataToString(newValue), dataToString(oldValue) };
    }
    
    protected String dataToString(T data) {
        if (data == null) return "NULL";
        if (data instanceof INameable) {
               INameable nameable = (INameable)data;
               return nameable.getName();
        }
        return data.toString();
    }
}
