
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
 * $Revision: 1.8 $
 * $Date: 2005-03-09 17:14:12 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.graph.engine.CoordinateSystem;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.util.ui.PointTextLabel;


/**
 * DataPointLabel
 * Class name and description
 *
 * Date created: Mar 1, 2005
 *
 * @author imoncada<p>
 *
 */
public class DataPointLabel extends PointTextLabel
{
	private GraphableList objList;
	private int indexPointOver = -1;
	private DataGraphable graphableOver = null;
	
	/**
	 * 
	 */
	public DataPointLabel()
	{
		this("Message");
	}
	
	public DataPointLabel(boolean newNote)
	{
		this();
		this.newNote = newNote;
	}
	
	/**
	 * 
	 */
	public DataPointLabel(String msg)
	{
		super(msg);
	}
	
	/**
	 * @param gList The GraphableList to set.
	 */
	public void setGraphableList(GraphableList gList)
	{
		this.objList = gList;
	}
	
	/**
	 * @see org.concord.graph.engine.MouseMotionReceiver#mouseMoved(java.awt.Point)
	 */
	public boolean mouseMoved(Point p)
	{
		if (newNote){
			findAvailablePointOver(p);
		}
		return super.mouseMoved(p);
	}
	
	/**
	 * 
	 */
	private void findAvailablePointOver(Point p)
	{
		if (objList != null){
			//Look for a point in one of the graphables in the list
			int index = -1;
			DataGraphable dg = null;
			for (int i=0; i<objList.size(); i++){
				Object obj = objList.elementAt(i);
				if (obj instanceof DataGraphable){
					dg = (DataGraphable)obj;
					index = dg.getIndexValueAtDisplay(p, 10);
					if (index != -1) break;
				}
			}
			
			if (index != -1){
				//Found a point!
				//System.out.println("Found a point!!!");
				if (index != indexPointOver || dg != graphableOver){
					indexPointOver = index;
					graphableOver = dg;
					notifyChange();
				}
			}
			else{
				//System.out.println("No point!");
				if (index != indexPointOver || graphableOver != null){
					indexPointOver = index;
					graphableOver = null;
					notifyChange();
				}
			}
		}
	}
	
	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseDragged(java.awt.Point)
	 */
	public boolean mouseDragged(Point p)
	{
		if (mouseInsideDataPoint){
			findAvailablePointOver(p);
		}
		return super.mouseDragged(p);
	}
	
	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseReleased(java.awt.Point)
	 */
	public boolean mouseReleased(Point p)
	{
		if (dragEnabled){
			if (indexPointOver != -1 && graphableOver != null){
				Point2D pW = getPointDataGraphable(graphableOver, indexPointOver);
				setDataPoint(pW);
			}
			else{
				restoreOriginalDataPoint();
			}
		}
		indexPointOver = -1;
		graphableOver = null;
		return super.mouseReleased(p);
	}
	
	/**
	 * 
	 */
	private void restoreOriginalDataPoint()
	{
		if (dataPoint != null){
			setDataPoint(originalDataPoint);
		}
	}

	/**
	 * @see org.concord.graph.util.ui.BoxTextLabel#addAtPoint(java.awt.Point)
	 */
	protected boolean addAtPoint(Point2D pD, Point2D pW)
	{
		if (indexPointOver != -1 && graphableOver != null){
			Point2D p = getPointDataGraphable(graphableOver, indexPointOver);
			return super.addAtPoint(null, p);
		}
		else{
			//super.addAtPoint(pD, pW);
			return false;
		}
	}
	
	/**
	 * @param graphableOver2
	 * @param indexPointOver2
	 * @return
	 */
	private static Point2D getPointDataGraphable(DataGraphable dg, int index)
	{
		Object objVal;
		double x,y;
		
		objVal = dg.getValueAt(index, 0);
		if (!(objVal instanceof Float)) return null;
		x = ((Float)objVal).floatValue();
		
		objVal = dg.getValueAt(index, 1);
		if (!(objVal instanceof Float)) return null;
		y = ((Float)objVal).floatValue();
		
		return new Point2D.Double(x, y);
	}

	/**
	 * @see org.concord.graph.engine.Drawable#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g)
	{
		if (newNote || mouseInsideDataPoint){
			if (indexPointOver != -1 && graphableOver != null){
				
				//System.out.println("painting an oval");
				
				Point2D p = getPointDataGraphable(graphableOver, indexPointOver);
				CoordinateSystem cs = graphArea.getCoordinateSystem();
				Point2D pD = cs.transformToDisplay(p);

				if (p != null){
					//System.out.println("painting an oval 2");
					g.drawOval((int)pD.getX() - 7, (int)pD.getY() - 7, 13, 13);
				}
			}
		}
		super.draw(g);
	}
}
