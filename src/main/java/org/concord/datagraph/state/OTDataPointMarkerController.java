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
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2007-01-08 20:06:15 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import java.awt.Color;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataPointMarker;
import org.concord.graph.util.state.OTGraphableController;


/**
 * PfDataGraphable
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public class OTDataPointMarkerController extends OTGraphableController
{
	public final static Class [] realObjectClasses = {DataPointMarker.class};
	public final static Class otObjectClass = OTDataPointMarker.class;
	
    public void loadRealObject(Object realObject) 
    {
        DataPointMarker marker = (DataPointMarker)realObject;
        OTDataPointMarker otMarker = (OTDataPointMarker) otObject;
        
        // find the correct graphable for this marker
        OTDataGraphable otGraphable = otMarker.getDataGraphable();
        if(otGraphable != null) {
            DataGraphable dg = (DataGraphable)controllerService.getRealObject(otGraphable);
            marker.setDataGraphable(dg);
        }
        
        marker.setColor(new Color(otMarker.getColor()));
        marker.setXValue(otMarker.getX());
        marker.setVisible(otMarker.getVisible());
        marker.setShape(otMarker.getShape());
        marker.setText(otMarker.getText());
    }

    public void saveRealObject(Object realObject) {
        DataPointMarker marker = (DataPointMarker)realObject;
        OTDataPointMarker otMarker = (OTDataPointMarker) otObject;
        
        DataGraphable dg = marker.getDataGraphable();
        if(dg != null) {
            OTDataGraphable otGraphable = (OTDataGraphable)controllerService.getOTObject(dg);
            otMarker.setDataGraphable(otGraphable);
        }
        
        otMarker.setX(marker.getXValue());
        otMarker.setColor(marker.getColor().getRGB());
        otMarker.setVisible(marker.isVisible());
        otMarker.setShape(marker.getShape());
        otMarker.setText(marker.getText());
    }
}
