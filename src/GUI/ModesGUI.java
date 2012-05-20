package GUI;

import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import messaging.Modes;
import messaging.Tower;

public class ModesGUI extends JFrame implements ActionListener, Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String CHRONOS = "CHRONOS";
	static String FUEL = "FUEL";
	static String TIME = "TIME";
	public Modes modes;
	JTextField textP = new JTextField("0");
	JTextField textC = new JTextField("0");
	JTextField textT = new JTextField("0");

	JLabel passageLabel = new JLabel("The total number of passagers");
	JLabel fuelLabel = new JLabel("The total number of fuel consumption");
	JLabel timeLabel = new JLabel("The waiting time of per passager");

	public ModesGUI() {
		JLabel board = new JLabel();
		board.setLayout(new GridLayout(3,2));
		board.add(passageLabel);
		textP.setSize(0, 80);
		board.add(textP);
		board.add(fuelLabel);
		board.add(textC);

		board.add(timeLabel);
		board.add(textT);

		this.setTitle("Modes");
		this.setLayout(new BorderLayout());
		JPanel mainPanel = (JPanel) this.getContentPane();
		mainPanel.setLayout(new GridLayout (2,1));
		mainPanel.add(board);
		// Create the buttons.
		JRadioButton chronosButton = new JRadioButton(CHRONOS);
		chronosButton.setSelected(true);
		JRadioButton fuelButton = new JRadioButton(FUEL);
		JRadioButton timeButton = new JRadioButton(TIME);

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(chronosButton);
		group.add(fuelButton);
		group.add(timeButton);

		// Register a listener for the radio buttons.
		chronosButton.addActionListener(this);
		fuelButton.addActionListener(this);
		timeButton.addActionListener(this);

		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(chronosButton);
		radioPanel.add(fuelButton);
		radioPanel.add(timeButton);

		mainPanel.add(radioPanel);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.setPreferredSize(new Dimension(500,300) );
		this.pack();
		this.setVisible(true);

	}

	// Listens to the radio buttons. */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(FUEL)) {
			Modes.reOrganiseFuel();
		} else if (e.getActionCommand().equals(TIME)) {
			Modes.reOrganiseTime();
		} else if (e.getActionCommand().equals(CHRONOS)) {
			Modes.reOrganiseChronos();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		 textP.setText(Tower.passgerNumber+" ");
		 textC.setText(Tower.consumption+" ");
		 textT.setText((double)Tower.landingTimeTotal/Tower.passgerNumber+" ");
		// TODO Auto-generated method stub
	}
}