package org.concord.datagraph.analysis;

import static org.fest.assertions.Assertions.assertThat;

import org.concord.datagraph.analysis.Graph;
import org.concord.datagraph.analysis.GraphSegment;
import org.fest.assertions.Delta;
import org.junit.BeforeClass;
import org.junit.Test;


public class GraphTest {
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
    public void range() {
        assertThat(receivedGraph.getRange()).as("Range").isEqualTo(43.7-4.98);
    }
    
    @Test
    public void domain() {
        assertThat(receivedGraph.getDomain()).as("Domain").isEqualTo(2.8142, Delta.delta(0.00001));
    }
    
    @Test
    public void minX() {
        assertThat(receivedGraph.getMinX()).as("MinX").isEqualTo(4.98);
    }
    
    @Test
    public void maxX() {
        assertThat(receivedGraph.getMaxX()).as("MaxX").isEqualTo(43.7);
    }
    
    @Test
    public void minY() {
        assertThat(receivedGraph.getMinY()).as("MinY").isEqualTo(0.5036, Delta.delta(0.0001));
    }
    
    @Test
    public void maxY() {
        assertThat(receivedGraph.getMaxY()).as("MaxY").isEqualTo(3.3178);
    }
    
    @Test
    public void minSlope() {
        assertThat(receivedGraph.getMinSlope()).as("MinSlope").isEqualTo(-0.498);
    }
    
    @Test
    public void maxSlope() {
        assertThat(receivedGraph.getMaxSlope()).as("MaxSlope").isEqualTo(0.126);
    }
}
