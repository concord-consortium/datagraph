/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-10-27 23:24:04 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.concord.framework.data.stream.DataProducer;
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
	boolean mouseClicked = false;
	
	int indexPointClicked = -1;
	
	/**
	 * 
	 */
	public ControllableDataGraphable()
	{
		super();
	}

	/**
	 * @param source
	 */
	public ControllableDataGraphable(DataProducer source)
	{
		super(source);
	}

	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseControllable#mousePressed(java.awt.Point)
	 */
	public boolean mousePressed(Point p)
	{
		mouseClicked = true;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.concord.graph.engine.MouseControllable#mouseDragged(java.awt.Point)
	 */
	public boolean mouseDragged(Point p)
	{
		Point2D pW;
		
		if (indexPointClicked == -1) return false;
		if (graphArea == null) return false;
		CoordinateSystem cs = graphArea.getCoordinateSystem();
		
		pW = cs.transformToWorld(p);
		
		setValueAt(indexPointClicked, 0, new Float(pW.getX()));
		setValueAt(indexPointClicked, 1, new Float(pW.getY()));
		
		return false;
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
		float x, y;
		Point2D pW, pD;
		int threshold = 5;
		Object objVal;
		
		indexPointClicked = -1;
		
		if (graphArea == null) return false;		
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

}
