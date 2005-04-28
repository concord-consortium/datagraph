

/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.concord.datagraph.engine;

/**
 * DataGraphableEx
 * This class is identical to the DataGraphable
 * except it support points as shape
 * if connectPoints property is <code>true</code>
 * DataGraphableEx behaves exactly as DataGraphable
 *
 * Date created: April 27, 2005
 *
 * @author Scott Cytacki<p>
 * @author Dmitry Markman<p>
 * @see org.concord.graph.engine.DefaultGraphable
 * @see org.concord.graph.engine.DefaultGraphable#setConnectPoints()
 */
 
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Area;

public class DataGraphableEx extends DataGraphable
{

    private static GeneralPath trianglePath = null;
    private static GeneralPath rectanglePath = null;
    private static GeneralPath ovalPath = null;
    private static GeneralPath starPath = null;
    private static GeneralPath crossPath = null;
    private static GeneralPath rhombPath = null;
		
	GeneralPath shapePath = null;
	GeneralPath currentPath = null;
	
	boolean fillShape = false;
	
	/**
     * Default constructor.
     */
     
    static{
        initShapes();
    } 
     
	public DataGraphableEx()
	{
        super();
        shapePath = new GeneralPath();
	}

    protected void resetPaths()
    {
        super.resetPaths();
	    shapePath.reset();
    }


/**
 * @return predefined triangular shape.
 */
    public static GeneralPath getTrianglePath(){
        return (GeneralPath)trianglePath.clone();
    }

/**
 * @return predefined square shape.
 */
    public static GeneralPath getSquarePath(){
        return (GeneralPath)rectanglePath.clone();
    }

/**
 * @return predefined circle shape.
 */
    public static GeneralPath getCirclePath(){
        return (GeneralPath)ovalPath.clone();
    }

/**
 * @return predefined star shape.
 */
    public static GeneralPath getStarPath(){
        return (GeneralPath)starPath.clone();
    }

/**
 * @return predefined cross shape.
 */
    public static GeneralPath getCrossPath(){
        return (GeneralPath)crossPath.clone();
    }

/**
 * @return predefined rhomb shape.
 */
    public static GeneralPath getRhombPath(){
        return (GeneralPath)rhombPath.clone();
    }


    private static void initShapes()
    {
		trianglePath = new GeneralPath();//dima
		trianglePath.moveTo(-2,2);//dima
		trianglePath.lineTo(0,-2);//dima
		trianglePath.lineTo(2,2);//dima
		trianglePath.lineTo(-2,2);//dima

		rectanglePath = new GeneralPath();//dima
	    rectanglePath.append(new java.awt.geom.Rectangle2D.Float(0,0,5,5),false);

		ovalPath = new GeneralPath();//dima
	    ovalPath.append(new java.awt.geom.Ellipse2D.Float(0,0,5,5),false);

        starPath = new GeneralPath();//dima
	    starPath.moveTo(-6,0);//dima
	    starPath.lineTo(-2,0);//dima
	    starPath.lineTo(0,-4);//dima
	    starPath.lineTo(2,0);//dima
	    starPath.lineTo(6,0);//dima
	    starPath.lineTo(2,4);//dima
	    starPath.lineTo(4,8);//dima
	    starPath.lineTo(0,6);//dima
	    starPath.lineTo(-4,8);//dima
	    starPath.lineTo(-2,4);//dima
	    starPath.lineTo(-6,0);//dima
	    
        crossPath = new GeneralPath();//dima
	    crossPath.moveTo(-3,0);//dima
	    crossPath.lineTo(3,0);//dima
	    crossPath.moveTo(0,-3);//dima
	    crossPath.lineTo(0,3);//dima
	    
	    rhombPath = new GeneralPath();
	    rhombPath.moveTo(-3,0);
	    rhombPath.lineTo(0,4);
	    rhombPath.lineTo(3,0);
	    rhombPath.lineTo(0,-4);
	    rhombPath.lineTo(-3,0);
	    
    }
/*
 * @see org.concord.graph.engine.DefaultGraphable#draw()
 */
    public void draw(Graphics2D g)
	{
		Object oldHint = null;
		if(/*!isConnectPoints() && */(currentPath != null)){//dima
		    oldHint = g.getRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING);
		    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
		}
        super.draw(g);
        additionalDrawing(g);
		if(oldHint != null) g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,oldHint);
	}
	

    protected void additionalDrawing(Graphics2D g)
    {
        if(shapePath != null){
    		Shape oldClip = null;
		    org.concord.graph.engine.GraphArea ga = getGraphArea();
    		if(ga != null){
    		    oldClip = g.getClip();
		        ga.clipGraphics(g);
    		}
    		if(!fillShape)   g.draw(shapePath);
    		else            g.fill(shapePath);
    		if(oldClip != null){
    		    g.setClip(oldClip);
    		}
        }
    }

/*
 * set custom shape
 * @param userPath 
 */
	
    public void setCustomShape(GeneralPath userPath){
        if(currentPath != null){
            currentPath.reset();
        }
        currentPath = null;
        if(userPath != null){
            currentPath = userPath;
        }
    }

/*
 * set custom shape
 * @param userShape 
 */
	
    public void setCustomShape(Shape userShape){
        if(currentPath != null){
            currentPath.reset();
        }
        if(userShape == null){
            currentPath = null;
        }else{
            currentPath.append(userShape,false);
        }
    }

/*
 * @see org.concord.graph.engine.DefaultGraphable#handleCurrentPoint()
 */
    protected void drawPoint(float ppx, float ppy)
    {
        if(isConnectPoints() || (currentPath == null)){
            super.drawPoint(ppx,ppy);
        }
    }
    
    protected void handleCurrentPoint(float ppx, float ppy)
    {
	    if(currentPath != null){
            java.awt.Rectangle bounds = currentPath.getBounds();
            double needDX = ppx - (bounds.x + bounds.width/2);
            double needDY = ppy - (bounds.y + bounds.height/2);
            currentPath.transform(java.awt.geom.AffineTransform.getTranslateInstance(needDX,needDY));
            shapePath.append(currentPath,false);
	    }
		
		
	}

/* set way how to draw shapes
 * @param fillShape if it is <code>true</code>
 * shape will be filled
 */
 	public void setFillShape(boolean fillShape)
	{
	    this.fillShape = fillShape;
	}

	public boolean getFillShape()
	{
	    return fillShape;
	}

}
