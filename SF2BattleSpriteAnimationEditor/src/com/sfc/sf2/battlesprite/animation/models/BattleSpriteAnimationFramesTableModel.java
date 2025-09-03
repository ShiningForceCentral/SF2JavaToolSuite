/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.models;

import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.weaponsprite.WeaponSprite;

/**
 *
 * @author TiMMy
 */
public class BattleSpriteAnimationFramesTableModel extends AbstractTableModel<BattleSpriteAnimationFrame> {
    
    public BattleSpriteAnimationFramesTableModel() {
        super(new String[] { "Frame", "Battlesprite", "Duration", "X", "Y", "Wpn Frame", "H.Flip", "V.Flip", "Behind", "Wpn X", "Wpn Y" }, 255);
    }

    @Override
    public Class<?> getColumnType(int col) {
        switch (col) {
            case 6: case 7: case 8: return Boolean.class;
            default: return Byte.class;
        }
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return false;   //Cannot edit the index row
        } else if (row == 0 && column <= 4) {
            return false;   //Core data for first frame cannot be edited
        } else if (column >= 5 && getRow(row).getType() == BattleSprite.BattleSpriteType.ENEMY) {
            return false;   //Enemy weapon data cannot be edited
        } else {
            return true;
        }
    }
 
    @Override
    public boolean isRowLocked(int row) {
        return row == 0;
    }

    @Override
    protected BattleSpriteAnimationFrame createBlankItem(int row) {
        return new BattleSpriteAnimationFrame((byte)row, (byte)20, (byte)0, (byte)0);
    }

    @Override
    protected BattleSpriteAnimationFrame cloneItem(BattleSpriteAnimationFrame item) {
        return item.clone();
    }

    @Override
    protected Object getValue(BattleSpriteAnimationFrame item, int row, int col) {
        switch (col) {
            case 0: return row;
            case 1: return item.getBattleSpriteIndex();
            case 2: return item.getDuration();
            case 3: return item.getX();
            case 4: return item.getY();
            case 5: return item.getWeaponFrame();
            case 6: return item.getWeaponFlipH();
            case 7: return item.getWeaponFlipV();
            case 8: return item.getWeaponBehind();
            case 9: return item.getWeaponX();
            case 10: return item.getWeaponY();
        }
        return null;
    }

    @Override
    protected BattleSpriteAnimationFrame setValue(BattleSpriteAnimationFrame item, int col, Object value) {
        switch (col) {
            case 0: break;
            case 1: item.setBattleSpriteIndex((byte)value); break;
            case 2: item.setDuration((byte)value); break;
            case 3: item.setX((byte)value); break;
            case 4: item.setY((byte)value); break;
            case 5: item.setWeaponFrame((byte)value); break;
            case 6: item.setWeaponFlipH((boolean)value); break;
            case 7: item.setWeaponFlipV((boolean)value); break;
            case 8: item.setWeaponBehind((boolean)value); break;
            case 9: item.setWeaponX((byte)value); break;
            case 10: item.setWeaponY((byte)value); break;
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(BattleSpriteAnimationFrame item, int col) {
        switch (col) {
            case 3:
            case 4:
            case 9:
            case 10:
                return Byte.MIN_VALUE;
            default:
                return (byte)0;
        }
    }

    @Override
    protected Comparable<?> getMaxLimit(BattleSpriteAnimationFrame item, int col) {
        switch (col) {
            case 1:
                return (byte)(item.getBattleSpriteAnim().getFrameCount()-1);
            case 5:
                return (byte)(WeaponSprite.WEAPONSPRITE_FRAMES_LENGTH-1);
            default:
                return Byte.MAX_VALUE;
        }
    }
}
