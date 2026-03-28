/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.INameable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author TiMMy
 */
public class CumulativeAction<T extends ICumulativeActionData> extends BasicAction<List<T>> {
    
    private final T singleNewValue;
    private final T singleOldValue;
    
    public CumulativeAction(Object owner, String operation, IActionable<List<T>> action, T newValue, T oldValue) {
        super(owner, operation, action, new ArrayList<T>(), new ArrayList<T>());
        singleNewValue = newValue;
        singleOldValue = oldValue;
        this.newValue.add(newValue);
        this.oldValue.add(oldValue);
    }

    @Override
    public boolean isInvalidated() {
        if (newValue.size() != oldValue.size()) return false;
        for (int i = 0; i < newValue.size(); i++) {
            if (!newValue.get(i).isInvalidated(oldValue.get(i))) return false;
        }
        return true;
    }

    @Override
    public boolean canBeCombined(IAction other) {
        if (!super.canBeCombined(other)) return false;
        CumulativeAction otherA = (CumulativeAction)other;
        return singleNewValue.canBeCombined(otherA.singleNewValue);
    }

    @Override
    public void combine(IAction other) {
        CumulativeAction<T> otherA = (CumulativeAction<T>)other;
        if (otherA == null) return;
        for (int i = 0; i < newValue.size(); i++) {
            if (newValue.get(i).isInvalidated(otherA.singleNewValue)) return;    //Data already in list
        }
        this.newValue.add(otherA.singleNewValue);
        this.oldValue.add(otherA.singleOldValue);
    }
    
    @Override
    public void dispose() {
        newValue.clear();
        oldValue.clear();
    }
    
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
        if (newValue.size() == 1) {
            return new Object[] { name, operation, singleNewValue.toString(), singleOldValue.toString() };
        } else {
            return new Object[] { name, operation, singleNewValue.toCumulativeString(newValue.size()), singleOldValue.toCumulativeString(oldValue.size()) };
        }
    }
}
