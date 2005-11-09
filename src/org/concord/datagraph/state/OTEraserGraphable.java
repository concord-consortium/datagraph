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

package org.concord.datagraph.state;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.EventObject;

import javax.swing.ImageIcon;

import org.concord.data.state.OTDataStore;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.DefaultOTObject;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.framework.otrunk.OTResourceSchema;
import org.concord.framework.otrunk.OTWrapper;
import org.concord.graph.event.GraphableListener;
import org.concord.graph.util.state.OTDrawingEraser;
import org.concord.graph.util.ui.EraserStamp;
import org.concord.graph.util.ui.ImageStamp;

public class OTEraserGraphable extends DefaultOTObject 
implements OTWrapper, GraphableListener{

    public static interface ResourceSchema extends OTResourceSchema
    {
		public byte [] getSrc();
		public void setSrc(byte[] src);
		
		public float getX();
		public void setX(float x);
		
		public float getY();
		public void setY(float y);
		
    	public OTDataStore getDataStore();
    	public void setDataStore(OTDataStore store);
    	
		//When the icon comes from a eraser
		public OTDrawingEraser getEraser();
		public void setEraser(OTDrawingEraser stamp);

		public static int DEFAULT_bgColor = 0x00FFFFFF;
    	public int getBgColor();
    	public void setBgColor(int color);
    	
    	public static boolean DEFAULT_allowHide = true;
    	public boolean getAllowHide();
    	public void setAllowHide(boolean flag);
    		
    	public static boolean DEFAULT_locked = false;
    	public boolean getLocked();
    	public void setLocked(boolean locked);
    	
    	// points are the path that eraser is drawn.
    	public float[] getPoints();
    	public void setPoints(float[] points);
    	
    	// weight is the pixel that the eraser should occupy.
    	public static int DEFAULT_weightX = 1;
    	public int getWeightX();
    	public void setWeightX(int weightX);
    	
    	public static int DEFAULT_weightY = 1;
    	public int getWeightY();
    	public void setWeightY(int weightY);
    }
    
    private ResourceSchema resources;
    
	public OTEraserGraphable(ResourceSchema resources) {
		super(resources);
		this.resources = resources;
	}

	public void setEraser(OTDrawingEraser eraser)
	{
		resources.setEraser(eraser);
	}
		
	public Object createWrappedObject()
	{
		EraserStamp eraserObj = new EraserStamp();
		
		//
		OTDataStore dataStore = resources.getDataStore();

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
        
        int channels = dataStore.getTotalNumChannels();
        int samples = dataStore.getTotalNumSamples();
        float[] points = new float[channels*samples]; 
        
        for(int i = 0; i < channels; i++) {
        	for(int j = 0; j < samples; j++) {
        		points[i + j*channels] = ((Float)dataStore.getValueAt(j, i)).floatValue();
        	}
        }

		eraserObj.setPoints(points);
        eraserObj.setBgColor(new Color(resources.getBgColor()));
        int[] weight = {resources.getWeightX(), resources.getWeightY()};
        eraserObj.setWeight(weight);

		registerWrappedObject(eraserObj);
		
        return eraserObj;
    }
    
	/**
	 * @see org.concord.framework.otrunk.OTWrapper#saveObject(java.lang.Object)
	 */
	public void saveObject(Object wrappedObject)
	{
		EraserStamp eraserObj = (EraserStamp)wrappedObject;
		Point2D loc = eraserObj.getLocation();
		
		resources.setX((float)loc.getX());
		resources.setY((float)loc.getY());
		
		resources.setWeightX(eraserObj.getWeight()[0]);
		resources.setWeightY(eraserObj.getWeight()[1]);
		
		//Color
		Color c = eraserObj.getBgColor();
		if (c != null){
			resources.setBgColor(c.getRGB() & 0x00FFFFFF);
		}
		//
	}

	/**
	 * @see org.concord.framework.otrunk.OTWrapper#getWrappedObjectClass()
	 */
	public Class getWrappedObjectClass()
	{
		return EraserStamp.class;
	}

	/**
	 * @see org.concord.framework.otrunk.OTWrapper#registerWrappedObject(java.lang.Object)
	 */
	public void registerWrappedObject(Object wrappedObject)
	{
		EraserStamp imgObj = (EraserStamp)wrappedObject;
		
		//Now, listen to this object so I can be updated automatically when it changes
		imgObj.addGraphableListener(this);		
		getOTObjectService().putWrapper(wrappedObject, this);
	}

	/**
	 * @see org.concord.graph.event.GraphableListener#graphableChanged(java.util.EventObject)
	 */
	public void graphableChanged(EventObject e)
	{
		saveObject(e.getSource());
	}

	/**
	 * @see org.concord.graph.event.GraphableListener#graphableRemoved(java.util.EventObject)
	 */
	public void graphableRemoved(EventObject e)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void setX(float x) {
		resources.setX(x);
	}
	public void setY(float y) {
		resources.setY(y);
	}

	public boolean getLocked() {
		return resources.getLocked();
	}
	public void setLocked(boolean locked) {
		resources.setLocked(locked);
	}	
	
	public void setWeight(int[] weight) {
		resources.setWeightX(weight[0]);
		resources.setWeightY(weight[1]);
	}
	public void setPoints(float[] points) {
		resources.setPoints(points);
	}
	public void setBgColor(int bgColor) {
		resources.setBgColor(bgColor);
	}
	public void setDataStore(OTDataStore dataStore) {
		resources.setDataStore(dataStore);
	}
}
