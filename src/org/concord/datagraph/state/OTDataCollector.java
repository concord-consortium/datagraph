/*
 * Created on Feb 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.concord.framework.otrunk.view.OTObjectView;

/**
 * @author scott
 *
 * show a data field with a button when the button is pressed
 * a dialog box should show up with a graph.  At the bottom of
 * the graph that saves the last value that is collected. 
 *  
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataCollector 
	implements OTObjectView, ActionListener 
{

	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.view.OTObjectView#getComponent(boolean)
	 */
	public JComponent getComponent(boolean editable) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
