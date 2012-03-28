package GUI

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import messaging.*;
import GUI.CircularBuffer;

/**
 * This class draws an airport and the planes. It makes the following assumption
 * about the rest of the code. Please modify this class or your code accordingly;
 * use your good judgment to produce the most elegant code you can :)
 * 
 * - It uses the method public static Tower getInstance() of the Tower class to
 *   retrieve the tower singleton object. -
 * - It uses the method public ArrayList<TowerAgent> getPlanes() of the Tower class
 *   to retrieve a list of planes that are currently connected to the Tower
 * - It uses the methods public int getPosX() and public int getPosY() of the
 *   TowerAgent class to retrieve the position of the airplanes.
 *   This position is expected to satisfy 0 <= x <= 1109 and 0 <= y <= 751 if the
 *   airplane should be displayed.
 * - It considers a plane crashed when the hasCrashed() method of the class
 *   TowerAgent returns true.
 */
public class AirportPanel extends JPanel {

	private static final long serialVersionUID = 5254715425216686775L;
	
	// The various images used in the GUI
	private BufferedImage imgBack;
	private BufferedImage imgPlane;
	private BufferedImage imgTower;
	private BufferedImage imgKboom;
	private BufferedImage imgRadar;
	
	// The position of the tower in the background image
	private static final int TOWER_X = 625;
	private static final int TOWER_Y = 445;
	
	// The time it takes the radar beam to make a full circle, in milliseconds
	private static final int RADAR_PERIOD = 5000;
	
	// How long should aircraft trails be (in milliseconds)?
	private static final int AIRCRAFT_TRAIL_DURATION = 5000;
	
	// Desired framerate (in frames per second)
	public static final int AIRPORT_PANEL_FRAMERATE = 10;
	
	// This stores the previous positions of aircrafts, so we can draw nice trails
	private HashMap<String, CircularBuffer<Point> > previousPositions = new HashMap<String, CircularBuffer<Point> >();

	public AirportPanel() {
		setLayout(null);

		try {
			this.imgBack  = ImageIO.read(new File("img/map.png"));
			this.imgPlane = ImageIO.read(new File("img/plane.png"));
			this.imgTower = ImageIO.read(new File("img/tower.png"));
			this.imgKboom = ImageIO.read(new File("img/kboom.png"));
			this.imgRadar = ImageIO.read(new File("img/radar.png"));
		} catch (IOException e) {
			System.err.println("Cannot read image files: " + e.getMessage());
			System.exit(1);
		}

		// Repaints the user interface regularly
		// Be careful, don't set a framerate too high
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				repaint();
			}
		}, 0, 1000 / AIRPORT_PANEL_FRAMERATE);
	}

	public Dimension getBackgroundDimension() {
		return new Dimension(imgBack.getWidth(), imgBack.getHeight());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(imgBack, 0,0, imgBack.getWidth(), imgBack.getHeight(), null);
		g.drawImage(imgTower,
				TOWER_X - imgTower.getWidth() / 2,
				TOWER_Y - imgTower.getHeight() / 2,
				imgTower.getWidth(),
				imgTower.getHeight(), null);

		// Draw the circles around the tower
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GREEN);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2d.setStroke(new BasicStroke(3F));
		for (int i = 0; i < 10; i++) {
			g2d.drawOval(TOWER_X - i * 75, TOWER_Y - i * 75, i * 150, i * 150);
		}

		// Draw the animated radar beam
		long currentTime = System.currentTimeMillis();
		double radarAngle = 2 * Math.PI * currentTime / RADAR_PERIOD;
		g2d.setTransform(AffineTransform.getRotateInstance(radarAngle, TOWER_X, TOWER_Y));
		g2d.drawImage(imgRadar, TOWER_X, TOWER_Y, imgRadar.getWidth(), imgRadar.getHeight(), null);
		g2d.setTransform(AffineTransform.getRotateInstance(0));

		// Store the current position of every plane in the previousPositions buffer
		for (TowerAgent plane: Tower.getInstance().getPlanes()) {
			Point p = new Point(plane.getPosX(), plane.getPosY());
			String planeId = new String(plane.getId());
			CircularBuffer<Point> cb = previousPositions.get(planeId);
			if (cb != null) {
				cb.add(p);
			} else {
				cb = new CircularBuffer<Point>(AIRCRAFT_TRAIL_DURATION * AIRPORT_PANEL_FRAMERATE / 1000);
				cb.add(p);
				cb.add(p); // Add it twice, so we have at least one line segment
				previousPositions.put(planeId, cb);
			}
		}
		
		// Draw trails so we can see the route that the planes have taken
		g2d.setColor(Color.CYAN);
		for (TowerAgent plane: Tower.getInstance().getPlanes()) {
			String planeId = new String(plane.getId());
			CircularBuffer<Point> previousPos = previousPositions.get(planeId);
			for (int j = 1; j < previousPos.size(); j++) {
				Point p1 = previousPos.get(j);
				Point p2 = previousPos.get(j - 1);
				if (p2.getX() >= 0 && p2.getY() >= 0)
					g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}

		// Draw the planes themselves
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2d.setColor(Color.GREEN);
		for (TowerAgent plane: Tour.getInstance().getPlanes()) {
			String planeId = new String(plane.getId());
			BufferedImage planeImg = plane.hasCrashed() ? imgKboom : imgPlane; 

			g2d.setTransform(AffineTransform.getRotateInstance(0, plane.getPosX(), plane.getPosY()));
			g2d.drawString(planeId + " (" + plane.getPosX() + ", " + plane.getPosY() + ")",
					plane.getPosX() + planeImg.getWidth() / 2, plane.getPosY());

			// Compute the rotation angle of the plane, and draw it
			CircularBuffer<Point> previousPos = previousPositions.get(planeId);
			double dx = previousPos.get(previousPos.size() - 1).getX() - previousPos.get(previousPos.size() - 2).getX();
			double dy = previousPos.get(previousPos.size() - 1).getY() - previousPos.get(previousPos.size() - 2).getY();
			double theta = Math.atan2(dy, dx);
			g2d.setTransform(AffineTransform.getRotateInstance(theta, plane.getPosX(), plane.getPosY()));
			g2d.drawImage(
					planeImg,
					plane.getPosX() - planeImg.getWidth() / 2,
					plane.getPosY() - planeImg.getHeight() / 2,
					null);
		}
	}
}




