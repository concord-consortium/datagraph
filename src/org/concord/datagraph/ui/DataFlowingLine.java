/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01742
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
 * END LICENSE */

/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2007-02-23 18:48:35 $
 * $Author: sfentress $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.concord.framework.data.stream.DataConsumer;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamEvent;
import org.concord.graph.engine.CoordinateSystem;
import org.concord.graph.engine.DefaultControllable;
import org.concord.graph.engine.Graphable;
import org.concord.graph.util.engine.DrawingObject;
import org.concord.graph.util.ui.ImageStamp;


/**
 * DrawingShape
 * Class name and description
 *
 * Date created: Mar 24, 2005
 *
 * @author imoncada<p>
 *
 */
public class DataFlowingLine extends DefaultControllable
    implements DrawingObject, DataListener, DataConsumer
{
	float forwardThreshold;
	float reverseThreshold;
		
	Color forwardColor1, forwardColor2;
	Color reverseColor1, reverseColor2; 
	Color stopColor;
	
	boolean flowing = false;
	Color color1, color2;
	
	int channelNumber = 0;
	
	private Stroke stroke;
	private float cycleDistance;
    private float cycleOffset;
    
    // this should be generalized to handle any drawable
    // but currently there isn't a way to get drawbles 
    // locations.  
    private ImageStamp image1;
    private ImageStamp image2;
    
	/**
	 * @see org.concord.graph.util.engine.DrawingObject#setColor(java.awt.Color)
	 */
	public void setFowardColor1(Color color)
	{
		this.forwardColor1 = color;
		notifyChange();
	}

    /**
     * @see org.concord.graph.util.engine.DrawingObject#getColor()
     */
    public Color getForwardColor1()
    {
        if (forwardColor1 == null) return Color.black;
        return forwardColor1;
    }

    /**
     * @see org.concord.graph.util.engine.DrawingObject#setColor(java.awt.Color)
     */
    public void setForwardColor2(Color color)
    {
        this.forwardColor2 = color;
        notifyChange();
    }

    /**
     * @see org.concord.graph.util.engine.DrawingObject#getColor()
     */
    public Color getForwardColor2()
    {
        if (forwardColor2 == null) return Color.green;
        return forwardColor1;
    }
    
	/**
	 * @see org.concord.graph.util.engine.DrawingObject#setColor(java.awt.Color)
	 */
	public void setReverseColor1(Color color)
	{
		this.reverseColor1 = color;
		notifyChange();
	}

    /**
     * @see org.concord.graph.util.engine.DrawingObject#getColor()
     */
    public Color getReverseColor1()
    {
        if (reverseColor1 == null) return Color.black;
        return reverseColor1;
    }

    /**
     * @see org.concord.graph.util.engine.DrawingObject#setColor(java.awt.Color)
     */
    public void setReverseColor2(Color color)
    {
        this.reverseColor2 = color;
        notifyChange();
    }

    /**
     * @see org.concord.graph.util.engine.DrawingObject#getColor()
     */
    public Color getReverseColor2()
    {
        if (reverseColor2 == null) return Color.red;
        return reverseColor1;
    }

    public void setImage1(ImageStamp image)
    {
        image1 = image;
    }
    
    public void setImage2(ImageStamp image)
    {
        image2 = image;
    }

    public void setCycleDistance(float distance)
    {
        cycleDistance = distance;
    }

    public void setCycleOffset(float offset)
    {
        cycleOffset = offset;
    }
    
    public Stroke getStroke()
    {
        return stroke;
    }
    
    public void setStroke(Stroke stroke)
    {
        this.stroke = stroke;
    }
    
	/**
	 * @see org.concord.graph.engine.Graphable#getCopy()
	 */
	public Graphable getCopy()
	{
		// TODO Auto-generated method stub
		return null;
	}
    
    /*
     * return a point a given a distance from the start
     * point on the line.  The direction of the line is 
     * from start point to the end point.
     * 
     *  I know this can be done with matrix calcs, but
     *  I don't see and java untils for this.
     */
    public Point2D getLinePoint(double distance, Point2D start, 
            Point2D end, Point2D linePoint)
    {
        // find the lines theta:
        double xOffset = end.getX() - start.getX();
        double yOffset = end.getY() - start.getY();

        double theta = Math.atan2(yOffset, xOffset);
        
        double ptY = distance*Math.sin(theta);
        double ptX = distance*Math.cos(theta);

        if(linePoint == null){
            linePoint = new Point2D.Double();
        }
        linePoint.setLocation(start.getX() + ptX, start.getY() + ptY);
        return linePoint;
    }
    
    public void start()
    {
        // This is over kill creating a new thread for each instance
        // It would be nice if the graph provided a framework for this
        // but in the meantime we 
        Thread runner = new Thread(){
            float currCycleOffset = 0f;
            public void run()            
            {
                while(true) {
                    setCycleOffset(currCycleOffset);
                    currCycleOffset += 2;
                    notifyChange();
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        
        runner.start();
   }

    
    /**
	 * @see org.concord.graph.engine.Drawable#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g)
	{
        Paint oldPaint = g.getPaint();
        Stroke oldStroke = g.getStroke();
        Shape oldClip = g.getClip();
        
        CoordinateSystem cs = graphArea.getCoordinateSystem();

		Point2D start = cs.transformToDisplay(image1.getLocation());
        Point2D end = cs.transformToDisplay(image2.getLocation());

        if(flowing) {
        	
        	Point2D cycleStart = getLinePoint(cycleOffset, start, end, null);
        	Point2D cycleEnd = getLinePoint(cycleDistance, cycleStart, end, null);
        	
        	Paint cyclePaint = new GradientPaint(cycleStart, color1, cycleEnd, color2, true);
        	
        	// probably we need to save the old gradient
        	g.setPaint(cyclePaint);
        	
        	Stroke lineStroke = getStroke();
        	if(stroke == null) {
        		stroke = new BasicStroke(20, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);;
        	}
        	
        	g.setStroke(stroke);
        } else {
        	g.setColor(stopColor);
        }
        
        Line2D line = new Line2D.Double(start, end);

        graphArea.clipGraphics(g);

        g.draw(line);
        
        g.setPaint(oldPaint);
		g.setStroke(oldStroke);
		g.setClip(oldClip);
	}
    
    /* (non-Javadoc)
     * @see org.concord.graph.engine.MouseSensitive#isPointInProximity(java.awt.Point)
     */
    public boolean isPointInProximity(Point p)
    {
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.concord.graph.util.engine.DrawingObject#getDrawingDragMode()
     */
    public int getDrawingDragMode()
    {
        return DRAWING_DRAG_MODE_NONE;
    }

    public boolean erase(Rectangle2D rectDisplay)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    public Color getColor()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Rectangle2D getBoundingRectangle()
    {
    	//TODO change to return actual bounding box
    	Rectangle2D fakeRect = new Rectangle2D.Double();
    	return fakeRect;
    }
    
    public boolean isResizeEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    public boolean setDrawingDragMode(int mode)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    public void setColor(Color color)
    {
        // TODO Auto-generated method stub
        
    }
    
	public void addDataProducer(DataProducer source) 
	{
		source.addDataListener(this);
	}
	
	public void removeDataProducer(DataProducer source) 
	{
		source.removeDataListener(this);		
	}
	
	public void dataReceived(DataStreamEvent dataEvent) 
	{
		int numSamples = dataEvent.getNumSamples();
		int channelsPerSample = dataEvent.getDataDescription().getChannelsPerSample();
		int nextSampleOff = dataEvent.getDataDescription().getNextSampleOffset();
		float value = dataEvent.data[(numSamples-1)*nextSampleOff+channelNumber];

		if(value > forwardThreshold) {
			flowing = true;
			color1 = getForwardColor1();
			color2 = getForwardColor2();			
		} else if(value < reverseThreshold) {
			flowing = true;
			color1 = getReverseColor1();
			color2 = getReverseColor2();
		} else {
			flowing = false;
		}
	}
	
	public void dataStreamEvent(DataStreamEvent dataEvent) 
	{
		// TODO Auto-generated method stub
		
	}	
}
