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
 * Created on Mar 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.Color;

import org.concord.data.state.OTDataStore;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.ControllableDataGraphableDrawing;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.framework.otrunk.OTResourceSchema;
import org.concord.framework.otrunk.OTWrapperService;
import org.concord.graph.util.state.OTGraphableWrapper;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataGraphable extends OTGraphableWrapper
{
	public static Class [] realObjectClasses =  {
		DataGraphable.class, ControllableDataGraphable.class, 
		ControllableDataGraphableDrawing.class
	};
	
    public static interface ResourceSchema extends OTResourceSchema
    {
    	public static int DEFAULT_color = 0x00FF0000;
    	public int getColor();
    	public void setColor(int color);
    	
    	public static boolean DEFAULT_connectPoints = true;
    	public boolean getConnectPoints();
    	public void setConnectPoints(boolean flag);

    	public static boolean DEFAULT_drawMarks = true;
    	public boolean getDrawMarks();
    	public void setDrawMarks(boolean flag);
    	
    	public static boolean DEFAULT_controllable = false;
    	public boolean getControllable();
    	public void setControllable(boolean flag);
    	
    	public static boolean DEFAULT_drawing = false;
    	public boolean getDrawing();
    	public void setDrawing(boolean flag);
    	
    	public static boolean DEFAULT_allowHide = true;
    	public boolean getAllowHide();
    	public void setAllowHide(boolean flag);
    		
    	public OTDataStore getDataStore();
    	public void setDataStore(OTDataStore store);
    	
    	public DataProducer getDataProducer();
    	public void setDataProducer(DataProducer producer);
    	
    	public static int DEFAULT_xColumn = 0;
    	public int getXColumn();
    	public void setXColumn(int xCol);
    	
    	public static int DEFAULT_yColumn = 1;
    	public int getYColumn();
    	public void setYColumn(int yCol);
    	
    	public static boolean DEFAULT_locked = false;
    	public boolean getLocked();
    	public void setLocked(boolean locked);
    }
    
    private ResourceSchema resources;
    
	public OTDataGraphable(ResourceSchema resources)
	{
	    super(resources);
	    this.resources = resources;
	}
	
    public void loadRealObject(OTWrapperService wrapperService, Object wrappedObject)
    {
		DataGraphable dg = (DataGraphable)wrappedObject;

        dg.setColor(new Color(resources.getColor()));
        dg.setShowCrossPoint(resources.getDrawMarks());
        dg.setLabel(resources.getName());
        dg.setUseVirtualChannels(true);
        dg.setLocked(resources.getLocked());
        
		DataProducer producer = resources.getDataProducer();
		OTDataStore dataStore = resources.getDataStore();

		if (resources.getControllable() && producer != null){
			// This is a schema type error
			// we should give more information about tracking it down
		    throw new RuntimeException("Can't control a graphable with a data producer");
		}
		
        if(dataStore == null) {
        	// If the dataStore is null then we create a new
        	// one to store the data so it can retrieved 
        	// later.  If the data needs to be referenced
        	// within the content then it should be explictly 
        	// defined in the content.
            try {
                OTObjectService objService = getOTObjectService();
                dataStore = (OTDataStore)objService.createObject(OTDataStore.class);
                resources.setDataStore(dataStore);
            } catch (Exception e) {
                // we can't handle this
                throw new RuntimeException(e);
            }
        }
        
        // now we can safely assume dataStore != null        
		if (producer != null){
		    dataStore.setDataProducer(producer);
		}
		
		dg.setDataStore(dataStore);

		dg.setChannelX(resources.getXColumn());
		dg.setChannelY(resources.getYColumn());
    }
    
    /**
     * There should be a better way to do this.  Given a DataGraphable that can be started and 
     * stopped (similar to implementing DataFlow interface), it should be possible to get that
     * flow object from the graphable.  Or the data graphable itself should implement the
     * DataFlow interface.
     * 
     * @return
     */
    public DataProducer getDataProducer()
    {
        return resources.getDataProducer();
    }
    
    public void setDataProducer(DataProducer producer) {
    	resources.setDataProducer(producer);
    }
    
	/**
	 * @see org.concord.framework.otrunk.OTWrapper#saveObject(java.lang.Object)
	 */
	public void saveRealObject(OTWrapperService wrapperService, Object wrappedObject)
	{
		DataGraphable dg = (DataGraphable)wrappedObject;
		
		Color c = dg.getColor();
		resources.setColor(c.getRGB() & 0x00FFFFFF);
		resources.setConnectPoints(dg.isConnectPoints());
		resources.setDrawMarks(dg.isShowCrossPoint());
		resources.setXColumn(dg.getChannelX());
		resources.setYColumn(dg.getChannelY());
		resources.setName(dg.getLabel());
        
        // This might not be quite right, lets cross our fingers
        // that it doesn't screw anything else up
        DataStore ds = dg.getDataStore();
        if(ds instanceof OTDataStore){
            resources.setDataStore((OTDataStore)ds);
        }
        
		// This is needed for some reason by the OTDrawingToolView
        // Apparently it is to set the realObject class.
        if(dg instanceof ControllableDataGraphableDrawing) {
    		resources.setDrawing(true);    		
        }
	}

	/**
	 * @see org.concord.framework.otrunk.OTWrapper#getRealObjectClass()
	 */
	public Class getRealObjectClass()
	{
		if (resources.getDrawing()){
        	return ControllableDataGraphableDrawing.class;            
		}
        else if (resources.getControllable()){
        	return ControllableDataGraphable.class;
        } 
        else {
        	return DataGraphable.class;
        }
	}

	/**
	 * @param otDataStore
	 */
	public void setDataStore(OTDataStore otDataStore)
	{
		resources.setDataStore(otDataStore);
	}

	public void setDrawMarks(boolean b) {
		resources.setDrawMarks(b);
	}
	
    public void copyInto(OTDataGraphable copy)
    {        
        resources.copyInto(copy.resources);
    }
}
