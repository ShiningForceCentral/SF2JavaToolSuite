/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.controls.Console;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Scales the scroll speed of the panel based on the size of the image
 * @author TiMMy
 */
public class LayoutScrollNormaliser extends BaseLayoutComponent implements ComponentListener {
    
    JScrollPane scrollPane;
    
    public LayoutScrollNormaliser(AbstractLayoutPanel panel) {
        java.awt.EventQueue.invokeLater(() -> { findScrollPane(panel); });
    }
    
    private void findScrollPane(Container panel) {
        try {
            Container parent = panel;
            for (int i = 0; i < 3; i++) {
                parent = parent.getParent();
                if (parent instanceof JScrollPane) {
                    scrollPane = (JScrollPane)parent;
                    break;
                }
            }
        } catch (Exception e) { }
        if (scrollPane == null) {
            Console.logger().warning("LayoutScrollNormaliser could not find vertical scroll bar for " + panel.getName() + ". LayoutScrollNormaliser will be disabled");
            setEnabled(false);
        } else {
            panel.addComponentListener(this);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        scrollPane.getHorizontalScrollBar().setUnitIncrement(e.getComponent().getSize().width/50);
        scrollPane.getVerticalScrollBar().setUnitIncrement(e.getComponent().getSize().height/50);
    }
    
    @Override
    public void componentMoved(ComponentEvent e) { }
    @Override
    public void componentShown(ComponentEvent e) { }
    @Override
    public void componentHidden(ComponentEvent e) { }
}
