/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2005-03-18 09:52:21 $
 * $Author: imoncada $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

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
	
	public static float DEFAULT_x = 0;
	public float getX();
	public void setX(float x);

	public static float DEFAULT_y = 0;
	public float getY();
	public void setY(float y);

	public String getText();
	public void setText(String text);

	public static boolean DEFAULT_isDataPoint = true;
	public boolean getIsDataPoint();
	public void setIsDataPoint(boolean y);
}
