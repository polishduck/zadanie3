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

public class mainView extends JFrame implements TreeSelectionListener {
	
	private JTree tree=null;
	private JTree tree2=null;
	private DefaultTreeModel treeModel ;
	//private String[] imageList= {"1","2"};
	public JLabel imagePanel; 
	public JSplitPane splitPane;
	public JScrollPane listScrollPane;
	public DicomInputStream dis = null;
	public DicomObject dcm = null;
	public File file;
	public File[] files;
	public DefaultMutableTreeNode top = new DefaultMutableTreeNode("");
	
	public mainView(mainController mController) throws IOException{
		setTitle("Przegladarka DICOM");
		setBackground(Color.white);
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar=new JMenuBar();
		JMenu menu = new JMenu("Plik");
		menuBar.add(menu);
	
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new DicomFilter());
		fc.setAcceptAllFileFilterUsed(true);
		
		JMenuItem menuItem1 = new JMenuItem("Otworz");
		JMenuItem menuItem2 = new JMenuItem("Zamknij");
		
		//menuItem1.addActionListener(mController);
		menuItem1.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(mainView.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					files = fc.getSelectedFiles();
				}
				
				for (int ii=0; ii<files.length;ii++) {
					
					System.out.print("Opened file " + files[ii].getAbsolutePath() + "\n");
					
					try {
						dis = new DicomInputStream(files[ii]);
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
				//		System.out.println( tag );
						}
					
					showDetails(dcm);
					
				}
				updateUI(files);
			}
		});
		menuItem2.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem2.addActionListener(mController);

		menu.add(menuItem1);
		menu.add(menuItem2);
		setJMenuBar(menuBar);

//		imageList = createTree(files);
		
		createUI();
		add(splitPane);
        setVisible(true);
	}

	public BufferedImage scaleImage(BufferedImage img, int width, int height, Color background) {
	    int imgWidth = img.getWidth();
	    int imgHeight = img.getHeight();
	    if (imgWidth*height < imgHeight*width) {
	        width = imgWidth*height/imgHeight;
	    } else {
	        height = imgHeight*width/imgWidth;
	    }
	    BufferedImage newImage = new BufferedImage(width, height,
	            BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = newImage.createGraphics();
	    try {
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        g.setBackground(background);
	        g.clearRect(0, 0, width, height);
	        g.drawImage(img, 0, 0, width, height, null);
	    } finally {
	        g.dispose();
	    }
	    return newImage;
	}
	
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
	
	private void createUI() {

		treeModel = new DefaultTreeModel(top);
		tree = new JTree(treeModel);
		
	    imagePanel = new JLabel();
		//listScrollPane = new JScrollPane(list);
	    listScrollPane = new JScrollPane(tree);
	        
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane , imagePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
			
	    Dimension minimumSize = new Dimension(100, 50);
	    listScrollPane.setMinimumSize(minimumSize);
	    imagePanel.setMinimumSize(minimumSize);
	 
	    //Provide a preferred size for the split pane.
	    splitPane.setPreferredSize(new Dimension(400, 200));
	}

	private void updateUI(File[] files2) {
		String name = null;
		//createTree(tree);
		for (int ii=0; ii<files2.length;ii++) {
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
			name = dcm.getString( Tag.PatientName);
			
		}
		
		
		DefaultMutableTreeNode top1 = new DefaultMutableTreeNode(name);
		treeModel.setRoot(top1);
		createNodes(top1, files2);
		//treeModel.nodeChanged(top1);
		
		//treeModel.insertNodeInto(top1, top, 0);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		
	}

	

	private void createNodes(DefaultMutableTreeNode top, File[] files2) {
		// TODO Auto-generated method stub
		
		int sizee = files2.length;
	    DefaultMutableTreeNode img = null;
		DefaultMutableTreeNode study = null;
		DefaultMutableTreeNode series = null;
	    String [] study_exam = new String[sizee];
	    String [] series_des = new String[sizee];
	    String [] names = new String[sizee];
		
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
		
			
			study_exam[ii] = dcm.getString(Tag.StudyDescription);			
			series_des[ii] = dcm.getString(Tag.SeriesDescription);
			names[ii] = files2[ii].getName();
		}
		//array = ArrayUtils.removeElement
		
	    for (int jj=0;jj<sizee;jj++) {
	    	
		    System.out.print("jj !! studies:" + study_exam[jj] + "\n");
		    System.out.print("jj !! series_des:" + series_des[jj] + "\n");
		    System.out.print("jj !! names:" + names[jj] + "\n");
	    }
		
	    for (int iii1=1;iii1<sizee;iii1++) {
	    	
		    System.out.print("studies:" + study_exam[iii1-1] + "\n");
		    System.out.print("series:" + series_des[iii1-1] + "\n");
		    System.out.print("img:" + names[iii1-1] + "\n");
		   
		    if (iii1==1){
		    	study = new DefaultMutableTreeNode(study_exam[iii1-1]);
			    top.add(study);
			    series = new DefaultMutableTreeNode(series_des[iii1-1]);
			    study.add(series);
		    }
		  /*
		    if(!(study_exam[iii-1].equals(study_exam[iii]))) {
		    	study = new DefaultMutableTreeNode(study_exam[iii-1]);
			    top.add(study);
		    }
		    if(!(series_des[iii-1].equals(series_des[iii]))) {
			    series = new DefaultMutableTreeNode(series_des[iii-1]);
			    study.add(series);
			    }
			   */
		    
	    }
	
	    for (int j=0;j<sizee;j++) {
			img = new DefaultMutableTreeNode(names[j]);
			series.add(img);
	    }
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null) return;
		if (node.isLeaf()) {
			System.out.println("click na itemie:" + node.getUserObject().toString());
			
			BufferedImage image = null;
			try {
				image = ImageIO.read(new File("/home/jrduck/aaaa/T1/" + node.getUserObject().toString()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.print("Opened file " + "/home/jrduck/aaaa/T1/" + node.getUserObject().toString() + "\n");
			
			try {
				dis = new DicomInputStream(new File("/home/jrduck/aaaa/T1/" + node.getUserObject().toString()));
				dcm = dis.readDicomObject();
			} catch (IOException e1) {
				// ex
			} finally {
				if (dis != null) {
					CloseUtils.safeClose(dis);
				}
				
			}
			
			showDetails(dcm);
			
			
			
			//image.setSize(imagePanel.getSize());
			Dimension d = imagePanel.getSize();
			int width =(int) d.getWidth();
			int height =(int) d.getHeight();
			System.out.println("wymiar okna:" + width + "x" +height);
			BufferedImage newImage = scaleImage(image, width, height, null);
			ImageIcon icon = new ImageIcon(newImage);
			
		//	ImageIcon icon = new ImageIcon(image);
			imagePanel.setIcon(icon);
			
		}
	}
	


}
