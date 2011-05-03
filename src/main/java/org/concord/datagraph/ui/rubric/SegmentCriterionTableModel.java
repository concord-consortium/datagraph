package org.concord.datagraph.ui.rubric;

import javax.swing.table.AbstractTableModel;

import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Operation;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;
import org.concord.framework.otrunk.OTObjectList;

public class SegmentCriterionTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private String[] columnNames = { "Property", "Operation", "Expected Value", "Points", "Optional?" };

    private OTObjectList criteria;

    public SegmentCriterionTableModel(OTObjectList criteria)
    {
        this.criteria = criteria;
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public int getRowCount()
    {
        return criteria.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        OTGraphSegmentCriterion criterion = (OTGraphSegmentCriterion) criteria.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return criterion.getProperty();
        case 1:
            return criterion.getOperation();
        case 2:
            return criterion.getExpectedValue();
        case 3:
            return criterion.getPoints();
        case 4:
            return criterion.getOptional();
        default:
            return null;
        }
    }

    @Override
    public String getColumnName(int col)
    {
        return columnNames[col];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return Property.class;
        case 1:
            return Operation.class;
        case 2:
            return Double.class;
        case 3:
            return Double.class;
        case 4:
            return Boolean.class;
        default:
            return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        return true;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        OTGraphSegmentCriterion segment = getCriterionAt(row);
        
        try {
            
            
            switch (col) {
            case 0:
                segment.setProperty((Property) value);
                break;
            case 1:
                segment.setOperation((Operation) value);
                break;
            case 2:
                segment.setExpectedValue(Double.parseDouble((String) value));
                break;
            case 3:
                segment.setPoints(Double.parseDouble((String) value));
                break;
            case 4:
                segment.setOptional(Boolean.parseBoolean((String) value));
                break;
            default:
                break;
            }
            
            fireTableCellUpdated(row, col);
        } catch (NumberFormatException e) {
            
        }
    }

    public OTGraphSegmentCriterion getCriterionAt(int rowIndex) {
        return (OTGraphSegmentCriterion) criteria.get(rowIndex);
    }
}
