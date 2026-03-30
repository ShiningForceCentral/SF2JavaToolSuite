/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.controls;

import com.sfc.sf2.core.gui.controls.MultiComboBox.CheckItem;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.accessibility.Accessible;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import static javax.swing.JComponent.WHEN_FOCUSED;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author TiMMy
 */
public class MultiComboBox extends JComboBox<CheckItem> {

    protected boolean keepOpen;
    
    public MultiComboBox() {
        super();
    }
    
    @Override
    public void setModel(ComboBoxModel aModel) {
        CheckItem[] items = (CheckItem[].class).cast(Array.newInstance(CheckItem[].class.getComponentType(), aModel.getSize()));
        for (int i = 0; i < items.length; i++) {
            items[i] = new CheckItem(aModel.getElementAt(i), false);
        }
        ComboBoxModel<CheckItem> checkModel = new DefaultComboBoxModel<>(items);
        super.setModel(checkModel);
    }

    @Override
    public void setSelectedItem(Object data) {
        String[] names = null;
        if (data instanceof String[]) {
            names = (String[])data;
        } else if (data instanceof String) {
            names = ((String)data).split("\\|");
            for (int i = 0; i < names.length; i++) {
                if (names[i].length() > 0 && names[i].charAt(0) == ' ')
                    names[i] = names[i].substring(1);
            }
        }
        
        if (names != null) {
            ComboBoxModel<CheckItem> model = getModel();
            for (int m = 0; m < model.getSize(); m++) {
                CheckItem item = model.getElementAt(m);
                item.setSelected(false);
                for (int i = 0; i < names.length; i++) {
                    if (item.item.equals(names[i])) {
                        item.setSelected(true);
                        model.setSelectedItem(item);
                        break;
                    }
                }
            }
        } else {
            super.setSelectedItem(data);
        }
    }
    
    

    @Override
    public Object[] getSelectedObjects() {
        ArrayList<Object> list = new ArrayList<>();
        ComboBoxModel<CheckItem> model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).isSelected()) {
                list.add(model.getElementAt(i).item);
            }
        }
        Object[] selected = new Object[list.size()];
        selected = list.toArray(selected);
        return selected;
    }
    
    public String getObjectsString() {
        CheckItemListCellRenderer renderer = (CheckItemListCellRenderer)getRenderer();
        return renderer.getCheckItemString(getModel());
    }
    
    public void clearSelection() {
        ComboBoxModel<CheckItem> model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            model.getElementAt(i).setSelected(false);
        }
    }
    
    public void setSelected(int index, boolean selected) {
        ComboBoxModel<CheckItem> model = getModel();
        if (index < 0 || index >= model.getSize()) return;
        CheckItem item = model.getElementAt(index);
        item.setSelected(selected);
        model.setSelectedItem(item);
    }
    
    public void setSelected(Object item, boolean selected) {
        ComboBoxModel<CheckItem> model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).item.equals(item)) {
                model.getElementAt(i).setSelected(selected);
                model.setSelectedItem(model.getElementAt(i));
                return;
            }
        }
    }
    
    public void setSelected(String[] items) {
        ComboBoxModel<CheckItem> model = getModel();
        clearSelection();
        for (int i = 0; i < items.length; i++) {
            for (int e = 0; e < model.getSize(); e++) {
                CheckItem element = model.getElementAt(e);
                if (items[i].equals(element.toString())) {
                    element.setSelected(true);
                    model.setSelectedItem(element);
                    break;
                }
            }
        }
    }
    
    public void setSelected(Object[] items) {
        ComboBoxModel<CheckItem> model = getModel();
        clearSelection();
        for (int i = 0; i < items.length; i++) {
            model.setSelectedItem(items[i]);
        }
    }

    @Override
    public void updateUI() {
        setRenderer(null);
        super.updateUI();
        Accessible a = getAccessibleContext().getAccessibleChild(0);
        if (a instanceof ComboPopup) {
            ((ComboPopup) a).getList().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JList<?> list = (JList<?>) e.getComponent();
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        keepOpen = true;
                        updateItem(list.locationToIndex(e.getPoint()));
                    }
                }
            });
        }
        setRenderer(new CheckItemListCellRenderer());
        initActionMap();
    }

    protected void initActionMap() {
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
        getInputMap(WHEN_FOCUSED).put(ks, "checkbox-select");
        getActionMap().put("checkbox-select", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Accessible a = getAccessibleContext().getAccessibleChild(0);
                if (a instanceof ComboPopup) {
                    updateItem(((ComboPopup) a).getList().getSelectedIndex());
                }
            }
        });
    }

    protected void updateItem(int index) {
        if (isPopupVisible() && index >= 0) {
            CheckItem item = (CheckItem)getItemAt(index);
            item.setSelected(!item.isSelected());
            setSelectedIndex(-1);
            setSelectedItem(item);
        }
    }

    @Override
    public void setPopupVisible(boolean v) {
        if (keepOpen) {
            keepOpen = false;
        } else {
            super.setPopupVisible(v);
        }
    }
    
    private final class CheckItemListCellRenderer implements ListCellRenderer<CheckItem> {

        private final JPanel panel = new JPanel(new BorderLayout());
        private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        private final JCheckBox check = new JCheckBox();

        @Override
        public Component getListCellRendererComponent(JList<? extends CheckItem> list, CheckItem value, int index, boolean isSelected, boolean cellHasFocus) {
            panel.removeAll();
            check.setOpaque(false);
            Component c = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (index < 0) {
                String txt = getCheckItemString((ListModel<CheckItem>)list.getModel());
                JLabel l = (JLabel) c;
                l.setText(txt.isEmpty() ? " " : txt);
                l.setOpaque(false);
                l.setForeground(list.getForeground());
                panel.setOpaque(false);
            } else {
                check.setSelected(value.isSelected());
                panel.add(check, BorderLayout.WEST);
                panel.setOpaque(true);
                panel.setBackground(c.getBackground());
            }
            panel.add(c);
            return panel;
        }

        protected String getCheckItemString(ListModel<CheckItem> model) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < model.getSize(); i++) {
                if (model.getElementAt(i).isSelected()) {
                    if (sb.length() > 0) sb.append("|");
                    sb.append(model.getElementAt(i).toString());
                }
            }
            return sb.toString();
        }
    }
    
    class CheckItem {
        private final Object item;
        private boolean selected;

        protected CheckItem(Object text, boolean selected) {
            this.item = text;
            this.selected = selected;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean isSelected) {
            selected = isSelected;
        }

        @Override
        public String toString() {
            return item.toString();
        }
    }
}