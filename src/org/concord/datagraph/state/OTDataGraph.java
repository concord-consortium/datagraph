/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-12-06 03:26:56 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;


/**
 * PfDataGraph
 * Class name and description
 *
 * Date created: Oct 27, 2004
 *
 * @author scott<p>
 *
 */
public interface OTDataGraph extends OTObject
{
	public OTObjectList getDataGraphables();
	
	public OTDataAxis getYDataAxis();
	public void setYDataAxis(OTDataAxis axis);

	public OTDataAxis getXDataAxis();
	public void setXDataAxis(OTDataAxis axis);
	
}
