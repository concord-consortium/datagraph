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
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JComponent;

import org.concord.datagraph.engine.DataGraphAutoScaler;
import org.concord.datagraph.ui.DataGraph;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;
import org.concord.framework.otrunk.view.OTDefaultComponentProvider;
import org.concord.framework.otrunk.view.OTJComponentViewContext;
import org.concord.framework.otrunk.view.OTJComponentViewContextAware;
import org.concord.framework.otrunk.view.OTLabbookViewProvider;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataCollectorView extends AbstractOTJComponentView 
	implements OTJComponentViewContextAware, OTDefaultComponentProvider, OTLabbookViewProvider
{
    AbstractOTJComponentView view;
    OTDataCollector dataCollector;
    boolean multipleGraphableEnabled = false;
	protected OTJComponentViewContext jComponentViewContext;
        
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#getComponent(boolean)
     */
    public JComponent getComponent(OTObject otObject)
    {
        setup(otObject);
        
        return view.getComponent(otObject);
    }
    
    /**
     * Initializes the basic variables. Refactored from getComponent method
     * so that labbook methods can use it as well.
     * @param otObject
     */
    private void setup(OTObject otObject){
    	this.dataCollector = (OTDataCollector)otObject;
        if(dataCollector.getSingleValue()) {
            view = new SingleValueDataView(dataCollector);
        }
        else {
            view = new DataCollectorView(dataCollector, getControllable(), true);
        }
        
        view.setViewContext(viewContext);
        if (view instanceof OTJComponentViewContextAware){
        	((OTJComponentViewContextAware)view).setOTJComponentViewContext(jComponentViewContext);
        }
    }

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
     */
    public void viewClosed()
    {
        if(view != null) {
            view.viewClosed();
        }
    }

	public void setOTJComponentViewContext(OTJComponentViewContext viewContext)
    {
	    this.jComponentViewContext = viewContext;
    }
	
	public boolean getControllable()
	{
		return dataCollector.getShowControlBar();
	}
	
	public DataCollectorView getDataCollectorView(){
		if (view instanceof DataCollectorView){
			return (DataCollectorView)view;
		} else {
			return null;
		}
	}

	public Component getDefaultComponent()
    {
		if (view instanceof DataCollectorView)
			return ((DataCollectorView)view).getDataGraph().getGraph();
		else
			return view.getComponent(dataCollector);
    }
	
	/**
	 * For OTLabbookViewProvider. Here we just clone the object
	 */
	public OTObject copyObjectForSnapshot(OTObject otObject)
    {
	    try {
	        OTObject copy =  otObject.getOTObjectService().copyObject(otObject, -1);
	        if (copy instanceof OTDataCollector){
	        	((OTDataCollector)copy).setMultipleGraphableEnabled(false);
	        	if(((OTDataCollector)copy).getSource() != null){
	        		((OTDataCollector)copy).getSource().setControllable(false);
	        	}
	    		((OTDataCollector)copy).setAutoScaleEnabled(false);
	    		((OTDataCollector)copy).setDisplayButtons("4");
	        }
	        return copy;
        } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        return otObject;
    }

	/**
	 * For OTLabbookViewProvider.
	 */
	public boolean drawtoolNeededForAlbum()
    {
	    // TODO Auto-generated method stub
	    return false;
    }

	/**
	 * For OTLabbookViewProvider. This returns the regular view with the graph set to not
	 * be controllable
	 */
	public JComponent getLabbookView(OTObject otObject)
    {
		if(((OTDataCollector)otObject).getSource() != null){
			((OTDataCollector)otObject).getSource().setControllable(false);
		}
		
		((OTDataCollector)otObject).setMultipleGraphableEnabled(true);
		setup(otObject);
		
	     //   view.getComponent(otObject);
			if (view instanceof DataCollectorView){
				DataGraph graph = ((DataCollectorView)view).getDataGraph(true, false);
				graph.setAutoFitMode(DataGraph.AUTO_SCALE_MODE);
				final DataGraphAutoScaler autoscaler = graph.getAutoScaler();
				autoscaler.setAutoScaleX(true);
				autoscaler.setAutoScaleY(true);
				
				return graph;
			} else
				return view.getComponent(dataCollector);
    }
	
	/**
	 * For OTLabbookViewProvider. This returns a scaled-down graph without the toolbars and
	 * with a smaller title.
	 */
	public JComponent getThumbnailView(OTObject otObject, int height)
    {
		if(((OTDataCollector)otObject).getSource() != null){
			((OTDataCollector)otObject).getSource().setControllable(false);
		}

		((OTDataCollector)otObject).setMultipleGraphableEnabled(false);
		setup(otObject);
		
     //   view.getComponent(otObject);
		if (view instanceof DataCollectorView){
			DataGraph graph = ((DataCollectorView)view).getDataGraph(false, false);
			graph.setScale(2, 2);
			graph.setAutoFitMode(DataGraph.AUTO_SCALE_MODE);
			graph.setInsets(new Insets(0,8,8,0));
			graph.setTitle(graph.getTitle(), 9);
			
			graph.setPreferredSize(new Dimension((int) (height*1.3), height));
			return graph;
		} else
			return view.getComponent(dataCollector);
			
    }
	
	public DataGraphManager getGraphManager() {
		if (view instanceof DataCollectorView) {
			return ((DataCollectorView) view).getDataGraphManager();
		}
		return null;
	}
}
