/*
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2004-11-10 22:28:54 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;



/**
 * AutoScaleGraphable
 * Class name and description
 *
 * Date created: Oct 21, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphAutoScaler extends DataGraphDaemon
{
	protected boolean autoScaleX = true;
	protected boolean autoScaleY = false;
	
	/**
	 * 
	 */
	public void handleUpdate()
	{
		DataGraphable dg;
		float minX=0, maxX=0, minY=0, maxY=0;
		float val;
		
		if (!autoScaleX && !autoScaleY) return;
		
		for (int i=0; i<graphables.size(); i++){
			Object obj = graphables.elementAt(i);
			if (obj instanceof DataGraphable){
				dg = (DataGraphable)obj;
				
				minX = Math.min(dg.getMinXValue(), minX);
				maxX = Math.max(dg.getMaxXValue(), maxX);
				minY = Math.min(dg.getMinYValue(), minY);
				maxY = Math.max(dg.getMaxYValue(), maxY);
			}
		}
		if (!Float.isNaN(minX) && !Float.isNaN(maxX) && !Float.isNaN(minY) && !Float.isNaN(maxY)){
			
			//System.out.println(minX + " " + maxX +  " " + minY +  " " + maxY);
			
			if (autoScaleX && autoScaleY){			
			graph.setLimitsAxisWorld(minX, maxX, minY, maxY);
			}
			else if (autoScaleX){			
				graph.setLimitsAxisWorld(minX, maxX, graph.getMinYAxisWorld(), graph.getMaxYAxisWorld());
			}
			else if (autoScaleY){			
				graph.setLimitsAxisWorld(graph.getMinXAxisWorld(), graph.getMaxXAxisWorld(), minY, maxY);
			}
		}
	}
	
	
	/**
	 * @param autoScaleX The autoScaleX to set.
	 */
	public void setAutoScaleX(boolean autoScaleX)
	{
		this.autoScaleX = autoScaleX;
	}
	
	
	/**
	 * @param autoScaleY The autoScaleY to set.
	 */
	public void setAutoScaleY(boolean autoScaleY)
	{
		this.autoScaleY = autoScaleY;
	}
}
