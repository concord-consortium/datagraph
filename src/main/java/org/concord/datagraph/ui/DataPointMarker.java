package org.concord.datagraph.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataStore;
import org.concord.graph.engine.CoordinateSystem;
import org.concord.graph.engine.DefaultGraphable;
import org.concord.graph.engine.Graphable;
import org.concord.graph.engine.GraphableList;

public class DataPointMarker extends DefaultGraphable implements DataAnnotation {
    private DataGraphable graphable;
    private double xValue = 0.0;
    private Color color;
    
    private static Stroke lineStroke = new BasicStroke(2.5f,       // Width
                                                       BasicStroke.CAP_SQUARE,     // End cap
                                                       BasicStroke.JOIN_MITER,     // Join style
                                                       1.0f);
    
    public Graphable getCopy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void draw(Graphics2D g) {
        CoordinateSystem cs = graphArea.getCoordinateSystem();
        Point2D closestPoint = findClosestPoint();

        Color originalColor = g.getColor();
        Stroke originalStroke = g.getStroke();
        
        if (closestPoint != null) {
            Point2D intersection = cs.transformToDisplay(closestPoint);
            
            // make an X with lines 10px long
            // a^2 + b^2 = c^2 then tells us that each side of the bounding box is ~7px
            // so from the center point, go up/down and left/right 3.5
            Point2D topLeft = new Point2D.Double(intersection.getX() + 3.5, intersection.getY() - 3.5);
            Point2D bottomRight = new Point2D.Double(intersection.getX() - 3.5, intersection.getY() + 3.5);
            Point2D topRight = new Point2D.Double(intersection.getX() + 3.5, intersection.getY() + 3.5);
            Point2D bottomLeft = new Point2D.Double(intersection.getX() - 3.5, intersection.getY() - 3.5);
            
            g.setColor(color);
            g.setStroke(lineStroke);
            
            g.draw(new Line2D.Double(topLeft, bottomRight));
            g.draw(new Line2D.Double(topRight, bottomLeft));
        }
        g.setColor(originalColor);
        g.setStroke(originalStroke);
    }

    private Point2D findClosestPoint() {
        DataStore dataStore = graphable.getDataStore();
        float minDelta = Float.POSITIVE_INFINITY;
        int sample = 0;
        for (int i = 0; i < dataStore.getTotalNumSamples(); i++) {
            float x = (Float) dataStore.getValueAt(i, 0);
            float delta = Math.abs(x - (float)xValue);
            if (delta < minDelta) {
                minDelta = delta;
                sample = i;
            } else {
                return new Point2D.Float((Float) dataStore.getValueAt(sample, 0), (Float) dataStore.getValueAt(sample, 1));
            }
        }
        return null;
    }

    public DataGraphable getDataGraphable() {
        return graphable;
    }

    public void setDataGraphable(DataGraphable dataGraphable) {
        graphable = dataGraphable;
    }

    public void setGraphableList(GraphableList gList) {
        // Not supported...
    }
    
    public void setXValue(double x) {
        this.xValue = x;
    }
    
    public double getXValue() {
        return this.xValue;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

}
