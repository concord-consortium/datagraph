/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-01-23 18:33:58 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.concord.data.ui.DataTableCellRenderer;
import org.concord.data.ui.DataTablePanel;
import org.concord.data.ui.DataTableEditor;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataStore;
import org.concord.graph.engine.GraphableList;


/**
 * DataGraphPanelExample
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphablesPanel extends JPanel
{
	protected DataGraph graph;
	protected JDialog tableDialog = null;

	protected JCheckBox connectPointsBox;
	protected JCheckBox showCrossBox;

	public DataGraphablesPanel()
	{
		super();
		
		setLayout(new BorderLayout());
		graph = new DataGraph();
		
		add(graph);
		add(new InputPanel(true, true), BorderLayout.SOUTH);
	}
	
	public void setConnectPoints(boolean connect)
	{
		if (connectPointsBox != null &&
				connectPointsBox.isSelected() != connect){
			connectPointsBox.setSelected(connect);
		}
		
		DataGraphable dg;
		Object obj;
		GraphableList objList;
		
		objList = graph.getObjList();
		for (int i=0; i<objList.size(); i++){
			obj = objList.elementAt(i);
			if (obj instanceof DataGraphable){
				dg = (DataGraphable)obj;
				dg.setConnectPoints(connect);
			}
		}
	}
	
	public void setShowCrossPoint(boolean b)
	{
		if (showCrossBox != null &&
				showCrossBox.isSelected() != b){
			showCrossBox.setSelected(b);
		}
		
		DataGraphable dg;
		Object obj;
		GraphableList objList;
		
		objList = graph.getObjList();
		for (int i=0; i<objList.size(); i++){
			obj = objList.elementAt(i);
			if (obj instanceof DataGraphable){
				dg = (DataGraphable)obj;
				dg.setShowCrossPoint(b);
			}
		}
	}
	
	public void setLineWidth(float lineWidth)
	{
		DataGraphable dg;
		Object obj;
		GraphableList objList;
		
		objList = graph.getObjList();
		for (int i=0; i<objList.size(); i++){
			obj = objList.elementAt(i);
			if (obj instanceof DataGraphable){
				dg = (DataGraphable)obj;
				dg.setLineWidth(lineWidth);
			}
		}
	}
	
	public void clearValues()
	{
		graph.reset();
	}
	
	public void showTable()
	{
		//Show one dialog for each data store :d 
		
		DataGraphable dg;
		Object obj;
		GraphableList objList;
		
		objList = graph.getObjList();
		for (int i=0; i<objList.size(); i++){
			obj = objList.elementAt(i);
			if (obj instanceof DataGraphable){
				dg = (DataGraphable)obj;
				showTable(dg.getDataStore());
			}
		}
	}
	
	public void showTable(DataStore dataStore)
	{
		if (tableDialog == null){
			tableDialog = new JDialog();
			tableDialog.setTitle("Table of Values");
		}
		
		DataTableEditor tableEx = new DataTableEditor();
		tableEx.setDataStore(dataStore);

		DataTablePanel tablePanel = tableEx.getTablePanel();
		
		//Make the columns the right color -- Cell renderer stuff
		DataGraphableTableCellColor colorModel = new DataGraphableTableCellColor(tablePanel.getTableModel().getDataColumns());
		colorModel.setDataGraphableList(graph.getObjList());
		DataTableCellRenderer cellRenderer = new DataTableCellRenderer(); 
		cellRenderer.setTableCellColorModel(colorModel);
		tablePanel.getTable().setDefaultRenderer(Object.class, cellRenderer);
		//
		
		//tablePanel.setPreferredSize(new Dimension(300,300));
		tableDialog.getContentPane().add(tableEx);
		tableDialog.setSize(400, 300);
		//dialog.invalidate();
		tableDialog.pack();
		tableDialog.show();
	}
	
	class InputPanel extends JPanel
		implements ActionListener
	{
		public InputPanel(boolean connect, boolean cross)
		{
			connectPointsBox = new JCheckBox("Connect points", connect);
			connectPointsBox.setActionCommand("connect");
			connectPointsBox.addActionListener(this);
			
			showCrossBox = new JCheckBox("Show cross", cross);
			showCrossBox.setActionCommand("cross");
			showCrossBox.addActionListener(this);
			
			JButton b = new JButton("Clear");
			b.setActionCommand("clear");
			b.addActionListener(this);
			
			JButton b2 = new JButton("Show Table");
			b2.setActionCommand("table");
			b2.addActionListener(this);
			
			add(connectPointsBox);
			add(showCrossBox);
			add(b);
			add(b2);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e)
		{
			String strAction = e.getActionCommand();
			
			if (strAction.equals("connect")){
				setConnectPoints(connectPointsBox.isSelected());
			}
			else if (strAction.equals("cross")){
				setShowCrossPoint(showCrossBox.isSelected());
			}
			else if (strAction.equals("clear")){
				clearValues();
			}
			else if (strAction.equals("table")){
				showTable();
			}
		}
	}
	/**
	 * @return Returns the graph.
	 */
	public DataGraph getGraph()
	{
		return graph;
	}
}
