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

package org.concord.datagraph.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.concord.datagraph.engine.DataGraphAutoScaler;
import org.concord.graph.util.ui.ResourceLoader;

/**
 * 
 * @author swang
 *
 */
public class AutoScaleAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	public static final int AUTOSCALE_X = 0;
	public static final int AUTOSCALE_Y = 1;
	public static final int AUTOSCALE_XY = 2;
	public static final int DEFAULT_MARGIN = 10;
	
	int autoScaleMode = AUTOSCALE_XY;
	int margin = DEFAULT_MARGIN;
	
	DataGraph dataGraph;
	
	public AutoScaleAction() {
		this(AUTOSCALE_XY, null);
	}
	
	public AutoScaleAction(DataGraph graph) {
		this(AUTOSCALE_XY, graph);
	}
	
	public AutoScaleAction(int autoScaleMode) {
		this(autoScaleMode, null);
	}
	
	public AutoScaleAction(int autoScaleMode, DataGraph dataGraph) {
		setAutoScaleMode(autoScaleMode);
		setDataGraph(dataGraph);
		setDefaultIcon();
	}
	
	public void setAutoScaleMode(int autoScaleMode) {
		if(autoScaleMode != AUTOSCALE_X && 
				autoScaleMode != AUTOSCALE_Y &&
				autoScaleMode != AUTOSCALE_XY)
			throw new IllegalArgumentException("autoScaleMode exception: " +
					"Must be either AutoScaleAction.AUTOSCALE_X, " +
					"AutoScaleAction.AUTOSCALE_Y, or" +
					"AutoScaleAction.AUTOSCALE_XY");
		this.autoScaleMode = autoScaleMode;
	}
	
	public void setDataGraph(DataGraph graph) {
		if(graph == null) return;
		this.dataGraph = graph;
	}

	public void actionPerformed(ActionEvent e) {
		autoScale();
	}
	
	public void autoScale() {
		if(dataGraph == null) return;
		
		DataGraphAutoScaler autoScaler = dataGraph.getAutoScaler();

		autoScaler.setAutoScaleX(autoScaleMode == AUTOSCALE_X 
				|| autoScaleMode == AUTOSCALE_XY);
		autoScaler.setAutoScaleY(autoScaleMode == AUTOSCALE_Y
				|| autoScaleMode == AUTOSCALE_XY);

		autoScaler.handleUpdate();
	}
	
	private void setDefaultIcon() {
		if(autoScaleMode == AUTOSCALE_X) {
			setIcon("auto-scale-x.png");
		} else if(autoScaleMode == AUTOSCALE_Y) {
			setIcon("auto-scale-y.png");
		} else if(autoScaleMode == AUTOSCALE_XY) {
			setIcon("auto-scale.png");
		}
	}
	
	public void setIcon(String strURL)
	{
		setIcon(ResourceLoader.getImageIcon(strURL, ""));
	}
	
	/**
	 * Sets the icon of the action
	 * (Equivalent to the Action.SMALL_ICON property)
	 * @param icon	icon of the action
	 */
	public void setIcon(Icon icon)
	{
		putValue(Action.SMALL_ICON, icon);
	}
}
