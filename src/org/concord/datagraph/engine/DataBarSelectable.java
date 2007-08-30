/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-08-30 18:14:52 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.concord.graph.engine.DefaultSelectable;
import org.concord.graph.engine.Graphable;
import org.concord.graph.engine.ToolTipHandler;


/**
 * DataBarSelectable
 * Class used to represent a single selectable bar
 * inside of a bar graph
 *
 * Date created: Aug 24, 2007
 *
 * @author imoncada<p>
 *
 */
public class DataBarSelectable extends DefaultSelectable
	implements ToolTipHandler
{
	protected DataBarGraphable dataGraphable;
	protected int dataIndex;
	
	protected Point2D displayPoint;

	protected Line2D barLine;
	protected Rectangle2D barRect;

	protected double barWidth = Double.NaN;
	protected Color barColor = null;
	
	/**
	 * Constructor
	 * @param ds
	 * @param index
	 */
	public DataBarSelectable(DataBarGraphable dg, int index)
	{
		dataGraphable = dg;
		dataIndex = index;
		
		//Inherit the graph area from the parent
		setGraphArea(dg.getGraphArea());

		displayPoint = new Point2D.Double();
		barLine = new Line2D.Double();
		barRect = new Rectangle2D.Double();
	}
	
	/**
	 * It's the DataBarGraphable's responsibility to keep this
	 * value updated. The Bar should always know it's display point.
	 * @param point
	 */
	public void setLocationDisplay(Point2D point)
	{
		displayPoint.setLocation(point);
	}
	
	/**
	 * Returns the x,y location of the data point that is representing
	 * (top of the bar if it's a positive value, bottom of the bar if it's negative)
	 * 
	 * @return
	 */
	public Point2D getLocationWorld()
	{
		Float xValue = (Float)dataGraphable.getValueAt(dataIndex, 0);
		Float yValue = (Float)dataGraphable.getValueAt(dataIndex, 1);
		
		return new Point2D.Double(xValue.doubleValue(), yValue.doubleValue());
	}
	
	public Point2D getLocationDisplay()
	{
		return displayPoint;
	}
	
	public double getBarWidth()
	{
		if (!Double.isNaN(barWidth)) return barWidth;
		return dataGraphable.getBarWidth();
	}
	
	public Color getBarColor()
	{
		if (barColor != null) return barColor;
		return dataGraphable.getColor();
	}
	
	/**
	 * @see org.concord.graph.engine.MouseSensitive#isPointInProximity(java.awt.Point)
	 */
	public boolean isPointInProximity(Point p)
	{		
		Point2D locDisplay = getLocationDisplay();
		
		//Check x
		double w = getBarWidth()/2;
		if (!(p.getX() >= locDisplay.getX() - w && 
				p.getX() <= locDisplay.getX() + w)){
			return false;
		}
		
		//Check y
		double yMin = locDisplay.getY();
		double yMax = getYOriginDisplay();
		if (yMin > yMax){
			double t = yMin;
			yMin = yMax;
			yMax = t;
		}
		
//		System.out.println("is point in prox "+dataIndex+ " "+locDisplay+" "+p+" "+yMin+" "+yMax);
		
		if (p.getY() >= yMin && p.getY() <= yMax){
			//System.out.println("true "+dataIndex);
			return true;
		}
		
		return false;
	}

	/**
	 * @return
	 */
	private double getYOriginDisplay()
	{
		return dataGraphable.getYOriginDisplay();
	}

	/**
	 * @see org.concord.graph.engine.Graphable#getCopy()
	 */
	public Graphable getCopy()
	{
		throw new UnsupportedOperationException("getCopy() not implemented");
	}

	/**
	 * @see org.concord.graph.engine.Drawable#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g)
	{
		//System.out.println("draw "+dataIndex);
		
		g.setColor(getBarColor());
		g.setStroke(dataGraphable.getStroke());
		
		//Draw a line going from (x, y) to (x, 0)  0 in world coordinates
		barLine.setLine(displayPoint.getX(), displayPoint.getY(), displayPoint.getX(), getYOriginDisplay());
		g.draw(barLine);
		
		if (isSelected()){
			g.setStroke(new BasicStroke(3.0f));
		}
		else{
			g.setStroke(new BasicStroke(1.0f));
		}
		
		g.setColor(Color.black);
		barRect.setRect(displayPoint.getX() - getBarWidth()/2, 
				displayPoint.getY(),
				getBarWidth(), 
				Math.abs(displayPoint.getY() - getYOriginDisplay()));
		g.draw(barRect);
		
		if (isSelected()){
			g.setColor(new Color(0,0,0,64));
			g.fill(barRect);
		}
	}

	/**
	 * @see org.concord.graph.engine.DefaultSelectable#select()
	 */
	public void select()
	{
		System.out.println("select "+dataIndex);
		super.select();
	}

	/* (non-Javadoc)
	 * @see org.concord.graph.engine.ToolTipHandler#handleToolTip(java.awt.Point)
	 */
	public String handleToolTip(Point pt)
	{
		Point2D loc = getLocationWorld();
		String strTmp = ""+(int)loc.getX()+". Value: "+loc.getY();
		return strTmp;
	}
}
