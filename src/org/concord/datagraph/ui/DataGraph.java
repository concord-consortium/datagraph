/*
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2004-09-07 17:59:21 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.*;
import org.concord.graph.ui.*;
import org.concord.graph.engine.*;
import org.concord.graph.examples.*;

/**
 * DataGraph
 * This is a panel with a graph and a toolbar
 * The graph has one default graph area that is not the whole window,
 * The grid is drawn at the beginning (LEFT and BOTTOM)
 * and there is some space left for the axis labels.
 *
 * Date created: June 18, 2004
 *
 * @author Scott Cytacki<p>
 * @author Ingrid Moncada<p>
 *
 */

public class DataGraph extends JPanel
	implements DataConsumer
{
	//Graph, grid and toolbar
	protected GraphWindow graph;
	protected Grid2D grid;
	protected GraphWindowToolBar toolBar;
	
	protected Hashtable sources = new Hashtable();

	protected boolean selectionMode = true;

	protected GraphableList objList;
	
	protected DefaultCoordinateSystem2D defaultCS;
	protected GraphArea defaultGA;
	
	protected boolean adjustOriginOnReset = true;
	
	public DataGraph()
	{
		////////
		// Graph
		//Create the graph
		graph = new GraphWindow();
		defaultGA = graph.getDefaultGraphArea();
		CoordinateSystem cs = defaultGA.getCoordinateSystem();
		//Make sure we are using a DefaultCoordinateSystem2D
		if (!(cs instanceof DefaultCoordinateSystem2D)){
			graph.setDefaultGraphArea(new GraphArea(new DefaultCoordinateSystem2D()));
		}
		defaultGA = graph.getDefaultGraphArea();
		defaultCS = (DefaultCoordinateSystem2D)defaultGA.getCoordinateSystem();

		defaultGA.setInsets(new Insets(5,40,40,5));
		
		//setOriginOffsetDisplay(20, 0);
		//defaultGA.setYCentered(true);
		////////
		
		////////
		// Grid
		grid = createGrid();
		
		//Add the grid to the graph
		graph.addDecoration(grid);
		////////
		
		////////
		// Tool Bar
		toolBar = new GraphWindowToolBar();
		toolBar.setGraphWindow(graph);
		toolBar.setButtonsMargin(0);
		toolBar.setFloatable(false);
		////////

		////////
		// List of Graphable Objects
		objList = new SelectableList();
		graph.add(objList);
		////////

		setLayout(new BorderLayout());
		add(graph);
		add(toolBar, BorderLayout.EAST);		

		initScaleObject();
	}
	
	protected Grid2D createGrid()
	{
		Grid2D gr = new Grid2D();
		//gr.setInterval(1.0,1.0);
		//gr.setLabelFormat(new DecimalFormat("#"));
		gr.getXGrid().setAxisLabelSize(12);
		gr.getYGrid().setAxisLabelSize(12);
		
		gr.getXGrid().setAxisDrawMode(SingleAxisGrid.BEGINNING);
		gr.getYGrid().setAxisDrawMode(SingleAxisGrid.BEGINNING);
		
		gr.getXGrid().setDrawGridOnAxis(true);
		gr.getYGrid().setDrawGridOnAxis(true);
		
		return gr;
	}

	protected void initScaleObject()
	{
		addScaleAxis(defaultGA);
	}
	
	protected void addScaleAxis(GraphArea ga)
	{
		//Adding the scaling object for the graph area
		AxisScale axisScale = new AxisScale();
		axisScale.setGraphArea(ga);
		axisScale.setDragMode(AxisScale.DRAGMODE_NONE);
		axisScale.setShowMessage(false);
		axisScale.setShowCover(false);
		graph.add(axisScale);
		toolBar.addAxisScale(axisScale);
	}
	
	public GraphWindow getGraph()
	{
		return graph;
	}
	
	public Grid2D getGrid()
	{
		return grid;
	}

	public void setSelectionMode(boolean mode)
	{
		selectionMode = mode;
	}

/*
	public void mouseDragged(MouseEvent e)
	{
		if (!selectionMode) return;

		Point2D.Double draggedWorldPoint = new Point2D.Double();


		Point2D.Double mousePoint = new Point2D.Double(e.getX(), e.getY());
		CoordinateSystem coord = getGraph().getDefaultGraphArea().getCoordinateSystem();
		
		coord.transformToWorld(mousePoint, draggedWorldPoint);

		setSelection((float)pressedWorldPoint.x, (float)pressedWorldPoint.y, 
					 (float)draggedWorldPoint.x-(float)pressedWorldPoint.x,
					 (float)draggedWorldPoint.y-(float)pressedWorldPoint.y);
	}

	public void mouseMoved(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	Point2D.Double pressedWorldPoint = new Point2D.Double();
	public void mousePressed(MouseEvent e)
	{
		if (!selectionMode) return;
		
		Point2D.Double mousePoint = new Point2D.Double(e.getX(), e.getY());
		CoordinateSystem coord = getGraph().getDefaultGraphArea().getCoordinateSystem();
		coord.transformToWorld(mousePoint, pressedWorldPoint);
		
		setSelection((float)pressedWorldPoint.x, (float)pressedWorldPoint.y, 0, 0);
	}

	public void mouseReleased(MouseEvent e)
	{

	}
*/	
	public void zoomSelection()
	{
		DashedBox selectionBox = toolBar.getSelectionBox();
		if(selectionBox == null) { 
			return; 
		}
		
		selectionBox.zoom();		
	}

	public void setScale(double xScale, double yScale)
	{
		CoordinateSystem coord = getGraph().getDefaultGraphArea().getCoordinateSystem();

		Point2D.Double scale = new Point2D.Double(xScale, yScale);
		coord.setScale(scale);
	}
	
	public double getXScale()
	{
		CoordinateSystem coord = getGraph().getDefaultGraphArea().getCoordinateSystem();
		
		return coord.getScale().getX();
	}

	public double getYScale()
	{
		CoordinateSystem coord = getGraph().getDefaultGraphArea().getCoordinateSystem();
		
		return coord.getScale().getY();
	}
	
	public void setOriginOffsetDisplay(double xPos, double yPos)
	{
		CoordinateSystem coord = getGraph().getDefaultGraphArea().getCoordinateSystem();

		Point2D.Double origin = new Point2D.Double(xPos, yPos);
		coord.setOriginOffsetDisplay(origin);		
	}
	
	public double getXOriginOffsetDisplay()
	{
		CoordinateSystem coord = getGraph().getDefaultGraphArea().getCoordinateSystem();

		return coord.getOriginOffsetDisplay().getX();		
	}

	public double getYOriginOffsetDisplay()
	{
		CoordinateSystem coord = getGraph().getDefaultGraphArea().getCoordinateSystem();

		return coord.getOriginOffsetDisplay().getY();		
	}
	
	public DataGraphable getGraphable(DataProducer source)
	{
		return (DataGraphable)sources.get(source);
	}

	public void reset()
	{
		//Reset each data graphable
		for(int i=0; i<objList.size(); i++)
		{
			if(objList.elementAt(i) instanceof DataGraphable)
			{
				DataGraphable dGraphable = (DataGraphable)objList.elementAt(i);
				dGraphable.reset();
			}
		}
		
		resetGraphArea();
	}
	
	protected void resetGraphArea()
	{
		//Reset graph areas
		if (adjustOriginOnReset){
			defaultGA.adjustCoordinateSystem();
		}
	}
	
	public void setSelection(float x, float y, float width, float height)
	{
		if(width < 0)
		{
			width=-width;
			x-=width;
		}
		if(height < 0)
		{
			height=-height;
			y-=height;
		}

		toolBar.showDashedBox(true);
		
		DashedBox selectionBox = toolBar.getSelectionBox();		
		if (selectionBox != null){
			selectionBox.setBounds(x,y,width,height);
		}
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.datastream.DataConsumer#addDataSource(org.concord.framework.datastream.DataProducer)
	 */
	public void addDataProducer(DataProducer source)
	{
		addDataProducer(source, defaultGA);		
	}

	/**
	 * 
	 * @param source
	 * @param ga
	 */
	protected void addDataProducer(DataProducer source, GraphArea ga)
	{
		// Create a graphable for this datasource
		// add it to the graph
		DataGraphable dGraphable = new DataGraphable();
		dGraphable.setDataProducer(source);
		
		dGraphable.setGraphArea(ga);
		
		objList.add(dGraphable);
		sources.put(source, dGraphable);		
	}
	
	/* (non-Javadoc)
	 * @see org.concord.framework.datastream.DataConsumer#removeDataSource(org.concord.framework.datastream.DataProducer)
	 */
	public void removeDataProducer(DataProducer source)
	{

		// TODO Auto-generated method stub
		// remove the associated data source from 
		// the graph
		DataGraphable dGraphable = (DataGraphable)sources.get(source);
		if (dGraphable != null){
			dGraphable.setDataProducer(null);
			objList.remove(dGraphable);
		}
	}

	public GraphableList getObjList()
	{
		return objList;
	}

	public GraphWindowToolBar getToolBar()
	{
		return toolBar;
	}

	/**
	 * @return Returns the adjustOnReset.
	 */
	public boolean isAdjustOriginOffsetOnReset()
	{
		return adjustOriginOnReset;
	}

	/**
	 * @param adjustOnReset The adjustOnReset to set.
	 */
	public void setAdjustOriginOffsetOnReset(boolean adjustOnReset)
	{
		this.adjustOriginOnReset = adjustOnReset;
	}
	
	//Testing purposes
    public static void main(String args[]) {
		final JFrame frame = new JFrame();
		final JPanel fa = new DataGraph();
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
