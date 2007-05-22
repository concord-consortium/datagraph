/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01742
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * END LICENSE */

/*
 * Created on Mar 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.Color;

import org.concord.data.state.OTDataStore;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.ControllableDataGraphableDrawing;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DataStoreListener;
import org.concord.framework.data.stream.DataStreamEvent;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.graph.util.state.OTGraphableController;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDataGraphableController extends OTGraphableController
{
	public static Class [] realObjectClasses =  {
		DataGraphable.class, ControllableDataGraphable.class, 
		ControllableDataGraphableDrawing.class
	};
	
	public static Class otObjectClass = OTDataGraphable.class;    
	
	DataStoreListener dataStoreListener = new DataStoreListener(){

		public void dataAdded(DataStoreEvent evt){}

		public void dataChanged(DataStoreEvent evt){}

		public void dataChannelDescChanged(DataStoreEvent evt)
		{
			// TODO we should verify that this is coming from
			// the producer, if the description is coming
			// from somewhere else then it isn't clear what
			// should happen to the producer.
		}

		public void dataRemoved(DataStoreEvent evt)
		{
			OTDataStore dataStore = (OTDataStore)evt.getSource();
			OTDataGraphable model = (OTDataGraphable)otObject;
			DataProducer dataProducer = model.getDataProducer();

			if(dataProducer != null && dataStore.getTotalNumSamples() == 0){
				dataStore.setDataProducer(dataProducer);
			}
			// TODO Auto-generated method stub
		}
	};
	
	DataListener dataProducerListener = new DataListener(){
		boolean started = false;
		
		public void reset(){
			started = false;
		}
		
		public void dataReceived(DataStreamEvent dataEvent)
        {
			if(started){
				return;
			}

			// Check if this dataProducer has already been added to the 
			// datastore, if not then add it.
			// This will modify the list of dataListeners in the dataStore
			// while it is iterating over it.  It seems like this will
			// work ok though
			// This will happen whenever data arrives so we should 
			// remove ourselves from the list but we can't safely do that
			// while in the method.  So we have the boolean above instead.
			OTDataGraphable model = (OTDataGraphable)otObject;
			DataProducer modelDataProducer = model.getDataProducer();
			OTDataStore dataStore = model.getDataStore();
			
			DataProducer oldDataProducer = dataStore.getDataProducer();
			if(oldDataProducer != modelDataProducer){
				dataStore.setDataProducer(modelDataProducer);
			}
        }			

		public void dataStreamEvent(DataStreamEvent dataEvent){}
		
	};
	
    public void loadRealObject(Object realObject)
    {
    	OTDataGraphable model = (OTDataGraphable)otObject;
    	
		DataGraphable dg = (DataGraphable)realObject;

        dg.setColor(new Color(model.getColor()));
        dg.setShowCrossPoint(model.getDrawMarks());
        dg.setLabel(model.getName());
        dg.setUseVirtualChannels(true);
        dg.setLocked(model.getLocked());
        
		DataProducer producer = model.getDataProducer();
		OTDataStore dataStore = model.getDataStore();

		if (model.getControllable() && producer != null){
			// This is a schema type error
			// we should give more information about tracking it down
		    throw new RuntimeException("Can't control a graphable with a data producer");
		}
		
        if(dataStore == null) {
        	// If the dataStore is null then we create a new
        	// one to store the data so it can retrieved 
        	// later.  If the data needs to be referenced
        	// within the content then it should be explictly 
        	// defined in the content.
            try {
            	
                OTObjectService objService = model.getOTObjectService();
                dataStore = (OTDataStore)objService.createObject(OTDataStore.class);
                model.setDataStore(dataStore);
            } catch (Exception e) {
                // we can't handle this
                throw new RuntimeException(e);
            }
        }
        
        // now we can safely assume dataStore != null
        // however we should not set the producer onto the datastore
        // if there is data in the data store, this could possibly
        // mess up the data that is in the data store, because the data
        // description of the data in the data store might be different
        // from the data description in the producer.
        // instead we need to listen to the dataStore and dataproducer
        // and when the datastore is cleared, or the producer is started
        // then we set the producer on the datastore.
        // cleared, or the start 
		if (producer != null){
			if(dataStore.getTotalNumSamples() == 0){
				dataStore.setDataProducer(producer);
			} 
			
			// listen to the dataStore so if the data is cleared at some
			// point then we will reset the producer 
			// we should be careful not to add a listener twice			
			dataStore.addDataStoreListener(dataStoreListener);

			producer.addDataListener(dataProducerListener);
		}
		
		dg.setDataStore(dataStore);

		dg.setChannelX(model.getXColumn());
		dg.setChannelY(model.getYColumn());
    }
    
	/**
	 * @see org.concord.framework.otrunk.OTController#saveObject(java.lang.Object)
	 */
	public void saveRealObject(Object realObject)
	{
    	OTDataGraphable model = (OTDataGraphable)otObject;

		DataGraphable dg = (DataGraphable)realObject;
		
		Color c = dg.getColor();
		model.setColor(c.getRGB() & 0x00FFFFFF);
		model.setConnectPoints(dg.isConnectPoints());
		model.setDrawMarks(dg.isShowCrossPoint());
		model.setXColumn(dg.getChannelX());
		model.setYColumn(dg.getChannelY());
		model.setName(dg.getLabel());
        
        // This might not be quite right, lets cross our fingers
        // that it doesn't screw anything else up
        DataStore ds = dg.getDataStore();
        if(ds instanceof OTDataStore){
        	model.setDataStore((OTDataStore)ds);
        }
        
		// This is needed for some reason by the OTDrawingToolView
        // Apparently it is to set the realObject class.
        if(dg instanceof ControllableDataGraphableDrawing) {
        	model.setDrawing(true);    		
        }
	}

	/**
	 * @see org.concord.framework.otrunk.OTController#getRealObjectClass()
	 */
	public Class getRealObjectClass()
	{
    	OTDataGraphable model = (OTDataGraphable)otObject;

		if (model.getDrawing()){
        	return ControllableDataGraphableDrawing.class;            
		}
        else if (model.getControllable()){
        	return ControllableDataGraphable.class;
        } 
        else {
        	return DataGraphable.class;
        }
	}
}
