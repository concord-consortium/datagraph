
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
 * Last modification information:
 * $Revision: 1.4 $
 * $Date: 2005-04-01 05:47:31 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.otrunk.OTObject;
import org.concord.graph.util.ui.BoxTextLabel;


/**
 * PfDataGraphable
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public interface OTDataPointLabel
	extends OTObject
{
	public static int DEFAULT_color = BoxTextLabel.DEFAULT_BACKGROUND_COLOR.getRGB();
	public int getColor();
	public void setColor(int color);
	
	public static float DEFAULT_x = Float.NaN;
	public float getX();
	public void setX(float x);

	public static float DEFAULT_y = Float.NaN;
	public float getY();
	public void setY(float y);

	public static float DEFAULT_xData = Float.NaN;
	public float getXData();
	public void setXData(float x);

	public static float DEFAULT_yData = Float.NaN;
	public float getYData();
	public void setYData(float y);
	
	public String getText();
	public void setText(String text);

	public static boolean DEFAULT_selectable = true;
	public boolean getSelectable();
	public void setSelectable(boolean b);

	public OTDataGraphable getDataGraphable();
	public void setDataGraphable(OTDataGraphable b);
}
