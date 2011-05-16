package org.concord.datagraph.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataStore;
import org.concord.graph.engine.CoordinateSystem;
import org.concord.graph.engine.DefaultGraphable;
import org.concord.graph.engine.Graphable;
import org.concord.graph.engine.GraphableList;

public class DataPointMarker extends DefaultGraphable implements DataAnnotation {
    public static enum MarkerShape { X, TRIANGLE }
    private DataGraphable graphable;
    private double xValue = 0.0;
    private Color color;
    private MarkerShape shape = MarkerShape.X;
    
    private static Stroke lineStroke = new BasicStroke(2.5f,       // Width
                                                       BasicStroke.CAP_SQUARE,     // End cap
                                                       BasicStroke.JOIN_MITER,     // Join style
                                                       1.0f);
    
    private static GeneralPath xShape = new GeneralPath();
    private static GeneralPath triShape = new GeneralPath();
    static {
        xShape.append(new Line2D.Double(0,0, 10,10), false);
        xShape.append(new Line2D.Double(0,10, 10,0), false);
        
        triShape.append(new Line2D.Double(5,0, 10,10), false);
        triShape.append(new Line2D.Double(10,10, 0,10), false);
        triShape.append(new Line2D.Double(0,10, 5,0), false);
    }
    
    public Graphable getCopy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void draw(Graphics2D gOrig) {
        CoordinateSystem cs = graphArea.getCoordinateSystem();
        Point2D point = interpolatePoint();
        
        Graphics2D g = (Graphics2D) gOrig.create();
        
        if (point != null) {
            Point2D intersection = cs.transformToDisplay(point);
            
            g.translate(intersection.getX()-5, intersection.getY()-5);
            
            g.setColor(color);
            g.setStroke(lineStroke);
            
            switch (shape) {
            case TRIANGLE:
                g.draw(triShape);
                break;
            
            default:
                g.draw(xShape);
                break;
            }
        }
    }

    // Finds the closest points and either side of the xValue
    // then linearly interpolates and calculates the yValue for the xValue
    private Point2D interpolatePoint() {
        DataStore dataStore = graphable.getDataStore();
        for (int i = 0; i < dataStore.getTotalNumSamples(); i++) {
            Float x = (Float) dataStore.getValueAt(i, 0);
            Float y = (Float) dataStore.getValueAt(i, 1);
            if (x == xValue) {
                // we have an exact match! return it
                return new Point2D.Float(x, y);
            } else if (x >= xValue) {
                // this will be the value to the right of our xValue
                if(i == 0) {
                    // we don't have a left point, so return this point
                    return new Point2D.Float(x, y);
                }
                
                Float leftX = (Float) dataStore.getValueAt(i-1, 0);
                Float leftY = (Float) dataStore.getValueAt(i-1, 1);
                
                // rise over run
                float m = (y - leftY)/(x - leftX);
                
                // point-slope formula: y = m(x - x1) + y1
                double yValue = m * (xValue - x) + y;
                return new Point2D.Double(xValue, yValue);
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

    public void setShape(MarkerShape shape) {
        this.shape = shape;
    }

    public MarkerShape getShape() {
        return shape;
    }

}
