/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-11-10 20:33:39 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.engine;

import java.util.EventObject;

import org.concord.datagraph.ui.DataGraph;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.event.GraphableListListener;


/**
 * DataGraphDaemon
 * Class name and description
 *
 * Date created: Nov 10, 2004
 *
 * @author scott<p>
 *
 */
public abstract class DataGraphDaemon
	implements GraphableListListener
{
	protected GraphableList graphables;
	protected DataGraph graph;
	protected boolean enabled = true;
	
	public abstract void handleUpdate();
	
	/**
	 * @param graphables The graphables to set.
	 */
	public void setGraphables(GraphableList graphables)
	{
		if(this.graphables != null) {
			this.graphables.removeGraphableListListener(this);
		}
		this.graphables = graphables;
		graphables.addGraphableListListener(this);
	}
	
	/**
	 * @return Returns the graph.
	 */
	public DataGraph getGraph()
	{
		return graph;
	}
	
	/**
	 * @param graph The graph to set.
	 */
	public void setGraph(DataGraph graph)
	{
		this.graph = graph;
	}

	
	/**
	 * @param enabled The enabled to set.
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableAdded(java.util.EventObject)
	 */
	public void listGraphableAdded(EventObject e)
	{
		// We ignore these because we iterate the list every
		// time there is an update
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableChanged(java.util.EventObject)
	 */
	public void listGraphableChanged(EventObject e)
	{
		if(!enabled) return;
		
		//remove ourselves as listener to prevent a loop and
		//this will only work all events are thown synchronisly
		graphables.removeGraphableListListener(this);
		
		handleUpdate();

		//add ourselves back
		graphables.addGraphableListListener(this);
	}
	
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableRemoved(java.util.EventObject)
	 */
	public void listGraphableRemoved(EventObject e)
	{
		// We ignore these because we iterate the list every
		// time there is an update
	}	
}

