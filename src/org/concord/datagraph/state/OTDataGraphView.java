
/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

/*
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2005-02-23 15:50:22 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import javax.swing.JComponent;

import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.DataGraphToolbar;
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

		DataGraphToolbar dgToolbar = new DataGraphToolbar();
		dgToolbar.setButtonsMargin(0);
		dgToolbar.setFloatable(false);
		dataGraph.setToolBar(dgToolbar);
		
		DataGraphStateManager manager = new DataGraphStateManager(pfObject, dataGraph);
		manager.initialize(editable);
				
		return dataGraph;
	}
}
