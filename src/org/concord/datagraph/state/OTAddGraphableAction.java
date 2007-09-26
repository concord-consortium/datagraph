/*
 * Last modification information:
 * $Revision: 1.3 $
 * $Date: 2007-09-26 18:39:31 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2007 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.Color;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DefaultDataStore;
import org.concord.framework.otrunk.DefaultOTObject;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTResourceSchema;
import org.concord.framework.otrunk.view.OTAction;
//import org.concord.otrunk.view.OTObjectListViewer;
import org.concord.framework.otrunk.view.OTActionContext;

/**
 * OTAddGraphableAction
 * Class name and description
 *
 * Date created: Mar 5, 2007
 *
 * @author Ingrid Moncada<p>
 *
 */
public class OTAddGraphableAction extends DefaultOTObject
	implements OTAction
{
	public static interface ResourceSchema extends OTResourceSchema
	{
		public OTDataGraph getDataGraph();
		public void setDataGraph(OTDataGraph dg);
		
		public OTObjectList getGraphablesToAdd();
		public void setGraphablesToAdd(OTObjectList list);
	}
	protected ResourceSchema resources;
	
	public OTAddGraphableAction(ResourceSchema resources)
	{
		super(resources);
		this.resources = (ResourceSchema)resources;
	}
	
	/**
	 * @see org.concord.framework.otrunk.view.OTAction#doAction(org.concord.framework.otrunk.view.OTActionContext)
	 */
	public void doAction(OTActionContext context)
	{
		OTDataGraph dataGraph = resources.getDataGraph();
		
		if (dataGraph.getGraphables().size() > 0){
			//Test: modify the first OT graphable
			OTDataGraphable otGraphable = (OTDataGraphable)dataGraph.getGraphables().get(0);
			otGraphable.setColor((int)(Math.random()*16777216));
			
			return;
		}
		
		//XXX: Add a default data graphable for now
		OTDataGraphable otDataG = getGraphableToAdd();
		
		dataGraph.getGraphables().add(otDataG);
	}

	/**
	 * @return
	 */
	private OTDataGraphable getGraphableToAdd()
	{
		//XXX: Add the first data graphable for now
		OTObjectList otObjList = resources.getGraphablesToAdd();
		OTDataGraphable dataG = (OTDataGraphable)otObjList.get(0);
		return dataG;
	}

	/**
	 * Shows a dialog with the list of possible objects to insert and lets the user choose
	 * selected object is guaranteed to be not null
	 * 
	 * @return OT Object selected by the user
	 */
	private OTObject getObjectToInsertFromUser()
	{
		OTObject otObj = null;
		
//		otObj = OTObjectListViewer.showDialog(null, "Choose data set to add", frameManager, viewFactory, 
//				resources.getGraphablesToAdd(), null, true, true);		//Last parameter is null because we don't have an ot object service
		
		return otObj;
	}

	/**
	 * @see org.concord.framework.otrunk.view.OTAction#getActionText()
	 */
	public String getActionText()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
