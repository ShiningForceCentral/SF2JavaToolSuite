/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author TiMMy
 */
public abstract class AnimatedLayoutPanel extends AbstractLayoutPanel implements ActionListener {
    
    private Timer idleTimer = null;
    private int currentAnimFrame = 0;
    private int frameMax;
    private boolean animPlaying = false;
    private boolean variableAnimationSpeed;
    private int speed;
    private boolean loop;
    
    ActionListener animationUpdated;
    
    public boolean isAnimating() {
        return animPlaying;
    }
    
    public int getCurrentAnimationFrame() {
        return currentAnimFrame;
    }

    public void setCurrentAnimationFrame(int currentAnimationFrame) {
        this.currentAnimFrame = currentAnimationFrame;
    }
    
    protected int getFrameSpeed(int frame) {
        return speed;
    }
    
    public void setFrameUpdatedListener(ActionListener l) {
        animationUpdated = l;
    }
    
    public void removeFrameUpdatedListener() {
        animationUpdated = null;
    }
    
    public void startAnimation(int speed) {
        startAnimation(speed, 100000, true, false);
    }
    
    public void startAnimation(int speed, int frameMax, boolean loop) {
        startAnimation(speed, frameMax, loop, false);
    }
    
    public void startAnimation(int speed, int frameMax, boolean loop, boolean variableAnimationSpeed) {
        if (!hasData()) return;
        this.speed = speed;
        this.loop = loop;
        this.currentAnimFrame = 0;
        this.frameMax = frameMax;
        this.variableAnimationSpeed = variableAnimationSpeed;
        if (!animPlaying) {
            currentAnimFrame = 0;
            idleTimer = new Timer(speed*1000/60, this);
            if (!variableAnimationSpeed) idleTimer.setRepeats(loop);
            idleTimer.start();
        }
        animPlaying = true;
        animationFrameUpdated(0);
    }
    
    public void stopAnimation() {
        if (!hasData()) return;
        if (animPlaying) {
            animPlaying = false;
            idleTimer.stop();
            idleTimer = null;
        }
    }
    
    protected void animationFrameUpdated(int frame) {
        redraw();
        if (animationUpdated != null) {
            animationUpdated.actionPerformed(new ActionEvent(this, frame, null));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != idleTimer) return;
        
        currentAnimFrame++;
        if (currentAnimFrame > frameMax) {
            if (loop) {
                currentAnimFrame=0;
            } else {
                stopAnimation();
                return;
            }
        }
        animationFrameUpdated(currentAnimFrame);
        if (variableAnimationSpeed) {
            int delay = getFrameSpeed(currentAnimFrame)*1000/60;
            idleTimer.setInitialDelay(delay);
            idleTimer.setDelay(delay);
            idleTimer.restart();
        }
    }
}
