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
 * $Revision: 1.6 $
 * $Date: 2005-08-04 21:46:09 $
 * $Author: maven $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.Graphics2D;
import java.util.Vector;

import org.concord.graph.engine.GraphArea;
import org.concord.graph.engine.Graphable;
import org.concord.graph.ui.Grid2D;
import org.concord.graph.ui.SingleAxisGrid;


/**
 * MultipleGrid2D
 * Class name and description
 *
 * Date created: Aug 18, 2004
 *
 * @author imoncada<p>
 *
 */
public class MultipleGrid2D extends Grid2D
{
	protected Vector xGrids;
	protected Vector yGrids;

	/**
	 * Constructs a default data grid with 1 x grid
	 */
	public MultipleGrid2D()
	{
		super();
		
		//By default, the grids draw the axis at the beginning
		xGrid.setAxisDrawMode(SingleAxisGrid.BEGINNING);
		yGrid.setAxisDrawMode(SingleAxisGrid.BEGINNING);
		
		xGrids = new Vector();
		yGrids = new Vector();
		
		addXGrid(xGrid);	
		addYGrid(yGrid);	
	}

	/* *
	 * @see org.concord.graph.engine.Drawable#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g)
	{
		if (graphArea == null) return;

		SingleAxisGrid grid;
		for (int i=0; i< xGrids.size(); i++){
			grid = (SingleAxisGrid)xGrids.elementAt(i);
			if (grid.isVisible()){
				grid.draw(g);
			}
		}
		for (int i=0; i< yGrids.size(); i++){
			grid = (SingleAxisGrid)yGrids.elementAt(i);
			if (grid.isVisible()){
				grid.draw(g);
			}
		}
//		if (yGrid.isVisible()){
//			yGrid.draw(g);
//		}
	}
	
	/**
	 * @see org.concord.graph.engine.Graphable#getCopy()
	 */
	public Graphable getCopy()
	{
		MultipleGrid2D g = new MultipleGrid2D();
		
		g.setGraphArea(graphArea);
		g.yGrid = (SingleAxisGrid)yGrid.getCopy();
		
		SingleAxisGrid sg;
		for (int i=0; i< xGrids.size(); i++){
			sg = (SingleAxisGrid)((SingleAxisGrid)xGrids.elementAt(i)).getCopy();
			g.addXGrid(sg);
		}		
		
		return g;
	}
	
	/**
	 * Returns the ith grid
	 * */
	public SingleAxisGrid getXGrid(int index)
	{
		if (index >=0 && index < xGrids.size()){
			return (SingleAxisGrid)xGrids.elementAt(index);
		}
		return null;
	}
	
	/**
	 * Adds a new xgrid to the grids
	 * @param grid
	 */
	public void addXGrid(SingleAxisGrid grid)
	{
		xGrids.add(grid);
		
		if (grid.getGraphArea() == null && graphArea != null){
			grid.setGraphArea(graphArea);
		}
	}
	
	/**
	 * Returns the ith grid
	 * */
	public SingleAxisGrid getYGrid(int index)
	{
		if (index >=0 && index < yGrids.size()){
			return (SingleAxisGrid)yGrids.elementAt(index);
		}
		return null;
	}
	
	/**
	 * Adds a new xgrid to the grids
	 * @param grid
	 */
	public void addYGrid(SingleAxisGrid grid)
	{
		yGrids.add(grid);
		
		if (grid.getGraphArea() == null && graphArea != null){
			grid.setGraphArea(graphArea);
		}
	}

	/**
	 * Adds a new xgrid to the grids
	 * @param grid
	 */
	public void addGrid(GraphArea ga, boolean showXAxis, boolean showYAxis)
	{
		SingleAxisGrid grid;
		
		grid = (SingleAxisGrid)xGrid.getCopy();	//new SingleAxisGrid(1);
		grid.setGraphArea(ga);
		if (!showXAxis){
			grid.setShowAxisLabels(false);
			grid.setShowGridLabels(false);
			grid.setShowGridLines(false);
			grid.setShowTickMarks(false);
		}
		addXGrid(grid);
		
		grid = (SingleAxisGrid)yGrid.getCopy();	//new SingleAxisGrid(2);
		grid.setGraphArea(ga);
		if (!showYAxis){
			grid.setShowAxisLabels(false);
			grid.setShowGridLabels(false);
			grid.setShowGridLines(false);
			grid.setShowTickMarks(false);
		}
		addYGrid(grid);
	}

	/* (non-Javadoc)
	 * @see org.concord.graph.engine.Drawable#setGraphArea(org.concord.graph.engine.GraphArea)
	 */
	public void setGraphArea(GraphArea area)
	{
		SingleAxisGrid sg;
		if (yGrid.getGraphArea() == graphArea){
			yGrid.setGraphArea(area);
		}
		for (int i=0; i< xGrids.size(); i++){
			sg = (SingleAxisGrid)xGrids.elementAt(i);
			if (sg.getGraphArea() == graphArea){
				sg.setGraphArea(area);
			}
		}

		this.graphArea = area;
	}
}
