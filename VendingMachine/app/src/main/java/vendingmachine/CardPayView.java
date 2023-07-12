package vendingmachine;

import java.awt.event.*;
import javax.swing.*;

public class CardPayView {

	private Timer timer;

	private Model model;
	private Controller controller;

	private JFrame defaultPageViewJFrame;

	private JFrame jframe;
	private JPanel jpanel;

	private JLabel cardNameLabel;
	private JTextField cardNameTextField;

	private JLabel cardNumberLabel;
	private JPasswordField cardNumberField;

	private JButton backButton;
	private JButton confirmButton;
	private JButton cancelButton;


	// DATA
	private final int[] WINDOW_SIZE = {300, 200};

	private final int[] CARD_NAME_LABEL_BP = {20, 10, 80, 32};
	private final int[] CARD_NAME_TEXT_FEILD_BP = {90, 10, 190, 32};

	private final int[] CARD_NUMBER_LABEL_BP = {20, 50, 80, 32};
	private final int[] CARD_NUMBER_FEILD_BP = {90, 50, 190, 32};

	private final int[] BACK_BUTTON_BP = {20, 95, 120, 32};
	private final int[] CONFIRM_BUTTON_BP = {160, 95, 120, 32};	
	private final int[] CANCEL_BUTTON_BP = {20, 125, 260, 32};	


