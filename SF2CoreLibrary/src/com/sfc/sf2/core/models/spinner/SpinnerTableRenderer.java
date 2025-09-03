/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.spinner;

import java.awt.Component;
import javax.swing.AbstractSpinnerModel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author TiMMy
 */
public class SpinnerTableRenderer implements TableCellRenderer {

    private final JSpinner spinner = new JSpinner();
    private final JTextField text = new JTextField();
    
    public SpinnerTableRenderer(AbstractSpinnerModel model) {
        super();
        text.setHorizontalAlignment(SwingConstants.TRAILING);
        text.setEnabled(false);
        text.setBorder(null);
        text.setDisabledTextColor(null);
        spinner.setModel(model);
        spinner.setUI(new LeftRightSpinnerUI());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (table.isCellEditable(row, column)) {
            spinner.setValue(value);
            return spinner;
        } else {
            text.setText(value.toString());
            return text;
        }
    }
}
