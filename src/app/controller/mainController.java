package app.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


import app.view.*;
import app.model.*;

public class mainController implements ActionListener {
	
	private mainView myView;

	public mainController() throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					myView = new mainView();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		 
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
