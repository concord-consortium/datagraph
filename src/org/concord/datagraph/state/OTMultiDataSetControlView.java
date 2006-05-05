/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2006-05-05 16:04:17 $
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
import org.concord.framework.otrunk.view.OTViewContainer;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.event.GraphableListListener;
import org.concord.view.CheckedColorTreeControler;
import org.concord.view.MultiCheckedColorTreeModel;

public class OTMultiDataSetControlView extends JPanel
    implements OTObjectView, DataGraphViewPlugin
{
    OTViewContainer viewContainer;
    Vector graphViews = new Vector();
    MultiCheckedColorTreeModel multiTreeModel;
    CheckedColorTreeControler treeControler;
    
    public void initialize(OTObject otObject, OTViewContainer viewContainer)
    {
        this.viewContainer = viewContainer;
        
        multiTreeModel = new MultiCheckedColorTreeModel();
        
        // for each graph I want to get the view of the graph that is
        // relavent to this view.  If I control the graphs are sub views
        // that is ok, but I don't want to require that. 
        // TODO Auto-generated method stub
    }

    public JComponent getComponent(boolean editable)
    {
        return this;
    }

    public void initialize()
    {
        treeControler = new CheckedColorTreeControler();
        JComponent treeComponent = treeControler.setup(multiTreeModel);
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
