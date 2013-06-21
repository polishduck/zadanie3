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

import app.model.*;

public class mainView extends JFrame implements ListSelectionListener {
	
	private JList list;
	private String[] imageList = {	"/home/patryk/aaaaaaa/T1/0001.dcm", 
									"/home/patryk/aaaaaaa/T1/0002.dcm", 
									"/home/patryk/aaaaaaa/T1/0003.dcm", 
									"/home/patryk/aaaaaaa/T1/0004.dcm", 
									"/home/patryk/aaaaaaa/T1/0005.dcm", 
									"/home/patryk/aaaaaaa/T1/0006.dcm", 
									"/home/patryk/aaaaaaa/T1/0007.dcm", 
									"/home/patryk/aaaaaaa/T1/0008.dcm", 
									"/home/patryk/aaaaaaa/T1/0009.dcm" 
		}; 
	public JLabel imagePanel; 
	public JSplitPane splitPane;
	public JScrollPane listScrollPane;
	public DicomInputStream dis = null;
	public DicomObject dcm = null;
	public File file;
	public File[] files;
	
	public mainView() throws IOException{
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
	
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList)e.getSource();
		System.out.println(imageList[list.getSelectedIndex()]);
//		ImageIcon icon = new ImageIcon(imageList[list.getSelectedIndex()]);
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(imageList[list.getSelectedIndex()]));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//image.setSize(imagePanel.getSize());
		Dimension d = imagePanel.getSize();
		int width =(int) d.getWidth();
		int height =(int) d.getHeight();
		System.out.println("wymiar:" + width + "x" +height);
		BufferedImage newImage = scaleImage(image, width, height, null);
		
		ImageIcon icon = new ImageIcon(newImage);
		imagePanel.setIcon(icon);
	}
	
	public BufferedImage scaleImage(BufferedImage img, int width, int height,
	        Color background) {
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
	


}

//http://docs.oracle.com/javase/tutorial/uiswing/components/tree.html