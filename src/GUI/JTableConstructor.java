package GUI;

import java.awt.*;
import java.util.*;
import javax.swing.*;

class JTableConstructor extends JFrame {
	public JTableConstructor() {
		setSize(400, 300);
		Vector vColumnNames = new Vector();
		vColumnNames.add("COLUMN1 ");
		vColumnNames.add("COLUMN2 ");
		vColumnNames.add("COLUMN3 ");

		Vector vRowData = new Vector();
		for (int i = 0; i < 10; i++) {
			Vector vOneRow = new Vector();
			vOneRow.add("COLUMN1Data " + Math.random());
			vOneRow.add("COLUMN2Data " + Math.random());
			vOneRow.add("COLUMN3Data " + Math.random());
			vRowData.add(vOneRow);
		}
		JTable table = new JTable(vRowData, vColumnNames);
		JScrollPane sp = new JScrollPane(table);
		getContentPane().add(sp, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JTableConstructor frame = new JTableConstructor();
		frame.validate();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setDefaultCloseOperation(3);
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
		// frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
}