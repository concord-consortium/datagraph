/*
 * Last modification information:
 * $Revision: 1.3 $
 * $Date: 2004-10-26 17:27:25 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;

import org.concord.data.ui.DataColumnDescription;
import org.concord.data.ui.DefaultTableCellColorModel;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataStore;
import org.concord.graph.engine.GraphableList;


/**
 * DataGraphableTableCellColor
 * Class name and description
 *
 * Date created: Sep 1, 2004
 *
 * @author imoncada<p>
 *
 */
public class DataGraphableTableCellColor extends DefaultTableCellColorModel
{
	Vector dataColumnDescriptions;	//List of DataColumnDescription objects 
	Hashtable dataStoreDataGraphables;

	/**
	 * 
	 */
	public DataGraphableTableCellColor()
	{
		this(null,null);
	}

	/**
	 * 
	 */
	public DataGraphableTableCellColor(Vector dataColDescList)
	{
		dataStoreDataGraphables = new Hashtable();
		setDataColumnDescriptionList(dataColDescList);
	}
	
	/**
	 * 
	 */
	public DataGraphableTableCellColor(Vector dataColDescList, GraphableList dataGraphables)
	{
		dataStoreDataGraphables = new Hashtable();
		setDataGraphableList(dataGraphables);
		setDataColumnDescriptionList(dataColDescList);
	}
	
	/**
	 * @see org.concord.data.ui.TableCellColorModel#getBackgroundColor(int, int, boolean)
	 */
	public Color getBackgroundColor(int row, int col, boolean selected, boolean focus)
	{
		Color specificColor = super.getBackgroundColor(row, col, selected, focus);
		if (specificColor != null) return specificColor;
		if (dataColumnDescriptions == null) return null;
		if (selected){
			return null;//new Color(210,210,200);
		}
		return Color.white;
	}

	/**
	 * @see org.concord.data.ui.TableCellColorModel#getForegroundColor(int, int, boolean)
	 */
	public Color getForegroundColor(int row, int col, boolean selected, boolean focus)
	{
		Color specificColor = super.getForegroundColor(row, col, selected, focus);
		if (specificColor != null) return specificColor;
		if (dataColumnDescriptions == null) return null;
		DataColumnDescription dcol = (DataColumnDescription)dataColumnDescriptions.elementAt(col);
		DataStore ds = dcol.getDataStore();
		if (ds instanceof DataGraphable){
			return ((DataGraphable)ds).getColor();
		}
		else{
			DataGraphable dg = (DataGraphable)dataStoreDataGraphables.get(ds);
			if (dg != null){
				return dg.getColor();
			}
			else{
				return dcol.getColor();
			}
		}
	}

	/**
	 * @see org.concord.data.ui.TableCellColorModel#getBorderColor(int, int, boolean)
	 */
	public Color getBorderColor(int row, int col, boolean selected, boolean focus)
	{
		Color specificColor = super.getBorderColor(row, col, selected, focus);
		if (specificColor != null) return specificColor;
		if (selected && focus){
			return getForegroundColor(row, col, selected, focus);
		}
		else{
			return null;
		}
	}

	/**
	 * @return Returns the dataGraphable.
	 */
	public Vector getDataGraphableList()
	{
		return dataColumnDescriptions;
	}
	
	/**
	 * @param dataGraphable The dataGraphable to set.
	 */
	public void setDataGraphableList(GraphableList dataGraphableList)
	{
		updateDataStoreDataGraphables(dataGraphableList);
	}
	
	/**
	 * 
	 */
	private void updateDataStoreDataGraphables(GraphableList dataGraphableList)
	{
		DataGraphable dg;
		for (int i=0; i < dataGraphableList.size(); i++){
			Object obj = dataGraphableList.elementAt(i);
			if (obj instanceof DataGraphable){
				dg = (DataGraphable)obj;
				dataStoreDataGraphables.put(dg.getDataStore(), dg);
			}
		}
	}

	/**
	 * @param dataGraphable The dataGraphable to set.
	 */
	public void setDataColumnDescriptionList(Vector dataColDescList)
	{
		this.dataColumnDescriptions = dataColDescList;
	}
}
