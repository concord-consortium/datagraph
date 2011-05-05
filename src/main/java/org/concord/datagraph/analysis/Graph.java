package org.concord.datagraph.analysis;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.concord.datagraph.analysis.rubric.GraphRubric;
import org.concord.datagraph.analysis.rubric.ResultSet;

public class Graph extends ArrayList<GraphSegment> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(Graph.class.getName());

    public ResultSet evaluateBasedOn(GraphRubric expected) {
        return new ResultSet(expected, this);
    }
    
    public double getRange() {
        return getMaxX() - getMinX();
    }
    
    public double getDomain() {
        return getMaxY() - getMinY();
    }
    
    public double getMinSlope() {
        double minSlope = Double.POSITIVE_INFINITY;
        for (GraphSegment s : this) {
            if (s.getB() < minSlope) {
                minSlope = s.getB();
            }
        }
        return minSlope;
    }
    
    public double getMaxSlope() {
        double maxSlope = Double.NEGATIVE_INFINITY;
        for (GraphSegment s : this) {
            if (s.getB() > maxSlope) {
                maxSlope = s.getB();
            }
        }
        return maxSlope;
    }
    
    public double getMinX() {
        double minX = Double.POSITIVE_INFINITY;
        for (GraphSegment s : this) {
            if (s.getX1() < minX) {
                minX = s.getX1();
            }
        }
        return minX;
    }
    
    public double getMaxX() {
        double maxX = Double.NEGATIVE_INFINITY;
        for (GraphSegment s : this) {
            if (s.getX2() > maxX) {
                maxX = s.getX1();
            }
        }
        return maxX;
    }
    
    public double getMinY() {
        double minY = Double.POSITIVE_INFINITY;
        for (GraphSegment s : this) {
            if (s.getY1() < minY) {
                minY = s.getY1();
            }
            
            if (s.getY2() < minY) {
                minY = s.getY2();
            }
        }
        return minY;
    }
    
    public double getMaxY() {
        double maxY = Double.NEGATIVE_INFINITY;
        for (GraphSegment s : this) {
            if (s.getY1() > maxY) {
                maxY = s.getY1();
            }
            
            if (s.getY2() > maxY) {
                maxY = s.getY2();
            }
        }
        return maxY;
    }

}
