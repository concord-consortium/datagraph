/*
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import javax.swing.JComponent;

import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataCollectorView
    implements OTObjectView
{
    OTObjectView view;
    
    /**
     * 
     */
    public OTDataCollectorView(OTDataCollector dataCollector, OTViewContainer container)
    {
        if(dataCollector.getSingleValue()) {
            view = new SingleValueDataView(dataCollector);
        }
        else {
            view = new DataCollectorView(dataCollector);
        }
    }

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTObjectView#getComponent(boolean)
     */
    public JComponent getComponent(boolean editable)
    {
        return view.getComponent(editable);
    }

}
