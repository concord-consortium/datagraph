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
 * $Revision: 1.9 $
 * $Date: 2007-03-08 22:10:53 $
 * $Author: sfentress $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.util.EventObject;

import org.concord.framework.data.stream.DataProducer;



/**
 * DataGraphAutoScroller
 * Class name and description
 *
 * Date created: Oct 21, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphAutoScroller extends DataGraphDaemon
{
	protected boolean autoScrollX = true;
	protected boolean autoScrollY = false;
		
	protected float minXValue = Float.NaN;
	protected float minYValue = Float.NaN;
	
	protected float xPaddingMin = Float.NaN;
	protected float yPaddingMin = Float.NaN;
	protected float xPaddingMax = 0;
	protected float yPaddingMax = 0;
	private float xPaddingMinPer = Float.NaN;
	private float xPaddingMaxPer;
	private float yPaddingMinPer = Float.NaN;
	private float yPaddingMaxPer;
	
	/**
	 * 
	 */
	public DataGraphAutoScroller(float desiredWidth, float desiredHeight)
	{
		super();
	}

	class Processor {
		float maxX=0, maxY=0;
		
		protected void processGraphable(Object obj)
		{
			if (!(obj instanceof DataGraphable)){
				return;
			}

			DataGraphable dg = (DataGraphable)obj;

			if(dg.isLocked()){
				return;
			}

			DataProducer dataProducer = dg.findDataProducer();
			if(dataProducer == null || !dataProducer.isRunning()){
				return;
			}

			maxX = Math.max(dg.getMaxXValue(), maxX);
			maxY = Math.max(dg.getMaxYValue(), maxY);
		}
		
		public boolean isValid(){
			return !Float.isNaN(maxX) && !Float.isNaN(maxY); 
		}
	}
	
	/**
	 * 
	 */
	public void handleUpdate(EventObject e)
	{
		if (! enabled) {
			return;
		}
		float minX=0, minY=0;
		
		Processor processor = new Processor();
		//float val;
		
		if (!autoScrollX && !autoScrollY) return;
		
		if (e == null) {
			for (Object obj : graphables){
				processor.processGraphable(obj);
			}
		} else {
			Object obj = e.getSource();
			processor.processGraphable(obj);
		}

		if(!processor.isValid()){
			return;
		}
		
		float maxX = processor.maxX;
		float maxY = processor.maxY;
		
		//System.out.println(minX + " " + maxX +  " " + minY +  " " + maxY);

		float worldWidth = (float)(graph.getMaxXAxisWorld() - graph.getMinXAxisWorld());
		float worldHeight = (float)(graph.getMaxYAxisWorld() - graph.getMinYAxisWorld());

		float xPaddingMinLocal = xPaddingMin;
		float xPaddingMaxLocal = xPaddingMax;
		float yPaddingMinLocal = yPaddingMin;
		float yPaddingMaxLocal = yPaddingMax;

		if(!Float.isNaN(xPaddingMinPer)) {
			xPaddingMinLocal = xPaddingMinPer * worldWidth;
			xPaddingMaxLocal = xPaddingMaxPer * worldWidth;
		}

		if(!Float.isNaN(yPaddingMinPer)) {
			yPaddingMinLocal = yPaddingMinPer * worldHeight;
			yPaddingMaxLocal = yPaddingMaxPer * worldHeight;
		}

		if (!Float.isNaN(xPaddingMinLocal) && maxX + xPaddingMinLocal - graph.getMinXAxisWorld() < worldWidth){
			return;
		}
		if (!Float.isNaN(yPaddingMinLocal) && maxY + yPaddingMinLocal - graph.getMinYAxisWorld() < worldHeight){
			return;
		}

		maxX = maxX + xPaddingMaxLocal;
		maxY = maxY + yPaddingMaxLocal;
		minX = maxX - worldWidth;
		minY = maxY - worldHeight;
		if (!((!Float.isNaN(minXValue) && minX < minXValue) || (!Float.isNaN(minYValue) && minY < minYValue))){
			if (autoScrollX && autoScrollY){			
				graph.setLimitsAxisWorld(minX, maxX, minY, maxY);
			}
			else if (autoScrollX){			
				graph.setLimitsAxisWorld(minX, maxX, graph.getMinYAxisWorld(), graph.getMaxYAxisWorld());
			}
			else if (autoScrollY){			
				graph.setLimitsAxisWorld(graph.getMinXAxisWorld(), graph.getMaxXAxisWorld(), minY, maxY);
			}
		}
	}

	/**
	 * @return Returns the minXValue.
	 */
	public float getMinXValue()
	{
		return minXValue;
	}
	/**
	 * @param minXValue The minXValue to set.
	 */
	public void setMinXValue(float minXValue)
	{
		this.minXValue = minXValue;
	}
	/**
	 * @return Returns the minYValue.
	 */
	public float getMinYValue()
	{
		return minYValue;
	}
	/**
	 * @param minYValue The minYValue to set.
	 */
	public void setMinYValue(float minYValue)
	{
		this.minYValue = minYValue;
	}
	/**
	 * @param padding The xPadding to set.
	 */
	public void setXPadding(float paddingMax)
	{
		xPaddingMax = paddingMax;
	}
	/**
	 * @param padding The yPadding to set.
	 */
	public void setYPadding(float paddingMax)
	{
		yPaddingMax = paddingMax;
	}
	/**
	 * @param padding The xPadding to set.
	 */
	public void setXPadding(float paddingMin, float paddingMax)
	{
		xPaddingMin = paddingMin;
		xPaddingMax = paddingMax;
		xPaddingMinPer = Float.NaN;
		xPaddingMaxPer = Float.NaN;

	}

	
	/*
	 * Set the padding in a percentage instead of world fixed values
	 */
	public void setXPaddingPercentage(float paddingMinPercentage, float paddingMaxPercentage)
	{
		xPaddingMinPer = paddingMinPercentage / 100;
		xPaddingMin = Float.NaN;
		xPaddingMaxPer = paddingMaxPercentage / 100;	
		xPaddingMax = Float.NaN;
	}
	/**
	 * @param padding The yPadding to set.
	 */
	public void setYPadding(float paddingMin, float paddingMax)
	{
		yPaddingMin = paddingMin;
		yPaddingMax = paddingMax;
		yPaddingMinPer = Float.NaN;
		yPaddingMaxPer = Float.NaN;
	}
	
	/*
	 * Set the padding in a percentage instead of world fixed values
	 */
	public void setYPaddingPercentage(float paddingMinPercentage, float paddingMaxPercentage)
	{
		yPaddingMinPer = paddingMinPercentage / 100;
		yPaddingMin = Float.NaN;
		yPaddingMaxPer = paddingMaxPercentage / 100;	
		yPaddingMax = Float.NaN;
	}

	/**
	 * 
	 * @param desiredWidth the width to scrolled to
	 * 
	 * @deprecated this method is ignored now.  The current width of the 
	 * graph is assumed to be the desired width
	 */
	public void setDesiredWidth(float desiredWidth)
	{
	}
	
	/**
	 * 
	 * @param desiredHeight the height to scrolled to
	 * 
	 * @deprecated this method is ignored now.  The current height of the
	 * graph is assumed to be the desired height
	 */
	public void setDesiredHeight(float desiredHeight)
	{
	}
}
