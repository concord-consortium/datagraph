package org.concord.datagraph.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JMenuItem;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;
import org.concord.graph.engine.CoordinateSystem;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.util.ui.PointTextLabel;

public class DataPointRuler extends PointTextLabel
	implements DataStoreListener
{	
	//
	//Variables to watch the graphables that it's mousing over
	protected GraphableList objList;
	protected int indexPointOver = -1;
	protected DataGraphable graphableOver = null;
	//
	
	//Actual graphable that the label is linked to 
	//(this is temporary because it should be a data point)
	protected DataGraphable dataGraphable;
	
	protected float fx = Float.NaN;
	protected float fy = Float.NaN;
	
	private DashedDataLine verticalDDL = new DashedDataLine(DashedDataLine.VERTICAL_LINE);
	private DashedDataLine horizontalDDL = new DashedDataLine(DashedDataLine.HORIZONTAL_LINE);
	
	//Labels and Units
	protected String xLabel = null;
	protected String xUnits = null;
	protected String yLabel = null;
	protected String yUnits = null;
	protected int xPrecision = 0;
	protected int yPrecision = 0;
	protected String pointLabel = null;	// format: (x, y)
	protected String pointInfoLabel = null;	//format: xlabel: x+unit   ylabel: y+unit
	
	private Point2D mouseLocation = null;
	
	/**
	 * 
	 */
	public DataPointRuler()
	{
		this("Message");
	}
	
	public DataPointRuler(boolean newNote)
	{
		this();
		this.newNote = newNote;
	}
	
	/**
	 * 
	 */
	public DataPointRuler(String msg)
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
		if (!newNote){
			if (isPointInDataPoint(p)){
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
		} else {				
			convertMouseLocationToWorld(p);
			notifyChange();
		}
		return super.mouseMoved(p);
	}
	
	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseDragged(java.awt.Point)
	 */
	public boolean mouseDragged(Point p)
	{
		return true;
	}
	
	/**
	 * @see org.concord.graph.engine.MouseControllable#mouseReleased(java.awt.Point)
	 */
	public boolean mouseReleased(Point p)
	{
		convertMouseLocationToWorld(p);
		//setDataPoint(mouseLocation);
		
		notifyChange();
		return true;
	}
	
	/**
	 * @see org.concord.graph.engine.MouseControllable#mousePressed(java.awt.Point)
	 */
	public boolean mousePressed(Point p)
	{
		dragEnabled = true;
		
		if (newNote){
			if (addAtPoint(p, null)){
				newNote = false;
				setCursor(null);
			}
			dragEnabled = false;
			return true;
		}
		
		if (super.mousePressed(p)){
			
			if (this.isPointInProximity(p)){
				showPopUpMenu(p);
				dragEnabled = false;
				return true;
			}
			
		}
		return false;
	}


	/**
	 * @see org.concord.graph.engine.Drawable#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g)
	{
		drawMessage(g);
		//if(dataGraphable != null && dataGraphable.isVisible()) super.draw(g);
	}
	
	/**
	 * @return Returns the dataGraphable.
	 */
	public DataGraphable getDataGraphable()
	{
		return dataGraphable;
	}
	
	/**
	 * @param dataGraphable The dataGraphable to set.
	 */
	public void setDataGraphable(DataGraphable dataGraphable)
	{
		if (this.dataGraphable == dataGraphable) return;
		
		if (this.dataGraphable != null){
			this.dataGraphable.removeDataStoreListener(this);
		}
		this.dataGraphable = dataGraphable;
	}
	
	/**
	 * @see org.concord.graph.util.ui.BoxTextLabel#doRemove()
	 */
	protected void doRemove()
	{
		setDataGraphable(null);
		super.doRemove();
	}
	
	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataAdded(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataAdded(DataStoreEvent evt)
	{
	}
	
	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChanged(DataStoreEvent evt)
	{
	}
		
	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataRemoved(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataRemoved(DataStoreEvent evt)
	{
		//FIXME See if the point is still in the DataGraphable?
		//For now, I'll check if the graphable is empty
		if(this.dataGraphable == null) return;
		if (this.dataGraphable.getTotalNumSamples() == 0){
			remove();
		}
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChannelDescChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChannelDescChanged(DataStoreEvent evt)
	{
	}
	
	private void drawDashedLines(Graphics2D g, float d1, float d2) {
		setDashedLines(d1, d2);
		verticalDDL.draw(g);
		horizontalDDL.draw(g);		
	}

	private void setDashedLines(float d1, float d2) {
		Point2D pVO = new Point2D.Double(d1,0);
		Point2D pD = new Point2D.Double(d1,d2);
		Point2D pHO = new Point2D.Double(0, d2);
		
		if(xPrecision == 0) xPrecision = 1;
		if(yPrecision == 0) yPrecision = 1;
		verticalDDL.setDataPrecision(xPrecision);
		horizontalDDL.setDataPrecision(yPrecision);

		int left = graphArea.getInsets().left;
		int bottom = graphArea.getInsets().bottom;
		int top = graphArea.getInsets().top;
		pD = new Point2D.Double(d1, graphArea.getSize().height - bottom + top);
		verticalDDL.setPoints(pVO, pD);
		pD = new Point2D.Double(graphArea.getSize().width - left, d2);
		horizontalDDL.setPoints(pHO, pD);			

		DashedDataLine.setGraphArea(graphArea);	
	}
	
	public void setDataPoint(Point2D p) {
        super.setDataPoint(p);
	}
	
	/**
	 * 
	 */
	protected void drawMessage(Graphics2D g)
	{
		drawMessage(g, true);
	}
	
	/**
	 * 
	 */
	protected void drawMessage(Graphics2D g, boolean bDraw)
	{
		drawDashedLines(g, fx, fy);
		
		CoordinateSystem cs = graphArea.getCoordinateSystem();
		Point2D p = cs.transformToDisplay(new Point2D.Double(fx, fy));
		Color oldColor = g.getColor();
		g.setColor(Color.BLUE);
		g.fillOval((int)p.getX()-4,(int)p.getY()-4,8,8);
		g.setColor(oldColor);
	}	
	
	private void convertMouseLocationToWorld(Point p) {
		mouseLocation = new Point2D.Double(p.getX(), p.getY());
		CoordinateSystem cs = graphArea.getCoordinateSystem();
		Point2D point = new Point2D.Double(p.getX(), p.getY());
		dataPoint = cs.transformToWorld(point);
		fx = (float)dataPoint.getX();
		fy = (float)dataPoint.getY();
		displayDataPoint = point;
	}
	
	protected void populatePopUpMenu()
	{
		newNote = false;
		JMenuItem changeColorItem = new JMenuItem("Move ruler");
		JMenuItem deleteItem = new JMenuItem("Delete ruler");
		popUpMenu.add(changeColorItem);
		popUpMenu.add(deleteItem);
		
		changeColorItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//doRemove();
				newNote = true;
			}
		});
		
		deleteItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				newNote = false;
				doRemove();
			}
		});
	}
	
	/**
	 * @param location
	 * @return
	 */
	private boolean isPointInDataPoint(Point p)
	{
		if (dataPoint == null) return false;
		
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
		if (isPointInDataPoint(location)){
			mouseInsideDataPoint = true;
			return true;
		}
		else{
			mouseInsideDataPoint = false;
		}
		return super.isPointInProximity(location);
	}
}
