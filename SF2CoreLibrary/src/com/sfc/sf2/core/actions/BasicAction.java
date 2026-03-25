/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.INameable;
import javax.swing.JComponent;

/**
 * Defines an action that utilises basic data types (anything that extends from Object)
 * Override BasicAction for simple datatypes or when you wish to execute the action within the class (e.g. see ToggleAction or ComboAction)
 * @author TiMMy
 */
public class BasicAction<T extends Object> implements IAction<BasicAction> {

    protected final Object owner;
    protected final String operation;
    
    private final IActionable<T> action;
    private final IActionable<T> undoAction;
    protected T newValue;
    protected T oldValue;
    
    public BasicAction(Object owner, String operation, IActionable<T> action, T newValue, T oldValue) {
        this(owner, operation, action, newValue, null, oldValue);
    }
    
    public BasicAction(Object owner, String operation, IActionable<T> redoAction, T redoValue, IActionable<T> undoAction, T undoValue) {
        this.action = redoAction;
        this.undoAction = undoAction;
        this.newValue = redoValue;
        this.oldValue = undoValue;
        this.owner = owner;
        this.operation = operation;
    }
    
    @Override
    public void execute() {
        if (owner instanceof JComponent) {
            ((JComponent)owner).requestFocus();
        }
        if (action != null) {
            action.setActionData(newValue);
        }
    }
    
    @Override
    public void undo() {
        if (owner instanceof JComponent) {
            ((JComponent)owner).requestFocus();
        }
        if (undoAction != null) {
            undoAction.setActionData(oldValue);
        } else if (action != null) {
            action.setActionData(oldValue);
        }
    }

    @Override
    public boolean isInvalidated() {
        return newValue.equals(oldValue);
    }

    @Override
    public boolean canBeCombined(IAction other) {
        if (this.getClass() != other.getClass()) return false;
        BasicAction otherA = (BasicAction)other;
        if (!this.owner.equals(otherA.owner)) return false;
        if (!this.operation.equals(otherA.operation)) return false;
        if ((this.action == null) != (otherA.action == null)) return false;
        if (this.action != null && !this.action.getClass().equals(otherA.action.getClass())) return false;
        if ((this.undoAction == null) != (otherA.undoAction == null)) return false;
        if (this.undoAction != null && !this.undoAction.getClass().equals(otherA.undoAction.getClass())) return false;
        return true;
    }

    @Override
    public void combine(IAction other) {
        BasicAction otherA = (BasicAction)other;
        this.newValue = (T)otherA.newValue;
    }
    
    @Override
    public void dispose() { }
    
    @Override
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
        if (data == null) return "NULL";
        if (data instanceof JComponent) {
            JComponent component = (JComponent)data;
            return component.getName();
        } else  if (data instanceof INameable) {
            INameable nameable = (INameable)data;
            return nameable.getName();
        }
        
        String s = data.toString();
        if (s.charAt(0) == '[') {
            s = s.substring(s.lastIndexOf('.')+1);
        }
        
        if (data instanceof Object[]) {
            Object[] array = (Object[])data;
            if (array.length == 0) return "NULL";
            s = s.substring(0, s.indexOf(";@"));
            return String.format("%s array. Size = %d", s, array.length);
        } else {
            return s;
        }
    }
}
