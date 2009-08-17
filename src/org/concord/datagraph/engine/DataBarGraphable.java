/*
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2007-09-10 17:15:18 $
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
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.Vector;

import org.concord.graph.engine.GraphArea;
import org.concord.graph.engine.MouseControllable;
import org.concord.graph.engine.MouseDrawManagerHandler;
import org.concord.graph.engine.Selectable;
import org.concord.graph.engine.SelectableList;
import org.concord.graph.engine.ToolTipHandler;
import org.concord.graph.event.SelectableListener;

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
	implements Selectable, MouseControllable, ToolTipHandler
{
	protected float barWidth;
	
	//Y position of 0 in display coordinates
	protected double yOriginDisplay = 0;
	
	protected SelectableList selectableBars;
	
	//This mouse draw manager handles the drawing of the list of bars and also
	//handles the selection of the bars with the mouse
	protected MouseDrawManagerHandler mouseDrawManager;
		
	/**
	 * Default constructor
	 */
	public DataBarGraphable()
	{
		super();
				
		selectableBars = new SelectableList();
		selectableBars.setSelectionMode(SelectableList.SELECTIONMODE_MULTIPLE);
		mouseDrawManager = new MouseDrawManagerHandler(selectableBars);
		showSampleLimit = 1;
	}
	
	/**
	 * Sets the width of each bar in DISPLAY coordinates. 
	 * Currently, all the bars are the same width
	 */
	public void setBarWidth(float width)
	{
		barWidth = width;
		super.setLineWidth(width);
	}
	
	/**
	 * Returns the width of the bar
	 * @return
	 */
	public float getBarWidth()
	{
		return barWidth;
	}
	
	/**
	 * @see org.concord.datagraph.engine.DataGraphable#setLineWidth(float)
	 */
	public void setLineWidth(float width)
	{
		//This will call super.setLineWidth()
		setBarWidth(width);

//		System.out.println("setLineWidth="+width);
	}
	
	protected void updateStroke()
	{
		stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
	}
	
	public Stroke getStroke()
	{
		return stroke;
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
		
		mouseDrawManager.draw(g);
		
		g.setColor(oldColor);
		g.setStroke(oldStroke);
		g.setClip(oldClip);		
	}
	
	public void update()
	{
		super.update();
		yOriginDisplay = graphArea.getCoordinateSystem().getOriginOffsetDisplay().getY();
		
		generateBarSelectables();
	}
	
	protected void generateBarSelectables()
	{
		//Regenerate the bars 
		selectableBars.removeAllElements();
		
		//Get an interator on the main point path to get each point already calculated
		//in display coordinates
		//Hopefully these points coincide with the points in the data store
		Point2D point = new Point2D.Double();
		PathIterator pathIter = path.getPathIterator(null);
		int i = 0;
		while (!pathIter.isDone()){
			
			//Get the point and draw the bar
			float[] coords = new float[6];
			int type = pathIter.currentSegment(coords);
			
			if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO){				
				point.setLocation(coords[0], coords[1]);
				//Add a new bar to the list
				addBar(point, i);
				//
				i++;
			}
			//
			
			pathIter.next();
		}
		//		
	}
	
	private boolean addBar(Point2D point, int index)
	{
		int totalNumBars = dataStore.getTotalNumSamples();
		if (showAllChannels)
			totalNumBars *= dataStore.getTotalNumChannels();
		if (index >= totalNumBars) return false;
		
		DataBarSelectable bar = new DataBarSelectable(this, index);
		bar.setLocationDisplay(point);
		selectableBars.add(bar);
		return true;
	}
	
	public double getYOriginDisplay()
	{
		return yOriginDisplay;
	}

	/**
	 * @see org.concord.graph.engine.Selectable#addSelectableListener(org.concord.graph.event.SelectableListener)
	 */
	public void addSelectableListener(SelectableListener l)
	{
		selectableBars.addSelectableListener(l);
	}

	/**
	 * @see org.concord.graph.engine.Selectable#deselect()
	 */
	public void deselect()
	{
		//selectableBars.deselect();
	}

	/**
	 * @see org.concord.graph.engine.Selectable#isSelected()
	 */
	public boolean isSelected()
	{
		return selectableBars.isSelected();
	}

	/**
	 * @see org.concord.graph.engine.Selectable#isSelectionEnabled()
	 */
	public boolean isSelectionEnabled()
	{
		return selectableBars.isSelectionEnabled();
	}

	/**
	 * @see org.concord.graph.engine.Selectable#removeSelectableListener(org.concord.graph.event.SelectableListener)
	 */
	public void removeSelectableListener(SelectableListener l)
	{
		selectableBars.removeSelectableListener(l);
	}

	/**
	 * @see org.concord.graph.engine.Selectable#select()
	 */
	public void select()
	{
		//System.out.println("select");
		//selectableBars.select();
	}

	/**
	 * @see org.concord.graph.engine.Selectable#setSelectionEnabled(boolean)
	 */
	public void setSelectionEnabled(boolean b)
	{
		selectableBars.setSelectionEnabled(b);
	}

	/**
	 * @see org.concord.graph.engine.DefaultGraphable#setGraphArea(org.concord.graph.engine.GraphArea)
	 */
	public void setGraphArea(GraphArea area)
	{
		super.setGraphArea(area);
		for (int i=0; i < selectableBars.size(); i++){
			DataBarSelectable bar = (DataBarSelectable)selectableBars.elementAt(i);
			bar.setGraphArea(area);
		}
	}
	
	public Vector getSelectedBars()
	{
		return selectableBars.getSelectedObjects();
	}

	/////////////////////
	//MouseControllable methods that are just delegated to the mouse manager handler
	//to handle the selection of the bars
	
	/**
	 * @see org.concord.graph.engine.MouseSensitive#isPointInProximity(java.awt.Point)
	 */
	public boolean isPointInProximity(Point p)
	{
		return mouseDrawManager.isPointInProximity(p);
	}

	/**
	 * @see org.concord.graph.engine.MouseControllable#isMouseControlled()
	 */
	public boolean isMouseControlled()
	{
		return mouseDrawManager.isMouseControlled();
	}

	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseDragged(java.awt.Point)
	 */
	public boolean mouseDragged(Point p)
	{
		return mouseDrawManager.mouseDragged(p);
	}

	/**
	 * @see org.concord.graph.engine.MouseControllable#mousePressed(java.awt.Point)
	 */
	public boolean mousePressed(Point p)
	{
		return mouseDrawManager.mousePressed(p);
	}

	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseReleased(java.awt.Point)
	 */
	public boolean mouseReleased(Point p)
	{
		return mouseDrawManager.mouseReleased(p);
	}

	/**
	 * @param i
	 */
	public void setMaxBarsCanBeSelected(int num)
	{
		selectableBars.setMaxNumberCanBeSelected(num);
	}

	/**
	 * @see org.concord.graph.engine.ToolTipHandler#handleToolTip(java.awt.Point)
	 */
	public String handleToolTip(Point p)
	{
		return mouseDrawManager.handleToolTip(p);
	}
	
	/////////////////////
}
