package org.concord.datagraph.ui;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.graph.engine.GraphableList;

/**
 * This is a common interface for any data graphables that are linked to other 
 * data graphables, and also linked to a graphable list.
 * 
 * @author scott
 *
 */
public interface DataAnnotation {
	public DataGraphable getDataGraphable();
	public void setDataGraphable(DataGraphable dataGraphable);
	
	public void setGraphableList(GraphableList gList);
}
