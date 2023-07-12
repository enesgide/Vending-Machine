package vendingmachine;

import java.awt.event.*;
import javax.swing.*;

public class LoginView implements WindowListener {

	private Timer timer;

	private Model model;
	private Controller controller;

	private JFrame defaultPageViewJFrame;

	private JFrame jframe;
	private JPanel jpanel;

	private JLabel userNameLabel;
	private JTextField userNameTextField;

	private JLabel passwordLabel;
	private JPasswordField passwordField;

	private JButton registerButton;
	private JButton loginButton;


	// FINAL DATA
	private final int[] WINDOW_SIZE = {300, 200};

	private final int[] USER_NAME_LABEL_BP = {20, 30, 80, 32};
	private final int[] USER_NAME_TEXT_FEILD_BP = {90, 30, 190, 32};

	private final int[] PASSWORD_LABEL_BP = {20, 70, 80, 32};
	private final int[] PASSWORD_FEILD_BP = {90, 70, 190, 32};

	private final int[] REGISTER_BUTTON_BP = {15, 120, 120, 32};
	private final int[] LOGIN_BUTTON_BP = {160, 120, 120, 32};


	public LoginView(Model model, Controller controller, JFrame defaultPageViewJFrame, Timer timer) {

		this.timer  =timer;

		this.model = model;
		this.controller = controller;
		this.defaultPageViewJFrame = defaultPageViewJFrame;

		this.jframe = new JFrame("Login");
		this.jpanel = new JPanel();

		this.userNameLabel = new JLabel();
		this.userNameTextField = new JTextField();

		this.passwordLabel = new JLabel();
		this.passwordField = new JPasswordField();

		this.registerButton = new JButton();
		this.loginButton = new JButton();
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

		this.jframe.addWindowListener(this);

		this.jpanel.setLayout(null);
		this.jframe.add(this.jpanel);


		/**
		 * ===========================
		 * ### Label and TextFeild ###
		 * ===========================
		 * */
		// JLabel for user Name
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


		/**
		 * ==========================
		 * ### Register and Login ###
		 * ==========================
		 * */

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
				timer.restart();
				RegisterView registerView = new RegisterView(model, controller, defaultPageViewJFrame, jframe, timer);
				registerView.launchWindow();
			}
		});
		this.jpanel.add(this.registerButton);

		// JButton for confirm
		this.loginButton.setText("Confirm");
		this.loginButton.setBounds(
				LOGIN_BUTTON_BP[0],
				LOGIN_BUTTON_BP[1],
				LOGIN_BUTTON_BP[2],
				LOGIN_BUTTON_BP[3]
			);
		this.loginButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				String userName = userNameTextField.getText();
				String password = passwordField.getText();
				if (controller.ifMatchUserInDB(userName, password)){
					controller.setCurrentUser(userName);
					controller.updateAfterLogin();
					jframe.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Invalid user name or password.");
				}
			}
		});
		this.jpanel.add(this.loginButton);

		// Show window
		this.jframe.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {
		this.timer.stop();
		this.defaultPageViewJFrame.dispose();
		this.controller.setDefaultPageView(null);

		// Normal user or Anonymous
		if (this.model.getCurrentUser().getName() == null || this.model.getCurrentUser().getType().equals(UserType.NORMAL)) {
			DefaultPageView defaultPageView = new DefaultPageView();
			this.controller.setDefaultPageView(defaultPageView);
			defaultPageView.setModel(this.model);
			defaultPageView.setController(this.controller);
			defaultPageView.launchWindow();

		// Seller
		} else if (this.model.getCurrentUser().getType().equals(UserType.SELLER)) {
			SellerView sellerView = new SellerView();
			this.controller.setSellerView(sellerView);
			sellerView.setModel(this.model);
			sellerView.setController(this.controller);
			sellerView.launchWindow();

		// Cashier
		} else if (this.model.getCurrentUser().getType().equals(UserType.CASHIER)) {
			CashierView cashierView = new CashierView();
			this.controller.setCashierView(cashierView);
			cashierView.setModel(this.model);
			cashierView.setController(this.controller);
			cashierView.launchWindow();

		// Owner
		} else if (this.model.getCurrentUser().getType().equals(UserType.OWNER)) {
			OwnerView ownerView = new OwnerView();
			this.controller.setOwnerView(ownerView);
			ownerView.setModel(this.model);
			ownerView.setController(this.controller);
			ownerView.launchWindow();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
}

