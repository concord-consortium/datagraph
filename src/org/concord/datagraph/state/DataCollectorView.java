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

import java.util.EventObject;

import javax.swing.JComponent;

import org.concord.datagraph.ui.AddDataPointLabelAction;
import org.concord.datagraph.ui.AddDataPointLabelActionExt;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.DataPointLabel;
import org.concord.datagraph.ui.DataPointRuler;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.framework.otrunk.OTWrapper;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;
import org.concord.graph.engine.SelectableList;
import org.concord.graph.event.GraphableListListener;
import org.concord.swing.SelectableToggleButton;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataCollectorView
 implements GraphableListListener, 	OTObjectView
{
    OTDataCollector dataCollector;
	SelectableList notesLayer;
	WritableDataStore dataStore;	
	DataGraphManager dataGraphManager;
	OTViewContainer vContainer;
	
    public DataCollectorView(OTDataCollector collector)
    {
        dataCollector = collector;
    }

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTObjectView#initialize(org.concord.framework.otrunk.OTObject, org.concord.framework.otrunk.view.OTViewContainer)
     */
    public void initialize(OTObject otObject, OTViewContainer viewContainer)
    {
        // TODO Auto-generated method stub
    	this.vContainer = viewContainer;
    }
    
    public JComponent getComponent(boolean editable)
    {
        return getDataGraph(editable, true);
    }
     
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTObjectView#viewClosed()
     */
    public void viewClosed()
    {
        // TODO Auto-generated method stub
    }
    
    public DataGraph getDataGraph(boolean showToolbar, boolean showDataControls)
    {
	    dataGraphManager = new DataGraphManager(dataCollector, showDataControls);

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
    
	/**
	 * @see org.concord.graph.event.GraphableListListener#listGraphableAdded(java.util.EventObject)
	 */
	public void listGraphableAdded(EventObject e)
	{
		Object obj = e.getSource();
		if (obj instanceof DataPointLabel){
			DataPointLabel l;
			OTDataPointLabel otLabel;

			try{
                OTObjectService objService = dataCollector.getOTObjectService();
				otLabel = (OTDataPointLabel)objService.createObject(OTDataPointLabel.class);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			
			l = (DataPointLabel)obj;
			
			otLabel.registerWrappedObject(l);
			otLabel.saveObject(l);

			dataCollector.getLabels().add(otLabel);
		} else if (obj instanceof DataPointRuler){
			DataPointRuler ruler = (DataPointRuler)obj;
			OTDataPointRuler otRuler;

			try{
                OTObjectService objService = dataCollector.getOTObjectService();
				otRuler = (OTDataPointRuler)objService.createObject(OTDataPointRuler.class);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			
			otRuler.registerWrappedObject(ruler);
			otRuler.saveObject(ruler);

			dataCollector.getLabels().add(otRuler);
		}
	}
		
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableChanged(java.util.EventObject)
	 */
	public void listGraphableChanged(EventObject e)
	{
		
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableRemoved(java.util.EventObject)
	 */
	public void listGraphableRemoved(EventObject e)
	{
		//dataGraphManager.listGraphableRemoved(e);
		Object obj = e.getSource();
		
		OTWrapper otWrapper = dataCollector.getOTObjectService().getWrapper(obj);
		
		if (otWrapper != null){
			if(otWrapper instanceof OTDataPointLabel || 
			   otWrapper instanceof OTDataPointRuler   ) {
				dataCollector.getLabels().remove(otWrapper);
				//System.out.println("deleted");
			}
			
		}
	}
}