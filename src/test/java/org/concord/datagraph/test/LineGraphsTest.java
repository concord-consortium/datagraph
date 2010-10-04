package org.concord.datagraph.test;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.state.DataGraphManager;
import org.concord.datagraph.state.OTDataCollectorView;
import org.concord.datagraph.state.OTDataGraph;
import org.concord.datagraph.state.OTDataGraphable;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTID;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTUser;
import org.concord.framework.otrunk.view.OTView;
import org.concord.graph.engine.CoordinateSystem;
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
	private OTDataGraph otGraph;
	private OTDataCollectorView view;
	private DataGraphManager graphManager;
	private DataGraphable source;
	private DataGraphable second;
	private MockGraphics2D mockG;
	private ArrayList<DataGraphable> allGraphables;
	
	// Data collector with multiple channels using MultiWaveProducer
	public void testMultiLinesAreDrawn() throws Exception {
		initOtrunk();
		setupObjects("multi-wave-graph", "multi-wave-source", "multi-wave-second");
		
		runGraph(graphManager, 300);

		redrawGraphables();
		
		ArrayList<ShapeRec> paths = mockG.getAllShapes(ShapeId.PATH);
		
		assertTrue(paths.size() == 2);
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width > 0);
	}
	
	public void testMultiLinesAreClearedOnReset() throws Exception {
		initOtrunk();
		setupObjects("multi-wave-graph", "multi-wave-source", "multi-wave-second");
		
		runGraph(graphManager, 300);

		redrawGraphables();
		
		ArrayList<ShapeRec> paths = mockG.getAllShapes(ShapeId.PATH);
		
		assertTrue(paths.size() == 2);
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width > 0);
		
		graphManager.getStartable().reset();

		redrawGraphables();

		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width == 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);

	}
	
	public void testMultipleLearnerLines() throws Exception {
		initOtrunk();
		setupObjects("wave-graph", "wave-source", "wave-second");
		
		// select one graphable, run dp, and assert that only one line is drawn
		
		graphManager.setSelectedItem(source, true);
		
		runGraph(graphManager, 300);

		redrawGraphables();
		
		ArrayList<ShapeRec> paths = mockG.getAllShapes(ShapeId.PATH);
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);
		
		// select second graphable, run dp, and assert that two lines are drawn
		
		graphManager.setSelectedItem(second, true);
		
		runGraph(graphManager, 300);

		redrawGraphables();
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width > 0);
	}
	
	public void testMultipleLearnerLinesClearCorrectly() throws Exception {
		initOtrunk();
		setupObjects("wave-graph", "wave-source", "wave-second");
		
		// select one graphable, run dp
		
		graphManager.setSelectedItem(source, true);
		
		runGraph(graphManager, 300);
		
		// select second graphable, run dp, and assert that two lines are drawn
		
		graphManager.setSelectedItem(second, true);
		
		runGraph(graphManager, 300);
		
		redrawGraphables();
		
		ArrayList<ShapeRec> paths = mockG.getAllShapes(ShapeId.PATH);
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width > 0);
		
		// with second graphable still selected, hit reset, and assert only second line is cleared
		
		graphManager.getStartable().reset();

		redrawGraphables();
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);
		
		// select first graphable again, hit reset, and assert first line is also cleared
		
		graphManager.setSelectedItem(source, true);
		
		graphManager.getStartable().reset();

		redrawGraphables();
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width == 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);
	}
	
	public void testCreatingNewLearnerLines() throws Exception {
		initOtrunk();
		setupObjects("wave-graph", "wave-source", "wave-second");
		
		// select one graphable, run dp, and assert that only one line is drawn
		
		graphManager.setSelectedItem(source, true);
		
		runGraph(graphManager, 300);

		redrawGraphables();
		
		ArrayList<ShapeRec> paths = mockG.getAllShapes(ShapeId.PATH);
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);
		
		// create new graphable from source. There should now be three graphables on graph, with only one drawn
		
		DataGraphable newGraphable = (DataGraphable) graphManager.addItem(null, "new", Color.GREEN);
		allGraphables.add(newGraphable);
		
		redrawGraphables();
		
		paths = mockG.getAllShapes(ShapeId.PATH);
		
		// each time we call redraw, list of shapes in mockGraphics adds to itself. So
		// after adding a third graphable and drawing it, plus the first two again, we should
		// have five shapes in the graphics.
		assertTrue(paths.size() == 5);

		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);
		assertTrue(((GeneralPath)paths.get(4).shape).getBounds().width == 0);
		
		// select new graphable, run dp, and assert that it is drawn
		
		graphManager.setSelectedItem(newGraphable, true);
		
		runGraph(graphManager, 300);

		redrawGraphables();
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);
		assertTrue(((GeneralPath)paths.get(4).shape).getBounds().width > 0);
		
		// clear new graphable
		graphManager.getStartable().reset();

		redrawGraphables();
		
		assertTrue(((GeneralPath)paths.get(0).shape).getBounds().width > 0);
		assertTrue(((GeneralPath)paths.get(1).shape).getBounds().width == 0);
		assertTrue(((GeneralPath)paths.get(4).shape).getBounds().width == 0);
	}
	
	public void testXAxisResetsOnGraphReset() throws Exception {
		initOtrunk();
		setupObjects("multi-wave-graph", "multi-wave-source", "multi-wave-second");
		
		CoordinateSystem coord = graphManager.getDataGraph().getGraphArea().getCoordinateSystem();
		Point2D originOffset = coord.getOriginOffsetDisplay();
		
		runGraph(graphManager, 300);
		
		coord.setOriginOffsetDisplay(new Point2D.Double(5, 5));

		redrawGraphables();
		
		assertTrue(coord.getOriginOffsetDisplay().getX() == 5);
		
		graphManager.getDataGraph().reset();
		
		assertTrue(coord.getOriginOffsetDisplay().getX() == 0);

	}
	
	/**
	 * Test not working yet...
	 * 
	 * @throws Exception
	 */
