package org.concord.datagraph.state.rubric;

import org.concord.framework.otrunk.OTObjectInterface;

public interface OTGraphSegmentCriterion extends OTObjectInterface {

    public static enum Property {
        BEGINNING_X, BEGINNING_Y, ENDING_X, ENDING_Y, SLOPE, DELTA_X, DELTA_Y
    }
    
    // positive and negative can be represented by GREATER_THAN 0 and LESS_THAN 0, respectively
    public static enum Operation {
        LESS_THAN, GREATER_THAN, EQUAL_TO, LESS_THAN_OR_EQUAL_TO, GREATER_THAN_OR_EQUAL_TO
    }
    
    public Property getProperty();
    public void setProperty(Property property);
    
    public static Operation DEFAULT_Operation = Operation.EQUAL_TO;
    public Operation getOperation();
    public void setOperation(Operation operation);
    
    public static double DEFAULT_expectedValue = 0.0;
    public double getExpectedValue();
    public void setExpectedValue(double value);
    
    public static double DEFAULT_points = 1.0;
    public double getPoints();
    public void setPoints(double points);
    
    public static boolean DEFAULT_optional = false;
    public boolean getOptional();
    public void setOptional(boolean optional);
    
    public static double DEFAULT_tolerance = 0.1;
    public double getTolerance();
    public void setTolerance(double tolerance);

}
