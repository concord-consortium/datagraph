

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
 * $Revision: 1.5 $
 * $Date: 2004-11-12 21:18:03 $
 * $Author: eblack $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;



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
	
	protected float width;
	protected float height;
	
	protected float minXValue = Float.NaN;
	protected float minYValue = Float.NaN;
	
	protected float xPaddingMin = Float.NaN;
	protected float yPaddingMin = Float.NaN;
	protected float xPaddingMax = 0;
	protected float yPaddingMax = 0;
	
	/**
	 * 
	 */
	public DataGraphAutoScroller(float desiredWidth, float desiredHeight)
	{
		super();
		this.width = desiredWidth;
		this.height = desiredHeight;
	}

	/**
	 * 
	 */
	public void handleUpdate()
	{
		DataGraphable dg;
		float minX=0, maxX=0, minY=0, maxY=0;
		float val;
		
		if (!autoScrollX && !autoScrollY) return;
		
		for (int i=0; i<graphables.size(); i++){
			Object obj = graphables.elementAt(i);
			if (obj instanceof DataGraphable){
				dg = (DataGraphable)obj;
				
				maxX = Math.max(dg.getMaxXValue(), maxX);
				maxY = Math.max(dg.getMaxYValue(), maxY);
			}
		}
		if (!Float.isNaN(maxX) && !Float.isNaN(maxY)){
			
			//System.out.println(minX + " " + maxX +  " " + minY +  " " + maxY);
			
			double worldWidth = graph.getMaxXAxisWorld() - graph.getMinXAxisWorld();
			double worldHeight = graph.getMaxYAxisWorld() - graph.getMinYAxisWorld();
			
			if (!Float.isNaN(xPaddingMin) && maxX + xPaddingMin - graph.getMinXAxisWorld() < width){
				return;
			}
			if (!Float.isNaN(yPaddingMin) && maxY + yPaddingMin - graph.getMinYAxisWorld() < height){
				return;
			}
			
			maxX = maxX + xPaddingMax;
			maxY = maxY + yPaddingMax;
			minX = maxX - width;
			minY = maxY - height;
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
	}
	/**
	 * @param padding The yPadding to set.
	 */
	public void setYPadding(float paddingMin, float paddingMax)
	{
		yPaddingMin = paddingMin;
		yPaddingMax = paddingMax;
	}
	
	/**
	 * 
	 * @param desiredWidth the width to scrolled to
	 */
	public void setDesiredWidth(float desiredWidth)
	{
		width = desiredWidth;
	}
	
	/**
	 * 
	 * @param desiredHeight the height to scrolled to
	 */
	public void setDesiredHeight(float desiredHeight)
	{
		height = desiredHeight;
	}
}
