

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
 * $Revision: 1.3 $
 * $Date: 2005-04-17 23:35:50 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.concord.graph.engine.MathUtil;
import org.concord.graph.ui.SingleAxisGrid;


/**
 * DataGraphAxisLimitsPanel
 * Class name and description
 *
 * Date created: Sep 14, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphAxisLimitsPanel extends JPanel
	implements ActionListener
{
	protected DataGraph littleGraph;
	protected JTextField xMinText;
	protected JTextField xMaxText;
	protected JTextField yMinText;
	protected JTextField yMaxText;
	protected JTextField yTitleText;
	protected JTextField xTitleText;

	public DataGraphAxisLimitsPanel()
	{
		super();
		
		JLabel yTitle = new JLabel("Y Axis");
		yTitleText = new JTextField();
		yTitleText.setBackground(yTitle.getBackground());
		yTitleText.setBorder(null);
		JLabel yMinTitle = new JLabel("Min:");
		JLabel yMaxTitle = new JLabel("Max:");
		yMinText = new JTextField();
		yMaxText = new JTextField();

		JLabel xTitle = new JLabel("X Axis");
		xTitleText = new JTextField();
		xTitleText.setBackground(xTitle.getBackground());
		xTitleText.setBorder(null);
		JLabel xMinTitle = new JLabel("Min:");
		JLabel xMaxTitle = new JLabel("Max:");
		xMinText = new JTextField();
		xMaxText = new JTextField();

		/////////////////////////////////
		//null layout
		setLayout(null);
		
		//
		Dimension titleDimension = new Dimension(50, 20);
		Dimension textTitleDimension = new Dimension(40, 20);
		Dimension textDimension = new Dimension(40, 20);
		Dimension labelTextDimension = new Dimension(200, 20);
		
		yTitle.setSize(titleDimension);
		yTitleText.setSize(labelTextDimension);
		yMinTitle.setSize(textTitleDimension);
		yMaxTitle.setSize(textTitleDimension);
		yMinText.setSize(textDimension);
		yMaxText.setSize(textDimension);
		
		xTitle.setSize(titleDimension);
		xTitleText.setSize(labelTextDimension);
		xMinTitle.setSize(textTitleDimension);
		xMaxTitle.setSize(textTitleDimension);
		xMinText.setSize(textDimension);
		xMaxText.setSize(textDimension);
		
		//
		
		//
		yTitle.setLocation(10, 0);
		yTitleText.setLocation(yTitle.getX() + yTitle.getWidth() + 5, yTitle.getY());				
		yMaxTitle.setLocation(yTitle.getX(), yTitle.getY() + 20);
		yMaxText.setLocation(yMaxTitle.getX() + yMaxTitle.getWidth() + 5, yMaxTitle.getY());
		yMinTitle.setLocation(yTitle.getX(), yTitle.getY() + 200);
		yMinText.setLocation(yMinTitle.getX() + yMinTitle.getWidth() + 5, yMinTitle.getY());
		
		xTitle.setLocation(80, 260);
		xTitleText.setLocation(xTitle.getX() + xTitle.getWidth() + 5, xTitle.getY());				
		xMinTitle.setLocation(xTitle.getX(), xTitle.getY() + 20);
		xMinText.setLocation(xMinTitle.getX() + xMinTitle.getWidth() + 5, xMinTitle.getY());
		xMaxTitle.setLocation(xTitle.getX() + 200, xTitle.getY() + 20);
		xMaxText.setLocation(xMaxTitle.getX() + xMaxTitle.getWidth() + 5, xMaxTitle.getY());
		//
		
		add(yTitle);
		add(yTitleText);
		add(yMinTitle);
		add(yMaxTitle);
		add(yMinText);
		add(yMaxText);
		
		add(xTitle);
		add(xTitleText);
		add(xMinTitle);
		add(xMaxTitle);
		add(xMinText);
		add(xMaxText);
						
		
		setPreferredSize(new Dimension(380, 300));				
		/////////////////////////////////
							
		//		
		xMinText.addActionListener(this);
		xMaxText.addActionListener(this);
		yMinText.addActionListener(this);
		yMaxText.addActionListener(this);	
		
		xTitleText.addActionListener(this);
		yTitleText.addActionListener(this);
		//
						
		///////////////////////
		//Little graph
		littleGraph = new DataGraph();
						
		littleGraph.getGraph().setOpaque(false);				
		littleGraph.setSize(275, 240);
		littleGraph.setLocation(110, 20);
		littleGraph.getToolBar().setVisible(false);
		littleGraph.getGraph().getDefaultGraphArea().setInsets(new Insets(5,31,34,5));
		
		//Set the limits of the axis
		//setLimitsLittleGraph();
		//
		
		add(littleGraph);
		///////////////////////
		
	}

	protected void setAxisLimitsLittleGraph()
	{			
		double xMin, xMax, yMin, yMax;
		xMin = Double.parseDouble(xMinText.getText());
		xMax = Double.parseDouble(xMaxText.getText());
		yMin = Double.parseDouble(yMinText.getText());
		yMax = Double.parseDouble(yMaxText.getText());
		
		littleGraph.setLimitsAxisWorld(xMin, xMax, yMin, yMax);

		double littlexInterval = 20 / (260 / (xMax - xMin));
		littleGraph.getGrid().getXGrid().setInterval(littleGraph.getGrid().getXGrid().getBestInterval(littlexInterval));	
		
		double littleyInterval = 20 / (230 / (yMax - yMin)); 
		littleGraph.getGrid().getYGrid().setInterval(littleGraph.getGrid().getYGrid().getBestInterval(littleyInterval));	
		
		littleGraph.getGrid().getXGrid().setAxisLabel(xTitleText.getText());
		littleGraph.getGrid().getYGrid().setAxisLabel(yTitleText.getText());
	}

	public void initAxisLimitsFromGraph(DataGraph graph)
	{
		//Labels
		if (graph.getGrid().getYGrid().getShowAxisLabels()){
			yTitleText.setText(graph.getGrid().getYGrid().getAxisLabel());
			yTitleText.setVisible(true);
		}
		else{
			yTitleText.setVisible(false);
		}
		
		if (graph.getGrid().getXGrid().getShowAxisLabels()){
			xTitleText.setText(graph.getGrid().getXGrid().getAxisLabel());
			xTitleText.setVisible(true);
		}
		else{
			xTitleText.setVisible(false);
		}
		//

		//AxisLimits
		double xMin, xMax, yMin, yMax;
		xMin = graph.getMinXAxisWorld();
		xMax = graph.getMaxXAxisWorld();
		yMin = graph.getMinYAxisWorld();
		yMax = graph.getMaxYAxisWorld();
		
		xMinText.setText(Double.toString(MathUtil.roundTenth(xMin, 100)));
		xMaxText.setText(Double.toString(MathUtil.roundTenth(xMax, 100)));
		yMinText.setText(Double.toString(MathUtil.roundTenth(yMin, 100)));
		yMaxText.setText(Double.toString(MathUtil.roundTenth(yMax, 100)));
		
		setAxisLimitsLittleGraph();
		//
	}
	
	public void applyAxisLimitsOnGraph(DataGraph graph)
	{
		double xInterval = graph.getGrid().getXGrid().getInterval();
		double yInterval = graph.getGrid().getYGrid().getInterval();
		double xMin, xMax, yMin, yMax;
		xMin = graph.getMinXAxisWorld();
		xMax = graph.getMaxXAxisWorld();
		yMin = graph.getMinYAxisWorld();
		yMax = graph.getMaxYAxisWorld();
		
		//Set the limits of the axis
		graph.setLimitsAxisWorld( 
				Double.parseDouble(xMinText.getText()), 
				Double.parseDouble(xMaxText.getText()), 
				Double.parseDouble(yMinText.getText()), 
				Double.parseDouble(yMaxText.getText()));
		
		//Set the "best" grid interval 
		double xLines = (xMax - xMin) / xInterval;
		double newXInterval = (graph.getMaxXAxisWorld() - graph.getMinXAxisWorld()) / xLines;
		
		//Better leave the grid interval as an integer, multiple of 2, 5 or 10
		newXInterval = littleGraph.getGrid().getXGrid().getBestInterval(newXInterval);						
		graph.getGrid().getXGrid().setInterval(newXInterval);						
		//
		
		double yLines = (yMax - yMin) / yInterval;
		double newYInterval = (graph.getMaxYAxisWorld() - graph.getMinYAxisWorld()) / yLines;
		
		//Better leave the grid interval as an integer, multiple of 2, 5 or 10
		newYInterval = littleGraph.getGrid().getYGrid().getBestInterval(newYInterval);						
		graph.getGrid().getYGrid().setInterval(newYInterval);
		
		//Labels
		graph.getGrid().getXGrid().setAxisLabel(xTitleText.getText());
		graph.getGrid().getYGrid().setAxisLabel(yTitleText.getText());
	}
	
	public static void main(String[] args)
	{
	}
	
	
	/**
	 * @return Returns the xMaxText.
	 */
	public JTextField getXMaxText()
	{
		return xMaxText;
	}
	/**
	 * @return Returns the xMinText.
	 */
	public JTextField getXMinText()
	{
		return xMinText;
	}
	/**
	 * @return Returns the xTitleText.
	 */
	public JTextField getXTitleText()
	{
		return xTitleText;
	}
	/**
	 * @return Returns the yMaxText.
	 */
	public JTextField getYMaxText()
	{
		return yMaxText;
	}
	/**
	 * @return Returns the yMinText.
	 */
	public JTextField getYMinText()
	{
		return yMinText;
	}
	/**
	 * @return Returns the yTitleText.
	 */
	public JTextField getYTitleText()
	{
		return yTitleText;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		setAxisLimitsLittleGraph();
	}
}
