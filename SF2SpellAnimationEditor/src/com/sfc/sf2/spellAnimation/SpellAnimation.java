/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation;

import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author TiMMy
 */
public class SpellAnimation {
    
    private Tileset spellGraphic;
    private SpellSubAnimation[] spellSubAnimations;

    public Tileset getSpellGraphic() {
        return spellGraphic;
    }

    public void setSpellGraphic(Tileset spellGraphic) {
        this.spellGraphic = spellGraphic;
    }
    
    public Palette getPalette() {
        if (spellGraphic == null) {
            return null;
        }
        return spellGraphic.getPalette();
    }

    public SpellSubAnimation[] getSpellSubAnimations() {
        return spellSubAnimations;
    }

    public void setSpellSubAnimations(SpellSubAnimation[] spellSubAnimations) {
        this.spellSubAnimations = spellSubAnimations;
    }
}
