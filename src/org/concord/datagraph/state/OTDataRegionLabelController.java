package org.concord.datagraph.state;

import org.concord.datagraph.ui.DataPointLabel;
import org.concord.datagraph.ui.DataRegionLabel;

public class OTDataRegionLabelController extends OTDataPointLabelController
{
	public final static Class [] realObjectClasses = {DataRegionLabel.class};
	public final static Class otObjectClass = OTDataRegionLabel.class;
	
    public void loadRealObject(Object realObject) 
    {
    	// TODO Auto-generated method stub
    	super.loadRealObject(realObject);
    	
    	OTDataRegionLabel resources = (OTDataRegionLabel) otObject;
    	DataRegionLabel l = (DataRegionLabel)realObject;
    	
    	l.setRegion(resources.getX1(), resources.getX2());
    }
}
