/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-08-27 16:56:44 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

/**
 * DataGraphable
 * This class is an object that listens to data (it's a data listener)
 * from a data producer,
 * and draws this data in a graph (it's a Graphable)
 * as a countinuous CONNECTED set of points.
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
	implements DataListener, DataStore
{
	protected DataProducer dataSource;

	protected Vector yValues = new Vector();
	
	protected Color lineColor = Color.black;
	protected float lineWidth = 2;

	private int dataOffset = 0;
	private int nextSampleOffset = 1;
	private float dt = 1;

	protected GeneralPath path;
		
	protected DataStreamDescription dataStreamDesc;
	
	protected Vector dataStoreListeners;
	
	/**
     * Default constructor.
     */
	public DataGraphable()
	{
		path = new GeneralPath();
		dataStoreListeners = new Vector();
	}
	
	/*
	 * Sets the data producer of this graphable
	 */
	public void setDataProducer(DataProducer source)
	{
		if (dataSource != null){
			dataSource.removeDataListener(this);
		}
		dataSource = source;
		if (dataSource != null){
			updateDataDescription(dataSource.getDataDescription());
			dataSource.addDataListener(this);
		}
	}

	/*
	 ** Handler of the data received event
	 * @see org.concord.framework.data.stream.DataListener#dataReceived(org.concord.framework.data.stream.DataEvent)
	 */
	public void dataReceived(DataStreamEvent dataEvent)
	{
		DataStreamEvent de;		
		if (!(dataEvent instanceof DataStreamEvent)) return;
		de = (DataStreamEvent)dataEvent;
		
		float [] data = de.getData();
		int numberOfSamples = de.getNumSamples();

		int sampleIndex = dataOffset;
		for(int i=0; i<numberOfSamples; i++)
		{
			yValues.addElement(new Float(data[sampleIndex]));
			sampleIndex+= nextSampleOffset;
		}

		notifyChange();
		notifyDataAdded();
	}

	/*
	 * Resets the data received
	 */
	public void reset()
	{
		yValues.removeAllElements();
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

	/*
	 * Sets the line width of the path drawn on the graph
	 */
	public void setLineWidth(float width)
	{
		lineWidth = width;
	}

	protected void updateDataDescription(DataStreamDescription desc)
	{
		dataStreamDesc = desc;
		dataOffset = desc.getDataOffset();
		nextSampleOffset = desc.getNextSampleOffset();
		dt = desc.getDt();
	}

	public void dataStreamEvent(DataStreamEvent dataEvent)
	{
		DataStreamEvent de;		
		if (!(dataEvent instanceof DataStreamEvent)) return;
		de = (DataStreamEvent)dataEvent;
		
		updateDataDescription(de.getDataDescription());
	}

    /**
     *  Draws this object on Graphics g 
     **/
    public void draw(Graphics2D g)
	{
		update();
		
		Shape oldClip = g.getClip();
		Color oldColor = g.getColor();
		Stroke oldStroke = g.getStroke();
		
		graphArea.clipGraphics(g);
		g.setColor(lineColor);
		g.setStroke(new BasicStroke(lineWidth));
		g.draw(path);
		
		g.setColor(oldColor);
		g.setStroke(oldStroke);
		g.setClip(oldClip);
	}

    /**
     * 
     */
    public void update()
	{
		path.reset();
		
		float ppx, ppy;
		float px, py;

		Point2D.Double pathPoint = new Point2D.Double();

		float time = 0;
		CoordinateSystem coord = getGraphArea().getCoordinateSystem();

		for(int i=0; i<yValues.size(); i++){
			Float yFloat = (Float)yValues.elementAt(i);
			
			px = time;
			//Flip y
			py = yFloat.floatValue();
			Point2D.Double dataPoint = new Point2D.Double(px, py);
			
			coord.transformToDisplay(dataPoint, pathPoint);
			
			ppx = (float)pathPoint.x;
			ppy = (float)pathPoint.y;

			if (i==0){
				path.moveTo(ppx, ppy);
			}
			else{
				path.lineTo(ppx, ppy);
			}
			time += dt;
		}
	}

	/** 
	 * Returns a copy of itself 
	 */
	public Graphable getCopy()
	{
		DataGraphable g = new DataGraphable();
		g.setColor(lineColor);
		g.setLineWidth(lineWidth);
		g.setDataProducer(dataSource);
		
		//FIXME Add values to the vector... is that enough?
		for(int i=0; i<yValues.size(); i++){
			g.yValues.add(yValues.elementAt(i));
		}
		
		return g;
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumSamples()
	 */
	public int getTotalNumSamples()
	{
		return yValues.size();
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getTotalNumChannels()
	 */
	public int getTotalNumChannels()
	{
		return 2;
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.data.stream.DataStore#getValueAt(int, int)
	 */
	public Object getValueAt(int numSample, int numChannel)
	{
		Float val = null; 
		
		if (numChannel == 0){
			val = new Float(dt * numSample);
		}
		if (numChannel == 1){
			val = (Float)yValues.elementAt(numSample);
		}
		
		return val;
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#getDataChannelDescription(int)
	 */
	public DataChannelDescription getDataChannelDescription(int numChannel)
	{
		DataChannelDescription channelDesc = null;
		if (dataStreamDesc != null){
			channelDesc = dataStreamDesc.getChannelDescription(numChannel);
		}
		if (channelDesc == null){
			channelDesc = new DataChannelDescription();
			channelDesc.setPrecision(2);
			if (numChannel == 0){
				channelDesc.setName("x value");
			}
			else if (numChannel == 1){
				channelDesc.setName("y value");
			}
		}
		return channelDesc;
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#addDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void addDataStoreListener(DataStoreListener l)
	{
		if (!dataStoreListeners.contains(l)){
			dataStoreListeners.add(l);
		}
	}

	/**
	 * @see org.concord.framework.data.stream.DataStore#removeDataStoreListener(org.concord.framework.data.stream.DataStoreListener)
	 */
	public void removeDataStoreListener(DataStoreListener l)
	{
		dataStoreListeners.remove(l);		
	}
	
	protected void notifyDataAdded()
	{
		DataStoreEvent evt = new DataStoreEvent(this, DataStoreEvent.DATA_ADDED);
		DataStoreListener l;
		for (int i=0; i<dataStoreListeners.size(); i++){
			l = (DataStoreListener)dataStoreListeners.elementAt(i);
			l.dataAdded(evt);
		}
	}
	
	protected void notifyDataRemoved()
	{
		DataStoreEvent evt = new DataStoreEvent(this, DataStoreEvent.DATA_ADDED);
		DataStoreListener l;
		for (int i=0; i<dataStoreListeners.size(); i++){
			l = (DataStoreListener)dataStoreListeners.elementAt(i);
			l.dataRemoved(evt);
		}
	}
	
	protected void notifyDataChanged()
	{
		DataStoreEvent evt = new DataStoreEvent(this, DataStoreEvent.DATA_ADDED);
		DataStoreListener l;
		for (int i=0; i<dataStoreListeners.size(); i++){
			l = (DataStoreListener)dataStoreListeners.elementAt(i);
			l.dataChanged(evt);
		}
	}
	
}
