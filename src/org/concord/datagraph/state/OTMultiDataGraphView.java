/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-04-11 23:21:25 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.DataGraphToolbar;
import org.concord.framework.data.DataFlow;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;
import org.concord.graph.examples.GraphWindowToolBar;

/**
 * OTMultiDataGraphView
 * Class name and description
 *
 * Date created: Apr 11, 2005
 *
 * @author scott<p>
 *
 */
public class OTMultiDataGraphView
    implements OTObjectView
{
    OTMultiDataGraph multiDataGraph;
    
    public OTMultiDataGraphView(OTMultiDataGraph object, OTViewContainer viewContainer)
    {
        multiDataGraph = object;        
    }
    
    
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTObjectView#getComponent(boolean)
     */
    public JComponent getComponent(boolean editable)
    {
        JPanel mainPanel = new JPanel(new BorderLayout());

		GraphWindowToolBar gwToolBar = new DataGraphToolbar(); //GraphWindowToolBar();	
		gwToolBar.setButtonsMargin(0);
		gwToolBar.setFloatable(false);
		gwToolBar.setGraphWindow(null);
		gwToolBar.setGrid(null);
		mainPanel.add(gwToolBar, BorderLayout.EAST);

        JPanel graphsPanel = new JPanel();
        GridLayout grid = new GridLayout(multiDataGraph.getRows(), multiDataGraph.getColumns());
		graphsPanel.setLayout(grid);

		boolean needFlowToolbar = false;
		Vector dataFlowObjects = new Vector();
		
		OTObjectList graphs = multiDataGraph.getGraphs();
		for(int i=0; i<graphs.size(); i++) {
		   OTDataCollector graph = (OTDataCollector)graphs.get(i);
		   DataCollectorView view = new DataCollectorView(graph);
		   DataGraph dataGraph = view.getDataGraph(false, false);
		   dataGraph.setToolBar(gwToolBar, false);
		   dataFlowObjects.add(dataGraph);
		   
		   DataProducer sourceDataProducer = view.getSourceDataProducer();
		   if(sourceDataProducer != null) {
		       needFlowToolbar = true;
		       dataFlowObjects.add(sourceDataProducer);
		   }
		   
		   graphsPanel.add(dataGraph);
		}
	
		mainPanel.add(graphsPanel, BorderLayout.CENTER);

		if(needFlowToolbar) {
			DataFlowControlToolBar toolBar = new DataFlowControlToolBar();
			
			for(int i=0; i<dataFlowObjects.size(); i++) {
			    toolBar.addDataFlowObject((DataFlow)dataFlowObjects.get(i));
			}
			
			mainPanel.add(toolBar, BorderLayout.SOUTH);		    
		}
		
        // TODO Auto-generated method stub
        return mainPanel;
    }

}
