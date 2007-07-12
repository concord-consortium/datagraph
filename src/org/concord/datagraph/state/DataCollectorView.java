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
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;
import org.concord.graph.engine.SelectableList;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataCollectorView extends AbstractOTJComponentView
{
    OTDataCollector dataCollector;
	SelectableList notesLayer;
	WritableDataStore dataStore;	
	DataGraphManager dataGraphManager;
	boolean controllable;
	
    public DataCollectorView(OTDataCollector collector)
    {
        dataCollector = collector;
        controllable = true;
    }
    
    public DataCollectorView(OTDataCollector collector, boolean controllable)
    {
        dataCollector = collector;
        this.controllable = controllable;
    }

    public JComponent getComponent(OTObject otObject, boolean editable)
    {
    	// For safety verify that the otObject is the same
    	// as the one used in the constructor
    	if(!otObject.equals(dataCollector)){
    		throw new RuntimeException("otObject != dataCollector");
    	}    	
    	
    	if (!controllable){
    		editable = false;
    	}
    	return getDataGraph(editable, controllable);
    }

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
     */
    public void viewClosed()
    {
    	dataGraphManager.viewClosed();
    }
    
    public DataGraph getDataGraph(boolean showToolbar, boolean showDataControls)
    {
	    dataGraphManager = 
	    	new DataGraphManager(dataCollector, serviceProvider, showDataControls);

	    dataGraphManager.setToolbarVisible(showToolbar);
	    
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
}