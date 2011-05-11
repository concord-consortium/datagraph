package org.concord.datagraph.analysis.rubric;

import org.concord.datagraph.analysis.GraphSegment;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Operation;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;

public class GraphRubricSegmentCriterion {
    private final Property property;
    private final Operation operation;
    private final double expectedValue;
    private final double tolerance;
    private final double points;
    private final boolean optional;
    
    public GraphRubricSegmentCriterion(Property property, Operation operation, double expectedValue, double tolerance, double points, boolean optional) {
        this.property = property;
        this.operation = operation;
        this.expectedValue = expectedValue;
        this.tolerance = tolerance;
        this.points = points;
        this.optional = optional;
    }

    public Property getProperty() {
        return property;
    }

    public Operation getOperation() {
        return operation;
    }

    public double getExpectedValue() {
        return expectedValue;
    }

    public double getPoints() {
        if (optional) {
            return 0;
        }
        return points;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean matches(GraphSegment graphSegment) {
        if (graphSegment == null) {
            return false;
        }
        double value = getPropertyValue(graphSegment);
        boolean matches = compareValue(value);
        return matches;
    }
    
    public double getPropertyValue(GraphSegment graphSegment) {
        switch (property) {
        case BEGINNING_X:
            return graphSegment.getX1();
        case BEGINNING_Y:
            return graphSegment.getY1();
        case ENDING_X:
            return graphSegment.getX2();
        case ENDING_Y:
            return graphSegment.getY2();
        case DELTA_X:
            return graphSegment.getX2() - graphSegment.getX1();
        case DELTA_Y:
            return graphSegment.getY2() - graphSegment.getY1();
        case SLOPE:
            return graphSegment.getB();
        default:
            return Double.NEGATIVE_INFINITY;
        }
    }
    
    private boolean compareValue(double value) {
        switch (operation) {
        case LESS_THAN:
            return ((value-tolerance) < expectedValue);
        case EQUAL_TO:
            return (Math.abs(value - expectedValue) < tolerance);
        case GREATER_THAN:
            return ((value+tolerance) > expectedValue);
        case LESS_THAN_OR_EQUAL_TO:
            return ((value-tolerance) <= expectedValue);
        case GREATER_THAN_OR_EQUAL_TO:
            return ((value+tolerance) >= expectedValue);
        default:
            return false;
        }
    }
}
