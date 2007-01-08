/**
 * 
 */
package org.concord.datagraph.state;

import org.concord.graph.util.state.OTPointTextLabel;

public interface OTDataAnnotation extends OTPointTextLabel
{	
	public OTDataGraphable getDataGraphable();
	public void setDataGraphable(OTDataGraphable b);
}