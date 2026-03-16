/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.animation.gui;

import com.sfc.sf2.core.gui.layout.LayoutAnimator;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;

/**
 *
 * @author TiMMy
 */
public class MapModifiedTilesetLayoutPanel extends MapAnimationTilesetLayoutPanel implements LayoutAnimator.AnimationController  {
    
    private int selectedTileset = -1;

    public MapModifiedTilesetLayoutPanel() {
        super();
        useFrameDestinationStart = true;
        animator = new LayoutAnimator(this);
    }

    public MapAnimation getMapAnimation() {
        return mapAnimation;
    }

    @Override
    public void setMapAnimation(MapAnimation mapAnimation) {
        this.mapAnimation = mapAnimation;
        redraw();
        setSelectedTileset(selectedTileset);
    }
    
    public void setSelectedTileset(int tileset) {
        selectedTileset = tileset;
        if (mapAnimation == null) {
            setTileset(null);
        } else if (selectedTileset == -1) {
            setTileset(mapAnimation.getAnimationTileset());
        } else {
            setTileset(mapAnimation.getModifiedTilesets()[selectedTileset]);
        }
        redraw();
    }
    
    public void setPreviewAnim(boolean play) {
        if (play && hasData()) {
            selectedFrame = 0;
            MapAnimationFrame[] frames = mapAnimation.getFrames();
            animator.startAnimation(frames[selectedFrame].getDelay(), frames.length-1, true, true);
        } else {
            animator.stopAnimation();
        }
    }

    @Override
    public void animationFrameUpdated(AnimationFrameEvent e) {
        if (mapAnimation == null) {
            setPreviewAnim(false);
            return;
        }
        selectedFrame = e.getCurrentFrame();
        mapAnimation.updateTileset(selectedFrame);
        if (mapAnimation.getFrames()[selectedFrame].getDelay() <= 1) {
            while (mapAnimation.getFrames()[selectedFrame].getDelay() <= 1) {
                //To reduce load from very short frame updates
                selectedFrame++;
                if (selectedFrame >= mapAnimation.getFrames().length) {
                    selectedFrame = 0;
                }
                mapAnimation.updateTileset(selectedFrame);
            }
            animator.setFrame(selectedFrame);
        }
        if (selectedTileset >= 0) {
            setTileset(mapAnimation.getModifiedTilesets()[selectedTileset]);
        }
        redraw();
    }

    @Override
    public int getAnimationFrameSpeed(int currentAnimFrame) {
        return mapAnimation.getFrames()[currentAnimFrame].getDelay();
    }
}
