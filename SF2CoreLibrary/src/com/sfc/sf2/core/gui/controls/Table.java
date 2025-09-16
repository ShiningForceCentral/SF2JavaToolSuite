/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.controls;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.core.models.SelectionInterval;
import com.sfc.sf2.core.models.spinner.SpinnerTableEditor;
import com.sfc.sf2.core.models.spinner.SpinnerTableRenderer;
import java.beans.BeanProperty;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author TiMMy
 */
public class Table extends javax.swing.JPanel {

    private AbstractTableModel tableModel;
    
    /**
     * Creates new form Table
     */
    public Table() {
        super();
        initComponents();
        tableModel = getModel();
    }
    
    public AbstractTableModel getModel() {
        return (AbstractTableModel)jTable.getModel();
    }
    
    public boolean getButtonsVisible() {
        return jPanelButtons.isVisible();
    }
    
    @BeanProperty(preferred = true, visualUpdate = true, description = "Whether or not to show a border around each cell.")
    public void setRowBorders(boolean visible) {
        jTable.setDrawBorder(visible);
    }
    
    @BeanProperty(preferred = true, visualUpdate = true, description = "Whether or not to show the add, remove, etc buttons.")
    public void setButtonsVisible(boolean visible) {
        jPanelButtons.setVisible(visible);
        jPanelButtons.setEnabled(visible);
    }
    
    /**
     * This is a fix for default JTable behaviour where they will not show the horizontal scroll bar even if the table is too small.
     * In most cases, though, you prefer the table to resize to fit the viewport.
     */
    @BeanProperty(preferred = true, visualUpdate = true, description = "This is a fix for default JTable behaviour where they will not show the horizontal scroll bar even if the table is too small.\n" +
            "In most cases, though, you prefer the table to resize to fit the viewport.")
    public void setHorizontalScrolling(boolean horizontalScrolling) {
        jTable.setHorizontalScrolling(horizontalScrolling);
    }
    
    @BeanProperty(preferred = true, visualUpdate = true, description = "Set the data model for the table.")
    public void setModel(AbstractTableModel model) {
        tableModel = model;
        jTable.setModel(model);
    }
    
    @BeanProperty(enumerationValues = {
        "javax.swing.ListSelectionModel.SINGLE_SELECTION",
        "javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION",
        "javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION"},
        preferred = true, visualUpdate = true, description = "The selection mode used by the row and column selection models.")
    public void setSelectionMode(int mode) {
        jTable.setSelectionMode(mode);
    }
    
    @BeanProperty(preferred = true, visualUpdate = false, description = "Sets text fields in the table as single-click to edit, rather than having to double-click.")
    public void setSingleClickText(boolean singleClick) {
        if (singleClick) {
            JTextField textField = new JTextField();
            DefaultCellEditor singleclick = new DefaultCellEditor(textField);
            singleclick.setClickCountToStart(1);
            jTable.setDefaultEditor(String.class, singleclick);
        } else {
            jTable.setDefaultEditor(String.class, null);
        }
    }
    
    @BeanProperty(preferred = true, visualUpdate = false, description = "Sets number fields in the table as spinners, rather than just numbers.")
    public void setSpinnerNumberEditor(boolean useSpinner) {
        if (useSpinner) {
            SpinnerNumberModel model = new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
            jTable.setDefaultEditor(Integer.class, new SpinnerTableEditor(model));
            model = new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
            jTable.setDefaultRenderer(Integer.class, new SpinnerTableRenderer(model));
            model = new SpinnerNumberModel(Byte.valueOf((byte)0), Byte.valueOf(Byte.MIN_VALUE), Byte.valueOf(Byte.MAX_VALUE), Byte.valueOf((byte)1));
            jTable.setDefaultEditor(Byte.class, new SpinnerTableEditor(model));
            model = new SpinnerNumberModel(Byte.valueOf((byte)0), Byte.valueOf(Byte.MIN_VALUE), Byte.valueOf(Byte.MAX_VALUE), Byte.valueOf((byte)1));
            jTable.setDefaultRenderer(Byte.class, new SpinnerTableRenderer(model));
        } else {
            jTable.setDefaultEditor(Integer.class, null);
            jTable.setDefaultEditor(Byte.class, null);
            jTable.setDefaultRenderer(Integer.class, null);
            jTable.setDefaultRenderer(Byte.class, null);
        }
    }
    
    /**
     * Use -1 to set for all columns
     */
    public void setMinColumnWidth(int column, int width) {
        TableColumnModel columns = jTable.getColumnModel();
        if (column == -1) {
            for (int i = 0; i < columns.getColumnCount(); i++) {
                columns.getColumn(i).setMinWidth(width);
                columns.getColumn(i).setPreferredWidth(width);
            }
        } else {
            columns.getColumn(column).setMinWidth(width);
            columns.getColumn(column).setPreferredWidth(width);
        }
    }
    
    /**
     * Use -1 to set for all columns
     */
    public void setMaxColumnWidth(int column, int width) {
        TableColumnModel columns = jTable.getColumnModel();
        if (column == -1) {
            for (int i = 0; i < columns.getColumnCount(); i++) {
                columns.getColumn(i).setMaxWidth(width);
                columns.getColumn(i).setPreferredWidth(width);
            }
        } else {
            columns.getColumn(column).setMaxWidth(width);
            columns.getColumn(column).setPreferredWidth(width);
        }
    }
  
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jTable = new com.sfc.sf2.core.models.JDisableableTable();
        jPanelButtons = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonClone = new javax.swing.JButton();
        jButtonUp = new javax.swing.JButton();
        jButtonDown = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Table"));
        setMinimumSize(new java.awt.Dimension(260, 260));
        setPreferredSize(new java.awt.Dimension(260, 260));

