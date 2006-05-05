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

import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectInterface;

public interface OTPluginView
    extends OTObjectInterface
{
    /**
     * This is the object representing the control that is supposed
     * to be plugged in.  A view will be created for this ot object
     * and then that view will be added to the view that contains
     * this plugin.
     * 
     * @return
     */
    public OTObject getControl();
    
    /**
     * This is the location where the control is supposed to be
     * added. 
     *  
     * @return
     */
    public String getLocation();
}
