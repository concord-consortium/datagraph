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
 * $Revision: 1.19 $
 * $Date: 2007-09-25 12:47:18 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.data.StartableMultiplexer;
import org.concord.data.state.OTDataStore;
import org.concord.data.ui.StartableToolBar;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.DataGraphToolbar;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreCollection;
import org.concord.framework.data.stream.DataStoreImporter;
import org.concord.framework.data.stream.WritableArrayDataStore;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;
import org.concord.framework.otrunk.view.OTJComponentViewContext;
import org.concord.framework.otrunk.view.OTJComponentViewContextAware;
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
public class OTMultiDataGraphView extends AbstractOTJComponentView
    implements DataStoreCollection, OTJComponentViewContextAware
{
    OTMultiDataGraph multiDataGraph;
    OTControllerService controllerService;
    
    ArrayList<DataGraphManager> graphManagers = new ArrayList<DataGraphManager>();
	private OTJComponentViewContext jCompViewContext;
    
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#getComponent(boolean)
     */
    public JComponent getComponent(OTObject otObject)
    {
        multiDataGraph = (OTMultiDataGraph)otObject;
    	controllerService = createControllerService(otObject);
        
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
		StartableMultiplexer multiplexer = new StartableMultiplexer();
		
        JPanel westPanel = new JPanel();
        // This is weird but we'll try it
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));        
        mainPanel.add(westPanel, BorderLayout.WEST);
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        OTObjectList plugins = multiDataGraph.getPluginViews();
        ArrayList<JComponent> pluginComponents = new ArrayList<JComponent>();
        for(int i=0; i<plugins.size(); i++) {
            OTPluginView pluginView = (OTPluginView)plugins.get(i);            
            OTObject pluginControl = pluginView.getControl();
            JComponent pluginComponent = 
            	getChildComponent(pluginControl, null);

            // change the alignment of the component so 
            // when it is put in the box it can fill the whole width
            // The default alignment of a button for example is 0
            // which tries to make the buttons left edge align with the 
            // center of other components.
            if(pluginView.getLocation() == null ||
            	pluginView.getLocation().equals("west")) {
            	pluginComponent.setAlignmentX(0.5f);
            	westPanel.add(pluginComponent);
            } else if(pluginView.getLocation().equals("south")){
            	// pluginComponent.setAlignmentX(0.5f);
            	southPanel.add(pluginComponent);           	
            }
            
            pluginComponents.add(pluginComponent);
        }
        
		OTObjectList graphs = multiDataGraph.getGraphs();
		for(int i=0; i<graphs.size(); i++) {
		   OTDataCollector graph = (OTDataCollector)graphs.get(i);
		   DataCollectorView view = new DataCollectorView(graph);
		   view.setOTJComponentViewContext(jCompViewContext);
		   view.setViewContext(viewContext);
		   DataGraph dataGraph = view.getDataGraph(false, false);
		   dataGraph.setToolBar(gwToolBar, false);
	
           graphManagers.add(view.getDataGraphManager());
           
		   DataProducer sourceDataProducer = view.getSourceDataProducer();
		   if(sourceDataProducer != null) {
               // only add the tool bar if the data collectors have a
               // source object.  Otherwise they don't actually 
               // collect data.
		       needFlowToolbar = true;
		       multiplexer.addStartable(view.getDataGraphManager().getStartable());
		   }
		   
		   graphsPanel.add(dataGraph);
		   for (JComponent pluginComponent : pluginComponents) {
               if(pluginComponent instanceof DataGraphViewPlugin) {
                   ((DataGraphViewPlugin)pluginComponent).addDataCollectorView(view);
               }			
		   }
		}

		for (JComponent pluginComponent : pluginComponents) {
            if(pluginComponent instanceof DataGraphViewPlugin) {                
                ((DataGraphViewPlugin)pluginComponent).initialize();
            }
            if(pluginComponent instanceof DataStoreImporter) {
                ((DataStoreImporter)pluginComponent).setDataStoreCollection(this);
            }
        }

        // Need to adjust the preferred size of the graphPanel
        // we want to keep the view from scrolling because it
        // is too large.   This needs to be higher than 
        // a single graph bacause it might be mutliple graph
        // These numbers are just made up at this point
        // we'll probably have to adjust them.
        graphsPanel.setPreferredSize(
        		new Dimension(400,graphManagers.size() * 200));
		mainPanel.add(graphsPanel, BorderLayout.CENTER);

		if(needFlowToolbar) {
			StartableToolBar toolBar = new StartableToolBar();
			toolBar.setStartable(multiplexer);
			
			southPanel.add(toolBar);
		}
		
        // TODO Auto-generated method stub
        return mainPanel;
    }

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
     */
    public void viewClosed()
    {
        for(int i=0; i<graphManagers.size(); i++) {
            DataGraphManager graphManager =
                (DataGraphManager)graphManagers.get(i);
            graphManager.viewClosed();
        }
        controllerService.dispose();       
    }


    public void addDataStore(String name, DataStore dStore)
    {
        // This will be called by the LoggerImporter
        // to add a new data store to this graph, 
        // The data store should match the configuration
        // of this graph.
        
        // This method should clone or copy the graphables
        // in its graphs, perhaps assign them new collors
        // and set the source to be this added DataStore.
        
        // However one problem is that the data store needs
        // to be OTDataStore or it won't be saved correctly
        // Perhaps the best thing to do is pass in a data
        // producer, which this method can use to create
        // its own data store.
        
        // TODO it would be better if this class created the 
    	// checked colortreemodel to hand off to its plugins
    	// or views.  However the tree model doesn't have events
    	// yet, so it possible yet to abstract the events that
    	// cause the tree model to update. 
        Color newColor = null;
        if(graphManagers.size() > 0){
            DataGraphManager graphManager =
                (DataGraphManager)graphManagers.get(0);
            newColor = graphManager.getNewColor();
        }
        
        OTDataStore otDataStore = (OTDataStore) controllerService.getOTObject(dStore);

        for(int i=0; i<graphManagers.size(); i++) {
            DataGraphManager graphManager =
                (DataGraphManager)graphManagers.get(i);
            
            
            DataGraphable graphable = 
                (DataGraphable)graphManager.addItem(null, name, newColor);

            // We have a datastore that was created by our controllerService 
            // from an OTDataStore.
            // We need a datastore that is created by the controllerService of the target DataGraphManager
            // otherwise the views and controlers in that DataGraphManager will not beable to find the 
            // OTObject for the datastore.  
            // It is pretty annoying that we have to do this but there is no other way right now.            
            OTControllerService gmControllerService = graphManager.getControllerService();
            DataStore gmDataStore = (DataStore) gmControllerService.getRealObject(otDataStore);
            
            graphable.setDataStore(gmDataStore);
        }        
    }


    public WritableArrayDataStore createDataStore()
    {
        try {
            OTObjectService objService = multiDataGraph.getOTObjectService();
            OTDataStore otDataStore = (OTDataStore)objService.createObject(OTDataStore.class);
            
            WritableArrayDataStore waDataStore = 
            	(WritableArrayDataStore) controllerService.getRealObject(otDataStore);
            return waDataStore;
        } catch (Exception e) {
            // we can't handle this
            e.printStackTrace();
        }

        // TODO Auto-generated method stub
        return null;
    }

	public void setOTJComponentViewContext(OTJComponentViewContext viewContext) {
		this.jCompViewContext = viewContext;
		
	}
}
