package org.concord.datagraph.state;
import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;
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
public interface OTDrawingTool
    extends OTObjectInterface
{
    public byte [] getBackgroundImage();
    
    public OTObjectList getStamps();
    
    public final static boolean DEFAULT_gridVisible = false;
    public boolean getGridVisible();
}