        jTable.setModel(new com.sfc.sf2.core.models.StringTableModel());
        jScrollPane.setViewportView(jTable);

        jButtonAdd.setText("Add");
        jButtonAdd.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonClone.setText("Clone");
        jButtonClone.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonClone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloneActionPerformed(evt);
            }
        });

        jButtonUp.setText("/\\");
            jButtonUp.setMargin(new java.awt.Insets(2, 2, 2, 2));
            jButtonUp.setPreferredSize(new java.awt.Dimension(26, 26));
            jButtonUp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonUpActionPerformed(evt);
                }
            });

            jButtonDown.setText("\\/");
            jButtonDown.setMargin(new java.awt.Insets(2, 2, 2, 2));
            jButtonDown.setPreferredSize(new java.awt.Dimension(26, 26));
            jButtonDown.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonDownActionPerformed(evt);
                }
            });

            jButtonRemove.setText("Remove");
            jButtonRemove.setMargin(new java.awt.Insets(2, 2, 2, 2));
            jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonRemoveActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanelButtonsLayout = new javax.swing.GroupLayout(jPanelButtons);
            jPanelButtons.setLayout(jPanelButtonsLayout);
            jPanelButtonsLayout.setHorizontalGroup(
                jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelButtonsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(jButtonClone, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12)
                    .addComponent(jButtonUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(jButtonDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                    .addComponent(jButtonRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanelButtonsLayout.setVerticalGroup(
                jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jButtonAdd)
                        .addComponent(jButtonRemove)
                        .addComponent(jButtonClone)
                        .addComponent(jButtonUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(jPanelButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addGap(0, 0, 0)
                    .addComponent(jPanelButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
        }// </editor-fold>//GEN-END:initComponents

    private SelectionInterval[] splitIntoIntervals(int[] selection) {
        ArrayList<SelectionInterval> selections = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < selection.length; i++) {
            if (i == selection.length-1 || selection[i]+1 != selection[i+1]) {
                selections.add(new SelectionInterval(selection[start], selection[i]));
                start = i+1;
            }
        }
        SelectionInterval[] sel = new SelectionInterval[selections.size()];
        return selections.toArray(sel);
    }
    
    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        if (tableModel == null) return;
        int[] rows = jTable.getSelectedRows();
        int row = rows.length == 0 ? -1 : rows[rows.length-1];
        if (tableModel.addRow(row)) {
            jTable.setRowSelectionInterval(row+1, row+1);
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveActionPerformed
        if (tableModel == null) return;
        SelectionInterval[] selection = splitIntoIntervals(jTable.getSelectedRows());
        jTable.clearSelection();
        int totalShift = 0;
        for (int i = 0; i < selection.length; i++) {
            SelectionInterval interval = tableModel.removeRows(selection[i].start()-totalShift, selection[i].end()-totalShift);
            if (interval.start() != -1) {
                totalShift += selection[i].end()-selection[i].start()+1;
                jTable.addRowSelectionInterval(interval.start(), interval.end());
            }
        }
    }//GEN-LAST:event_jButtonRemoveActionPerformed

    private void jButtonCloneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloneActionPerformed
        if (tableModel == null) return;
        SelectionInterval[] selection = splitIntoIntervals(jTable.getSelectedRows());
        jTable.clearSelection();
        for (int i = selection.length-1; i >= 0; i--) {
            SelectionInterval interval = tableModel.cloneRows(selection[i].start(), selection[i].end());
            jTable.addRowSelectionInterval(interval.start(), interval.end());
        }
    }//GEN-LAST:event_jButtonCloneActionPerformed

    private void jButtonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpActionPerformed
        if (tableModel == null) return;
        SelectionInterval[] selection = splitIntoIntervals(jTable.getSelectedRows());
        jTable.clearSelection();
        for (int i = 0; i < selection.length; i++) {
            SelectionInterval interval = tableModel.shiftUp(selection[i].start(), selection[i].end());
            jTable.addRowSelectionInterval(interval.start(), interval.end());
        }
    }//GEN-LAST:event_jButtonUpActionPerformed

    private void jButtonDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownActionPerformed
        if (tableModel == null) return;
        SelectionInterval[] selection = splitIntoIntervals(jTable.getSelectedRows());
        jTable.clearSelection();
        for (int i = 0; i < selection.length; i++) {
            SelectionInterval interval = tableModel.shiftDown(selection[i].start(), selection[i].end());
            jTable.addRowSelectionInterval(interval.start(), interval.end());
        }
    }//GEN-LAST:event_jButtonDownActionPerformed
        
    public synchronized void addTableModelListener(TableModelListener l) {
        jTable.getModel().addTableModelListener(l);
    }

    public synchronized void removeTableModelListener(TableModelListener l) {
        jTable.getModel().removeTableModelListener(l);
    }
    
    public synchronized void addListSelectionListener(ListSelectionListener l) {
        jTable.getSelectionModel().addListSelectionListener(l);
    }

    public synchronized void removeListSelectionListenerModelListener(ListSelectionListener l) {
        jTable.getSelectionModel().removeListSelectionListener(l);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonClone;
    private javax.swing.JButton jButtonDown;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JButton jButtonUp;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JScrollPane jScrollPane;
    public com.sfc.sf2.core.models.JDisableableTable jTable;
    // End of variables declaration//GEN-END:variables
}
