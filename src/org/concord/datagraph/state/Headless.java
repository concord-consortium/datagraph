package org.concord.datagraph.state;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

public class Headless extends JInternalFrame 
{
    public Headless(JComponent component) {
        setContentPane(component);
    }

    public void show() {
        super.show();
        // Although the above calculates the size of the components, it does not lay them out.
        // For some reason frame.validate simply delegates to Container.validate(), which does nothing
        // if there is no peer defined.
        addNotify();
        super.validateTree();
    }
}
