package org.concord.datagraph.state;


public interface OTDataPointRuler extends OTDataAnnotation {
	
	public static boolean DEFAULT_labelVisible = false;
	public void setLabelVisible(boolean visible);
	public boolean getLabelVisible();
	
	public static boolean DEFAULT_verticalVisible = true;
	public void setVerticalVisible(boolean visible);
	public boolean getVerticalVisible();
	
	public static boolean DEFAULT_horizontalVisible = true;
	public void setHorizontalVisible(boolean visible);
	public boolean getHorizontalVisible();
	
	public static boolean DEFAULT_intersectionVisible = true;
	public void setIntersectionVisible(boolean b);
	public boolean getIntersectionVisible();
	
	public static boolean DEFAULT_verticalValueVisible = true;
	public void setVerticalValueVisible(boolean visible);
	public boolean getVerticalValueVisible();
	
	public static boolean DEFAULT_horizontalValueVisible = true;
	public void setHorizontalValueVisible(boolean visible);
	public boolean getHorizontalValueVisible();
}
