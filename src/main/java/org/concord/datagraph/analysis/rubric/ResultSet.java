package org.concord.datagraph.analysis.rubric;

import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.concord.datagraph.analysis.Graph;
import org.concord.datagraph.analysis.GraphSegment;

public class ResultSet {
    private static final Logger logger = Logger.getLogger(ResultSet.class.getName());
    private GraphRubric expected;
    private Graph received;

    private Difference[][] differenceTable;
    private double xTolerance;
    private double yTolerance;
    private double sTolerance;
    
    private Difference finalDifference;

    public ResultSet(GraphRubric expected, Graph received) {
        this.expected = expected;
        this.received = received;

        differenceTable = new Difference[expected.size()+1][received.size()+1];

        double maxPositionError = 0.05;
        double maxSlopeError = 0.1;

        xTolerance = maxPositionError * received.getRange();
        yTolerance = maxPositionError * received.getDomain();
        sTolerance = maxSlopeError * received.getMaxSlope();

        try {
            finalDifference = analyze();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            finalDifference = new Difference(-1, new ArrayList<SegmentResult>());
        }
    }

    public double getScore() {
        return finalDifference.getPoints();
    }
    
    public double getMaxScore() {
        return expected.getPossiblePoints();
    }
    
    public double getScorePercent() {
        return (getScore()/getMaxScore())*100;
    }

    public ArrayList<SegmentResult> getReasons() {
        return finalDifference.getReasons();
    }

    // Uses the Levenshtein distance algorithm to find the shortest "distance" between the 2 Graphs.
    // Also stores the reasons for the distance so that when we find the shortest distance, we can report
    // about why that was the shortest, and what is different
    private Difference analyze() throws Exception {
        // setup the results table
        setupDifferenceTable();

        for (int j = 1; j <= received.size(); j++) {
            for (int i = 1; i <= expected.size(); i++) {
                GraphRubricSegment exp = expected.get(i-1);
                GraphSegment rec = received.get(j-1);
                SegmentResult result = rec.evaluateBasedOn(exp);
                
                Difference diff;
                if (result.getFailures().size() == 0) {
                     diff = createMatch(j, i, result);
                } else {
                    Difference up = differenceTable[i - 1][j];
                    Difference left = differenceTable[i][j - 1];
                    Difference diagonal = differenceTable[i - 1][j - 1];

                    
                    switch (getMinimumDistance(up, left, diagonal)) {
                    case DIAGONAL:
                        diff = createReplace(result, diagonal);
                        break;
                    case UP:
                        diff = createMissing(exp, up);
                        break;
                    case LEFT:
                        diff = createExtra(rec, left, exp);
                        break;
                    default:
                        throw new Exception("Somehow there wasn't a minimum distance!");
                    }
                }
                differenceTable[i][j] = diff;
            }
        }

        return differenceTable[expected.size()][received.size()];
    }
    
    private enum Direction { UP, LEFT, DIAGONAL };
    private Direction getMinimumDistance(Difference up, Difference left, Difference diagonal) throws Exception {
        if (diagonal.getDistance() <= left.getDistance() && diagonal.getDistance() <= up.getDistance()) {
            return Direction.DIAGONAL;
        } else if (up.getDistance() <= left.getDistance() && up.getDistance() <= diagonal.getDistance()) {
            return Direction.UP;
        } else if (left.getDistance() <= up.getDistance() && left.getDistance() <= diagonal.getDistance()) {
            return Direction.LEFT;
        } else {
            throw new Exception("Somehow there wasn't a minimum distance!");
        }
    }

    @SuppressWarnings("unchecked")
    private Difference createReplace(SegmentResult result, Difference diagonal) {
        ArrayList<SegmentResult> reasons = (ArrayList<SegmentResult>) diagonal.getReasons().clone();
        reasons.add(result);
        Difference diff = new Difference(diagonal.getDistance() + result.getFailedPoints(), reasons);
        return diff;
    }

    @SuppressWarnings("unchecked")
    private Difference createExtra(GraphSegment rec, Difference left, GraphRubricSegment exp) {
        ArrayList<SegmentResult> reasons = (ArrayList<SegmentResult>) left.getReasons().clone();
        reasons.add(new SegmentResult(rec, false));
        Difference diff = new Difference(left.getDistance() + exp.getPossiblePoints(), reasons);
        return diff;
    }

    @SuppressWarnings("unchecked")
    private Difference createMissing(GraphRubricSegment exp, Difference up) {
        ArrayList<SegmentResult> reasons = (ArrayList<SegmentResult>) up.getReasons().clone();
        reasons.add(getMissingResult(exp));
        Difference diff = new Difference(up.getDistance() + exp.getPossiblePoints(), reasons);
        return diff;
    }

    @SuppressWarnings("unchecked")
    private Difference createMatch(int j, int i, SegmentResult result) {
        Difference prevDiff = differenceTable[i - 1][j - 1];
         ArrayList<SegmentResult> reasons = (ArrayList<SegmentResult>) prevDiff.getReasons().clone();
         reasons.add(result);
         Difference diff = new Difference(prevDiff.getDistance(), reasons);
         return diff;
    }

    private void setupDifferenceTable() {
        // fill first column with "missing" segments
        for (int i = 0; i <= expected.size(); i++) {
            ArrayList<SegmentResult> reasons = new ArrayList<SegmentResult>();
            for (int r = 0; r < i; r++) {
                SegmentResult res = getMissingResult(expected.get(r));
                reasons.add(res);
            }
            differenceTable[i][0] = new Difference(i, reasons);
        }
        
        // fill first row with "extra" segments
        for (int j = 0; j <= received.size(); j++) {
            ArrayList<SegmentResult> reasons = new ArrayList<SegmentResult>();
            for (int r = 0; r < j; r++) {
                reasons.add(new SegmentResult(received.get(r), false));
            }
            differenceTable[0][j] = new Difference(j, reasons);
        }
    }

    private SegmentResult getMissingResult(GraphRubricSegment graphRubricSegment) {
        SegmentResult res = new SegmentResult(null, graphRubricSegment.isOptional());
        for (GraphRubricSegmentCriterion crit : graphRubricSegment) {
            res.addFailure(crit);
        }
        return res;
    }
    
    public static Color OPTIONAL_COLOR = new Color(0x000099); // green
    public static Color CORRECT_COLOR = new Color(0x009900); // green
    public static Color MODERATELY_CORRECT_COLOR = new Color(0xcc4400); // orangeish
    public static Color INCORRECT_COLOR = new Color(0x990000); // red
    
    public Color getResultColor() {
        double scorePct = getScorePercent();

        if (scorePct > 79) {
            return CORRECT_COLOR;
        } else if (scorePct > 49) {
            return MODERATELY_CORRECT_COLOR;
        } else {
            return INCORRECT_COLOR;
        }
    }

    class Difference {
        private double distance;
        private ArrayList<SegmentResult> reasons;

        public Difference(double distance, ArrayList<SegmentResult> reasons) {
            this.distance = distance;
            this.reasons = reasons;
        }

        public double getPoints() {
            double score = 0.0;
            for (SegmentResult res : reasons) {
                if (! res.isOptional()) {
                    score += res.getPoints();
                }
            }
            return score;
        }

        public double getDistance() {
            return distance;
        }

        public ArrayList<SegmentResult> getReasons() {
            return reasons;
        }
    }
}
