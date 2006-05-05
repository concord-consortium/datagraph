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
 * Created on Apr 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.concord.data.Unit;
import org.concord.data.ui.DataFlowControlAction;
import org.concord.data.ui.DataFlowControlButton;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.data.ui.DataStoreLabel;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.AddDataPointLabelAction;
import org.concord.datagraph.ui.AddDataPointLabelActionExt;
import org.concord.datagraph.ui.AutoScaleAction;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.DataPointLabel;
import org.concord.datagraph.ui.DataPointRuler;
import org.concord.datagraph.ui.SingleDataAxisGrid;
import org.concord.framework.data.DataDimension;
import org.concord.framework.data.DataFlow;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.framework.otrunk.OTWrapper;
import org.concord.framework.util.CheckedColorTreeModel;
import org.concord.framework.util.Copyable;
import org.concord.graph.engine.GraphableList;
import org.concord.graph.engine.SelectableList;
import org.concord.graph.event.GraphableListListener;
import org.concord.graph.examples.GraphWindowToolBar;
import org.concord.graph.ui.GraphTreeView;
import org.concord.graph.ui.Grid2D;
import org.concord.graph.ui.SingleAxisGrid;
import org.concord.graph.util.control.DrawingAction;
import org.concord.swing.SelectableToggleButton;
import org.concord.view.CheckedColorTreeControler;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataGraphManager
	implements GraphableListListener, OTChangeListener, 
        ChangeListener, CheckedColorTreeModel, DataFlow
{
    OTDataCollector dataCollector;
    OTDataGraph otDataGraph;
    DataGraph dataGraph;
    
	DataGraphable sourceGraphable;
	DataProducer sourceDataProducer;
	DataStoreLabel valueLabel;
	
	OTObjectList labels;
	SelectableList notesLayer;
	
	OTDataAxis xOTAxis;
	OTDataAxis yOTAxis;
	
    Hashtable otDataGraphableMap = new Hashtable();
    
	JPanel bottomPanel;
	DataFlowControlToolBar toolBar = null;
	
	boolean isCausingOTChange = false;
	boolean showDataControls;
	
	DataFlowControlButton bStart;
	DataFlowControlButton bStop;
	DataFlowControlButton bClear;
	
    Color[] colors = {Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW, Color.BLACK};

    /**
     * 
     */
    public DataGraphManager(OTDataGraph pfObject, boolean showDataControls)
    {
    	this.otDataGraph = pfObject;
        if(pfObject instanceof OTDataCollector)
        	dataCollector = (OTDataCollector)pfObject;
        this.showDataControls = showDataControls;
        
        initialize(showDataControls);
    }

    public DataGraphable getDataGraphable(OTDataGraphable otGraphable)
    {
        return (DataGraphable)otDataGraphableMap.get(otGraphable);
    }
    
    public DataProducer getSourceDataProducer()
    {
        return sourceDataProducer;
    }

    /**
     * This is required to pull out dataset tree code
     *
     */
    protected void setSourceDataProducer(DataProducer sDataProducer)
    {
        sourceDataProducer = sDataProducer;
    }
    
    
    public float getLastValue()
    {
        if(valueLabel == null) {
            return Float.NaN;
        }
        return valueLabel.getValue();
    }
    
    /**
     * @return
     */
    public JPanel getBottomPanel()
    {
        return bottomPanel;
    }

    public DataGraph getDataGraph()
    {
        return dataGraph;
    }
    
    protected OTDataGraph getOTDataGraph()
    {
        return otDataGraph;
    }
    
    public DataFlowControlToolBar getFlowToolBar()
    {
        return toolBar;
    }
    
	public DataFlowControlToolBar createFlowToolBar()
	{
	    DataFlowControlToolBar toolbar = 
	        new DataFlowControlToolBar(false);
		
		bStart = new DataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_START);
		toolbar.add(bStart);

		bStop = new DataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_STOP);
		toolbar.add(bStop);
		
		bClear = new DataFlowControlButton(DataFlowControlAction.FLOW_CONTROL_RESET);
		bClear.setText("Clear");
		toolbar.add(bClear);
	 
		toolbar.addDataFlowObject(dataGraph);
		
		bStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bClear.setEnabled(true);
				bStart.setEnabled(false);
				bStop.setEnabled(true);
			}
		});
		
		bStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bClear.setEnabled(true);
				bStart.setEnabled(false);
				bStop.setEnabled(false);
			}
		});
		
		bClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bClear.setEnabled(false);
				bStart.setEnabled(true);
				bStop.setEnabled(false);
			}
		});
		
	    return toolbar;
	}
	
	public void setToolBarEnabled(boolean enabled) {
		if(bStart != null) bStart.setEnabled(enabled);
		if(bStop != null) bStop.setEnabled(enabled);
		if(bClear != null) bClear.setEnabled(enabled);			
	}

	public void setToolbarVisible(boolean visible)
	{
		GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
		if(gwToolbar != null) {
		    // FIXME
			gwToolbar.setVisible(visible);
		}
	}		
	
	/**
	 * This only works for graphables that came from a loaded
	 * pfgraphables.  It doesn't yet handel cases where new
	 * graphables are created by some external thing
	 *
	 */
	public void updateState(Object obj)
	{
		Grid2D grid = dataGraph.getGrid();

		xOTAxis.setDoNotifyChangeListeners(false);
		yOTAxis.setDoNotifyChangeListeners(false);
		
		xOTAxis.setMin((float)dataGraph.getMinXAxisWorld());
		xOTAxis.setMax((float)dataGraph.getMaxXAxisWorld());
		yOTAxis.setMin((float)dataGraph.getMinYAxisWorld());
		yOTAxis.setMax((float)dataGraph.getMaxYAxisWorld());

		SingleAxisGrid sXAxis = grid.getXGrid();
		if(sXAxis.getAxisLabel() != null){
			xOTAxis.setLabel(sXAxis.getAxisLabel());
		}
		
		SingleAxisGrid sYAxis = grid.getYGrid();
		if(sYAxis.getAxisLabel() != null){
			yOTAxis.setLabel(sYAxis.getAxisLabel());
		}

		isCausingOTChange = true;
		xOTAxis.notifyOTChange();
		yOTAxis.notifyOTChange();
		isCausingOTChange = false;

		/*
		if(obj instanceof DataGraphable) {
		    OTDataGraphable source = dataCollector.getSource();
		    
		    source.saveObject(obj);		
		}
		*/
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
                OTObjectService objService = otDataGraph.getOTObjectService();
				otLabel = (OTDataPointLabel)objService.createObject(OTDataPointLabel.class);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			
			l = (DataPointLabel)obj;
			
			otLabel.registerWrappedObject(l);
			otLabel.saveObject(l);

			otDataGraph.getLabels().add(otLabel);
		} else if (obj instanceof DataPointRuler){
			DataPointRuler l;
			OTDataPointRuler otRuler;

			try{
                OTObjectService objService = otDataGraph.getOTObjectService();
				otRuler = (OTDataPointRuler)objService.createObject(OTDataPointRuler.class);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			
			l = (DataPointRuler)obj;
			
			otRuler.registerWrappedObject(l);
			otRuler.saveObject(l);

			otDataGraph.getLabels().add(otRuler);
		}
	}
		
	/**
	 * @see org.concord.graph.event.GraphableListListener#listGraphableChanged(java.util.EventObject)
	 */
	public void listGraphableChanged(EventObject e)
	{
		updateState(e.getSource());
	}
	
	/**
	 * @see org.concord.graph.event.GraphableListListener#listGraphableRemoved(java.util.EventObject)
	 */
	public void listGraphableRemoved(EventObject e)
	{
		Object obj = e.getSource();
		
		OTWrapper otWrapper = otDataGraph.getOTObjectService().getWrapper(obj);
		
		if (otWrapper != null){
			if(otWrapper instanceof OTDataPointLabel ||
					otWrapper instanceof OTDataPointRuler )
				otDataGraph.getLabels().remove(otWrapper);
		}
	}

	/* (non-Javadoc)
	 * @see org.concord.framework.otrunk.OTChangeListener#stateChanged(org.concord.framework.otrunk.OTChangeEvent)
	 */
	public void stateChanged(OTChangeEvent e)
	{
	    if(isCausingOTChange) {
	        // we are the cause of this change
	        return;
	    }

	    if(e.getSource() == xOTAxis || e.getSource() == yOTAxis) {
	        dataGraph.setLimitsAxisWorld(xOTAxis.getMin(), xOTAxis.getMax(),
	                yOTAxis.getMin(), yOTAxis.getMax());
	    }	    	    
	}
	
	/* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e)
    {
        Object source = e.getSource();
        updateState(source);
    }
    
    public void setSelectedItem(Object item, boolean checked){
         setSelectedDataGraphable((DataGraphable)item, checked);
     }

    
    protected void setSelectedDataGraphable(DataGraphable dg, boolean visible)
    {
        if(dg == null) {
           setToolBarEnabled(false);
           return;
        }

        Dimension labelSize = null;
        Point labelLocation = null;
        
        if(toolBar != null) {
            // disconnect toolBar from the last data producer
            toolBar.removeDataFlowObject(sourceDataProducer);
            labelSize = valueLabel.getSize();
            labelLocation = valueLabel.getLocation();
            bottomPanel.remove(valueLabel);
            bottomPanel.remove(toolBar);
        }
        
        //Connect new data producer with toolBar and valueLabel
        DataProducer dp = dg.findDataProducer();
        if(dp != null) sourceDataProducer = dp;
        //sourceDataProducer = dg.findDataProducer();
        
        if(toolBar != null) {
            toolBar.addDataFlowObject(sourceDataProducer);
            valueLabel = new DataStoreLabel(dg, 1);
            valueLabel.setColumns(4);
            
            bottomPanel.setLayout(new FlowLayout());
            valueLabel.setSize(labelSize);
            valueLabel.setLocation(labelLocation);
            bottomPanel.add(valueLabel);
            bottomPanel.add(toolBar);
            //bottomPanel.add(about);
        }
        
        dg.setVisible(visible);
            
        if(dg.isLocked()) {
            setToolBarEnabled(false);
        } else {
            setToolBarEnabled(visible);
        }
    }
    
    private OTDataGraphable getOTDataGraphable(DataGraphable dataGraphable) {
   		Enumeration enu = otDataGraphableMap.keys();
   		while(enu.hasMoreElements()) {
   			OTDataGraphable otDataGraphable = (OTDataGraphable) enu.nextElement();
   			if(otDataGraphableMap.get(otDataGraphable).equals(dataGraphable)) {
   	   			return otDataGraphable;
   			}
   		}
    	return null;
    }
    
    private void removeLabelsFrom(OTDataGraphable dataGraphable) {
    	OTObjectList labelList = otDataGraph.getGraphables();
    	for(int i = 0; i < labelList.size(); i++) {
    		Object obj = labelList.get(i);
    		if(obj.equals(dataGraphable)) {
    			labelList.remove(i);
        		//return;
    		}
    		//System.out.println("label: " + obj.toString());
    		if(obj instanceof OTDataPointLabel) {
    			//System.out.println("ot datapoint label");
    			OTDataPointLabel dataPointLabel = (OTDataPointLabel)obj;
    			if(dataPointLabel.getDataGraphable().equals(dataGraphable)) {
    				labelList.remove(dataPointLabel);
    			}
    		} else if(obj instanceof OTDataPointRuler) {
    			//System.out.println("ot datapoint ruler");
    			OTDataPointRuler dataPointRuler = (OTDataPointRuler)obj;
    			if(dataPointRuler.getDataGraphable().equals(dataGraphable)) {
    				labelList.remove(dataPointRuler);
    			}
    		}
    	}
    }
    
	public static void setupAxisLabel(SingleDataAxisGrid sAxis, OTDataAxis axis)
	{
		if(axis.getLabel() != null) {
			sAxis.setAxisLabel(axis.getLabel());
		}
	
		if(axis.getUnits() != null) {
			String unitStr = axis.getUnits();
			Unit unit = Unit.findUnit(unitStr);
			if(unit == null) {
				System.err.println("Can't find unit: " + unitStr);
				sAxis.setUnit(new UnknownUnit(unitStr)); 
			} 
			else {
				sAxis.setUnit(unit);
			}
		}		
	}
	
	public void initialize() {
		this.initialize(true);
	}
	
	protected void initialize(boolean showToolBar) {
        dataGraph = new DataGraph();
		dataGraph.changeToDataGraphToolbar();
		dataGraph.setAutoFitMode(DataGraph.AUTO_SCROLL_RUNNING_MODE);
		
        dataGraph.setFocusable(true);
        
		notesLayer = new SelectableList();
		dataGraph.getGraph().add(notesLayer);

		SelectableToggleButton addNoteButton = new SelectableToggleButton(new AddDataPointLabelAction(notesLayer, dataGraph.getObjList()));
		dataGraph.getToolBar().addButton(addNoteButton, "Add a note to a point in the graph");
		
		//DataPointRuler need to be explicitly enabled to show per Brad's request.
		if(dataCollector != null && dataCollector.getRulerEnabled()) {
			SelectableToggleButton addNoteButton2 = new SelectableToggleButton(new AddDataPointLabelActionExt(notesLayer, dataGraph.getObjList()));
			dataGraph.getToolBar().addButton(addNoteButton2, "Add a ruler to a point in the graph");			
		}
		
		//AutoScale need to be explicitly enabled to show per Brad's request.
		if(dataCollector != null && dataCollector.getAutoScaleEnabled()) {
			JButton autoScaleButton = new JButton(new AutoScaleAction(dataGraph));
			JButton autoScaleXButton = new JButton(new AutoScaleAction(AutoScaleAction.AUTOSCALE_X, dataGraph));
			JButton autoScaleYButton = new JButton(new AutoScaleAction(AutoScaleAction.AUTOSCALE_Y, dataGraph));
	
			dataGraph.getToolBar().addButton(autoScaleButton, "Autoscale the graph");
			dataGraph.getToolBar().addButton(autoScaleXButton, "Autoscale X axis");
			dataGraph.getToolBar().addButton(autoScaleYButton, "Autoscale Y axis");
		}
		
        KeyStroke keyStroke = KeyStroke.getKeyStroke(new Character('t'), InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        dataGraph.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "ShowTree");
        dataGraph.getActionMap().put("ShowTree", new AbstractAction(){
            /**
             * First version of action
             */
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Got event: " + e);
                GraphTreeView gtv = new GraphTreeView();
                gtv.setGraph(dataGraph.getGraph());
                GraphTreeView.showAsDialog(gtv, "graph tree");                
            }            
        });
        
		xOTAxis = otDataGraph.getXDataAxis();
		xOTAxis.addOTChangeListener(this);
		yOTAxis = otDataGraph.getYDataAxis();
		yOTAxis.addOTChangeListener(this);

		dataGraph.setLimitsAxisWorld(xOTAxis.getMin(), xOTAxis.getMax(),
				yOTAxis.getMin(), yOTAxis.getMax());

		Grid2D grid = dataGraph.getGrid();

		SingleDataAxisGrid sXAxis = (SingleDataAxisGrid)grid.getXGrid();

		//DataGraphStateManager.setupAxisLabel(sXAxis, xOTAxis);
		setupAxisLabel(sXAxis, xOTAxis);
		
		SingleDataAxisGrid sYAxis = (SingleDataAxisGrid)grid.getYGrid();
		//DataGraphStateManager.setupAxisLabel(sYAxis, yOTAxis);
		setupAxisLabel(sYAxis, yOTAxis);

		// FIXME This should be changed.  The graphables should
		// listen to themselves, and this code then can just listen
		// to the graph area for graph area changes.
		// right now both graph area and source graphable changes
		// are listened too
		// GraphableList graphableList = dataGraph.getObjList();
		// graphableList.addGraphableListListener(this);
		
		initGraphables();
		
		dataGraph.setPreferredSize(new Dimension(400,320));
		
		dataGraph.getGraphArea().addChangeListener(this);
		
		initLabels();
	}
	
	protected void initGraphables() {
		OTObjectList pfGraphables = otDataGraph.getGraphables();
		Vector realGraphables = new Vector();
		
		// for each list item get the data producer object
		// add it to the data graph
		for(int i=0; i<pfGraphables.size(); i++) {
			OTDataGraphable otGraphable = (OTDataGraphable)pfGraphables.get(i);

			DataGraphable realGraphable = (DataGraphable)otGraphable.createWrappedObject();

			if (realGraphable.getDataProducer() != null){
			    System.err.println("Trying to display a background graphable with a data producer");
			}
			
			realGraphables.add(realGraphable);
		    dataGraph.addDataGraphable(realGraphable);
		    //dataGraph.addBackgroundDataGraphable(realGraphable);
            otDataGraphableMap.put(otGraphable, realGraphable);
		}

		OTDataGraphable source = null;
		if(dataCollector != null)
			source = dataCollector.getSource();
		
		if(source != null) {
			String title = dataCollector.getTitle(); 
			if(title == null) {
			    title = source.getName();			    
			}
			
			if(title != null) {
			    dataGraph.setTitle(title);
			}

			sourceGraphable = (DataGraphable)source.createWrappedObject();
			sourceDataProducer = sourceGraphable.findDataProducer();
			
			// dProducer.getDataDescription().setDt(0.1f);
			if(sourceGraphable instanceof ControllableDataGraphable) {
			    bottomPanel = new JPanel(new FlowLayout());
			    JButton clearButton = new JButton("Clear");
			    clearButton.addActionListener(new ActionListener(){
			       public void actionPerformed(ActionEvent e){
			           dataGraph.reset();			           
			       }
			    });
			    
				DrawingAction a = new DrawingAction();
				a.setDrawingObject((ControllableDataGraphable)sourceGraphable);
				GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
				gwToolbar.addButton(new SelectableToggleButton(a), "Draw a function");
				
				bottomPanel.add(clearButton);
			    //bottomPanel.add(about);
			    
			    dataGraph.add(bottomPanel, BorderLayout.SOUTH);
			    
			} else if(showDataControls) {
			    bottomPanel = new JPanel(new FlowLayout());
			    valueLabel = new DataStoreLabel(sourceGraphable, 1);
			    valueLabel.setColumns(4);
			    bottomPanel.add(valueLabel);

			    toolBar = createFlowToolBar();
			    toolBar.addDataFlowObject(sourceDataProducer);
			    
			    bottomPanel.add(toolBar);
			    //bottomPanel.add(about);
			    
			    //if(dataCollector.getShowTare()){
			        // need to add a button that runs the sourceGraphable
			        // for some amount of time and then offsets the input
			        // values after that.
			        // The code for the Tare can be put into a dataproducer
			        // that wraps the input dataproducer
			    //}
			    
			    dataGraph.add(bottomPanel, BorderLayout.SOUTH);  			    
			}

			if(sourceGraphable != null) {
			    realGraphables.insertElementAt(sourceGraphable, 0);
			    dataGraph.addDataGraphable(sourceGraphable);
                otDataGraphableMap.put(source, sourceGraphable);
			}
		}

		// If the enabled is not set then the multiple graphable control is
        // shown if there is more than one graphable
        // If the enabled is set to false then the multiple graphable control
        // is not shown even if there is more than one.
        boolean multiAllowed = true;
        boolean multiEnabled = false;
        if(dataCollector != null) {
            boolean multiEnabledSet = dataCollector.isResourceSet("multipleGraphableEnabled");
            multiEnabled = dataCollector.getMultipleGraphableEnabled();
            if(multiEnabledSet) {
                multiAllowed = multiEnabled;
            }
        }
		if(multiEnabled || (multiAllowed && realGraphables.size() > 1 )){
		    CheckedColorTreeControler dataSetTree = new CheckedColorTreeControler();
            JComponent treeComponent = 
                dataSetTree.setup(this);
            
            dataGraph.add(treeComponent, BorderLayout.WEST);
		}
	}

	protected void initLabels() {
		OTObjectList pfDPLabels = otDataGraph.getLabels();

        //Load the data point labels
        for (int i=0; i<pfDPLabels.size(); i++){
        	Object obj = pfDPLabels.get(i);
        	if(obj instanceof OTDataPointLabel) {
    			OTDataPointLabel otDPLabel = (OTDataPointLabel)obj;
            	
    			//Create a data point label
    			DataPointLabel l = (DataPointLabel)otDPLabel.createWrappedObject();

                // find the correct graphable for this label
                OTDataGraphable otGraphable = otDPLabel.getDataGraphable();
                if(otGraphable != null) {
                    l.setDataGraphable((DataGraphable)otDataGraphableMap.get(otGraphable));
                    l.setForeground(getDataGraphable(otGraphable).getColor());
                }
    			l.setGraphableList(dataGraph.getObjList());
    			notesLayer.add(l);			        		
        	} else if (obj instanceof OTDataPointRuler) {
    			OTDataPointRuler otDPRuler = (OTDataPointRuler)obj;
            	
    			//Create a data point label
    			DataPointRuler r = (DataPointRuler)otDPRuler.createWrappedObject();

                // find the correct graphable for this label
                OTDataGraphable otGraphable = otDPRuler.getDataGraphable();
                if(otGraphable != null) {
                    r.setDataGraphable((DataGraphable)otDataGraphableMap.get(otGraphable));
                }
    			r.setGraphableList(dataGraph.getObjList());
    			notesLayer.add(r);			
        	}
        }
        
		notesLayer.addGraphableListListener(this);
		
		GraphableList graphableList = dataGraph.getObjList();
		graphableList.addGraphableListListener(this);				
	}
	
	public static class UnknownUnit implements DataDimension
	{
		String unit;
		
		public UnknownUnit(String unit)
		{
			this.unit = unit;
		}
		
		/* (non-Javadoc)
		 * @see org.concord.framework.data.DataDimension#getDimension()
		 */
		public String getDimension() 
		{
			return unit;
		}
		
		/* (non-Javadoc)
		 * @see org.concord.framework.data.DataDimension#setDimension(java.lang.String)
		 */
		public void setDimension(String dimension) 
		{
			unit = dimension;
		}
	}
    
    protected DataGraphable addGraphable(String name, Color color, 
            OTDataGraphable prototype)
        throws Exception
    {
        OTObjectService service = getOTDataGraph().getOTObjectService();

        OTDataGraphable otGraphable = 
            (OTDataGraphable)service.createObject(OTDataGraphable.class);

        if(prototype == null) {
        
            DataProducer sourceDataProducer = getSourceDataProducer();
            if(sourceDataProducer != null) {
                DataProducer producer = 
                    (DataProducer)service.createObject(sourceDataProducer.getClass());
                
                if(producer instanceof Copyable) {
                    producer = 
                        (DataProducer)((Copyable)sourceDataProducer).getCopy();
                }
                otGraphable.setDataProducer(producer);
            }
            
            otGraphable.setDrawMarks(false);
            otGraphable.setName(name);
        } else {
            prototype.copyInto(otGraphable);
        }
        
        otGraphable.setColor(color);
        DataGraphable graphable = (DataGraphable)otGraphable.createWrappedObject();
        
        otDataGraphableMap.put(otGraphable, graphable);
        otDataGraph.getGraphables().add(otGraphable);
        dataGraph.addDataGraphable(graphable);
        
        return graphable;
    }
    
    public Object addItem(Object parent, String name, Color color)
    {
        DataGraphable newGraphable = null;
        
        OTObjectList prototypes = 
            getOTDataGraph().getPrototypeGraphables();
        
        try {
            if(prototypes == null || prototypes.size() == 0) {
                newGraphable = addGraphable(name, color, null);
            } else {
                for(int i=0; i<prototypes.size(); i++){
                    newGraphable = addGraphable(name, color, 
                            (OTDataGraphable)prototypes.get(i));
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return newGraphable;
    }
    
    public Object removeItem(Object parent, Object item)
    {
        DataGraphable dataGraphable = (DataGraphable)item;
        dataGraph.removeDataGraphable(dataGraphable);
        //dataGraphable.setVisible(false);
        OTDataGraphable otDataGraphable = getOTDataGraphable(dataGraphable);
        otDataGraph.getGraphables().remove(otDataGraphable);
        removeLabelsFrom(otDataGraphable);
        otDataGraphableMap.remove(dataGraphable);
        return null;
    }
    
    public void updateItems()
    {
        getDataGraph().repaint();
    }
    
    public Color getItemColor(Object item)
    {
        return ((DataGraphable)item).getColor();
    }
    
    public String getItemLabel(Object item)
    {
        return ((DataGraphable)item).getLabel();
    }
    
    public void setItemLabel(Object item, String label)
    {
        ((DataGraphable)item).setLabel(label);
        
    }
    
    public void setItemChecked(Object item, boolean checked)
    {
        ((DataGraphable)item).setVisible(checked);
        
    }    
    public String getItemTypeName()
    {
        return "Data Set";
    }

    public Vector getItems(Object parent)
    {
        return (Vector)(dataGraph.getObjList().clone());
    }

    public void reset()
    {
        if(sourceDataProducer == null) {
            return;
        }
        sourceDataProducer.reset();        
    }

    public void stop()
    {
        if(sourceDataProducer == null) {
            return;
        }
        sourceDataProducer.stop();
    }

    public void start()
    {
        if(sourceDataProducer == null) {
            return;
        }
        sourceDataProducer.start();        
    }

    public Color getNewColor() {
        Color color = null;
        
        Vector graphables = dataGraph.getObjList();
        for(int i = 0; i < colors.length; i++) {
            color = colors[i];
            boolean uniqueColor = true;
            for(int j=0; j<graphables.size(); j++) {
                Color graphableColor = getItemColor(graphables.get(j));
                
                if(graphableColor.equals(colors[i])){
                    uniqueColor = false;
                    break;                    
                }
            }
            if(uniqueColor) {
                break;
            }
        }

        if(color == null) color = Color.BLACK;

        return color;       
    }
}
