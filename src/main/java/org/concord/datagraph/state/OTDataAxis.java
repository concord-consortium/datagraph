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
 * $Revision: 1.7 $
 * $Date: 2007-08-30 21:16:06 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.OTResourceMap;


/**
 * PfDataAxis
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public interface OTDataAxis
	extends OTObjectInterface
{
	public float getMin();
	public void setMin(float min);
	
	public float getMax();
	public void setMax(float max);

	public void setLabel(String label);
	public String getLabel();
	
	public void setUnits(String units);
	public String getUnits();
	
	public void setIntervalWorld(int units);
	public int getIntervalWorld();
	
	public OTObjectList getGraphables();
	
	public String getLabelFormat();
	public void setLabelFormat(String labelFormat);
	public static String FORMAT_ENGINEERING = "Engineering";	// engineering notation, exponents are multiples of 3
	public static String FORMAT_NONE = "None";					// no formatting
	public static String DEFAULT_labelFormat = FORMAT_ENGINEERING;
	
	/**
	 * A resource map, keys are doubles, values are strings.
	 * The string will replace the default numeric label at that value.
	 * 
	 * If it looks like it's an image url, try to add an image to the axis. For now, if
	 * it's an image, we'll always keep the grid numbers as well. Eventually this should be 
	 * settable.
	 * 
	 * Image urls can contain their size within them. For instance, "example.jpg" will always
	 * be scaled to 25x25, but "example-50px.jpg" take example.jpg and scale it to 50x50
	 * 
	 * Also, you can set *both* a text override and an image by using '::', e.g. 'Red::red-image.jpg'. This
	 * will set the text of the label to "Red" and the image to "red-image.jpg." At it's most complex, you
	 * could have 'Big plant::images/big-plant-100px.jpg,' which would set the text of the label to "Big plant," the
	 * image to 'images/big-plant.jpg,' and scale the image to 100x100 pixels.
	 * @return
	 */
	public OTResourceMap getCustomGridLabels();
	
	public static boolean DEFAULT_locked = false;
	public boolean isLocked();
	public void setLocked(boolean locked);
}
