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
 * $Revision: 1.4 $
 * $Date: 2007-01-24 22:11:22 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.datagraph.ui.DataGraph;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.event.GraphableListListener;
import org.concord.view.CheckedColorTreeControler;
import org.concord.view.MultiCheckedColorTreeModel;

public class OTMultiDataSetControlView extends JPanel
    implements OTObjectView, DataGraphViewPlugin
{
	/**
	 * Not intended to be serialized, just added remove compile warning
	 */
	private static final long serialVersionUID = 1L;
	
    Vector graphViews = new Vector();
    MultiCheckedColorTreeModel multiTreeModel;
    CheckedColorTreeControler treeControler;
    OTMultiDataSetControl otControl;
    
    public JComponent getComponent(OTObject otObject, boolean editable)
    {
        otControl = (OTMultiDataSetControl)otObject;
        
        multiTreeModel = new MultiCheckedColorTreeModel();
        
        // for each graph I want to get the view of the graph that is
        // relavent to this view.  If I control the graphs are sub views
        // that is ok, but I don't want to require that. 
        // TODO Auto-generated method stub

        return this;
    }

    public void initialize()
    {
        treeControler = new CheckedColorTreeControler();
        JComponent treeComponent = treeControler.setup(multiTreeModel,
        		otControl.getShowNew());
        this.setLayout(new BorderLayout());
        add(treeComponent, BorderLayout.CENTER);        
    }
    
    public void viewClosed()
    {
        // TODO Auto-generated method stub

    }

    public void addDataCollectorView(DataCollectorView view)
    {
        DataGraphManager manager = view.getDataGraphManager();

        multiTreeModel.addModel(manager);  
        
        DataGraph dataGraph = manager.getDataGraph();
        GraphableList graphables = dataGraph.getObjList();

        // one thing we have to worry about are double events        
        graphables.addGraphableListListener(new GraphableListListener(){

            public void listGraphableAdded(EventObject e)
            {
                treeControler.refresh();                
            }

            public void listGraphableRemoved(EventObject e)
            {
                treeControler.refresh();
            }

            public void listGraphableChanged(EventObject e)
            {
//                treeControler.refresh();
            }
            
        });
    }
}
