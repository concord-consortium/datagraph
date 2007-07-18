/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01742
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
 * END LICENSE */

/*
 * Last modification information:
 * $Revision: 1.23 $
 * $Date: 2007-07-18 17:04:54 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.Dimension;

import javax.swing.JComponent;

import org.concord.datagraph.ui.DataGraph;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;


/**
 * PfDataGraphView
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public class OTDataGraphView extends AbstractOTJComponentView
{
	OTDataGraph pfObject;
	DataGraph dataGraph;
	DataGraphManager manager;
	
	/* (non-Javadoc)
	 * @see org.concord.portfolio.views.PortfolioView#getComponent(boolean)
	 */
	public JComponent getComponent(OTObject otObject, boolean editable)
	{
		this.pfObject = (OTDataGraph)otObject;

		//dataGraph = new DataGraph();
		//dataGraph.changeToDataGraphToolbar();
				
		//manager = new DataGraphStateManager(pfObject, dataGraph);
		manager = new DataGraphManager(pfObject, viewContext, editable);

		dataGraph = manager.getDataGraph();
				
		dataGraph.setAutoFitMode(DataGraph.AUTO_SCROLL_RUNNING_MODE);
				
		dataGraph.setPreferredSize(new Dimension(400,320));
		
		return dataGraph;				    
	}
	
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
     */
    public void viewClosed()
    {
    	manager.viewClosed();
    }
}
