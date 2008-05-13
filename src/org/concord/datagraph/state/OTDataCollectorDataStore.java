package org.concord.datagraph.state;

import org.concord.data.state.OTDataStore;

/**
 * This dataStore is a way of representing particular aspects of a datacollector
 * as a datastore. For example, it can provide a datastore representing the
 * different labels on a graph.
 * 
 * @author sfentress
 *
 */
public interface OTDataCollectorDataStore
    extends OTDataStore
{
	public OTDataCollector getDataCollector();
	public void setDataCollector(OTDataCollector dataCollector);
	
	/**
	 * What aspect of the dataCollector is to be used to create the dataStore.
	 * 0 = dataLabels
	 * 1 = ??
	 * 
	 * @return
	 */
	public int getDataType();
	public void setDataType(int dataType);
	public static int DEFAULT_dataType = 0;
}
