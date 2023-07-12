package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DefaultPageView {

	private Timer timer;

	private Model model;
	private Controller controller;

	private JFrame jframe;
	private JPanel jpanel;

	private JButton userButton;
	private JButton roleFunctioButton;

	private JLabel recentProductsLabel;
	private JTable recentProductsTable;
	private JScrollPane recentProductsScrollPane;

	private JLabel groupedProductsLabel;
	private JTable groupedProductsTable;
	private JTable groupedProductsTable_DRINK;
	private JTable groupedProductsTable_CHOCOLATE;
	private JTable groupedProductsTable_CHIP;
	private JTable groupedProductsTable_CANDY;
	private JScrollPane groupedProductsScrollPane;

	private JComboBox<String> groupedProductsTypeBox;
	private ProductType currentGroupedProductsType;

	private JLabel selectedProductsLabel;
	private JTable selectedProductsTable;
	private JScrollPane selectedProductsScrollPane;

	private JLabel totalPriceLabel;
	private Double totalPrice;

	private JButton confirmButton;


	// DATA
	
	private int DELAY = 120000;

	private final int[] WINDOW_SIZE = {600, 750};
	private final int[] USER_BUTTON_BP = {16, 16, 100, 36};
	private final int[] ROLE_FUNCTION_BUTTON_BP = {116, 16, 200, 36};

	private final int[] PRODUCTS_TABLE_COLUMN_WIDTH = {50, 120, 180, 70, 20, 70, 20};

	private final int LABEL_FONT_SIZE = 20;
	private final String LABEL_FONT = "Arial";
	private final int LABEL_FONT_MODE = Font.PLAIN;

	private final int[] RECENT_PRODUCTS_LABEL_BP = {18, 60, 300, 32};
	private final int[] RECENT_PRODUCTS_SCROLL_PANE_BP = {18, 100, 564, 100};

	private final int[] GROUPED_PRODUCTS_LABEL_BP = {18, 220, 200, 32};
	private final int[] GROUPED_PRODUCTS_TYPE_BOX_BP = {180, 221, 200, 32};
	private final int[] GROUPED_PRODUCTS_SCROLL_PANE_BP = {18, 260, 564, 120};

	private final int[] SELECTED_PRODUCTS_LABEL_BP = {18, 400, 200, 32};
	private final int[] SELECTED_PRODUCTS_SCROLL_PANE_BP = {18, 440, 564, 200};

	private final int[] TOTAL_AMOUNT_BP = {18, 660, 200, 32};
	
	private final int[] CONFIRM_BUTTON_BP = {470, 660, 110, 36};


	public DefaultPageView() {

		this.timer = null;

		this.model = null;
		this.controller = null;

		this.jframe = new JFrame("Vending Machine");
        this.jpanel = new JPanel();

		this.userButton = new JButton();
		this.roleFunctioButton = new JButton();

		this.recentProductsLabel = new JLabel();
		this.recentProductsTable = new JTable();
		this.recentProductsScrollPane = new JScrollPane();

		this.groupedProductsLabel = new JLabel();
		this.groupedProductsTable = new JTable();
		this.groupedProductsTable_DRINK = new JTable();
		this.groupedProductsTable_CHOCOLATE = new JTable();
		this.groupedProductsTable_CHIP = new JTable();
		this.groupedProductsTable_CANDY = new JTable();
		this.groupedProductsScrollPane = new JScrollPane();

		this.groupedProductsTypeBox = new JComboBox<String>();
		this.currentGroupedProductsType = ProductType.DRINK;

		this.selectedProductsLabel = new JLabel();
		this.selectedProductsTable = new JTable();
		this.selectedProductsScrollPane = new JScrollPane();

		this.totalPriceLabel = new JLabel();
		this.totalPrice = 0.0;

		this.confirmButton = new JButton();
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void launchWindow() {
		/**
		 * ===================
		 * ### Basic Setup ###
		 * ===================
		 * */

		// Set up the timer
		this.timer = new Timer(DELAY, null);
		this.timer.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.stop();
				jframe.dispose();
				controller.restart();
			}
		});
		this.timer.start();

		// Set up JFrame
		this.jframe.setSize(WINDOW_SIZE[0], WINDOW_SIZE[1]);
		this.jframe.setResizable(false);
		this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jframe.setLocationRelativeTo(null);

		// Set up JPanel
        this.jpanel.setLayout(null);
        this.jframe.add(this.jpanel);


		/**
		 * =========================
		 * ### Login / User Info ###
		 * =========================
		 * */
		// JButton for logged in user
		this.userButton.setBounds(
				USER_BUTTON_BP[0], 
				USER_BUTTON_BP[1], 
				USER_BUTTON_BP[2], 
				USER_BUTTON_BP[3]
			);

		// Set userButton text
		if (this.model.getCurrentUser().getName() == null) {
			this.userButton.setText("Login");;
		} else {
			this.userButton.setText(this.model.getCurrentUser().getName());
		}
		// Set up userButton actions
		if (this.model.getCurrentUser().getName() != null) {
			// If logged in
			this.userButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					timer.restart();
					launchTryToLogOutWindow();	
				}
			});
		} else {
			// If not logged in
			this.userButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					timer.restart();
					LoginView loginView = new LoginView(model, controller, jframe, timer);
					loginView.launchWindow();
				}
			});
		}
		this.jpanel.add(this.userButton);

		// JButton for role function	
		this.roleFunctioButton.setBounds(
				ROLE_FUNCTION_BUTTON_BP[0],
				ROLE_FUNCTION_BUTTON_BP[1],
				ROLE_FUNCTION_BUTTON_BP[2],
				ROLE_FUNCTION_BUTTON_BP[3]
			);
		if (this.model.getCurrentUser().getName() != null) {
			if (this.model.getCurrentUser().getType().equals(UserType.SELLER)) {
				this.roleFunctioButton.setText("Maintain Products");
				this.roleFunctioButton.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						timer.stop();
						jframe.dispose();
						controller.setDefaultPageView(null);
						SellerView sellerView = new SellerView();
						sellerView.setModel(model);	
						sellerView.setController(controller);
						controller.setSellerView(sellerView);
						sellerView.launchWindow();
					}
				});
				this.jpanel.add(this.roleFunctioButton);
			} else if (this.model.getCurrentUser().getType().equals(UserType.CASHIER)) {
				this.roleFunctioButton.setText("Maintain Cashes");
				this.roleFunctioButton.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						timer.stop();
						jframe.dispose();
						controller.setDefaultPageView(null);
						CashierView cashierView = new CashierView();
						cashierView.setModel(model);
						cashierView.setController(controller);
						controller.setCashierView(cashierView);
						cashierView.launchWindow();
					}
				});
				this.jpanel.add(this.roleFunctioButton);
			} else if (this.model.getCurrentUser().getType().equals(UserType.OWNER)) {
				this.roleFunctioButton.setText("Maintain Accounts");
				this.roleFunctioButton.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						timer.stop();
						jframe.dispose();
						controller.setDefaultPageView(null);
						OwnerView ownerView = new OwnerView();
						ownerView.setModel(model);
						ownerView.setController(controller);
						controller.setOwnerView(ownerView);
						ownerView.launchWindow();
					}
				});
				this.jpanel.add(this.roleFunctioButton);
			}
		}

		/**
		 * =======================
		 * ### Recent Products ###
		 * =======================
		 * */
		// JLabel for recent products
		this.recentProductsLabel.setText("Top 5 Recent Products: ");
		this.recentProductsLabel.setFont(new Font(LABEL_FONT, LABEL_FONT_MODE, LABEL_FONT_SIZE));	
		this.recentProductsLabel.setBounds(
				RECENT_PRODUCTS_LABEL_BP[0], 
				RECENT_PRODUCTS_LABEL_BP[1], 
				RECENT_PRODUCTS_LABEL_BP[2], 
				RECENT_PRODUCTS_LABEL_BP[3]
			);
		this.jpanel.add(this.recentProductsLabel);

		// JTable for recent Products
		buildRecentProductsTable();
		updateRecentProductsTable();
	

		/**
		 * ========================
		 * ### Grouped Products ###
		 * ========================
		 * */
		// JLabel for list of porducts
		this.groupedProductsLabel.setText("List of Products: ");
		this.groupedProductsLabel.setFont(new Font(LABEL_FONT, LABEL_FONT_MODE, LABEL_FONT_SIZE));
		this.groupedProductsLabel.setBounds(
				GROUPED_PRODUCTS_LABEL_BP[0], 
				GROUPED_PRODUCTS_LABEL_BP[1], 
				GROUPED_PRODUCTS_LABEL_BP[2], 
				GROUPED_PRODUCTS_LABEL_BP[3]
			);
		this.jpanel.add(this.groupedProductsLabel);
	
		// JComboBox for grouped products
		this.groupedProductsTypeBox.setBounds(
				GROUPED_PRODUCTS_TYPE_BOX_BP[0], 
				GROUPED_PRODUCTS_TYPE_BOX_BP[1], 
				GROUPED_PRODUCTS_TYPE_BOX_BP[2], 
				GROUPED_PRODUCTS_TYPE_BOX_BP[3]
			);
		this.groupedProductsTypeBox.addItem("Drinks");
		this.groupedProductsTypeBox.addItem("Chocolates");;
		this.groupedProductsTypeBox.addItem("Chips");
		this.groupedProductsTypeBox.addItem("Candies");
		this.groupedProductsTypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				timer.restart();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object source = e.getSource();
					if (source instanceof JComboBox) {
						JComboBox<?> cb = (JComboBox<?>)source;
						Object selectedItem = cb.getSelectedItem();
						if (selectedItem.equals("Drinks")) {
							updateGroupedProductsTableWithTypeChanged(ProductType.DRINK);
						} else if (selectedItem.equals("Chocolates")) {
							updateGroupedProductsTableWithTypeChanged(ProductType.CHOCOLATE);
						} else if (selectedItem.equals("Chips")) {
							updateGroupedProductsTableWithTypeChanged(ProductType.CHIP);
						} else if (selectedItem.equals("Candies")) {
							updateGroupedProductsTableWithTypeChanged(ProductType.CANDY);
						}
					}
				}
			}
		});
		this.groupedProductsTypeBox.setVisible(true);
		this.jpanel.add(this.groupedProductsTypeBox);

		// JTable for grouped products
		buildGroupedProductsTable();
		updateGroupedProductsTableWithTypeChanged(this.currentGroupedProductsType);


		/**
		 * =========================
		 * ### Selected Products ###
		 * =========================
		 * */
		// JLabel for selected products
		this.selectedProductsLabel.setText("Selected Products: ");
		this.selectedProductsLabel.setFont(new Font(LABEL_FONT, LABEL_FONT_MODE, LABEL_FONT_SIZE));
		this.selectedProductsLabel.setBounds(
				SELECTED_PRODUCTS_LABEL_BP[0],
				SELECTED_PRODUCTS_LABEL_BP[1],
				SELECTED_PRODUCTS_LABEL_BP[2],
				SELECTED_PRODUCTS_LABEL_BP[3]
			);
		this.jpanel.add(this.selectedProductsLabel);

		// JTable for selected products
		updateSelectedProductsTable();


		/**
		 * ====================
		 * ### Total Amount ###
		 * ====================
		 * */
		// JLabel for total amount
		this.totalPrice = this.model.getTotalPrice();
		this.totalPriceLabel.setText("Total Price: $ " + this.totalPrice);
		this.totalPriceLabel.setFont(new Font(LABEL_FONT, LABEL_FONT_MODE, LABEL_FONT_SIZE));
		this.totalPriceLabel.setBounds(
				TOTAL_AMOUNT_BP[0],
				TOTAL_AMOUNT_BP[1],
				TOTAL_AMOUNT_BP[2],
				TOTAL_AMOUNT_BP[3]
			);
		this.jpanel.add(this.totalPriceLabel);


		/**
		 * ======================
		 * ### Confirm Button ### 
		 * ======================
		 * */
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
				if (model.getTotalPrice() > 0) launchChoosePaymentMethodWindow();	
				else JOptionPane.showMessageDialog(null, "Please select products to purchase.");
			}
		});
		this.jpanel.add(this.confirmButton);

    	
		// Show window
        this.jframe.setVisible(true);
	}

	public void updateView() {
		this.timer.restart();
		updateRecentProductsTable();
		updateGroupedProductsTableWithSameType();
		updateSelectedProductsTable();
		updateTotalPrice();
	}



	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	private void buildGroupedProductsTable() {
		String[] productsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};

		// Build DRINK table
		Object[][] productsData_DRINK = new Object[this.model.getProductsByTypeFromDB(ProductType.DRINK).size()][7];
		this.groupedProductsTable_DRINK = new JTable(productsData_DRINK, productsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return productsData_DRINK[row][column];

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				productsData_DRINK[row][column] = value;
			}
		};
		setColumnWidth(this.groupedProductsTable_DRINK, PRODUCTS_TABLE_COLUMN_WIDTH);
		this.groupedProductsTable_DRINK.setRowSelectionAllowed(false);
		this.groupedProductsTable_DRINK.getColumnModel().getColumn(4).setCellEditor(new GroupedButtonEditor(new JTextField(), this.controller));
		this.groupedProductsTable_DRINK.getColumnModel().getColumn(4).setCellRenderer(new GroupedButtonRenderer());
		this.groupedProductsTable_DRINK.getColumnModel().getColumn(6).setCellEditor(new GroupedButtonEditor(new JTextField(), this.controller));
		this.groupedProductsTable_DRINK.getColumnModel().getColumn(6).setCellRenderer(new GroupedButtonRenderer());

		// Build CHOCOLATE table
		Object[][] productsData_CHOCOLATE = new Object[this.model.getProductsByTypeFromDB(ProductType.CHOCOLATE).size()][7];
		this.groupedProductsTable_CHOCOLATE = new JTable(productsData_CHOCOLATE, productsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return productsData_CHOCOLATE[row][column];

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				productsData_CHOCOLATE[row][column] = value;
			}
		};
		setColumnWidth(this.groupedProductsTable_CHOCOLATE, PRODUCTS_TABLE_COLUMN_WIDTH);
		this.groupedProductsTable_CHOCOLATE.setRowSelectionAllowed(false);
		this.groupedProductsTable_CHOCOLATE.getColumnModel().getColumn(4).setCellEditor(new GroupedButtonEditor(new JTextField(), this.controller));
		this.groupedProductsTable_CHOCOLATE.getColumnModel().getColumn(4).setCellRenderer(new GroupedButtonRenderer());
		this.groupedProductsTable_CHOCOLATE.getColumnModel().getColumn(6).setCellEditor(new GroupedButtonEditor(new JTextField(), this.controller));
		this.groupedProductsTable_CHOCOLATE.getColumnModel().getColumn(6).setCellRenderer(new GroupedButtonRenderer());

		// Build CHIP table
		Object[][] productsData_CHIP = new Object[this.model.getProductsByTypeFromDB(ProductType.CHIP).size()][7];
		this.groupedProductsTable_CHIP = new JTable(productsData_CHIP, productsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return productsData_CHIP[row][column];

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				productsData_CHIP[row][column] = value;
			}
		};
		setColumnWidth(this.groupedProductsTable_CHIP, PRODUCTS_TABLE_COLUMN_WIDTH);
		this.groupedProductsTable_CHIP.setRowSelectionAllowed(false);
		this.groupedProductsTable_CHIP.getColumnModel().getColumn(4).setCellEditor(new GroupedButtonEditor(new JTextField(), this.controller));
		this.groupedProductsTable_CHIP.getColumnModel().getColumn(4).setCellRenderer(new GroupedButtonRenderer());
		this.groupedProductsTable_CHIP.getColumnModel().getColumn(6).setCellEditor(new GroupedButtonEditor(new JTextField(), this.controller));
		this.groupedProductsTable_CHIP.getColumnModel().getColumn(6).setCellRenderer(new GroupedButtonRenderer());

		// Build CANDY table
		Object[][] productsData_CANDY = new Object[this.model.getProductsByTypeFromDB(ProductType.CANDY).size()][7];
		this.groupedProductsTable_CANDY = new JTable(productsData_CANDY, productsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return productsData_CANDY[row][column];

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				productsData_CANDY[row][column] = value;
			}
		};
		setColumnWidth(this.groupedProductsTable_CANDY, PRODUCTS_TABLE_COLUMN_WIDTH);
		this.groupedProductsTable_CANDY.setRowSelectionAllowed(false);
		this.groupedProductsTable_CANDY.getColumnModel().getColumn(4).setCellEditor(new GroupedButtonEditor(new JTextField(), this.controller));
		this.groupedProductsTable_CANDY.getColumnModel().getColumn(4).setCellRenderer(new GroupedButtonRenderer());
		this.groupedProductsTable_CANDY.getColumnModel().getColumn(6).setCellEditor(new GroupedButtonEditor(new JTextField(), this.controller));
		this.groupedProductsTable_CANDY.getColumnModel().getColumn(6).setCellRenderer(new GroupedButtonRenderer());
	}

	private void buildRecentProductsTable() {
		String[] productsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		int productDataLength = 0;
		for (Product p: this.model.getRecentProducts().keySet()) {
			if (p != null && p.getName() != null) productDataLength++;
		}
		Object[][] productsData = new Object[productDataLength][7];
		this.recentProductsTable = new JTable(productsData, productsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return productsData[row][column];

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				productsData[row][column] = value;
			}
		};
		setColumnWidth(this.recentProductsTable, PRODUCTS_TABLE_COLUMN_WIDTH);
		this.recentProductsTable.setRowSelectionAllowed(false);
		this.recentProductsTable.getColumnModel().getColumn(4).setCellEditor(new IODButtonEditor(new JTextField(), this.controller));
		this.recentProductsTable.getColumnModel().getColumn(4).setCellRenderer(new IODButtonRenderer());
		this.recentProductsTable.getColumnModel().getColumn(6).setCellEditor(new IODButtonEditor(new JTextField(), this.controller));
		this.recentProductsTable.getColumnModel().getColumn(6).setCellRenderer(new IODButtonRenderer());
	}

	public void buildSelectedProductsTable() {
		String[] productsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		Object[][] productsData = new Object[this.model.getSelectedProducts().keySet().size()][7];
		this.selectedProductsTable = new JTable(productsData, productsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return productsData[row][column];

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				productsData[row][column] = value;
			}
		};
		// Set column width
		setColumnWidth(this.selectedProductsTable, PRODUCTS_TABLE_COLUMN_WIDTH);
		this.selectedProductsTable.setRowSelectionAllowed(false);
		// Set buttons
		this.selectedProductsTable.getColumnModel().getColumn(4).setCellEditor(new SelectedButtonEditor(new JTextField(), this.controller));
		this.selectedProductsTable.getColumnModel().getColumn(4).setCellRenderer(new SelectedButtonRenderer());
		this.selectedProductsTable.getColumnModel().getColumn(6).setCellEditor(new SelectedButtonEditor(new JTextField(), this.controller));
		this.selectedProductsTable.getColumnModel().getColumn(6).setCellRenderer(new SelectedButtonRenderer());
	}

	private void drawGroupedProductsTable() {
		this.jpanel.remove(this.groupedProductsScrollPane);
		this.groupedProductsScrollPane = new JScrollPane(this.groupedProductsTable);
		this.groupedProductsScrollPane.setBounds(
				GROUPED_PRODUCTS_SCROLL_PANE_BP[0], 
				GROUPED_PRODUCTS_SCROLL_PANE_BP[1], 
				GROUPED_PRODUCTS_SCROLL_PANE_BP[2], 
				GROUPED_PRODUCTS_SCROLL_PANE_BP[3]
			);
		this.groupedProductsScrollPane.repaint();
		this.groupedProductsScrollPane.setVisible(true);
		this.jpanel.add(this.groupedProductsScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}

	private void drawRecentProductsTable() {
		this.jpanel.remove(this.recentProductsScrollPane);
		this.recentProductsScrollPane = new JScrollPane(this.recentProductsTable);
		this.recentProductsScrollPane.setBounds(
				RECENT_PRODUCTS_SCROLL_PANE_BP[0], 
				RECENT_PRODUCTS_SCROLL_PANE_BP[1], 
				RECENT_PRODUCTS_SCROLL_PANE_BP[2], 
				RECENT_PRODUCTS_SCROLL_PANE_BP[3]
			);
		this.recentProductsScrollPane.repaint();
		this.recentProductsScrollPane.setVisible(true);
		this.jpanel.add(this.recentProductsScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}

	private void drawSelectedProductsTable() {
		this.jpanel.remove(this.selectedProductsScrollPane);
		this.selectedProductsScrollPane = new JScrollPane(this.selectedProductsTable);
		this.selectedProductsScrollPane.setBounds(
				SELECTED_PRODUCTS_SCROLL_PANE_BP[0], 
				SELECTED_PRODUCTS_SCROLL_PANE_BP[1], 
				SELECTED_PRODUCTS_SCROLL_PANE_BP[2], 
				SELECTED_PRODUCTS_SCROLL_PANE_BP[3]
			);
		this.selectedProductsScrollPane.repaint();
		this.selectedProductsScrollPane.setVisible(true);
		this.jpanel.add(this.selectedProductsScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
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

	private void launchTryToLogOutWindow() {
		Object[] options = {"OK", "Log Out"};
		Object answer = JOptionPane.showOptionDialog(
				null, 
				"Current User: " + model.getCurrentUser().getName(), 
				"User Info", 
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, 
				options, 
				options[0]
			);
		if (answer.equals(1)) {
			timer.stop();
			jframe.dispose();
			controller.restart();
		}
	}

	private void loadGroupedProductsTableData() {
		HashMap<Product, Integer> groupedProducts = this.model.getGroupedProducts();
		ArrayList<String> productNameList = new ArrayList<String>();
		for (Product p: groupedProducts.keySet()) {
			productNameList.add(p.getName());
		}
		// Sort to fix the order
		productNameList.sort(Comparator.naturalOrder());
		for (int i = 0; i < productNameList.size(); i++) {
			// Maintain order
			Product p = new Product();
			for (Product p0: groupedProducts.keySet()) {
				if (p0 == null || p0.getName() == null) continue;
				if (p0.getName().equals(productNameList.get(i))) {
					p = p0;
					break;
				}
			}
			// Set table data
			this.groupedProductsTable.setValueAt(i+1, i, 0);
			this.groupedProductsTable.setValueAt(p.getTypeString(), i, 1);
			this.groupedProductsTable.setValueAt(p.getName(), i, 2);
			this.groupedProductsTable.setValueAt(p.getPrice(), i, 3);
			this.groupedProductsTable.setValueAt("-", i, 4);
			this.groupedProductsTable.setValueAt(groupedProducts.get(p), i, 5);
			this.groupedProductsTable.setValueAt("+", i, 6);
		}
	}

	private void loadRecentProductsTableData() {
		HashMap<Product, Integer> recenrProducts = this.model.getRecentProducts();
		ArrayList<String> productNameList = new ArrayList<String>();
		for (Product p: recenrProducts.keySet()) {
			if (p == null || p.getName() == null) continue;
			productNameList.add(p.getName());
		}
		// Sort to fix the order
		productNameList.sort(Comparator.naturalOrder());
		for (int i = 0; i < productNameList.size(); i++) {
			// Maintain order
			Product p = new Product();
			for (Product p0: recenrProducts.keySet()) {
				if (p0 == null || p0.getName() == null) continue;
				if (p0.getName().equals(productNameList.get(i))) {
					p = p0;
					break;
				}
			}
			// Set table data
			this.recentProductsTable.setValueAt(i+1, i, 0);
			this.recentProductsTable.setValueAt(p.getTypeString(), i, 1);
			this.recentProductsTable.setValueAt(p.getName(), i, 2);
			this.recentProductsTable.setValueAt(p.getPrice(), i, 3);
			this.recentProductsTable.setValueAt("-", i, 4);
			this.recentProductsTable.setValueAt(recenrProducts.get(p), i, 5);
			this.recentProductsTable.setValueAt("+", i, 6);
		}
	}

	private void loadSelectedProductsTableData() {
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();
		ArrayList<String> productNameList = new ArrayList<String>();
		for (Product p: selectedProducts.keySet()) {
			productNameList.add(p.getName());
		}
		// Sort to fix the order
		productNameList.sort(Comparator.naturalOrder());
		for (int i = 0; i < productNameList.size(); i++) {
			// Maintain order
			Product p = new Product();
			for (Product p0: selectedProducts.keySet()) {
				if (p0 == null || p0.getName() == null) continue;
				if (p0.getName().equals(productNameList.get(i))) {
					p = p0;
					break;
				}
			}
			// Set table data
			this.selectedProductsTable.setValueAt(i+1, i, 0);
			this.selectedProductsTable.setValueAt(p.getTypeString(), i, 1);
			this.selectedProductsTable.setValueAt(p.getName(), i, 2);
			this.selectedProductsTable.setValueAt(p.getPrice(), i, 3);
			this.selectedProductsTable.setValueAt("-", i, 4);
			this.selectedProductsTable.setValueAt(selectedProducts.get(p), i, 5);
			this.selectedProductsTable.setValueAt("+", i, 6);
		}
	}

	private void setColumnWidth(JTable table, int[] sizeArray) {
		for (int i = 0; i < sizeArray.length; i++) {
			table.getColumnModel().getColumn(i).setMaxWidth(sizeArray[i]);
			table.getColumnModel().getColumn(i).setMinWidth(sizeArray[i]);
		}
	}

	private void updateGroupedProductsTableWithSameType() {
		loadGroupedProductsTableData();
		drawGroupedProductsTable();
	}

	private void updateGroupedProductsTableWithTypeChanged(ProductType newType) {
		this.currentGroupedProductsType = newType;
		if (newType.equals(ProductType.DRINK)) {
			this.groupedProductsTable = this.groupedProductsTable_DRINK;
		} else if (newType.equals(ProductType.CHOCOLATE)) {
			this.groupedProductsTable = this.groupedProductsTable_CHOCOLATE;
		} else if (newType.equals(ProductType.CHIP)) {
			this.groupedProductsTable = this.groupedProductsTable_CHIP;
		} else if (newType.equals(ProductType.CANDY)) {
			this.groupedProductsTable = this.groupedProductsTable_CANDY;
		}
		this.controller.changeGroup(newType);
		updateGroupedProductsTableWithSameType();
	}

	private void updateRecentProductsTable() {
		loadRecentProductsTableData();
		drawRecentProductsTable();
	}

	private void updateSelectedProductsTable() {
		buildSelectedProductsTable();
		loadSelectedProductsTableData();
		drawSelectedProductsTable();
	}

	private void updateTotalPrice() {
		this.jpanel.remove(this.totalPriceLabel);
		this.totalPrice = this.model.getTotalPrice();
		this.totalPriceLabel.setText("Total Price: $ " + this.totalPrice);
		this.jpanel.add(this.totalPriceLabel);
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}



	/**
	 * ######################
	 * ### HELPER CLASSES ###
	 * ######################
	 * */

	class IODButtonRenderer extends JButton implements TableCellRenderer {

		public IODButtonRenderer() {
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

	class IODButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public IODButtonEditor(JTextField textField, Controller controller) {
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
				int value = Integer.parseInt(jtable.getValueAt(row, 5).toString());
				String productName = jtable.getValueAt(row, 2).toString();	
				// Parse to Controller to update
				if (this.controller.ifHasEnoughProducts(productName, value, column)) {
					this.controller.updateRecentAmount(productName, value, column);
					this.controller.updateViewDefaultPage();
				} else {
					JOptionPane.showMessageDialog(null, "Not enough products!");
				}
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
	}

	class GroupedButtonRenderer extends JButton implements TableCellRenderer {

		public GroupedButtonRenderer() {
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

	class GroupedButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public GroupedButtonEditor(JTextField textField, Controller controller) {
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
				int value = Integer.parseInt(jtable.getValueAt(row, 5).toString());
				String productName = jtable.getValueAt(row, 2).toString();	
				// Parse to Controller to update
				if (this.controller.ifHasEnoughProducts(productName, value, column)) {
					this.controller.updateGroupedAmount(productName, value, column);
					this.controller.updateViewDefaultPage();
				} else {
					JOptionPane.showMessageDialog(null, "Not enough products!");
				}
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
	}

	class SelectedButtonRenderer extends JButton implements TableCellRenderer {

		public SelectedButtonRenderer() {
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

	class SelectedButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public SelectedButtonEditor(JTextField textField, Controller controller) {
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
				int value = Integer.parseInt(jtable.getValueAt(row, 5).toString());
				String productName = jtable.getValueAt(row, 2).toString();	
				// Parse to Controller to update
				if (this.controller.ifHasEnoughProducts(productName, value, column)) {
					this.controller.updateSelectedAmount(productName, value, column);
					this.controller.updateViewDefaultPage();
				} else {
					JOptionPane.showMessageDialog(null, "Not enough products!");
				}
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
	}
}
