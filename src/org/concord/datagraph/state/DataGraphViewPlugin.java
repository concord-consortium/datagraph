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

public interface DataGraphViewPlugin
{
    public void addDataCollectorView(DataCollectorView view);
    
    /**
     * This will be called after all the data collectors have been
     * added
     *
     */
    public void initialize();
}
