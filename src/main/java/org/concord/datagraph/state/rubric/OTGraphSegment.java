package org.concord.datagraph.state.rubric;

import org.concord.framework.otrunk.OTObjectInterface;
import org.concord.framework.otrunk.OTObjectList;

public interface OTGraphSegment extends OTObjectInterface {
    // list of criteria for this segment
    public OTObjectList getCriteria();

    public static boolean DEFAULT_optional = false;
    public boolean getOptional();
    public void setOptional(boolean optional);
}
