
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
 * Created on Jan 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;

import org.concord.data.state.OTDataStore;
import org.concord.data.stream.DataStoreUtil;
import org.concord.data.stream.PointsDataStore;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.ui.Grid2D;
import org.concord.graph.ui.SingleAxisGrid;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataGraphStateManagerTmp extends DataGraphStateManager 
{
	ControllableDataGraphable lastGraphable;
	
	public DataGraphStateManagerTmp(OTDataGraph pfDataGraph, DataGraph dataGraph)
	{
		super(pfDataGraph, dataGraph);
	}

	public ControllableDataGraphable getLastGraphable()
	{
		return lastGraphable;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.portfolio.views.PortfolioView#getComponent(boolean)
	 */
	public void initializeControllable()
	{
		xAxis = pfObject.getXDataAxis();
		yAxis = pfObject.getYDataAxis();
		
		pfGraphables = pfObject.getDataGraphables();

		// OTObjectList dataProducers = pfObject.getDataProducers();				
		DataFlowControlToolBar toolBar = null;
		
		dataGraph.setLimitsAxisWorld(xAxis.getMin(), xAxis.getMax(),
				yAxis.getMin(), yAxis.getMax());

		Grid2D grid = dataGraph.getGrid();

		SingleAxisGrid sXAxis = grid.getXGrid();
		sXAxis.setAxisLabel(xAxis.getLabel());
		SingleAxisGrid sYAxis = grid.getYGrid();
		sYAxis.setAxisLabel(yAxis.getLabel());
				
		// for each list item get the data producer object
		// add it to the data graph
		for(int i=0; i<pfGraphables.size(); i++) {
			OTDataGraphable dGraphable = (OTDataGraphable)pfGraphables.get(i);
			Object dataProducer = dGraphable.getDataProducer();
			OTDataStore dataStore = (OTDataStore)dGraphable.getDataStore();
			
			// dProducer.getDataDescription().setDt(0.1f);
			DataGraphable realGraphable = null;
			if(dataProducer instanceof DataProducer) {
				realGraphable = dataGraph.createDataGraphable((DataProducer)dataProducer);
				if(toolBar == null) {					
					toolBar = new DataFlowControlToolBar(dataGraph);
					dataGraph.add(toolBar, BorderLayout.SOUTH);
				}
				toolBar.addDataFlowObject((DataProducer)dataProducer);
			} else {
				realGraphable = new ControllableDataGraphable();
				realGraphable.setDataStore(dataStore, 0, 1);
				realGraphable.setConnectPoints(dGraphable.getConnectPoints());
				realGraphable.setShowCrossPoint(dGraphable.getDrawMarks());

				realGraphable.setLineWidth(3);
				lastGraphable = (ControllableDataGraphable)realGraphable;
			}
			// realGraphable.addGraphableListener(this);
			realGraphable.setColor(new Color(dGraphable.getColor()));
			dataGraph.addDataGraphable(realGraphable);
		}
		
		GraphableList graphableList = dataGraph.getObjList();
		graphableList.addGraphableListListener(this);				
	}
}
