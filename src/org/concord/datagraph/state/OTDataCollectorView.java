/*
 * Created on Mar 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.data.state.OTDataStore;
import org.concord.data.ui.DataFlowControlAction;
import org.concord.data.ui.DataFlowControlButton;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.SingleDataAxisGrid;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.engine.SelectableList;
import org.concord.graph.event.GraphableListListener;
import org.concord.graph.examples.GraphWindowToolBar;
import org.concord.graph.ui.Grid2D;
import org.concord.graph.ui.SingleAxisGrid;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataCollectorView
    implements OTObjectView, GraphableListListener
{
    OTDataCollector dataCollector;
	protected OTViewContainer viewContainer;
	DataGraph dataGraph;
	SelectableList notesLayer;
	DataGraphable sourceGraphable;
	
	OTDataAxis xOTAxis;
	OTDataAxis yOTAxis;
	
	public OTDataCollectorView(OTDataCollector collector, OTViewContainer container)
	{
	    dataCollector = collector;
	    viewContainer = container; 
	}
    
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTObjectView#getComponent(boolean)
     */
    public JComponent getComponent(boolean editable)
    {
        if(dataCollector.getSingleValue()){
            System.err.println("single value is not supported");
            return null;
        }
        
		dataGraph = new DataGraph();
		dataGraph.changeToDataGraphToolbar();
		dataGraph.setAutoFitMode(DataGraph.AUTO_SCROLL_RUNNING_MODE);
		
		xOTAxis = dataCollector.getXDataAxis();
		yOTAxis = dataCollector.getYDataAxis();

		OTObjectList pfGraphables = dataCollector.getGraphables();

		DataFlowControlToolBar toolBar = null;

		dataGraph.setLimitsAxisWorld(xOTAxis.getMin(), xOTAxis.getMax(),
				yOTAxis.getMin(), yOTAxis.getMax());

		GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
		if(gwToolbar != null) {
		    // FIXME
			gwToolbar.setVisible(editable);
		}
		
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
			OTDataStore dataStore = (OTDataStore)otGraphable.getDataStore();
			
			// dProducer.getDataDescription().setDt(0.1f);
			DataGraphable realGraphable = null;

			if(dataStore == null) {
			    System.err.println("Trying to display graphable with out a data store");
			    continue;
			}
			
			realGraphable = dataGraph.createDataGraphable(dataStore, 
			        otGraphable.getXColumn(), 
			        otGraphable.getYColumn());

			// realGraphable.addGraphableListener(this);
			realGraphable.setColor(new Color(otGraphable.getColor()));
			realGraphable.setConnectPoints(otGraphable.getConnectPoints());
			realGraphable.setShowCrossPoint(otGraphable.getDrawMarks());
			realGraphable.setLabel(otGraphable.getName());
			realGraphables.add(realGraphable);
			dataGraph.addDataGraphable(realGraphable);
		}

		OTDataGraphable source = dataCollector.getSource();
		if(source != null) {
			DataProducer dataProducer = (DataProducer)source.getDataProducer();
			OTDataStore dataStore = (OTDataStore)source.getDataStore();

			String title = dataCollector.getTitle(); 
			if(title == null) {
			    title = source.getName();			    
			}
			
			if(title != null) {
			    dataGraph.setTitle(title);
			}
			
			// dProducer.getDataDescription().setDt(0.1f);
			if(source.getControllable()) {
				sourceGraphable = new ControllableDataGraphable();
				sourceGraphable.setDataStore(dataStore, 
						source.getXColumn(), 
						source.getYColumn());
				// TODO need to add the sketch components here
			} else if(dataProducer != null) {
			    // need to set the data store to be the data store for this
			    // graphable
			    if(dataStore != null){
			        dataStore.setDataProducer(dataProducer);
					sourceGraphable = dataGraph.createDataGraphable(dataStore);
			    } else {
			        sourceGraphable = dataGraph.createDataGraphable(dataProducer);
			    }
				toolBar = createFlowToolBar();
				dataGraph.add(toolBar, BorderLayout.SOUTH);
				toolBar.addDataFlowObject((DataProducer)dataProducer);
			}

			if(sourceGraphable != null) {
			    sourceGraphable.setColor(new Color(source.getColor()));
			    sourceGraphable.setConnectPoints(source.getConnectPoints());
			    sourceGraphable.setShowCrossPoint(source.getDrawMarks());
			    sourceGraphable.setLabel(source.getName());
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
		
		GraphableList graphableList = dataGraph.getObjList();
		graphableList.addGraphableListListener(this);				
		
		JPanel graphWrapper = new JPanel(){
		  public void removeNotify()
		  {
		      System.err.println("got remove notify");
		      
		      // FIXME need to only reset the sourceGraphable
		      // dataGraph.reset();
		  }
		};
		
		graphWrapper.setLayout(new BorderLayout());
		graphWrapper.add(dataGraph, BorderLayout.CENTER);
		return graphWrapper;
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

		OTDataGraphable source = dataCollector.getSource();

		Color c = sourceGraphable.getColor();
		source.setColor(c.getRGB());
		source.setConnectPoints(sourceGraphable.isConnectPoints());
		source.setDrawMarks(sourceGraphable.isShowCrossPoint());
		
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
