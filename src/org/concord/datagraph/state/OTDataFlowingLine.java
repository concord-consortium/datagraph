/**
 * 
 */
package org.concord.datagraph.state;

import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.graph.util.state.OTDrawingImageIcon;

public interface OTDataFlowingLine extends OTObjectInterface
{		
    public OTDrawingImageIcon getImage1();
    public void setImage1(OTDrawingImageIcon image);
    
    public OTDrawingImageIcon getImage2();
    public void setImage2(OTDrawingImageIcon image);
    
	public DataProducer getDataProducer();
}