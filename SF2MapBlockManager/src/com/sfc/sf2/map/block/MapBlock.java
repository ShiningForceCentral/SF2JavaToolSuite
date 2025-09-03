/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import com.sfc.sf2.graphics.Block;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class MapBlock extends Block {
    public static final int MAP_FLAG_MASK_EVENTS = 0x3C00;
    public static final int MAP_FLAG_MASK_NAV = 0xC000;
       
    private int flags;
    
    private BufferedImage explorationFlagImage;
    private BufferedImage interactionFlagImage;
    
    public MapBlock(int index, int flags, Tile[] tiles) {
        super(index, tiles);
        this.flags = flags;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public BufferedImage getExplorationFlagImage() {
        return explorationFlagImage;
    }

    public void setExplorationFlagImage(BufferedImage explorationFlagImage) {
        this.explorationFlagImage = explorationFlagImage;
    }

    public BufferedImage getInteractionFlagImage() {
        return interactionFlagImage;
    }

    public void setInteractionFlagImage(BufferedImage interactionFlagImage) {
        this.interactionFlagImage = interactionFlagImage;
    }
    
    public boolean equalsIgnoreTiles(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof MapBlock)) return false;
        MapBlock block = (MapBlock)obj;
        if (this.index == block.getIndex() && this.flags == block.getFlags()) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean equalsWithPriority(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof MapBlock)) return false;
        MapBlock block = (MapBlock)obj;
        if (this.index == block.getIndex() && this.flags == block.getFlags()) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override 
    public MapBlock clone() {
        MapBlock clone = new MapBlock(index, flags, tiles.clone());
        clone.setExplorationFlagImage(this.explorationFlagImage);
        clone.setInteractionFlagImage(this.interactionFlagImage);
        return clone;
    }
    
    public static MapBlock EmptyMapBlock(int index, int flags, Palette palette) {
        return new MapBlock(index, 0, EmptyTilset(palette, TILE_WIDTH).getTiles());
    }
}
