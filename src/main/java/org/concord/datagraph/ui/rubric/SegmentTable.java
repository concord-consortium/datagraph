package org.concord.datagraph.ui.rubric;

import java.awt.Color;

import javax.swing.ListSelectionModel;

public class SegmentTable extends ScrollingTable {
    private static final long serialVersionUID = 1L;

    public SegmentTable(GraphSegmentTableModel tableModel) {
        super(tableModel);
        
        setGridColor(Color.LIGHT_GRAY);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
