/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-03-15 03:17:59 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;

import org.concord.data.stream.PointsDataStore;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.framework.data.stream.DataStore;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.util.ui.ResourceLoader;
import org.concord.swing.SelectableAction;


/**
 * DrawDataGraphableAction
 * Class name and description
 *
 * Date created: Mar 14, 2005
 *
 * @author imoncada<p>
 *
 */
public class DrawDataGraphableAction extends SelectableAction
{
	protected ControllableDataGraphable dataGraphable;
	protected GraphableList objList;
	
	/**
	 * 
	 */
	public DrawDataGraphableAction()
	{
		super();
		setName("Draw");
	}

	/**
	 * Sets the name of the icon
	 * (Equivalent to the Action.NAME property)
	 * @param name
	 */
	public void setName(String name)
	{
		putValue(Action.NAME, name);
	}

	/**
	 * Sets the icon of the action given the url
	 */
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
	
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		
		if (isSelected()){

			if (dataGraphable == null){
				
				createDefaultDataGraphable();
			}
			
			dataGraphable.setDragMode(ControllableDataGraphable.DRAGMODE_ADDMULTIPLEPOINTS);
		}
		else{
			
			if (dataGraphable != null){
				
				dataGraphable.setDragMode(ControllableDataGraphable.DRAGMODE_NONE);
			}
		}
	}
	
	/**
	 * 
	 */
	private void createDefaultDataGraphable()
	{
		if (objList == null) return;
		
		ControllableDataGraphable dg = new ControllableDataGraphable();
		DataStore points = new PointsDataStore();		
		
		dg.setDataStore(points, 0, 1);
		dg.setConnectPoints(true);
		dg.setLineType(ControllableDataGraphable.LINETYPE_FUNCTION);
		objList.add(dg);
		
		setDataGraphable(dg);
	}

	/**
	 * @return Returns the dataGraphable.
	 */
	public ControllableDataGraphable getDataGraphable()
	{
		return dataGraphable;
	}
	
	/**
	 * @param dataGraphable The dataGraphable to set.
	 */
	public void setDataGraphable(ControllableDataGraphable dataGraphable)
	{
		this.dataGraphable = dataGraphable;
	}
	
	/**
	 * @return Returns the objList.
	 */
	public GraphableList getGraphableList()
	{
		return objList;
	}
	
	/**
	 * @param objList The objList to set.
	 */
	public void setGraphableList(GraphableList objList)
	{
		this.objList = objList;
	}
}
