/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationListener.AnimationFrameEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author TiMMy
 */
public class LayoutAnimator extends BaseLayoutComponent implements ActionListener {
    
    private final AnimationController animationController;
    private final ArrayList<AnimationListener> animationListeners;
    
    private Timer idleTimer = null;
    private int currentFrame = 0;
    private int frameMax;
    private int finalFrameDelay = 0;
    private boolean variableSpeed;
    private boolean loop;
    private boolean isFinalFrameDelay = false;
    
    public LayoutAnimator(AnimationController controller) {
        super();
        this.animationController = controller;
        animationListeners = new ArrayList<>();
        animationListeners.add(controller);
        setEnabled(false);
    }
    
    public void addAnimationListener(AnimationListener listener) {
        animationListeners.add(listener);
    }
    
    public void removeAnimationListener(AnimationListener listener) {
        animationListeners.add(listener);
        if (animationListeners.size() == 0) {
            setEnabled(false);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            stopAnimation();
        }
        super.setEnabled(enabled);
    }
    
    public boolean isAnimating() {
        return isEnabled();
    }

    public int getFrame() {
        return currentFrame;
    }

    public void setFrame(int frame) {
        if (isAnimating()) {
            updateFrame(frame);
        } else {
            this.currentFrame = frame;
            AnimationFrameEvent evt = new AnimationFrameEvent(currentFrame);
            for (int i = 0; i < animationListeners.size(); i++) {   
                animationListeners.get(i).animationFrameUpdated(evt);
            }
        }
    }
    
    public void startAnimation(int speed) {
        startAnimation(speed, 100000, true, finalFrameDelay, false);
    }
    
    public void startAnimation(int speed, int frameMax, boolean loop) {
        startAnimation(speed, frameMax, loop, finalFrameDelay, false);
    }
    
    public void startAnimation(int speed, int frameMax, boolean loop, int finalFrameDelay) {
        startAnimation(speed, frameMax, loop, finalFrameDelay, false);
    }
    
    public void startAnimation(int speed, int frameMax, boolean loop, int finalFrameDelay, boolean variableAnimationSpeed) {
        this.loop = loop;
        this.currentFrame = 0;
        this.frameMax = frameMax;
        this.variableSpeed = variableAnimationSpeed;
        this.finalFrameDelay = finalFrameDelay;
        this.isFinalFrameDelay = false;
        if (!isEnabled()) {
            currentFrame = 0;
            idleTimer = new Timer(speed*1000/60, this);
            if (!variableAnimationSpeed) idleTimer.setRepeats(loop);
            idleTimer.start();
        }
        super.setEnabled(true);
        if (animationListeners != null) {
            AnimationFrameEvent evt = new AnimationFrameEvent(0);
            for (int i = 0; i < animationListeners.size(); i++) {   
                animationListeners.get(i).animationFrameUpdated(evt);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != idleTimer) return;
        updateFrame(currentFrame+1);
    }
    
    private void updateFrame(int frame) {
        currentFrame = frame;
        if (currentFrame > frameMax) {
            if (loop) {
                currentFrame=0;
            } else if (finalFrameDelay > 0 && !isFinalFrameDelay) {
                isFinalFrameDelay = true;
                currentFrame = frameMax;
            } else {
                currentFrame = 0;
                isFinalFrameDelay = false;
                stopAnimation();
            }
        }
        AnimationFrameEvent evt = new AnimationFrameEvent(currentFrame);
        for (int i = 0; i < animationListeners.size(); i++) {
            animationListeners.get(i).animationFrameUpdated(evt);
        }
        if (isAnimating() && animationController != null && variableSpeed) {
            int delay = isFinalFrameDelay ? finalFrameDelay : animationController.getAnimationFrameSpeed(currentFrame)*1000/60;
            idleTimer.setInitialDelay(delay);
            idleTimer.setDelay(delay);
            idleTimer.restart();
        }
    }
    
    public void stopAnimation() {
        if (idleTimer != null) {
            idleTimer.stop();
            idleTimer = null;
            setEnabled(false);
        }
    }
    
    public interface AnimationController extends AnimationListener {
        public int getAnimationFrameSpeed(int currentAnimFrame);
    }
    
    public interface AnimationListener extends java.util.EventListener {
        
        public void animationFrameUpdated(AnimationFrameEvent e);
    
        public class AnimationFrameEvent {
            private int currentFrame;

            public AnimationFrameEvent(int currentFrame) {
                this.currentFrame = currentFrame;
            }

            public int getCurrentFrame() {
                return currentFrame;
            }
        }
    }
}
