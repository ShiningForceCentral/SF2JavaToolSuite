/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.io;

import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import com.sfc.sf2.core.io.AbstractDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.helpers.BinaryHelpers;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationDisassemblyProcessor extends AbstractDisassemblyProcessor<BattleSpriteAnimation, BattleSpriteAnimationPackage> {

    @Override
    protected BattleSpriteAnimation parseDisassemblyData(byte[] data, BattleSpriteAnimationPackage pckg) throws DisassemblyException {
        
        boolean isAlly = pckg.type() == BattleSprite.BattleSpriteType.ALLY;
        int offset = isAlly ? 8 : 4;
        byte frameCount = (byte)(BinaryHelpers.getByte(data, 0)+1); //Add 1 because first frame is part of this 'core' data
        BattleSpriteAnimationFrame[] frames = new BattleSpriteAnimationFrame[frameCount];

        byte initFrame = BinaryHelpers.getByte(data, 1);
        byte spellAnim = BinaryHelpers.getByte(data, 2);
        boolean endSpellAnim = BinaryHelpers.getByte(data, 3) != 0;
        if (isAlly) {
            byte weaponData = BinaryHelpers.getByte(data, 4);
            byte idleWeaponFrame = (byte)(weaponData&0xF);
            boolean idleWeaponFlipX = (weaponData&0x10) != 0;
            boolean idleWeaponFlipY = (weaponData&0x20) != 0;
            boolean idleWeaponBehind = BinaryHelpers.getByte(data, 5) == 1;
            byte idleWeaponX = BinaryHelpers.getByte(data, 6);
            byte idleWeaponY = BinaryHelpers.getByte(data, 7);
            frames[0] = new BattleSpriteAnimationFrame((byte)0, (byte)0, (byte)0, (byte)0, idleWeaponFrame, idleWeaponFlipX, idleWeaponFlipY, idleWeaponBehind, idleWeaponX, idleWeaponY);
        } else {
            frames[0] = BattleSpriteAnimationFrame.EmptyFrame();
        }

        for (byte i=1; i < frames.length; i++) {
            byte battleSpriteIndex = BinaryHelpers.getByte(data, i*offset+0);
            if (battleSpriteIndex == 0xF) {
                battleSpriteIndex = frames[i-1].getBattleSpriteIndex(); //Disasm stores value 0xF to mean "same as previous frame"
            }
            byte duration = BinaryHelpers.getByte(data, i*offset+1);
            byte x = BinaryHelpers.getByte(data, i*offset+2);
            byte y = BinaryHelpers.getByte(data, i*offset+3);
            if (isAlly) {
                byte weaponData = BinaryHelpers.getByte(data, i*offset+4);
                byte weaponFrame = (byte)(weaponData&0xF);
                boolean weaponFlipX = (weaponData&0x10) != 0;
                boolean weaponFlipY = (weaponData&0x20) != 0;
                boolean behind = BinaryHelpers.getByte(data, i*offset+5) == 1;
                byte weaponX = BinaryHelpers.getByte(data, i*offset+6);
                byte weaponY = BinaryHelpers.getByte(data, i*offset+7);
                frames[i] = new BattleSpriteAnimationFrame(battleSpriteIndex, duration, x, y, weaponFrame, weaponFlipX, weaponFlipY, behind, weaponX, weaponY);
            } else {
                frames[i] = new BattleSpriteAnimationFrame(battleSpriteIndex, duration, x, y);
            }
        }
        return new BattleSpriteAnimation(pckg.type(), frames, initFrame, spellAnim, endSpellAnim);
    }

    @Override
    protected byte[] packageDisassemblyData(BattleSpriteAnimation item, BattleSpriteAnimationPackage pckg) throws DisassemblyException {
        
        boolean isAlly = pckg.type() == BattleSprite.BattleSpriteType.ALLY;
        int offset = isAlly ? 8 : 4;
        BattleSpriteAnimationFrame frame;
        byte weaponData;
        byte frameCount = item.getFrameCount();
        byte[] animationFileBytes = new byte[(frameCount)*offset];

        animationFileBytes[0] = (byte)(frameCount-1); //Remove 1 because first frame is part of this 'core' data 
        animationFileBytes[1] = item.getSpellInitFrame();
        animationFileBytes[2] = item.getSpellAnim();
        animationFileBytes[3] = (byte)(item.getEndSpellAnim() ? 1 : 0);
        if (isAlly) {
            frame = item.getFrames()[0];
            weaponData = (byte)(frame.getWeaponFrame() + (frame.getWeaponFlipH() ? 0x10 : 0) + (frame.getWeaponFlipV() ? 0x20 : 0));
            animationFileBytes[4] = weaponData;
            animationFileBytes[5] = (byte)(frame.getWeaponBehind() ? 1 : 2);
            animationFileBytes[6] = frame.getWeaponX();
            animationFileBytes[7] = frame.getWeaponY();
        }

        for (byte i=1; i < frameCount; i++) {
            frame = item.getFrames()[i];
            byte battleSpriteIndex = frame.getBattleSpriteIndex();
            if (i >= 2 && battleSpriteIndex == item.getFrames()[i-1].getBattleSpriteIndex()) {
                battleSpriteIndex = 0xF;    //Disasm stores value 0xF to mean "same as previous frame" (ignore frames 0-1 because frame 0 is core data)
            }
            animationFileBytes[i*offset+0] = battleSpriteIndex;
            animationFileBytes[i*offset+1] = frame.getDuration();
            animationFileBytes[i*offset+2] = frame.getX();
            animationFileBytes[i*offset+3] = frame.getY();
            if (isAlly) {
                weaponData = (byte)(frame.getWeaponFrame() + (frame.getWeaponFlipH() ? 0x10 : 0) + (frame.getWeaponFlipV() ? 0x20 : 0));
                animationFileBytes[i*offset+4] = weaponData;
                animationFileBytes[i*offset+5] = (byte)(frame.getWeaponBehind() ? 1 : 0);
                animationFileBytes[i*offset+6] = frame.getWeaponX();
                animationFileBytes[i*offset+7] = frame.getWeaponY();
            }
        }
        return animationFileBytes;
    }
}
