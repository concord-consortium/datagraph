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
import org.concord.framework.otrunk.OTWrappedObject;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataGraphable extends DefaultOTObject
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
    }
}
