/**
 * 
 */
package org.concord.datagraph.state;

import org.concord.data.state.OTDataPackage;
import org.concord.framework.otrunk.OTControllerRegistry;
import org.concord.framework.otrunk.OTPackage;
import org.concord.framework.otrunk.OTrunk;
import org.concord.graph.util.state.OTGraphUtilPackage;

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
			otrunk.getService(OTControllerRegistry.class);

		registry.registerControllerClass(OTDataPointLabelController.class);
		registry.registerControllerClass(OTDataRegionLabelController.class);
		registry.registerControllerClass(OTDataPointRulerController.class);
        registry.registerControllerClass(OTDataPointMarkerController.class);
		registry.registerControllerClass(OTDataGraphableController.class);
		registry.registerControllerClass(OTDataBarGraphableController.class);
		registry.registerControllerClass(OTEraserGraphableController.class);
		registry.registerControllerClass(OTDataFlowingLineController.class);
		registry.registerControllerClass(OTDataAnnotationController.class);
		registry.registerControllerClass(OTDataCollectorDataStoreController.class);
	}

	public Class[] getOTClasses() 
	{
		return new Class [] {
				OTAddGraphableAction.class,
				OTDataAnnotation.class,
				OTDataAxis.class,
				OTDataCollector.class,
				OTDataGraph.class,
				OTDataGraphable.class,
				OTDataBarGraphable.class,
				OTDataPointLabel.class,
				OTDataPointRuler.class,
                OTDataPointMarker.class,
				OTEraserGraphable.class,
				OTMultiDataGraph.class,
				OTMultiDataSetControl.class,
				OTDataCollectorDataStore.class,
				OTDataRegionLabel.class,
		};
	}

	public Class[] getPackageDependencies() 
	{
		return new Class [] {
				OTDataPackage.class,
				OTGraphUtilPackage.class,
				};
	}

}
