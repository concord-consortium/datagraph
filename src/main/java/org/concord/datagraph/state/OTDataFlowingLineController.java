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
 * $Revision: 1.1 $
 * $Date: 2007-01-08 20:06:15 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/

package org.concord.datagraph.state;

import org.concord.datagraph.ui.DataFlowingLine;
import org.concord.graph.util.state.OTGraphableController;
import org.concord.graph.util.ui.ImageStamp;

/**
 * OTDataFlowingLineController
 * Class name and description
 *
 * Date created: Apr 07, 2005
 *
 * @author imoncada<p>
 *
 */
public class OTDataFlowingLineController extends OTGraphableController
{
	public static Class [] realObjectClasses = {DataFlowingLine.class};
	public static Class otObjectClass = OTDataFlowingLine.class;
	
    public void loadRealObject(Object realObject)
    {
    	OTDataFlowingLine resources =  (OTDataFlowingLine)otObject;

    	Object image1 = controllerService.getRealObject(resources.getImage1());
        Object image2 = controllerService.getRealObject(resources.getImage2());;
    	
        DataFlowingLine fLine = (DataFlowingLine)realObject;
        fLine.setImage1((ImageStamp)image1);
        fLine.setImage2((ImageStamp)image2);
        
        fLine.setCycleDistance(20);
        
        fLine.addDataProducer(resources.getDataProducer());
    }
    
	/**
	 * @see org.concord.framework.otrunk.OTController#saveObject(java.lang.Object)
	 */
	public void saveRealObject(Object realObject)
	{
	    // we currently don't support saving
	}

	/**
	 * @see org.concord.framework.otrunk.OTController#registerWrappedObject(java.lang.Object)
	 */
	public void registerRealObject(Object realObject)
	{
		super.registerRealObject(realObject);

		DataFlowingLine fLine = (DataFlowingLine)realObject;
        fLine.start();
	}

}
