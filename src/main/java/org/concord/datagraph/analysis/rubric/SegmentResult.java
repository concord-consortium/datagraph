package org.concord.datagraph.analysis.rubric;

import java.util.ArrayList;

import org.concord.datagraph.analysis.GraphSegment;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;

public class SegmentResult {
    private GraphSegment received;
    private boolean isOptional;
    
    private ArrayList<GraphRubricSegmentCriterion> failures = new ArrayList<GraphRubricSegmentCriterion>();
    private ArrayList<GraphRubricSegmentCriterion> optionalFailures = new ArrayList<GraphRubricSegmentCriterion>();
    private ArrayList<GraphRubricSegmentCriterion> successes = new ArrayList<GraphRubricSegmentCriterion>();
    
    private ArrayList<Property> failedProperties = new ArrayList<Property>();

    public SegmentResult(GraphSegment received, boolean isOptional) {
        this.received = received;
        this.isOptional = isOptional;
    }
    
    public void addFailure(GraphRubricSegmentCriterion criterion) {
        if (criterion.isOptional()) {
            optionalFailures.add(criterion);
        } else {
            failures.add(criterion);
            failedProperties.add(criterion.getProperty());
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
        if (! isOptional) {
            for (GraphRubricSegmentCriterion crit : successes) {
                points += crit.getPoints();
            }
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

    public double getPossiblePoints() {
        // sum of all the failed and passed criterion
        double points = 0;
        for (GraphRubricSegmentCriterion crit : getFailures()) {
            points += crit.getPoints();
        }
        for (GraphRubricSegmentCriterion crit : getSuccesses()) {
            points += crit.getPoints();
        }
        return points;
    }
    
    public boolean isFailed(Property property) {
        return failedProperties.contains(property);
    }
}
