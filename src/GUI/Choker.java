package GUI;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import messaging.Tower;
import messaging.messages.ChokeMessage;

public class Choker extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private JLabel imageLabel;
	private JPanel panel;
	ImageIcon chokeButton;
	ImageIcon unChokeButton;
	boolean status = false;
	Timer timer = new Timer();
	public Choker() throws IOException {
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

	private void chokeEnabled(boolean status) throws IOException {
		if (status) {
			imageLabel.removeAll();
			imageLabel.setIcon(unChokeButton);
			for (int i = 0; i < Tower.planeCounter; i++) {
				Socket socket = Tower.planes[i].getSocket();
				DataOutputStream outData = new DataOutputStream(
						socket.getOutputStream());
				ChokeMessage chock = new ChokeMessage("Tour0000".getBytes(), 0,
						0, 0);
				chock.write(outData);
			}
			timer.schedule(new Counter(), 10000);
		} else {
			if(status == false){
			imageLabel.removeAll();
			imageLabel.setIcon(chokeButton);}
	}}
	class Counter extends TimerTask {
		public void run() {
			status = false;
			System.out.println("Choke finish");
			timer.cancel();
			timer = new Timer();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		status = !status;
		try {
			chokeEnabled(status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
