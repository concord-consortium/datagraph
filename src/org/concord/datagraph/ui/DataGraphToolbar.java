
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
 * Created on Feb 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.ui;

import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.concord.graph.engine.AxisScale;
import org.concord.graph.engine.MultiRegionAxisScale;
import org.concord.graph.examples.GraphWindowToolBar;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataGraphToolbar extends GraphWindowToolBar
{
    public DataGraphToolbar()
    {
        super(false);
        
		addButton("arrow.gif", 
		        "" + MultiRegionAxisScale.DRAGMODE_TRANSLATE_DILATE, 
		        "Move and Scale graph");

		addButton("zoomin.gif", 
		        "" + AxisScale.DRAGMODE_ZOOM_IN, 
		        "Zoom in to a point");

		addButton("zoomout.gif", 
		        "" + AxisScale.DRAGMODE_ZOOM_OUT, 
		        "Zoom out from a point");
		
		addButton("restorescale.gif", "restorescale", "Restore initial scale", false);
    }
}
