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

/* Author: Edward Burke
 * Based on work by Eric Brown-Munoz
   $Revision: 1.1 $
*/
package org.concord.datagraph.ui;

import java.util.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.graph.engine.*;
import org.concord.graph.event.*;

public class DataGraphTable extends JTable 
{
    private SelectableList graphablesList = null;

    ListSelectionListener listSelectionListener = new ListSelectionListener() 
	{
	    public void valueChanged(ListSelectionEvent e) 
		{
//System.out.println(" @@ ListSelection.valueChanged event " + e.getValueIsAdjusting());
			if (e.getValueIsAdjusting()) return;
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	
			if ( graphablesList != null) {
				int index;
				int fIndex;
				
				if (getSelectionModel().getSelectionMode()==ListSelectionModel.SINGLE_SELECTION){
					index = lsm.getMinSelectionIndex();
					fIndex = graphablesList.getFirstSelectedIndex();
					if (index >= 0 && fIndex!=index) {
					//The table has something selected but it is different from the selection on the list
						graphablesList.select(index);
					} 
					else if (fIndex!=index && fIndex >= 0){
					//The table doesn't have anything selected and
					//it is different from the selection on the list
					//	setSelectionIndexInternal(lsm, fIndex);
					}
				}
				else{
					index = lsm.getLeadSelectionIndex();
					if (index >= 0) {
					//The table has something selected
						graphablesList.select(index);
					} 
				}
			}
			setupColWidth();
	    }
	};

    public DataGraphTable() 
	{
		super();
	
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
				
		setSelectionBackground(Color.white);
		
		//scroll.setViewportView(table);
		setPreferredSize(new Dimension(200, 200));
    }

	private void setupColWidth()
	{
		TableColumn column = getColumnModel().getColumn(0);
		column.setMaxWidth(50);
	}
	
	public void setSelectableList( SelectableList list ) 
	{
		if (graphablesList!=null){
			graphablesList.removeGraphableListListener(graphableListListener);
		}
		
		this.graphablesList = list;

		Renderer cellRenderer = new Renderer();
		cellRenderer.setSelectableList(graphablesList);
		setDefaultRenderer(String.class, cellRenderer);
		
		Model tableModel = new Model(graphablesList);
		setTableModel(tableModel);

		setupColWidth();

		if (graphablesList!=null){
			ListSelectionModel lsm = getSelectionModel();
			int newIndex = graphablesList.getFirstSelectedIndex();
			int index = lsm.getMinSelectionIndex();
			if (newIndex >= 0 && newIndex!=index) {
				addSelectionIndexInternal(lsm, newIndex);
			}
			graphablesList.addGraphableListListener(graphableListListener);
		}
		
    }
    
	GraphableListListener graphableListListener = new GraphableListAdapter()
	{
		public void listGraphableSelected(EventObject e) 
		{
//System.out.println(" T Table receives selected event");
			int newIndex = graphablesList.indexOf((Graphable)e.getSource());
			if (newIndex!=-1){
				ListSelectionModel lsm = getSelectionModel();
				int index = lsm.getMinSelectionIndex();
				//if (index!=newIndex){
					addSelectionIndexInternal(lsm, newIndex);
				//}
			}
		}

		public void listGraphableDeselected(EventObject e) 
		{
//System.out.println(" T Table receives deselected event");
			int newIndex = graphablesList.indexOf((Graphable)e.getSource());
			if (newIndex!=-1){
				ListSelectionModel lsm = getSelectionModel();
				int index = lsm.getMinSelectionIndex();
				//if (index!=newIndex){
					removeSelectionIndexInternal(lsm, newIndex);
				//}
			}
		}
	};

	private void addSelectionIndexInternal(ListSelectionModel lsm, int newIndex)
	{
		lsm.removeListSelectionListener(listSelectionListener);
		//lsm.setLeadSelectionIndex(newIndex);
		lsm.addSelectionInterval(newIndex,newIndex);
		lsm.addListSelectionListener(listSelectionListener);
	}
	
	private void removeSelectionIndexInternal(ListSelectionModel lsm, int newIndex)
	{
		lsm.removeListSelectionListener(listSelectionListener);
		lsm.removeSelectionInterval(newIndex,newIndex);
		lsm.addListSelectionListener(listSelectionListener);
	}
	
