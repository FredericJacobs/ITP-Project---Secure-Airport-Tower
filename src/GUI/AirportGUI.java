package GUI;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import messaging.*;

/**
 * This class represents the main window of the application. It mainly consists
 * of an AirportPanel, but also contains the two other windows of the
 * application (The journal and the list of downloaded files).
 */
public class AirportGUI extends JFrame {

	private static final long serialVersionUID = -6319538639673860639L;
	private DownloadGUI downloadGUI;
	private static JournalGUI journalGUI;
	public static Choker choker;

	public AirportGUI() {

		// Create a window. The program will exit when the window is closed.
		// See http://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
		super("Airport");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Create other windows for the journal and the list of downloaded files
		journalGUI = new JournalGUI();
		downloadGUI = new DownloadGUI();
		choker = new Choker();

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
		choker.setVisible(true);

		// this is how the GUI interfaces with the tower, to get the
		// journal and the list of downloaded files. 
		// Add here the observers to he appropriate classes

		// Run the tower
		new Thread(Tower.getInstance()).start();
	}

	/** This method starts a standalone GUI, for testing purposes. 
	 * @throws IOException */
	public static void main(String[] args) throws IOException {
		new AirportGUI();
	}

	public static JournalGUI getJournalGUI() {
		return journalGUI;
	}
}