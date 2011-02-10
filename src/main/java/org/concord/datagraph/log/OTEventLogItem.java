package org.concord.datagraph.log;


import org.concord.framework.otrunk.OTObjectInterface;

/**
 * Event log item for individual events.
 * 
 * This is now deprecated! User org.concord.otrunk.logging.OTModelEvent instead!
 * @author aunger
 *
 */
@Deprecated
public interface OTEventLogItem extends OTObjectInterface {
    
    public String getName();
    public void setName(String name);
    
    public String getValue();
    public void setValue(String value);
    
    public long getTime();
    public void setTime(long ms);
    
}