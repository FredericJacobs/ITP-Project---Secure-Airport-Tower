package GUI;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JButton;

public class Test extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private Timer timer;

	private char sp[] = new char[100];
	private String text = "Hello"; // @jve:decl-index=0:

	private JTextField jTextField = null;

	private JSpinner spinner = null;

	private JPanel jPanel = null;

	private JButton jButton = null;

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			String str = jTextField.getText();
			str = str.substring(1) + str.substring(0, 1);
			jTextField.setText(str);
		}
		if (e.getSource() == jButton) {
			String str = JOptionPane.showInputDialog(this, "请输入要设置的滚动字：");
			if (str != null) {
				text = str;
				jTextField.setText(new String(sp) + text);
			}
		}

	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			java.util.Arrays.fill(sp, ' ');
			jTextField.setText(new String(sp) + text);
			jTextField.setEditable(false);

		}
		return jTextField;
	}

	private JSpinner getJSpinner() {
		if (spinner == null) {
			spinner = new JSpinner();
			spinner.setValue(100);
			spinner.setName("spinner");
			spinner.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					timer.setDelay(Integer.parseInt("" + spinner.getValue()));

				}
			});

		}
		return spinner;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			jPanel.add(getJButton(), null);
			jPanel.add(getJSpinner(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton("设置文字");
			jButton.addActionListener(this);

		}
		return jButton;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Test thisClass = new Test();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);

			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public Test() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setBounds(300, 240, 345, 100);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle("滚动字");
		this.setVisible(false);
		timer = new Timer(100, this);
		timer.start();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout = new GridLayout(2, 1);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout);
			jContentPane.add(getJTextField(), null);
			jContentPane.add(getJPanel(), null);
		}
		return jContentPane;
	}
}