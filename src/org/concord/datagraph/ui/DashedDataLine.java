package org.concord.datagraph.ui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.text.NumberFormat;

import org.concord.graph.engine.DefaultCoordinateSystem2D;
import org.concord.graph.engine.GraphArea;

public class DashedDataLine {
	public static final int VERTICAL_LINE = 1;
	public static final int HORIZONTAL_LINE = 2;
	private Point2D startPoint = null;
	private Point2D endPoint = null;
	
	private int type = VERTICAL_LINE;
	private Stroke stroke = null;
	private int precision = -1;
	private static GraphArea graphArea;
	
	public DashedDataLine() {
		precision = 2;
		stroke = new BasicStroke(1.0f,		// Width
                BasicStroke.CAP_SQUARE,		// End cap
                BasicStroke.JOIN_MITER,		// Join style
                1.0f,						// Miter limit
                new float[] {10.0f,5.0f},	// Dash pattern
                0.0f);
	}
	
	public DashedDataLine(int type) {
		this();
		setType(type);
	}
	
	public DashedDataLine(Point2D xvalue, Point2D yvalue, int type) {
		this();
		setType(type);
		setPoints(xvalue, yvalue);
	}
	
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
	
	public void setDataPrecision(int precision) {
		this.precision = precision;
	}
	
	public void setType(int type) {
		if(type != VERTICAL_LINE && type != HORIZONTAL_LINE) throw new IllegalArgumentException();
		this.type = type;
	}
	
	public void setPoints(Point2D startPoint, Point2D endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	public void setStartPoint(Point2D startPoint) {
		this.startPoint = startPoint;
	}
	
	public void setEndPoint(Point2D endPoint) {
		this.endPoint = endPoint;
	}
	
	public Point2D getStartPoint() {
		return startPoint;
	}
	
	public Point2D getEndPoint() {
		return endPoint;
	}
	
	public int getPrecision() {
		return precision;
	}
	
	public static void setGraphArea(GraphArea graphArea) {
		DashedDataLine.graphArea = graphArea;
	}
	
	public void draw(Graphics2D g) {
		Stroke oldStroke = g.getStroke();
		
		int left = graphArea.getInsets().left;
		int top = graphArea.getInsets().top;
		
		DefaultCoordinateSystem2D cs = (DefaultCoordinateSystem2D)graphArea.getCoordinateSystem();
		Point2D pStart = 
			cs.transformToDisplay(new Point2D.Double(startPoint.getX(), startPoint.getY()));
		Point2D pEnd = 
			cs.transformToDisplay(new Point2D.Double(endPoint.getX(), endPoint.getY()));
		
		if(type == VERTICAL_LINE) {
			pStart.setLocation(pStart.getX(), graphArea.getSize().height + top );
		} else {
			pStart.setLocation(left, pEnd.getY());
		}
		
		g.setStroke(stroke);
		g.drawLine((int)pStart.getX(), (int)pStart.getY(), (int)pEnd.getX(), (int)pEnd.getY());
		g.setStroke(oldStroke);
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(precision);
		
		if(type == VERTICAL_LINE) {
			double value = startPoint.getX();
			String svalue = nf.format(value);
			g.drawString(svalue, (int)pStart.getX(), (int)pStart.getY() - 10);
			g.fillOval((int)pStart.getX()-3, (int)pStart.getY()-3, 6, 6);
		} else {
			double value = startPoint.getY();
			String svalue = nf.format(value);
			g.drawString(svalue, (int)pStart.getX() + 10, (int)pStart.getY());			
			g.fillOval((int)pStart.getX(), (int)pStart.getY()-3, 6, 6);
		}
	}
}
