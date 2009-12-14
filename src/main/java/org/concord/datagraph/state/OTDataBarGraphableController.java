/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-08-30 21:08:33 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.ControllableDataGraphableDrawing;
import org.concord.datagraph.engine.DataBarGraphable;
import org.concord.datagraph.engine.DataGraphable;


/**
 * OTDataBarGraphableController
 * Class name and description
 *
 * Date created: Aug 30, 2007
 *
 * @author imoncada<p>
 *
 */
public class OTDataBarGraphableController extends OTDataGraphableController
{
	public static Class [] realObjectClasses =  {
		DataBarGraphable.class
	};
	
	public static Class otObjectClass = OTDataBarGraphable.class;    
	
	/**
	 * @see org.concord.framework.otrunk.OTController#getRealObjectClass()
	 */
	public Class getRealObjectClass()
	{
    	return DataBarGraphable.class;
	}
	
	/**
	 * @see org.concord.datagraph.state.OTDataGraphableController#loadRealObject(java.lang.Object)
	 */
	public void loadRealObject(Object realObject)
	{
		super.loadRealObject(realObject);
		
    	OTDataBarGraphable model = (OTDataBarGraphable)otObject;
		DataBarGraphable dg = (DataBarGraphable)realObject;

		if (model.isResourceSet("maxBarsToSelect")){
			dg.setMaxBarsCanBeSelected(model.getMaxBarsToSelect());
		}
	}

}
