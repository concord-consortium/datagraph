/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2004-10-28 18:57:41 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.awt.Point;
import java.awt.geom.Point2D;

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
	
	private boolean mouseClicked = false;
	private int indexPointClicked = -1;
		
	/**
	 * 
	 */
	public ControllableDataGraphable()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseControllable#mousePressed(java.awt.Point)
	 */
	public boolean mousePressed(Point p)
	{
		Point2D pW;
		
		mouseClicked = true;

		if (indexPointClicked == -1 &&
				(dragMode == DRAGMODE_MOVEPOINTS || dragMode == DRAGMODE_REMOVEPOINTS)) return false;
		
		if (graphArea == null) return false;
		CoordinateSystem cs = graphArea.getCoordinateSystem();
		
		pW = cs.transformToWorld(p);
		
		if (dragMode == DRAGMODE_ADDPOINTS || dragMode == DRAGMODE_ADDMULTIPLEPOINTS){
			//Add a new point
			addPoint(pW.getX(), pW.getY());
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
		Point2D pW;
		
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
			addPoint(pW.getX(), pW.getY());
		}
		
		return false;
	}

	public void addPoint(double x, double y)
	{
		int newPointIndex = getTotalNumSamples();
		setValueAt(newPointIndex, 0, new Float(x));
		setValueAt(newPointIndex, 1, new Float(y));
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseControllable#mouseReleased(java.awt.Point)
	 */
	public boolean mouseReleased(Point p)
	{
		indexPointClicked = -1;
		mouseClicked = false;
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
		Point2D pW, pD;
		float x, y;
		int threshold = 5;
		Object objVal;
		
		CoordinateSystem cs = graphArea.getCoordinateSystem();
		
		for (int i=0; i<getTotalNumSamples(); i++){
			
			objVal = getValueAt(i, 0);
			if (!(objVal instanceof Float)) continue;
			x = ((Float)objVal).floatValue();
			
			objVal = getValueAt(i, 1);
			if (!(objVal instanceof Float)) continue;
			y = ((Float)objVal).floatValue();
			
			pW = new Point2D.Double(x,y);
			pD = cs.transformToDisplay(pW);
			
			//Threshold
			if (Math.abs(pD.getX() - p.getX()) <= threshold &&
					Math.abs(pD.getY() - p.getY()) <= threshold){
				indexPointClicked = i;
				return true;
			}
		}
		
		return false;
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
