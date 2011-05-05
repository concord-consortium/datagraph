package org.concord.datagraph.analysis;

import static org.fest.assertions.Assertions.assertThat;

import org.concord.datagraph.analysis.GraphSegment;
import org.concord.datagraph.analysis.rubric.GraphRubricSegment;
import org.concord.datagraph.analysis.rubric.GraphRubricSegmentCriterion;
import org.concord.datagraph.analysis.rubric.SegmentResult;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Operation;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphSegmentTest {
    private static GraphRubricSegment expectedSegment;
    private static GraphRubricSegmentCriterion beginningX;
    private static GraphRubricSegmentCriterion beginningY;
    private static GraphRubricSegmentCriterion endingX;
    private static GraphRubricSegmentCriterion endingY;
    private static GraphRubricSegmentCriterion slope;

    // TODO Add some tests for DELTA_X
    
    @BeforeClass
    public static void setup() {
        beginningX = new GraphRubricSegmentCriterion(Property.BEGINNING_X, Operation.EQUAL_TO, 1.0, 0.1, 1, false);
        beginningY = new GraphRubricSegmentCriterion(Property.BEGINNING_Y, Operation.EQUAL_TO, 6.0, 0.1, 1, false);
        endingX = new GraphRubricSegmentCriterion(Property.ENDING_X, Operation.EQUAL_TO, 3.0, 0.1, 1, false);
        endingY = new GraphRubricSegmentCriterion(Property.ENDING_Y, Operation.EQUAL_TO, 14.0, 0.1, 1, false);
        slope = new GraphRubricSegmentCriterion(Property.SLOPE, Operation.EQUAL_TO, 4.0, 0.1, 1, false);
        
        expectedSegment = new GraphRubricSegment(false);
        expectedSegment.add(beginningX);
        expectedSegment.add(beginningY);
        expectedSegment.add(endingX);
        expectedSegment.add(endingY);
        expectedSegment.add(slope);
    }
    
    @Test
    public void invalidStartingPointBadX() {
        SegmentResult result = new GraphSegment(2,3,0,4,2).evaluateBasedOn(expectedSegment);
        
        assertThat(result.getFailures().size()).as("number of failures").isEqualTo(2);
        assertThat(result.getFailedPoints()).as("failed points").isEqualTo(2);
        
        int i = 0;
        assertThat(result.getFailures().get(i++)).isEqualTo(beginningX);
        assertThat(result.getFailures().get(i++)).isEqualTo(beginningY);
    }
    
    @Test
    public void invalidStartingPointBadY() {
        SegmentResult result = new GraphSegment(1,3,0,3,5).evaluateBasedOn(expectedSegment);
        
        assertThat(result.getFailures().size()).as("number of failures").isEqualTo(2);
        assertThat(result.getFailedPoints()).as("failed points").isEqualTo(2);
        
        int i = 0;
        assertThat(result.getFailures().get(i++)).isEqualTo(beginningY);
        assertThat(result.getFailures().get(i++)).isEqualTo(slope);
    }
    
    @Test
    public void invalidEndingPointBadX() {
        SegmentResult result = new GraphSegment(1,4,0,4,2).evaluateBasedOn(expectedSegment);
        
        assertThat(result.getFailures().size()).as("number of failures").isEqualTo(2);
        assertThat(result.getFailedPoints()).as("failed points").isEqualTo(2);
        
        int i = 0;
        assertThat(result.getFailures().get(i++)).isEqualTo(endingX);
        assertThat(result.getFailures().get(i++)).isEqualTo(endingY);
    }
    
    @Test
    public void invalidEndingPointBadY() {
        SegmentResult result = new GraphSegment(1,3,0,6,0).evaluateBasedOn(expectedSegment);
        
        assertThat(result.getFailures().size()).as("number of failures").isEqualTo(2);
        assertThat(result.getFailedPoints()).as("failed points").isEqualTo(2);
        
        int i = 0;
        assertThat(result.getFailures().get(i++)).isEqualTo(endingY);
        assertThat(result.getFailures().get(i++)).isEqualTo(slope);
    }
    
    @Test
    public void invalidSlope() {
        SegmentResult result = new GraphSegment(1,3,0,5,1).evaluateBasedOn(expectedSegment);
        
        assertThat(result.getFailures().size()).as("number of failures").isEqualTo(2);
        assertThat(result.getFailedPoints()).as("failed points").isEqualTo(2);
        
        int i = 0;
        assertThat(result.getFailures().get(i++)).isEqualTo(endingY);
        assertThat(result.getFailures().get(i++)).isEqualTo(slope);
        
    }
    
    @Test
    public void match() {
        SegmentResult result = new GraphSegment(1,3,0,4,2).evaluateBasedOn(expectedSegment);
        
        assertThat(result.getFailures().size()).as("number of failures").isEqualTo(0);
        assertThat(result.getFailedPoints()).as("failed points").isEqualTo(0);
        assertThat(result.getPoints()).as("successful points").isEqualTo(5);
    }

}
