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
 * $Revision: 1.10 $
 * $Date: 2007-03-08 22:10:53 $
 * $Author: sfentress $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.awt.geom.Point2D;
import java.util.EventObject;



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
	public static final int DEFAULT_MARGIN = 10;
	
	protected boolean autoScaleX = true;
	protected boolean autoScaleY = false;
	
	int margin = DEFAULT_MARGIN;
	
	/**
	 * 
	 */
	public void handleUpdate(EventObject e)
	{
		DataGraphable dg;
		float minX=Float.MAX_VALUE, maxX=0, minY=Float.MAX_VALUE, maxY=0;
		//float val;
		
		if (!autoScaleX && !autoScaleY) return;
		
		if(graphables.size() < 1) return;
		
		boolean needUpdate = false;
		
		for (int i=0; i<graphables.size(); i++){
			Object obj = graphables.elementAt(i);
			if (obj instanceof DataGraphable){
				dg = (DataGraphable)obj;
				if(dg.getTotalNumSamples() < 1 || !dg.isVisible()) {
					continue;
				}
				
				minX = Math.min(dg.getMinXValue(), minX);
				maxX = Math.max(dg.getMaxXValue(), maxX);
				minY = Math.min(dg.getMinYValue(), minY);
				maxY = Math.max(dg.getMaxYValue(), maxY);
				needUpdate = true;
			}
		}
		
		if(!needUpdate) return;
		
		if (!Float.isNaN(minX) && !Float.isNaN(maxX) && !Float.isNaN(minY) && !Float.isNaN(maxY)){
			
			//System.out.println(minX + " " + maxX +  " " + minY +  " " + maxY);
			
			if (autoScaleX && autoScaleY){			
				graph.setLimitsAxisWorld(minX, maxX, minY, maxY);
				
				Point2D scale = graph.getGraphArea().getCoordinateSystem().getScale();

				minY = (float) (minY - margin/scale.getY());
				maxX = (float) (maxX + margin/scale.getX());
				maxY = (float) (maxY + margin/scale.getY());
				graph.setLimitsAxisWorld(minX, maxX, minY, maxY);
			}
			else if (autoScaleX){			
				graph.setLimitsAxisWorld(minX, maxX, graph.getMinYAxisWorld(), graph.getMaxYAxisWorld());
				
				Point2D scale = graph.getGraphArea().getCoordinateSystem().getScale();
				
				maxX = (float) (maxX + margin/scale.getX());
				
				graph.setLimitsAxisWorld(minX, maxX, graph.getMinYAxisWorld(), graph.getMaxYAxisWorld());
			}
			else if (autoScaleY){			
				graph.setLimitsAxisWorld(graph.getMinXAxisWorld(), graph.getMaxXAxisWorld(), minY, maxY);
				
				Point2D scale = graph.getGraphArea().getCoordinateSystem().getScale();
				
				minY = (float) (minY - margin/scale.getY());
				maxY = (float) (maxY + margin/scale.getY());
				
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
	
	public void setMargin(int margin) {
		this.margin = margin;
	}
}
