
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
