package org.concord.datagraph.ui.rubric;

import java.awt.Color;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;

import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Operation;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;

public class SegmentCriteriaTable extends ScrollingTable {
    private static final long serialVersionUID = 1L;

    public SegmentCriteriaTable(SegmentCriterionTableModel tableModel) {
        super(tableModel);
        
        setGridColor(Color.LIGHT_GRAY);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JComboBox propertyChoices = new JComboBox();
        propertyChoices.setRenderer(new PropertyCellRenderer());
        propertyChoices.addItem(Property.BEGINNING_X);
        propertyChoices.addItem(Property.BEGINNING_Y);
        propertyChoices.addItem(Property.ENDING_X);
        propertyChoices.addItem(Property.ENDING_Y);
        propertyChoices.addItem(Property.SLOPE);
        propertyChoices.addItem(Property.DELTA_X);
        
        JComboBox operationChoices = new JComboBox();
        operationChoices.setRenderer(new OperationCellRenderer());
        operationChoices.addItem(Operation.EQUAL_TO);
        operationChoices.addItem(Operation.LESS_THAN);
        operationChoices.addItem(Operation.GREATER_THAN);
        operationChoices.addItem(Operation.LESS_THAN_OR_EQUAL_TO);
        operationChoices.addItem(Operation.GREATER_THAN_OR_EQUAL_TO);
        
        setDefaultRenderer(Property.class, new PropertyCellRenderer());
        setDefaultEditor(Property.class, new DefaultCellEditor(propertyChoices));
        setDefaultRenderer(Operation.class, new OperationCellRenderer());
        setDefaultEditor(Operation.class, new DefaultCellEditor(operationChoices));
    }
}
