package org.concord.datagraph.ui.rubric;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.TableModel;

public class ScrollingTable extends JTable {
    private static final long serialVersionUID = 1L;

    public ScrollingTable(TableModel model) {
        super(model);
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
