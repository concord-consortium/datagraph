/*
 * Last modification information:
 * $Revision: 1.18 $
 * $Date: 2004-11-10 04:17:19 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

/**
 * DataGraphable
 * This class is an object that takes data from a 
 * data store and draws this data in a graph (it's a Graphable)
 * as a countinuous set of points that can be connected or not
 * (connected by default).
 * It is itself a DataStore with 2 channels (x,y), so it can easily
 * be added to a data table.
 *
 * Date created: June 18, 2004
 *
 * @author Scott Cytacki<p>
 * @author Ingrid Moncada<p>
 *
 */
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import org.concord.graph.engine.*;
import org.concord.framework.data.stream.*;

public class DataGraphable extends DefaultGraphable
	implements DataStoreListener, DataStore
{
	protected DataStore dataStore;

	//By default, it graphs the dt (x axis) and the first channel (y axis) 
	protected int channelX = -1;
	protected int channelY = 0;
	
	protected Color lineColor = Color.black;
	protected float lineWidth = 2;
	
	protected Stroke stroke;
	
	protected GeneralPath path;
	protected GeneralPath crossPath;

	protected Vector dataStoreListeners;
	
	protected boolean connectPoints = true; 
	protected boolean showCrossPoint = false;
	protected int crossSize = 3;
	
	private int lastValueCalculated = -1;
	protected boolean needUpdate = true;
	protected boolean needUpdateDataReceived = false;
	
	protected boolean autoRepaintData = true;
	
	private float minXValue;
	private float maxXValue;
	private float minYValue;
	private float maxYValue;
	
	/**
     * Default constructor.
     */
	public DataGraphable()
	{
		path = new GeneralPath();
		crossPath = new GeneralPath();
		dataStoreListeners = new Vector();
		updateStroke();
		
		minXValue = Float.NaN;
		maxXValue = Float.NaN;
		minYValue = Float.NaN;
		maxYValue = Float.NaN;
	}
	
	/*
	 * Sets the data producer of this graphable.
	 * By default it will graph dt vs channel 0
     * If the data will be shared by more components, this method is not recommended.
     * Create a ProducerDataStore with the data prodeucer and share the data store with other
     * components. Use setDataStore() instead
	 */
	public void setDataProducer(DataProducer dataProducer)
	{
		//Create a default data store for this data producer
		ProducerDataStore pDataStore = new ProducerDataStore(dataProducer);
		
		setDataStore(pDataStore);
	}
	
	/**
     * If the data will be shared by more components, this method is not recommended.
     * Create a ProducerDataStore with the data prodeucer and share the data store with other
     * components. Use setDataStore() instead
	 * @param dataProducer
	 * @param channelXAxis
	 * @param channelYAxis
	 */
	public void setDataProducer(DataProducer dataProducer, int channelXAxis, int channelYAxis)
	{
		setDataProducer(dataProducer);
		setChannelX(channelXAxis);
		setChannelY(channelYAxis);
	}
	
	/**
	 * Sets the data store that this graphable will be using 
	 * to get its data
	 * By default it will graph dt vs channel 0
	 * @param dataStore
	 */
	public void setDataStore(DataStore dataStore)
	{
		if (this.dataStore != null){
			this.dataStore.removeDataStoreListener(this);
		}
		this.dataStore = dataStore;
		if (this.dataStore != null){
			this.dataStore.addDataStoreListener(this);
		}
	}
	
	/**
	 * Sets the data store that this graphable will be using 
	 * to get its data
	 * It will graph channelXAxis vs channelYAxis 
	 * @param dataStore
	 */
	public void setDataStore(DataStore dataStore, int channelXAxis, int channelYAxis)
	{
		setDataStore(dataStore);
		setChannelX(channelXAxis);
		setChannelY(channelYAxis);
	}	
	
	/*
	 * Resets the data received
	 */
	public void reset()
	{
		dataStore.clearValues();
		
		needUpdate = true;
		needUpdateDataReceived = false;
		lastValueCalculated = -1;
		
		minXValue = Float.NaN;
		maxXValue = Float.NaN;
		minYValue = Float.NaN;
		maxYValue = Float.NaN;
		
		notifyChange();
	}

	/*
	 * Sets the color of the path drawn on the graph
	 */
	public void setColor(int r, int g, int b)
	{
		setColor(new Color(r,g,b));
	}

	/*
	 * Sets the color of the path drawn on the graph
	 */
	public void setColor(Color c)
	{
		lineColor = c;
	}

	public Color getColor()
	{
		return lineColor;
	}
	
	/*
	 * Sets the line width of the path drawn on the graph
	 */
	public void setLineWidth(float width)
	{
		lineWidth = width;
		updateStroke();
	}

    /**
	 * 
	 */
	private void updateStroke()
	{
		stroke = new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
	}

	/**
     *  Draws this object on Graphics g 
     **/
    public void draw(Graphics2D g)
	{
		//long b = System.currentTimeMillis();
		if (needUpdate){
			update();
		}
		
		Shape oldClip = g.getClip();
		Color oldColor = g.getColor();
		Stroke oldStroke = g.getStroke();
		
		graphArea.clipGraphics(g);
		g.setColor(lineColor);
		g.setStroke(stroke);
		
		if (!connectPoints && showCrossPoint){
		}
		else{
			g.draw(path);
		}
		if (showCrossPoint){
			g.draw(crossPath);
		}
		
		g.setColor(oldColor);
		g.setStroke(oldStroke);
		g.setClip(oldClip);

		//long a = System.currentTimeMillis();		
		//System.out.println(a-b);
	}

    /**
     * 
     */
    public void update()
	{		
		float ppx, ppy;
		float px, py;
		int initialI;
		
		//movePoint indicates that when "processing" the current point, 
		//the path should do a moveTo, instead of lineTo
		boolean movePoint = false;		
		
		//lastMovePoint stores the last point that was "processed" doing moveTo instead of lineTo. 
		//It's used only in case there will be another moveTo immediately after, 
		//instead of lineTo (in that case the point has to be drawn as a point)
		Point2D lastMovePoint = null;	
		
		Point2D currentPathPoint;

		if (dataStore == null) return;
		
		Point2D.Double pathPoint = new Point2D.Double();

		CoordinateSystem coord = getGraphArea().getCoordinateSystem();
		
		// if new data was not received or no values have been
		// calculated then all the points need to be recalculated
    	if (!needUpdateDataReceived || lastValueCalculated < 0){
    		path.reset();
    		crossPath.reset();
    		lastValueCalculated = -1;
    	}
   		initialI = lastValueCalculated + 1;
    	    	
   		int i;
		for(i=initialI; i < dataStore.getTotalNumSamples(); i++){
			
			Object objVal;
			
			objVal = dataStore.getValueAt(i, channelX);
			//Handling non null objects different than Floats
			if (objVal != null && !(objVal instanceof Float)){
				System.err.println("Warning! The value is not a Float object: "+objVal);
				movePoint = true;
				continue;
			}

			Float xFloat = (Float)objVal;			
			if (xFloat == null || xFloat.isNaN()){
				px = Float.NaN;
				//When connecting points and not drawing crosses, it should draw a point 
				if (connectPoints && !showCrossPoint && lastMovePoint != null){
					drawPoint(lastMovePoint);
					lastMovePoint = null;
				}
				movePoint = true;
				continue;				
			}
			
			px = xFloat.floatValue(); 
			
			objVal = dataStore.getValueAt(i, channelY);
			//Handling non null objects different than Floats
			if (objVal != null && !(objVal instanceof Float)){
				System.err.println("Warning! The value is not a Float object: "+objVal);
				movePoint = true;
				continue;
			}

			Float yFloat = (Float)objVal;
			if (yFloat == null || yFloat.isNaN()) {
				py = Float.NaN;
				//When connecting points and not drawing crosses, it should draw a point 
				if (connectPoints && !showCrossPoint && lastMovePoint != null){
					drawPoint(lastMovePoint);
					lastMovePoint = null;
				}
				movePoint = true;
				continue;				
			}
			
			py = yFloat.floatValue();
			
			//Always keep the min and max value available
			if (Float.isNaN(minXValue) || px < minXValue){
				minXValue = px;
			}
			if (Float.isNaN(maxXValue) || px > maxXValue){
				maxXValue = px;
			}
			if (Float.isNaN(minYValue) || py < minYValue){
				minYValue = py;
			}
			if (Float.isNaN(maxYValue) || py > maxYValue){
				maxYValue = py;
			}
			
			Point2D.Double dataPoint = new Point2D.Double(px, py);
			
			coord.transformToDisplay(dataPoint, pathPoint);
			
			ppx = (float)pathPoint.x;
			ppy = (float)pathPoint.y;

			currentPathPoint = path.getCurrentPoint();
			
			double thresholdPointTheSame = lineWidth/2 - 0.01;
			
			float dy = 0;
			if (!connectPoints){
				dy = 1;//TODO dy is 1 because of MAC OS X
			}
			
			if (currentPathPoint != null && 
					MathUtil.equalsDouble(ppx, currentPathPoint.getX(), thresholdPointTheSame) && 
					MathUtil.equalsDouble(ppy, currentPathPoint.getY() - dy, thresholdPointTheSame)){
				//System.out.println("Not adding this point:"+ppx+","+ppy+" "+lastPathPoint);
			}
			else{
				
				if (connectPoints){
					if (i==0 || movePoint){
						path.moveTo(ppx, ppy);
						if (lastMovePoint == null){
							lastMovePoint = path.getCurrentPoint();
						}
						movePoint = false;
					}
					else{
						path.lineTo(ppx, ppy);
						lastMovePoint = null;
					}
				}
				else{
					drawPoint(ppx, ppy);
				}
				
				if (showCrossPoint){
					drawCrossPoint(ppx, ppy);
				}
				
			}			
		}
			
		lastValueCalculated = i-1;
		
		//When connecting points and not drawing crosses, it should draw a point 
		if (connectPoints && !showCrossPoint && lastMovePoint != null){
			drawPoint(lastMovePoint);
			lastMovePoint = null;
		}
		
		//System.out.println("size:"+yValues.size());
		
		needUpdateDataReceived = false;
		needUpdate = false;
	}

	/**
	 * @param ppx
	 * @param ppy
	 */
	private void drawCrossPoint(float ppx, float ppy)
	{
		crossPath.moveTo(ppx - crossSize, ppy - crossSize);
		crossPath.lineTo(ppx + crossSize, ppy + crossSize);
		crossPath.moveTo(ppx - crossSize, ppy + crossSize);
		crossPath.lineTo(ppx + crossSize, ppy - crossSize);
	}

	/**
	 * @param ppx
	 * @param ppy
	 */
	private void drawPoint(float ppx, float ppy)
	{
		//Make a vertical "dot" of 1 pixel
		path.moveTo(ppx, ppy);
		path.lineTo(ppx, ppy + 1);//TODO Is 1 because of MAC OS X
	}

	/**
	 * @param ppx
	 * @param ppy
	 */
	private void drawPoint(Point2D p)
	{
		//Make a vertical "dot" of 1 pixel
		drawPoint((float)p.getX(), (float)p.getY());
	}
	
	/** 
	 * Returns a copy of itself 
	 */
	public Graphable getCopy()
	{
		DataGraphable g = new DataGraphable();
		g.setColor(lineColor);
		g.setLineWidth(lineWidth);
		g.setDataStore(dataStore);
		
		//FIXME Add values to the vector... is that enough?
		//for(int i=0; i<yValues.size(); i++){
		//	g.yValues.add(yValues.elementAt(i));
		//}
		
		return g;
	}

/*
public Object getValueAt(int numSample, int numChannel):

	{
		Float val = null; 
		
		if (numChannel == 0){
			if (channelX == -1){
				val = new Float(dt * numSample);
			}
			else{
				val = (Float)xValues.elementAt(numSample);
			}
		}
		else if (numChannel == 1){
			if (channelY == -1){
				val = new Float(dt * numSample);
			}
			else{
				val = (Float)yValues.elementAt(numSample);
			}
		}
		
		return val;
	}
*/
	
/*
getDataChannelDescription(int numChannel):

			channelDesc = new DataChannelDescription();
			channelDesc.setPrecision(2);
			if (numChannel == 0){
				channelDesc.setName("x value");
			}
			else if (numChannel == 1){
				channelDesc.setName("y value");
			}
*/
		
	/**
	 * @return Returns the channelX.
	 */
	public int getChannelX()
	{
		return channelX;
	}
	/**
	 * @param channelX The channelX to set.
	 */
	public void setChannelX(int channelX)
	{
		if (channelX < -1){
			channelX = -1;
		}
		this.channelX = channelX;
	}
	/**
	 * @return Returns the channelY.
	 */
	public int getChannelY()
	{
		return channelY;
	}
	/**
	 * @param channelY The channelY to set.
	 */
	public void setChannelY(int channelY)
	{
		if (channelY < -1){
			channelY = -1;
		}
		this.channelY = channelY;
	}
	/**
	 * @return Returns the connectPoints.
	 */
	public boolean isConnectPoints()
	{
		return connectPoints;
	}
	/**
	 * @param connectPoints The connectPoints to set.
	 */
	public void setConnectPoints(boolean connectPoints)
	{
		if (this.connectPoints == connectPoints) return;
		this.connectPoints = connectPoints;
		needUpdate = true;
		needUpdateDataReceived = false;
		notifyChange();
	}
	
/*	//Debugging purposes
	public void setData(Vector xValues, Vector yValues)
	{
		this.xValues = xValues;
		this.yValues = yValues;
		
		needUpdate = true;
		needUpdateDataReceived = false;
	}
*/
	
	/**
	 * @return Returns the autoRepaintData.
	 */
	public boolean isAutoRepaintData()
	{
		return autoRepaintData;
	}
	/**
	 * @param autoRepaintData The autoRepaintData to set.
	 */
	public void setAutoRepaintData(boolean autoRepaintData)
	{
		this.autoRepaintData = autoRepaintData;
	}
	
	/**
	 * @see org.concord.graph.engine.DefaultGraphable#notifyChange()
	 */
	protected void notifyChange()
	{
		if (autoRepaintData){
			super.notifyChange();
		}
	}

	/**
	 * @see org.concord.graph.engine.DefaultGraphable#notifyChange()
	 */
	public void repaint()
	{
		if (needUpdate || needUpdateDataReceived){
			super.notifyChange();
		}
	}
	
	/**
	 * @return Returns the showCrossPoint.
	 */
	public boolean isShowCrossPoint()
	{
		return showCrossPoint;
	}
	
	/**
	 * @param showCrossPoint The showCrossPoint to set.
	 */
	public void setShowCrossPoint(boolean showCrossPoint)
	{
		if (this.showCrossPoint == showCrossPoint) return;
		this.showCrossPoint = showCrossPoint;
		needUpdate = true;
		needUpdateDataReceived = false;
		notifyChange();
	}
	
	/**
	 * @return Returns the data producer.
	 */
/*	public DataProducer getDataProducer()
	{
		return dataProducer;
	}
*/
	
	public float getMinXValue()
	{
		return minXValue;
	}

	public float getMaxXValue()
	{
		return maxXValue;
	}
	
	public float getMinYValue()
	{
		return minYValue;
	}

	public float getMaxYValue()
	{
		return maxYValue;
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStoreListener#dataAdded(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataAdded(DataStoreEvent evt)
	{
		needUpdate = true;
		needUpdateDataReceived = true;
		
		notifyChange();
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStoreListener#dataRemoved(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataRemoved(DataStoreEvent evt)
	{
		needUpdate = true;
		needUpdateDataReceived = false;
//		lastValueCalculated = -1;
		
		notifyChange();
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChanged(DataStoreEvent evt)
	{
		needUpdate = true;
		needUpdateDataReceived = false;
//		lastValueCalculated = -1;
		
		notifyChange();
	}

	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChannelDescChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChannelDescChanged(DataStoreEvent evt)
	{
		//TODO ?
	}
	
	/**
	 * @return Returns the dataStore.
	 */
	public DataStore getDataStore()
	{
		return dataStore;
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumSamples()
	 */
	public int getTotalNumSamples()
	{
		if (dataStore == null) return 0;
		return dataStore.getTotalNumSamples();
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumChannels()
	 */
	public int getTotalNumChannels()
	{
		return 2;
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
	 */
	public Object getValueAt(int numSample, int numChannel)
	{
		if (numChannel == 0){
			return dataStore.getValueAt(numSample, channelX);
		}
		else if (numChannel == 1){
			return dataStore.getValueAt(numSample, channelY);
		}
		
		return null;
	}
	
	/**
	 * Only works with a Writable Data Store!
	 * @param numSample
	 * @param numChannel
	 * @param value
	 */
	public void setValueAt(int numSample, int numChannel, Object value)
	{
		//Only works with a Writable Data Store!
		if (!(dataStore instanceof WritableDataStore)) {
			throw new IllegalArgumentException("The Data Store "+dataStore+" is not Writable!");
		}
		
		if (numChannel == 0){
			((WritableDataStore)dataStore).setValueAt(numSample, channelX, value);
		}
		else if (numChannel == 1){
			((WritableDataStore)dataStore).setValueAt(numSample, channelY, value);
		}
	}

	/**
	 * Only works with a Writable Data Store!
	 * @param numSample
	 */
	public void removeValueAt(int numSample)
	{
		//Only works with a Writable Data Store!
		if (!(dataStore instanceof WritableDataStore)) {
			throw new IllegalArgumentException("The Data Store "+dataStore+" is not Writable!");
		}
		
		((WritableDataStore)dataStore).removeValueAt(numSample);
	}
	
	public void addPoint(double x, double y)
	{
		//Only works with a Writable Data Store!
		if (!(dataStore instanceof WritableDataStore)) {
			throw new IllegalArgumentException("The Data Store "+dataStore+" is not Writable!");
		}
		
		int newPointIndex = getTotalNumSamples();
		setValueAt(newPointIndex, 0, new Float(x));
		setValueAt(newPointIndex, 1, new Float(y));
	}
	
	/**
	 * @see org.concord.framework.data.stream.DataStore#addDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void addDataStoreListener(DataStoreListener l)
	{
		if (dataStore == null) return;
		dataStore.addDataStoreListener(l);
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#removeDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void removeDataStoreListener(DataStoreListener l)
	{
		if (dataStore == null) return;
		dataStore.removeDataStoreListener(l);
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getDataChannelDescription(int)
	 */
	public DataChannelDescription getDataChannelDescription(int numChannel)
	{
		if (dataStore == null) return null;
		if (numChannel == 0){
			return dataStore.getDataChannelDescription(channelX);
		}
		else if (numChannel == 1){
			return dataStore.getDataChannelDescription(channelY);
		}
		return dataStore.getDataChannelDescription(numChannel);
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#clearValues()
	 */
	public void clearValues()
	{
		reset();
	}
	
	/*
	//It would be nice to have something like this:
	
	public float[] getYValue(float xValue)
	{
	}

	public float[] getYValue(float xValue)
	{
	}
	*/
}
