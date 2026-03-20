/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.animation.actions;

import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.actions.MapLayoutActionData;

/**
 *
 * @author TiMMy
 */
public class MapAnimationActionData extends MapLayoutActionData {
    
    private final MapAnimation animation;
    private final String sharedAnimationInfo;

    public MapAnimationActionData(MapLayout layout, MapBlockset blockset, String sharedBlockInfo, MapAnimation animation, String sharedAnimationInfo) {
        super(layout, blockset, sharedBlockInfo);
        this.animation = animation;
        this.sharedAnimationInfo = sharedAnimationInfo;
    }

    public MapAnimation animation() {
        return animation;
    }

    public String sharedAnimationInfo() {
        return sharedAnimationInfo;
    }

    @Override
    public boolean isInvalidated(MapLayoutActionData other) {
        if (!super.isInvalidated(other)) return false;
        MapAnimationActionData otherA = (MapAnimationActionData)other;
        if (otherA == null) return false;
        return this.animation.equals(otherA.animation);
    }

    @Override
    public boolean canBeCombined(MapLayoutActionData other) {
        return super.isInvalidated(other);
    }

    @Override
    public MapLayoutActionData combine(MapLayoutActionData other) {
        return other;
    }
    

    @Override
    public String toString() {
        return super.toString();
    }
}
