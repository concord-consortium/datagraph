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
 * $Revision: 1.11 $
 * $Date: 2007-09-28 18:35:02 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;


/**
 * PfDataGraph
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public interface OTDataGraph extends OTObjectInterface
{
    public String getTitle();
    public void setTitle(String title);
    
    /**
     * The graphables are cloned when a new data store is 
     * added to the graph.  This could happen when the new button
     * is pressed to create a spot to store newly collected data
     * or this could happen when data is imported into the graph. 
     * @return
     */
    public OTObjectList getPrototypeGraphables();

    public OTObjectList getGraphables();
	
	public OTObjectList getLabels();
	
	public OTDataAxis getYDataAxis();
	public void setYDataAxis(OTDataAxis yDataAxis);

	public OTDataAxis getXDataAxis();
	public void setXDataAxis(OTDataAxis xDataAxis);
	
	public boolean getShowToolbar();
	public void setShowToolbar(boolean flag);
	
	/** This sets the desired aspect ratio (X/Y) for the graph component */
	public static float DEFAULT_aspectRatio = 1.5f;
	float getAspectRatio();

    
	/** This determines whether the list of graphables in shown to the left of the graph */
	public boolean getShowGraphableList();
	public void setShowGraphableList(boolean flag);
	
	public static boolean DEFAULT_graphableListEditable=true;
	public boolean getGraphableListEditable();
	public void setGraphableListEditable(boolean flag);
}
