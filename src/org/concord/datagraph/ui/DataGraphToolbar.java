
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
 * $Revision: 1.6 $
 * $Date: 2005-04-18 02:55:50 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import javax.swing.AbstractButton;

import org.concord.graph.engine.AxisScale;
import org.concord.graph.engine.MultiRegionAxisScale;
import org.concord.graph.examples.GraphWindowToolBar;

/**
 * DataGraphToolbar
 *
 * Date created: Feb 22, 2005
 *
 * @author Scott Cytacki<p>
 * @author Ingrid Moncada<p>
 *
 */
public class DataGraphToolbar extends GraphWindowToolBar
{
	protected AbstractButton selButton;
	
    public DataGraphToolbar()
    {
        super(false);
        
		selButton = addButton("arrow.gif", 
		        "" + MultiRegionAxisScale.DRAGMODE_TRANSLATE_DILATE, 
		        "Move and Scale graph");

		addButton("zoomin.gif", 
		        "" + AxisScale.DRAGMODE_ZOOM_IN, 
		        "Zoom in to a point");

		addButton("zoomout.gif", 
		        "" + AxisScale.DRAGMODE_ZOOM_OUT, 
		        "Zoom out from a point");
		
		addButton("restorescale.gif", "restorescale", "Restore initial scale", false);

		setDefaultButton(selButton);
		
    }
    
	/**
	 * @see org.concord.graph.examples.GraphWindowToolBar#addAxisScale(org.concord.graph.engine.AxisScale)
	 */
	public void addAxisScale(AxisScale ax)
	{
		super.addAxisScale(ax);
		selButton.doClick();
	}
}
