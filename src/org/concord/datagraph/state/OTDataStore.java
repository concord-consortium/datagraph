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


/**
 * PfDataStore
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public interface OTDataStore
	extends OTObject
{
	public String getValues();
	public void setValues(String values);
}
