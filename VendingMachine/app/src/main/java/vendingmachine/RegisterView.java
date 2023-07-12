package vendingmachine;

import java.awt.event.*;
import javax.swing.*;

public class RegisterView {

	private Timer timer;

	private Model model;
	private Controller controller;

	private JFrame defaultPageViewJFrame;
	private JFrame loginViewJFrame;

	private JFrame jframe;
	private JPanel jpanel;

	private JLabel userNameLabel;
	private JTextField userNameTextField;

	private JLabel passwordLabel;
	private JPasswordField passwordField;

	private JLabel reenterPasswordLabel;
	private JPasswordField reenterPasswordField;

	private JButton cancelButton;
	private JButton registerButton;


	// DATA
	private final int[] WINDOW_SIZE = {300, 240};

	private final int[] USER_NAME_LABEL_BP = {20, 30, 80, 32};
	private final int[] USER_NAME_TEXT_FEILD_BP = {90, 30, 190, 32};

	private final int[] PASSWORD_LABEL_BP = {20, 70, 80, 32};
	private final int[] PASSWORD_FEILD_BP = {90, 70, 190, 32};

	private final int[] PASSWORD_CONFIRM_LABEL_BP = {20, 110, 80, 32};
	private final int[] PASSWORD_CONFIRM_FEILD_BP = {90, 110, 190, 32};

	private final int[] CANCEL_BUTTON_BP = {15, 160, 120, 32};
	private final int[] REGISTER_BUTTON_BP = {160, 160, 120, 32};	



	public RegisterView(Model model, Controller controller, JFrame defaultPageViewJFrame, JFrame loginViewJFrame, Timer timer) {
	
		this.timer = timer;

		this.model = model;
		this.controller = controller;
		this.defaultPageViewJFrame = defaultPageViewJFrame;
		this.loginViewJFrame = loginViewJFrame;

		this.jframe = new JFrame("Register");
		this.jpanel = new JPanel();

		this.userNameLabel = new JLabel();
		this.userNameTextField = new JTextField();

		this.passwordLabel = new JLabel();
		this.passwordField = new JPasswordField();

		this.reenterPasswordLabel = new JLabel();
		this.reenterPasswordField = new JPasswordField();

		this.registerButton = new JButton();
		this.cancelButton = new JButton();

	}

	public void launchWindow() {

		/**
		 * ===================
		 * ### Basic Setup ###
		 * ===================
		 * */
		this.timer.restart();

		this.jframe.setSize(WINDOW_SIZE[0], WINDOW_SIZE[1]);;
		this.jframe.setResizable(false);
		this.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.jframe.setLocationRelativeTo(null);

		this.jpanel.setLayout(null);
		this.jframe.add(this.jpanel);

		/**
		 * ===========================
		 * ### Label and TextFeild ###
		 * ===========================
		 * */
		// JLabel for user name
		this.userNameLabel.setText("Username: ");
		this.userNameLabel.setBounds(
				USER_NAME_LABEL_BP[0],
				USER_NAME_LABEL_BP[1],
				USER_NAME_LABEL_BP[2],
				USER_NAME_LABEL_BP[3]
			);
		this.jpanel.add(this.userNameLabel);

		// JTextField for user name
		this.userNameTextField.setBounds(
				USER_NAME_TEXT_FEILD_BP[0],
				USER_NAME_TEXT_FEILD_BP[1],
				USER_NAME_TEXT_FEILD_BP[2],
				USER_NAME_TEXT_FEILD_BP[3]
			);
		this.jpanel.add(this.userNameTextField);

		// JLabel for password
		this.passwordLabel.setText("Password: ");
		this.passwordLabel.setBounds(
				PASSWORD_LABEL_BP[0],
				PASSWORD_LABEL_BP[1],
				PASSWORD_LABEL_BP[2],
				PASSWORD_LABEL_BP[3]
			);
		this.jpanel.add(this.passwordLabel);

		// JPasswordField for password
		this.passwordField.setText("");
		this.passwordField.setBounds(
				PASSWORD_FEILD_BP[0],
				PASSWORD_FEILD_BP[1],
				PASSWORD_FEILD_BP[2],
				PASSWORD_FEILD_BP[3]
			);
		this.jpanel.add(this.passwordField);

		// JLabel for re-entered password
		this.reenterPasswordLabel.setText("Re-enter: ");
		this.reenterPasswordLabel.setBounds(
				PASSWORD_CONFIRM_LABEL_BP[0],
				PASSWORD_CONFIRM_LABEL_BP[1],
				PASSWORD_CONFIRM_LABEL_BP[2],
				PASSWORD_CONFIRM_LABEL_BP[3]
			);
		this.jpanel.add(this.reenterPasswordLabel);

		// JPasswordField for re-entered password
		this.reenterPasswordField.setText("");
		this.reenterPasswordField.setBounds(
				PASSWORD_CONFIRM_FEILD_BP[0],
				PASSWORD_CONFIRM_FEILD_BP[1],
				PASSWORD_CONFIRM_FEILD_BP[2],
				PASSWORD_CONFIRM_FEILD_BP[3]
			);
		this.jpanel.add(this.reenterPasswordField);


		/**
		 * ===============
		 * ### Buttons ###
		 * ===============
		 * */
		// JButton for cancel
		this.cancelButton.setText("Cancel");
		this.cancelButton.setBounds(
				CANCEL_BUTTON_BP[0],
				CANCEL_BUTTON_BP[1],
				CANCEL_BUTTON_BP[2],
				CANCEL_BUTTON_BP[3]
			);
		this.cancelButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				jframe.dispose();
			}
		});
		this.jpanel.add(this.cancelButton);

		// JButton for register
		this.registerButton.setText("Register");
		this.registerButton.setBounds(
				REGISTER_BUTTON_BP[0],
				REGISTER_BUTTON_BP[1],
				REGISTER_BUTTON_BP[2],
				REGISTER_BUTTON_BP[3]
			);
		this.registerButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String userName = userNameTextField.getText();
				String password_1 = passwordField.getText();
				String password_2 = reenterPasswordField.getText();
				if (password_1.equals(password_2)) {
					if (controller.ifHasUserInDB(userName)) {
						JOptionPane.showMessageDialog(null, 
								"User with the same already exists.\nPlease try another."
							);
					} else {
						timer.restart();
						controller.register(userName, password_1); // add a new user into database
						controller.setCurrentUser(userName);
						controller.updateAfterLogin();
						defaultPageViewJFrame.dispose();
						loginViewJFrame.dispose();
						jframe.dispose();
					}
				} else {
					JOptionPane.showMessageDialog(null, 
							"The two passwords entered must be the same!"
						);
				}
			}
		});
		this.jpanel.add(this.registerButton);

		// Show window
		this.jframe.setVisible(true);
	}
}

