package org.concord.datagraph.ui.rubric;

import javax.swing.table.AbstractTableModel;

import org.concord.datagraph.state.rubric.OTGraphSegment;
import org.concord.framework.otrunk.OTObjectList;

public class GraphSegmentTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private String[] columnNames = { "# of criteria", "Optional?" };

    private OTObjectList segments;

    public GraphSegmentTableModel(OTObjectList segments)
    {
        this.segments = segments;
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public int getRowCount()
    {
        return segments.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        OTGraphSegment segment = (OTGraphSegment) segments.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return segment.getCriteria().size();
        case 1:
            return segment.getOptional();
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
            return Integer.class;
        case 1:
            return Boolean.class;
        default:
            return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        if (col == 0) {
            return false;
        }
        return true;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        OTGraphSegment segment = getSegmentAt(row);
        
        switch (col) {
        case 1:
            segment.setOptional(((Boolean)value).booleanValue());
            break;
        default:
            break;
        }
        
        fireTableCellUpdated(row, col);
    }

    public OTGraphSegment getSegmentAt(int rowIndex) {
        return (OTGraphSegment) segments.get(rowIndex);
    }
}
