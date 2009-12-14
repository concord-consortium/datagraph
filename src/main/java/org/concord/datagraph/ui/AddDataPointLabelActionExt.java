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

package org.concord.datagraph.ui;

import org.concord.graph.engine.GraphableList;
import org.concord.graph.examples.GraphWindowToolBar;
import org.concord.graph.util.ui.BoxTextLabel;

/**
 * @author shengyao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AddDataPointLabelActionExt extends AddDataPointLabelAction{

	private static final long serialVersionUID = 1L;
	private GraphWindowToolBar toolBar;
	
	public AddDataPointLabelActionExt(GraphableList gList, GraphableList objList) {
		super(gList, objList);
		setIcon("toolbar_icon_note_ext.gif");
	}

	public AddDataPointLabelActionExt(GraphableList gList, GraphableList objList, GraphWindowToolBar toolBar) {
		super(gList, objList);
		setIcon("toolbar_icon_note_ext.gif");
		this.toolBar = toolBar;
	}

	/**
	 * @see org.concord.graph.util.control.AddLabelAction.createTextLabel
	 */
	protected BoxTextLabel createTextLabel()
	{
		//DataPointLabelExt label = new DataPointLabelExt(true);
		DataPointRuler label = new DataPointRuler(true);
		label.setGraphableList(dataGraphablesList);
		if (toolBar != null)
			label.setToolBar(toolBar);
		return label;
	}
}
