/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01742
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * END LICENSE */

/*
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import javax.swing.JComponent;

import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.view.AbstractOTJComponentView;
import org.concord.framework.otrunk.view.OTJComponentViewContext;
import org.concord.framework.otrunk.view.OTJComponentViewContextAware;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataCollectorView extends AbstractOTJComponentView implements OTJComponentViewContextAware
{
    AbstractOTJComponentView view;
    OTDataCollector dataCollector;
    boolean multipleGraphableEnabled = false;
	private OTJComponentViewContext jComponentViewContext;
        
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#getComponent(boolean)
     */
    public JComponent getComponent(OTObject otObject)
    {
        this.dataCollector = (OTDataCollector)otObject;
        if(dataCollector.getSingleValue()) {
            view = new SingleValueDataView(dataCollector);
        }
        else {
            view = new DataCollectorView(dataCollector, getControllable());
        }
        
        // We need to intialize the view so it can access it services correctly.
        view.setViewContext(viewContext);
        if (view instanceof OTJComponentViewContextAware){
        	((OTJComponentViewContextAware)view).setOTJComponentViewContext(jComponentViewContext);
        }
        return view.getComponent(otObject);
    }

    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTJComponentView#viewClosed()
     */
    public void viewClosed()
    {
        if(view != null) {
            view.viewClosed();
        }
    }

	public void setOTJComponentViewContext(OTJComponentViewContext viewContext)
    {
	    this.jComponentViewContext = viewContext;
    }
	
	public boolean getControllable()
	{
		return true;
	}
}
