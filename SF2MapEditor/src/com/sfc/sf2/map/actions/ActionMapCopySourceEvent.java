/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.map.MapCopyEvent;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author TiMMy
 */
public record ActionMapCopySourceEvent(MapCopyEvent copyEvent, int itemIndex, String event, Rectangle source, Point dest) { }
