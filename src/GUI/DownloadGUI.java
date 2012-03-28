package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileSystemView;

/**
 * Graphical interface to show the files that have been sent by the planes.
 * @author Jonathan Cheseaux, Alexis Kessel, Maud Chami
 *
 */
public class DownloadGUI extends JFrame implements ActionListener /* , Observer */ {

	private static final long serialVersionUID = -2822355651114549873L;
	private JPanel titlePanel, panEmpty;
	private JScrollPane scroll;
	private JButton btnOpen, btnQuit;

	private static GridBagLayout gbTrace = new GridBagLayout();

	public DownloadGUI() {
		buildComponents();
		this.setLayout(gbTrace);
		setSize(300,200);
		
		// Ensure the downloads directory exists
		File downloads = new File("downloads");
		downloads.mkdir();
	}

	/**
	 * Constructs the graphical elements of this window.
	 */
	public void buildComponents() {
		buildFileList();
		buildBottomBtn();
	}

	private void buildFileList() {
		titlePanel = new JPanel();
		titlePanel.setBorder(BorderFactory.createTitledBorder("Received files"));
		titlePanel.setLayout(gbTrace);

		addComponent(this, titlePanel, 1, 1, GridBagConstraints.BOTH);

		panEmpty = new JPanel();
		panEmpty.setLayout(new BoxLayout(panEmpty, BoxLayout.Y_AXIS));

		JPanel borderPanel = new JPanel();
		borderPanel.setLayout(new BorderLayout());
		borderPanel.add(panEmpty, BorderLayout.NORTH);

		scroll = new JScrollPane(borderPanel);

		addComponent(titlePanel, scroll, 1, 1, GridBagConstraints.BOTH);
	}

	/**
	 * This is a helper method that adds a component to a GridBagLayout.
	 * @param container     the container to which we add the component
	 * @param component     the component to add
	 * @param weightx       the horizontal weight
	 * @param weighty       the vertical weight
	 * @param fill          how the component behaves when the window is resized
	 */
	private void addComponent(
			Container container,
			Component component,
			int weightx, int weighty,
			int fill) {
		GridBagConstraints gbConstraints  = new GridBagConstraints();
		gbConstraints.weightx = weightx;
		gbConstraints.weighty = weighty;
		gbConstraints.fill = fill;
		gbConstraints.gridx = 0;
		gbConstraints.gridy = GridBagConstraints.RELATIVE;
		gbConstraints.gridwidth = 1;
		gbConstraints.gridheight = 1;
		gbConstraints.insets = new Insets(5, 5, 5, 5);
		
		gbTrace.setConstraints( component, gbConstraints );
		container.add( component );
	}

	/**
	 * Returns the icon associated with the given filename
	 * @param fileName the name of the file
	 * @return the icon corresponding to the file type
	 */
	private Icon getExtFileIcon(File file) {
		// getSystemIcon only works for existing files... hence, create a
		// (temporary) file with the given extension
		try {
			File temp = File.createTempFile("temp", file.getName());
			Icon result = FileSystemView.getFileSystemView().getSystemIcon(file);
			temp.delete();
			return result;
		} catch (IOException e) {
			System.err.println("Warning: could not create temporary file.");
			return null;
		}
	}


	/**
	 * This method fills the list of files with the files given in fileList
	 * @param fileList the list of received files
	 */
	public synchronized void addFilesToDownloadBox(ArrayList<File> fileList) {
		// Whether the current row should have a different background color or
		// not. Used to color every other row.
		boolean colorChange = false;
		
		panEmpty.removeAll();

		for (int i = 0; i < fileList.size(); i++) {	
			File bFile = fileList.get(i);

			JPanel panIcon = new JPanel();
			panIcon.setOpaque(false);
			panIcon.setLayout(new BoxLayout(panIcon, BoxLayout.X_AXIS));
			JLabel image = new JLabel(getExtFileIcon(bFile));
			image.setOpaque(false);
			panIcon.add(image);
			panIcon.add(Box.createHorizontalStrut(5));

			JPanel rowDownload = new JPanel();
			rowDownload.setLayout(new BoxLayout(rowDownload, BoxLayout.X_AXIS));

			JPanel rowFileName = new JPanel();
			rowFileName.setOpaque(false);
			rowFileName.setLayout(new BoxLayout(rowFileName, BoxLayout.X_AXIS));

			JLabel lblName = new JLabel(bFile.getName());
			lblName.setFont(new Font("Arial", Font.BOLD, 11));
			lblName.setOpaque(false);

			rowFileName.add(lblName);
			rowFileName.add(Box.createHorizontalGlue());

			rowDownload.add(panIcon);
			rowDownload.add(Box.createHorizontalStrut(5));
			rowDownload.add(rowFileName);
			rowDownload.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

			Color color = (colorChange ? new Color(0xd0d0d0) : Color.WHITE);
			rowDownload.setBackground(color);
			colorChange = !colorChange;

			JPanel panBorder = new JPanel();
			panBorder.setLayout(new BoxLayout(panBorder, BoxLayout.X_AXIS));
			panBorder.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
			panBorder.add(rowDownload);

			panEmpty.add(panBorder);
		}
		
		// Repaint the window because its components changed
		validate();
	}

	/**
	 * Creates the buttons at the bottom of this window
	 */
	private void buildBottomBtn() {
		JPanel rowButtons = new JPanel();
		rowButtons.setLayout(new BoxLayout(rowButtons, BoxLayout.X_AXIS));

		btnOpen = new JButton("Open Folder");
		btnOpen.addActionListener(this);

		btnQuit = new JButton("Quit", new ImageIcon("resources/exit_icon.gif"));
		btnQuit.addActionListener(this);

		rowButtons.add(Box.createHorizontalGlue());
		rowButtons.add(btnOpen);
		rowButtons.add(Box.createHorizontalStrut(5));
		rowButtons.add(btnQuit);
		rowButtons.add(Box.createHorizontalStrut(5));      

		addComponent(this, rowButtons , 1, 0,
				GridBagConstraints.HORIZONTAL);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == btnOpen) {
			try {
				java.awt.Desktop.getDesktop().open(new File("downloads"));
			} catch (IOException e1) {
				System.err.println("Could not open downloads folder: " + e1.getMessage());
			}
		} else if (obj == btnQuit) {
			dispose();
			System.exit(0);
		}
	}

	/** TODO: Do not forget to notify the DownloadGUI about new files */

}
