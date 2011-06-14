package org.concord.datagraph.engine;

import junit.framework.TestCase;

import org.concord.data.state.OTDataProducer;
import org.concord.datagraph.state.OTDataGraphable;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.otrunk.test.OtmlTestHelper;

public class DataGraphableTest extends TestCase {
	public void testDataGraphableBasicDataProducerChange() throws Exception{
		OtmlTestHelper helper = new OtmlTestHelper();
		helper.initOtrunk(getClass().getResource("data-graphable-basic-change.otml"));
			
	    // when there is data in the datastore of the graphable with a different data description than the producer
		// check if that data is still valid before the producer starts running
		// this is the case if the user collected data earlier and is looking at that data on the graph
		// but then changed the data producer of the graphable.
		// Currently this only happens when a user collects data with one vendors probe and then switches vendors
		// That switch can sometimes change from using a dt based producer to a non dt based producer.
		OTDataGraphable otDataGraphable = (OTDataGraphable)helper.getObject("data_graphable");
		OTControllerService controllerService = helper.getControllerService();
		DataGraphable dataGraphable = (DataGraphable)controllerService.getRealObject(otDataGraphable);
		
		DataStore store = dataGraphable.getDataStore();
		
		// note virtual changes are not used here
		DataChannelDescription description = store.getDataChannelDescription(0);
		
		assertNotNull(description);		
		assertEquals("degC", description.getUnit().getDimension());
		
		// now collect some data and check the description again
		
		// the way to get the data producer to start is really odd, 
		// but that is how the DataGraphManager does it which makes this all work
		OTDataProducer otDataProducer = otDataGraphable.getDataProducer();
		DataProducer dataProducer = (DataProducer)controllerService.getRealObject(otDataProducer);
		dataProducer.start();
		Thread.sleep(500);
		dataProducer.stop();
		
		description = store.getDataChannelDescription(0);
		assertNotNull(description);		
		assertEquals("m", description.getUnit().getDimension());
	}

	public void testDataGraphableDataProducerDtChange() throws Exception{
		OtmlTestHelper helper = new OtmlTestHelper();
		helper.initOtrunk(getClass().getResource("data-graphable-dt-change.otml"));
			
	    // when there is data in the datastore of the graphable with a different data description than the producer
		// check if that data is still valid before the producer starts running
		// this is the case if the user collected data earlier and is looking at that data on the graph
		// but then changed the data producer of the graphable.
		// Currently this only happens when a user collects data with one vendors probe and then switches vendors
		// That switch can sometimes change from using a dt based producer to a non dt based producer.
		OTDataGraphable otDataGraphable = (OTDataGraphable)helper.getObject("data_graphable");
		assertNotNull(otDataGraphable);
		
		OTControllerService controllerService = helper.getControllerService();
		DataGraphable dataGraphable = (DataGraphable)controllerService.getRealObject(otDataGraphable);
		
		assertEquals(1, dataGraphable.getDataStoreChannelY());
		
		DataStore store = dataGraphable.getDataStore();
		DataChannelDescription description = store.getDataChannelDescription(dataGraphable.getDataStoreChannelY());
		
		assertNotNull(description);
		assertEquals("degC", description.getUnit().getDimension());		

		// the way to get the data producer to start is really odd, 
		// but that is how the DataGraphManager does it which makes this all work
		OTDataProducer otDataProducer = otDataGraphable.getDataProducer();
		DataProducer dataProducer = (DataProducer)controllerService.getRealObject(otDataProducer);
		dataProducer.start();
		Thread.sleep(500);
		dataProducer.stop();

		assertEquals(0, dataGraphable.getDataStoreChannelY());

		description = store.getDataChannelDescription(dataGraphable.getDataStoreChannelY());
		
		assertNotNull(description);
		assertEquals("m", description.getUnit().getDimension());		
		
	}
}
