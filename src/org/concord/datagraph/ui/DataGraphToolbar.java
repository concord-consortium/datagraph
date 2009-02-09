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
 * Last modification information:
 * $Revision: 1.8 $
 * $Date: 2007-03-08 22:10:52 $
 * $Author: sfentress $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.ui;

import javax.swing.AbstractButton;

import org.concord.graph.engine.AxisScale;
import org.concord.graph.engine.MultiRegionAxisScale;
import org.concord.graph.examples.GraphWindowToolBar;

/**
 * DataGraphToolbar
 *
 * Date created: Feb 22, 2005
 *
 * @author Scott Cytacki<p>
 * @author Ingrid Moncada<p>
 *
 */
public class DataGraphToolbar extends GraphWindowToolBar
{
	private static final long serialVersionUID = 1L;
	protected AbstractButton selButton;

	public final static int SELECT_BTN = 0;
	public final static int ZOOM_IN_BTN = 1;
	public final static int ZOOM_OUT_BTN = 2;
	public final static int RESTORE_SCALE_BTN = 3;
	
    public DataGraphToolbar()
    {
        super(false);
        
		selButton = addButton(SELECT_BTN);
		addButton(ZOOM_IN_BTN);
		addButton(ZOOM_OUT_BTN);
		addButton(RESTORE_SCALE_BTN);

		setDefaultButton(selButton);
		
    }
    
    /**
     * Creates a tool bar with the buttons specified. The buttons will be
     * added in the order specified, and the first button added will be the
     * default button, which is selected when the graph is first opened.
     * 
     * @param buttons
     */
    public DataGraphToolbar(int[] buttons){
    	super(false);
    	
    	for (int i = 0; i < buttons.length; i++) {
	        AbstractButton button = addButton(buttons[i]);
	        if (i == 0 && button != null){
	        	setDefaultButton(button);
	        }
        }
    	
    }
    
    /**
     * Adds button of the specified type and returns the newly
     * created button.
     * 
     * @param buttonType
     * @return
     */
    public AbstractButton addButton(int buttonType){
    	AbstractButton button = null;
    	switch (buttonType){
    		case SELECT_BTN:
    			button = addButton("arrow.gif", 
    			        "" + MultiRegionAxisScale.DRAGMODE_TRANSLATE_DILATE, 
    			        "Move and Scale graph");
    			selButton = button;
    			break;
    		case ZOOM_IN_BTN:
    			button = addButton("zoomin.gif", 
        		        "" + AxisScale.DRAGMODE_ZOOM_IN, 
        		        "Zoom in to a point");
    			break;
    		case ZOOM_OUT_BTN:
    			button = addButton("zoomout.gif", 
        		        "" + AxisScale.DRAGMODE_ZOOM_OUT, 
        		        "Zoom out from a point");
    			break;
    		case RESTORE_SCALE_BTN:
    			button = addButton("restorescale.gif", 
    					"restorescale", "Restore initial scale", false);
    			break;
    		default:
    			System.err.println("No button of that tye is defined");
    	}
    	return button;
    }
    
	/**
	 * This is called when graph is first viewed, and is a good time to
	 * select the default button.
	 * 
	 * @see org.concord.graph.examples.GraphWindowToolBar#addAxisScale(org.concord.graph.engine.AxisScale)
	 */
	public void addAxisScale(AxisScale ax)
	{
		super.addAxisScale(ax);
		if (getDefaultButton() != null){
			getDefaultButton().doClick();
		}
	}
}
