/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-04-11 23:21:25 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;

/**
 * OTMultiDataGraph
 * Class name and description
 *
 * Date created: Apr 11, 2005
 *
 * @author scott<p>
 *
 */
public interface OTMultiDataGraph extends OTObjectInterface
{
    public OTObjectList getGraphs();
    
    public int getRows();
    public int getColumns();
}
