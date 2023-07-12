package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.HashMap;

public class CashPayView {
	
	private Timer timer;

	private Model model;
	private Controller controller;

	private JFrame defaultPageViewJFrame;

	private JFrame jframe;
	private JPanel jpanel;

	private JLabel instructionLabel;

	private JTable cashTable;
	private JScrollPane cashScrollPane;

	private JLabel totalPriceLabel;
	private JLabel currentPriceLabel;
	private JLabel leftPriceLabel;

	private JButton backButton;
	private JButton confirmButton;
	private JButton cancelButton;

	
	// DATA
	private final int[] WINDOW_SIZE = {300, 450};

	private final int[] INSTRUCTION_LABEL_BP = {20, 7, 200, 32};

	private final int[] CASH_TABLE_COLUMN_WIDTH = {100, 30, 80, 30};
	private final int[] CASH_SCROLL_PANE_BP = {20, 40, 260, 230};

	private final int[] TOTAL_PRICE_BP = {20, 275, 200, 20};
	private final int[] CURRENT_PRICE_BP = {20, 295, 200, 20};
	private final int[] LEFT_PRICE_BP = {20, 315, 200, 20};

	private final int[] BACK_BUTTON_BP = {20, 340, 100, 32};
	private final int[] CONFIRM_BUTTON_BP = {180, 340, 100, 32};
	private final int[] CANCEL_BUTTON_BP = {20, 370, 260, 32};


