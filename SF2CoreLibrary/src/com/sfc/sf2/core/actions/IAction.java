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
public interface IAction {
    
    /**
     * Execute and redo the action
     */
    public void execute();

    /**
     * Undo the action
     */
    public void undo();

    /**
     * If any 2 actions should be combined
     */
    public boolean canBeCombined(IAction action);

    /**
     * When 2 actions are identical their data is combined. This effectively collapses the number of actions in the history and reduces the number of repeated actions (e.g. if the same value is changed in succession)
     */
    public void combine(IAction action);

    /**
     * @return <code>True</code> if the undo/redo data is no longer valid (e.g. if the data is identical)
     */
    public boolean isInvalidated();

    /**
     * Dispose the action and its data
     */
    public void dispose();

    /**
     * How to convert the data in this action into a table format....
     */
    public Object[] toTableData();
}
