/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-09-14 20:14:00 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.concord.datagraph.examples.DataGraphExample2MainPanel;
import org.concord.graph.ui.Grid2D;
import org.concord.swing.CustomDialog;


/**
 * DataGraphActions
 * Class name and description
 *
 * Date created: Sep 13, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphActions
{
	protected DataGraph graph;
	
	public static final int ACTION_SETLIMITS = 1; 
	
	public static final int ACTION_HIDE_SHOW_GRID = 2; 
	public static final int ACTION_HIDE_SHOW_GRID_LINES = 3; 
	public static final int ACTION_HIDE_SHOW_TICK_MARKS = 4; 
	public static final int ACTION_HIDE_SHOW_GRID_NUMBERS = 5; 
	public static final int ACTION_HIDE_SHOW_AXIS_LABELS = 6; 
	
	/**
	 * 
	 */
	public DataGraphActions(DataGraph graph)
	{
		this.graph = graph;
	}
	
	public JMenu getMenu()
	{
		JMenu m = new JMenu("Graph");
				
		addDataGraphActions(m);
		
		JMenu gridMenu = new JMenu("Grid");
		m.add(gridMenu);
		
		addGridActions(gridMenu);
		
		return m;
	}

	public void addDataGraphActions(JMenu m)
	{
		JMenuItem mitem;
		
		mitem = new JMenuItem(getAction(ACTION_SETLIMITS));
		m.add(mitem);
	}

	public void addGridActions(JMenu m)
	{
		JMenuItem mitem;
		
		mitem = new JMenuItem(getAction(ACTION_HIDE_SHOW_GRID));
		m.add(mitem);
		
		mitem = new JMenuItem(getAction(ACTION_HIDE_SHOW_GRID_LINES));
		m.add(mitem);
		
		mitem = new JMenuItem(getAction(ACTION_HIDE_SHOW_TICK_MARKS));
		m.add(mitem);

		mitem = new JMenuItem(getAction(ACTION_HIDE_SHOW_GRID_NUMBERS));
		m.add(mitem);
		
		mitem = new JMenuItem(getAction(ACTION_HIDE_SHOW_AXIS_LABELS));
		m.add(mitem);
	}
	
	public AbstractAction getAction(int type)
	{
		return new GraphAction(type);
	}
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		DataGraphExample2MainPanel panel = new DataGraphExample2MainPanel();	
		DataGraphActions graphA = new DataGraphActions(panel.getGraph());
		
		JMenuBar menubar = new JMenuBar();
		menubar.add(graphA.getMenu());
		f.setJMenuBar(menubar);
		
		f.getContentPane().add(panel);
		f.setSize(800, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.show();
	}

	class GraphAction extends AbstractAction
	{
		protected int type;
				
		public GraphAction(int type)
		{
			super();
			this.type = type;
			setName(getDefaultName(type));
		}
		
		public void setName(String name)
		{
			putValue(Action.NAME, name);
		}
		
		private String getDefaultName(int type)
		{
			if (type == ACTION_SETLIMITS){
				return "Set Axis Limits";
			}
			else if (type == ACTION_HIDE_SHOW_GRID){
				return "Hide Whole Grid";
			}
			else if (type == ACTION_HIDE_SHOW_GRID_LINES){
				return "Hide Grid Lines";
			}
			else if (type == ACTION_HIDE_SHOW_TICK_MARKS){
				return "Hide Tick Marks";
			}
			else if (type == ACTION_HIDE_SHOW_GRID_NUMBERS){
				return "Hide Grid Numbers";
			}
			else if (type == ACTION_HIDE_SHOW_AXIS_LABELS){
				return "Hide Axis Labels";
			}
			
			return "";
		}
		
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (graph == null) return;
			
			if (type == ACTION_SETLIMITS){
				
				DataGraphAxisLimitsPanel panel = new DataGraphAxisLimitsPanel();
				panel.initAxisLimitsFromGraph(graph);
				int retDialog = CustomDialog.showOKCancelDialog(null, panel, "Set Limits of the Graph's Axes");
								
				if (retDialog == JOptionPane.OK_OPTION){
					try{
						
						panel.applyAxisLimitsOnGraph(graph);
						
					}
					catch(Throwable ex){
						ex.printStackTrace();
					}
				}
			}	
			else if (type == ACTION_HIDE_SHOW_GRID){
				
				Grid2D grid = graph.getGrid();
				grid.setVisible(!grid.isVisible());
				if (grid.isVisible()){
					setName("Hide Whole Grid");
				}
				else{
					setName("Show Whole Grid");
				}
				
			}
			else if (type == ACTION_HIDE_SHOW_GRID_LINES){
				
				Grid2D grid = graph.getGrid();
				grid.getXGrid().setShowGridLines(!grid.getXGrid().getShowGridLines());
				grid.getYGrid().setShowGridLines(!grid.getYGrid().getShowGridLines());
				if (grid.getXGrid().getShowGridLines()){
					setName("Hide Grid Lines");
				}
				else{
					setName("Show Grid Lines");
				}
				graph.getGraph().repaint();
				
			}
			else if (type == ACTION_HIDE_SHOW_TICK_MARKS){
				
				Grid2D grid = graph.getGrid();
				grid.getXGrid().setShowTickMarks(!grid.getXGrid().getShowTickMarks());
				grid.getYGrid().setShowTickMarks(!grid.getYGrid().getShowTickMarks());
				if (grid.getXGrid().getShowTickMarks()){
					setName("Hide Tick Marks");
				}
				else{
					setName("Show Tick Marks");
				}
				graph.getGraph().repaint();
				
			}
			else if (type == ACTION_HIDE_SHOW_GRID_NUMBERS){
				
				Grid2D grid = graph.getGrid();
				grid.getXGrid().setShowGridLabels(!grid.getXGrid().getShowGridLabels());
				grid.getYGrid().setShowGridLabels(!grid.getYGrid().getShowGridLabels());
				if (grid.getXGrid().getShowGridLabels()){
					setName("Hide Grid Numbers");
				}
				else{
					setName("Show Grid Numbers");
				}
				graph.getGraph().repaint();
				
			}
			else if (type == ACTION_HIDE_SHOW_AXIS_LABELS){
				
				Grid2D grid = graph.getGrid();
				grid.getXGrid().setShowAxisLabels(!grid.getXGrid().getShowAxisLabels());
				grid.getYGrid().setShowAxisLabels(!grid.getYGrid().getShowAxisLabels());
				if (grid.getXGrid().getShowAxisLabels()){
					setName("Hide Axis Labels");
				}
				else{
					setName("Show Axis Labels");
				}
				graph.getGraph().repaint();
				
			}
		}		
	}
}
