/*
 * Last modification information:
 * $Revision: 1.1 $
 * $Date: 2004-12-06 03:26:56 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObject;


/**
 * PfDataGraphable
 * Class name and description
 *
 * Date created: Nov 18, 2004
 *
 * @author scott<p>
 *
 */
public interface OTDataGraphable
	extends OTObject
{
	public static int DEFAULT_color = 0x00FF0000;
	public int getColor();
	public void setColor(int color);
	
	public static boolean DEFAULT_connectPoints = true;
	public boolean getConnectPoints();
	public void setConnectPoints(boolean flag);

	public static boolean DEFAULT_drawMarks = true;
	public boolean getDrawMarks();
	public void setDrawMarks(boolean flag);
	
	public OTDataStore getDataStore();
	public void setDataStore(OTDataStore store);
	
	public OTObject getDataProducer();
	public void setDataProducer(OTObject producer);
	
}
