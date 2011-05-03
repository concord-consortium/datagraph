package org.concord.datagraph.ui.rubric;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;

public class SegmentTable extends JTable {

    public SegmentTable(GraphSegmentTableModel tableModel) {
        super(tableModel);
        
        setGridColor(Color.LIGHT_GRAY);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    // Custom viewport height tracking code, adapted from JList's implementation
    @Override
    public boolean getScrollableTracksViewportHeight() {
        if (getRowCount() <= 0) {
            return true;
        }
        if (getParent() instanceof JViewport) {
            return (((JViewport)getParent()).getHeight() > getPreferredSize().height);
        }
        return false;
    }
}
