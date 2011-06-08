package org.concord.datagraph.analysis;

import java.awt.Component;
import java.util.ArrayList;

import org.concord.data.state.OTDataStore;
import org.concord.datagraph.analysis.rubric.GraphRubric;
import org.concord.datagraph.analysis.rubric.ResultSet;
import org.concord.datagraph.state.OTDataCollector;
import org.concord.datagraph.state.OTDataGraphable;
import org.concord.framework.otrunk.OTObjectList;
import org.concord.graph.util.state.OTHideableAnnotation;

public interface GraphAnalyzer {
    public Graph getSegments(OTDataStore dataStore, int xChannel, int yChannel, double tolerance) throws IndexOutOfBoundsException;
    public GraphRubric buildRubric(OTObjectList rubric);
    public ResultSet compareGraphs(GraphRubric expected, Graph received);
    public String getHtmlReasons(ResultSet results);
    public void displayHtmlReasonsPopup(Component parent, ResultSet results);
    public ArrayList<OTHideableAnnotation> annotateResults(OTDataCollector dataCollector, ResultSet scoreResults);
    public OTDataGraphable drawSegmentResults(OTDataCollector dataCollector, Graph graph);
}
