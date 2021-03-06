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
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import org.concord.framework.data.stream.DataStore;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.examples.GraphWindowToolBar;
import org.concord.graph.util.control.AddLabelAction;
import org.concord.graph.util.ui.BoxTextLabel;


/**
 * AddDataPointLabelAction
 * Class name and description
 *
 * Date created: Mar 5, 2005
 *
 * @author imoncada<p>
 *
 */
public class AddDataPointLabelAction extends AddLabelAction
{
	private static final long serialVersionUID = 1L;
	protected DataStore dataStore;
	protected GraphableList dataGraphablesList;
	private GraphWindowToolBar toolBar;
	private boolean showLabelCoordinates;
	private int labelDecPlaces;
	private boolean fillLabelBackground = true;
	private boolean showInfoLabel = false;
	
	/**
	 * @param gList
	 */
	public AddDataPointLabelAction(GraphableList gList, GraphableList objList)
	{
		this(gList, objList, null);
	}
	
	public AddDataPointLabelAction(GraphableList gList, GraphableList objList, GraphWindowToolBar toolBar) {
		this(gList, objList, toolBar, true, 2, false);
	}

	public AddDataPointLabelAction(GraphableList gList, GraphableList objList, GraphWindowToolBar toolBar, boolean showLabelCoordinates, int labelDecPlaces, boolean showInfoLabel) {
		super(gList);
		dataGraphablesList = objList;
		setIcon("toolbar_icon_note.gif");
		this.toolBar = toolBar;
		this.showLabelCoordinates = showLabelCoordinates;
		this.labelDecPlaces = labelDecPlaces;
		this.showInfoLabel = showInfoLabel;
	}

	/**
	 * @see org.concord.graph.util.control.AddLabelAction.createTextLabel
	 */
	@Override
    protected BoxTextLabel createTextLabel()
	{
		DataPointLabel label = new DataPointLabel(true);
		label.setGraphableList(dataGraphablesList);
		label.setMessage("Data Point");
		label.setShowCoordinates(showLabelCoordinates);
		label.setShowInfoLabel(showInfoLabel);
		label.setCoordinateDecimalPlaces(labelDecPlaces);
		label.setFillBackground(fillLabelBackground);
		if (toolBar != null){
			label.setToolBar(toolBar);
		}
		return label;
	}
	
	/**
	 * @return Returns the dataStore.
	 */
	public DataStore getDataStore()
	{
		return dataStore;
	}
	
	/**
	 * @param dataStore The dataStore to set.
	 */
	public void setDataStore(DataStore dataStore)
	{
		this.dataStore = dataStore;
	}

    public void setFillLabelBackground(boolean fillLabelBackground) {
        this.fillLabelBackground = fillLabelBackground;
    }
	
	/*
	public void graphableRemoved(EventObject e) {
		Object obj = e.getSource();
		if(obj instanceof DataPointLabel) {
			DataPointLabel dataPointLabel = (DataPointLabel)obj;
			if(dataGraphablesList.contains(dataPointLabel))
				dataGraphablesList.remove(dataPointLabel);
			if(notesList.contains(dataPointLabel))
				notesList.remove(dataPointLabel);
		}
		super.graphableRemoved(e);
	}
	*/
}
