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

import org.concord.data.ui.DataFlowControlAction;
import org.concord.data.ui.DataFlowControlButton;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.data.ui.DataValueLabel;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.SingleDataAxisGrid;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.OTObjectList;
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
	implements GraphableListListener
{
    OTDataCollector dataCollector;
    DataGraph dataGraph;
    
	DataGraphable sourceGraphable;
	DataProducer sourceDataProducer;
	DataValueLabel valueLabel;
	
	OTDataAxis xOTAxis;
	OTDataAxis yOTAxis;
	
	JPanel bottomPanel;
	
    /**
     * 
     */
    public DataGraphManager(OTDataCollector collector)
    {
        dataCollector = collector;

        dataGraph = new DataGraph();
		dataGraph.changeToDataGraphToolbar();
		dataGraph.setAutoFitMode(DataGraph.AUTO_SCROLL_RUNNING_MODE);
		
		xOTAxis = dataCollector.getXDataAxis();
		yOTAxis = dataCollector.getYDataAxis();

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
			DataGraphable realGraphable = otGraphable.getDataGraphable();
			
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

			sourceGraphable = source.getDataGraphable();
			sourceDataProducer = sourceGraphable.findDataProducer();
			
			// dProducer.getDataDescription().setDt(0.1f);
			if (sourceGraphable instanceof ControllableDataGraphable){

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
			else {
			    bottomPanel = new JPanel(new FlowLayout());
			    valueLabel = new DataValueLabel(sourceDataProducer);
			    valueLabel.setColumns(4);
			    bottomPanel.add(valueLabel);

			    toolBar = createFlowToolBar();
			    bottomPanel.add(toolBar);
			    toolBar.addDataFlowObject(sourceDataProducer);

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
				
		GraphableList graphableList = dataGraph.getObjList();
		graphableList.addGraphableListListener(this);
		
		dataGraph.setPreferredSize(new Dimension(400,320));		
    }

    public DataProducer getSourceDataProducer()
    {
        return sourceDataProducer;
    }

    public DataValueLabel getValueLabel()
    {
        return valueLabel;
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

		source.saveObject();		
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
		updateState();
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableRemoved(java.util.EventObject)
	 */
	public void listGraphableRemoved(EventObject e)
	{
	}


}
