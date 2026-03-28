/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation;

import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;
import com.sfc.sf2.core.INameable;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimation implements INameable {
    
    private final String name;
    private final BattleSprite battleSprite;
    private BattleSpriteAnimationFrame[] frames;
    
    private byte spellInitFrame;
    private byte spellAnim;
    private boolean endSpellAnim;

    public BattleSpriteAnimation(String name, BattleSprite battleSprite, BattleSpriteAnimationFrame[] frames, byte spellInitFrame, byte spellAnim, boolean endSpellAnim) {
        this.name = name;
        this.battleSprite = battleSprite;
        this.spellInitFrame = spellInitFrame;
        this.spellAnim = spellAnim;
        this.endSpellAnim = endSpellAnim;
        setFrames(frames);
    }

    public String getName() {
        return String.format("%s (%s)", name, battleSprite.getName());
    }

    public BattleSprite getBattleSprite() {
        return battleSprite;
    }

    public BattleSpriteType getType() {
        return battleSprite.getType();
    }

    public BattleSpriteAnimationFrame[] getFrames() {
        return frames;
    }

    public void setFrames(BattleSpriteAnimationFrame[] frames) {
        this.frames = frames;
        if (frames != null) {
            for (int i = 0; i < frames.length; i++) {
                frames[i].setBattleSpriteAnim(this);
            }
        }
    }

    public byte getFrameCount() {
        return (byte)frames.length;
    }

    public byte getSpellInitFrame() {
        return spellInitFrame;
    }

    public void setSpellInitFrame(byte spellInitFrame) {
        this.spellInitFrame = spellInitFrame;
    }

    public byte getSpellAnim() {
        return spellAnim;
    }

    public void setSpellAnim(byte spellAnim) {
        this.spellAnim = spellAnim;
    }

    public boolean getEndSpellAnim() {
        return endSpellAnim;
    }

    public void setEndSpellAnim(boolean endSpellAnim) {
        this.endSpellAnim = endSpellAnim;
    }
    
    @Override
    public BattleSpriteAnimation clone() {
        return new BattleSpriteAnimation(name, battleSprite, frames, spellInitFrame, spellAnim, endSpellAnim);
    }
        
    public static BattleSpriteAnimation EmptyAnimation(BattleSprite battleSprite) {
        BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[] { BattleSpriteAnimationFrame.EmptyFrame() };
        return new BattleSpriteAnimation("Empty Anim", battleSprite, frames, (byte)0, (byte)0, true);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BattleSpriteAnimation)) return super.equals(obj);
        BattleSpriteAnimation other = (BattleSpriteAnimation)obj;
        if (!this.name.equals(other.name)) return false;
        if (!this.battleSprite.equals(other.battleSprite)) return false;
        if (!Arrays.equals(this.frames, other.frames)) return false;
        if (this.spellInitFrame != other.spellInitFrame) return false;
        if (this.spellAnim != other.spellAnim) return false;
        if (this.endSpellAnim != other.endSpellAnim) return false;
        return true;
    }
}
