package org.concord.datagraph.test;

import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.concord.data.state.OTDataStore;
import org.concord.datagraph.state.OTDataCollector;
import org.concord.datagraph.state.OTDataGraphable;
import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.OTResourceList;
import org.concord.graph.ui.GraphWindow;
import org.concord.otrunk.view.OTConfig;
import org.concord.otrunk.view.OTViewer;
import org.concord.testing.gui.JDialogMatcher;
import org.concord.testing.gui.TestHelper;
import org.fest.assertions.Assertions;
import org.fest.assertions.Condition;
import org.fest.swing.core.MouseButton;
import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultiPredictionGraph {
    private static FrameFixture window;
    private static OTViewer viewer;
    private static OTDataCollector collector;
    private static JTreeFixture datagraphList;
    private static GraphWindow graphArea;

    @BeforeClass
    public static void setup() throws Exception {
        System.out.println("Running test setup");
        
        System.setProperty("sailotrunk.otmlurl", MultiPredictionGraph.class.getResource("/org/concord/datagraph/test/multi-prediction-graph.otml").toExternalForm());
        System.setProperty(OTConfig.AUTHOR_PROP, "false");
        System.setProperty(OTConfig.SINGLE_USER_PROP,"true");
        System.setProperty(OTConfig.SHOW_STATUS_PROP, "true");

        window = TestHelper.getFrameFixture();
        viewer = (OTViewer) window.component();
        collector = (OTDataCollector) viewer.getRoot();
        datagraphList = window.tree("colorTree");
        graphArea = window.robot.finder().findByType(GraphWindow.class, true);
    }
    
    @AfterClass
    public static void tearDown() throws Exception {
        System.out.println("Running test teardown");
        
        window.close();
        window.cleanUp();
    }
    
    private void clickAndDrag(Component c, Point start, Point end, ArrayList<Point> path) {
        window.robot.pressMouse(c, start, MouseButton.LEFT_BUTTON);
        for (Point p : path) {
            window.robot.moveMouse(c, p);
        }
        window.robot.moveMouse(c, end);
        window.robot.releaseMouse(MouseButton.LEFT_BUTTON);

    }
    
    private void drawRandomLines() {
        Point start = new Point(100, 50);
        Random r = new Random();
        ArrayList<Point> path = new ArrayList<Point>();
        for (int x = 101; x < 400; x += 5) {
            path.add(new Point(x, r.nextInt(300) + 50));
        }
        
        Point end = new Point(400, 350);
        
        clickAndDrag(graphArea, start, end, path);
    }
    
    private void assertHasSomeDataPoints(OTDataStore dataStore) {
        Assertions.assertThat(dataStore.getValues().size() > 0).as("Datastore '" + dataStore.getName() + "' should have values").isTrue();
    }
    
    private void assertHasNoDataPoints(OTDataStore dataStore) {
        Assertions.assertThat(dataStore.getValues().size() == 0).as("Datastore '" + dataStore.getName() + "' should not have values").isTrue();
    }
    
    private OTDataStore getDataStore(String name) {
        if (collector.getSource().getName().equals(name)) {
            return collector.getSource().getDataStore();
        }
        for (OTObject g : collector.getGraphables()) {
            if (g.getName().equals(name)) {
                return ((OTDataGraphable)g).getDataStore();
            }
        }
        return null;
    }
    
    private void createNewDatastore(String name) {
        window.button(JButtonMatcher.withText("New")).click();
        DialogFixture dialog = window.dialog(JDialogMatcher.withTitle("Input"));
        dialog.textBox().enterText(name);
        dialog.button(JButtonMatcher.withText("OK")).click();
    }
    
    @Test
    public void predictionWhenFirstLoaded() throws Exception {
        OTDataStore store = getDataStore("Motion");
        assertHasNoDataPoints(store);
        drawRandomLines();
        assertHasSomeDataPoints(store);
    }
    
    @Test
    public void predictionOnNewDatastore() throws Exception {
        createNewDatastore("new1");
        OTDataStore store = getDataStore("new1");
        assertHasNoDataPoints(store);
        drawRandomLines();
        assertHasSomeDataPoints(store);
    }
    
    private ArrayList<Object> getResourceListClone(OTResourceList list) {
        ArrayList<Object> outList = new ArrayList<Object>();
        for (Object val : list) {
            outList.add(val);
        }
        return outList;
    }

    @Test
    public void switchBackToOriginalDatastore() throws Exception {
        datagraphList.clickPath("Motion");
        final ArrayList<Object> originalValues = getResourceListClone(getDataStore("Motion").getValues());
        drawRandomLines();
        Assertions.assertThat(getDataStore("Motion").getValues().toArray()).as("New values should be different").doesNotSatisfy(new Condition<Object[]>() {
            @Override
            public boolean matches(Object[] arg0) {
                for (Object obj : arg0) {
                    if (! originalValues.contains(obj)) {
                        return false;
                    }
                }
                return true;
            }
        });
    }
    
    @Test
    public void clearOnlyClearsCurrentSelectedDatastore() throws Exception {
        window.button(JButtonMatcher.withText("Clear")).click();
        Assertions.assertThat(getDataStore("Motion").getValues().size()).as("Selected datastore should have no values").isEqualTo(0);
        Assertions.assertThat(getDataStore("new1").getValues().size()).as("Non-seletected datastore should still have values").isGreaterThan(0);
    }
}
