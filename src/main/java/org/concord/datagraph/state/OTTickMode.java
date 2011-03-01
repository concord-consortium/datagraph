package org.concord.datagraph.state;

import org.concord.datagraph.ui.DataGraph.TickMode;
import org.concord.framework.otrunk.OTObjectInterface;

public interface OTTickMode extends OTObjectInterface {
    public void setValue(TickMode mode);
    public TickMode getValue();
    public static TickMode DEFAULT_tickMode = TickMode.AUTO;
}
