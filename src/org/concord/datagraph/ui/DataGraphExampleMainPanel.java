/*
 * Last modification information:
 * $Revision: 1.2 $
 * $Date: 2004-09-02 16:27:28 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;

import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DefaultDataProducer;
import org.concord.data.ui.DataTableCellRenderer;
import org.concord.data.ui.DataTableModel;
import org.concord.data.ui.DataTablePanel;
import org.concord.data.ui.TableCellColorModel;

/**
 * DataGraphExampleMainPanel
 * Main class that illustrates the data graph
 *
 * Date created: Aug 19, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphExampleMainPanel extends JPanel
{
	Timer timer;
	MultipleDataGraph graph;
	
	DataProducer dp1;
	DataProducer dp2;
	DataProducer dp3;
	DataProducer dp4;
	DataProducer dp5;
	
	JButton startButton;
	JButton stopButton;
	JButton resetButton;
	
	JTable dataTable;
	DataTablePanel tablePanel;
	
	float t;
	
	public DataGraphExampleMainPanel()
	{
		timer = new Timer(100, timerListener);	
		
		graph = new MultipleDataGraph(4);
		
		MultipleGrid2D mgrid = (MultipleGrid2D)graph.getGrid();
		mgrid.getXGrid(1).setAxisLabel("Time (s)");
		mgrid.getXGrid(2).setAxisLabel("Time (s)");
		mgrid.getXGrid(3).setAxisLabel("Time (s)");
		mgrid.getXGrid(4).setAxisLabel("Time (s)");
		
		graph.getGrid().getYGrid().setAxisLabel("Y Value");
		
		dp1 = createNewDataProducer();
		dp2 = createNewDataProducer();
		dp3 = createNewDataProducer();
		dp4 = createNewDataProducer();
		dp5 = createNewDataProducer();
		
		graph.addDataProducer(dp1,0);
		graph.addDataProducer(dp2,0);
		graph.addDataProducer(dp3,1);
		graph.addDataProducer(dp4,1);
		graph.addDataProducer(dp5,2);
		graph.getGraphable(dp1).setColor(Color.red);
		graph.getGraphable(dp2).setColor(Color.blue);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		setLayout(new BorderLayout());

		mainPanel.add(graph);
		
		tablePanel = new DataTablePanel();
		
		DataTableModel tableModel = tablePanel.getTableModel();
		
		//tableModel.addDataStore(graph.getGraphable(dp1));
		//tableModel.addDataStore(graph.getGraphable(dp2));
		//tableModel.addDataStore(graph.getGraphable(dp3));
		//tableModel.addDataStore(graph.getGraphable(dp4));
		//tableModel.addDataStore(graph.getGraphable(dp5));
		
		tableModel.addDataColumn(graph.getGraphable(dp1), 0);
		tableModel.addDataColumn(graph.getGraphable(dp1), 1);
		tableModel.addDataColumn(graph.getGraphable(dp2), 1);
		tableModel.addDataColumn(graph.getGraphable(dp3), 1);
		tableModel.addDataColumn(graph.getGraphable(dp4), 1);
		tableModel.addDataColumn(graph.getGraphable(dp5), 1, "Ha! the last one", null);
		
		tableModel.setDataStep(5);
		
		dataTable = tablePanel.getTable();
		//dataTable.setPreferredSize(new Dimension(500,300));
//		dataTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
		
		//JScrollPane dataTableScrollpane = new JScrollPane(dataTable);
		//dataTableScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//tablePanel.revalidate();
		
//		tablePanel.getScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//////////////////////////////////////////////////////////////////////////////
		//JFrame scroll = new JFrame();
		//scroll.getContentPane().add(tablePanel);
		//scroll.setSize(500,300);
		//scroll.show();
		///////////////////////////////////////////////////////////////////////////
		
		//colorModel = new DefaultTableCellColorModel();
		//((DefaultTableCellColorModel)colorModel).setBackgroundColor(Color.pink, 1, 1, true, false);

		//Cell renderer stuff
		TableCellColorModel colorModel;
		colorModel = new DataGraphableTableCellColor(tableModel.getDataColumns());
		DataTableCellRenderer cellRenderer = new DataTableCellRenderer(); 
		((DataGraphableTableCellColor)colorModel).setColorColumn(new Color(230,230,200), Color.black, null, 0);		
		((DataGraphableTableCellColor)colorModel).setBackgroundColorColumn(null, 0, true, false);		
		cellRenderer.setTableCellColorModel(colorModel);
		dataTable.setDefaultRenderer(Object.class, cellRenderer);
		//
		
		mainPanel.add(tablePanel);	

		add(mainPanel);
		
		startButton = new JButton("Start");
		startButton.addActionListener(buttonListener);
		startButton.setActionCommand("start");
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(buttonListener);
		stopButton.setActionCommand("stop");
		stopButton.setEnabled(false);
		
		resetButton = new JButton("Restart");
		resetButton.addActionListener(buttonListener);
		resetButton.setActionCommand("reset");
		resetButton.setEnabled(false);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(resetButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		t = 0;
	}
	
	ActionListener buttonListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			String strAction = ((AbstractButton)e.getSource()).getActionCommand();
			if (strAction.equals("start")){
				timer.start();
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
				resetButton.setEnabled(true);
			}
			else if (strAction.equals("stop")){
				timer.stop();
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
				resetButton.setEnabled(true);
				
				dataTable.repaint();
				((DataTableModel)dataTable.getModel()).printData();
			}
			else if (strAction.equals("reset")){
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
				resetButton.setEnabled(false);
				timer.stop();
				graph.reset();
				t = 0;
			}
		}
	};

	ActionListener timerListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			//System.err.println(t);
			
			((DefaultDataProducer)dp1).addValue((float)Math.sin(t));
			((DefaultDataProducer)dp2).addValue((float)Math.cos(4*t)-1.5f);
			((DefaultDataProducer)dp3).addValue(t);
			((DefaultDataProducer)dp4).addValue(4-t);
			((DefaultDataProducer)dp5).addValue(t*t - 5*t + 2);			
			
			//weird:
			//((DefaultDataProducer)dp4).addValue((float)Math.cos(t));
			//((DefaultDataProducer)dp4).addValue(6-t);
			
			//dataTable.setPreferredSize(new Dimension(500,(int)(t*40)));
//			dataTable.revalidate();
			//dataTable.updateUI();
//			tablePanel.getScrollPane().getVerticalScrollBar().setValue(tablePanel.getScrollPane().getVerticalScrollBar().getMaximum());
//			tablePanel.getTable().scrollRectToVisible(tablePanel.getTable().getCellRect(tablePanel.getTable().getRowCount()-1, -1, true));
			
			t+=0.1;
		}
	};

	public DataProducer createNewDataProducer()
	{
		DataProducer dp = new DefaultDataProducer(0.1f);
		dp.getDataDescription().getChannelDescription().setPrecision(2);
		dp.getDataDescription().getChannelDescription().setName("x value");
		return dp;
	}
	
	public static void main(String[] args)
	{
		final JFrame frame = new JFrame();
		DataGraphExampleMainPanel p = new DataGraphExampleMainPanel();
		frame.getContentPane().add(p);
		frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.show();		
	}
}
