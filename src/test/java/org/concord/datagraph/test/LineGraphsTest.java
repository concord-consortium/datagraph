package org.concord.datagraph.test;


import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.concord.data.state.OTAlphaDataProducer;
import org.concord.data.state.OTDataProducer;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.state.DataGraphManager;
import org.concord.datagraph.state.OTDataBarGraphable;
import org.concord.datagraph.state.OTDataCollectorView;
import org.concord.datagraph.state.OTDataGraph;
import org.concord.datagraph.state.OTDataGraphView;
import org.concord.datagraph.state.OTDataGraphable;
import org.concord.framework.data.stream.DataProducer;
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


public class LineGraphsTest extends TestCase
{
	private static final Logger logger = Logger.getLogger(LineGraphsTest.class.getCanonicalName());
	private static URL authoredContent = LineGraphsTest.class.getResource("line-graphs.otml");
	private OTViewerHelper viewerHelper;
	private OTDatabase mainDb;
	private OTrunkImpl otrunk;
	private String documentUUID = "5c7e6035-f50f-49fb-ac6c-6bc71eb3c7ca";
	
	// Data collector with multiple channels using AlphaDataProducer
	public void testMultiLinesAreDrawn() throws Exception {
		initOtrunk();
		OTDataGraph otGraph = (OTDataGraph) getObject("multi-wave-graph", false);
		OTDataCollectorView view = (OTDataCollectorView) getView(otGraph);
		DataGraphManager graphManager = view.getGraphManager();
		OTControllerService controllerService = graphManager.getControllerService();
		
		OTDataGraphable otGraphableSource = (OTDataGraphable) getObject("multi-wave-source", false);
		OTDataGraphable otGraphableSecond = (OTDataGraphable) getObject("multi-wave-second", false);
		OTDataProducer otDp = (OTDataProducer) getObject("multi_wave_generator", false);
		DataGraphable source = (DataGraphable) controllerService.getRealObject(otGraphableSource);
		DataGraphable second = (DataGraphable) controllerService.getRealObject(otGraphableSecond);
		DataProducer dp = (DataProducer) controllerService.getRealObject(otDp);
		
		dp.start();
		
		long t0 = System.currentTimeMillis();
		long t1 = 0;
		do {
			t1 = System.currentTimeMillis();
		} while (t1-t0 < 300);
		
		dp.stop();
		
		MockGraphics2D g = new MockGraphics2D();
		
		source.draw(g);
		second.draw(g);
		
		ArrayList<ShapeRec> paths = g.getAllShapes(ShapeId.PATH);
		
		assertTrue(paths.size() == 2);
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width > 0);
		
		paths = g.getAllShapes(ShapeId.PATH);
		System.out.println(paths.size());
		System.out.println(((GeneralPath)paths.get(0).shape).getBounds());

	}
	
	public void testMultiLinesAreClearedOnReset() throws Exception {
		initOtrunk();
		OTDataGraph otGraph = (OTDataGraph) getObject("multi-wave-graph", false);
		OTDataCollectorView view = (OTDataCollectorView) getView(otGraph);
		DataGraphManager graphManager = view.getGraphManager();
		OTControllerService controllerService = graphManager.getControllerService();
		
		OTDataGraphable otGraphableSource = (OTDataGraphable) getObject("multi-wave-source", false);
		OTDataGraphable otGraphableSecond = (OTDataGraphable) getObject("multi-wave-second", false);
		OTDataProducer otDp = (OTDataProducer) getObject("multi_wave_generator", false);
		DataGraphable source = (DataGraphable) controllerService.getRealObject(otGraphableSource);
		DataGraphable second = (DataGraphable) controllerService.getRealObject(otGraphableSecond);
		DataProducer dp = (DataProducer) controllerService.getRealObject(otDp);
		
		dp.start();
		
		long t0 = System.currentTimeMillis();
		long t1 = 0;
		do {
			t1 = System.currentTimeMillis();
		} while (t1-t0 < 300);
		
		dp.stop();
		
		MockGraphics2D g = new MockGraphics2D();
		
		source.draw(g);
		second.draw(g);
		
		ArrayList<ShapeRec> paths = g.getAllShapes(ShapeId.PATH);
		
		assertTrue(paths.size() == 2);
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width > 0);
		
		graphManager.getStartable().reset();
		
		source.draw(g);
		second.draw(g);
		
		paths = g.getAllShapes(ShapeId.PATH);

		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width == 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);

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
