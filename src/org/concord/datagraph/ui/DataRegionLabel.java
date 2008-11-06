package org.concord.datagraph.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.color.ColorSpace;
import java.awt.geom.Point2D;
import java.text.NumberFormat;

import org.concord.data.stream.ProducerDataStore;
import org.concord.datagraph.engine.DataGraphable;
import org.concord.framework.data.stream.DataStore;
import org.concord.framework.data.stream.DataStoreEvent;
import org.concord.framework.data.stream.DefaultDataStore;

public class DataRegionLabel extends DataPointLabel
{
	private float xUpperBounds;
	private float xLowerBounds;
	private Color DEFAULT_COLOR = new Color(100,255,100,150);
	private DefaultDataStore highliterDataStore;
	private boolean dsNeedsUpdate = true;
	
	protected int xPrecision = 1;
	protected int yPrecision = 1;
	
	private float x1;
	private float x2;
	private float y1;
	private float y2;
	private float xMiddle;
	private float yMiddle;
	private boolean showLabel = true;
	private boolean showHighlight = true;
	
	public void draw(Graphics2D g)
	{
		if (dsNeedsUpdate){
			processDataStore(dataGraphable);
			updateDataPointLabels();
			setDataPoint(new Point2D.Float(xMiddle, yMiddle));
			setAdjustDisplayDataPointY(-5);
			dsNeedsUpdate = false;
		}
		
		setDrawDataPoint(false);
		
		if (showLabel){
			super.draw(g);
		}
		
		if(showHighlight && dataGraphable != null && dataGraphable.isVisible()) {
			DataGraphable highlighter = new DataGraphable();
			highlighter.setGraphArea(dataGraphable.getGraphArea());
			highlighter.setDataStore(highliterDataStore);
			highlighter.setChannelX(dataGraphable.getChannelX());
			highlighter.setChannelY(dataGraphable.getChannelY());
			highlighter.setLineWidth(12);
			Color color = new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue(), 200);
			highlighter.setColor(color);
			highlighter.draw(g);
		}
	}
	
	private void processDataStore(DataStore dataStore){
		highliterDataStore = new DefaultDataStore();
		highliterDataStore.clearValues();
		
		x1 = Float.POSITIVE_INFINITY;
		x2 = Float.NEGATIVE_INFINITY;
		float middleDifference = Float.POSITIVE_INFINITY;
		float middle = xLowerBounds + ((xUpperBounds - xLowerBounds) / 2);
		Float xValue = null;
		for (int i = 0; i < dataStore.getTotalNumSamples(); i++) {
			xValue = (Float) dataStore.getValueAt(i, 0);
			if (xValue.floatValue() <= xUpperBounds && xValue.floatValue() >= xLowerBounds){
	        	Float yValue = yValue = (Float) dataStore.getValueAt(i, 1);

	        	highliterDataStore.setValueAt(i, 0, xValue);
	        	highliterDataStore.setValueAt(i, 1, yValue);
	        	
	        	if (xValue.floatValue() < x1){
	        		x1 = xValue.floatValue();
	        		y1 = yValue.floatValue();
	        	} else if (xValue.floatValue() > x2){
	        		x2 = xValue.floatValue();
	        		y2 = yValue.floatValue();
	        	}
	        	
	        	if (Math.abs(xValue.floatValue() - middle) < Math.abs(middleDifference)){
	        		xMiddle = xValue.floatValue();
	        		yMiddle = yValue.floatValue();
	        		middleDifference = xMiddle - middle;
	        	}
	        }
        }
	}
	
	protected void updateDataPointLabels()
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(xPrecision);
        pointLabel = "(" + nf.format(x1) + ((xUnits== null)?"":xUnits) + ", ";
        nf.setMaximumFractionDigits(yPrecision);
        pointLabel += nf.format(y1) + ((yUnits== null)?"":yUnits) + ") - ";
        
        nf.setMaximumFractionDigits(xPrecision);
        pointLabel += "(" + nf.format(x2) + ((xUnits== null)?"":xUnits) + ", ";
        nf.setMaximumFractionDigits(yPrecision);
        pointLabel += nf.format(y2) + ((yUnits== null)?"":yUnits) + ")";
    }
	
	public float getXUpperBounds(){
		return xUpperBounds;
	}
	
	public float getXLowerBounds(){
		return xLowerBounds;
	}
	
	public void setXUpperBounds(float startX){
		this.xUpperBounds = startX;
	}
	
	public void setXLowerBounds(float endX){
		this.xLowerBounds = endX;
	}
	
	public void setRegion(float x1, float x2){
		setXUpperBounds(x1 > x2 ? x1 : x2);
		setXLowerBounds(x1 > x2 ? x2 : x1);
		dataGraphable.getDataStore().addDataStoreListener(this);
	}
	
	public void setShowLabel(boolean showLabel){
		this.showLabel = showLabel;
	}
	
	public boolean getShowLabel(){
		return showLabel;
	}
	
	public void setShowHighlight(boolean showHighlight){
		this.showHighlight = showHighlight;
	}
	
	public boolean getShowHighlight(){
		return showHighlight;
	}
	
	/**
	 * @see org.concord.framework.data.stream.DataStoreListener#dataChanged(org.concord.framework.data.stream.DataStoreEvent)
	 */
	public void dataChanged(DataStoreEvent evt)
	{
		super.dataChanged(evt);
		dsNeedsUpdate = true;
	}
}
