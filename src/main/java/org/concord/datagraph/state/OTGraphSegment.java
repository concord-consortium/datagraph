package org.concord.datagraph.state;

import org.concord.framework.otrunk.OTObjectInterface;

/**
 * Describes a segment of a graph, in the form ax^2 + bx + c,
 * and defining x1 as the low edge of the segment,
 * and defining x2 as the high edge of the segment.
 * @author aunger
 *
 */
public interface OTGraphSegment extends OTObjectInterface {
    public static double DEFAULT_x1 = 0.0;
    public double getX1();
    public void setX1(double var);
    
    public static double DEFAULT_x2 = 0.0;
    public double getX2();
    public void setX2(double var);
    
    public static double DEFAULT_a = 0.0;
    public double getA();
    public void setA(double var);
    
    public static double DEFAULT_b = 0.0;
    public double getB();
    public void setB(double var);
    
    public static double DEFAULT_c = 0.0;
    public double getC();
    public void setC(double var);
}
