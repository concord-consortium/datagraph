
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
 * $Revision: 1.3 $
 * $Date: 2005-03-02 07:30:57 $
 * $Author: maven $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import javax.swing.event.ChangeEvent;

import org.concord.graph.engine.CoordinateSystem;
import org.concord.graph.engine.DefaultControllable;
import org.concord.graph.engine.GraphArea;
import org.concord.graph.engine.Graphable;
import org.concord.graph.examples.BoxTextLabel;


/**
 * DataPointLabel
 * Class name and description
 *
 * Date created: Mar 1, 2005
 *
 * @author imoncada<p>
 *
 */
public class DataPointLabel extends BoxTextLabel
{
	//Temporary x,y point to link
	protected Point2D dataPoint;
	
	protected Point2D displayDataPoint;
	
	private boolean dataPointChanged = true;
	
	private boolean mouseInsideDataPoint = false;
	
	protected Point2D originalDataPoint;
	
	/**
	 * 
	 */
	public DataPointLabel()
	{
		this("Message");
	}
	
	/**
	 * 
	 */
	public DataPointLabel(String msg)
	{
		super(msg);
		dataPoint = new Point2D.Double(1,1);
		
		displayDataPoint = new Point2D.Double();
		originalDataPoint = new Point2D.Double();
	}
	
	/**
	 * @see org.concord.graph.engine.Graphable#update()
	 */
	public void update()
	{
		super.update();
		if (dataPointChanged){
			CoordinateSystem cs = graphArea.getCoordinateSystem();
			displayDataPoint = cs.transformToDisplay(dataPoint);
			dataPointChanged = false;
		}
	}
	/**
	 * @see org.concord.graph.engine.Drawable#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g)
	{
		if (needUpdate){
			update();
			needUpdate = false;
		}
		
		Color oldColor = g.getColor();
		Stroke oldStroke = g.getStroke();
		Shape oldClip = g.getClip();
		Font oldFont = g.getFont();
		
		graphArea.clipGraphics(g);
		
		drawDataPointLink(g);

		super.draw(g);
		
		drawDataPoint(g);
		
		g.setColor(oldColor);
		g.setStroke(oldStroke);
		g.setClip(oldClip);
		g.setFont(oldFont);
	}

	/**
	 * 
	 */
	protected void drawDataPoint(Graphics2D g)
	{
		if (isSelected()){
			if (mouseInsideDataPoint){
				g.setColor(Color.red);
			}
			else{
				g.setColor(foreColor);
			}
			g.setStroke(getStroke());
			
			g.drawLine((int)displayDataPoint.getX() - 3, (int)displayDataPoint.getY() - 3,
					(int)displayDataPoint.getX() + 3, (int)displayDataPoint.getY() + 3);
			g.drawLine((int)displayDataPoint.getX() - 3, (int)displayDataPoint.getY() + 3,
					(int)displayDataPoint.getX() + 3, (int)displayDataPoint.getY() - 3);
		}
	}

	/**
	 * @param g
	 */
	protected void drawDataPointLink(Graphics2D g)
	{
		g.setColor(foreColor);
		g.setStroke(getStroke());
		
		g.drawLine((int)displayDataPoint.getX(), (int)displayDataPoint.getY(),
				(int)(displayPositionFin.getX() + displayPositionIni.getX())/2, 
				(int)(displayPositionFin.getY() + displayPositionIni.getY())/2);
	}
	
	/**
	 * @return Returns the dataPoint.
	 */
	public Point2D getDataPoint()
	{
		return dataPoint;
	}
	
	/**
	 * @param dataPoint The dataPoint to set.
	 */
	public void setDataPoint(Point2D dataPoint)
	{
		this.dataPoint = dataPoint;
		dataPointChanged = true;
		needUpdate = true;
		notifyChange();
	}
	
	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e)
	{
		dataPointChanged = true;
		super.stateChanged(e);
	}
	
	
	/**
	 * @see org.concord.graph.engine.MouseMotionReceiver#mouseMoved(java.awt.Point)
	 */
	public boolean mouseMoved(Point location)
	{
		if (isPointInDataPoint(location)){
			if (!mouseInsideDataPoint){
				mouseInsideDataPoint = true;
				notifyChange();
			}
		}
		else{
			if (mouseInsideDataPoint){
				mouseInsideDataPoint = false;
				notifyChange();
			}
		}
		return super.mouseMoved(location);
	}

	/**
	 * @param location
	 * @return
	 */
	private boolean isPointInDataPoint(Point p)
	{
		if (p.getX() >= displayDataPoint.getX() - 3 && 
				p.getX() <= displayDataPoint.getX() + 3 &&
				p.getY() >= displayDataPoint.getY() - 3 && 
				p.getY() <= displayDataPoint.getY() + 3){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * @see org.concord.graph.engine.MouseSensitive#isPointInProximity(java.awt.Point)
	 */
	public boolean isPointInProximity(Point location)
	{
		if (isSelected()){
			if (isPointInDataPoint(location)){
				mouseInsideDataPoint = true;
				return true;
			}
		}
		return super.isPointInProximity(location);
	}
	
	/**
	 * @see org.concord.graph.engine.MouseMotionReceiver#mouseExited(java.awt.Point)
	 */
	public boolean mouseExited(Point location)
	{
		mouseInsideDataPoint = false;
		return super.mouseExited(location);
	}
	
	/**
	 * @see org.concord.graph.engine.MouseControllable#mousePressed(java.awt.Point)
	 */
	public boolean mousePressed(Point p)
	{
		if (super.mousePressed(p)){
			return true;
		}
		
		//Store my original data point
		originalDataPoint.setLocation(this.dataPoint);
		
		return true;
	}
	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseDragged(java.awt.Point)
	 */
	public boolean mouseDragged(Point p)
	{
		if (!mouseInsideDataPoint){
			return super.mouseDragged(p);
		}
		
		Point2D p2 = graphArea.getCoordinateSystem().transformToWorld(p);
		
		Point2D newP = new Point2D.Double(originalDataPoint.getX() + (p2.getX() - clickPointWorld.getX()),
				originalDataPoint.getY() + (p2.getY() - clickPointWorld.getY()));
		
		setDataPoint(newP);
		
		return true;
	}
}
