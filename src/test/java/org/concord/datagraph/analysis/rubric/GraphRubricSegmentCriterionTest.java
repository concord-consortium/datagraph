package org.concord.datagraph.analysis.rubric;

import static org.fest.assertions.Assertions.assertThat;

import org.concord.datagraph.analysis.GraphSegment;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Operation;
import org.concord.datagraph.state.rubric.OTGraphSegmentCriterion.Property;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphRubricSegmentCriterionTest {
    private static GraphSegment graphSegment;
    @BeforeClass
    public static void setup() {
        graphSegment = new GraphSegment(0.0, 1.2, 0.0, 3.4, 2.1);
    }

    @Test
    public void testAllCombinations() {
        for (Property p : Property.values()) {
            for (Operation o : Operation.values()) {
                checkMatch(p, o);
            }
        }
    }
    
    @Test
    public void propertyNull() {
        GraphRubricSegmentCriterion crit = new GraphRubricSegmentCriterion(null, Operation.EQUAL_TO, 1.0, 0.0, 1, false);
        assertThat(crit.matches(graphSegment)).as("Null property test").isFalse();
    }
    
    @Test
    public void operationNull() {
        GraphRubricSegmentCriterion crit = new GraphRubricSegmentCriterion(Property.DELTA_X, null, 1.0, 0.0, 1, false);
        assertThat(crit.matches(graphSegment)).as("Null operation test").isFalse();
    }
    
    private void checkMatch(Property p, Operation o) {
        double defaultExpectedValue = getExpected(p);
        double expectedValue;
        boolean expectedResult = true;
        
        // Equals
        expectedValue = defaultExpectedValue;
        expectedResult = true;
        if (o.equals(Operation.GREATER_THAN) || o.equals(Operation.LESS_THAN)) {
            expectedResult = false;
        }
        GraphRubricSegmentCriterion crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.0, 1, false);
        assertThat(crit.matches(graphSegment)).as("Equal - p: " + p + ", o: " + o + ", v: " + expectedValue + ", v: " + defaultExpectedValue).isEqualTo(expectedResult);
        
        // Opposite sign
        expectedValue = 0 - defaultExpectedValue;
        if (expectedValue == 0.0) {
            expectedResult = true;
            if (o.equals(Operation.GREATER_THAN) || o.equals(Operation.LESS_THAN)) {
                expectedResult = false;
            }
        } else {
            expectedResult = false;
            if (o.equals(Operation.GREATER_THAN) || o.equals(Operation.GREATER_THAN_OR_EQUAL_TO)) {
                expectedResult = true;
            }
        }
        crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.0, 1, false);
        assertThat(crit.matches(graphSegment)).as("Opposite sign - p: " + p + ", o: " + o + ", ev: " + expectedValue + ", rv: " + defaultExpectedValue).isEqualTo(expectedResult);
        
        // Too low
        expectedValue = defaultExpectedValue+0.7;
        expectedResult = false;
        if (o.equals(Operation.LESS_THAN) || o.equals(Operation.LESS_THAN_OR_EQUAL_TO)) {
            expectedResult = true;
        }
        crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.0, 1, false);
        assertThat(crit.matches(graphSegment)).as("Low (out) - p: " + p + ", o: " + o + ", v: " + expectedValue + ", v: " + defaultExpectedValue).isEqualTo(expectedResult);
        
        // Too high
        expectedValue = defaultExpectedValue-0.7;
        expectedResult = false;
        if (o.equals(Operation.GREATER_THAN) || o.equals(Operation.GREATER_THAN_OR_EQUAL_TO)) {
            expectedResult = true;
        }
        crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.0, 1, false);
        assertThat(crit.matches(graphSegment)).as("High (out) - p: " + p + ", o: " + o + ", e: " + expectedValue + ", v: " + defaultExpectedValue).isEqualTo(expectedResult);
        
        // Equals, within tolerance
        expectedValue = defaultExpectedValue;
        expectedResult = true;
        crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.5, 1, false);
        assertThat(crit.matches(graphSegment)).as("Equal (tol) - p: " + p + ", o: " + o + ", v: " + expectedValue + ", v: " + defaultExpectedValue).isEqualTo(expectedResult);
        
        // Low, within tolerance
        expectedValue = defaultExpectedValue+0.4;
        expectedResult = true;
        crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.5, 1, false);
        assertThat(crit.matches(graphSegment)).as("Low (tol) - p: " + p + ", o: " + o + ", v: " + expectedValue + ", v: " + defaultExpectedValue).isEqualTo(expectedResult);
        
        // high, within tolerance
        expectedValue = defaultExpectedValue-0.4;
        expectedResult = true;
        crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.5, 1, false);
        assertThat(crit.matches(graphSegment)).as("High (tol) - p: " + p + ", o: " + o + ", v: " + expectedValue + ", v: " + defaultExpectedValue).isEqualTo(expectedResult);
        
        // Too low, out of tolerance
        expectedValue = defaultExpectedValue+0.7;
        expectedResult = false;
        if (o.equals(Operation.LESS_THAN) || o.equals(Operation.LESS_THAN_OR_EQUAL_TO)) {
            expectedResult = true;
        }
        crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.5, 1, false);
        assertThat(crit.matches(graphSegment)).as("Low (out) - p: " + p + ", o: " + o + ", v: " + expectedValue + ", v: " + defaultExpectedValue).isEqualTo(expectedResult);
        
        // Too high, out of tolerance
        expectedValue = defaultExpectedValue-0.7;
        expectedResult = false;
        if (o.equals(Operation.GREATER_THAN) || o.equals(Operation.GREATER_THAN_OR_EQUAL_TO)) {
            expectedResult = true;
        }
        crit = new GraphRubricSegmentCriterion(p, o, expectedValue, 0.5, 1, false);
        assertThat(crit.matches(graphSegment)).as("High (out) - p: " + p + ", o: " + o + ", e: " + expectedValue + ", v: " + defaultExpectedValue).isEqualTo(expectedResult);
    }

    private double getExpected(Property p) {
        switch (p) {
        case BEGINNING_X:
            return graphSegment.getX1();
        case BEGINNING_Y:
            return graphSegment.getY1();
        case ENDING_X:
            return graphSegment.getX2();
        case ENDING_Y:
            return graphSegment.getY2();
        case DELTA_X:
            return graphSegment.getX2() - graphSegment.getX1();
        case DELTA_Y:
            return graphSegment.getY2() - graphSegment.getY1();
        case SLOPE:
            return graphSegment.getB();
        default:
            break;
        }
        return 0;
    }
}
