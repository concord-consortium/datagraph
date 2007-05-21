/**
 * 
 */
package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTControllerRegistry;
import org.concord.framework.otrunk.OTPackage;
import org.concord.framework.otrunk.OTrunk;

/**
 * @author scott
 *
 */
public class OTDatagraphPackage
    implements OTPackage
{

	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTPackage#initialize(org.concord.framework.otrunk.OTrunk)
	 */
	public void initialize(OTrunk otrunk)
	{
		// get controller registry and register our controllers
		OTControllerRegistry registry =
			(OTControllerRegistry)otrunk.getService(OTControllerRegistry.class);

		registry.registerControllerClass(OTDataPointLabelController.class);
		registry.registerControllerClass(OTDataPointRulerController.class);
		registry.registerControllerClass(OTDataGraphableController.class);
		registry.registerControllerClass(OTEraserGraphableController.class);
		registry.registerControllerClass(OTDataFlowingLineController.class);
		registry.registerControllerClass(OTDataAnnotationController.class);
	}

}
