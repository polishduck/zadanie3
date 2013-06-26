package app.model;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTree;

@SuppressWarnings("serial")
public class listPanel extends JScrollPane{

	public listPanel(JTree tree) {
		super(tree);
		
	    Dimension minimumSize = new Dimension(100, 50);
	    setMinimumSize(minimumSize);
	}
}
