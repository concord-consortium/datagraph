package org.concord.datagraph.ui.rubric;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Operation;

public class OperationCellRenderer implements ListCellRenderer, TableCellRenderer {
    
    HashMap<Operation, String> modeToStringMap = new HashMap<Operation, String>();
    HashMap<Operation, JLabel> modeToLabelMap = new HashMap<Operation, JLabel>();
    
    Color blue = new Color(0x9999ff);
    
    public OperationCellRenderer() {
        super();
        // LESS_THAN, GREATER_THAN, EQUAL_TO, LESS_THAN_OR_EQUAL_TO, GREATER_THAN_OR_EQUAL_TO, POSITIVE, NEGATIVE
        modeToStringMap.put(Operation.LESS_THAN, "<");
        modeToStringMap.put(Operation.GREATER_THAN, ">");
        modeToStringMap.put(Operation.EQUAL_TO, "=");
        modeToStringMap.put(Operation.LESS_THAN_OR_EQUAL_TO, "<=");
        modeToStringMap.put(Operation.GREATER_THAN_OR_EQUAL_TO, ">=");
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        JLabel label = modeToLabelMap.get(value);
        if (label == null) {
            label = new JLabel(modeToStringMap.get(value));
            label.setOpaque(true);
            modeToLabelMap.put((Operation)value, label);
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
