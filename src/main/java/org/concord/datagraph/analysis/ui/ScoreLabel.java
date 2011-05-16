package org.concord.datagraph.analysis.ui;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JLabel;

import org.concord.datagraph.analysis.Graph;
import org.concord.datagraph.analysis.GraphAnalyzer;
import org.concord.datagraph.analysis.GraphAnalyzerProvider;
import org.concord.datagraph.analysis.GraphAnalyzerProvider.Type;
import org.concord.datagraph.analysis.rubric.GraphRubric;
import org.concord.datagraph.analysis.rubric.ResultSet;
import org.concord.datagraph.state.OTDataCollector;
import org.concord.datagraph.state.OTDataGraphable;
import org.concord.graph.util.state.OTHideableAnnotation;

public class ScoreLabel extends JLabel {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ScoreLabel.class.getName());
    private static GraphAnalyzer graphAnalyzer;
    private OTDataCollector dataCollector;
    private OTDataGraphable graphable;
    private ResultSet results;
    private ArrayList<OTHideableAnnotation> graphAnalysisAnnotations = new ArrayList<OTHideableAnnotation>();
    private boolean annotationsVisible = false;

    public ScoreLabel(OTDataCollector dataCollector) {
        this.dataCollector = dataCollector;
        this.graphable = dataCollector.getSource();
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent paramMouseEvent) {
                if (graphAnalyzer != null) {
                    graphAnalyzer.displayHtmlReasonsPopup(ScoreLabel.this, results);
                }
            }
        });

        setText("...");

        Thread t = new Thread(new Runnable() {
            public void run() {
                graphAnalyzer = GraphAnalyzerProvider.findAnalyzer(Type.ANY);

                if (graphAnalyzer == null) {
                    logger.severe("Couldn't get a GraphAnalyzer!!!");
                } else {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            calculateScore();
                        }
                    });
                }
            }
        });
        t.start();
    }

    public void setGraphAnalysisAnnotationsVisible(boolean show) {
        annotationsVisible = show;
        refreshAnnotations();
    }

    public void calculateScore() {
        // Need graph analysis service...
        if (graphAnalyzer != null && graphable.getRubric().size() > 0) {
            clearAnnotations();
            
            Graph segments = graphAnalyzer.getSegments(graphable.getDataStore(), 0, 1, graphable.getSegmentingTolerance());
            GraphRubric rubric = graphAnalyzer.buildRubric(graphable.getRubric());
            results = graphAnalyzer.compareGraphs(rubric, segments);
            double scorePct = results.getScorePercent();
            setText(String.format("%2.0f%%", scorePct));

            setForeground(results.getResultColor());
            
            refreshAnnotations();
        } else {
            setText("??");
        }
        repaint();
    }
    
    public ResultSet getResults() {
        return results;
    }
    
    private void refreshAnnotations() {
        if (graphAnalysisAnnotations != null && graphAnalysisAnnotations.size() > 0) {
            for (OTHideableAnnotation ann : graphAnalysisAnnotations) {
                ann.setVisible(annotationsVisible);
            }
        } else if (annotationsVisible && results != null) {
            graphAnalysisAnnotations = graphAnalyzer.annotateResults(dataCollector, results);
        }
    }
    
    private void clearAnnotations() {
        if (graphAnalysisAnnotations != null && graphAnalysisAnnotations.size() > 0) {
            for (OTHideableAnnotation ann : graphAnalysisAnnotations) {
                ann.setVisible(false);
                dataCollector.getLabels().remove(ann);
            }
            graphAnalysisAnnotations.clear();
        }
    }
}
