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

import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataPointRuler;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.graph.util.state.OTPointTextLabel;
import org.concord.graph.util.ui.BoxTextLabel;

public class OTDataPointRuler  extends OTPointTextLabel
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
	public OTDataPointRuler(ResourceSchema resources)
	{
		super(resources);
		this.resources = resources;
	}
	
    public OTDataGraphable getDataGraphable()
    {
        return resources.getDataGraphable();
    }
    
	/**
	 * @see org.concord.graph.util.state.OTPointTextLabel#createNewWrappedObject()
	 */
	protected BoxTextLabel createNewWrappedObject()
	{
		DataPointRuler l = new DataPointRuler();

		// we have to rely on the caller that is creating this wrapped
		// object to initialize the dataGraphable of the label.  
		// because the same OTDataGraphable can be displayed in more 
		// than one place at a time the real DataGraphable that should
		// be used here is only known by the caller of this method		
		
		return l;
	}
	
	/**
	 * @see org.concord.framework.otrunk.OTWrapper#saveObject(java.lang.Object)
	 */
	public void saveObject(Object wrappedObject)
	{
		DataPointRuler l = (DataPointRuler)wrappedObject;
		
		OTObjectService objService = getOTObjectService();

		DataGraphable dg = l.getDataGraphable();
		if(dg == null) {
			super.saveObject(wrappedObject);
			return;
		}
		OTDataGraphable otGraphable = (OTDataGraphable)objService.getWrapper(dg);
		resources.setDataGraphable(otGraphable);
		
		super.saveObject(wrappedObject);
	}
}