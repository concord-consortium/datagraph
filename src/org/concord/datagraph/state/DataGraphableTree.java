/*
 * Created on Mar 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.concord.datagraph.engine.DataGraphable;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataGraphableTree extends JPanel
{
    public DataGraphableTree()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));        
    }
    
    public void addGraphable(DataGraphable graphable)
    {
        JLabel gLabel = new JLabel();
        gLabel.setText(graphable.getLabel());
        gLabel.setForeground(graphable.getColor());
        add(gLabel);
    }
}
