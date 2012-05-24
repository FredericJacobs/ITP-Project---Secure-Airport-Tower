package GUI;

import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import messaging.Event;
import messaging.Tower;
import messaging.messages.ChokeMessage;
import messaging.messages.UnchokeMessage;

/**
 * 
 * This class has the function and the GUI of the Choker and Unchoker. Each time the choke button was clicked,
 * the unchoke function will be blocked for 10 minutes, by using the timer. And when we get a mayday message, it
 * will call the chokeEnabled method to active the choke function
 * @author Hantao Zhao 
 * @author Frederic Jacobs
 * @version 1.0
 */

public class Choker extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private JLabel imageLabel;
	private JPanel panel;
	ImageIcon chokeButton;
	ImageIcon unChokeButton;
	boolean status = false;
	boolean choking = false;
	Timer timer = new Timer();
	//The GUI of choker, it has two buttons- choke and unchoke
	public Choker() {
		chokeButton = new ImageIcon("src" + File.separator + "GUI"
				+ File.separator + "img" + File.separator + "Choke_Button.png");
		unChokeButton = new ImageIcon("src" + File.separator + "GUI"
				+ File.separator + "img" + File.separator
				+ "UnChoke_Button.png");
		this.setName("Choker");
		panel = new JPanel();
		LayoutManager overlay = new OverlayLayout(panel);
		panel.setLayout(overlay);
		imageLabel = new JLabel();
		imageLabel.setIcon(chokeButton);
		imageLabel.addMouseListener(this);
		panel.add(imageLabel);
		this.getContentPane().add(panel);
		this.pack();
		this.setResizable(false);
		chokeEnabled(status);
	}

	public void updateChoker() {
		// Getting Choke Events from the tower
	}
	// The functional method to active the choke method. It will send a choke method to all the planes that is saved 
	// in the tower. At the same time run the time counter and change the icon.
	public void chokeEnabled(boolean status)  {
		// If the choke is not active
		if (status) {
			imageLabel.removeAll();
			imageLabel.setIcon(unChokeButton);
			for (int i = 0; i < Tower.getInstance().getPlanes().size(); i++) {
				Socket socket = Tower.getInstance().getPlanes().get(i).getSocket();
				DataOutputStream outData;
				try {
					outData = new DataOutputStream(
							socket.getOutputStream());

				ChokeMessage chock = new ChokeMessage("Tower".getBytes(), 0,
						0, 0);
				chock.write(outData);
				Event eventR = new Event(chock, "Tower",
						"Allplanes");
				Tower.getInstance().getJournal().addEvent(eventR);
				} catch (IOException e) {
					// 
					e.printStackTrace();
				}
			}
			// Block the chock function for 10 minutes
			timer.schedule(new Counter(), 600000);
		}
		// If the choke is already active
		else {

			for (int i = 0; i < Tower.getInstance().getPlanes().size(); i++) {
				Socket socket = Tower.getInstance().getPlanes().get(i).getSocket();
				DataOutputStream outData;
				try {
					outData = new DataOutputStream(
							socket.getOutputStream());

				UnchokeMessage unchock = new UnchokeMessage("Tower".getBytes(), 0,
						0, 0);
				unchock.write(outData);
				Event eventR = new Event(unchock, "Tower",
						"Allplanes");
				Tower.getInstance().getJournal().addEvent(eventR);} catch (IOException e) {
					e.printStackTrace();
				}
			}
			choking = false;
			imageLabel.removeAll();
			imageLabel.setIcon(chokeButton);}
	}
	// The class which extends the TimerTask, it can arrange the time counter and run the method after a specified time
	class Counter extends TimerTask {
		public void run() {
			System.out.println("Choke finish");
			timer.cancel();
			timer = new Timer();
			choking = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(!choking){
		status = !status;
		choking = true;
		chokeEnabled(status);}else{
			System.out.println("choking! block");
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

}