package org.concord.datagraph.analysis.rubric;

import java.util.ArrayList;

import org.concord.datagraph.state.rubric.OTGraphSegment;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion;
import org.concord.framework.otrunk.OTObject;

public class GraphRubricSegment extends ArrayList<GraphRubricSegmentCriterion> {
    private static final long serialVersionUID = 1L;
    private boolean optional = false;

    public GraphRubricSegment(boolean isOptional) {
        super();
        this.optional = isOptional;
    }
    
    public GraphRubricSegment(OTGraphSegment segment) {
        super();
        
        this.optional = segment.getOptional();
        
        for (OTObject obj : segment.getCriteria()) {
            if (obj instanceof OTGraphSegmentCriterion) {
                OTGraphSegmentCriterion crit = (OTGraphSegmentCriterion) obj;
                GraphRubricSegmentCriterion grsc = new GraphRubricSegmentCriterion(crit.getProperty(), crit.getOperation(), crit.getExpectedValue(), crit.getTolerance(), crit.getPoints(), crit.getOptional());
                this.add(grsc);
            } else {
                throw new RuntimeException("Invalid object type in criteria list!");
            }
        }
    }

    public double getPossiblePoints() {
        double points = 0;
        for (GraphRubricSegmentCriterion crit : this) {
            if (! crit.isOptional()) {
                points += crit.getPoints();
            }
        }
        return points;
    }
    
    public boolean isOptional() {
        return this.optional;
    }
}
