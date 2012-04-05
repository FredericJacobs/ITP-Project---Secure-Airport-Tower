package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 * This class displays the Tower's Journal, i.e. a list of received messages.
 * 
 * It should be self-contained. You just have make it an observer and feed it
 * the right data (see the addEvent method)
 * 
 */
public class JournalGUI extends JFrame implements ActionListener /*, Observer*/ {

	private static final long serialVersionUID = -8316495054463238988L;
	private static final String[] columnNames = {"Priority", "Type", "Source", "Destination", "Date"};

    private static final Color[] PRIORITY_BACKCOLOR = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE, Color.CYAN};
    private static final Color[] PRIORITY_FORECOLOR = new Color[]{Color.WHITE, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK};
    
	private TableRowSorter<DefaultTableModel> sorter;
	private CustomTableModel model;
	private JTable table;
	private JButton btnClose, btnFilter;
	private Container container;
	private JTextField tfFilter;
	private JComboBox cbFilter;

	public JournalGUI() {
		init();
	}

	public void init() {
		setTitle("Event Journal");
		setLocationRelativeTo(null);
		setSize(new Dimension(700,400));
		setResizable(true);
		setLayout(new BorderLayout());
		this.container = getContentPane();
		buildButtons();
		buildTable();
		setMinimumSize(new Dimension(550, 370));
	}

	public void clearAll() {
		table.removeAll();
		repaint();
	}

	public void buildButtons() {
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));

		JLabel lblFilter = new JLabel("Filter by ");
		cbFilter = new JComboBox(new String[]{"Priority", "Type", "Source", "Destination"});
		tfFilter = new JTextField();
		tfFilter.setMinimumSize(new Dimension(80, tfFilter.getHeight()));
		tfFilter.setPreferredSize(tfFilter.getMinimumSize());

		btnFilter = new JButton("Filter");
		btnFilter.addActionListener(this);

		pan.add(Box.createHorizontalStrut(5));
		pan.add(lblFilter);
		pan.add(Box.createHorizontalStrut(5));
		pan.add(cbFilter);
		pan.add(Box.createHorizontalStrut(5));
		pan.add(tfFilter);
		pan.add(Box.createHorizontalStrut(5));
		pan.add(btnFilter);
		pan.add(Box.createHorizontalStrut(5));

		btnClose = new JButton("Close");
		btnClose.addActionListener(this);

		pan.add(Box.createHorizontalGlue());
		pan.add(Box.createHorizontalStrut(5));
		pan.add(btnClose);
		pan.add(Box.createHorizontalStrut(5));

		container.add(pan, BorderLayout.SOUTH);
	}

	/** This method expects to receive a vector with the following content:
	 * 	int priority          the priority of the message forming this event
	 *  String type           the type of message (e.g. DATA)
	 *  String source         the message source (e.g. "Tower" or a plane id
	 *  String destination    the message destination (same format as source)
	 *  java.util.Date data   the date and time when the message arrived
     */
	@SuppressWarnings("rawtypes")
	public void addEvent(Vector v) {
		model.addRow(v);
	}

	private void newFilter(String text) {
		RowFilter<DefaultTableModel, Object> rf = null;
		//If current expression doesn't parse, don't update.
		try {
			rf = RowFilter.regexFilter(text, cbFilter.getSelectedIndex());
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorter.setRowFilter(rf);
	}


	private void buildTable() {
		model = new CustomTableModel();

		for (String header : columnNames) {
			model.addColumn(header);
		}

		table = new JTable(model);

		// Color the lines
		for (Enumeration<TableColumn> e = table.getColumnModel().getColumns() ; e.hasMoreElements() ;) {
			e.nextElement().setCellRenderer(new CustomTableCellRenderer());
		}

		sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);

		table.getTableHeader().setReorderingAllowed(false);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(table);
		container.add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == btnClose) {
			setVisible(false);
		} else if (obj == btnFilter) {
			newFilter(tfFilter.getText());
		} 
	}

	private class CustomTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value instanceof Date) {
				setText(DateFormat.getDateTimeInstance(
						DateFormat.SHORT, DateFormat.SHORT).format(value));
			}

			Object val = table.getValueAt(row, 0);
			if (val instanceof Integer) {
				int priority = (Integer) val;
				cell.setBackground(PRIORITY_BACKCOLOR[priority]);
				cell.setForeground(PRIORITY_FORECOLOR[priority]);
			}

			return cell;
		}

	}

	private class CustomTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

	}

	/** MODIFY ME: This is how the JournalGUI expects new events to be delivered from the Tower. */

}
