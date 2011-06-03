package org.concord.datagraph.analysis.rubric;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;

import org.concord.datagraph.analysis.Graph;
import org.concord.datagraph.analysis.GraphSegment;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Operation;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;
import org.junit.BeforeClass;
import org.junit.Test;

public class ResultSetTest {
    private static Graph receivedGraph;

    @BeforeClass
    public static void setup() {
        receivedGraph = new Graph();
        receivedGraph.add(new GraphSegment(4.98, 24.2, 0, 0.124, 0.317));
        receivedGraph.add(new GraphSegment(24.2, 29.8, 0, -0.498, 15.344));
        receivedGraph.add(new GraphSegment(29.8, 43.7, 0, 0.031, -0.411));
        receivedGraph.add(new GraphSegment(43.7, 60, 0, 0.126, -4.57));
    }
    
    @Test
    public void evaluateCorrectGraph() throws Exception {
        Graph expectedGraph = new Graph();
        expectedGraph.add(new GraphSegment(4.98, 24.2, 0, 0.124, 0.317));
        expectedGraph.add(new GraphSegment(24.2, 29.8, 0, -0.498, 15.344));
        expectedGraph.add(new GraphSegment(29.8, 43.7, 0, 0.031, -0.411));
        expectedGraph.add(new GraphSegment(43.7, 60, 0, 0.126, -4.57));
        
        ResultSet results = new ResultSet(buildRubric(expectedGraph), receivedGraph);
        
        int count = 0;
        for (SegmentResult result : results.getReasons()) {
            assertThat(result.getFailedPoints()).as("Segment " + (++count) + " results").isEqualTo(0.0);
        }
    }
    
    @Test
    public void evaluateBadEndPointGraph() throws Exception {
        Graph expectedGraph = new Graph();
        expectedGraph.add(new GraphSegment(4.98, 21.2, 0, 1.0/8.0, 3.0/8.0));
        expectedGraph.add(new GraphSegment(24.2, 29.8, 0, -0.498, 15.344));
        expectedGraph.add(new GraphSegment(29.8, 43.7, 0, 0.031, -0.411));
        expectedGraph.add(new GraphSegment(43.7, 60, 0, 0.126, -4.57));
        
        ArrayList<SegmentResult> results = new ResultSet(buildRubric(expectedGraph), receivedGraph).getReasons();
        
        int i = 0;
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(2.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
    }
    
    @Test
    public void evaluateBadBeginningPointGraph() throws Exception {
        Graph expectedGraph = new Graph();
        expectedGraph.add(new GraphSegment(4.98, 24.2, 0, 0.25, -2.65));
        expectedGraph.add(new GraphSegment(24.2, 29.8, 0, -0.498, 15.344));
        expectedGraph.add(new GraphSegment(29.8, 43.7, 0, 0.031, -0.411));
        expectedGraph.add(new GraphSegment(43.7, 60, 0, 0.126, -4.57));
        
        ArrayList<SegmentResult> results = new ResultSet(buildRubric(expectedGraph), receivedGraph).getReasons();
        
        int i = 0;
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(2.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
    }
    
    @Test
    public void evaluateBadSlopeGraph() throws Exception {
        Graph expectedGraph = new Graph();
        expectedGraph.add(new GraphSegment(4.98, 24.2, 0, 0.25, -0.2475));
        expectedGraph.add(new GraphSegment(24.2, 29.8, 0, -0.498, 15.344));
        expectedGraph.add(new GraphSegment(29.8, 43.7, 0, 0.031, -0.411));
        expectedGraph.add(new GraphSegment(43.7, 60, 0, 0.126, -4.57));
        
        ArrayList<SegmentResult> results = new ResultSet(buildRubric(expectedGraph), receivedGraph).getReasons();
        
        int i = 0;
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(2.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
    }
    
    @Test
    public void evaluateBadOppositeSignSlopeGraph() throws Exception {
        Graph expectedGraph = new Graph();
        expectedGraph.add(new GraphSegment(4.98, 24.2, 0, 0.25, -0.2475));
        expectedGraph.add(new GraphSegment(24.2, 29.8, 0, 0.498, 15.344));
        expectedGraph.add(new GraphSegment(29.8, 43.7, 0, 0.031, -0.411));
        expectedGraph.add(new GraphSegment(43.7, 60, 0, 0.126, -4.57));
        
        ArrayList<SegmentResult> results = new ResultSet(buildRubric(expectedGraph), receivedGraph).getReasons();
        
        int i = 0;
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(2.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(3.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
    }
    
    @Test
    public void evaluateMismatchedSegmentCountMissingSegmentGraph() throws Exception {
        Graph expectedGraph = new Graph();
        expectedGraph.add(new GraphSegment(4.98, 24.2, 0, 0.124, 0.317));
        expectedGraph.add(new GraphSegment(24.2, 29.8, 0, -0.498, 15.344));
        expectedGraph.add(new GraphSegment(29.8, 43.7, 0, 0.031, -0.411));
        expectedGraph.add(new GraphSegment(43.7, 60, 0, 0.126, -4.57));
        expectedGraph.add(new GraphSegment(58.7, 71, 0, 0, -1));
        
        ArrayList<SegmentResult> results = new ResultSet(buildRubric(expectedGraph), receivedGraph).getReasons();
        
        int i = 0;
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i).getFailedPoints()).as("Segment " + i + " results").isEqualTo(5.0);
        assertThat(results.get(i).getPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        
    }
    
    @Test
    public void evaluateMismatchedSegmentCountExtraSegmentGraph() throws Exception {
        Graph expectedGraph = new Graph();
        expectedGraph.add(new GraphSegment(4.98, 24.2, 0, 0.124, 0.317));
        expectedGraph.add(new GraphSegment(24.2, 29.8, 0, -0.498, 15.344));
        expectedGraph.add(new GraphSegment(29.8, 43.7, 0, 0.031, -0.411));
        
        ArrayList<SegmentResult> results = new ResultSet(buildRubric(expectedGraph), receivedGraph).getReasons();
        
        int i = 0;
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i++).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i).getFailedPoints()).as("Segment " + i + " results").isEqualTo(0.0);
        assertThat(results.get(i).getPoints()).as("Segment " + i + " results").isEqualTo(0.0);
    }
    
    // TODO Add tests for extra/missing segments at the beginning of and middle of the graphs.
    // TODO Add tests for various combinations of missing/extra segments to make sure the matching algorithm can handle it
    
    private GraphRubric buildRubric(Graph graph) {
        GraphRubric rubric = new GraphRubric();
        for (GraphSegment seg : graph) {
            GraphRubricSegment segRubric = new GraphRubricSegment(false);
            segRubric.add(new GraphRubricSegmentCriterion(Property.BEGINNING_X, Operation.EQUAL_TO, seg.getX1(), 0.1, 1, false));
            segRubric.add(new GraphRubricSegmentCriterion(Property.BEGINNING_Y, Operation.EQUAL_TO, seg.getY1(), 0.1, 1, false));
            segRubric.add(new GraphRubricSegmentCriterion(Property.ENDING_X, Operation.EQUAL_TO, seg.getX2(), 0.1, 1, false));
            segRubric.add(new GraphRubricSegmentCriterion(Property.ENDING_Y, Operation.EQUAL_TO, seg.getY2(), 0.1, 1, false));
            segRubric.add(new GraphRubricSegmentCriterion(Property.SLOPE, Operation.EQUAL_TO, seg.getB(), 0.1, 1, false));
            
            rubric.add(segRubric);
        }
        return rubric;
    }
}
