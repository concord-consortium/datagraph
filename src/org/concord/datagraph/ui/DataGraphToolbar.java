/*
 * Created on Feb 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.ui;

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
        
		addButton("regionscale.gif", 
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
