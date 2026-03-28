/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import java.util.Arrays;

/**
 *
 * @author TiMMy
 */
public class WeaponSpriteActionData implements IActionData<WeaponSpriteActionData> {
    
    private final WeaponSprite weaponSprite;
    private final Palette[] palettes;

    public WeaponSpriteActionData(WeaponSprite weaponSprite, Palette[] palettes) {
        this.weaponSprite = weaponSprite;
        this.palettes = palettes;
    }

    public WeaponSprite weaponSprite() {
        return weaponSprite;
    }

    public Palette[] palettes() {
        return palettes;
    }

    @Override
    public boolean isInvalidated(WeaponSpriteActionData other) {
        return this.weaponSprite.equals(other.weaponSprite) && Arrays.equals(this.palettes, other.palettes);
    }

    @Override
    public boolean canBeCombined(WeaponSpriteActionData other) {
        return this.weaponSprite.equals(other.weaponSprite);
    }

    @Override
    public WeaponSpriteActionData combine(WeaponSpriteActionData other) {
        return other;
    }
    
    @Override
    public String toString() {
        if (weaponSprite == null) {
            return "NULL";
        } else {
            return String.format("Weaponsprite: %d. Palettes: %d", weaponSprite.getIndex(), palettes.length);
        }
    }
}
