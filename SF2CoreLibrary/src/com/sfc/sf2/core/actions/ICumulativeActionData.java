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
public interface ICumulativeActionData<T extends Object> extends IActionData<T> {
    
    /**
     * How to print the data in a readable format
     */
    public String toCumulativeString(int number);
}
