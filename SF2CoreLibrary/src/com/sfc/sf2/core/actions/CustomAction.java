/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.INameable;
import javax.swing.JComponent;

/**
 * Defines an action that utilises a complex data format (should extend from IActionData)
 * Override CustomAction when the datatype should check if it is combinable, etc (this saves having to define custom actions and their data types)
 * @author TiMMy
 */
public class CustomAction<T extends IActionData> implements IAction<CustomAction> {

    private final Object owner;
    protected final String operation;
    
    private final IActionable<T> action;
    private final IActionable<T> undoAction;
    protected T newValue;
    protected T oldValue;
    
    public CustomAction(Object owner, String operation, IActionable<T> action, T newValue, T oldValue) {
        this(owner, operation, action, newValue, null, oldValue);
    }
    
    public CustomAction(Object owner, String operation, IActionable<T> redoAction, T redoValue, IActionable<T> undoAction, T undoValue) {
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
    public boolean isInvalidated() {
        return newValue.isInvalidated(oldValue);
    }

    @Override
    public boolean canBeCombined(IAction other) {
        if (this.getClass() != other.getClass()) return false;
        CustomAction otherA = (CustomAction)other;
        if (!this.owner.equals(otherA.owner)) return false;
        if (!this.operation.equals(otherA.operation)) return false;
        if ((this.action == null) != (otherA.action == null)) return false;
        if (this.action != null && !this.action.getClass().equals(otherA.action.getClass())) return false;
        if ((this.undoAction == null) != (otherA.undoAction == null)) return false;
        if (this.undoAction != null && !this.undoAction.getClass().equals(otherA.undoAction.getClass())) return false;
        return newValue.canBeCombined(otherA.newValue);
    }

    @Override
    public void combine(IAction other) {
        CustomAction otherA = (CustomAction)other;
        newValue = (T)newValue.combine(otherA.newValue);
    }
    
    public void dispose() { }
    
    public Object[] toTableData() {
        String name = null;
        if (owner != null) {
            if (owner instanceof JComponent) {
                JComponent component = (JComponent)owner;
                name = component.getName();
            } else  if (owner instanceof INameable) {
                INameable nameable = (INameable)owner;
                name = nameable.getName();
            }
            if (name == null || name.isEmpty()) {
                name = owner.getClass().toString();
            }
        }
        return new Object[] { name, operation, dataToString(newValue), dataToString(oldValue) };
    }
    
    protected String dataToString(T data) {
        if (data == null) {
            return "NULL";
        } else {
            return data.toString();
        }
    }
}
