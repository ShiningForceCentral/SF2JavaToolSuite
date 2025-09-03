/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.spinner;

import com.formdev.flatlaf.ui.FlatArrowButton;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.formdev.flatlaf.ui.FlatSpinnerUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author TiMMy
 */
public class LeftRightSpinnerUI extends FlatSpinnerUI {

    private static EmptyBorder nextBorder = new EmptyBorder(0, 0, 0, 5);
    private static EmptyBorder prevBorder = new EmptyBorder(0, 5, 0, 0);
    
    public static ComponentUI createUI(JComponent c) {
        return new LeftRightSpinnerUI();
    }

    @Override
    protected Component createNextButton() {
        JButton b = createArrowButton(SwingConstants.NORTH);
        b.setName("Spinner.nextButton");
        b.setBorder(nextBorder);
        installNextButtonListeners(b);
        return b;
    }

    @Override
    protected Component createPreviousButton() {
        JButton b = createArrowButton(SwingConstants.SOUTH);
        b.setName("Spinner.previousButton");
        b.setBorder(prevBorder);
        installPreviousButtonListeners(b);
        return b;
    }

    // copied from BasicSpinnerUI
    private JButton createArrowButton(int direction) {
        JButton b = new FlatArrowButton(direction, "Spinner", getForeground(true), getForeground(false), buttonHoverArrowColor, focusedBackground, buttonPressedArrowColor, focusedBackground);
        b.setMinimumSize(new Dimension(10, 5));
        return b;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.removeAll();
        c.setLayout(new BorderLayout(-3, 0));
        JComponent editor = createEditor();
        c.add(editor, BorderLayout.CENTER);
        c.add(createNextButton(), BorderLayout.EAST);
        c.add(createPreviousButton(), BorderLayout.WEST);
        JTextField textField = ((DefaultEditor)editor).getTextField();
        JComponent container = (JComponent)editor.getParent();
        container.setMinimumSize(new Dimension(30, 26));
    }
}