/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import java.awt.Point;

/**
 *
 * @author TiMMy
 */
public record ActionMapDataPoint(Object mapDataItem, int itemIndex, String event, Point point) { }
