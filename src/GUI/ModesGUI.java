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
	static String CHRONOS = "CHRONOS";
	static String FUEL = "FUEL";
	static String TIME = "TIME";
	public Modes modes;
	JTextField textF = new JTextField("0");
	JLabel passageLabel = new JLabel("The total number of passagers");
	JLabel fuelLabel = new JLabel("The total number of fuel consumption");
	JLabel timeLabel = new JLabel("The waiting time of per passager");

	public ModesGUI() {
		JLabel board = new JLabel();
		board.setLayout(new BorderLayout());
		board.add(passageLabel);
		board.add(fuelLabel);
		board.add(timeLabel);

		this.setName("Modes");
		this.setLayout(new BorderLayout());
		JPanel mainPanel = (JPanel) this.getContentPane();
		mainPanel.setLayout(new BorderLayout());
		textF.setSize(0, 20);
		mainPanel.add(passageLabel, BorderLayout.WEST);
		mainPanel.add(textF, BorderLayout.EAST);

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

		mainPanel.add(radioPanel, BorderLayout.SOUTH);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		// basePanel.setVisible(true);
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
		 textF.setText(Tower.passgerNumber+" ");
		// TODO Auto-generated method stub
	}
}