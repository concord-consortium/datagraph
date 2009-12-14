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
 * $Revision: 1.22 $
 * $Date: 2007-10-11 20:15:06 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.data.state.OTDataStore;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.ControllableDataGraphableDrawing;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.graph.util.engine.DrawingObject;
import org.concord.graph.util.state.OTDrawingTool;
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
{
	/**
     * Not intended to be serialized but this is here so 
     * the compiler warning goes away.
     */
    private static final long serialVersionUID = 1L;
    OTObject tool;
	
    protected void setup(OTObject tool)
    {
    	super.setup(tool);
    	this.tool = tool;

    	// We need to make sure this package is registered.  This view can be used to display 
    	// an OTDrawingTool which is in a different package.  So if no OTObjects are used from 
    	// the OTDatagraphPackage then the OTrunk system won't know about this package.  
    	// And then the controllers won't be correctly registered.  When the data store is created
    	// below.
    	tool.getOTObjectService().registerPackageClass(OTDatagraphPackage.class);
    	
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
            OTObjectService objService = drawingTool.getOTObjectService();            
            otDataStore = (OTDataStore)objService.createObject(OTDataStore.class);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
		// The controllable data graphable needs a writable datastore 
		// it is just using the the setDataStore method from DataGraphable
		WritableDataStore wDataStore = (WritableDataStore) controllerService.getRealObject(otDataStore);
		
		ControllableDataGraphable dg = new ControllableDataGraphableDrawing();
		((ControllableDataGraphableDrawing)dg).setShowBoundingBox(((OTDrawingTool)tool).getIsVectorStyle());
		dg.setDrawAlwaysConnected(false);
		dg.setDataStore(wDataStore, 0, 1);
		dg.setLineType(ControllableDataGraphable.LINETYPE_FREE);
		objList.add(dg);
		return dg;
	}	
}
