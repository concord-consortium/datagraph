/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.concord.datagraph.ui;

import org.concord.data.Unit;
import org.concord.framework.data.DataDimension;
import org.concord.graph.ui.SingleAxisGrid;

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SingleDataAxisGrid 
	extends SingleAxisGrid 
{
	DataDimension unit = null;
	
	public SingleDataAxisGrid(int orientation)
	{
		super(orientation);
	}

	public void setUnit(DataDimension unit)
	{
		this.unit = unit;
	}
	
	public String getUnitString()
	{
		if (unit != null){
			return unit.getDimension();			
		}
		
		return null;
	}
	
	protected String getAxisLabelToDraw()
	{
		String unitStr = getUnitString();
		if (unitStr != null){ 
			return getAxisLabel() + " (" + unitStr + ")";
		}
		
		return getAxisLabel();
	}
	
}
