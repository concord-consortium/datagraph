package org.concord.datagraph.state;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataPointRuler;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.graph.util.state.OTPointTextLabel;
import org.concord.graph.util.ui.BoxTextLabel;

public class OTDataPointRuler  extends OTPointTextLabel
{
	public static interface ResourceSchema extends OTPointTextLabel.ResourceSchema
	{	
		public OTDataGraphable getDataGraphable();
		public void setDataGraphable(OTDataGraphable b);
	}

	private ResourceSchema resources;
	
	/**
	 * 
	 */
	public OTDataPointRuler(ResourceSchema resources)
	{
		super(resources);
		this.resources = resources;
	}
	
    public OTDataGraphable getDataGraphable()
    {
        return resources.getDataGraphable();
    }
    
	/**
	 * @see org.concord.graph.util.state.OTPointTextLabel#createNewWrappedObject()
	 */
	protected BoxTextLabel createNewWrappedObject()
	{
		DataPointRuler l = new DataPointRuler();

		// we have to rely on the caller that is creating this wrapped
		// object to initialize the dataGraphable of the label.  
		// because the same OTDataGraphable can be displayed in more 
		// than one place at a time the real DataGraphable that should
		// be used here is only known by the caller of this method		
		
		return l;
	}
	
	/**
	 * @see org.concord.framework.otrunk.OTWrapper#saveObject(java.lang.Object)
	 */
	public void saveObject(Object wrappedObject)
	{
		DataPointRuler l = (DataPointRuler)wrappedObject;
		
		OTObjectService objService = getOTObjectService();

		DataGraphable dg = l.getDataGraphable();
		if(dg == null) {
			super.saveObject(wrappedObject);
			return;
		}
		OTDataGraphable otGraphable = (OTDataGraphable)objService.getWrapper(dg);
		resources.setDataGraphable(otGraphable);
		
		super.saveObject(wrappedObject);
	}
}