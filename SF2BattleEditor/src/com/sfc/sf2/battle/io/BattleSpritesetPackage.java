/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.io;

import com.sfc.sf2.battle.EnemyData;
import com.sfc.sf2.battle.EnemyEnums;

/**
 *
 * @author TiMMy
 */
public record BattleSpritesetPackage(int index, EnemyData[] enemyData, EnemyEnums enemyEnums) { }
