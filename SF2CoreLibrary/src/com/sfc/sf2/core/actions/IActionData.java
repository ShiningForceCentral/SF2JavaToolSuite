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
public interface IActionData<T extends Object> {

    /**
     * @return <code>True</code> if the both data items are identical (i.e. no longer valid)
     */
    public boolean isInvalidated(T other);

    /**
     * If any 2 action data items can be combined
     */
    public boolean canBeCombined(T other);

    /**
     * When 2 actions are identical their data is combined. Combine the data items
     */
    public T combine(T other);
    
    
    /**
     * How to print the data in a readable format
     */
    public String toString();
}
