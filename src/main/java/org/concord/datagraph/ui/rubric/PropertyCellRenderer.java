package org.concord.datagraph.ui.rubric;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;

public class PropertyCellRenderer implements ListCellRenderer, TableCellRenderer {
    
    HashMap<Property, String> modeToStringMap = new HashMap<Property, String>();
    HashMap<Property, JLabel> modeToLabelMap = new HashMap<Property, JLabel>();
    
    Color blue = new Color(0x9999ff);
    
    public PropertyCellRenderer() {
        super();
        
        modeToStringMap.put(Property.BEGINNING_X, "Beginning X");
        modeToStringMap.put(Property.BEGINNING_Y, "Beginning Y");
        modeToStringMap.put(Property.ENDING_X, "Ending X");
        modeToStringMap.put(Property.ENDING_Y, "Ending Y");
        modeToStringMap.put(Property.SLOPE, "Slope");
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        JLabel label = modeToLabelMap.get(value);
        if (label == null) {
            label = new JLabel(modeToStringMap.get(value));
            label.setOpaque(true);
            modeToLabelMap.put((Property)value, label);
        }
        
        if (isSelected) {
            label.setBackground(blue);
        } else {
            label.setBackground(Color.WHITE);
        }
        return label;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return getListCellRendererComponent(null, value, 0, isSelected, hasFocus);
    }

}
