/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-01-27 16:43:13 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import javax.swing.JComponent;

import org.concord.datagraph.ui.DataGraph;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;


/**
 * PfDataGraphView
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public class OTDataGraphView
	implements OTObjectView
{
	OTDataGraph pfObject;
	protected OTViewContainer viewContainer;
	DataGraph dataGraph;
	
	public OTDataGraphView(OTDataGraph pfDataGraph, OTViewContainer vContainer)
	{
		this.pfObject = pfDataGraph;
		viewContainer = vContainer;
	}

	/* (non-Javadoc)
	 * @see org.concord.portfolio.views.PortfolioView#getComponent(boolean)
	 */
	public JComponent getComponent(boolean editable)
	{
		dataGraph = new DataGraph();

		DataGraphStateManager manager = new DataGraphStateManager(pfObject, dataGraph);
		manager.initialize();
				
		return dataGraph;
	}
}
