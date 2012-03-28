package GUI;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



@SuppressWarnings("serial")
public class Radar extends JFrame {
	private ScanningBar scanner;
	public Radar (String arg){	
	this.setName("Secure Airport Tower");
	if (arg == null ) {
	        arg = "src/RadarScreen.png";
	    }
	
	JPanel panel = new JPanel(); 
	LayoutManager overlay = new OverlayLayout(panel);
    panel.setLayout(overlay);
    ImageIcon icon = new ImageIcon(arg); 
    JLabel label = new JLabel(); 
    label.setIcon(icon); 
    scanner = new ScanningBar();
    scanner.start();
    panel.add(scanner, BorderLayout.CENTER);
    panel.add(label);
    this.getContentPane().add(panel);
    this.pack();
	this.setResizable(false);
    
	}
	 
}

@SuppressWarnings("serial")
class ScanningBar extends JComponent {
    
    private static final double TWO_PI   = 2.0 * Math.PI;
    
    private static final int    UPDATE_INTERVAL = 80;  
    
    
    private Calendar _now = Calendar.getInstance(); 
    
    private int _diameter;                 
    private int _centerX;                 
    private int _centerY;                  
      
    
    private javax.swing.Timer _timer;     
    
    
    public ScanningBar() {
        _timer = new javax.swing.Timer(UPDATE_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTime();
                repaint();
            }
        });
    }
    
    /** Start the timer. */
    public void start() {
        _timer.start(); 
    }
   
    /** Stop the timer. */
    public void stop() {
        _timer.stop(); 
    }

    private void updateTime() {
        _now.setTimeInMillis(System.currentTimeMillis());
    }
    

    @Override public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        //... The panel may have been resized, get current dimension
        _diameter = 715;
        _centerX = 500;
        _centerY = 492;
        
        //... Create the clock face background image if this is the first time,
        //    or if the size of the panel has changed
        

        //... Draw the clock hands dynamically each time.
        drawRadarLine(g2);
    }

    private void drawRadarLine(Graphics2D g2) {
        //... Get the various time elements from the Calendar object.
        int seconds = _now.get(Calendar.SECOND);
        int millis  = _now.get(Calendar.MILLISECOND);


        int handMax = _diameter / 2;    // Second hand extends to outer rim.
        double fseconds = (seconds + (double)millis/1000) / 60.0;
        drawRadius(g2, fseconds, 0, handMax);
        
    }
    

    private void drawRadius(Graphics2D g2, double percent,
                            int minRadius, int maxRadius) {
        //... percent parameter is the fraction (0.0 - 1.0) of the way
        //    clockwise from 12.   Because the Graphics2D methods use radians
        //    counterclockwise from 3, a little conversion is necessary.
        //    It took a little experimentation to get this right.
        double radians = (0.5 - percent) * TWO_PI;
        double sine   = Math.sin(radians);
        double cosine = Math.cos(radians);
        
        int dxmin = _centerX + (int)(minRadius * sine);
        int dymin = _centerY + (int)(minRadius * cosine);
        
        int dxmax = _centerX + (int)(maxRadius * sine);
        int dymax = _centerY + (int)(maxRadius * cosine);
        g2.drawLine(dxmin, dymin, dxmax, dymax);
    }
}
