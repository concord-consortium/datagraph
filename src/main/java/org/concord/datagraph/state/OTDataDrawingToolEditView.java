package org.concord.datagraph.state;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.concord.framework.otrunk.OTObject;
import org.concord.framework.otrunk.otcore.OTClassProperty;
import org.concord.graph.util.state.OTDrawingStamp;
import org.concord.graph.util.state.OTDrawingTool;

/**
 * 
 * @deprecated org.concord.otrunk.graph.OTDataDrawingToolEditView should be used instead
 *
 */
public class OTDataDrawingToolEditView extends OTDataDrawingToolView implements ListSelectionListener {

	private OTDrawingTool otDraw;
	private JPanel wrapper;
	private JList stampsList;
	private JButton addButton;
	private JButton removeButton;
	private JTextField stampOptionText;
	private DefaultListModel listModel;
	private JFrame stampSelectorFrame;

	public JComponent getComponent(OTObject otObject)
	{
		otDraw = (OTDrawingTool)otObject;
		JComponent drawingWindow = super.getComponent(otObject);
		
		JPanel editbar = createToolBar();
		
		wrapper = new JPanel(new BorderLayout());
		wrapper.add(drawingWindow, BorderLayout.CENTER);
		wrapper.add(editbar, BorderLayout.SOUTH);
		
		return wrapper;
	}

	private JPanel createToolBar() {
		final JPanel toolBar = new JPanel();
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
		final JTextField bgImageText = new JTextField(20);
		bgImageText.setText(otDraw.getBackgroundImageURL());
		
		toolBar.add(wrapComponent(bgImageText, "Background image: "));
		JButton setBgImage = new JButton("Set background");
		toolBar.add(wrapComponent(setBgImage, null));
		JButton addStamps = new JButton("Add stamps");
		toolBar.add(wrapComponent(addStamps, null));
		
		setBgImage.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if (bgImageText.getText().length() < 1){
					if (otDraw.getBackgroundImage() != null && otDraw.getBackgroundImage().length > 1){
						byte[] emptyBytes = {};
						otDraw.setBackgroundImage(emptyBytes);
						redraw();
					} else {
						JOptionPane.showMessageDialog(toolBar, "Please specify a URL for the background image");
					}
				} else {
					try {
						URL bkImageURL = new URL(bgImageText.getText().trim());
						OTClassProperty property = otDraw.otClass().getProperty("backgroundImage");
						otDraw.otSet(property, bkImageURL);
						otDraw.setBackgroundImageURL(bgImageText.getText().trim());
						redraw();
					} catch (MalformedURLException e1) {
						JOptionPane.showMessageDialog(toolBar, "That URL is not valid.\nPlease use a URL starting with http://...", 
								"Invalid URL", JOptionPane.WARNING_MESSAGE);
					}
				}
			}});
		
		addStamps.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				showStampsDialog();
			}});
		return toolBar;
	}
	
	private void redraw(){
		wrapper.add(super.getComponent(otDraw));
	}
	
	private JPanel wrapComponent(JComponent component, String label) {
		JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEADING));
		
		if (label != null) {
			JLabel jLabel = new JLabel(label);
			wrapper.add(jLabel);
		}
		if (component != null) {
			wrapper.add(component);
		}

		return wrapper;
	}
	
	private void showStampsDialog(){
		JPanel listPanel = new JPanel(new BorderLayout());
		
		listModel = new DefaultListModel();
		addOriginalStampsToList();

        //Create the list and put it in a scroll pane.
        stampsList = new JList(listModel);
        stampsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stampsList.addListSelectionListener(this);
        stampsList.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(stampsList);

        addButton = new JButton("Add");
        AddListener listListener = new AddListener(addButton);
        addButton.setActionCommand("Remove");
        addButton.addActionListener(listListener);
        addButton.setEnabled(true);

        removeButton = new JButton("Remove");
        removeButton.setActionCommand("Remove");
        removeButton.addActionListener(new RemoveListener());
        removeButton.setEnabled(false);

        stampOptionText = new JTextField(10);
        stampOptionText.addActionListener(listListener);
        stampOptionText.getDocument().addDocumentListener(listListener);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        
        buttonPane.add(stampOptionText);
        buttonPane.add(addButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(removeButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        listPanel.add(listScrollPane, BorderLayout.CENTER);
        listPanel.add(buttonPane, BorderLayout.PAGE_END);
        
        stampSelectorFrame = new JFrame();
        stampSelectorFrame.getContentPane().add(listPanel);
        stampSelectorFrame.pack();
        stampSelectorFrame.setVisible(true);
	}
	
	private void addOriginalStampsToList(){
		Vector origStamps = otDraw.getStamps().getVector();
		for (int i = 0; i < origStamps.size(); i++) {
			if (origStamps.get(i) instanceof OTDrawingStamp){
				String text = ((OTDrawingStamp)origStamps.get(i)).getImageURL();
				if (text == null){
					text = "[unnamed stamp]";
				}
				listModel.addElement(text);
			} 
			
		}
	}
	
	private boolean addStamp(String stampURLStr){
		if (stampURLStr.length() < 1){
		    JOptionPane.showMessageDialog(stampSelectorFrame, "Please specify a URL for the stamp");
		} else {
			try {
				URL stampURL = new URL(stampURLStr.trim());
				OTDrawingStamp stamp = (OTDrawingStamp) otDraw.getOTObjectService().createObject(OTDrawingStamp.class);
				OTClassProperty property = stamp.otClass().getProperty("src");
				stamp.otSet(property, stampURL);
				stamp.setImageURL(stampURLStr.trim());
				System.out.println(stamp);
				otDraw.getStamps().add(stamp);
				redraw();
				
				return true;
			} catch (MalformedURLException e1) {
				JOptionPane.showMessageDialog(stampSelectorFrame, "That URL is not valid.\nPlease use a URL starting with http://...", 
						"Invalid URL", JOptionPane.WARNING_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private void removeStamp(int stampNumber){
		otDraw.getStamps().remove(stampNumber);
		redraw();
	}

	public void valueChanged(ListSelectionEvent e) {
		if(stampsList.getSelectedIndex() >= 0)
			removeButton.setEnabled(true);
		else removeButton.setEnabled(false);
	}
	
	class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
            
        }

        public void actionPerformed(ActionEvent e) {
            String name = stampOptionText.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                stampOptionText.requestFocusInWindow();
                stampOptionText.selectAll();
                return;
            }
            
            boolean success = addStamp(name);
            
            if (!success){
            	return;
            }

            int index = stampsList.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }

            listModel.insertElementAt(stampOptionText.getText(), index);
            
            stampOptionText.requestFocusInWindow();
            stampOptionText.setText("");

            stampsList.setSelectedIndex(index);
            stampsList.ensureIndexIsVisible(index);
        }

        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

		public void changedUpdate(DocumentEvent e)
        {
	        // TODO Auto-generated method stub
	        
        }

		public void insertUpdate(DocumentEvent e)
        {
	        // TODO Auto-generated method stub
	        
        }

		public void removeUpdate(DocumentEvent e)
        {
	        // TODO Auto-generated method stub
	        
        }

    }

	class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
            int index = stampsList.getSelectedIndex();
            listModel.remove(index);
            
            removeStamp(index);

            int size = listModel.getSize();

            if (size == 0) {
                removeButton.setEnabled(false);

            } else {
                if (index == listModel.getSize()) {
                    index--;
                }

                stampsList.setSelectedIndex(index);
                stampsList.ensureIndexIsVisible(index);
            }
            
       //     setOptions();
        }
    }


}
