package org.concord.datagraph.state;

import org.concord.datagraph.ui.DataPointMarker.MarkerShape;
import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.graph.util.state.OTVisibleGraphable;

public interface OTDataPointMarker extends OTObjectInterface,OTVisibleGraphable {

    public static double DEFAULT_x = 0.0;
    public double getX();
    public void setX(double x);
    
    public OTDataGraphable getDataGraphable();
    public void setDataGraphable(OTDataGraphable var);
    
    public static int DEFAULT_color = 0xff0000;
    public int getColor();
    public void setColor(int color);
    
    public static MarkerShape DEFAULT_shape = MarkerShape.X;
    public MarkerShape getShape();
    public void setShape(MarkerShape shape);
    
    public static String DEFAULT_text = "text";
    public String getText();
    public void setText(String text);
}
