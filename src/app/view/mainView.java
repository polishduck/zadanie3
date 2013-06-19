package app.view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.imageio.*;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import app.controller.*;
import app.model.*;

public class mainView extends JFrame implements ListSelectionListener {
	
	private JList list;
	private String[] imageList = {"/home/patryk/Desktop/fotos/1.jpg", "/home/patryk/Desktop/fotos/2.png", "/home/patryk/Desktop/fotos/3.jpg", "/home/patryk/Desktop/fotos/4.jpg" };
	
	public mainView() throws IOException{
		setTitle("Przegladarka DICOM");
		setBackground(Color.white);
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane splitPane;
		JMenuBar menuBar=new JMenuBar();
		JMenu menu = new JMenu("Plik");
		menuBar.add(menu);
		
		File file = null;
		
		final JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new DicomFilter());
		fc.setAcceptAllFileFilterUsed(false);
		
		JMenuItem menuItem1 = new JMenuItem("Otworz");
		JMenuItem menuItem2 = new JMenuItem("Zamknij");
		
		menuItem1.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(mainView.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file;
					file = fc.getSelectedFile();
					System.out.print("Opened file " + file.getAbsolutePath() + "\n");
					
				}
			}
		});
		menuItem2.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem2.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(menuItem1);
		menu.add(menuItem2);
		setJMenuBar(menuBar);

		list = new JList(imageList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
		
		
		Image image = new ImageIcon("/home/patryk/Desktop/final.jpg").getImage();
		JLabel imagePanel = new JLabel(new ImageIcon(image));
		JScrollPane listScrollPane = new JScrollPane(list);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane , imagePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        imagePanel.setMinimumSize(minimumSize);
 
        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(400, 200));
    
		
		
	//	listScrollPane.setVisible(true);
	//	imagePanel.setVisible(true);
		add(splitPane);
//		add(listScrollPane);
	//	add(imagePanel);

		
		
		
		setVisible(true);
/*		
		BufferedImage image = ImageIO.read(file);
		System.out.println(image);
		
		JLabel imagePanel = new JLabel(new ImageIcon(image));
//		

//		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imagePanel, imagePanel);
//		splitPane.setOneTouchExpandable(true);
//		splitPane.setDividerLocation(150);
		
		
		
//		add(splitPane);
		add(imagePanel);
		
		DicomInputStream dis = null;
		DicomObject dcm = null;
		try {
			dis = new DicomInputStream(file);
			dcm = dis.readDicomObject();
		} catch (IOException e) {
			// ex
		} finally {
			if (dis != null) {
				CloseUtils.safeClose(dis);
			}
			
		}
		Iterator<DicomElement> iter = dcm.datasetIterator();
		while ( iter.hasNext() ) {
			DicomElement tag = iter.next();
			// print dicom tag
			System.out.println( tag );
			}
*/			
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		System.out.println("selected");
		
	}
	


}