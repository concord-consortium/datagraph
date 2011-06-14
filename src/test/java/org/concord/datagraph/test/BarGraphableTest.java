package org.concord.datagraph.test;


import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.concord.data.state.OTAlphaDataProducer;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.state.DataGraphManager;
import org.concord.datagraph.state.OTDataBarGraphable;
import org.concord.datagraph.state.OTDataCollectorView;
import org.concord.datagraph.state.OTDataGraph;
import org.concord.datagraph.state.OTDataGraphView;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.otrunk.test.OtmlTestHelper;


public class BarGraphableTest extends TestCase
{	
	private static URL authoredContent = BarGraphableTest.class.getResource("bar-graph.otml");

	// Dual channel data graph with data from data store
	public void testBarGraphable() throws Exception {
		try {
			OtmlTestHelper helper = new OtmlTestHelper();
			helper.initOtrunk(authoredContent);
			OTDataGraph otGraph = (OTDataGraph) helper.getObject("bar_graph_1");
			OTDataGraphView view = (OTDataGraphView) helper.getView(otGraph);
			DataGraphManager graphManager = view.getGraphManager();
			OTControllerService csvc = graphManager.getControllerService();
			
			OTDataBarGraphable otGraphable = (OTDataBarGraphable) helper.getObject("bar_graphable_1", false);
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
		OtmlTestHelper helper = new OtmlTestHelper();
		helper.initOtrunk(authoredContent);
		OTDataGraph otGraph = (OTDataGraph) helper.getObject("data_collector_2");
		OTDataCollectorView view = (OTDataCollectorView) helper.getView(otGraph);
		DataGraphManager graphManager = view.getGraphManager();
		OTControllerService controllerService = graphManager.getControllerService();
		
		OTDataBarGraphable otGraphable = (OTDataBarGraphable) helper.getObject("bar_graphable_2", false);
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
		OtmlTestHelper helper = new OtmlTestHelper();
		helper.initOtrunk(authoredContent);
		OTDataGraph otGraph = (OTDataGraph) helper.getObject("colored_bar_chart", false);
		OTDataCollectorView view = (OTDataCollectorView) helper.getView(otGraph);
		DataGraphManager graphManager = view.getGraphManager();
		OTControllerService controllerService = graphManager.getControllerService();
		
		OTDataBarGraphable otGraphable = (OTDataBarGraphable) helper.getObject("colored_bars", false);
		DataGraphable graphable = (DataGraphable) controllerService.getRealObject(otGraphable);
		
		MockGraphics2D g = new MockGraphics2D();
		graphable.draw(g);
		
		ArrayList<ShapeRec> bars = g.getAllShapes(ShapeId.LINE);
		Color[] colors = { Color.RED, Color.GREEN, Color.RED };
		
		for (int i = 0; i < bars.size(); i++) {
			assertEquals(colors[i], bars.get(i).color);
		}
	}
	
}
