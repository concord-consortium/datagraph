package org.concord.datagraph.analysis;

import org.concord.datagraph.analysis.rubric.GraphRubricSegment;
import org.concord.datagraph.analysis.rubric.GraphRubricSegmentCriterion;
import org.concord.datagraph.analysis.rubric.SegmentResult;


public class GraphSegment {
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;
    private final double a;
    private final double b;
    private final double c;
    
    /**
     * Represents a graph segment, with x1 as the left edge, x2 as the right edge, and a,b, and c representing the values for the curve: ax^2 + bx + c
     * 
     * @param x1
     * @param x2
     * @param a
     * @param b
     * @param c
     */
    public GraphSegment(double x1, double x2, double a, double b, double c) {
        this.x1 = x1;
        this.x2 = x2;
        this.a = a;
        this.b = b;
        this.c = c;
        
        this.y1 = calcY(x1);
        this.y2 = calcY(x2);
    }

    private double calcY(double x) {
        // y = ax^2 + bx + c
        return (a * Math.pow(x, 2)) + (b*x) + c;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }
    
    public double getY1() {
        return y1;
    }

    public double getY2() {
        return y2;
    }

    public SegmentResult evaluateBasedOn(GraphRubricSegment expectedSegment) {
        SegmentResult results = new SegmentResult(this, expectedSegment.isOptional());
        
        for (GraphRubricSegmentCriterion criterion : expectedSegment) {
             if (criterion.matches(this)) {
                 results.addSuccess(criterion);
             } else {
                 results.addFailure(criterion);
             }
        }
        
        return results;
    }

}
