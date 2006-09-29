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
 * $Revision: 1.2 $
 * $Date: 2006-09-29 14:01:00 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataAnnotation;
import org.concord.framework.otrunk.OTWrapperService;
import org.concord.graph.util.state.OTPointTextLabel;


/**
 * PfDataGraphable
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public class OTDataAnnotation extends OTPointTextLabel
{
	public static interface ResourceSchema extends OTPointTextLabel.ResourceSchema
	{	
		public OTDataGraphable getDataGraphable();
		public void setDataGraphable(OTDataGraphable b);
	}

	private ResourceSchema resources;
	
	/**
	 * 
	 */
	public OTDataAnnotation(ResourceSchema resources)
	{
		super(resources);
		this.resources = resources;
	}
	
    public OTDataGraphable getDataGraphable()
    {
        return resources.getDataGraphable();
    }

	public Class getRealObjectClass()
	{
		return getRealObjectClassInternal();
	}
	
    public void loadRealObject(OTWrapperService wrapperService, Object wrappedObject) {
    	super.loadRealObject(wrapperService, wrappedObject);

    	DataAnnotation a = (DataAnnotation)wrappedObject;
    	
    	// find the correct graphable for this label
    	OTDataGraphable otGraphable = resources.getDataGraphable();
    	if(otGraphable != null) {
    		DataGraphable dg = (DataGraphable)wrapperService.getRealObject(otGraphable);
    		a.setDataGraphable(dg);
    	} 
    }

	/**
	 * @see org.concord.framework.otrunk.OTWrapper#saveObject(java.lang.Object)
	 */
	public void saveRealObject(OTWrapperService wrapperService, Object wrappedObject)
	{
		DataAnnotation a = (DataAnnotation)wrappedObject;
		
		DataGraphable dg = a.getDataGraphable();
		if(dg != null) {
			OTDataGraphable otGraphable = 
				(OTDataGraphable)wrapperService.getWrapper(dg);
			resources.setDataGraphable(otGraphable);
		} 
		
		super.saveRealObject(wrapperService, wrappedObject);
	}
}
