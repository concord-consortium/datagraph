/*
 * Last modification information:
 * $Revision: 1.3 $
 * $Date: 2004-10-26 17:33:45 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;

import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DefaultDataProducer;
import org.concord.framework.data.stream.DefaultMultipleDataProducer;
import org.concord.graph.ui.Grid2D;
import org.concord.data.ui.DataTableCellRenderer;
import org.concord.data.ui.DataTableModel;
import org.concord.data.ui.DataTablePanel;
import org.concord.data.ui.DataValueLabel;
import org.concord.data.ui.TableCellColorModel;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.DataGraphableTableCellColor;

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
	DataProducer dp4;
	
	JButton startButton;
	JButton stopButton;
	JButton resetButton;
	
	JTable dataTable;
	DataTablePanel tablePanel;
	
	float t;
	float r;
	
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
		
		graph.addDataProducer(dp1);
		graph.addDataProducer(dp2);
		graph.getGraphable(dp1).setColor(Color.red);
		graph.getGraphable(dp2).setColor(Color.blue);
		
		dp3 = new DefaultMultipleDataProducer();
		dp3.getDataDescription().setDataType(DataStreamDescription.DATA_SERIES);
		DataGraphable dg3 = graph.createDataGraphable(dp3, 0, 1);
		dg3.setColor(255,128,0);
		graph.addDataGraphable(dg3);

		dp4 = new DefaultMultipleDataProducer();
		dp4.getDataDescription().setDataType(DataStreamDescription.DATA_SERIES);
		dp4.getDataDescription().setChannelDescription(new DataChannelDescription(), 0);
		dp4.getDataDescription().getChannelDescription(0).setPrecision(2);
		dp4.getDataDescription().getChannelDescription(0).setName("dark blue x");
		dp4.getDataDescription().setChannelDescription(new DataChannelDescription(), 1);
		dp4.getDataDescription().getChannelDescription(1).setPrecision(2);
		dp4.getDataDescription().getChannelDescription(1).setName("dark blue y");
		DataGraphable dg4 = graph.createDataGraphable(dp4, 0, 1);
		dg4.setColor(0,0,150);
		dg4.setConnectPoints(false);
		dg4.setLineWidth(3);
		graph.addDataGraphable(dg4);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		setLayout(new BorderLayout());

		//graph.setPreferredSize(new Dimension(100,100));
		mainPanel.add(graph);
		
		tablePanel = new DataTablePanel();
		
		DataTableModel tableModel = tablePanel.getTableModel();
		
		//tableModel.addDataStore(graph.getGraphable(dp1));
		//tableModel.addDataStore(graph.getGraphable(dp2));
		//tableModel.addDataStore(graph.getGraphable(dp3));
		//tableModel.addDataStore(graph.getGraphable(dp4));
		//tableModel.addDataStore(graph.getGraphable(dp5));
		
		//tableModel.addDataStore(graph.getGraphable(dp1));
		tableModel.addDataColumn(graph.getGraphable(dp1).getDataStore(), -1);
		tableModel.addDataColumn(graph.getGraphable(dp1).getDataStore(), 0);
		tableModel.addDataColumn(graph.getGraphable(dp2).getDataStore(), 0);
		//tableModel.addDataStore(graph.getGraphable(dp2).getDataStore());
		
		tableModel.addDataStore(dg3);
		tableModel.addDataStore(dg4);
	
//		tableModel.setDataStep(5);
		
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
		colorModel = new DataGraphableTableCellColor(tableModel.getDataColumns(), graph.getObjList());
		DataTableCellRenderer cellRenderer = new DataTableCellRenderer(); 
		((DataGraphableTableCellColor)colorModel).setColorColumn(new Color(230,230,200), Color.black, null, 0);		
		((DataGraphableTableCellColor)colorModel).setBackgroundColorColumn(null, 0, true, false);		
		cellRenderer.setTableCellColorModel(colorModel);
		dataTable.setDefaultRenderer(Object.class, cellRenderer);
		//
		
		tablePanel.setPreferredSize(new Dimension(100,300));
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
		
		//Data value labels
		JPanel labelPanel = new JPanel();
		
		DataValueLabel valLabel;
		
		labelPanel.add(new JLabel("Red Y Value: "));
		
		valLabel = new DataValueLabel();
		valLabel.setDataProducer(dp1, 0);
		valLabel.setBackground(Color.red);
		valLabel.setOpaque(true);
		labelPanel.add(valLabel);

		labelPanel.add(new JLabel("Blue Y Value: "));
		
		valLabel = new DataValueLabel();
		valLabel.setDataProducer(dp2, 0);
		valLabel.setBackground(Color.blue);
		valLabel.setOpaque(true);
		labelPanel.add(valLabel);
		
		valLabel = new DataValueLabel();
		valLabel.setDataProducer(dp3, 0);
		valLabel.setBackground(Color.orange);
		valLabel.setOpaque(true);
		labelPanel.add(valLabel);
		
		valLabel = new DataValueLabel();
		valLabel.setDataProducer(dp3, 1);
		valLabel.setBackground(Color.yellow);
		valLabel.setOpaque(true);
		labelPanel.add(valLabel);
		
		valLabel = new DataValueLabel();
		valLabel.setDataProducer(dp4, 0);
		valLabel.setBackground(Color.green);
		valLabel.setOpaque(true);
		labelPanel.add(valLabel);
		
		valLabel = new DataValueLabel();
		valLabel.setDataProducer(dp4, 1);
		valLabel.setBackground(Color.cyan);
		valLabel.setOpaque(true);
		labelPanel.add(valLabel);
		
		mainPanel.add(labelPanel);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		t = 0;
		r = 4;
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
				r = 4;
			}
		}
	};

	ActionListener timerListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			System.err.println(t+" "+(float)Math.sin(t));
			
			((DefaultDataProducer)dp1).addValue((float)Math.sin(t));
			((DefaultDataProducer)dp2).addValue((float)Math.cos(4*t)-1.5f);
			
			float vals3[], vals4[];
			
			vals3 = new float[3];
			vals3[0] = (float)(r * Math.cos(2*t));
			vals3[1] = (float)(r * Math.sin(2*t));
			vals3[2] = (float)(r);
			((DefaultMultipleDataProducer)dp3).addValues(vals3);

			vals4 = new float[2];
			vals4[0] = (float)(r * Math.cos(t));
			vals4[1] = (float)(r * Math.sin(t));			
			((DefaultMultipleDataProducer)dp4).addValues(vals4, false);
			
			vals4[0] = (float)(r * Math.cos(t)+1);
			vals4[1] = (float)(r * Math.sin(t)+1);			
			((DefaultMultipleDataProducer)dp4).addValues(vals4, true);
			
			t+=0.1;
			r-=0.05;
		}
	};

	public DataProducer createNewDataProducer()
	{
		DataProducer dp;
		
		dp = new DefaultDataProducer(0.1f);

		dp.getDataDescription().addChannelDescription(new DataChannelDescription());
		
		dp.getDataDescription().getChannelDescription(0).setPrecision(2);
		dp.getDataDescription().getChannelDescription(0).setName("x value");
		dp.getDataDescription().getChannelDescription(1).setPrecision(2);
		dp.getDataDescription().getChannelDescription(1).setName("y value");
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
	/**
	 * @return Returns the graph.
	 */
	public DataGraph getGraph()
	{
		return graph;
	}
}
