/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-08-17 18:32:47 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;


/**
 * DataBarGraphable
 * This is a special data graphable that draws vertical bars 
 * underneath each x,y point, instead of just points
 *
 * Date created: Aug 17, 2007
 *
 * @author imoncada<p>
 *
 */
public class DataBarGraphable extends DataGraphable
{
	protected float barWidth;
	protected Line2D barLine;
	
	//Y position of 0 in display coordinates
	protected double yOriginDisplay = 0;
	
	/**
	 * Default constructor
	 */
	public DataBarGraphable()
	{
		super();
		
		barLine = new Line2D.Double();
	}
	
	/**
	 * Sets the width of each bar in DISPLAY coordinates. 
	 * Currently, all the bars are the same width
	 */
	public void setBarWidth(float width)
	{
		barWidth = width;
		setLineWidth(width);
	}
	
	protected void updateStroke()
	{
		stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
	}
	
	/**
	 *  Draws this object on Graphics g 
	 *  Basically copied from DataGraphable but instead
	 *  of drawing the path, it's looping through the path 
	 *  to draw a bar in each point
	 **/
	public void draw(Graphics2D g)
	{
		if (needUpdate){
			update();
		}
		
		Shape oldClip = g.getClip();
		Color oldColor = g.getColor();
		Stroke oldStroke = g.getStroke();
		
		graphArea.clipGraphics(g);
		g.setColor(lineColor);
		g.setStroke(stroke);
		
		//Get an interator on the main point path to get each point already calculated
		//in display coordinates
		Point2D point = new Point2D.Double();
		PathIterator pathIter = path.getPathIterator(null);
		while (!pathIter.isDone()){
			
			//Get the point and draw the bar
			float[] coords = new float[6];
			int type = pathIter.currentSegment(coords);
			
			if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO){
				point.setLocation(coords[0], coords[1]);
				drawBar(g, point);
			}
			//
			
			pathIter.next();
		}
		
		g.setColor(oldColor);
		g.setStroke(oldStroke);
		g.setClip(oldClip);		
	}
	
	public void update()
	{
		super.update();
		yOriginDisplay = graphArea.getCoordinateSystem().getOriginOffsetDisplay().getY();
	}

	/**
	 * @param point
	 */
	protected void drawBar(Graphics2D g, Point2D point)
	{
		//Draw a line going from (x, y) to (x, 0)  0 in world coordinates
		barLine.setLine(point.getX(), point.getY(), point.getX(), yOriginDisplay);
		g.draw(barLine);
	}

}
