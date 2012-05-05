package GUI;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class Choker extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private JLabel imageLabel;
	private JPanel panel;
	ImageIcon chokeButton; 
    ImageIcon unChokeButton;
    boolean status = false;
	
	public Choker() {
		chokeButton = new ImageIcon("src"+ File.separator +"GUI"+ File.separator +"img"+ File.separator + "Choke_Button.png");
		unChokeButton = new ImageIcon("src"+File.separator +"GUI"+ File.separator+ "img"+ File.separator + "UnChoke_Button.png");
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
	
	public void updateChoker(){
		//Getting Choke Events from the tower	
	}
	
	private void chokeEnabled(boolean status){
		if (status)
		{
			imageLabel.removeAll();
			imageLabel.setIcon(unChokeButton);
		}
		else
		{
			imageLabel.removeAll();
			imageLabel.setIcon(chokeButton);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		status = !status;
		chokeEnabled(status);
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
