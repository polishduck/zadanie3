package app.controller;

import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;


import app.view.*;
import app.model.*;

public class mainController implements ActionListener, TreeSelectionListener {
	
	private mainView myView;
	public File[] files;
	public DicomInputStream dis = null;
	public DicomObject dcm = null;
	public JFileChooser fc = null;

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
				//		System.out.println( tag );
						}
					
					//showDetails(dcm);
					
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
		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()) {
			int a = clicked_index(mainView.tree, node);
			System.out.println("click na itemie:" +  a);
			BufferedImage image = null;
			try {
				image = ImageIO.read(new File(files[a].getAbsolutePath()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.print("Opened file " + files[a].getAbsolutePath() + "\n");
			
			try {
				dis = new DicomInputStream(new File(files[a].getAbsolutePath()));
				dcm = dis.readDicomObject();
			} catch (IOException e1) {
				// ex
			} finally {
				if (dis != null) {
					CloseUtils.safeClose(dis);
				}
				
			}
			
			//showDetails(dcm);
			
			//image.setSize(imagePanel.getSize());
			Dimension d = mainView.imagePanel.getSize();
			int width =(int) d.getWidth();
			int height =(int) d.getHeight();
			System.out.println("wymiar okna:" + width + "x" +height);
			BufferedImage newImage = mainView.imagePanel.scaleImage(image, width, height, null);
			ImageIcon icon = new ImageIcon(newImage);
			
		//	ImageIcon icon = new ImageIcon(image);
			mainView.imagePanel.setIcon(icon);
			
		}
	}

	private int clicked_index(JTree tree, DefaultMutableTreeNode node) {
		   TreeNode root = (TreeNode) tree.getModel().getRoot();
		    if (node == root) {
		        return 0;
		    }
		    TreeNode parent = node.getParent();
		    if (parent == null) {
		        return -1;
		    }
		    int parentIndex= clicked_index(tree, (DefaultMutableTreeNode)parent);
		    if (parentIndex == 0) {
		        return -1;
		    }
		    return parent.getIndex(node);
	}
}