//	public void testScaleCanResetOnGraphReset() throws Exception {
//		initOtrunk();
//		setupObjects("multi-wave-graph", "multi-wave-source", "multi-wave-second");
//		
//		graphManager.getDataGraph().getGraphArea().setSize(new Dimension(100,100));
//		
//		CoordinateSystem coord = graphManager.getDataGraph().getGraphArea().getCoordinateSystem();
//		Point2D originalScale = coord.getScale();
//		Point2D originalOffset = coord.getOriginOffsetDisplay();
//		
//
//		System.out.println("coord.getScale() = "+coord.getScale());
//		System.out.println("coord.getOffset() = "+coord.getOriginOffsetDisplay());
//		
//		
//		runGraph(graphManager, 300);
//		
//		graphManager.getDataGraph().getGraphArea().setLimitsAxisWorld(10, 100, 5, 100);
//		
//
//		System.out.println("coord.getScale() = "+coord.getScale());
//		System.out.println("coord.getOffset() = "+coord.getOriginOffsetDisplay());
//		
//		graphManager.getDataGraph().reset();
//		
//		System.out.println("coord.getScale() = "+coord.getScale());
//		System.out.println("coord.getOffset() = "+coord.getOriginOffsetDisplay());
//		
//		graphManager.getDataGraph().setRestoreScaleOnReset(true);
//		
//		runGraph(graphManager, 300);
//		
////		graphManager.getDataGraph().getGraphArea().setLimitsAxisWorld(20, 100, 10, 100);
//		
//		System.out.println("coord.getScale() = "+coord.getScale());
//		System.out.println("coord.getOffset() = "+coord.getOriginOffsetDisplay());
//		
//		graphManager.getDataGraph().reset();
//		
//		System.out.println("coord.getScale() = "+coord.getScale());
//		System.out.println("coord.getOffset() = "+coord.getOriginOffsetDisplay());
//		
//		assertTrue(coord.getScale().equals(new Point2D.Double(1.0, 1.0)));
//
//	}
	
	private void setupObjects(String graphId, String sourceId, String secondGraphableId) throws Exception {
		otGraph = (OTDataGraph) getObject(graphId, false);
		view = (OTDataCollectorView) getView(otGraph);
		graphManager = view.getGraphManager();
		OTControllerService controllerService = graphManager.getControllerService();
		
		OTDataGraphable otGraphableSource = (OTDataGraphable) getObject(sourceId, false);
		OTDataGraphable otGraphableSecond = (OTDataGraphable) getObject(secondGraphableId, false);
		source = (DataGraphable) controllerService.getRealObject(otGraphableSource);
		second = (DataGraphable) controllerService.getRealObject(otGraphableSecond);
		
		allGraphables = new ArrayList<DataGraphable>();
		allGraphables.add(source);
		allGraphables.add(second);
		
		mockG = new MockGraphics2D();
	}
	
	private void redrawGraphables(){
		for (DataGraphable graphable : allGraphables) {
			graphable.draw(mockG);
		}
	}
	
	private static void runGraph(DataGraphManager manager, long ms){
		manager.getStartable().start();

		long t0 = System.currentTimeMillis();
		long t1 = 0;
		do {
			t1 = System.currentTimeMillis();
		} while (t1-t0 < ms);
		
		manager.getStartable().stop();
	}
	
	private static void runDp(DataProducer dp, long ms){
		dp.start();
		
		long t0 = System.currentTimeMillis();
		long t1 = 0;
		do {
			t1 = System.currentTimeMillis();
		} while (t1-t0 < ms);
		
		dp.stop();
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
