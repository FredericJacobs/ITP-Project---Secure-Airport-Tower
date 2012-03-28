package GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import messaging.Tour;

/**
 * This class represents the main window of the application. It mainly consists
 * of an AirportPanel, but also contains the two other windows of the
 * application (The journal and the list of downloaded files).
 */
public class AirportGUI extends JFrame {

	private static final long serialVersionUID = -6319538639673860639L;
	private DownloadGUI downloadGUI;
	private JournalGUI journalGUI;
	
	public AirportGUI() {
		
		// Create a window. The program will exit when the window is closed.
		// See http://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
		super("Airport");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Create other windows for the journal and the list of downloaded files
		journalGUI = new JournalGUI();
		downloadGUI = new DownloadGUI();
		
		// Create an airport panel, add it to this window
		setLayout(new BorderLayout());
		AirportPanel airport = new AirportPanel();
		getContentPane().add(airport, BorderLayout.CENTER);
		setSize(airport.getBackgroundDimension());
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		journalGUI.setVisible(true);
		downloadGUI.setVisible(true);

		// TODO: this is how the GUI interfaces with the tower, to get the
		// journal and the list of downloaded files. 
		// Add here the observers to he appropriate classes
		
		// Run the tower
		new Thread(Tour.getInstance()).start();
	}
	
	/** This method starts a standalone GUI, for testing purposes. */
	public static void main(String[] args) {
		new AirportGUI();
	}

}
