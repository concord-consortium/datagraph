package org.concord.datagraph.log;


import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;


public interface OTEventLog extends OTObjectInterface {
	
	public String START = "start";
	public String STOP = "stop";
	public String RESET = "reset";
	
	public String PLAYBACK_START = "playback-start";
    public String PLAYBACK_STOP = "playback-stop";
    public String PLAYBACK_RESET = "playback-reset";

	public OTObjectList getItems();
	
}
