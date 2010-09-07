package org.concord.datagraph.log;


import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;


public interface OTEventLog extends OTObjectInterface {
	
	public String START = "start";
	public String STOP = "stop";
	public String RESET = "reset";

	public OTObjectList getItems();
	
}
