

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
 * $Revision: 1.3 $
 * $Date: 2005-01-31 17:41:33 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;


/**
 * PfDataAxis
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public interface OTDataAxis
	extends OTObject
{
	public float getMin();
	public void setMin(float min);
	
	public float getMax();
	public void setMax(float max);

	public void setLabel(String label);
	public String getLabel();
	
	public void setUnits(String units);
	public String getUnits();
	
	public OTObjectList getGraphables();	
}
