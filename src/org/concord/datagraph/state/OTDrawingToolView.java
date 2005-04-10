
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
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2005-04-10 17:58:54 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
 */
package org.concord.datagraph.state;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;
import org.concord.graph.event.GraphableListListener;
import org.concord.graph.util.state.OTDrawingShape;
import org.concord.graph.util.state.OTDrawingStamp;
import org.concord.graph.util.state.OTDrawingTool;
import org.concord.graph.util.state.OTDrawingImageIcon;
import org.concord.graph.util.ui.DrawingGraph;
import org.concord.graph.util.ui.ImageStamp;

/**
 * OTDrawingToolView
 * Class name and description
 *
 * Date created: Apr 05, 2005
 *
 * @author imoncada<p>
 *
 */
public abstract class OTDrawingToolView extends DrawingGraph
    implements OTObjectView, GraphableListListener
{
    OTDrawingTool drawingTool;
    
    public OTDrawingToolView(OTDrawingTool tool, OTViewContainer container)
    {
    	super();
    	
    	//Listen to the graph so the ot object can be saved every time there is a change
    	objList.addGraphableListListener(this);
    	
        drawingTool = tool;
    }
    
	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.view.OTObjectView#getComponent(boolean)
	 */
	public JComponent getComponent(boolean editable)
	{
		setGridVisible(drawingTool.getGridVisible());
		
		byte [] bgBytes = drawingTool.getBackgroundImage();
		if(bgBytes != null) {
			ImageIcon bgImage = new ImageIcon(bgBytes);
			setBackgroundImage(bgImage);
		}
		
		OTObjectList stamps = drawingTool.getStamps();
		
		for(int i=0; i<stamps.size(); i++) {
			OTDrawingStamp stamp = (OTDrawingStamp)stamps.get(i);
			byte [] stampBytes = stamp.getSrc();
			ImageIcon stampIcon = new ImageIcon(stampBytes, stamp.getDescription());
			addStampIcon(stampIcon);
		}
		
		OTObjectList graphables = drawingTool.getGraphables();
		
		for(int i=0; i<graphables.size(); i++) {
			Object objOT = graphables.get(i);
			if (objOT instanceof OTDrawingImageIcon){
				OTDrawingImageIcon otImage = (OTDrawingImageIcon)objOT;	
				
				addImageIcon(otImage.getWrappedObject());
			}
			else if (objOT instanceof OTDrawingShape){
				OTDrawingShape otShape = (OTDrawingShape)objOT;	
				
				addShape(otShape.getWrappedObject());
			}
		}
		
		return this;
	}

	/**
	 * @see org.concord.graph.event.GraphableListListener#listGraphableAdded(java.util.EventObject)
	 */
	public void listGraphableAdded(EventObject e)
	{
		Object obj = e.getSource();
		
		if (obj instanceof ImageStamp){
			
/*			ImageStamp imgObj = (ImageStamp)obj;
			OTImageIconGraphable otImg;
			
			try{
				otImg = (OTImageIconGraphable)drawingTool.getOTDatabase().createObject(OTImageIconGraphable.class);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			
		//	otImg.setX((float)imgObj.getLocation().getX());
		//	otImg.setY((float)imgObj.getLocation().getY());
			
			drawingTool.getGraphables().add(otImg);*/
		}
	}

	/**
	 * @see org.concord.graph.event.GraphableListListener#listGraphableRemoved(java.util.EventObject)
	 */
	public void listGraphableRemoved(EventObject e)
	{
	}

	/**
	 * @see org.concord.graph.event.GraphableListListener#listGraphableChanged(java.util.EventObject)
	 */
	public void listGraphableChanged(EventObject e)
	{
	}
}
