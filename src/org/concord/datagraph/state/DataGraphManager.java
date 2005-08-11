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
 * Created on Apr 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.concord.data.ui.DataFlowControlAction;
import org.concord.data.ui.DataFlowControlButton;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.data.ui.DataStoreLabel;
import org.concord.data.ui.DataValueLabel;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.SingleDataAxisGrid;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTWrapper;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.event.GraphableListListener;
import org.concord.graph.examples.GraphWindowToolBar;
import org.concord.graph.ui.Grid2D;
import org.concord.graph.ui.SingleAxisGrid;
import org.concord.graph.util.control.DrawingAction;
import org.concord.swing.SelectableToggleButton;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataGraphManager
	implements GraphableListListener, OTChangeListener, ChangeListener
{
    OTDataCollector dataCollector;
    DataGraph dataGraph;
    
	DataGraphable sourceGraphable;
	DataProducer sourceDataProducer;
	DataStoreLabel valueLabel;
	
	OTDataAxis xOTAxis;
	OTDataAxis yOTAxis;
	
	JPanel bottomPanel;

	boolean isCausingOTChange = false;
	
    /**
     * 
     */
    public DataGraphManager(OTDataCollector collector, boolean showDataControls)
    {
        dataCollector = collector;

        dataGraph = new DataGraph();
		dataGraph.changeToDataGraphToolbar();
		dataGraph.setAutoFitMode(DataGraph.AUTO_SCROLL_RUNNING_MODE);
			
		xOTAxis = dataCollector.getXDataAxis();
		xOTAxis.addOTChangeListener(this);
		yOTAxis = dataCollector.getYDataAxis();
		yOTAxis.addOTChangeListener(this);

		OTObjectList pfGraphables = dataCollector.getGraphables();

		DataFlowControlToolBar toolBar = null;

		dataGraph.setLimitsAxisWorld(xOTAxis.getMin(), xOTAxis.getMax(),
				yOTAxis.getMin(), yOTAxis.getMax());

		Grid2D grid = dataGraph.getGrid();

		SingleDataAxisGrid sXAxis = (SingleDataAxisGrid)grid.getXGrid();

		DataGraphStateManager.setupAxisLabel(sXAxis, xOTAxis);
		
		SingleDataAxisGrid sYAxis = (SingleDataAxisGrid)grid.getYGrid();
		DataGraphStateManager.setupAxisLabel(sYAxis, yOTAxis);

		Vector realGraphables = new Vector();
		
		// for each list item get the data producer object
		// add it to the data graph
		for(int i=0; i<pfGraphables.size(); i++) {
			OTDataGraphable otGraphable = (OTDataGraphable)pfGraphables.get(i);
			
			// dProducer.getDataDescription().setDt(0.1f);
			DataGraphable realGraphable = (DataGraphable)otGraphable.createWrappedObject();
			
			if (realGraphable.getDataProducer() != null){
			    System.err.println("Trying to display a background graphable with a data producer");
			}
			
			realGraphables.add(realGraphable);
			dataGraph.addBackgroundDataGraphable(realGraphable);
		}

		OTDataGraphable source = dataCollector.getSource();
		if(source != null) {
			String title = dataCollector.getTitle(); 
			if(title == null) {
			    title = source.getName();			    
			}
			
			if(title != null) {
			    dataGraph.setTitle(title);
			}

			sourceGraphable = (DataGraphable)source.createWrappedObject();
			sourceDataProducer = sourceGraphable.findDataProducer();
			
			// dProducer.getDataDescription().setDt(0.1f);
			if(sourceGraphable instanceof ControllableDataGraphable) {

			    bottomPanel = new JPanel(new FlowLayout());
			    JButton clearButton = new JButton("Clear");
			    clearButton.addActionListener(new ActionListener(){
			       public void actionPerformed(ActionEvent e){
			           dataGraph.reset();			           
			       }
			    });
			    
				DrawingAction a = new DrawingAction();
				a.setDrawingObject((ControllableDataGraphable)sourceGraphable);
				GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
				gwToolbar.addButton(new SelectableToggleButton(a), "Draw a function");
				
			    bottomPanel.add(clearButton);

			    dataGraph.add(bottomPanel, BorderLayout.SOUTH);  			    			    
			} 
			else if(showDataControls){
			    bottomPanel = new JPanel(new FlowLayout());
			    valueLabel = new DataStoreLabel(sourceGraphable, 1);
			    valueLabel.setColumns(4);
			    bottomPanel.add(valueLabel);

			    toolBar = createFlowToolBar();
			    bottomPanel.add(toolBar);
			    toolBar.addDataFlowObject(sourceDataProducer);

			    if(dataCollector.getShowTare()){
			        // need to add a button that runs the sourceGraphable
			        // for some amount of time and then offsets the input
			        // values after that.
			        // The code for the Tare can be put into a dataproducer
			        // that wraps the input dataproducer
			        
			    }
			    
			    dataGraph.add(bottomPanel, BorderLayout.SOUTH);  			    
			}

			if(sourceGraphable != null) {
			    realGraphables.insertElementAt(sourceGraphable, 0);
			    dataGraph.addDataGraphable(sourceGraphable);
			}
		}

		if(realGraphables.size() > 1) {
		    
		    DataGraphableTree dTree = new DataGraphableTree();
		    // add legend to the left
		    for(int i=0; i<realGraphables.size(); i++){
		        dTree.addGraphable((DataGraphable)realGraphables.get(i));
		    }
		    
		    dataGraph.add(dTree, BorderLayout.WEST);
		}
		
		// FIXME This should be changed.  The graphables should
		// listen to themselves, and this code then can just listen
		// to the graph area for graph area changes.
		// right now both graph area and source graphable changes
		// are listened too
		GraphableList graphableList = dataGraph.getObjList();
		// graphableList.addGraphableListListener(this);
		
		dataGraph.setPreferredSize(new Dimension(400,320));
		
		dataGraph.getGraphArea().addChangeListener(this);
    }

    public DataProducer getSourceDataProducer()
    {
        return sourceDataProducer;
    }

    public float getLastValue()
    {
        if(valueLabel == null) {
            return Float.NaN;
        }
        return valueLabel.getValue();
    }
    
    /**
     * @return
     */
    public JPanel getBottomPanel()
    {
        return bottomPanel;
    }

    public DataGraph getDataGraph()
    {
        return dataGraph;
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

	public void setToolbarVisible(boolean visible)
	{
		GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
		if(gwToolbar != null) {
		    // FIXME
			gwToolbar.setVisible(visible);
		}
	}		
	
	/**
	 * This only works for graphables that came from a loaded
	 * pfgraphables.  It doesn't yet handel cases where new
	 * graphables are created by some external thing
	 *
	 */
	public void updateState(Object obj)
	{
		Grid2D grid = dataGraph.getGrid();

		xOTAxis.setDoNotifyChangeListeners(false);
		yOTAxis.setDoNotifyChangeListeners(false);
		
		xOTAxis.setMin((float)dataGraph.getMinXAxisWorld());
		xOTAxis.setMax((float)dataGraph.getMaxXAxisWorld());
		yOTAxis.setMin((float)dataGraph.getMinYAxisWorld());
		yOTAxis.setMax((float)dataGraph.getMaxYAxisWorld());

		SingleAxisGrid sXAxis = grid.getXGrid();
		if(sXAxis.getAxisLabel() != null){
			xOTAxis.setLabel(sXAxis.getAxisLabel());
		}
		
		SingleAxisGrid sYAxis = grid.getYGrid();
		if(sYAxis.getAxisLabel() != null){
			yOTAxis.setLabel(sYAxis.getAxisLabel());
		}

		isCausingOTChange = true;
		xOTAxis.notifyOTChange();
		yOTAxis.notifyOTChange();
		isCausingOTChange = false;

		/*
		if(obj instanceof DataGraphable) {
		    OTDataGraphable source = dataCollector.getSource();
		    
		    source.saveObject(obj);		
		}
		*/
	}
	
	/**
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
		updateState(e.getSource());
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableRemoved(java.util.EventObject)
	 */
	public void listGraphableRemoved(EventObject e)
	{
		Object obj = e.getSource();
		
		OTWrapper otWrapper = dataCollector.getOTDatabase().getWrapper(obj);
		
		if (otWrapper != null){
			if(otWrapper instanceof OTDataPointLabel)
				dataCollector.getLabels().remove(otWrapper);
		}
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTChangeListener#stateChanged(org.concord.framework.otrunk.OTChangeEvent)
	 */
	public void stateChanged(OTChangeEvent e)
	{
	    if(isCausingOTChange) {
	        // we are the cause of this change
	        return;
	    }

	    if(e.getSource() == xOTAxis || e.getSource() == yOTAxis) {
	        dataGraph.setLimitsAxisWorld(xOTAxis.getMin(), xOTAxis.getMax(),
	                yOTAxis.getMin(), yOTAxis.getMax());
	    }	    	    
	}
	
	/* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e)
    {
        Object source = e.getSource();
        updateState(source);
    }
	
}
