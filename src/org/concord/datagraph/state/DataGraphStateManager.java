

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
 * $Revision: 1.9 $
 * $Date: 2005-04-01 06:21:31 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.EventObject;
import java.util.Vector;

import org.concord.data.Unit;
import org.concord.data.state.OTDataStore;
import org.concord.data.ui.DataFlowControlAction;
import org.concord.data.ui.DataFlowControlButton;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.SingleDataAxisGrid;
import org.concord.framework.data.DataDimension;
import org.concord.framework.data.stream.DataProducer;
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

	public static void setupAxisLabel(SingleDataAxisGrid sAxis, OTDataAxis axis)
	{
		if(axis.getLabel() != null) {
			sAxis.setAxisLabel(axis.getLabel());
		}
	
		if(axis.getUnits() != null) {
			String unitStr = axis.getUnits();
			Unit unit = Unit.findUnit(unitStr);
			if(unit == null) {
				System.err.println("Can't find unit: " + unitStr);
				sAxis.setUnit(new UnknownUnit(unitStr)); 
			} 
			else {
				sAxis.setUnit(unit);
			}
		}		
	}
		
	public static class UnknownUnit implements DataDimension
	{
		String unit;
		
		public UnknownUnit(String unit)
		{
			this.unit = unit;
		}
		
		/* (non-Javadoc)
		 * @see org.concord.framework.data.DataDimension#getDimension()
		 */
		public String getDimension() 
		{
			return unit;
		}
		
		/* (non-Javadoc)
		 * @see org.concord.framework.data.DataDimension#setDimension(java.lang.String)
		 */
		public void setDimension(String dimension) 
		{
			unit = dimension;
		}
	}
	
	public void initialize()
	{
		initialize(true);
	}

	public class GraphAreaMap
	{
		OTDataAxis xAxis = null;
		OTDataAxis yAxis = null;
		Vector graphables = new Vector();
		boolean duplicate = false;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.portfolio.views.PortfolioView#getComponent(boolean)
	 */
	public void initialize(boolean showToolbar)
	{
		Vector areaMap = new Vector();
		
		OTObjectList xAxisList = pfObject.getXDataAxis();
		OTObjectList yAxisList = pfObject.getYDataAxis();

		pfGraphables = pfObject.getGraphables();
/*
		
		for (int i=0; i<xAxisList.size(); i++){
			OTDataAxis currAxis = (OTDataAxis)xAxisList.get(i);
			OTObjectList axisGraphables = currAxis.getGraphables();
			for (int j=0; j<axisGraphables.size(); j++){
				GraphAreaMap gMap = new GraphAreaMap();
				gMap.xAxis = (OTDataAxis)xAxisList.get(i);
				gMap.graphables.add(axisGraphables.get(j));
				areaMap.add(gMap);
			}
		}

		for (int i=0; i<yAxisList.size(); i++){
			OTDataAxis currAxis = (OTDataAxis)yAxisList.get(i);
			OTObjectList axisGraphables = currAxis.getGraphables();
			for (int j=0; j<axisGraphables.size(); j++){
				OTDataGraphable currGraphable = 
					(OTDataGraphable)axisGraphables.get(j);
				for (int k=0; k<areaMap.size(); k++){
					GraphAreaMap gMap = (GraphAreaMap)areaMap.get(k);
					OTDataGraphable mapGraphable = (OTDataGraphable)(gMap.graphables.get(0));
					if (mapGraphable.equals(currGraphable)){
						gMap.yAxis = currAxis;
					}
				}
			}
		}

		for (int i=0; i<areaMap.size(); i++){
			GraphAreaMap gMap = (GraphAreaMap)areaMap.get(i);
			if(gMap.duplicate) continue;
			for (int j=i; j<areaMap.size(); j++){
				GraphAreaMap gMap2 = (GraphAreaMap)areaMap.get(j);
				if(gMap.xAxis == gMap2.xAxis  &&
						gMap.yAxis == gMap2.yAxis){
					gMap2.duplicate = true;
					gMap.graphables.add(gMap2.graphables.get(0));
				}
			}
		}
		
		for (int i=0; i<areaMap.size(); i++){
		}

		GraphAreaMap gMap = (GraphAreaMap)areaMap.get(0);
		xAxis = gMap.xAxis;
		yAxis = gMap.yAxis;
		
	*/
		
		// we are ignoring the complicated code above for now
		xAxis = (OTDataAxis)xAxisList.get(0);
		yAxis = (OTDataAxis)yAxisList.get(0);
		
		// OTObjectList dataProducers = pfObject.getDataProducers();				
		DataFlowControlToolBar toolBar = null;
		
		dataGraph.setLimitsAxisWorld(xAxis.getMin(), xAxis.getMax(),
				yAxis.getMin(), yAxis.getMax());

		GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
		if(gwToolbar != null) {
			gwToolbar.setVisible(showToolbar);
		}
		
		Grid2D grid = dataGraph.getGrid();

		SingleDataAxisGrid sXAxis = (SingleDataAxisGrid)grid.getXGrid();
		//sXAxis.setMajorInterval(5);
		//sXAxis.setShowLabelsOnMajorOnly(true);
		//sXAxis.setBestPercentageInterval(0.02);
		//sXAxis.setIntervalFixedPercentage(0.02);
		//sXAxis.setIntervalFixedWorld(0.2);
		setupAxisLabel(sXAxis, xAxis);
		
		SingleDataAxisGrid sYAxis = (SingleDataAxisGrid)grid.getYGrid();
		setupAxisLabel(sYAxis, yAxis);
		//sYAxis.setMajorInterval(5);
		//sYAxis.setShowLabelsOnMajorOnly(true);
		
		
		// for each list item get the data producer object
		// add it to the data graph
		for(int i=0; i<pfGraphables.size(); i++) {
			OTDataGraphable otGraphable = (OTDataGraphable)pfGraphables.get(i);
			DataProducer dataProducer = (DataProducer)otGraphable.getDataProducer();
			OTDataStore dataStore = (OTDataStore)otGraphable.getDataStore();
			
			// dProducer.getDataDescription().setDt(0.1f);
			DataGraphable realGraphable = otGraphable.getDataGraphable();
			
			if(dataProducer != null) {
				if(realGraphable instanceof ControllableDataGraphable) {
					System.err.println("Can't control a graphable with a data producer");
				}
				
				dataProducer.reset();
				realGraphable.setDataProducer(dataProducer);				
				if(toolBar == null) {					
					toolBar = createFlowToolBar();
					dataGraph.add(toolBar, BorderLayout.SOUTH);
				}
				toolBar.addDataFlowObject((DataProducer)dataProducer);
			} else {
			    realGraphable.setDataStore(dataStore, 
						otGraphable.getXColumn(), 
						otGraphable.getYColumn());
			}

			dataGraph.addDataGraphable(realGraphable);
		}
		
		GraphableList graphableList = dataGraph.getObjList();
		graphableList.addGraphableListListener(this);				
	}

	public DataFlowControlToolBar createFlowToolBar()
	{
	    DataFlowControlToolBar toolbar = 
	        new DataFlowControlToolBar(false);

		DataFlowControlButton b = null;
		
		b = new DataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_START);
		toolbar.add(b);

		b = new DataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_STOP);
		toolbar.add(b);
		
		b = new DataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_RESET);
		b.setText("Clear");
		toolbar.add(b);
	 
		toolbar.addDataFlowObject(dataGraph);
		
	    return toolbar;
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
			
			pfGraphable.saveObject();			
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
