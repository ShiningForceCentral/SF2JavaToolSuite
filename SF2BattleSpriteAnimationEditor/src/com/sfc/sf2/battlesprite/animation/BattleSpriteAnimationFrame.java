/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationFrame {
    
    private byte battleSpriteIndex;
    private byte duration;
    private byte x;
    private byte y;
    
    private byte weaponFrame;
    private boolean weaponFlipH;
    private boolean weaponFlipV;
    private boolean weaponBehind;
    private byte weaponX;
    private byte weaponY;

    /**
     * For creating Ally animations
     */
    public BattleSpriteAnimationFrame(byte battleSpriteIndex, byte duration, byte x, byte y, byte weaponFrame, boolean weaponFlipH, boolean weaponFlipV, boolean weaponBehind, byte weaponX, byte weaponY) {
        this.battleSpriteIndex = battleSpriteIndex;
        this.duration = duration;
        this.x = x;
        this.y = y;
        this.weaponFrame = weaponFrame;
        this.weaponFlipH = weaponFlipH;
        this.weaponFlipV = weaponFlipV;
        this.weaponBehind = weaponBehind;
        this.weaponX = weaponX;
        this.weaponY = weaponY;
    }
    
    /**
     * For creating Enemy animations
     */
    public BattleSpriteAnimationFrame(byte battleSpriteIndex, byte duration, byte x, byte y) {
        this.battleSpriteIndex = battleSpriteIndex;
        this.duration = duration;
        this.x = x;
        this.y = y;
        this.weaponFrame = -1;
        this.weaponFlipH = false;
        this.weaponFlipV = false;
        this.weaponBehind = false;
        this.weaponX = -1;
        this.weaponY = -1;
    }
    
    public byte getBattleSpriteIndex() {
        return battleSpriteIndex;
    }

    public void setBattleSpriteIndex(byte battleSpriteIndex) {
        this.battleSpriteIndex = battleSpriteIndex;
    }

    public byte getDuration() {
        return duration;
    }

    public void setDuration(byte duration) {
        this.duration = duration;
    }

    public byte getX() {
        return x;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public byte getY() {
        return y;
    }

    public void setY(byte y) {
        this.y = y;
    }

    public byte getWeaponFrame() {
        return weaponFrame;
    }

    public void setWeaponFrame(byte weaponFrame) {
        this.weaponFrame = weaponFrame;
    }

    public boolean getWeaponFlipH() {
        return weaponFlipH;
    }

    public void setWeaponFlipH(boolean weaponFlipX) {
        this.weaponFlipH = weaponFlipX;
    }

    public boolean getWeaponFlipV() {
        return weaponFlipV;
    }

    public void setWeaponFlipV(boolean weaponFlipY) {
        this.weaponFlipV = weaponFlipY;
    }

    public boolean getWeaponBehind() {
        return weaponBehind;
    }

    public void setWeaponBehind(boolean weaponBehind) {
        this.weaponBehind = weaponBehind;
    }

    public byte getWeaponX() {
        return weaponX;
    }

    public void setWeaponX(byte weaponX) {
        this.weaponX = weaponX;
    }

    public byte getWeaponY() {
        return weaponY;
    }

    public void setWeaponY(byte weaponY) {
        this.weaponY = weaponY;
    }
    
    @Override
    public BattleSpriteAnimationFrame clone() {
        return new BattleSpriteAnimationFrame(battleSpriteIndex, duration, x, y, weaponFrame, weaponFlipH, weaponFlipV, weaponBehind, weaponX, weaponY);
    }
        
    public static BattleSpriteAnimationFrame EmptyFrame() {
        return new BattleSpriteAnimationFrame((byte)0, (byte)0, (byte)0, (byte)0);
    }
}
