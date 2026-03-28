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
public interface IAction<T extends IAction> {
    
    /**
     * Execute and redo the action
     */
    public void execute();

    /**
     * Undo the action
     */
    public void undo();

    /**
     * @return <code>True</code> if the undo/redo data is no longer valid (e.g. if the data is identical)
     */
    public boolean isInvalidated();

    /**
     * If any 2 actions should be combined
     * @return <code>FALSE</code> if actions cannot be combined
     */
    public boolean canBeCombined(IAction other);

    /**
     * Combines the data in the events
     */
    public void combine(IAction other);

    /**
     * Dispose the action and its data
     */
    public void dispose();

    /**
     * How to convert the data in this action into a table format....
     */
    public Object[] toTableData();
}
