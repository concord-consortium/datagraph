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
 * $Revision: 1.3 $
 * $Date: 2005-08-04 21:46:08 $
 * $Author: maven $
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
	
	public abstract void handleUpdate(EventObject e);
	
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
		
		handleUpdate(e);

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

