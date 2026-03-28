/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.actions;

import com.sfc.sf2.core.actions.IActionData;
import java.awt.Point;

/**
 *
 * @author TiMMy
 */
public class SpritesetPosActionData implements IActionData<SpritesetPosActionData> {
    
    private final Object item;
    private final Point pos;

    public SpritesetPosActionData(Object item, Point pos) {
        this.item = item;
        this.pos = pos;
    }

    public Object item() {
        return item;
    }

    public Point pos() {
        return pos;
    }

    @Override
    public boolean isInvalidated(SpritesetPosActionData other) {
        return this.item.equals(other.item) && this.pos.equals(other.pos);
    }

    @Override
    public boolean canBeCombined(SpritesetPosActionData other) {
        return this.item.equals(other.item);
    }

    @Override
    public SpritesetPosActionData combine(SpritesetPosActionData other) {
        return other;
    }
    
    @Override
    public String toString() {
        return String.format("Pos: (%d, %d)", pos.x, pos.y);
    }
}
