/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-10-29 07:43:12 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import org.concord.datagraph.ui.DataGraph;
import org.concord.graph.engine.GraphableList;


/**
 * DataGraphAutoScroller
 * Class name and description
 *
 * Date created: Oct 21, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphAutoScroller 
{
	protected GraphableList graphables;
	protected DataGraph graph;
	
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
	public void scrollCoordinateSystem()
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
	 * @param graphables The graphables to set.
	 */
	public void setGraphables(GraphableList graphables)
	{
		this.graphables = graphables;
	}
	
	/**
	 * @return Returns the graph.
	 */
	public DataGraph getGraph()
	{
		return graph;
	}
	
	/**
	 * @param graph The graph to set.
	 */
	public void setGraph(DataGraph graph)
	{
		this.graph = graph;
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
}
