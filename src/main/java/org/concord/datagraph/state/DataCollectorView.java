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

import javax.swing.JComponent;

import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.DataGraph.AspectDimension;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;
import org.concord.framework.otrunk.view.OTJComponentViewContext;
import org.concord.framework.otrunk.view.OTJComponentViewContextAware;
import org.concord.graph.engine.SelectableList;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataCollectorView extends AbstractOTJComponentView implements OTJComponentViewContextAware
{
    OTDataCollector dataCollector;
	SelectableList notesLayer;
	WritableDataStore dataStore;	
	DataGraphManager dataGraphManager;
	boolean controllable;
	private OTJComponentViewContext jComponentViewContext;
	private boolean showToolBar;
	
    public DataCollectorView(OTDataCollector collector)
    {
        dataCollector = collector;
        controllable = true;
        showToolBar = true;
    }
    
    public DataCollectorView(OTDataCollector collector, boolean controllable)
    {
    	// Keeping this backwards compatible, the original code had both controllable
    	// and showToolBar linked
        this(collector, controllable, controllable);
    }
    
    public DataCollectorView(OTDataCollector collector, boolean controllable, boolean showToolBar)
    {
        dataCollector = collector;
        this.controllable = controllable;
        this.showToolBar = showToolBar;
    }

    public JComponent getComponent(OTObject otObject)
    {
    	// For safety verify that the otObject is the same
    	// as the one used in the constructor
    	if(!otObject.equals(dataCollector)){
    		throw new RuntimeException("otObject != dataCollector");
    	}    	
    	
    	DataGraph graph = getDataGraph(showToolBar, controllable);
    	
        if (dataCollector.getUseAspectRatio()) {
            float ratio = dataCollector.getAspectRatio();
            AspectDimension dim = dataCollector.getAspectDimension();
            graph.setAspectDimension(dim);
            graph.setAspectRatio(ratio);
            graph.setUseAspectRatio(true);
         }
    	       
    	return graph;
    }

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
     */
    @Override
    public void viewClosed()
    {
    	dataGraphManager.viewClosed();
    	dataGraphManager= null;
    }
    
    public DataGraph getDataGraph(boolean showToolbar, boolean showDataControls)
    {
	    dataGraphManager = 
	    	new DataGraphManager(dataCollector, viewContext, showDataControls, jComponentViewContext);

	    dataGraphManager.setToolbarVisible(showToolbar);
	    
	   // dataGraphManager.setOTJComponentViewContext(jComponentViewContext);
	    
	    return dataGraphManager.getDataGraph();
    }
    
    public DataGraph getDataGraph(){
    	return dataGraphManager.getDataGraph();
    }
    
    public DataProducer getSourceDataProducer()
    {
    	return dataGraphManager.getSourceDataProducer();
    }
    
    public DataGraphManager getDataGraphManager()
    {
        return dataGraphManager;
    }

	public void setOTJComponentViewContext(OTJComponentViewContext viewContext)
    {
	    jComponentViewContext = viewContext;
    }    
	
	public void setInstantRestart(boolean instantRestart){
		dataGraphManager.setInstantRestart(instantRestart);
	}
}