/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-08-30 21:08:33 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;


/**
 * OTDataBarGraphable
 * Class name and description
 *
 * Date created: Aug 30, 2007
 *
 * @author imoncada<p>
 *
 */
public interface OTDataBarGraphable
	extends OTDataGraphable
{
	public void setMaxBarsToSelect(int n);
	public int getMaxBarsToSelect();
}
