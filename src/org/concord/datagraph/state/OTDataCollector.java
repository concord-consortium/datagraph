/*
 * Created on Mar 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface OTDataCollector
    extends OTObject
{
    public String getTitle();
    
	public OTDataGraphable getSource();
	
	public OTDataAxis getYDataAxis();

	public OTDataAxis getXDataAxis();
	
	public final static boolean DEFAULT_singleValue = false;
	public boolean getSingleValue();
	
	// if this is null then this doesn't allow saving 
	// named data sets
	public OTObject getDataSetFolder();
	
	public OTObjectList getGraphables();
}
