package org.concord.datagraph.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
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
    public static enum MarkerShape { X, TRIANGLE, CIRCLE, TEXT_ABOVE, TEXT_BELOW }
    private DataGraphable graphable;
    private double xValue = 0.0;
    private Color color;
    private MarkerShape shape = MarkerShape.X;
    private String text = "text";
    
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
        if (graphable.isVisible() && isVisible()) {
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
                case CIRCLE:
                    g.fillOval(1, 1, 8, 8);
                    break;
                case TEXT_ABOVE:
                    drawString(g, text, 1);
                    break;
                case TEXT_BELOW:
                    drawString(g, text, -1);
                    break;
                default:
                    g.draw(xShape);
                    break;
                }
            }
        }
    }
    
    private void drawString(Graphics2D g, String str, int verticalShift) {
        // 5,5 in the graphics obj is the point on the graph where this marker corresponds
        FontMetrics fontMetrics = g.getFontMetrics();
        int width = fontMetrics.stringWidth(str);
        float gap = fontMetrics.getLeading()/2.0f;
        if (verticalShift > 0) {
            // shift up
            g.drawString(text, 5 - (width/2.0f), 5-(fontMetrics.getDescent()+gap));
        } else {
            g.drawString(text, 5 - (width/2.0f), Math.round(5 + gap + fontMetrics.getAscent()));
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
            } else if (i == (dataStore.getTotalNumSamples() - 1)) {
                // this is the last sample, and we're still on the left of the expected x.
                // just return this point
                return new Point2D.Float(x, y);
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

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
