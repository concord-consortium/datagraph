package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.graph.util.state.OTHideableAnnotation;

public interface OTDataPointMarker extends OTObjectInterface,OTHideableAnnotation {

    public static double DEFAULT_x = 0.0;
    public double getX();
    public void setX(double x);
    
    public OTDataGraphable getDataGraphable();
    public void setDataGraphable(OTDataGraphable var);
    
    public static int DEFAULT_color = 0xff0000;
    public int getColor();
    public void setColor(int color);
}
