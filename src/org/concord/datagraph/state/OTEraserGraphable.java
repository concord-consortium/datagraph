/**
 * 
 */
package org.concord.datagraph.state;

import org.concord.data.state.OTDataStore;
import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.graph.util.state.OTDrawingEraser;

public interface OTEraserGraphable extends OTObjectInterface
{
	public byte [] getSrc();
	public void setSrc(byte[] src);
	
	public float getX();
	public void setX(float x);
	
	public float getY();
	public void setY(float y);
	
	public OTDataStore getDataStore();
	public void setDataStore(OTDataStore store);
	
	//When the icon comes from a eraser
	public OTDrawingEraser getEraser();
	public void setEraser(OTDrawingEraser stamp);

	public static int DEFAULT_bgColor = 0x00FFFFFF;
	public int getBgColor();
	public void setBgColor(int color);
	
	public static boolean DEFAULT_allowHide = true;
	public boolean getAllowHide();
	public void setAllowHide(boolean flag);
		
	public static boolean DEFAULT_locked = false;
	public boolean getLocked();
	public void setLocked(boolean locked);
	
	// points are the path that eraser is drawn.
	public float[] getPoints();
	public void setPoints(float[] points);
	
	// weight is the pixel that the eraser should occupy.
	public static int DEFAULT_weightX = 1;
	public int getWeightX();
	public void setWeightX(int weightX);
	
	public static int DEFAULT_weightY = 1;
	public int getWeightY();
	public void setWeightY(int weightY);
}