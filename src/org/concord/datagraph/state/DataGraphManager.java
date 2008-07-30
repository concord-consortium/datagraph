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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.concord.data.Unit;
import org.concord.data.state.OTDataProducer;
import org.concord.data.ui.DataFlowControlAction;
import org.concord.data.ui.DataFlowControlButton;
import org.concord.data.ui.DataFlowControlToolBar;
import org.concord.data.ui.DataStoreLabel;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.datagraph.ui.AddDataPointLabelAction;
import org.concord.datagraph.ui.AddDataPointLabelActionExt;
import org.concord.datagraph.ui.AutoScaleAction;
import org.concord.datagraph.ui.DataAnnotation;
import org.concord.datagraph.ui.DataGraph;
import org.concord.datagraph.ui.SingleDataAxisGrid;
import org.concord.framework.data.DataDimension;
import org.concord.framework.data.DataFlow;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DefaultMultipleDataProducer;
import org.concord.framework.otrunk.OTChangeEvent;
import org.concord.framework.otrunk.OTChangeListener;
import org.concord.framework.otrunk.OTControllerService;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTObjectService;
import org.concord.framework.otrunk.view.OTControllerServiceFactory;
import org.concord.framework.otrunk.view.OTJComponentViewContext;
import org.concord.framework.otrunk.view.OTViewContext;
import org.concord.framework.util.CheckedColorTreeModel;
import org.concord.framework.util.Copyable;
import org.concord.graph.engine.Graphable;
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
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DataGraphManager implements OTChangeListener, ChangeListener,
		CheckedColorTreeModel, DataFlow {
	OTDataCollector otDataCollector;
	OTDataGraph otDataGraph;
	DataGraph dataGraph;

	DataGraphable sourceGraphable;
	DataStoreLabel valueLabel;

	OTObjectList labels;
	SelectableList notesLayer;

	OTDataAxis xOTAxis;
	OTDataAxis yOTAxis;

	JPanel bottomPanel;
	DataFlowControlToolBar toolBar = null;

	boolean isCausingOTChange = false;
	boolean isCausingRealObjChange = false;
	
	boolean instantRestart = false;				//sets whether start button is enabled after stop

	boolean showDataControls;

	DataFlowControlButton bStart;
	DataFlowControlButton bStop;
	DataFlowControlButton bClear;

	Color[] colors = { Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN,
			Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW,
			Color.BLACK };

	protected OTControllerService controllerService;
	private OTViewContext viewContext;
	private OTJComponentViewContext jComponentViewContext;
	private KeyEventDispatcher treeDispatcher;

	/**
	 * @param serviceProvider
	 * @param jComponentViewContext
	 *            TODO
	 * 
	 */
	public DataGraphManager(OTDataGraph pfObject,
			OTViewContext serviceProvider, boolean showDataControls,
			OTJComponentViewContext jComponentViewContext) {
		this.otDataGraph = pfObject;
		if (pfObject instanceof OTDataCollector)
			otDataCollector = (OTDataCollector) pfObject;
		this.showDataControls = showDataControls;
		this.viewContext = serviceProvider;
		this.jComponentViewContext = jComponentViewContext;

		initialize();
	}

	public Object getViewService(Class serviceClass) {
		return viewContext.getViewService(serviceClass);
	}

	public OTControllerService getControllerService() {
		return controllerService;
	}

	public DataProducer getSourceDataProducer() {
		// This will return the potential dataProducer of the
		// sourceGraphable, this might be different than the current
		// dataProducer. This is because of how the producerDataStores
		// interact with dataDescriptions coming from their data Producer
		OTDataGraphable otSourceGraphable = (OTDataGraphable) controllerService
				.getOTObject(sourceGraphable);
		if (otSourceGraphable == null) {
			return null;
		}
		return getDataProducer(otSourceGraphable);
	}

	public float getLastValue() {
		if (valueLabel == null) {
			return Float.NaN;
		}
		return valueLabel.getValue();
	}

	/**
	 * @return
	 */
	public JPanel getBottomPanel() {
		return bottomPanel;
	}

	public DataGraph getDataGraph() {
		return dataGraph;
	}

	protected OTDataGraph getOTDataGraph() {
		return otDataGraph;
	}

	public DataFlowControlToolBar getFlowToolBar() {
		return toolBar;
	}

	public DataFlowControlToolBar createFlowToolBar() {
		DataFlowControlToolBar toolbar = new DataFlowControlToolBar(false);

		bStart = new DataFlowControlButton(
				DataFlowControlAction.FLOW_CONTROL_START);
		toolbar.add(bStart);

		bStop = new DataFlowControlButton(
				DataFlowControlAction.FLOW_CONTROL_STOP);
		bStop.setEnabled(false);
		toolbar.add(bStop);

		bClear = new DataFlowControlButton(
				DataFlowControlAction.FLOW_CONTROL_RESET);
		bClear.setText("Clear");
		toolbar.add(bClear);

		// The below wrapper is to fix the problem where the dataGraph was reset
		// all of the graphables
		// because the datagraph doesn't know which is the selected graphable
		// the DataGraphManager
		// needs to take care of the reset. However DataGraphManager already
		// implements DataFlow so
		// so it can be used in the case of multiple graph panels.
		// FIXME: this should be cleaned up so that DataGraphManager's
		// implementation of DataFlow can be used
		// both in the case of the multiple graphs and single graph.
		toolbar.addDataFlowObject(new DataFlow() {

			public void reset() {
				// We bypass the normal dataGraph reset method so only the
				// selected graphable is cleared.
				if (sourceGraphable != null) {
					sourceGraphable.reset();
				}
				
				// sfentress: If our data producer is a MultipleDataProducer, 
				// clear should clear all data lines that are being actively 
				// generated by that data producer, including any graphables 
				// with the same producer
				DataProducer dp = getSourceDataProducer();
				if (dp instanceof DefaultMultipleDataProducer) {
					if (((DefaultMultipleDataProducer) dp).getClearAll()){
						Vector graphablesToReset = getDataGraph().getAllGraphables(dp);
						for (int i = 0; i < graphablesToReset.size(); i++) {
	                       ((DataGraphable)graphablesToReset.get(i)).reset();
                        }
					}
				}
			}

			public void start() {
				dataGraph.start();
			}

			public void stop() {
				dataGraph.stop();
			}

		});

		bStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bClear.setEnabled(true);
				bStart.setEnabled(false);
				bStop.setEnabled(true);
			}
		});

		bStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bClear.setEnabled(true);
				if (!instantRestart){
					bStart.setEnabled(false);
				} else {
					bStart.setEnabled(true);
				}
				bStop.setEnabled(false);
			}
		});

		bClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bClear.setEnabled(false);
				bStart.setEnabled(true);
				bStop.setEnabled(false);
			}
		});

		return toolbar;
	}

	public void setToolBarEnabled(boolean enabled) {
		if (bStart != null)
			bStart.setEnabled(enabled);
		if (bStop != null)
			bStop.setEnabled(enabled);
		if (bClear != null)
			bClear.setEnabled(enabled);
	}

	public void setToolbarVisible(boolean visible) {
		GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
		if (gwToolbar != null) {
			// FIXME
			gwToolbar.setVisible(visible);
		}
	}

	/**
	 * This only works for graphables that came from a loaded pfgraphables. It
	 * doesn't yet handel cases where new graphables are created by some
	 * external thing
	 * 
	 */
	public void updateState(Object obj) {
		// If the change was due to the graph area or coordinate system
		if (obj == dataGraph.getGraphArea()
				|| obj == dataGraph.getGraphArea().getCoordinateSystem()) {

			Grid2D grid = dataGraph.getGrid();

			xOTAxis.setDoNotifyChangeListeners(false);
			yOTAxis.setDoNotifyChangeListeners(false);

			xOTAxis.setMin((float) dataGraph.getMinXAxisWorld());
			xOTAxis.setMax((float) dataGraph.getMaxXAxisWorld());
			yOTAxis.setMin((float) dataGraph.getMinYAxisWorld());
			yOTAxis.setMax((float) dataGraph.getMaxYAxisWorld());

			SingleAxisGrid sXAxis = grid.getXGrid();
			if (sXAxis.getAxisLabel() != null) {
				xOTAxis.setLabel(sXAxis.getAxisLabel());
			}

			SingleAxisGrid sYAxis = grid.getYGrid();
			if (sYAxis.getAxisLabel() != null) {
				yOTAxis.setLabel(sYAxis.getAxisLabel());
			}

			isCausingOTChange = true;

			// This is a general notification of a change, not one specific to a
			// property
			xOTAxis.notifyOTChange(null, null, null, null);
			yOTAxis.notifyOTChange(null, null, null, null);
			isCausingOTChange = false;
		}

		/*
		 * if(obj instanceof DataGraphable) { OTDataGraphable source =
		 * dataCollector.getSource();
		 * 
		 * source.saveObject(obj); }
		 */
	}

	/**
	 * @see org.concord.framework.otrunk.OTChangeListener#stateChanged(org.concord.framework.otrunk.OTChangeEvent)
	 */
	public void stateChanged(OTChangeEvent e) {
		System.err.println("---- OT state changed " + e.getSource() + " - "
				+ isCausingOTChange + " " + this);
		// System.out.println(e.getOperation() +" "+e.getValue());

		if (isCausingOTChange) {
			// we are the cause of this change
			return;
		}

		if (e.getSource() == xOTAxis || e.getSource() == yOTAxis) {
			dataGraph.setLimitsAxisWorld(xOTAxis.getMin(), xOTAxis.getMax(),
					yOTAxis.getMin(), yOTAxis.getMax());
		} else if (e.getSource() == otDataGraph) {
			// OT Data Graph has changed. We need to update the real DataGraph
			// Find out what kind of change it is and whether it was a data
			// graphable or a label
			if (e.getOperation() == OTChangeEvent.OP_ADD
					|| e.getOperation() == OTChangeEvent.OP_REMOVE) {

				// Graphable added or removed
				OTObject otGraphable = (OTObject) e.getValue();
				if (e.getProperty().equals("graphables")) {
					initNewGraphable(otGraphable);
				} else if (e.getProperty().equals("labels")) {
					initNewLabel(otGraphable);
				}
			}
		} else if (e.getSource() instanceof OTDataGraphable) {
			// A OT data graphable changed (not implemented anymore)
			/*
			 * if (e.getOperation() == OTChangeEvent.OP_SET){ OTObject
			 * otGraphable = (OTObject)e.getSource();
			 * updateGraphable(otGraphable); }
			 */
		}
	}

	/**
	 * Event triggered by the graph area changing
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		// System.out.println("state changed "+e.getSource());

		Object source = e.getSource();
		updateState(source);
	}

	public void setSelectedItem(Object item, boolean checked) {
		setSelectedDataGraphable((DataGraphable) item, checked);
	}

	protected void setSelectedDataGraphable(DataGraphable dg, boolean visible) {
		if (dg == null) {
			setToolBarEnabled(false);
			return;
		}

		DataGraphable oldGraphable = sourceGraphable;
		sourceGraphable = dg;
		updateBottomPanel(oldGraphable, sourceGraphable);

		dg.setVisible(visible);

		if (dg.isLocked()) {
			setToolBarEnabled(false);
		} else {
			setToolBarEnabled(visible);
		}
	}

	private void removeLabelsFrom(DataGraphable dataGraphable,
			GraphableList graphables) {
		for (int i = 0; i < graphables.size(); i++) {
			Object graphable = graphables.get(i);
			if (graphable instanceof DataAnnotation) {
				if (((DataAnnotation) graphable).getDataGraphable() == dataGraphable) {
					graphables.remove(i);
					continue;
				}
			}
		}
	}

	public static void setupAxisLabel(SingleDataAxisGrid sAxis, OTDataAxis axis) {
		if (axis.getLabel() != null) {
			sAxis.setAxisLabel(axis.getLabel());
		}

		if (axis.getUnits() != null) {
			String unitStr = axis.getUnits();
			Unit unit = Unit.findUnit(unitStr);
			if (unit == null) {
				System.err.println("Can't find unit: " + unitStr);
				sAxis.setUnit(new UnknownUnit(unitStr));
			} else {
				sAxis.setUnit(unit);
			}
		}

		if (axis.isResourceSet("intervalWorld")) {
			sAxis.setIntervalFixedWorld(axis.getIntervalWorld());
		}
	}

	public void initialize() {
		OTControllerServiceFactory controllerServiceFactory = (OTControllerServiceFactory) getViewService(OTControllerServiceFactory.class);

		controllerService = controllerServiceFactory.createControllerService();
		controllerService.addService(OTViewContext.class, viewContext);
		controllerService.addService(OTJComponentViewContext.class,
				jComponentViewContext);

		dataGraph = new DataGraph();

		if (otDataGraph.isResourceSet("showToolbar")
				&& !otDataGraph.getShowToolbar()) {
			dataGraph.setToolBar(null);
		} else {
			dataGraph.changeToDataGraphToolbar();
		}

		initGraphables();
		dataGraph.setAutoFitMode(DataGraph.AUTO_SCROLL_RUNNING_MODE);

		dataGraph.setFocusable(true);

		notesLayer = new SelectableList();
		dataGraph.getGraph().add(notesLayer);

		if (dataGraph.getToolBar() != null) {

			SelectableToggleButton addNoteButton = new SelectableToggleButton(
					new AddDataPointLabelAction(notesLayer, dataGraph
							.getObjList(), dataGraph.getToolBar()));
			dataGraph.getToolBar().addButton(addNoteButton,
					"Add a note to a point in the graph");

			// DataPointRuler need to be explicitly enabled to show per Brad's
			// request.

			if (otDataCollector != null && otDataCollector.getRulerEnabled()) {
				SelectableToggleButton addNoteButton2 = new SelectableToggleButton(
						new AddDataPointLabelActionExt(notesLayer, dataGraph
								.getObjList(), dataGraph.getToolBar()));
				dataGraph.getToolBar().addButton(addNoteButton2,
						"Add a ruler to a point in the graph");
			}

			// AutoScale need to be explicitly enabled to show per Brad's
			// request.
			if (otDataCollector != null && otDataCollector.getAutoScaleEnabled()) {
				JButton autoScaleButton = new JButton(new AutoScaleAction(
						dataGraph));
				JButton autoScaleXButton = new JButton(new AutoScaleAction(
						AutoScaleAction.AUTOSCALE_X, dataGraph));
				JButton autoScaleYButton = new JButton(new AutoScaleAction(
						AutoScaleAction.AUTOSCALE_Y, dataGraph));

				dataGraph.getToolBar().addButton(autoScaleButton,
						"Autoscale the graph");
				dataGraph.getToolBar().addButton(autoScaleXButton,
						"Autoscale X axis");
				dataGraph.getToolBar().addButton(autoScaleYButton,
						"Autoscale Y axis");
			}

			KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			
			treeDispatcher = new KeyEventDispatcher(){					
				public boolean dispatchKeyEvent(KeyEvent e) {
					if(dataGraph.isAncestorOf(e.getComponent()) &&
							((e.getModifiers() | KeyEvent.KEY_RELEASED) != 0) &&
					        ((e.getModifiersEx() & java.awt.event.InputEvent.CTRL_DOWN_MASK) != 0) &&
					        (e.getKeyCode() == java.awt.event.KeyEvent.VK_T)) {
						GraphTreeView gtv = new GraphTreeView();
						gtv.setGraph(dataGraph.getGraph());
						GraphTreeView.showAsDialog(gtv, "graph tree");
						return true;
					}
					
					return false;
				}
				
			};

			focusManager.addKeyEventDispatcher(treeDispatcher);
			
		}

		xOTAxis = otDataGraph.getXDataAxis();
		xOTAxis.addOTChangeListener(this);
		yOTAxis = otDataGraph.getYDataAxis();
		yOTAxis.addOTChangeListener(this);

		dataGraph.setLimitsAxisWorld(xOTAxis.getMin(), xOTAxis.getMax(),
				yOTAxis.getMin(), yOTAxis.getMax());

		Grid2D grid = dataGraph.getGrid();

		SingleDataAxisGrid sXAxis = (SingleDataAxisGrid) grid.getXGrid();

		// DataGraphStateManager.setupAxisLabel(sXAxis, xOTAxis);
		setupAxisLabel(sXAxis, xOTAxis);

		SingleDataAxisGrid sYAxis = (SingleDataAxisGrid) grid.getYGrid();
		// DataGraphStateManager.setupAxisLabel(sYAxis, yOTAxis);
		setupAxisLabel(sYAxis, yOTAxis);

		dataGraph.setPreferredSize(new Dimension(400, 320));

		dataGraph.getGraphArea().addChangeListener(this);

		otDataGraph.addOTChangeListener(this);

		initLabels();
	}

	protected void initGraphables() {
		OTObjectList pfGraphables = otDataGraph.getGraphables();
		Vector realGraphables = new Vector();

		// for each list item get the data producer object
		// add it to the data graph
		for (int i = 0; i < pfGraphables.size(); i++) {
			DataGraphable realGraphable = initNewGraphable((OTObject) pfGraphables
					.get(i));

			realGraphables.add(realGraphable);
		}

		OTDataGraphable source = null;
		if (otDataCollector != null) {
			source = otDataCollector.getSource();
		}

		String title = otDataGraph.getTitle();		
		
		if (source != null) {
			if (title == null) {
				title = source.getName();
			}

			sourceGraphable = (DataGraphable) controllerService
					.getRealObject(source);

			// dProducer.getDataDescription().setDt(0.1f);
			if (sourceGraphable instanceof ControllableDataGraphable) {
				bottomPanel = new JPanel(new FlowLayout());
				JButton clearButton = new JButton("Clear");
				clearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dataGraph.reset();
					}
				});

				DrawingAction a = new DrawingAction();
				a.setDrawingObject((ControllableDataGraphable) sourceGraphable);
				GraphWindowToolBar gwToolbar = dataGraph.getToolBar();
				if (gwToolbar != null) {
					gwToolbar.addButton(new SelectableToggleButton(a),
							"Draw a function", 0, false, true);
				}

				bottomPanel.add(clearButton);
				// bottomPanel.add(about);

				dataGraph.add(bottomPanel, BorderLayout.SOUTH);

			} else if (showDataControls) {
				bottomPanel = new JPanel(new FlowLayout());
				toolBar = createFlowToolBar();

				updateBottomPanel(null, sourceGraphable);

				dataGraph.add(bottomPanel, BorderLayout.SOUTH);
			}

			if (sourceGraphable != null) {
				realGraphables.insertElementAt(sourceGraphable, 0);
				dataGraph.addDataGraphable(sourceGraphable);
			}
		}

		if (title != null) {
			dataGraph.setTitle(title);
		}

		// If the enabled is not set then the multiple graphable control is
		// shown if there is more than one graphable
		// If the enabled is set to false then the multiple graphable control
		// is not shown even if there is more than one.
		// This whole logic should be re-worked
		boolean multiAllowed = true;
		boolean multiEnabled = false;
		if (otDataCollector != null) {
			boolean multiEnabledSet = otDataCollector
					.isResourceSet("multipleGraphableEnabled");
			multiEnabled = otDataCollector.getMultipleGraphableEnabled();
			if (multiEnabledSet) {
				multiAllowed = multiEnabled;
			}
		} else {
			boolean multiEnabledSet = otDataGraph
					.isResourceSet("showGraphableList");
			if (multiEnabledSet) {
				multiEnabled = otDataGraph.getShowGraphableList();
				multiAllowed = multiEnabled;
			}
		}
		if (multiEnabled || (multiAllowed && realGraphables.size() > 1)) {
			CheckedColorTreeControler dataSetTree = new CheckedColorTreeControler();
			JComponent treeComponent = dataSetTree.setup(this, true, otDataGraph.getGraphableListEditable());

			// The source should be the last item because it was setup that
			// way above. We want it selected
			// This works but there is some strange behavior here.
			// the dataSetTree.setup changes the sourceGraphable. because it
			// sets the
			// selected to be the first realGraphable.
			if (source != null) {
				dataSetTree.setSelectedRow(realGraphables.size() - 1);
			}
			dataGraph.add(treeComponent, BorderLayout.WEST);
		}

		// Listen to the graphable list
		GraphableList graphableList = dataGraph.getObjList();
		graphableList
				.addGraphableListListener(new MainLayerGraphableListener());
	}

	protected void updateBottomPanel(DataGraphable oldSourceGraphable,
			DataGraphable newSourceGraphable) {
		if (toolBar == null) {
			return;
		}

		Dimension labelSize = null;
		Point labelLocation = null;

		if (oldSourceGraphable != null) {
			// disconnect toolBar from the last data producer
			// getting the data producer from the graphable is not always safe
			// the graphable stores the dataproducer in its datastore, so
			// if the datastore hasn't been updated to use the correct producer
			// then this will return the wrong thing
			DataProducer oldSourceDataProducer = oldSourceGraphable
					.findDataProducer();
			toolBar.removeDataFlowObject(oldSourceDataProducer);
			labelSize = valueLabel.getSize();
			labelLocation = valueLabel.getLocation();
			bottomPanel.remove(valueLabel);
			bottomPanel.remove(toolBar);
		}

		// It isn't good to call newSourceGraphable.findDataProducer()
		// because the producer might not be set data store of the
		// real graphable yet. This setting is delay so previous data stored
		// in the datastore does not get messed up.
		OTDataGraphable newOTSourceGraphable = (OTDataGraphable) controllerService
				.getOTObject(newSourceGraphable);
		DataProducer newSourceDataProducer = getDataProducer(newOTSourceGraphable);
		toolBar.addDataFlowObject(newSourceDataProducer);

		DataStore dataStore = newSourceGraphable.getDataStore();
		if (dataStore.getTotalNumSamples() > 0) {
			bStart.setEnabled(false);
		}

		// TODO we need a way to check if the datastore is running or not
		// if it is not running then the stop button shoudl be disabled.

		valueLabel = new DataStoreLabel(newSourceGraphable, 1);
		valueLabel.setColumns(8);

		bottomPanel.setLayout(new FlowLayout());
		if (oldSourceGraphable != null) {
			valueLabel.setSize(labelSize);
			valueLabel.setLocation(labelLocation);
		}
		bottomPanel.add(valueLabel);
		bottomPanel.add(toolBar);
	}

	/**
	 * Called when a new OT graphable was added and we need to create a real
	 * graphable object for it and add it to the Data Graph
	 * 
	 * @param object
	 * @return the new DataGraphable just added to the Data Graph
	 */
	protected DataGraphable initNewGraphable(OTObject otGraphable) {
		isCausingRealObjChange = true;

		DataGraphable realGraphable = (DataGraphable) controllerService
				.getRealObject(otGraphable);

		if (realGraphable == null) {
			System.err
					.println("Unable to get realGraphable from controllerService");
			return null;
		}
		if (realGraphable.getDataProducer() != null) {
			System.err
					.println("Trying to display a background graphable with a data producer");
		}

		dataGraph.addDataGraphable(realGraphable);

		// Listen to OT graphable changes (not anymore! the Graphable controller
		// takes care of this)
		// ((OTDataGraphable)otGraphable).addOTChangeListener(this);

		isCausingRealObjChange = false;

		return realGraphable;
	}

	/**
	 * Called when an OT graphable is changed and we need to update the real
	 * graphable object too
	 * 
	 * @param otGraphable
	 */
	protected void updateGraphable(OTObject otGraphable) {
		isCausingRealObjChange = true;

		DataGraphable realGraphable = (DataGraphable) controllerService
				.getRealObject(otGraphable);

		// Call loadRealObject on the controller
		controllerService.loadRealObject(otGraphable, realGraphable);

		isCausingRealObjChange = false;
	}

	protected void initLabels() {
		OTObjectList pfDPLabels = otDataGraph.getLabels();

		// Load the data point labels
		for (int i = 0; i < pfDPLabels.size(); i++) {
			OTObject obj = pfDPLabels.get(i);
			initNewLabel(obj);
		}

		// Listen to the graphable list
		notesLayer.addGraphableListListener(new NotesLayerGraphableListener());
	}

	/**
	 * @param obj
	 */
	private Graphable initNewLabel(OTObject obj) {
		Graphable label = (Graphable) controllerService.getRealObject(obj);

		if (label instanceof DataAnnotation) {
			((DataAnnotation) label).setGraphableList(dataGraph.getObjList());
		}
		notesLayer.add(label);

		return label;
	}

	public static class UnknownUnit implements DataDimension {
		String unit;

		public UnknownUnit(String unit) {
			this.unit = unit;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.concord.framework.data.DataDimension#getDimension()
		 */
		public String getDimension() {
			return unit;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.concord.framework.data.DataDimension#setDimension(java.lang.String)
		 */
		public void setDimension(String dimension) {
			unit = dimension;
		}
	}

	/**
	 * This should work with Graphable protoypes not OTGraphable prototypes that
	 * would make it more generally useful.
	 * 
	 * @param name
	 * @param color
	 * @param prototype
	 * @return
	 * @throws Exception
	 */
	protected DataGraphable addGraphable(String name, Color color,
			OTDataGraphable prototype) throws Exception {
		OTObjectService service = getOTDataGraph().getOTObjectService();

		OTDataGraphable otGraphable = null;

		if (prototype == null) {

			otGraphable = (OTDataGraphable) service
					.createObject(OTDataGraphable.class);
			DataProducer sourceDataProducer = getSourceDataProducer();
			if (sourceDataProducer != null) {
				// copy the the producer,
				// TODO there might be some way to use the same producer on 2
				// datastores
				// that would be a fall back for un copyable data producers
				if (sourceDataProducer instanceof Copyable) {
					DataProducer producer = (DataProducer) ((Copyable) sourceDataProducer)
							.getCopy();
					setDataProducer(otGraphable, producer);
				} else {
					System.err
							.println("Cannot copy the source data producer:\n"
									+ "  "
									+ sourceDataProducer
									+ "\n"
									+ "  It doesn't implement the Copyable interface");
				}
			}

			otGraphable.setDrawMarks(false);

			// Might need to set default values for color
			// and the name.
		} else {
			otGraphable = (OTDataGraphable) service.copyObject(prototype, -1);
		}

		DataGraphable graphable = (DataGraphable) controllerService
				.getRealObject(otGraphable);

		// I don't know if this should be done or not
		if (name != null) {
			// This doesn't fire a graphable changed event it should
			graphable.setLabel(name);
		}

		if (color != null) {
			graphable.setColor(color);
		}

		dataGraph.addDataGraphable(graphable);
		// adding the graphable to the dataGraph will cause an event to
		// be thrown so the ot graphable is added to the ot data graph
		// otDataGraph.getGraphables().add(otGraphable);

		return graphable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.concord.framework.util.CheckedColorTreeModel#addItem(java.lang.Object,
	 *      java.lang.String, java.awt.Color)
	 */
	public Object addItem(Object parent, String name, Color color) {
		DataGraphable newGraphable = null;

		OTObjectList prototypes = getOTDataGraph().getPrototypeGraphables();

		try {
			if (prototypes == null || prototypes.size() == 0) {
				newGraphable = addGraphable(name, color, null);
			} else {
				for (int i = 0; i < prototypes.size(); i++) {
					newGraphable = addGraphable(name, color,
							(OTDataGraphable) prototypes.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newGraphable;
	}

	public Object removeItem(Object parent, Object item) {
		DataGraphable dataGraphable = (DataGraphable) item;
		dataGraph.removeDataGraphable(dataGraphable);

		// remove any graphables that reference this one in
		// either of the 2 graphable lists
		removeLabelsFrom(dataGraphable, dataGraph.getObjList());
		removeLabelsFrom(dataGraphable, notesLayer);

		return null;
	}

	public void updateItems() {
		getDataGraph().repaint();
	}

	public Color getItemColor(Object item) {
		return ((DataGraphable) item).getColor();
	}

	public String getItemLabel(Object item) {
		return ((DataGraphable) item).getLabel();
	}

	public void setItemLabel(Object item, String label) {
		((DataGraphable) item).setLabel(label);

	}

	public void setItemChecked(Object item, boolean checked) {
		((DataGraphable) item).setVisible(checked);

	}

	public String getItemTypeName() {
		return "Data Set";
	}

	public Vector getItems(Object parent) {
		return (Vector) (dataGraph.getObjList().clone());
	}

	/**
	 * This method and the other DataFlow methods are used when there are
	 * multiple datagraphs.
	 */
	public void reset() {
		DataProducer sourceDataProducer = getSourceDataProducer();
		if (sourceDataProducer == null) {
			return;
		}
		sourceDataProducer.reset();
	}

	public void stop() {
		DataProducer sourceDataProducer = getSourceDataProducer();
		if (sourceDataProducer == null) {
			return;
		}
		sourceDataProducer.stop();
	}

	public void start() {
		DataProducer sourceDataProducer = getSourceDataProducer();
		if (sourceDataProducer == null) {
			return;
		}
		sourceDataProducer.start();
	}

	public Color getNewColor() {
		Color color = null;

		Vector graphables = dataGraph.getObjList();
		for (int i = 0; i < colors.length; i++) {
			color = colors[i];
			boolean uniqueColor = true;
			for (int j = 0; j < graphables.size(); j++) {
				Color graphableColor = getItemColor(graphables.get(j));

				if (graphableColor.equals(colors[i])) {
					uniqueColor = false;
					break;
				}
			}
			if (uniqueColor) {
				break;
			}
		}

		if (color == null)
			color = Color.BLACK;

		return color;
	}

	public void viewClosed() {
		// Remove all the OT listeners
		xOTAxis.removeOTChangeListener(this);
		yOTAxis.removeOTChangeListener(this);
		otDataGraph.removeOTChangeListener(this);

		// This should call dispose on all the controllers that are syncing
		// the real objects with the ot objects.
		controllerService.dispose();

		KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		focusManager.removeKeyEventDispatcher(treeDispatcher);
		
		// Not anymore!
		// OTObjectList pfGraphables = otDataGraph.getGraphables();
		// for(int i=0; i<pfGraphables.size(); i++) {
		// OTChangeNotifying pfGraphable =
		// (OTChangeNotifying)pfGraphables.get(i);
		// pfGraphable.removeOTChangeListener(this);
		// }
	}

	class NotesLayerGraphableListener implements GraphableListListener {
		public void listGraphableAdded(EventObject e) {
			// Verify we are not triggering this change ourselves
			if (!isCausingRealObjChange) {
				isCausingOTChange = true;
				Object obj = e.getSource();
				OTObject otObject = controllerService.getOTObject(obj);
				otDataGraph.getLabels().add(otObject);
				isCausingOTChange = false;
			}
		}

		public void listGraphableChanged(EventObject e) {
			// TODO verify this is necessary
			// this is just copied from the old code
			// Verify we are not triggering this change ourselves
			if (!isCausingRealObjChange) {
				isCausingOTChange = true;
				updateState(e.getSource());
				isCausingOTChange = false;
			}
		}

		public void listGraphableRemoved(EventObject e) {
			// Verify we are not triggering this change ourselves
			if (!isCausingRealObjChange) {
				isCausingOTChange = true;
				Object obj = e.getSource();
				OTObject otObject = controllerService.getOTObject(obj);
				otDataGraph.getLabels().remove(otObject);
				isCausingOTChange = false;
			}
		}
	}

	class MainLayerGraphableListener implements GraphableListListener {
		public void listGraphableAdded(EventObject e) {
			// TODO verify this is doesn't screw up things
			// Verify we are not triggering this change ourselves
			if (!isCausingRealObjChange) {
				isCausingOTChange = true;
				Object obj = e.getSource();
				OTObject otObject = controllerService.getOTObject(obj);
				otDataGraph.getGraphables().add(otObject);
				isCausingOTChange = false;
			}
		}

		public void listGraphableChanged(EventObject e) {
			// TODO verify this is necessary
			// this is just copied from the old code
			// Verify we are not triggering this change ourselves
			if (!isCausingRealObjChange) {
				isCausingOTChange = true;
				updateState(e.getSource());
				isCausingOTChange = false;
			}
		}

		public void listGraphableRemoved(EventObject e) {
			// TODO verify this is doesn't screw up things
			// Verify we are not triggering this change ourselves
			if (!isCausingRealObjChange) {
				isCausingOTChange = true;
				Object obj = e.getSource();
				OTObject otObject = controllerService.getOTObject(obj);
				otDataGraph.getGraphables().remove(otObject);
				isCausingOTChange = false;
			}
		}
	}

	DataProducer getDataProducer(OTDataGraphable model) {
		OTDataProducer otDataProducer = model.getDataProducer();
		return (DataProducer) controllerService.getRealObject(otDataProducer);
	}

	void setDataProducer(OTDataGraphable model, DataProducer dp) {
		OTDataProducer otDataProducer = (OTDataProducer) controllerService
				.getOTObject(dp);
		model.setDataProducer(otDataProducer);
	}

	public void setOTJComponentViewContext(OTJComponentViewContext viewContext) {
		jComponentViewContext = viewContext;
	}
	
	public void setInstantRestart(boolean instantRestart){
		this.instantRestart = instantRestart;
	}
	
	public boolean getInstantRestart(){
		return instantRestart;
	}
}