

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
 * $Date: 2004-11-12 21:18:04 $
 * $Author: eblack $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.concord.framework.data.stream.DataConsumer;
import org.concord.framework.data.stream.DataProducer;
import org.concord.graph.engine.AxisScale;
import org.concord.graph.engine.DefaultCoordinateSystem2D;
import org.concord.graph.engine.GraphArea;
import org.concord.graph.ui.Grid2D;

/**
 * MultipleDataGraph
 * Class name and description
 *
 * Date created: Aug 27, 2004
 *
 * @author imoncada<p>
 *
 */
public class MultipleDataGraph extends DataGraph
	implements DataConsumer
{
	protected Vector subGraphAreas;	//GraphArea objects
	
	public MultipleDataGraph()
	{
		super();
		
		////////
		defaultGA.setYCentered(true);
		////////
	}
	
	public MultipleDataGraph(int numGraphAreas)
	{
		this();
		initGraphAreas(numGraphAreas);
	}
	
	protected void initGraphAreas(int numGraphAreas)
	{
		if (numGraphAreas < 1) numGraphAreas = 1;
				
		////////
		// Graph Areas
		subGraphAreas = new Vector();

		//Create more graph areas that share the same y axis
		GraphArea ga;
		for (int i=0; i<numGraphAreas; i++){
			addGraphArea();
		}
		////////
	}
	
	protected Grid2D createGrid()
	{
		//Create a data grid that can have more than 1 graph area
		Grid2D gr = new MultipleGrid2D();
		//gr.setInterval(1.0,1.0);
		//gr.setLabelFormat(new DecimalFormat("#"));
		gr.getXGrid().setAxisLabelSize(12);
		gr.getYGrid().setAxisLabelSize(12);
		gr.getXGrid().setVisible(false);
		
		return gr;
	}

	protected void initScaleObject()
	{
	}
	
	public void addGraphArea()
	{
		int defaultGASize = 230;
		int defaultGAGap = 10;
		
		//Create a new graph area that share the same y axis
		GraphArea ga = new GraphArea(new DefaultCoordinateSystem2D(null, defaultCS.getYAxis()));
		ga.setSize(new Dimension(defaultGASize,0));
		Insets ins = defaultGA.getInsets();
		ga.setInsets(new Insets(ins.top, ins.left + ((defaultGASize + defaultGAGap)*subGraphAreas.size()), ins.bottom, ins.right));
		ga.setAutoResizeLeft(true);
		ga.setAutoResizeRight(false);
		ga.setAutoResizeY(true);
		ga.setOriginPositionPercentage(0, defaultGA.getOriginPositionY());
		
		addGraphArea(ga);		

		//Just in case
		graph.registerGraphArea(ga);

		//Adding the scaling object for the graph area
		AxisScale axisScale = new AxisScale();
		axisScale.setGraphArea(ga);
		axisScale.setDragMode(AxisScale.DRAGMODE_NONE);
		axisScale.setShowMessage(false);
		axisScale.setShowCover(false);
		graph.add(axisScale);
		toolBar.addAxisScale(axisScale);
	}
	
	public void addGraphArea(GraphArea ga)
	{
		subGraphAreas.add(ga);
		
		//Add another grid for this new graph area
		((MultipleGrid2D)grid).addGrid(ga);
	}
	
	protected void resetGraphArea()
	{
	//Reset graph areas
		if (adjustOriginOnReset){
			for (int i=0; i< subGraphAreas.size(); i++){
				((GraphArea)subGraphAreas.elementAt(i)).adjustCoordinateSystem();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.datastream.DataConsumer#addDataSource(org.concord.framework.datastream.DataProducer)
	 */
	public void addDataProducer(DataProducer source)
	{
		addDataProducer(source, 0);		
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.datastream.DataConsumer#addDataSource(org.concord.framework.datastream.DataProducer)
	 */
	public void addDataProducer(DataProducer source, int graphAreaIndex)
	{
		if (graphAreaIndex < 0 || graphAreaIndex >= subGraphAreas.size()) return;
		
		GraphArea ga = (GraphArea)subGraphAreas.elementAt(graphAreaIndex);
		addDataProducer(source, ga);
	}
	
	//Testing purposes
    public static void main(String args[]) {
		final JFrame frame = new JFrame();
		final JPanel fa = new MultipleDataGraph(1);
		frame.getContentPane().add(fa);
		frame.setSize(800,600);
		frame.show();
		
		frame.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e){				
				System.exit(0);
			}			
		});
	}
}
