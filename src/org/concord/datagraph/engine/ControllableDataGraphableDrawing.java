/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-03-21 08:18:01 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.graph.engine.CoordinateSystem;
import org.concord.graph.engine.Graphable;
import org.concord.graph.engine.Selectable;
import org.concord.graph.event.GraphableListener;
import org.concord.graph.event.SelectableListener;
import org.concord.graph.util.engine.DrawingObject;


/**
 * ControllableDataGraphableDrawing
 * Class name and description
 *
 * Date created: Mar 19, 2005
 *
 * @author imoncada<p>
 *
 */
public class ControllableDataGraphableDrawing extends ControllableDataGraphable
	implements Selectable
{
	protected boolean selected = false;
	protected boolean selectable = true;
	protected Vector selectableListeners;
	
	private Rectangle2D boundingBox;
	
	private boolean clickOnBoundingBox = false;
	
	/**
	 * 
	 */
	public ControllableDataGraphableDrawing()
	{
		super();
		selectableListeners = new Vector();
		boundingBox = new Rectangle2D.Double();
	}

	/**
	 * @see org.concord.graph.engine.Drawable#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g)
	{
		super.draw(g);

		if (isSelected()){
			//draw a bounding box around the graphable
			drawBoundingBox(g);
		}
	}

	/**
	 * @param g
	 */
	protected void drawBoundingBox(Graphics2D g)
	{
		//draw a bounding box around the graphable
		g.draw(boundingBox);
	}

	/**
	 * @see org.concord.graph.engine.Graphable#update()
	 */
	public void update()
	{
		super.update();
		updateBoundingBox();
	}
	
	/**
	 * @param g
	 */
	protected void updateBoundingBox()
	{
		//System.out.println("bounding box update");
		
		CoordinateSystem cs = graphArea.getCoordinateSystem();
		
		Point2D p1 = new Point2D.Double(getMinXValue(), getMaxYValue());
		Point2D p2 = new Point2D.Double(getMaxXValue(), getMinYValue());
		
		Point2D p1W = cs.transformToDisplay(p1);
		Point2D p2W = cs.transformToDisplay(p2);
		
		boundingBox.setRect(p1W.getX(), p1W.getY(), 
				(p2W.getX() - p1W.getX()), (p2W.getY() - p1W.getY()));
	}
	
	/**
	 * @see org.concord.graph.util.engine.DrawingObject#setDrawingDragMode(int)
	 */
	public boolean setDrawingDragMode(int mode)
	{
		try{
			if (super.setDrawingDragMode(mode)){
				return true;
			}
		}
		catch (Exception ex) {}
		
		if (mode == DrawingObject.DRAWING_DRAG_MODE_SELECT ||
				mode == DrawingObject.DRAWING_DRAG_MODE_MOVE){

			setDragMode(mode);
			return true;
		}
		return true;
	}
	
	/**
	 * @see org.concord.graph.engine.MouseSensitive#isPointInProximity(java.awt.Point)
	 */
	public boolean isPointInProximity(Point p)
	{
		if (super.isPointInProximity(p)){
			return true;
		}
		
		//System.out.println("drag mode: "+dragMode);
		if (dragMode == DrawingObject.DRAWING_DRAG_MODE_SELECT){
			if (isPointAValue(p)){
				return true;
			}
		}
		else if (dragMode == DrawingObject.DRAWING_DRAG_MODE_MOVE){
			if (isSelected()){
				//return isPointInBoundingBox(p);
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * @param p
	 * @return
	 */
	protected boolean isPointInBoundingBox(Point p)
	{
		return boundingBox.contains(p);
	}

	/**
	 * @see org.concord.graph.engine.MouseControllable#mousePressed(java.awt.Point)
	 */
	public boolean mousePressed(Point p)
	{
		boolean b = super.mousePressed(p);
		if (dragMode == DrawingObject.DRAWING_DRAG_MODE_MOVE){			
			clickOnBoundingBox = isPointInBoundingBox(p);
		}		
		return b;
	}
	
	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseReleased(java.awt.Point)
	 */
	public boolean mouseReleased(Point p)
	{
		clickOnBoundingBox = false;
		return super.mouseReleased(p);
	}
	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseDragged(java.awt.Point)
	 */
	public boolean mouseDragged(Point p)
	{
		double dx = 0;
		double dy = 0;
				
		if (lastPointW != null){
			dx = lastPointW.getX();
		}
		if (lastPointW != null){
			dy = lastPointW.getY();
		}
		
		if (super.mouseDragged(p)){
			return true;
		}
		
		if (dragMode == DrawingObject.DRAWING_DRAG_MODE_MOVE){
			//System.out.println("mouse dragged");
			
			if (!clickOnBoundingBox) return false;
			
			CoordinateSystem cs = graphArea.getCoordinateSystem();
			Point2D pW = cs.transformToWorld(p);

			dx = pW.getX() - dx;
			dy = pW.getY() - dy;
			
			//System.out.println("move  " + dx + "," + dy);
			
			move(dx, dy);
			//lastPointW
			
		}
		
		return false;
	}
	
	/**
	 * @param dx
	 * @param dy
	 */
	public void move(double dx, double dy)
	{
		float x, y;
		
		for (int i=0; i<getTotalNumSamples(); i++){
			x = handleValue(getValueAt(i, 0)) + (float)dx;
			y = handleValue(getValueAt(i, 1)) + (float)dy;
			
			setValueAt(i, 0, new Float(x));
			setValueAt(i, 1, new Float(y));
		}
	}

	/**
	 * Selects this object. 
	 * This also fires an objectSelected(obj) event.
	 */
	public void select()
	{
		if (!selected){
			selected = true;
			notifySelect();
		}
	}
	
	/** Deselects this object
	 * This also fires an objectDeselected(obj) event.
	 */
	public void deselect()
	{
		if (selected){
			selected = false;
			notifyDeselect();
		}
	}

	/**
	 * Returns if the object is selected or not 
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * Sets if the object can be selected or not 
	 */
	public void setSelectionEnabled(boolean b)
	{
		selectable = b;
	}

	/**
	 * Returns if the object can be selected or not 
	 */
	public boolean isSelectionEnabled()
	{
		return selectable;
	}

	/*
	 * Adds a SelectableListener to listen for selection events 
	 */
	public void addSelectableListener(SelectableListener l)
	{
		selectableListeners.add(l);
	}
	
	/*
	 * Removes a SelectableListener.
	 */
	public void removeSelectableListener(SelectableListener l)
	{
		selectableListeners.remove(l);
	}

	protected void notifySelect() 
	{		
		EventObject e = new EventObject(this);
		Enumeration lists = selectableListeners.elements();
		while (lists.hasMoreElements()) {			
			GraphableListener gl = (GraphableListener)lists.nextElement();
			if (gl instanceof SelectableListener) {
				((SelectableListener)gl).graphableSelected(e);
			}
		}
	}

	protected void notifyDeselect() 
	{
		EventObject e = new EventObject(this);
		Enumeration lists = selectableListeners.elements();
		while (lists.hasMoreElements()) {			
			GraphableListener gl = (GraphableListener)lists.nextElement();
			if (gl instanceof SelectableListener) {
				((SelectableListener)gl).graphableDeselected(e);
			}
		}
	}
	
	/**
	 * @see org.concord.graph.engine.Graphable#getCopy()
	 */
	public Graphable getCopy()
	{
		ControllableDataGraphableDrawing g = new ControllableDataGraphableDrawing();
		g.setColor(lineColor);
		g.setLineWidth(lineWidth);
		g.setDataStore(dataStore);
		
		//FIXME Add values to the vector... is that enough?
		//for(int i=0; i<yValues.size(); i++){
		//	g.yValues.add(yValues.elementAt(i));
		//}
		
		return g;
	}
}
