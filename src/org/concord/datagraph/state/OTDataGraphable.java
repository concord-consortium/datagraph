

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
 * $Date: 2005-01-31 17:41:33 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.data.state.OTDataStore;
import org.concord.framework.otrunk.OTObject;


/**
 * PfDataGraphable
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public interface OTDataGraphable
	extends OTObject
{
	public static int DEFAULT_color = 0x00FF0000;
	public int getColor();
	public void setColor(int color);
	
	public static boolean DEFAULT_connectPoints = true;
	public boolean getConnectPoints();
	public void setConnectPoints(boolean flag);

	public static boolean DEFAULT_drawMarks = true;
	public boolean getDrawMarks();
	public void setDrawMarks(boolean flag);
	
	public static boolean DEFAULT_controllable = false;
	public boolean getControllable();
	public void setControllable(boolean flag);
	
	public OTDataStore getDataStore();
	public void setDataStore(OTDataStore store);
	
	public OTObject getDataProducer();
	public void setDataProducer(OTObject producer);

	public static int DEFAULT_xColumn = 0;
	public int getXColumn();
	public void setXColumn(int xCol);
	
	public static int DEFAULT_yColumn = 1;
	public int getYColumn();
	public void setYColumn(int yCol);
}