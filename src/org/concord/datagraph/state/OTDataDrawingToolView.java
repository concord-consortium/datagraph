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
 * $Revision: 1.10 $
 * $Date: 2005-08-04 21:46:09 $
 * $Author: maven $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.Dimension;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.concord.data.state.OTDataStore;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.ControllableDataGraphableDrawing;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTWrapper;
import org.concord.framework.otrunk.view.OTPrintDimension;
import org.concord.framework.otrunk.view.OTViewContainer;
import org.concord.graph.util.engine.DrawingObject;
import org.concord.graph.util.state.OTDrawingToolView;

/**
 * OTDataDrawingToolView
 * Class name and description
 *
 * Date created: Apr 10, 2005
 *
 * @author imoncada<p>
 *
 */
public class OTDataDrawingToolView extends OTDrawingToolView
implements OTPrintDimension {
	OTObject tool;
	OTViewContainer viewContainer;
	
    public void initialize(OTObject tool, OTViewContainer container)
    {
    	super.initialize(tool, container);
    	this.tool = tool;
    	this.viewContainer = container;
        //setMaximumSize(new Dimension(550, 220));

    }
    
	/**
	 * @see org.concord.graph.util.engine.DrawingObjectFactory#createNewDrawingObject(int)
	 */
	public DrawingObject createNewDrawingObject(int type)
	{
		//PointsDataStore points = new PointsDataStore();
		OTDataStore otDataStore;
		try{
			otDataStore = (OTDataStore)drawingTool.getOTDatabase().createObject(OTDataStore.class);
            otDataStore.setUseDtAsChannel(false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
		ControllableDataGraphable dg = new ControllableDataGraphableDrawing();
		dg.setDrawAlwaysConnected(false);
		dg.setDataStore(otDataStore, 0, 1);
		dg.setLineType(ControllableDataGraphable.LINETYPE_FREE);
		objList.add(dg);
		return dg;
	}
	
	/**
	 * @see org.concord.graph.event.GraphableListListener#listGraphableAdded(java.util.EventObject)
	 */
	public void listGraphableAdded(EventObject e)
	{
		Object obj = e.getSource();
		OTWrapper otWrapper = null;
		
		if (obj instanceof ControllableDataGraphableDrawing){
			
			ControllableDataGraphableDrawing drawObj = (ControllableDataGraphableDrawing)obj;
			OTDataGraphable otDataGraphable;
		
			try{
				otDataGraphable = (OTDataGraphable)drawingTool.getOTDatabase().createObject(OTDataGraphable.class);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			otDataGraphable.setDrawing(true);
			otDataGraphable.setDataStore((OTDataStore)drawObj.getDataStore());
			
			otWrapper = otDataGraphable;
			drawingTool.getGraphables().add(otDataGraphable);
			
			if (otWrapper != null){
				otWrapper.registerWrappedObject(obj);
				otWrapper.saveObject(obj);
			}
			
		}
		else{
			super.listGraphableAdded(e);
		}
	}
	
	/**
	 * @see org.concord.graph.util.state.OTDrawingToolView#loadGraphable(java.lang.Object)
	 */
	protected void loadGraphable(Object objOT)
	{
		if (objOT instanceof OTDataGraphable){
			OTDataGraphable otDataGraphable = (OTDataGraphable)objOT;	
			
			addDrawingObject((ControllableDataGraphableDrawing)otDataGraphable.createWrappedObject());
		}
		else{
			super.loadGraphable(objOT);
		}
	}

	public Dimension getPrintDimention(int containerDisplayWidth, int containerDisplayHeight) {
		Dimension dim = new Dimension(550, 225);
        if(dim.height > containerDisplayHeight) 
            dim.height = containerDisplayHeight;
        if(dim.width > containerDisplayWidth) 
            dim.width = containerDisplayWidth;
        return dim;
	}    
}
