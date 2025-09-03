/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.io;

import com.sfc.sf2.mapsprite.MapSpriteManager;
import com.sfc.sf2.palette.Palette;

/**
 * 
 * @author TiMMy
 */
public record MapSpritePackage(String name, int[] indices, Palette palette, MapSpriteManager.MapSpriteExportMode exportMode) { }