package org.concord.datagraph.ui;

import javax.swing.table.AbstractTableModel;

import org.concord.datagraph.state.OTGraphSegment;
import org.concord.framework.otrunk.OTObjectList;

public class GraphSegmentTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private String[] columnNames = { "X1", "X2", "A", "B", "C" };

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
            return segment.getX1();
        case 1:
            return segment.getX2();
        case 2:
            return segment.getA();
        case 3:
            return segment.getB();
        case 4:
            return segment.getC();
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
    public boolean isCellEditable(int row, int col)
    {
        return true;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        OTGraphSegment segment = getSegmentAt(row);
        
        try {
            double val = Double.parseDouble((String) value);
            
            switch (col) {
            case 0:
                segment.setX1(val);
                break;
            case 1:
                segment.setX2(val);
                break;
            case 2:
                segment.setA(val);
                break;
            case 3:
                segment.setB(val);
                break;
            case 4:
                segment.setC(val);
                break;
            default:
                break;
            }
            
            fireTableCellUpdated(row, col);
        } catch (NumberFormatException e) {
            
        }
    }

    public OTGraphSegment getSegmentAt(int rowIndex) {
        return (OTGraphSegment) segments.get(rowIndex);
    }
}
