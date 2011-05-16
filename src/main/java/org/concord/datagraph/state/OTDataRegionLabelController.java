package org.concord.datagraph.state;

import org.concord.datagraph.ui.DataRegionLabel;

public class OTDataRegionLabelController extends OTDataPointLabelController
{
	public final static Class [] realObjectClasses = {DataRegionLabel.class};
	public final static Class otObjectClass = OTDataRegionLabel.class;
	
    @Override
    public void loadRealObject(Object realObject) 
    {
    	// TODO Auto-generated method stub
    	super.loadRealObject(realObject);
    	
    	OTDataRegionLabel resources = (OTDataRegionLabel) otObject;
    	DataRegionLabel l = (DataRegionLabel)realObject;
    	
    	l.setRegion(resources.getX1(), resources.getX2());
    	l.setShowLabel(resources.getShowLabel());
    	l.setShowHighlight(resources.getShowHighlight());
    	l.setOpacity(resources.getOpacity());
    }
    
    @Override
    public void saveRealObject(Object realObject)
	{
    	super.saveRealObject(realObject);
    	
    	OTDataRegionLabel resources = (OTDataRegionLabel) otObject;
    	DataRegionLabel l = (DataRegionLabel)realObject;
    	
		resources.setX1(l.getXLowerBounds());
		resources.setX2(l.getXUpperBounds());
		resources.setShowLabel(l.getShowLabel());
		resources.setShowHighlight(l.getShowHighlight());
		resources.setOpacity(l.getOpacity());
	}
}
