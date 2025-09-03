/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.controls.Console;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Finds a parent {@link JComponent} of the {@code LayoutPanel} with a {@link TitledBorder} and appends the hovered tile coords
 * @author TiMMy
 */
public class LayoutCoordsHeader extends BaseMouseCoordsComponent {
    
    private JComponent coordsContainer;
    private TitledBorder coordsTitle;
    private String origTitle;
    
    private boolean showIndex;
    private int itemsPerRow;

    public LayoutCoordsHeader(AbstractLayoutPanel panel, int gridX, int gridY) {
        this(panel, gridX, gridY, false);
    }

    /**
     *
     * @param showIndex Whether to show the index (x+y*length) or the coords (x, y)
     */
    public LayoutCoordsHeader(AbstractLayoutPanel panel, int gridX, int gridY, boolean showIndex) {
        super(panel, gridX, gridY);
        this.showIndex = showIndex;
        setupListeners(null, this::onMouseMotion);
        java.awt.EventQueue.invokeLater(() -> { findCoordsTitle(panel); });
    }
    
    public void setItemsPerRow(int itemsPerRow) {
        this.itemsPerRow = itemsPerRow;
    }
    
    private void findCoordsTitle(Container panel) {
        try {
            Border border = null;
            Container parent = panel;
            for (int i = 0; i < 3; i++) {
                parent = parent.getParent();
                if (parent instanceof JScrollPane) {
                    coordsContainer = (JComponent)parent;
                    border = ((JScrollPane)coordsContainer).getViewportBorder();
                } else if (parent instanceof JComponent) {
                    coordsContainer = (JComponent)parent;
                    border = coordsContainer.getBorder();
                }
                if (border != null && border instanceof TitledBorder) {
                    coordsTitle = (TitledBorder)border;
                    break;
                }
            }
        } catch (Exception e) { }
        if (coordsTitle == null) {
            Console.logger().warning("LayoutCoordsHeader could not find title for " + panel.getName() + ". LayoutCoordsHeader will be disabled");
            setEnabled(false);
        } else {
            origTitle = coordsTitle.getTitle();
        }
    }

    private void onMouseMotion(GridMouseMoveEvent evt) {
        if (showIndex) {
            if (evt.x() < 0 || evt.y() < 0) {
                coordsTitle.setTitle(String.format("%s : ( - )", origTitle));
            } else {
                int index = evt.x()+evt.y()*itemsPerRow;
                coordsTitle.setTitle(String.format("%s : ( %d )", origTitle, index));
            }
        } else {
            if (evt.x() < 0 || evt.y() < 0) {
                coordsTitle.setTitle(String.format("%s : (-, -)", origTitle));
            } else {
                coordsTitle.setTitle(String.format("%s : (%d, %d)", origTitle, evt.x(), evt.y()));
            }
        }
        coordsContainer.repaint();
    }
}
