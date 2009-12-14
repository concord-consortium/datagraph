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
 * $Date: 2007-03-08 22:10:52 $
 * $Author: sfentress $
 *
 * License Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.concord.graph.ui.Grid2D;
import org.concord.swing.CustomDialog;


/**
 * DataGraphActions
 * A class that manages the possible actions that a user
 * can do with a Data Graph
 * This class can be used to add a swing or AWT menu that
 * automatically contains all the possible actions on a 
 * data graph.
 * It can also be used as a factory class for all the actions 
 * related to the data graph. For example, a user interface
 * can add buttons and associate them with actions generated
 * from this class.
 *
 * Date created: Sep 13, 2004
 *
 * @author imoncada<p>
 * @author dima<p>
 *
 */
public class DataGraphActions
{
	protected DataGraph graph;
	
	protected JMenu swingMenu;
	protected Menu  awtMenu;
	
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
	
	protected void initSwingMenu()
	{
		swingMenu = new JMenu("Graph");
				
		addDataGraphActionsSwing(swingMenu);
		
		JMenu gridSwingMenu = new JMenu("Grid");
		swingMenu.add(gridSwingMenu);
		
		addGridActionsSwing(gridSwingMenu);

	}

	protected void initAWTMenu()
	{
		awtMenu = new Menu("Graph");
				
		addDataGraphActionsAWT(awtMenu);
		
		Menu gridAWTMenu = new Menu("Grid");
		awtMenu.add(gridAWTMenu);
		
		addGridActionsAWT(gridAWTMenu);

	}

	public void addDataGraphActionsSwing(JMenu m)
	{
		JMenuItem mitem;
		
		mitem = new JMenuItem(getAction(ACTION_SETLIMITS));
		m.add(mitem);
	}

	public void addDataGraphActionsAWT(Menu m)
	{
		MenuItem mitem;
		GraphAction action = (GraphAction)getAction(ACTION_SETLIMITS);
		mitem = new MenuItem(action.getValue(Action.NAME).toString());
		mitem.addActionListener(action);
		action.setAWTMenuOwner(mitem);
		m.add(mitem);
	}

	public void addGridActionsSwing(JMenu m)
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

	public void addGridActionsAWT(Menu m)
	{
		MenuItem mitem;
		GraphAction action = (GraphAction)getAction(ACTION_HIDE_SHOW_GRID);
		mitem = new MenuItem(action.getValue(Action.NAME).toString());
		mitem.addActionListener(action);
		m.add(mitem);
		action.setAWTMenuOwner(mitem);
		
        action = (GraphAction)getAction(ACTION_HIDE_SHOW_GRID_LINES);
		mitem = new MenuItem(action.getValue(Action.NAME).toString());
		mitem.addActionListener(action);
		m.add(mitem);
		action.setAWTMenuOwner(mitem);

        action = (GraphAction)getAction(ACTION_HIDE_SHOW_TICK_MARKS);
		mitem = new MenuItem(action.getValue(Action.NAME).toString());
		mitem.addActionListener(action);
		m.add(mitem);
		action.setAWTMenuOwner(mitem);

        action = (GraphAction)getAction(ACTION_HIDE_SHOW_GRID_NUMBERS);
		mitem = new MenuItem(action.getValue(Action.NAME).toString());
		mitem.addActionListener(action);
		m.add(mitem);
		action.setAWTMenuOwner(mitem);

        action = (GraphAction)getAction(ACTION_HIDE_SHOW_AXIS_LABELS);
		mitem = new MenuItem(action.getValue(Action.NAME).toString());
		mitem.addActionListener(action);
		m.add(mitem);
		action.setAWTMenuOwner(mitem);
	}

	public AbstractAction getAction(int type)
	{
		return new GraphAction(type);
	}
	
	public static void main(String[] args)
	{
	    boolean swingFrame = true;
	    
	    if (swingFrame){
    		JFrame f = new JFrame();
    		DataGraph panel = new DataGraph();	
    		DataGraphActions graphA = new DataGraphActions(panel);
    		
    		JMenuBar swingMenubar = new JMenuBar();
    		swingMenubar.add(graphA.getSwingMenu());
    		f.setJMenuBar(swingMenubar);
    		
    		f.getContentPane().add(panel);
    		f.setSize(800, 600);
    		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		f.setVisible(true);
        }
	    else{
    		Frame f = new Frame();
    		DataGraph panel = new DataGraph();	
    		DataGraphActions graphA = new DataGraphActions(panel);
    		
    		MenuBar awtMenubar = new MenuBar();
    		awtMenubar.add(graphA.getAWTMenu());
    		f.setMenuBar(awtMenubar);
    		
    		f.add(panel);
    		f.setSize(800, 600);
    		f.addWindowListener( new WindowAdapter(){
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
    		});
    		f.setVisible(true);
        }
	}

	class GraphAction extends AbstractAction
	{
		private static final long serialVersionUID = 1L;
		protected int type;
		MenuItem    awtMenuOwner = null;  
		
				
		public GraphAction(int type)
		{
			super();
			this.type = type;
			setName(getDefaultName(type));
		}
		
		public void setName(String name)
		{
			putValue(Action.NAME, name);
            if(awtMenuOwner != null) awtMenuOwner.setLabel(name);
		}
		
		private String getDefaultName(int type)
		{
			if (type == ACTION_SETLIMITS){
				return "Set Axis Limits";
			}
			else if (type == ACTION_HIDE_SHOW_GRID){
				return "Hide Grid & Axis";
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
					setName("Hide Grid & Axis");
				}
				else{
					setName("Show Grid & Axis");
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
		
		void setAWTMenuOwner(MenuItem awtMenuOwner)
		{
		    this.awtMenuOwner = awtMenuOwner;
		}
  
		MenuItem getAWTMenuOwner(Menu awtMenuOwner)
		{
		    return awtMenuOwner;
		}
  
	}
	
	/** 
	 * Returns a swing menu with all the actions 
	 * that the user can do with the data graph
	 * @return swing menu.
	 */
	public JMenu getSwingMenu()
	{
		if (swingMenu == null){
			initSwingMenu();
		}
		return swingMenu;
	}
	
	/** 
	 * Returns an AWT menu with all the actions 
	 * that the user can do with the data graph
	 * @return AWT menu.
	 */
	public Menu getAWTMenu()
	{
		if (awtMenu == null){
			initAWTMenu();
		}
		return awtMenu;
	}
}
