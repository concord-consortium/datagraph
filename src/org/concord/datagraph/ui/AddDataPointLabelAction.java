/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-03-06 06:11:51 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import org.concord.framework.data.stream.DataStore;
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
	
	/**
	 * @param gList
	 */
	public AddDataPointLabelAction(GraphableList gList)
	{
		super(gList);
		setName("Note");
	}

	/**
	 * @see org.concord.graph.util.control.AddLabelAction.createTextLabel
	 */
	protected BoxTextLabel createTextLabel()
	{
		DataPointLabel label = new DataPointLabel(true);
		label.setGraphableList(objList);
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
}
