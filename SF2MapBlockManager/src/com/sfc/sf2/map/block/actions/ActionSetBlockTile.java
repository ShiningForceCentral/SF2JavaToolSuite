/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapTile;

/**
 *
 * @author TiMMy
 */
public record ActionSetBlockTile(MapBlock block, int index, MapTile tile) { }
