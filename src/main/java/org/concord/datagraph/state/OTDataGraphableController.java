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

import org.concord.data.state.OTDataProducer;
import org.concord.data.state.OTDataStore;
import org.concord.data.stream.LazySyncProducerDataStore;
import org.concord.data.stream.LazySyncProducerDataStore.DataProducerAndDataStoreProvider;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.ControllableDataGraphableDrawing;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.engine.DataGraphableEx;
import org.concord.framework.data.stream.DataChannelDescription;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.WritableDataStore;
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
		DataGraphable.class, DataGraphableEx.class, ControllableDataGraphable.class, 
		ControllableDataGraphableDrawing.class
	};
	
	public static Class otObjectClass = OTDataGraphable.class;    
	
	LazySyncProducerDataStore dataStoreSyncer = 
		new LazySyncProducerDataStore(new DataProducerAndDataStoreProvider() {
		
		public DataStore getDataStore() {
			return OTDataGraphableController.this.getDataStore((OTDataGraphable)otObject);
		}
		
		public DataProducer getDataProducer() {
			return OTDataGraphableController.this.getDataProducer((OTDataGraphable)otObject);
		}
	});
	
    public void loadRealObject(Object realObject)
    {
    	OTDataGraphable model = (OTDataGraphable)otObject;
    	
		DataGraphable dg = (DataGraphable)realObject;

        dg.setColor(new Color(model.getColor()));
        dg.setShowCrossPoint(model.getDrawMarks());
        dg.setLabel(model.getName());
        dg.setUseVirtualChannels(true);
        dg.setLocked(model.getLocked());
        dg.setConnectPoints(model.getConnectPoints());
        dg.setVisible(model.getVisible());
        dg.setShowAllChannels(model.getShowAllChannels());
        dg.setShowSampleLimit(model.getShowSampleLimit());

		DataProducer producer = getDataProducer(model);
		DataStore dataStore = getDataStore(model);

		if (model.getControllable() && producer != null){
			// This is a schema type error
			// we should give more information about tracking it down
		    throw new RuntimeException("A graphable can't be controllable and have a data producer");
		}
		
        if(dataStore == null) {
        	// If the dataStore is null then we create a new
        	// one to store the data so it can retrieved 
        	// later.  If the data needs to be referenced
        	// within the content then it should be explicitly 
        	// defined in the content.
            try {
            	
                OTObjectService objService = model.getOTObjectService();
                OTDataStore otDataStore = objService.createObject(OTDataStore.class);
                
                // this is a dangerous because the loadRealObject method we are in 
                // is called when the model (OTObject) changes.
                // The listener that calls loadRealObject (defined in super class) 
                // tries to be smart and not go recursive, but it doesn't handle the case
                // where a call to loadRealObject changes the model (OTObject)
                // by wrapping this with the bOTObjectChanging code that will prevent
                // the recursion.
                bOTObjectChanging = true;
                model.setDataStore(otDataStore);
                bOTObjectChanging = false;
                dataStore = getDataStore(model);
            } catch (Exception e) {
                // we can't handle this
                throw new RuntimeException(e);
            }
        }
        
        // now we can safely assume dataStore != null
        // however we should not set the producer onto the datastore
        //    ((ProducerDataStore)dataStore).setDataProducer(producer);
        // if there is data in the data store, this could possibly
        // mess up the data that is in the data store
        // so use the lazy syncer to handle this for us
        dataStoreSyncer.init();
        		
		if(model.getControllable()){
			// make sure the dataStore has the 2 channels that are needed for this.
			if(model.getYColumn() >= dataStore.getTotalNumChannels() ||
					model.getXColumn() >= dataStore.getTotalNumChannels()){
				DataChannelDescription dataChannelDescriptionX = dataStore.getDataChannelDescription(model.getXColumn());
				DataChannelDescription dataChannelDescriptionY = dataStore.getDataChannelDescription(model.getYColumn());
				
				// assume this is a writable datastore
				WritableDataStore wDataStore = (WritableDataStore) dataStore;
				if(dataChannelDescriptionX == null){
					dataChannelDescriptionX = new DataChannelDescription();
				}
				wDataStore.setDataChannelDescription(model.getXColumn(), dataChannelDescriptionX);
				if(dataChannelDescriptionY == null){
					dataChannelDescriptionY = new DataChannelDescription();
				}
				wDataStore.setDataChannelDescription(model.getYColumn(), dataChannelDescriptionY);
			}
		}
		
		dg.setDataStore(dataStore);

		dg.setChannelX(model.getXColumn());
		dg.setChannelY(model.getYColumn());
		
		if (model.isResourceSet("lineWidth")){
			dg.setLineWidth(model.getLineWidth());
		}
    }
    
	/**
	 * @see org.concord.framework.otrunk.OTController#saveObject(java.lang.Object)
	 */
	public void saveRealObject(Object realObject)
	{
    	OTDataGraphable model = (OTDataGraphable)otObject;

		DataGraphable dg = (DataGraphable)realObject;
		
		Color c = dg.getColor();
		if(c != null){
			model.setColor(c.getRGB() & 0x00FFFFFF);
		}
		model.setConnectPoints(dg.isConnectPoints());
		model.setDrawMarks(dg.isShowCrossPoint());
		model.setXColumn(dg.getChannelX());
		model.setYColumn(dg.getChannelY());
		model.setName(dg.getLabel());
		model.setLineWidth(dg.getLineWidth());
		model.setShowAllChannels(dg.getShowAllChannels());
		
        // This might not be quite right, lets cross our fingers
        // that it doesn't screw anything else up
        DataStore ds = dg.getDataStore();
        OTDataStore otDataStore = (OTDataStore) controllerService.getOTObject(ds);
        
        // the otDataStore could be null here, if the dataStore doesn't have an otObject.
        // lets print a warning for now
        if(otDataStore == null){
        	System.err.println("Warning trying to save a datastore which doesn't have an otObject");
        	System.err.println("  " + ds);
        } 
        
        model.setDataStore(otDataStore);
        
        model.setVisible(dg.isVisible());
        
		// This is needed for some reason by the OTDrawingToolView
        // Apparently it is to set the realObject class.
        if(dg instanceof ControllableDataGraphableDrawing) {
        	model.setDrawing(true);    		
        }
        
        // FIXME we ought to be saving the dataproducer here, but there isn't
        // a clear way to figure out which dataproducer to save.
        // so for now we won't save any of them.
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
	
	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.DefaultOTController#dispose(Object)
	 */
	@Override
	public void dispose(Object realObject)
	{
		dataStoreSyncer.dispose();

		// our realObject should be a DataGraphable
		DataGraphable dataGraphable = (DataGraphable) realObject;
		
		// set the data store to null, so our graphable stops
		// listening to the datastore.  Otherwise the datastore will
		// keep a reference to the graphable, which might in turn keep
		// references to other objects...
		dataGraphable.setDataStore(null);
		
	    // TODO Auto-generated method stub
	    super.dispose(realObject);
	}
	
	DataProducer getDataProducer(OTDataGraphable model)
	{
		OTDataProducer otDataProducer = model.getDataProducer();
		return (DataProducer) controllerService.getRealObject(otDataProducer);
	}
	
	DataStore getDataStore(OTDataGraphable model)
	{
		return (DataStore) controllerService.getRealObject(model.getDataStore());
	}
	
}