	public CashPayView(Model model, Controller controller, JFrame defaultPageViewJFrame, Timer timer) {
		this.timer = timer;

		this.model = model;
		this.controller = controller;
		this.controller.setCashPayView(this);
		this.defaultPageViewJFrame = defaultPageViewJFrame;

		this.jframe = new JFrame("Cash Pay");
		this.jpanel = new JPanel();

		this.instructionLabel = new JLabel();

		this.cashTable = null;
		this.cashScrollPane = new JScrollPane();

		this.totalPriceLabel = new JLabel();
		this.currentPriceLabel = new JLabel();
		this.leftPriceLabel = new JLabel();

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
		 * ===================
		 * ### Instruction ###
		 * ===================
		 * */
		// JLabel for instructions
		this.instructionLabel.setText("Please insert cash: ");
		this.instructionLabel.setBounds(
				INSTRUCTION_LABEL_BP[0],
				INSTRUCTION_LABEL_BP[1],
				INSTRUCTION_LABEL_BP[2],
				INSTRUCTION_LABEL_BP[3]
			);
		this.jpanel.add(this.instructionLabel);

		/**
		 * ==================
		 * ### Cash Table ###
		 * ==================
		 * */
		// JTable for cash table
		buildCashTable();
		loadCashTableData();
		drawCashTable();


		/**
		 * =============
		 * ### PRICE ###
		 * =============
		 * */
		// JLabel for total price
		this.totalPriceLabel.setBounds(
				TOTAL_PRICE_BP[0],
				TOTAL_PRICE_BP[1],
				TOTAL_PRICE_BP[2],
				TOTAL_PRICE_BP[3]
			);
		// JLabel for current price
		this.currentPriceLabel.setBounds(
				CURRENT_PRICE_BP[0],
				CURRENT_PRICE_BP[1],
				CURRENT_PRICE_BP[2],
				CURRENT_PRICE_BP[3]
			);
		// JLabel for change
		this.leftPriceLabel.setBounds(
				LEFT_PRICE_BP[0],
				LEFT_PRICE_BP[1],
				LEFT_PRICE_BP[2],
				LEFT_PRICE_BP[3]
			);
		updatePrice();

		/**
		 * ===============
		 * ### Buttons ###
		 * ===============
		 * */
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
				jframe.dispose();
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
				if (!controller.ifGaveEnoughMoney()) {
					JOptionPane.showMessageDialog(null, "Not enough money!");
				} else if (!controller.ifHasEnoughChange()) {
					JOptionPane.showMessageDialog(null, "Not enough change!");
				} else {
					controller.produceReportSuccessful(0);
					controller.confirmPay(0);
					JOptionPane.showMessageDialog(null, "Payment Successful!");
					restart();	
				}
			}
		});
		this.jpanel.add(this.confirmButton);


		// Show window
		this.jframe.setVisible(true);
	}

	public void updateView() {
		this.timer.restart();
		updateCashTable();
		updatePrice();
	}

	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */
	private void buildCashTable() {
		Object[][] cashData = new Object[this.model.getCashMap().size()][7];
		// Create table
		String[] cashTableColumnNames = {"Name", "-", "Amount", "+"};
		this.cashTable = new JTable(cashData, cashTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return cashData[row][column];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 1 || column == 3) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				cashData[row][column] = value;
			}
		};
		// Set column width
		for (int i = 0; i < CASH_TABLE_COLUMN_WIDTH.length; i++) {
			this.cashTable.getColumnModel().getColumn(i).setMaxWidth(CASH_TABLE_COLUMN_WIDTH[i]);
			this.cashTable.getColumnModel().getColumn(i).setMinWidth(CASH_TABLE_COLUMN_WIDTH[i]);
		}
		this.cashTable.setRowSelectionAllowed(false);
		// Set buttons
		this.cashTable.getColumnModel().getColumn(1).setCellEditor(new CashButtonEditor(new JTextField(), this.controller));
		this.cashTable.getColumnModel().getColumn(1).setCellRenderer(new CashButtonRenderer());
		this.cashTable.getColumnModel().getColumn(3).setCellEditor(new CashButtonEditor(new JTextField(), this.controller));
		this.cashTable.getColumnModel().getColumn(3).setCellRenderer(new CashButtonRenderer());
	}

	private void drawCashTable() {
		this.jpanel.remove(this.cashScrollPane);
		this.cashScrollPane = new JScrollPane(this.cashTable);
		this.cashScrollPane.setBounds(
				CASH_SCROLL_PANE_BP[0],
				CASH_SCROLL_PANE_BP[1],
				CASH_SCROLL_PANE_BP[2],
				CASH_SCROLL_PANE_BP[3]
			);
		this.cashScrollPane.repaint();
		this.cashScrollPane.setVisible(true);
		this.jpanel.add(this.cashScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
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

	private void loadCashTableData() {
		HashMap<Cash, Integer> cashMap = this.model.getCashMap();
		String[] cashNameList = {
			"$100", "$50", "$20", "$10", 
			"$5", "$2", "$1", "¢50", "¢20",
			"¢10", "¢5", "¢2", "¢1"
		};
		for (int i = 0; i < cashNameList.length; i++) {
			Cash c = new Cash();
			for (Cash c0: cashMap.keySet()) {
				if (c0 == null || c0.getName() == null) continue;
				if (c0.getName().equals(cashNameList[i])) {
					c = c0;
					break;
				}
			}
			this.cashTable.setValueAt(c.getName(), i, 0);;
			this.cashTable.setValueAt("-", i, 1);;
			this.cashTable.setValueAt(cashMap.get(c), i, 2);;
			this.cashTable.setValueAt("+", i, 3);;
		}
	}

	private void restart() {
		this.timer.stop();
		this.defaultPageViewJFrame.dispose();
		this.jframe.dispose();
		this.controller.restart();
	}

	private void updateCashTable() {
		loadCashTableData();
		drawCashTable();
	}

	private void updatePrice() {
		// Calcualte data
		Double totalPrice = this.model.getTotalPrice();
		Double currentPrice = this.model.getCurrentPrice();
		Double leftPrice = totalPrice - currentPrice;

		// Draw
		this.jpanel.remove(this.totalPriceLabel);
		this.jpanel.remove(this.currentPriceLabel);
		this.jpanel.remove(this.leftPriceLabel);

		this.totalPriceLabel.setText("Total Price: $ " + totalPrice);
		this.currentPriceLabel.setText("Current Value: $ " + String.format("%.2f", currentPrice));
		this.leftPriceLabel.setText("Value left: $ " + String.format("%.2f", leftPrice));

		this.jpanel.add(this.totalPriceLabel);
		this.jpanel.add(this.currentPriceLabel);
		this.jpanel.add(this.leftPriceLabel);

		this.jpanel.revalidate();
		this.jpanel.repaint();
	}



	/**
	 * ######################
	 * ### HELPER CLASSES ###
	 * ######################
	 * */

	class CashButtonRenderer extends JButton implements TableCellRenderer {

		public CashButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	class CashButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public CashButtonEditor(JTextField textField, Controller controller) {
			super(textField);
			this.button = new JButton();
			this.setClickCountToStart(1);
			this.button.setOpaque(true);
			this.button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
			this.controller = controller;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, 
				boolean isSelected, int row, int column) {
			if (isSelected) {
				this.button.setForeground(table.getSelectionForeground());
				this.button.setBackground(table.getSelectionBackground());
			} else {
				this.button.setForeground(table.getForeground());
				this.button.setBackground(table.getBackground());
			}
			this.label = (value == null) ? "" : value.toString();
			this.button.setText(label);
			this.isPushed = true;
			this.jtable = table;
			return this.button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				timer.restart();
				// Get data from JTable
				int row = jtable.getSelectedRow();
				int column = jtable.getSelectedColumn();
				int value = Integer.parseInt(jtable.getValueAt(row, 2).toString());
				String cashName = jtable.getValueAt(row, 0).toString();	
				// Parse to Controller to update
				this.controller.updateCashAmount(cashName, value, column);
				this.controller.updateViewCashPay();
			}
			this.isPushed = false;
			return new String(label);
		}

		@Override
		public boolean stopCellEditing() {
			this.isPushed = false;
			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}

		@Override
		protected void fireEditingCanceled() {
			super.fireEditingCanceled();
		}
	}
}
