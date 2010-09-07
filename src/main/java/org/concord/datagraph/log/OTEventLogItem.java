package org.concord.datagraph.log;


import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;


public interface OTEventLogItem extends OTObjectInterface {
	
	public String getName();
	public void setName(String name);
	
	public String getValue();
	public void setValue(String value);
	
	public long getTime();
	public void setTime(long ms);
	
}
