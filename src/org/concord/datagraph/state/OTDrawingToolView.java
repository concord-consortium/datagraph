package org.concord.datagraph.state;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.concord.data.stream.PointsDataStore;
import org.concord.datagraph.engine.ControllableDataGraphable;
import org.concord.datagraph.engine.ControllableDataGraphableDrawing;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.framework.otrunk.view.OTObjectView;
import org.concord.framework.otrunk.view.OTViewContainer;
import org.concord.graph.util.engine.DrawingObject;
import org.concord.graph.util.ui.DrawingGraph;
import org.concord.graph.util.ui.ResourceLoader;
/*
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author scott
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OTDrawingToolView extends DrawingGraph
    implements OTObjectView
{
    OTDrawingTool drawingTool;
    
    public OTDrawingToolView(OTDrawingTool tool, OTViewContainer container)
    {
        drawingTool = tool;
    }
    
    /* (non-Javadoc)
     * @see org.concord.framework.otrunk.view.OTObjectView#getComponent(boolean)
     */
    public JComponent getComponent(boolean editable)
    {
        setGridVisible(drawingTool.getGridVisible());
        
        byte [] bgBytes = drawingTool.getBackgroundImage();
        if(bgBytes != null) {
            ImageIcon bgImage = new ImageIcon(bgBytes);
            setBackgroundImage(bgImage);
        }
        
        OTObjectList stamps = drawingTool.getStamps();
        
        for(int i=0; i<stamps.size(); i++) {
           OTDrawingStamp stamp = (OTDrawingStamp)stamps.get(i);
           byte [] stampBytes = stamp.getSrc();
           ImageIcon stampIcon = new ImageIcon(stampBytes, stamp.getDescription());
           addStampIcon(stampIcon);
        }
        
        return this;
    }

	/**
	 * @see org.concord.graph.util.engine.DrawingObjectFactory#createNewDrawingObject(int)
	 */
	public DrawingObject createNewDrawingObject(int type)
	{
		PointsDataStore points = new PointsDataStore();
		ControllableDataGraphable dg = new ControllableDataGraphableDrawing();
		dg.setDrawAlwaysConnected(false);
		dg.setDataStore(points, 0, 1);
		dg.setLineType(ControllableDataGraphable.LINETYPE_FREE);
		objList.add(dg);
		return dg;
	}
}
