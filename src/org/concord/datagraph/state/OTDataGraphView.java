
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
 * $Revision: 1.10 $
 * $Date: 2005-04-05 04:56:23 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.datagraph.ui.AddDataPointLabelAction;
import org.concord.datagraph.ui.DataGraph;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;
import org.concord.graph.engine.SelectableList;
import org.concord.swing.SelectableToggleButton;


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
	DataGraphStateManager manager;
	SelectableList notesLayer;
	
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
		dataGraph.changeToDataGraphToolbar();
		
		//Add notes button
		notesLayer = new SelectableList();
		dataGraph.getGraph().add(notesLayer);
		SelectableToggleButton addNoteButton = new SelectableToggleButton(new AddDataPointLabelAction(notesLayer, dataGraph.getObjList()));
		dataGraph.getToolBar().addButton(addNoteButton, "Add a note to a point in the graph");
		//
		
		manager = new DataGraphStateManager(pfObject, dataGraph);
		manager.initialize(editable);
				
		dataGraph.setAutoFitMode(DataGraph.AUTO_SCROLL_RUNNING_MODE);
		
		JPanel graphWrapper;
		
		dataGraph.setPreferredSize(new Dimension(400,320));
		
		return dataGraph;				    
	}
}
