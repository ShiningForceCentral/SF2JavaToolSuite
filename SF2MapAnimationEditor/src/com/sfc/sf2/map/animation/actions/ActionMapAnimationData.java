/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.animation.actions;

import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.layout.actions.ActionMapLayoutData;

/**
 *
 * @author TiMMy
 */
public class ActionMapAnimationData extends ActionMapLayoutData {
    
    private final MapAnimation animation;
    private final String sharedAnimationInfo;

    public ActionMapAnimationData(MapLayout layout, MapBlockset blockset, String sharedBlockInfo, MapAnimation animation, String sharedAnimationInfo) {
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
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (obj instanceof ActionMapAnimationData) {
            ActionMapAnimationData other = (ActionMapAnimationData)obj;
            return animation.equals(other.animation);
        }
        return false;
    }
}
