/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.actions;

import com.sfc.sf2.battle.AIRegion;
import com.sfc.sf2.core.actions.IActionData;
import java.awt.Point;

/**
 *
 * @author TiMMy
 */
public class SpritesetRegionActionData implements IActionData<SpritesetRegionActionData> {
    
    private final AIRegion region;
    private final int regionPoint;
    private final Point pos;

    public SpritesetRegionActionData(AIRegion region, int regionPoint, Point pos) {
        this.region = region;
        this.regionPoint = regionPoint;
        this.pos = pos;
    }

    public AIRegion region() {
        return region;
    }

    public int regionPoint() {
        return regionPoint;
    }

    public Point pos() {
        return pos;
    }

    @Override
    public boolean isInvalidated(SpritesetRegionActionData other) {
        return this.region.equals(other.region) && this.regionPoint == other.regionPoint && this.pos.equals(other.pos);
    }

    @Override
    public boolean canBeCombined(SpritesetRegionActionData other) {
        return this.region.equals(other.region) && this.regionPoint == other.regionPoint;
    }

    @Override
    public SpritesetRegionActionData combine(SpritesetRegionActionData other) {
        return other;
    }
    
    @Override
    public String toString() {
        return String.format("Region type: %s. Point: %d. Pos: (%d, %d)", region.getTypeString(), regionPoint, pos.x, pos.y);
    }
}
