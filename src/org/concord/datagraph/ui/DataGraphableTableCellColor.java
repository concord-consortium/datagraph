

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
 * $Revision: 1.4 $
 * $Date: 2004-11-12 21:18:04 $
 * $Author: eblack $
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
