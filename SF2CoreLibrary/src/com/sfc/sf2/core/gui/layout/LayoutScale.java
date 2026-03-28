/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.helpers.RenderScaleHelpers;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * A component to support scaling panel image
 * @author TiMMy
 */
public class LayoutScale extends BaseLayoutComponent {
    
    private int renderScaleIndex;
    private float renderScale;
    
    public LayoutScale() {
        this.renderScaleIndex = RenderScaleHelpers.RENDER_SCALE_1X;
        this.renderScale = 1f;
    }
    
    public LayoutScale(int layoutScale) {
        this.renderScaleIndex = RenderScaleHelpers.RENDER_SCALE_1X + layoutScale-1;
        this.renderScale = layoutScale;
    }

    public float getScale() {
        return renderScale;
    }

    public int getScaleIndex() {
        return renderScaleIndex;
    }

    public void setScaleIndex(int renderScaleIndex) {
        this.renderScaleIndex = renderScaleIndex;
        this.renderScale = RenderScaleHelpers.indexToRenderScale(renderScaleIndex);
    }
    
    public BufferedImage resizeImage(BufferedImage image) {
        if (renderScaleIndex == RenderScaleHelpers.RENDER_SCALE_1X) {
            //Scale = 1 so do nothing
            return image;
        } else if (renderScaleIndex < RenderScaleHelpers.RENDER_SCALE_1X) {
            //Scale is a fraction
            int scaleX = (int)(image.getWidth()*renderScale);
            int scaleY = (int)(image.getHeight()*renderScale);
            Image temp = image.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
            image = new BufferedImage(scaleX, scaleY, BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(temp, 0, 0 , null);
            return image;
        } else {
            int scale = (int)renderScale;
            //Scale is integer multiple
            BufferedImage newImage = new BufferedImage(image.getWidth(null)*scale, image.getHeight(null)*scale, BufferedImage.TYPE_INT_ARGB);
            Graphics g = newImage.getGraphics();
            g.drawImage(image, 0, 0, image.getWidth(null)*scale, image.getHeight(null)*scale, null);
            g.dispose();
            image.flush();
            return newImage;
        }
    }
}
