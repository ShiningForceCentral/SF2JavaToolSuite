/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.io;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.map.block.MapBlockset;

/**
 *
 * @author TiMMy
 */
public record MapLayoutPackage(int index, MapBlockset blockset, Tileset[] tilesets) { }
