package org.concord.datagraph.analysis;

import java.awt.Component;
import java.util.ArrayList;

import org.concord.data.state.OTDataStore;
import org.concord.datagraph.analysis.rubric.GraphRubric;
import org.concord.datagraph.analysis.rubric.ResultSet;
import org.concord.datagraph.state.OTDataCollector;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.graph.util.state.OTVisibleGraphable;

public interface GraphAnalyzer {
    public enum AnnotationStyle { ONE, TWO, THREE, FOUR }
    public Graph getSegments(OTDataStore dataStore, int xChannel, int yChannel, double tolerance) throws IndexOutOfBoundsException;
    public GraphRubric buildRubric(OTObjectList rubric);
    public ResultSet compareGraphs(GraphRubric expected, Graph received);
    public String getHtmlReasons(ResultSet results);
    public void displayHtmlReasonsPopup(Component parent, ResultSet results);
    public ArrayList<OTVisibleGraphable> annotateResults(OTDataCollector dataCollector, ResultSet scoreResults, AnnotationStyle style);
    public ArrayList<OTVisibleGraphable> drawSegmentResults(OTDataCollector dataCollector, Graph graph);
}
