

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
 * $Revision: 1.9 $
 * $Date: 2005-03-10 06:05:37 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.concord.data.stream.DataStoreFunctionUtil;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.graph.engine.CoordinateSystem;
import org.concord.graph.engine.MouseControllable;


/**
 * ControllableDataGraphable
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author imoncada<p>
 *
 */
public class ControllableDataGraphable extends DataGraphable
	implements MouseControllable
{
	public final static int DRAGMODE_NONE = 0;
	public final static int DRAGMODE_MOVEPOINTS = 1;
	public final static int DRAGMODE_ADDPOINTS = 2;
	public final static int DRAGMODE_ADDMULTIPLEPOINTS = 3;
	public final static int DRAGMODE_REMOVEPOINTS = 4;
	
	protected int dragMode = DRAGMODE_MOVEPOINTS;
	
	public final static int LINETYPE_FREE = 0;
	public final static int LINETYPE_FUNCTION = 1;
	
	protected int lineType = LINETYPE_FUNCTION;//LINETYPE_FREE;	
	
	private boolean mouseClicked = false;
	private int indexPointClicked = -1;
	
	private float startDragX = Float.NaN;
	private float startDragY = Float.NaN;
	private DataStoreFunctionUtil functionUtil;
		
	/**
	 * 
	 */
	public ControllableDataGraphable()
	{
		super();
		functionUtil = new DataStoreFunctionUtil();
	}
	
	/**
	 * @see org.concord.datagraph.engine.DataGraphable#setDataStore(org.concord.framework.data.stream.DataStore)
	 */
	public void setDataStore(DataStore dataStore)
	{
		//This Data Graphable only makes sense with a Writable Data Store!
		if (!(dataStore instanceof WritableDataStore)) {
			throw new IllegalArgumentException("The Data Store "+dataStore+" is not Writable!");
		}
		
		super.setDataStore(dataStore);
		functionUtil.setDataStore((WritableDataStore)dataStore);
	}
	
	/**
	 * @see org.concord.datagraph.engine.DataGraphable#setChannelX(int)
	 */
	public void setChannelX(int channelX)
	{
		super.setChannelX(channelX);
		functionUtil.setXChannel(channelX);
	}
	
	/**
	 * @see org.concord.graph.engine.MouseControllable#mousePressed(java.awt.Point)
	 */
	public boolean mousePressed(Point p)
	{
		Point2D pW;
		
		mouseClicked = true;

		if (dragMode == DRAGMODE_NONE) return false;
		
		if (indexPointClicked == -1 &&
				(dragMode == DRAGMODE_MOVEPOINTS || dragMode == DRAGMODE_REMOVEPOINTS)) return false;
		
		if (graphArea == null) return false;
		CoordinateSystem cs = graphArea.getCoordinateSystem();
		
		pW = cs.transformToWorld(p);
		
		if (dragMode == DRAGMODE_ADDPOINTS || dragMode == DRAGMODE_ADDMULTIPLEPOINTS){
			//Add a new point
			if (lineType == LINETYPE_FREE){
				addPoint(pW.getX(), pW.getY());
			}
			else if (lineType == LINETYPE_FUNCTION){
				
				addPointOrder((float)pW.getX(), (float)pW.getY());
				startDragX = (float)pW.getX();
				startDragY = (float)pW.getY();
			}
		}
		else if (dragMode == DRAGMODE_REMOVEPOINTS){
			//Remove the current point
			removeSampleAt(indexPointClicked);
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseControllable#mouseDragged(java.awt.Point)
	 */
	public boolean mouseDragged(Point p)
	{
		//System.out.println("dragged "+p);
		Point2D pW;
		
		if (dragMode == DRAGMODE_NONE) return false;
		
		if (indexPointClicked == -1 &&
				(dragMode == DRAGMODE_MOVEPOINTS || dragMode == DRAGMODE_REMOVEPOINTS)) return false;
		
		if (graphArea == null) return false;
		CoordinateSystem cs = graphArea.getCoordinateSystem();
		
		pW = cs.transformToWorld(p);
		
		if (dragMode == DRAGMODE_MOVEPOINTS){
			//Drag the current point
			setValueAt(indexPointClicked, 0, new Float(pW.getX()));
			setValueAt(indexPointClicked, 1, new Float(pW.getY()));
		}
		else if (dragMode == DRAGMODE_ADDMULTIPLEPOINTS){
			//Add a new point
			if (lineType == LINETYPE_FREE){
				addPoint(pW.getX(), pW.getY());
			}
			else if (lineType == LINETYPE_FUNCTION){
				
				addPointOrderFromTo((float)pW.getX(), (float)pW.getY(), startDragX);
				startDragX = (float)pW.getX();
				startDragY = (float)pW.getY();
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseControllable#mouseReleased(java.awt.Point)
	 */
	public boolean mouseReleased(Point p)
	{
		indexPointClicked = -1;
		mouseClicked = false;
		startDragX = Float.NaN;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseControllable#isMouseControlled()
	 */
	public boolean isMouseControlled()
	{
		return mouseClicked;
	}

	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseSensitive#isPointInProximity(java.awt.Point)
	 */
	public boolean isPointInProximity(Point p)
	{		
		indexPointClicked = -1;
		
		if (graphArea == null) return false;
		
		if (dragMode == DRAGMODE_NONE) return false;
		
		if (dragMode == DRAGMODE_MOVEPOINTS || dragMode == DRAGMODE_REMOVEPOINTS){
			return isPointAValue(p);
		}
		if (dragMode == DRAGMODE_ADDPOINTS || dragMode == DRAGMODE_ADDMULTIPLEPOINTS){
			return true;
		}
		
		return false;
	}

	/**
	 * @param p
	 * @return
	 */
	private boolean isPointAValue(Point p)
	{
		indexPointClicked = getIndexValueAtDisplay(p, 5);
		if (indexPointClicked == -1){
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * @return Returns the dragMode.
	 */
	public int getDragMode()
	{
		return dragMode;
	}
	
	/**
	 * @param dragMode The dragMode to set.
	 */
	public void setDragMode(int dragMode)
	{
		this.dragMode = dragMode;
	}
	
	/* Function stuff */

	/**
	 * @param f
	 * @param g
	 */
	private void addPointOrder(float x, float y)
	{
		float value;
		int i = functionUtil.findSamplePositionValue(x);
		value = functionUtil.getXValueAt(i);
		
		addPointOrder(x, y, i, value);
	}

	/**
	 * @param x
	 * @param i
	 * @param value
	 */
	private void addPointOrder(float x, float y, int i, float value)
	{
		//float v = functionUtil.getXValueAt(i);
		//if (v != value){
		//	System.err.println("values different: "+value+" "+v);
		//}
		if (i < getTotalNumSamples()){
			if (value != x){
				insertSampleAt(i);
			}
			for (int j=0; j < getTotalNumChannels(); j++){
				if (j == channelX){
					setValueAt(i, j, new Float(x));
					//System.out.println("addPointOrder set "+getFloatVectorStr(channel));
				}
				else if (j == channelY){
					setValueAt(i, j, new Float(y));
				}
				else{
					setValueAt(i, j, null);
				}				
			}
			//notifyDataChanged();
		}
		else{
			addPoint(x,y);
		}
	}
	
	public void addPointOrderFromTo(float x, float y, float otherX)
	{
		if (otherX <= x){
			addPointOrderFrom(x, y, otherX);
		}
		else{
			addPointOrderTo(x, y, otherX);
		}
	}
	
	public void addPointOrderFrom(float x, float y, float startX)
	{
		float value;
		int i;
		int startI;
		int t;
		int ti;
		
		if (x < startX){
			return;
		}
		if (x == startX){
			addPointOrder(x, y);
			return;
		}
		
		i = functionUtil.findSamplePositionValue(x);
		t = getTotalNumSamples();
		startI = functionUtil.findSamplePositionValue(startX);
		value = functionUtil.getXValueAt(i);
		
		//System.out.println("addPointOrderFrom("+x+" "+i+" "+startX+" "+startI+") in "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		//Removing all values from startI to i
		ti = Math.min(i, t);
		for (int ii = startI+1; ii < ti; ii++){
			//for (int j=0; j < channelsValues.size(); j++){
			//	Vector channel = (Vector)channelsValues.elementAt(j);
			//	channel.remove(startI+1);
			//}
			//The only problem with doing this is that removeSampleAt fires a removed event
			removeSampleAt(startI+1);
			i--;
		}
		//i = i - (ti - (startI +1));
		
		//System.out.println("after removing, i:"+i+" "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		addPointOrder(x, y, i, value);
		
	}
	
	public void addPointOrderTo(float x, float y, float endX)
	{
		float value;
		int i;
		int endI;
		int t;
		int ti;
		
		if (x > endX){
			return;
		}
		if (x == endX){
			addPointOrder(x, y);
			return;
		}
		
		i = functionUtil.findSamplePositionValue(x);
		t = getTotalNumSamples();
		endI = functionUtil.findSamplePositionValue(endX);
		value = functionUtil.getXValueAt(i);
		
		//System.out.println("addPointOrderFrom("+x+" "+i+" "+endX+" "+endI+") in "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		//Removing all values from i to endI
		ti = Math.min(endI, t);
		for (int ii = i; ii < ti; ii++){
			removeSampleAt(i);
		}
		
		//System.out.println("after removing, i:"+i+" "+getFloatVectorStr((Vector)channelsValues.elementAt(0)));
		
		addPointOrder(x, y, i, value);
	}
	
}
