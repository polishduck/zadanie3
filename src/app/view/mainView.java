package app.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.Destroyable;
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
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.imageio.*;

import jj2000.j2k.util.ArrayUtil;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import app.controller.mainController;
import app.model.*;

public class mainView extends JFrame {
	
	public static JTree tree=null;
	private JTree tree2=null;
	private static DefaultTreeModel treeModel ;
	//private String[] imageList= {"1","2"};
	public static imagePanel imagePanel; 
	public JSplitPane splitPane;
	public listPanel listScrollPane;
	public static DicomInputStream dis = null;
	public static DicomObject dcm = null;
	public File file;
	public File[] files;
	public DefaultMutableTreeNode top = new DefaultMutableTreeNode("");
	public static DefaultMutableTreeNode name = null;
	public static DefaultMutableTreeNode img = null;
	public static DefaultMutableTreeNode study = null;
	public static DefaultMutableTreeNode series = null;
	
	public mainView(mainController mController) throws IOException{
		setTitle("Przegladarka DICOM");
		setBackground(Color.white);
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar=new JMenuBar();
		JMenu menu = new JMenu("Plik");
		menuBar.add(menu);
	

		
		JMenuItem menuItem1 = new JMenuItem("Otworz");
		JMenuItem menuItem2 = new JMenuItem("Zamknij");
		
		menuItem1.addActionListener(mController);
		menuItem2.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem2.addActionListener(mController);

		menu.add(menuItem1);
		menu.add(menuItem2);
		setJMenuBar(menuBar);

		createUI();
		add(splitPane);
        setVisible(true);
	}

	private void createUI() {

		treeModel = new DefaultTreeModel(top);
		
		tree = new JTree(treeModel);
		tree.setRootVisible(false); // Sets everything invisible
		
	    imagePanel = new imagePanel();
		//listScrollPane = new JScrollPane(list);
	    listScrollPane = new listPanel(tree);
	        
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane , imagePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
			
	    splitPane.setPreferredSize(new Dimension(400, 200));
	    
	    
	}

	public static void updateUI(File[] files2, mainController mController) {
		
		DefaultMutableTreeNode top1 = new DefaultMutableTreeNode("new");
		treeModel.setRoot(top1);
		createNodes(top1, files2);
		
	    tree.setRootVisible(false); 
	    tree.setEditable(true); 
	    tree.setShowsRootHandles(true);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(mController);
		tree.expandPath(new TreePath(top1.getPath()));
		
	}

	private static void createNodes(DefaultMutableTreeNode top, File[] files2) {
		// TODO Auto-generated method stub
		
		int sizee = files2.length;

		String [] names = new String[sizee];
	    String [] study_exam = new String[sizee];
	    String [] series_des = new String[sizee];
	    String [] file_names = new String[sizee];
		
		for (int ii=0; ii<files2.length;ii++) {
			
			System.out.print("Create nodes Opened file " + files2[ii].getAbsolutePath() + "\n");
			
			try {
				dis = new DicomInputStream(files2[ii]);
				dcm = dis.readDicomObject();
			} catch (IOException e1) {
				// ex
			} finally {
				if (dis != null) {
					CloseUtils.safeClose(dis);
				}
				
			}
		
			names[ii] = dcm.getString( Tag.PatientName);
			study_exam[ii] = dcm.getString(Tag.StudyDescription);			
			series_des[ii] = dcm.getString(Tag.SeriesDescription);
			file_names[ii] = files2[ii].getName();
		}
		
	    for (int iii1=0;iii1<sizee;iii1++) {
	    		    
		    	if (iii1 == 0) {
		    		name = new DefaultMutableTreeNode(names[iii1]);
			    	top.add(name);	
		    	}
		    	else if (!(names[iii1-1].equals(names[iii1]))) {
		    		name = new DefaultMutableTreeNode(names[iii1]);
			    	top.add(name);	
		    	}
	    	
		    	if (iii1 == 0) {
			    	study = new DefaultMutableTreeNode(study_exam[iii1]);
				    name.add(study);
		    	}
		    	else if (!(study_exam[iii1-1].equals(study_exam[iii1]))) {
			    	study = new DefaultMutableTreeNode(study_exam[iii1]);
				    name.add(study);
		    	}
	    	
		    	if (iii1 == 0) {
				    series = new DefaultMutableTreeNode(series_des[iii1]);
				    study.add(series);
		    	}
		    	else if (!(series_des[iii1-1].equals(series_des[iii1]))) {
				    series = new DefaultMutableTreeNode(series_des[iii1]);
				    study.add(series);
		    	}
		    	
		    	
		    	if (iii1 == 0) {
					img = new DefaultMutableTreeNode(file_names[iii1]);
					series.add(img);
		    	}
		    	else if (!(img.equals(file_names[iii1]))) {
					img = new DefaultMutableTreeNode(file_names[iii1]);
					series.add(img);
		    	}

	    }
	
	    for (int j=0;j<sizee;j++) {

	    }

	}
/*
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub

	}
	*/
	private void showDetails(DicomObject dcm) {
		// TODO Auto-generated method stub
		
		System.out.println("Show Details\n");
		
		String name = dcm.getString( Tag.PatientName);
		System.out.println("Imie i Nazwisko: " + name);
		
		Date date_birth = dcm.getDate(Tag.PatientBirthDate);
		System.out.println("Urodzony: " + date_birth);
		
		String study_exam = dcm.getString(Tag.StudyDescription);			
		System.out.println("Badanie: " + study_exam);
        
		String series_des = dcm.getString(Tag.SeriesDescription);
		System.out.println("Seria: " + series_des);
		
		Date date_des = dcm.getDate(Tag.StudyDate);
		System.out.println("Data badania: " + date_des);
		
		String studyID = dcm.getString(Tag.StudyID);
		System.out.println("ID badania: " + studyID);
		
		String seriesNo = dcm.getString(Tag.SeriesNumber);
		System.out.println("Nr Serii: " + seriesNo);
		
		String instNo = dcm.getString(Tag.InstanceNumber);
		System.out.println("Nr instancji: " + instNo);
						
		String study_type = dcm.getString(Tag.Modality);
		System.out.println("Rodzaj Badanie: " + study_type);
		
		String studyInstance = dcm.getString(Tag.StudyInstanceUID);
		System.out.println("studyInstance: " + studyInstance);
		
		String seriseInstance = dcm.getString(Tag.SeriesInstanceUID);
		System.out.println("seriseInstance: " + seriseInstance);
		
		String sopInstanceUID = dcm.getString(Tag.SOPInstanceUID);
		System.out.println("sopInstanceUID: " + sopInstanceUID + "\n\n");
		
	}

}
