package org.concord.datagraph.test;


import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.concord.data.state.OTAlphaDataProducer;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.state.DataGraphManager;
import org.concord.datagraph.state.OTDataBarGraphable;
import org.concord.datagraph.state.OTDataCollectorView;
import org.concord.datagraph.state.OTDataGraph;
import org.concord.datagraph.state.OTDataGraphView;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTID;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTUser;
import org.concord.framework.otrunk.view.OTView;
import org.concord.otrunk.OTrunkImpl;
import org.concord.otrunk.datamodel.OTDatabase;
import org.concord.otrunk.datamodel.OTIDFactory;
import org.concord.otrunk.datamodel.OTTransientMapID;
import org.concord.otrunk.view.OTConfig;
import org.concord.otrunk.view.OTViewContainerPanel;
import org.concord.otrunk.view.OTViewerHelper;


public class BarGraphableTest extends TestCase
{
	private static final Logger logger = Logger.getLogger(BarGraphableTest.class.getCanonicalName());
	private static URL authoredContent = BarGraphableTest.class.getResource("bar-graph.otml");
	private OTViewerHelper viewerHelper;
	private OTDatabase mainDb;
	private OTrunkImpl otrunk;
	private String documentUUID = "CD47F7EC-5E1F-47F8-81F5-C34CC8FCD83E";

	// Dual channel data graph with data from data store
	public void testBarGraphable() throws Exception {
		try {
			initOtrunk();
			OTDataGraph otGraph = (OTDataGraph) getObject("bar_graph_1", false);
			OTDataGraphView view = (OTDataGraphView) getView(otGraph);
			DataGraphManager graphManager = view.getGraphManager();
			OTControllerService csvc = graphManager.getControllerService();
			
			OTDataBarGraphable otGraphable = (OTDataBarGraphable) getObject("bar_graphable_1", false);
			DataGraphable graphable = (DataGraphable) csvc.getRealObject(otGraphable);
			
			MockGraphics2D g = new MockGraphics2D();
			graphable.draw(g);
			
			assertTrue(graphManager.getDataGraph().getGraphArea() == graphable.getGraphArea());
			//assertTrue(otGraphable.getDataStore().getValuesString().split("\\s").length == g.getNumRects() * 2);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw(e);
		}
	}
	
	// Data collector with multiple channels using AlphaDataProducer
	public void testBarGraphable2() throws Exception {
		initOtrunk();
		OTDataGraph otGraph = (OTDataGraph) getObject("data_collector_2", false);
		OTDataCollectorView view = (OTDataCollectorView) getView(otGraph);
		DataGraphManager graphManager = view.getGraphManager();
		OTControllerService controllerService = graphManager.getControllerService();
		
		OTDataBarGraphable otGraphable = (OTDataBarGraphable) getObject("bar_graphable_2", false);
		DataGraphable graphable = (DataGraphable) controllerService.getRealObject(otGraphable);
		
		OTAlphaDataProducer otdp = (OTAlphaDataProducer) otGraphable.getDataProducer();
		// Incrementing "step" notifies AlphaProducer to add values.
		otdp.setStep(otdp.getStep() + 1);
		
		MockGraphics2D g = new MockGraphics2D();
		graphable.draw(g);
		
		assertTrue(graphManager.getDataGraph().getGraphArea() == graphable.getGraphArea());
		assertTrue(otdp.getNumChannels() == g.getNumRects());
	}
	
	// Data collector with multiple channels using AlphaDataProducer
	public void testBarGraphableColor() throws Exception {
		initOtrunk();
		OTDataGraph otGraph = (OTDataGraph) getObject("colored_bar_chart", false);
		OTDataCollectorView view = (OTDataCollectorView) getView(otGraph);
		DataGraphManager graphManager = view.getGraphManager();
		OTControllerService controllerService = graphManager.getControllerService();
		
		OTDataBarGraphable otGraphable = (OTDataBarGraphable) getObject("colored_bars", false);
		DataGraphable graphable = (DataGraphable) controllerService.getRealObject(otGraphable);
		
		MockGraphics2D g = new MockGraphics2D();
		graphable.draw(g);
		
		ArrayList<ShapeRec> bars = g.getAllLines();
		Color[] colors = { Color.RED, Color.GREEN, Color.RED };
		
		for (int i = 0; i < bars.size(); i++) {
			assertEquals(colors[i], bars.get(i).color);
		}
	}
	
	private void initOtrunk() throws Exception {
		logger.finer("loading otrunk");
		System.setProperty(OTConfig.NO_USER_PROP, "true");
	    viewerHelper = new OTViewerHelper();
		mainDb = viewerHelper.loadOTDatabase(authoredContent);
		viewerHelper.loadOTrunk(mainDb, null);
		otrunk = (OTrunkImpl) viewerHelper.getOtrunk();
	}
	
	private OTView getView(OTObject object) throws Exception {
		OTViewContainerPanel panel = viewerHelper.createViewContainerPanel();
		panel.setCurrentObject(object);
		Thread.sleep(2000);
		return panel.getView();
	}
	
	private OTObject getObject(String localId, boolean userVersion) throws Exception {
		// load in the first argument as an otml file
		// assume the root object is a folder, and then 
		// get the first child of the folder and
		// copy it and store the copy as the second
		// object in the folder

		// GET THE OBJECT
		logger.fine("Getting object");
		// OTFolder root = (OTFolder)viewerHelper.getRootObject();
		// OTObject first = (OTObject)root.getChild(firstIndex);
		OTID objectId = OTIDFactory.createOTID(documentUUID + "!/" + localId);
		OTObject first = otrunk.getRootObjectService().getOTObject(objectId);
		if (userVersion) {
    		OTUser user = otrunk.getUsers().get(0);
    		first = otrunk.getUserRuntimeObject(first, user);
    		assertTrue(first.getGlobalId() instanceof OTTransientMapID);
		}

		return first;
	}
}
