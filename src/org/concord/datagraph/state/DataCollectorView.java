
/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
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
 */

/*
 * Created on Apr 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.EventObject;

import javax.swing.JComponent;

import org.concord.datagraph.ui.AddDataPointLabelAction;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.DataPointLabel;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.WritableDataStore;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTrunk;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.graph.engine.SelectableList;
import org.concord.graph.event.GraphableListListener;
import org.concord.swing.SelectableToggleButton;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataCollectorView
 implements GraphableListListener, 	OTObjectView

{
    OTDataCollector dataCollector;
	SelectableList notesLayer;
	WritableDataStore dataStore;	
	DataGraphManager dataGraphManager;
	
    public DataCollectorView(OTDataCollector collector)
    {
        dataCollector = collector;
    }

    public JComponent getComponent(boolean editable)
    {
        return getDataGraph(editable, true);
    }
        
    public DataGraph getDataGraph(boolean showToolbar, boolean showDataControls)
    {
	    dataGraphManager = new DataGraphManager(dataCollector, showDataControls);

	    dataGraphManager.setToolbarVisible(showToolbar);
	    
	    DataGraph dataGraph = dataGraphManager.getDataGraph();
	    
		//Add notes button
		notesLayer = new SelectableList();
		dataGraph.getGraph().add(notesLayer);
		SelectableToggleButton addNoteButton = new SelectableToggleButton(new AddDataPointLabelAction(notesLayer, dataGraph.getObjList()));
		dataGraph.getToolBar().addButton(addNoteButton, "Add a note to a point in the graph");

		OTObjectList pfDPLabels = dataCollector.getLabels();
		
        //Load the data point labels
        for (int i=0; i<pfDPLabels.size(); i++){
			OTDataPointLabel otDPLabel = (OTDataPointLabel)pfDPLabels.get(i);
        	
			//Create a data point label
			DataPointLabel l = (DataPointLabel)otDPLabel.createWrappedObject();
						
			l.setGraphableList(dataGraphManager.getDataGraph().getObjList());
			notesLayer.add(l);			
        }
        
		notesLayer.addGraphableListListener(this);
        		
        return dataGraph;
    }
    
    public DataProducer getSourceDataProducer()
    {
        return dataGraphManager.getSourceDataProducer();
    }
    
    
	/**
	 * @see org.concord.graph.event.GraphableListListener#listGraphableAdded(java.util.EventObject)
	 */
	public void listGraphableAdded(EventObject e)
	{
		Object obj = e.getSource();
		if (obj instanceof DataPointLabel){
			DataPointLabel l;
			OTDataPointLabel otLabel;

			try{
				otLabel = (OTDataPointLabel)dataCollector.getOTDatabase().createObject(OTDataPointLabel.class);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			
			l = (DataPointLabel)obj;
			
			otLabel.registerWrappedObject(l);
			otLabel.saveObject(l);

			dataCollector.getLabels().add(otLabel);
		}
	}
		
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableChanged(java.util.EventObject)
	 */
	public void listGraphableChanged(EventObject e)
	{
		
	}
	
	/* (non-Javadoc)
	 * @see org.concord.graph.event.GraphableListListener#listGraphableRemoved(java.util.EventObject)
	 */
	public void listGraphableRemoved(EventObject e)
	{
	}
	

	/**
	 * @param otLabel
	 * @param l
	 */
/*	private void saveStateLabel(OTDataPointLabel otLabel, DataPointLabel l)
	{
		otLabel.setColor(l.getBackground().getRGB());
		if (l.getDataPoint() != null){
			otLabel.setXData((float)l.getDataPoint().getX());
			otLabel.setYData((float)l.getDataPoint().getY());
		}
		if (l.getDataGraphable() != null){
//			otLabel.setDataGraphable();
		}
		otLabel.setX((float)l.getLocation().getX());
		otLabel.setY((float)l.getLocation().getY());

		otLabel.setText(l.getMessage());
		
		OTrunk otrunk = dataCollector.getOTDatabase();
		OTDataGraphable otGraphable = (OTDataGraphable)otrunk.getWrapper(l.getDataGraphable());
		otLabel.setDataGraphable(otGraphable);
	}
*/
	
	/**
	 * @param l
	 * @param otDPLabel
	 */
/*	private void loadStateLabel(DataPointLabel l, OTDataPointLabel otDPLabel)
	{
		Point2D locPoint = null;
		Point2D dataPoint = null;
		
		l.setMessage(otDPLabel.getText());
		l.setBackground(new Color(otDPLabel.getColor()));
		if (!Float.isNaN(otDPLabel.getX()) && !Float.isNaN(otDPLabel.getY())){
			locPoint = new Point2D.Double(otDPLabel.getX(), otDPLabel.getY());
		}
		if (!Float.isNaN(otDPLabel.getXData()) && !Float.isNaN(otDPLabel.getYData())){
			dataPoint = new Point2D.Double(otDPLabel.getXData(), otDPLabel.getYData());
		}
		
		if (dataPoint != null){
			l.setDataPoint(dataPoint);
			if (locPoint != null){
				l.setLocation(locPoint);
			}
			else{
				//This cannot be done here because the label hasn't been added to the graph,
				//so there is no graph area yet
				//l.setLocation(l.getTextBoxDefaultLocation(null, l.getDataPoint()));
			}
		}
		else{
			l.setLocation(locPoint);
		}
		
		OTDataGraphable otGraphable = otDPLabel.getDataGraphable();
		if (otGraphable != null){		    
			l.setDataGraphable(otGraphable.getDataGraphable());
		}
		//l.setGraphableList(dataGraphManager.getDataGraph().getObjList());
		l.setSelectionEnabled(otDPLabel.getSelectable());
	}
*/
}

