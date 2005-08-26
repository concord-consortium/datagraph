package org.concord.datagraph.ui;

import org.concord.graph.engine.GraphableList;
import org.concord.graph.util.ui.BoxTextLabel;

/**
 * @author shengyao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AddDataPointLabelActionExt extends AddDataPointLabelAction{

	public AddDataPointLabelActionExt(GraphableList gList, GraphableList objList) {
		super(gList, objList);
		setIcon("toolbar_icon_note_ext.gif");
	}

	/**
	 * @see org.concord.graph.util.control.AddLabelAction.createTextLabel
	 */
	protected BoxTextLabel createTextLabel()
	{
		//DataPointLabelExt label = new DataPointLabelExt(true);
		DataPointLabel label = new DataPointLabel(true);
		label.setGraphableList(dataGraphablesList);
		label.setMessage("Data Point");
		label.setAllowOffLine(true);
		return label;
	}
}
