/*
 * Last modification information:
 * $Revision: 1.6 $
 * $Date: 2004-09-09 22:14:04 $
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
import org.concord.framework.data.stream.DefaultMultipleDataProducer;
import org.concord.graph.ui.Grid2D;
import org.concord.data.ui.DataTableCellRenderer;
import org.concord.data.ui.DataTableModel;
import org.concord.data.ui.DataTablePanel;
import org.concord.data.ui.TableCellColorModel;
import org.concord.datagraph.engine.DataGraphable;

/**
 * DataGraphExampleMainPanel
 * Main class that illustrates the data graph
 *
 * Date created: Aug 19, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphExample2MainPanel extends JPanel
{
	Timer timer;
	DataGraph graph;
	
	DataProducer dp1;
	DataProducer dp2;
	DataProducer dp3;
	
	JButton startButton;
	JButton stopButton;
	JButton resetButton;
	
	JTable dataTable;
	DataTablePanel tablePanel;
	
	float t;
	float r = 4;
	
	public DataGraphExample2MainPanel()
	{
		timer = new Timer(100, timerListener);	
		
		graph = new DataGraph();
		
		Grid2D mgrid = (Grid2D)graph.getGrid();
		mgrid.getXGrid().setAxisLabel("X Axis");
		mgrid.getYGrid().setAxisLabel("Y Axis");
		
		//mgrid.getXGrid().setAxisDrawMode(SingleAxisGrid.ALWAYSVISIBLE);
		//mgrid.getYGrid().setAxisDrawMode(SingleAxisGrid.ALWAYSVISIBLE);
		
		graph.getGraph().getDefaultGraphArea().setOriginCentered(true);

		//graph.setOriginOffsetPercentage(0.2, 0.2);
		
		//graph.setLimitsAxisWorld(6.9, 10.1, -1.1, 5.1);
		
		//graph.setOriginOffsetDisplay(0, 0);
		
		dp1 = createNewDataProducer();
		dp2 = createNewDataProducer();
		
		dp3 = new DefaultMultipleDataProducer();
		DataGraphable dg = graph.createDataGraphable(dp3, 0, 1);
		dg.setColor(255,128,0);
		graph.addDataGraphable(dg);
		
		graph.addDataProducer(dp1);
		graph.addDataProducer(dp2);
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
			
//			((DefaultDataProducer)dp1).addValue((float)Math.sin(t));
//			((DefaultDataProducer)dp2).addValue((float)Math.cos(4*t)-1.5f);
			
			float vals[] = new float[3];
			vals[0] = (float)(r * Math.cos(t));
			vals[1] = (float)(r * Math.sin(t));
			vals[2] = (float)(r);
			
			((DefaultMultipleDataProducer)dp3).addValues(vals);
			
			t+=0.1;
			r-=0.05;
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
		DataGraphExample2MainPanel p = new DataGraphExample2MainPanel();
		frame.getContentPane().add(p);
		frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.show();		
	}
}