    public void setTableModel(Model model) 
	{
    	setModel(model);
    	getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public Model getTableModel() 
	{
		return (Model) getModel();
    }
    
    public class Renderer extends DefaultTableCellRenderer
    {
    	SelectableList graphableList;
    	
        public void setSelectableList( SelectableList list ) 
    	{
    		graphableList = list;
    	}

        /**
         *
         * Returns the default table cell renderer.
         *
    	 * This method is overriden to use the specific colors for each row
    	 * @see javax.swing.table.DefaultTableCellRenderer
         */
    	public Component getTableCellRendererComponent(JTable table, Object value,
                              boolean isSelected, boolean hasFocus, int row, int column) {

    		Color rowColor=null;
    		Color foreColor=null;
    		Color backColor=null;

    		// TODO: figure out how to get this color
    		if (graphableList != null && row < graphableList.size()){
    			Graphable g = (Graphable) graphableList.elementAt(row);
    			if (g instanceof DataGraphable)
    			rowColor = ((DataGraphable) g).getColor();
    		}
    					
    		if (isSelected) {
    			
    			foreColor = rowColor;
    			if(foreColor == null){
    				foreColor = (table.getSelectionForeground() != null) ? table.getSelectionForeground() : table.getForeground();
    			}

    			if (backColor == null){
    				backColor = (table.getSelectionBackground() != null) ? table.getSelectionBackground() : table.getBackground();
    			}
    			
    			super.setBackground(backColor);
    			super.setForeground(foreColor);
    			
    			setFont(table.getFont().deriveFont(Font.BOLD));
    			super.setBorder(BorderFactory.createLineBorder(rowColor));
    			
    		}
    		else {
    			
    			foreColor = rowColor;
    			if(foreColor == null){
    				foreColor = table.getForeground();
    			}
    			if(backColor == null){
    				backColor = table.getBackground();
    			}
    			
    			super.setForeground(foreColor);
    			super.setBackground(backColor);
    	
    			setFont(table.getFont());
    			super.setBorder(noFocusBorder);
    		}		
    		
    		if (hasFocus) {
    		}
    	
    		setValue(value); 
    	
    		// ---- begin optimization to avoid painting background ----
    		Color back = getBackground();
    		boolean colorMatch = (back != null) && ( back.equals(table.getBackground()) ) && table.isOpaque();
    			setOpaque(!colorMatch);
    		// ---- end optimization to aviod painting background ----
    	
    		return this;
    	}
    }
    
    public class Model extends AbstractTableModel 
    {
    	SelectableList graphables;
    	TableCellRenderer cellRenderer;
    	
    	private String colNames[] = {"Visible","Function"};

    	public Model() 
    	{
    	}
    	
    	public Model(SelectableList list ) 
    	{
    		setSelectableList(list);
    	}
    	
    	public void setSelectableList( SelectableList list ) 
    	{
    		graphables = list;
    		
    		if (list==null) return;
    		list.addGraphableListListener(new GraphableListAdapter() 
    		{
    			int index;
    			
    			public void listGraphableAdded(EventObject e) 
    			{		
    				index = graphables.indexOf((Graphable)e.getSource());
    				fireTableRowsInserted(index,index);
    			}
    	
    			public void listGraphableRemoved(EventObject e) 
    			{
    				//The following two lines cause the columns to change size,
    				//so I took them out
    				//index = graphables.indexOf((Graphable)e.getSource());
    				//fireTableRowsDeleted(index,index);
    				
    				fireTableDataChanged();
    			}
    	
    			public void listGraphableChanged(EventObject e) 
    			{
    				//This is necessary to update the table with the info of the graphable
    				//But if also fires a valueChanged(ListSelectionEvent) event on the table
    				//even if the selection is the same.
    				//The handler of this event is in GraphableTable, and it doesn't
    				//do anything if the selected index is the same
    				index = graphables.indexOf((Graphable)e.getSource());
    				fireTableRowsUpdated(index,index);
    			}
    	
    			public void listGraphableSelected(EventObject e) 
    			{
    				//This is not necessary because the Table is listening to selections
    				//fireTableDataChanged();
    			}
    			
    			public void listGraphableDeselected(EventObject e) 
    			{
    				//This is not necessary because the Table is listening to selections
    				//fireTableDataChanged();
    			}
    	
    		});
    	}
    	
    	public int getColumnCount() 
    	{
    		return 2;
    	}
    	
    	public int getRowCount() 
    	{
    		return graphables.size();
    	}
    	
    	public String getColumnName(int column) 
    	{
    		return colNames[column];
    	}
    	
    	public void setColumnName(int column, String name) 
    	{
    		colNames[column] = name;
    		fireTableStructureChanged();
    	}
    	
    	public Object getValueAt(int rowIndex, int columnIndex) 
    	{
    		Object retval = null;
    		DefaultGraphable graphable = (DefaultGraphable)graphables.elementAt(rowIndex);
    		if (graphable==null) return null;
    		switch (columnIndex) {
    			case 0:
    				retval = new Boolean(graphable.isVisible());
    				break;
    			case 1:
    				retval = " " + graphable.getLabel();
    				break;
    		}
    	
    		return retval;
    	}
    	
    	public Class getColumnClass( int columnIndex) 
    	{
    		Class retval = String.class;
    		if (columnIndex == 0 ) {
    			retval = Boolean.class;
    		} 
    		return retval;
    	}
    	
    	public boolean isCellEditable(int rowIndex, int columnIndex) 
    	{
    		boolean retval = false;
    		if (columnIndex == 0) retval = true;
    		return retval;
    	}
    	
    	public void setValueAt( Object aValue, int rowIndex, int columnIndex) 
    	{	
    		if (columnIndex == 0) 
    		{
    			Graphable theOne = (Graphable)graphables.elementAt(rowIndex);
    			theOne.setVisible(((Boolean)aValue).booleanValue());
    		}
    	}
    }
}
