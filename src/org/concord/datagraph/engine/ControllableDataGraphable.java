

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
 * $Date: 2005-03-08 08:54:55 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.concord.data.stream.PointsDataStore;
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

		
	/**
	 * 
	 */
	public ControllableDataGraphable()
	{
		super();
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
				
				//XXX FIXME TEST!!!
				if ((dataStore instanceof PointsDataStore)) {
					//System.out.println("pressed "+(float)pW.getX());
					((PointsDataStore)dataStore).addPointOrder((float)pW.getX(), (float)pW.getY());
					startDragX = (float)pW.getX();
					startDragY = (float)pW.getY();
				}
			}
		}
		else if (dragMode == DRAGMODE_REMOVEPOINTS){
			//Remove the current point
			removeValueAt(indexPointClicked);
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseControllable#mouseDragged(java.awt.Point)
	 */
	public boolean mouseDragged(Point p)
	{
		System.out.println("dragged "+p);
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
				
				//XXX FIXME TEST!!!
				if ((dataStore instanceof PointsDataStore)) {
					//if (startDragX != (float)pW.getX() && startDragY != (float)pW.getY()){
						//System.out.println("dragged "+(float)pW.getX());
						//((PointsDataStore)dataStore).addPointOrder((float)pW.getX(), (float)pW.getY());
						((PointsDataStore)dataStore).addPointOrderFromTo((float)pW.getX(), (float)pW.getY(), startDragX);
						startDragX = (float)pW.getX();
						startDragY = (float)pW.getY();
					//}
				}
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
}
