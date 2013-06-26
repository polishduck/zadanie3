package app.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReader;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.tool.jpg2dcm.Jpg2Dcm;
import org.dcm4che2.util.CloseUtils;

import sun.util.resources.CalendarData;


import app.view.*;
import app.model.*;

public class mainController implements ActionListener, TreeSelectionListener {
	
	private mainView myView;
	public File[] files;
	public DicomInputStream dis = null;
	public DicomObject dcm = null;
	public JFileChooser fc = null;
	public patientData pData;

	public mainController() throws IOException {
		myView = new mainView(this);
		fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new DicomFilter());
		fc.setAcceptAllFileFilterUsed(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JMenuItem)
		{
			JMenuItem menuItem = ((JMenuItem)e.getSource());
			String label = menuItem.getText();
			
			System.out.print(label);
			
			if(label.equals("Otworz")) {
				System.out.print("Open call");
				int returnVal = fc.showOpenDialog(myView);
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
						System.out.println( tag );
						}					
				}
				mainView.updateUI(files,this);
			}
			
			if(label.equals("Zamknij")) {
				System.exit(0);
			}
			
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)mainView.tree.getLastSelectedPathComponent();
		if (node == null) return;
		if (node.isLeaf()) {
			//String a = clicked_index(mainView.tree, node);
	
			String aa = node.getUserObject().toString();
			String a = aa + ".dcm";
			int index = 0;	
			
			for (int i=0; i<files.length;i++) {
				if (files[i].getName().equals(a)) {
					index = i;
				}
			}
				
			//TreeNode parent = node.getParent();

			System.out.println("click na itemie nr:" +  index + " i nazwie" + a);
	
			System.out.print("Opened file " + files[index].getAbsolutePath() + "\n");
			
			try {
				dis = new DicomInputStream(new File(files[index].getAbsolutePath()));
				dcm = dis.readDicomObject();
			} catch (IOException e1) {
				// ex
			} finally {
				if (dis != null) {
					CloseUtils.safeClose(dis);
				}
				
			}
			
			String name = dcm.getString( Tag.PatientName);
			Date birth = dcm.getDate(Tag.PatientBirthDate);
			Calendar date_birth = Calendar.getInstance();
			if (birth !=null) {
				date_birth.set(Calendar.DATE, birth.getDay());
				date_birth.set(Calendar.MONTH, birth.getMonth());
				date_birth.set(Calendar.YEAR, birth.getYear()+1900);
					
			}
			String study_type = dcm.getString(Tag.Modality);

			pData = new patientData(name, date_birth, study_type);
			
			FileImageInputStream fiis;
			BufferedImage image = null;
			
			try {
				fiis = new FileImageInputStream(new File(files[index].getAbsolutePath()));
				image = ImageIO.read(fiis);   //(fiis);
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			Dimension d = mainView.imagePanel.getSize();
			int width =(int) d.getWidth();
			int height =(int) d.getHeight();
			System.out.println("wymiar okna:" + width + "x" +height);
			BufferedImage newImage = mainView.imagePanel.scaleImage(image, width, height, null);
			BufferedImage newImage2 = mainView.imagePanel.process(newImage, pData);
			ImageIcon icon = new ImageIcon(newImage2);
			mainView.imagePanel.setIcon(icon);

		}
	}

}