	public CardPayView(Model model, Controller controller, JFrame defaultPageViewJFrame, Timer timer) {
		this.timer = timer;

		this.model = model;
		this.controller = controller;
		this.controller.setCardPayView(this);
		this.defaultPageViewJFrame = defaultPageViewJFrame;

		this.jframe = new JFrame("Card Pay");
		this.jpanel = new JPanel();

		this.cardNameLabel = new JLabel();
		this.cardNameTextField = new JTextField();

		this.cardNumberLabel = new JLabel();
		this.cardNumberField = new JPasswordField();

		this.backButton = new JButton();
		this.confirmButton = new JButton();
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
		 * =================
		 * ### Card Name ###
		 * =================
		 * */
		// JLabel for card name
		this.cardNameLabel.setText("Name: ");
		this.cardNameLabel.setBounds(
				CARD_NAME_LABEL_BP[0],
				CARD_NAME_LABEL_BP[1],
				CARD_NAME_LABEL_BP[2],
				CARD_NAME_LABEL_BP[3]
			);
		this.jpanel.add(this.cardNameLabel);

		// JTextField for card name
		if (this.controller.ifCurrentUserHasCard()) {
			this.cardNameTextField.setText(this.model.getCurrentUser().getCardName());
		}
		this.cardNameTextField.setBounds(
				CARD_NAME_TEXT_FEILD_BP[0],
				CARD_NAME_TEXT_FEILD_BP[1],
				CARD_NAME_TEXT_FEILD_BP[2],
				CARD_NAME_TEXT_FEILD_BP[3]
			);
		this.jpanel.add(this.cardNameTextField);


		/**
		 * ===================
		 * ### Card Number ###
		 * ===================
		 * */
		// JLabel for card number
		this.cardNumberLabel.setText("Number: ");
		this.cardNumberLabel.setBounds(
				CARD_NUMBER_LABEL_BP[0],
				CARD_NUMBER_LABEL_BP[1],
				CARD_NUMBER_LABEL_BP[2],
				CARD_NUMBER_LABEL_BP[3]
			);
		this.jpanel.add(this.cardNumberLabel);

		// JPasswordField for card number
		if (this.controller.ifCurrentUserHasCard()) {
			this.cardNumberField.setText(this.model.getCardInfoMap().get(this.cardNameTextField.getText()));
		}
		this.cardNumberField.setBounds(
				CARD_NUMBER_FEILD_BP[0],
				CARD_NUMBER_FEILD_BP[1],
				CARD_NUMBER_FEILD_BP[2],
				CARD_NUMBER_FEILD_BP[3]
			);
		this.jpanel.add(this.cardNumberField);

		/**
		 * ===============
		 * ### Buttons ###
		 * ===============
		 * */
		
		// JButton for back
		this.backButton.setText("Back");
		this.backButton.setBounds(
				BACK_BUTTON_BP[0],
				BACK_BUTTON_BP[1],
				BACK_BUTTON_BP[2],
				BACK_BUTTON_BP[3]
			);
		this.backButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				// Close current window
				jframe.dispose();
				// Launch choose pay mathod
				launchChoosePaymentMethodWindow();			
			}
		});
		this.jpanel.add(this.backButton);

		// JButton for confirm
		this.confirmButton.setText("Confirm");
		this.confirmButton.setBounds(
				CONFIRM_BUTTON_BP[0],
				CONFIRM_BUTTON_BP[1],
				CONFIRM_BUTTON_BP[2],
				CONFIRM_BUTTON_BP[3]
			);
		this.confirmButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				// Retrieve data from current window
				String name = cardNameTextField.getText();
				String number = cardNumberField.getText();
				// Check data validity
				if (controller.ifHasCardGlobal(name)) {
					if (controller.ifMatchCardGlobal(name, number)) {
						if (controller.ifLoggedIn()) {
							if (!controller.ifCurrentUserHasCard(name)) {
								launchTryToSaveCardInfoWindow(name, number);
							}
						}
						controller.produceReportSuccessful(1);
						controller.confirmPay(1);
						JOptionPane.showMessageDialog(null, "Payment Successful!");
						restart();	
					} else {
						JOptionPane.showMessageDialog(null, "Invalid card name or number!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Invalid card name or number!");
				}
			}
		});
		this.jpanel.add(this.confirmButton);

		// JButton for cancel
		this.cancelButton.setText("Cancel Order");
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
				lanuchTryToCancelWindow();	
			}
		});
		this.jpanel.add(this.cancelButton);


		// Show window
		this.jframe.setVisible(true);
	}


	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	private void launchChoosePaymentMethodWindow() {
		Object[] options = {"Cash", "Card"};
		Object answer = JOptionPane.showOptionDialog(
				null, 
				"Choose way of paying: ",
				"Payment",
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, 
				options, 
				null
			);
		if (answer.equals(0)) {
			controller.resetCashPayData();
			CashPayView cashPayView = new CashPayView(model, controller, jframe, timer);
			cashPayView.launchWindow();
		} else if (answer.equals(1)){
			CardPayView cardPayView = new CardPayView(model, controller, jframe, timer);
			cardPayView.launchWindow();
		}
	}

	private void launchTryToSaveCardInfoWindow(String cardName, String cardNumber) {
		Object[] options = {"No", "Yes"};
		Object answer = JOptionPane.showOptionDialog(
				null, 
				"Do you want to save card info?", 
				"Warning", 
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.WARNING_MESSAGE, 
				null, 
				options, 
				options[0]
			);
		if (answer.equals(0)) {
		} else if (answer.equals(1)){
			controller.updateUserCardInfo(cardName);	
			JOptionPane.showMessageDialog(null, "Card info added successfully!");
		}
	}

	private void lanuchTryToCancelWindow() {
		Object[] options = {"No", "Yes"};
		Object answer = JOptionPane.showOptionDialog(
					null, 
					"Are you sure to cancel the order?", 
					"Warning", 
					JOptionPane.DEFAULT_OPTION, 
					JOptionPane.WARNING_MESSAGE, 
					null, 
					options, 
					options[0]
				);
		if (!answer.equals(0)) {
			String reason = (String)JOptionPane.showInputDialog("Cancel reason: ");
			controller.produceReportCancel(reason);
			restart();	
		}
	}

	private void restart() {
		this.timer.stop();
		this.defaultPageViewJFrame.dispose();
		this.jframe.dispose();
		this.controller.restart();
	}
}
