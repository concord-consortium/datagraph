package org.concord.datagraph.analysis.rubric;

import static org.fest.assertions.Assertions.assertThat;

import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Operation;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;
import org.junit.Test;

public class GraphRubricTest {

    @Test
    public void possiblePointsNoSegments() {
        GraphRubric rubric = new GraphRubric();
        assertThat(rubric.getPossiblePoints()).as("Possible points").isEqualTo(0);
    }
    
    @Test
    public void possiblePoints() {
        GraphRubric rubric = new GraphRubric();
        for (int i = 0; i < 5; i++) {
            GraphRubricSegment seg = new GraphRubricSegment(false);
            for (int j = 0; j < 3; j++) {
                GraphRubricSegmentCriterion crit = new GraphRubricSegmentCriterion(Property.BEGINNING_X, Operation.EQUAL_TO, 0.0, 0.0, 1.0, false);
                seg.add(crit);
            }
            rubric.add(seg);
        }
        assertThat(rubric.getPossiblePoints()).as("Possible points").isEqualTo(15);
    }
    
    @Test
    public void possiblePointsWithOptionalCriterion() {
        GraphRubric rubric = new GraphRubric();
        for (int i = 0; i < 5; i++) {
            GraphRubricSegment seg = new GraphRubricSegment(false);
            for (int j = 0; j < 3; j++) {
                boolean optional = (((i*3)+j) % 5 == 0 ? true : false); // every fifth one is optional
                GraphRubricSegmentCriterion crit = new GraphRubricSegmentCriterion(Property.BEGINNING_X, Operation.EQUAL_TO, 0.0, 0.0, 1.0, optional);
                seg.add(crit);
            }
            rubric.add(seg);
        }
        assertThat(rubric.getPossiblePoints()).as("Possible points").isEqualTo(12);
    }
    
    @Test
    public void possiblePointsWithOptionalSegment() {
        GraphRubric rubric = new GraphRubric();
        for (int i = 0; i < 5; i++) {
            GraphRubricSegment seg = new GraphRubricSegment(false);
            for (int j = 0; j < 3; j++) {
                boolean optional = ((i == 2 || i == 4) ? true : false); // every fifth one is optional
                GraphRubricSegmentCriterion crit = new GraphRubricSegmentCriterion(Property.BEGINNING_X, Operation.EQUAL_TO, 0.0, 0.0, 1.0, optional);
                seg.add(crit);
            }
            rubric.add(seg);
        }
        assertThat(rubric.getPossiblePoints()).as("Possible points").isEqualTo(9);
    }
    
    @Test
    public void possiblePointsWithOptionalSegmentAndCriterion() {
        GraphRubric rubric = new GraphRubric();
        for (int i = 0; i < 5; i++) {
            GraphRubricSegment seg = new GraphRubricSegment(false);
            for (int j = 0; j < 3; j++) {
                boolean optional = ((i == 2 || j == 1) ? true : false); // every fifth one is optional
                GraphRubricSegmentCriterion crit = new GraphRubricSegmentCriterion(Property.BEGINNING_X, Operation.EQUAL_TO, 0.0, 0.0, 1.0, optional);
                seg.add(crit);
            }
            rubric.add(seg);
        }
        assertThat(rubric.getPossiblePoints()).as("Possible points").isEqualTo(8);
    }
}
