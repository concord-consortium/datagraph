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

import org.concord.data.state.OTDataProducer;
import org.concord.datagraph.ui.DataGraph.AspectDimension;
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
	
	/**
	 * List of extra graphables that are to be shown in the graph (not data)
	 * @return
	 */
	public OTObjectList getExtraGraphables();
	
	public OTDataAxis getYDataAxis();
	public void setYDataAxis(OTDataAxis yDataAxis);

	public OTDataAxis getXDataAxis();
	public void setXDataAxis(OTDataAxis xDataAxis);
	
	public static boolean DEFAULT_showToolbar = true;
	public boolean getShowToolbar();
	public void setShowToolbar(boolean flag);
	
	/** Toggle for turning on or off adjusting the size to maintain an aspect ratio */
	public static boolean DEFAULT_useAspectRatio = true;
	public boolean getUseAspectRatio();
	public void setUseAspectRatio(boolean use);
	
	/** This sets the desired aspect ratio (X/Y) for the graph component */
	public static float DEFAULT_aspectRatio = 1.5f;
	public float getAspectRatio();
	public void setAspectRatio(float ratio);
	
	/** Which dimension we should adjust when maintaining an aspect ratio */
	public static AspectDimension DEFAULT_aspectDimension = AspectDimension.HEIGHT;
	public AspectDimension getAspectDimension();
	public void setAspectDimension(AspectDimension d);
	
	public boolean getAutoTick();
	public void setAutoTick(boolean autoTick);
	public static boolean DEFAULT_autoTick = true;
	
	/**
	 * This set the tick interval. This is read ONLY if autoTick is false.
	 */
	public double getXTickInterval();
	public void setXTickInterval(double xTickInterval);
	public static double DEFAULT_xTickInterval = 2;
	
	public double getYTickInterval();
	public void setYTickInterval(double yTickInterval);
	public static double DEFAULT_yTickInterval = 2;
    
	/** This determines whether the list of graphables in shown to the left of the graph */
	public boolean getShowGraphableList();
	public void setShowGraphableList(boolean flag);
	
	public static boolean DEFAULT_graphableListEditable=true;
	public boolean getGraphableListEditable();
	public void setGraphableListEditable(boolean flag);
	
	/**
	 * These correspond to the static fields in DataGraph. Current values are:
	 *     AUTO_FIT_NONE = 0;
	 *     AUTO_SCALE_MODE = 1;
	 *     AUTO_SCROLL_MODE = 2;
	 *     AUTO_SCROLL_RUNNING_MODE = 3;
	 */
	public static int DEFAULT_autoFitMode = 3;
	public int getAutoFitMode();
	public void setAutoFitMode(int mode);
	
	/** A "playback" data producer, which can read in multiple datastores and play them back in realtime. See OTTimerDataStoreDataProducer. */
    public OTDataProducer getPlaybackDataProducer();
    
    public static boolean DEFAULT_antialias = true;
    public boolean getAntialias();
}
