

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
 * $Revision: 1.2 $
 * $Date: 2004-12-12 04:04:57 $
 * $Author: maven $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.EventObject;

import org.concord.data.stream.DataStoreUtil;
import org.concord.data.stream.PointsDataStore;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DefaultDataStore;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.event.GraphableListListener;
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

	/* (non-Javadoc)
	 * @see org.concord.portfolio.views.PortfolioView#getComponent(boolean)
	 */
	public void initialize()
	{
		xAxis = pfObject.getXDataAxis();
		yAxis = pfObject.getYDataAxis();
		
		pfGraphables = pfObject.getDataGraphables();

		// OTObjectList dataProducers = pfObject.getDataProducers();				
		DataFlowControlToolBar toolBar = null;
		
		dataGraph.setLimitsAxisWorld(xAxis.getMin(), xAxis.getMax(),
				yAxis.getMin(), yAxis.getMax());
		
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
				String values = dataStore.getValues();
				WritableDataStore dStore = new DefaultDataStore();
				try {
					DataStoreUtil.loadData(new StringReader(values), dStore, false);
				} catch(IOException e) {
					e.printStackTrace();
				}
				realGraphable = dataGraph.createDataGraphable(dStore);
			}
			// realGraphable.addGraphableListener(this);
			realGraphable.setColor(new Color(dGraphable.getColor()));
			realGraphable.setConnectPoints(dGraphable.getConnectPoints());
			realGraphable.setShowCrossPoint(dGraphable.getDrawMarks());
			dataGraph.addDataGraphable(realGraphable);
		}
		
		GraphableList graphableList = dataGraph.getObjList();
		graphableList.addGraphableListListener(this);				
	}

	
	ControllableDataGraphable lastGraphable;
	
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
				String values = dataStore.getValues();
								
				WritableDataStore dStore = new PointsDataStore();
				try {
					DataStoreUtil.loadData(new StringReader(values), dStore, false);
				} catch(IOException e) {
					e.printStackTrace();
				}
				
				realGraphable = new ControllableDataGraphable();
				realGraphable.setDataStore((PointsDataStore)dStore, 0, 1);
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
				ByteArrayOutputStream outS = new ByteArrayOutputStream();
				PrintStream printStream = new PrintStream(outS);
				DataStoreUtil.printData(printStream, dStore, null, false);
			
				String values = outS.toString();

				OTDataStore pfDataStore = pfGraphable.getDataStore();
				pfDataStore.setValues(values);
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
