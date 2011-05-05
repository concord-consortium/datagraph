package org.concord.datagraph.analysis.rubric;

import java.util.ArrayList;

import org.concord.datagraph.analysis.GraphSegment;

public class SegmentResult {
    private GraphSegment received;
    private boolean isOptional;
    
    private ArrayList<GraphRubricSegmentCriterion> failures = new ArrayList<GraphRubricSegmentCriterion>();
    private ArrayList<GraphRubricSegmentCriterion> optionalFailures = new ArrayList<GraphRubricSegmentCriterion>();
    private ArrayList<GraphRubricSegmentCriterion> successes = new ArrayList<GraphRubricSegmentCriterion>();

    public SegmentResult(GraphSegment received, boolean isOptional) {
        this.received = received;
        this.isOptional = isOptional;
    }
    
    public void addFailure(GraphRubricSegmentCriterion criterion) {
        if (criterion.isOptional()) {
            optionalFailures.add(criterion);
        } else {
            failures.add(criterion);
        }
    }
    
    public void addSuccess(GraphRubricSegmentCriterion criterion) {
        successes.add(criterion);
    }

    public GraphSegment getReceived() {
        return received;
    }
    
    public double getFailedPoints() {
        double points = 0.0;
        for (GraphRubricSegmentCriterion crit : failures) {
            points += crit.getPoints();
        }
        return points;
    }
    
    public double getPoints() {
        double points = 0.0;
        for (GraphRubricSegmentCriterion crit : successes) {
            points += crit.getPoints();
        }
        return points;
    }

    public ArrayList<GraphRubricSegmentCriterion> getFailures() {
        return failures;
    }
    
    public ArrayList<GraphRubricSegmentCriterion> getOptionalFailures() {
        return optionalFailures;
    }

    public ArrayList<GraphRubricSegmentCriterion> getSuccesses() {
        return successes;
    }
    
    public boolean isOptional() {
        return this.isOptional;
    }
}
