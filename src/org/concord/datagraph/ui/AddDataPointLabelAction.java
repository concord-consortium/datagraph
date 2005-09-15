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
 * $Revision: 1.7 $
 * $Date: 2005-09-15 14:10:27 $
 * $Author: swang $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.util.EventObject;
import java.util.Vector;

import org.concord.framework.data.stream.DataStore;
import org.concord.graph.engine.Graphable;
import org.concord.graph.engine.GraphableList;
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
	protected DataStore dataStore;
	protected GraphableList dataGraphablesList;
	
	/**
	 * @param gList
	 */
	public AddDataPointLabelAction(GraphableList gList, GraphableList objList)
	{
		super(gList);
		dataGraphablesList = objList;
		//setName("Note");
		setIcon("toolbar_icon_note.gif");
	}

	/**
	 * @see org.concord.graph.util.control.AddLabelAction.createTextLabel
	 */
	protected BoxTextLabel createTextLabel()
	{
		DataPointLabel label = new DataPointLabel(true);
		label.setGraphableList(dataGraphablesList);
		label.setMessage("Data Point");
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
