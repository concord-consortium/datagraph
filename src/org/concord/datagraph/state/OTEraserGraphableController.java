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

import javax.swing.ImageIcon;

import org.concord.data.state.OTDataStore;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.graph.util.state.OTDrawingEraser;
import org.concord.graph.util.state.OTGraphableController;
import org.concord.graph.util.ui.EraserStamp;

public class OTEraserGraphableController extends OTGraphableController
{
	public static Class [] realObjectClasses = {EraserStamp.class};
	public static Class otObjectClass = OTEraserGraphable.class;    
	
    public void loadRealObject(Object realObject)
    {
    	OTEraserGraphable resources = (OTEraserGraphable)otObject;
        // this should do any tasks needed to setup this
        // real object based on the otObject 
    	// the controllerService should be used create new real objects
    	// from children otObjects
		byte [] eraserBytes = resources.getSrc();
		
		if (eraserBytes == null) {
			
			//Maybe icon comes from a stamp
			OTDrawingEraser otEraser = resources.getEraser();
			if (otEraser != null) {
				eraserBytes = otEraser.getSrc();
			}
			
		}
		
		if (eraserBytes == null) {
			// nothing to initialize
			return;
		}
			
		ImageIcon eraserIcon = new ImageIcon(eraserBytes);

        EraserStamp eraserObj = (EraserStamp)realObject;
        
        eraserObj.setImage(eraserIcon);
        
		OTDataStore dataStore = resources.getDataStore();

        if(dataStore == null) {
        	// If the dataStore is null then we create a new
        	// one to store the data so it can retrieved 
        	// later.  If the data needs to be referenced
        	// within the content then it should be explictly 
        	// defined in the content.
            try {
                OTObjectService objService = resources.getOTObjectService();
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
        
    }

	/**
	 * @see org.concord.framework.otrunk.OTController#saveObject(java.lang.Object)
	 */
	public void saveRealObject(Object realObject)
	{
    	OTEraserGraphable resources = (OTEraserGraphable)otObject;

    	EraserStamp eraserObj = (EraserStamp)realObject;
		Point2D loc = eraserObj.getLocation();
		
		resources.setX((float)loc.getX());
		resources.setY((float)loc.getY());
		
		resources.setWeightX(eraserObj.getWeight()[0]);
		resources.setWeightY(eraserObj.getWeight()[1]);
		
        resources.setPoints(eraserObj.getPoints());

		//Color
		Color c = eraserObj.getBgColor();
		if (c != null){
			resources.setBgColor(c.getRGB() & 0x00FFFFFF);
		}
		//
	}
}
