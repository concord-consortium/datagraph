
/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
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
 */

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
import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.otrunk.DefaultOTObject;
import org.concord.framework.otrunk.OTResourceSchema;
import org.concord.framework.otrunk.OTWrapper;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataGraphable extends DefaultOTObject
	implements OTWrapper
{
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
    }
    
    private ResourceSchema resources;
    
	public OTDataGraphable(ResourceSchema resources)
	{
	    super(resources);
	    this.resources = resources;
	}

	DataGraphable wrappedObject = null;
	
	/**
	 * This method is used by the otrunk framework to save this
	 * object.  It will also be using during instanciation so 
	 * objects that need DataGraphables don't need to know about
	 * this OTDataGraphable they will just get the wrapped object.
	 * Finally this is used ad Author time to validate the use of
	 * this object
	 * 
	 * @return
	 */
	public DataGraphable getWrappedObject()
	{
	    return getDataGraphable();
	}
	
    public DataGraphable getDataGraphable()
    {
        if(wrappedObject == null) {
            if(resources.getControllable()){
                wrappedObject = new ControllableDataGraphable();
            } else {
                wrappedObject = new DataGraphable();
            }
            wrappedObject.setColor(new Color(resources.getColor()));
            wrappedObject.setShowCrossPoint(resources.getDrawMarks());
            wrappedObject.setLabel(resources.getName());
        }

        return wrappedObject;
    }

    public int getXColumn()
    {
        return resources.getXColumn();
    }
    
    public int getYColumn()
    {
        return resources.getYColumn();
    }
    
    public DataProducer getDataProducer()
    {
        return resources.getDataProducer();
    }
    
    public DataStore getDataStore()
    {
        return resources.getDataStore();
    }
    
    public void saveObject()
    {
		Color c = wrappedObject.getColor();
		resources.setColor(c.getRGB() & 0x00FFFFFF);
		resources.setConnectPoints(wrappedObject.isConnectPoints());
		resources.setDrawMarks(wrappedObject.isShowCrossPoint());
		resources.setXColumn(wrappedObject.getChannelX());
		resources.setYColumn(wrappedObject.getChannelY());
    }
}
