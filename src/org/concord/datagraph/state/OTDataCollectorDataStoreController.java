package org.concord.datagraph.state;

import java.util.Vector;

import org.concord.data.state.OTDataStoreRealObject;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.ControllableDataGraphableDrawing;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.DataPointLabel;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DefaultDataStore;
import org.concord.framework.otrunk.DefaultOTController;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;
import org.concord.framework.otrunk.otcore.OTClassProperty;

public class OTDataCollectorDataStoreController extends DefaultOTController implements OTChangeListener
{
	public static Class [] realObjectClasses =  { DefaultDataStore.class };
	public static Class otObjectClass = OTDataCollectorDataStore.class;
	private DefaultDataStore dataStore;
	private OTDataCollectorDataStore otDCDS;
	private OTDataCollector dataCollector; 
	
	private static int DATA_LABEL_STORE = 0;

	public void loadRealObject(Object realObject)
	{
		dataStore = (DefaultDataStore) realObject;
		otDCDS = (OTDataCollectorDataStore) otObject;
		dataCollector = otDCDS.getDataCollector();
		
		int dataType = otDCDS.getDataType();
		if (dataType == DATA_LABEL_STORE){
			loadDataLabelStore();
		}
		
		dataCollector.addOTChangeListener(this);
		
	}
	
	private void loadDataLabelStore(){
		dataStore.clearValues();
		
		String xAxisString = dataCollector.getXDataAxis().getLabel();
		String yAxisString = dataCollector.getYDataAxis().getLabel();
		dataStore.setDataChannelDescription(0, new DataChannelDescription("Label"));
		dataStore.setDataChannelDescription(1, new DataChannelDescription(xAxisString, 1));
		dataStore.setDataChannelDescription(2, new DataChannelDescription(yAxisString, 1));
		
		// There is some hacking going on here to remove extraneous labels (0,0) and duplicates.
		// This should be fixed in the dataCollector.
		Vector labels = dataCollector.getLabels().getVector();
		int index = 0;
		Vector allLabels = new Vector();
		for (int i = 0; i < labels.size(); i++) {
			OTDataPointLabel label = (OTDataPointLabel) labels.get(i);
			OTClassProperty property = label.otClass().getProperty("xData");
			if (!label.otIsSet(property)){
				continue;
			}
			
			if (allLabels.contains(label)){
				continue;
			}
			
	        dataStore.setValueAt(index, 0, label.getText());
	        dataStore.setValueAt(index, 1, new Float(label.getXData()));
	        dataStore.setValueAt(index, 2, new Float(label.getYData()));
	        allLabels.add(label);
	        index++;
        }
	}

	public void registerRealObject(Object realObject)
	{
		
	}

	public void saveRealObject(Object realObject)
	{
		// TODO Auto-generated method stub

	}

	public void stateChanged(OTChangeEvent e)
    {
		System.out.println("state changed");
        loadDataLabelStore();
    }

}
