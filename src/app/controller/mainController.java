package app.controller;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


import app.view.*;
import app.model.*;

public class mainController implements ActionListener {
	
	private mainView myView;

	public mainController() throws IOException {
		myView = new mainView(this);/*
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					myView = new mainView(this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        */
		 
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JMenuItem)
		{
			JMenuItem l2 = ((JMenuItem)e.getSource());
			String label2 = l2.getText();
			
			if(label2.equals("Zamknij")) {
				System.exit(0);
			}
			
		}
	}
}
