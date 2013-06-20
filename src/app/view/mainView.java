package app.view;


import java.awt.Color;
import java.awt.Dimension;

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
import java.net.URL;
import java.util.Date;
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
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import app.controller.*;
import app.model.*;

public class mainView extends JFrame implements ListSelectionListener {
	
	private JList list;
	private String[] imageList = {"/home/patryk/Desktop/fotos/1.jpg", "/home/patryk/Desktop/fotos/2.png", "/home/patryk/Desktop/fotos/3.jpg", "/home/patryk/Desktop/fotos/4.jpg" };
	public JLabel imagePanel; 
	public JSplitPane splitPane;
	public JScrollPane listScrollPane;
	public DicomInputStream dis = null;
	public DicomObject dcm = null;
	public File file;
	
	public mainView() throws IOException{
		setTitle("Przegladarka DICOM");
		setBackground(Color.white);
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar=new JMenuBar();
		JMenu menu = new JMenu("Plik");
		menuBar.add(menu);
		
	
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
					file = fc.getSelectedFile();
					System.out.print("Opened file " + file.getAbsolutePath() + "\n");
					
				}
				
				try {
					dis = new DicomInputStream(file);
					dcm = dis.readDicomObject();
				} catch (IOException e1) {
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
				String name = dcm.getString( Tag.PatientName);
				System.out.println("Imie i Nazwisko: " + name);
				
				Date date_birth = dcm.getDate(Tag.PatientBirthDate);
				System.out.println("Urodzony: " + date_birth);
				
				
				String study_des = dcm.getString(Tag.StudyDescription);			
				System.out.println("Badanie: " + study_des);
				
				
				String series_des = dcm.getString(Tag.SeriesDescription);
				System.out.println("Seria: " + series_des);
				
				Date date_des = dcm.getDate(Tag.StudyDate);
				System.out.println("Data badania: " + date_des);
								
				String study_type = dcm.getString(Tag.Modality);
				System.out.println("Rodzaj Badanie: " + study_type);
				
				String siu = dcm.getInt(Tag.SOPInstanceUID);
				System.out.println("Siu: " + siu);
				
				//DicomElement image1 = dcm.get(Tag.ReferencedImageSequence); //getObject(Tag.ReferencedImageSequence);
			//	displayTag.chooserTagDicom( );
				//DicomObject = dcm. // Bytes(Tag.ReferencedImageSequence);
			//	System.out.println("Lokalizacja: " + image1.length());
				

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
        
        imagePanel = new JLabel();
		listScrollPane = new JScrollPane(list);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane , imagePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        imagePanel.setMinimumSize(minimumSize);
 
        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(400, 200));
    

		add(splitPane);
		
		setVisible(true);
/*
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
		JList list = (JList)e.getSource();
		System.out.println(imageList[list.getSelectedIndex()]);
		ImageIcon icon = new ImageIcon(imageList[list.getSelectedIndex()]);
/*		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("/home/patryk/aaaaaaa/T1/0001.dcm"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ImageIcon icon = new ImageIcon(image);
*/
		imagePanel.setIcon(icon);

	}
	


}