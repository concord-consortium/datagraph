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
 * Created on Mar 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import org.concord.data.state.OTDataProducer;
import org.concord.data.state.OTDataStore;
import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTResourceList;

public interface OTDataGraphable extends OTObjectInterface
{
	public static boolean DEFAULT_showControlButtons = true;
	public void setShowControlButtons(boolean flag);
	public boolean getShowControlButtons();
	
	public static boolean DEFAULT_showToolButtons = true;
	public void setShowToolButtons(boolean flag);
	public boolean getShowToolButtons();

	public static boolean DEFAULT_showAllChannels = false;
	public boolean getShowAllChannels();
	public void setShowAllChannels(boolean flag);
	
	public static int DEFAULT_showSampleLimit = -1;
	public int getShowSampleLimit();
	public void setShowSampleLimit(int limit);
	
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
	
	public static boolean DEFAULT_drawing = false;
	public boolean getDrawing();
	public void setDrawing(boolean flag);
	
	public static boolean DEFAULT_allowHide = true;
	public boolean getAllowHide();
	public void setAllowHide(boolean flag);
	
	public static boolean DEFAULT_visible = true;
	public boolean getVisible();
	public void setVisible(boolean flag);
		
	public OTDataStore getDataStore();
	public void setDataStore(OTDataStore store);
	
	public OTDataProducer getDataProducer();
	public void setDataProducer(OTDataProducer producer);
	
	public static int DEFAULT_xColumn = 0;
	public int getXColumn();
	public void setXColumn(int xCol);
	
	public static int DEFAULT_yColumn = 1;
	public int getYColumn();
	public void setYColumn(int yCol);
	
	public static boolean DEFAULT_locked = false;
	public boolean getLocked();
	public void setLocked(boolean locked);
	
	public static float DEFAULT_lineWidth = 2.0f;
	public float getLineWidth();
	public void setLineWidth(float w);
	
	/**
	 * An OTObjectList of OTGraphSegment objects.
	 * Each OTGraphSegment describes the criteria for determining if a part of the graph is correct or not.
	 * @return
	 */
	public OTObjectList getRubric();
	
	/**
	 * A list of urls to OTOverlays which contain example graph data which can be used to debug rubrics
	 * @return
	 */
    public OTResourceList getExampleGraphOverlayUrls();
    
    public static double DEFAULT_segmentingTolerance = 4.4;
    public void setSegmentingTolerance(double tolerance);
    public double getSegmentingTolerance();
}