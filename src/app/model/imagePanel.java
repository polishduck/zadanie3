package app.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;

import sun.util.resources.CalendarData;

@SuppressWarnings("serial")
public class imagePanel extends JLabel{
	
	public imagePanel() {
		Dimension minimumSize = new Dimension(100, 50);
	    setMinimumSize(minimumSize);
	
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
	
    public BufferedImage process(BufferedImage old, patientData pData) {
        int w = old.getWidth();
        int h = old.getHeight();
        
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(Color.white);
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        
        String full_name = pData.getFullName();
        Calendar date = pData.getBirthDate();
        String date_s;
        if (date == null)
        	date_s = "0000-00-00";
        else
        	date_s = date.get(Calendar.DAY_OF_MONTH) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.YEAR);
        String mod = pData.getModality();
        
        FontMetrics fm = g2d.getFontMetrics();
     //   int x = img.getWidth() - fm.stringWidth(s) - 5;
        int x = 40;
        int y = fm.getHeight();
        g2d.drawString(full_name, x, y);
        g2d.drawString(date_s, x, y+20);
        g2d.drawString(mod, x, y+40);
        g2d.dispose();
        return img;
    }
}


