/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import java.awt.Rectangle;

/**
 *
 * @author TiMMy
 */
public record ActionMapDataRect(Object mapDataItem, int itemIndex, String variableId, Rectangle rect) { }
