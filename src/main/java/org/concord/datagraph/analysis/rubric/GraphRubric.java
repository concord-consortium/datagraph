package org.concord.datagraph.analysis.rubric;

import java.util.ArrayList;

import org.concord.datagraph.state.rubric.OTGraphSegment;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTObjectList;

public class GraphRubric extends ArrayList<GraphRubricSegment> {
    private static final long serialVersionUID = 1L;

    public GraphRubric() {
        super();
    }
    
    public GraphRubric(OTObjectList rubric) {
        super();
        
        for (OTObject obj : rubric) {
            if (obj instanceof OTGraphSegment) {
                GraphRubricSegment grs = new GraphRubricSegment((OTGraphSegment) obj);
                this.add(grs);
            } else {
                throw new RuntimeException("Invalid object type in rubric list!");
            }
        }
    }

    public double getPossiblePoints() {
        double points = 0;
        for (GraphRubricSegment crit : this) {
            points += crit.getPossiblePoints();
        }
        return points;
    }
}
