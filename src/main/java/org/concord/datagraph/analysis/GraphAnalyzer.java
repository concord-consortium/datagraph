package org.concord.datagraph.analysis;

import java.awt.Component;

import org.concord.data.state.OTDataStore;
import org.concord.datagraph.analysis.rubric.GraphRubric;
import org.concord.datagraph.analysis.rubric.ResultSet;
import org.concord.framework.otrunk.OTObjectList;

public interface GraphAnalyzer {
    public Graph getSegments(OTDataStore dataStore, int xChannel, int yChannel, double tolerance) throws IndexOutOfBoundsException;
    public GraphRubric buildRubric(OTObjectList rubric);
    public ResultSet compareGraphs(GraphRubric expected, Graph received);
    public String getHtmlReasons(ResultSet results);
    public void displayHtmlReasonsPopup(Component parent, ResultSet results);
}
