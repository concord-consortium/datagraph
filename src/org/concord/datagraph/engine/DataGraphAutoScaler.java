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
 * AutoScaleGraphable
 * Class name and description
 *
 * Date created: Oct 21, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphAutoScaler 
{
	protected GraphableList graphables;
	protected DataGraph graph;
	
	protected boolean autoScaleX = true;
	protected boolean autoScaleY = false;
	
	/**
	 * 
	 */
	public DataGraphAutoScaler()
	{
		super();
	}

	/**
	 * 
	 */
	public void scaleCoordinateSystem()
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
}
