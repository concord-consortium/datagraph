

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
 * $Revision: 1.5 $
 * $Date: 2005-02-14 06:19:19 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.EventObject;

import org.concord.data.state.OTDataStore;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataProducerProxy;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.event.GraphableListListener;
import org.concord.graph.examples.GraphWindowToolBar;
import org.concord.graph.ui.Grid2D;
import org.concord.graph.ui.SingleAxisGrid;


/**
 * PfDataGraphView
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public class DataGraphStateManager
	implements GraphableListListener
{
	OTDataGraph pfObject;
	OTDataAxis xAxis;
	OTDataAxis yAxis;
	OTObjectList pfGraphables;
	DataGraph dataGraph;
	
	public DataGraphStateManager(OTDataGraph pfDataGraph, DataGraph dataGraph)
	{
		this.pfObject = pfDataGraph;
		this.dataGraph = dataGraph;
	}

	public static void setupAxisLabel(SingleAxisGrid sAxis, OTDataAxis axis)
	{
		String axisLabel = "";
		
		if(axis.getLabel() != null) {
			axisLabel += axis.getLabel();
		}
	
		if(axis.getUnits() != null) {
			axisLabel += "(" + axis.getUnits() + ")";
		}
		
		if(axisLabel.length() > 0) {
			sAxis.setAxisLabel(axisLabel);
		}		
	}
	
	public void initialize()
	{
		initialize(true);
	}
	
	/* (non-Javadoc)
	 * @see org.concord.portfolio.views.PortfolioView#getComponent(boolean)
	 */
	public void initialize(boolean showToolbar)
	{
		OTObjectList xAxisList = pfObject.getXDataAxis();
		OTObjectList yAxisList = pfObject.getYDataAxis();

		xAxis = (OTDataAxis)xAxisList.get(0);
		yAxis = (OTDataAxis)yAxisList.get(0);
		
		pfGraphables = pfObject.getGraphables();

		// OTObjectList dataProducers = pfObject.getDataProducers();				
		DataFlowControlToolBar toolBar = null;
		
		dataGraph.setLimitsAxisWorld(xAxis.getMin(), xAxis.getMax(),
				yAxis.getMin(), yAxis.getMax());

		GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
		if(gwToolbar != null) {
			gwToolbar.setVisible(showToolbar);
		}
		
		Grid2D grid = dataGraph.getGrid();

		SingleAxisGrid sXAxis = grid.getXGrid();
		setupAxisLabel(sXAxis, xAxis);
		
		SingleAxisGrid sYAxis = grid.getYGrid();
		setupAxisLabel(sYAxis, yAxis);
		

		// for each list item get the data producer object
		// add it to the data graph
		for(int i=0; i<pfGraphables.size(); i++) {
			OTDataGraphable otGraphable = (OTDataGraphable)pfGraphables.get(i);
			Object dataProducer = otGraphable.getDataProducer();
			OTDataStore dataStore = (OTDataStore)otGraphable.getDataStore();
			
			// dProducer.getDataDescription().setDt(0.1f);
			DataGraphable realGraphable = null;
			if(dataProducer instanceof DataProducerProxy) {
				dataProducer = ((DataProducerProxy)dataProducer).getDataProducer();
			}
			
			if(dataProducer instanceof DataProducer) {
				if(otGraphable.getControllable()) {
					System.err.println("Can't control a graphable with a data producer");
				}
				
				realGraphable = dataGraph.createDataGraphable((DataProducer)dataProducer);
				if(toolBar == null) {					
					toolBar = new DataFlowControlToolBar(dataGraph);
					dataGraph.add(toolBar, BorderLayout.SOUTH);
				}
				toolBar.addDataFlowObject((DataProducer)dataProducer);
			} else {
				if(otGraphable.getControllable()) {
					realGraphable = new ControllableDataGraphable();
					realGraphable.setDataStore(dataStore, 
							otGraphable.getXColumn(), 
							otGraphable.getYColumn());
				} else {
					realGraphable = dataGraph.createDataGraphable(dataStore, 
							otGraphable.getXColumn(), 
							otGraphable.getYColumn());
				}
			}
			// realGraphable.addGraphableListener(this);
			realGraphable.setColor(new Color(otGraphable.getColor()));
			realGraphable.setConnectPoints(otGraphable.getConnectPoints());
			realGraphable.setShowCrossPoint(otGraphable.getDrawMarks());
			dataGraph.addDataGraphable(realGraphable);
		}
		
		GraphableList graphableList = dataGraph.getObjList();
		graphableList.addGraphableListListener(this);				
	}

	
	/**
	 * This only works for graphables that came from a loaded
	 * pfgraphables.  It doesn't yet handel cases where new
	 * graphables are created by some external thing
	 *
	 */
	public void updateState()
	{
		Grid2D grid = dataGraph.getGrid();

		xAxis.setMin((float)dataGraph.getMinXAxisWorld());
		xAxis.setMax((float)dataGraph.getMaxXAxisWorld());
		yAxis.setMin((float)dataGraph.getMinYAxisWorld());
		yAxis.setMax((float)dataGraph.getMaxYAxisWorld());

		SingleAxisGrid sXAxis = grid.getXGrid();
		if(sXAxis.getAxisLabel() != null){
			xAxis.setLabel(sXAxis.getAxisLabel());
		}
		
		SingleAxisGrid sYAxis = grid.getYGrid();
		if(sYAxis.getAxisLabel() != null){
			yAxis.setLabel(sYAxis.getAxisLabel());
		}
		
		// TODO change the name of this method so it is more descriptive
		GraphableList graphableList = dataGraph.getObjList();
		
		// for each list item get the data producer object
		// add it to the data graph
		for(int i=0; i<graphableList.size(); i++) {
			DataGraphable dGraphable = (DataGraphable)graphableList.get(i);
			
			// This assumes you have not added a graphable to the graph
			OTDataGraphable pfGraphable = (OTDataGraphable)pfGraphables.get(i);
			
			DataStore dStore = dGraphable.getDataStore();
			DataProducer dProducer = dGraphable.getDataProducer();
			
			if(dProducer != null) {
				// We can't store the data values yet because we don't have a way
				// to populate the graph when it is data producer
			} else {
				// FIXME we should probably update the 
				// the data description of the data store 
				// somewhere around here				
			}
			
			Color c = dGraphable.getColor();
			pfGraphable.setColor(c.getRGB());
			pfGraphable.setConnectPoints(dGraphable.isConnectPoints());
			pfGraphable.setDrawMarks(dGraphable.isShowCrossPoint());
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableAdded(java.util.EventObject)
	 */
	public void listGraphableAdded(EventObject e)
	{
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableChanged(java.util.EventObject)
	 */
	public void listGraphableChanged(EventObject e)
	{
		updateState();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableRemoved(java.util.EventObject)
	 */
	public void listGraphableRemoved(EventObject e)
	{
	}
}
