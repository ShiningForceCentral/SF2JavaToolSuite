/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.controls;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.BeanProperty;
import javax.swing.ImageIcon;

/**
 *
 * @author TiMMy
 */
public class IconPanel extends Component {
    
    private ImageIcon icon;
    
    @BeanProperty(preferred = true, visualUpdate = true, description = "The icon to display.")
    public void setIcon(String path) {
        icon = new ImageIcon(getClass().getClassLoader().getResource(path));
        setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    }
    
    @Override
    public void paint(Graphics g) {
        if (icon != null) {
            g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
        } else {
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
