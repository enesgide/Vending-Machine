package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.File;
import java.io.FileWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class SellerView {

	private Timer timer;

	private Model model;
	private Controller controller;

	private JFrame jframe;
	private JPanel jpanel;

	private JButton userButton;
	private JButton purchaseButton;
	private JButton backToOwnerButton;

	private JLabel productsLabel;
	private JTable productsTable;
	private JScrollPane productsScrollPane;

	private JButton addButton;
	private JButton deleteButton;


	// WINDOW LAYNOUT DATA
	
	private int DELAY = 120000;

	private final int[] WINDOW_SIZE = {600, 750};
	private final int[] USER_BUTTON_BP = {16, 16, 100, 36};
	private final int[] PURCHASE_BUTTON_BP = {116, 16, 100, 36};
	private final int[] BACK_TO_OWNER_BUTTON_BP = {216, 16, 100, 36};

	private final int[] PRODUCTS_TABLE_COLUMN_WIDTH = {50, 120, 180, 70, 20, 70, 20};

	private final int LABEL_FONT_SIZE = 20;
	private final String LABEL_FONT = "Arial";
	private final int LABEL_FONT_MODE = Font.PLAIN;

	private final int[] PRODUCTS_LABEL_BP = {18, 60, 300, 32};
	private final int[] PRODUCTS_SCROLL_PANE_BP = {18, 100, 564, 300};

	private final int[] ADD_BUTTON_BP = {495, 60, 90, 30};
	private final int[] DELETE_BUTTON_BP = {405, 60, 90, 30};



	public SellerView() {
		this.timer = null;
		this.model = null;
		this.controller = null;

		this.jframe = new JFrame("Seller Mode");
        this.jpanel = new JPanel();

		this.userButton = new JButton();
		this.purchaseButton = new JButton();
		this.backToOwnerButton = new JButton();

		this.productsLabel = new JLabel();
		this.productsTable = new JTable();
		this.productsScrollPane = new JScrollPane();

		this.addButton = new JButton();
		this.deleteButton = new JButton();

	}

	public void setController(Controller controller) {
		this.controller = controller;
		this.controller.setSellerView(this);
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

		this.jframe.setSize(WINDOW_SIZE[0], WINDOW_SIZE[1]);
		this.jframe.setResizable(false);
		this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jframe.setLocationRelativeTo(null);

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
		this.userButton.setText(this.model.getCurrentUser().getName());
		this.userButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					timer.restart();
					launchTryToLogOutWindow();	
				}
			});
		this.jpanel.add(this.userButton);

		// JButton for defaultPageView
		this.purchaseButton.setText("Purchase");
		this.purchaseButton.setBounds(
				PURCHASE_BUTTON_BP[0],
				PURCHASE_BUTTON_BP[1],
				PURCHASE_BUTTON_BP[2],
				PURCHASE_BUTTON_BP[3]
			);
		this.purchaseButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				launchDefaultPageViewWindow();
			}
		});
		this.jpanel.add(this.purchaseButton);

		// JButton for back to ownerView
		this.backToOwnerButton.setText("Accounts");
		this.backToOwnerButton.setBounds(
				BACK_TO_OWNER_BUTTON_BP[0],
				BACK_TO_OWNER_BUTTON_BP[1],
				BACK_TO_OWNER_BUTTON_BP[2],
				BACK_TO_OWNER_BUTTON_BP[3]
			);
		this.backToOwnerButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				launchOwnerViewWindow();
			}
		});
		if (this.model.getCurrentUser().getType().equals(UserType.OWNER)) {
			this.jpanel.add(this.backToOwnerButton);
		}


		/**
		 * ======================
		 * ### Products Table ###
		 * ======================
		 * */
		// JLabel for products table
		this.productsLabel.setText("Maintain Products: ");
		this.productsLabel.setFont(new Font(LABEL_FONT, LABEL_FONT_MODE, LABEL_FONT_SIZE));
		this.productsLabel.setBounds(
				PRODUCTS_LABEL_BP[0],
				PRODUCTS_LABEL_BP[1],
				PRODUCTS_LABEL_BP[2],
				PRODUCTS_LABEL_BP[3]
			);
		this.jpanel.add(this.productsLabel);

		// JButton for add
		this.addButton.setText("Add");
		this.addButton.setBounds(
				ADD_BUTTON_BP[0],
				ADD_BUTTON_BP[1],
				ADD_BUTTON_BP[2],
				ADD_BUTTON_BP[3]
			);
		this.addButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				launchTryToAddProductWindow();
			}
		});
		this.jpanel.add(this.addButton);

		// JButton for delete
		this.deleteButton.setText("Delete");
		this.deleteButton.setBounds(
				DELETE_BUTTON_BP[0],
				DELETE_BUTTON_BP[1],
				DELETE_BUTTON_BP[2],
				DELETE_BUTTON_BP[3]
			);
		this.deleteButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				launchTryToDeleteProductWindow();
			}
		});
		this.jpanel.add(this.deleteButton);

		// JTable for products table
		updateProductsTable();	


		// Prodce a report upon logged in
		obtainReports();


		// Show window
		this.jframe.setVisible(true);
	}

	public void updateView() {
		this.timer.restart();
		updateProductsTable();
	}



	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */
	private void buildProductsTable() {
		String[] productsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		Object[][] productsData = new Object[this.model.getProductsAllFromDB().size()][productsTableColumnNames.length];
		this.productsTable = new JTable(productsData, productsTableColumnNames) {

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
		setColumnWidth(this.productsTable, PRODUCTS_TABLE_COLUMN_WIDTH);
		this.productsTable.getColumnModel().getColumn(4).setCellEditor(new SellerProductsButtonEditor(new JTextField(), this.controller));
		this.productsTable.getColumnModel().getColumn(4).setCellRenderer(new SellerProductsButtonRenderer());
		this.productsTable.getColumnModel().getColumn(6).setCellEditor(new SellerProductsButtonEditor(new JTextField(), this.controller));
		this.productsTable.getColumnModel().getColumn(6).setCellRenderer(new SellerProductsButtonRenderer());
	}

	private void drawProductsTable() {
		this.jpanel.remove(this.productsScrollPane);
		this.productsScrollPane = new JScrollPane(this.productsTable);
		this.productsScrollPane.setBounds(
				PRODUCTS_SCROLL_PANE_BP[0],
				PRODUCTS_SCROLL_PANE_BP[1],
				PRODUCTS_SCROLL_PANE_BP[2],
				PRODUCTS_SCROLL_PANE_BP[3]
			);
		this.productsScrollPane.repaint();
		this.productsScrollPane.setVisible(true);
		this.jpanel.add(this.productsScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}

	private void launchDefaultPageViewWindow() {
		timer.stop();
		jframe.dispose();
		controller.setSellerView(null);
		DefaultPageView defaultPageView = new DefaultPageView();
		defaultPageView.setModel(model);
		defaultPageView.setController(controller);
		controller.setDefaultPageView(defaultPageView);
		defaultPageView.launchWindow();
	}

	private void launchOwnerViewWindow() {
		timer.stop();
		jframe.dispose();
		controller.setSellerView(null);
		OwnerView ownerView = new OwnerView();
		ownerView.setModel(model);
		ownerView.setController(controller);
		controller.setOwnerView(ownerView);
		ownerView.launchWindow();
	}

	private void loadProductsTableData() {
		ArrayList<Product> productListInDB = this.model.getProductsAllFromDB();
		ArrayList<String> productNameList = new ArrayList<String>();
		for (Product p: productListInDB) {
			productNameList.add(p.getName());
		}
		// Sort to fix the order
		productNameList.sort(Comparator.naturalOrder());
		for (int i = 0; i < productNameList.size(); i++) {
			// Maintain order
			Product p = new Product();
			for (Product p0: productListInDB) {
				if (p0 == null || p0.getName() == null) continue;
				if (p0.getName().equals(productNameList.get(i))) {
					p = p0;
					break;
				}
			}
			// Set table data
			this.productsTable.setValueAt(i+1, i, 0);
			this.productsTable.setValueAt(p.getTypeString(), i, 1);
			this.productsTable.setValueAt(p.getName(), i, 2);
			this.productsTable.setValueAt(p.getPrice(), i, 3);
			this.productsTable.setValueAt("-", i, 4);
			this.productsTable.setValueAt(p.getAmount(), i, 5);
			this.productsTable.setValueAt("+", i, 6);
		}
	}

	private void launchTryToAddProductWindow() {
		String productDataString = JOptionPane.showInputDialog("Enter product data in the following format: \n" + 
					"<ID>;<TYPE>;<NAME>;<PRICE>"
				);
		if (productDataString == null) return;
		String[] productData = productDataString.split(";");
		if (productData.length != 4) {
			JOptionPane.showMessageDialog(null,"Wrong format!");
			return;
		}
		
		if (productData[0].matches("-?(0|[1-9]\\d*)")) {
			Integer id = Integer.parseInt(productData[0]);
			if (this.controller.ifHasProductInDB(id)) {
				JOptionPane.showMessageDialog(null,"Already have product with the same id.");
				return;
			}
			ProductType type = null;
			if (productData[1].toUpperCase().equals("DRINK"))type = ProductType.DRINK;
			if (productData[1].toUpperCase().equals("CHOCOLATE")) type = ProductType.CHOCOLATE; 
			if (productData[1].toUpperCase().equals("CHIP")) type = ProductType.CHIP; 
			if (productData[1].toUpperCase().equals("CANDY")) type = ProductType.CANDY; 
			if (type == null) {
				JOptionPane.showMessageDialog(null,"Wrong TYPE format!");
				return;
			}
			String name = productData[2];
			if (this.controller.ifHasProductInDB(name)) {
				JOptionPane.showMessageDialog(null,"Already have this product with the same name.");
				return;
			}
			Double price = null;
			try {
				price = Double.parseDouble(productData[3]);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"Wrong Price format!");
				return;
			}
			this.controller.insertProductToDB(new Product(id, type, name, price, 0, 0));
			JOptionPane.showMessageDialog(null,"Product added successfully!");
			updateView();
		} else {
			JOptionPane.showMessageDialog(null,"Wrong ID format!");
			return;
		}
	}

	public void launchTryToDeleteProductWindow() {
		String productName = JOptionPane.showInputDialog(null, "Enter the name of the product to delete: ");
		if (this.controller.ifHasProductInDB(productName)) {
			this.controller.deleteProductInDB(productName);
			JOptionPane.showMessageDialog(null, productName + " deleted successfully.");
			this.controller.updateViewSeller();
		} else if (productName == null) {
			return;
		} else {
			JOptionPane.showMessageDialog(null, "No such product!");
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

	private void obtainReports() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter ldtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String reportTail = ldtf.format(ldt);
		try {
			File current = new File("./Reports/Products/Current/ProductsReport_Current" + reportTail + ".txt");
			FileWriter fw = new FileWriter(current);
			fw.write("From Seller: " + this.model.getCurrentUser().getName() + "\n");
			for (Product p: this.model.getProductsAllFromDB()) {
				if (p.getAmount() == 0) continue;
				String msg = "";
				msg += "ID: " + p.getId() + "; ";
				msg += "Type: " + p.getTypeString() + "; ";
				msg += "Name: " + p.getName() + "; ";
				msg += "Price: " + p.getPrice() + "; ";
				msg += "Amount left: " + p.getAmount() + ".\n";
				fw.write(msg);
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			File current = new File("./Reports/Products/History/ProductsReport_History" + reportTail + ".txt");
			FileWriter fw = new FileWriter(current);
			fw.write("From Seller: " + this.model.getCurrentUser().getName() + "\n");
			for (Product p: this.model.getProductsAllFromDB()) {
				if (p.getTotalSold() == 0) continue;
				String msg = "";
				msg += "ID: " + p.getId() + "; ";
				msg += "Type: " + p.getTypeString() + "; ";
				msg += "Name: " + p.getName() + "; ";
				msg += "Price: " + p.getPrice() + "; ";
				msg += "Amount sold: " + p.getTotalSold() + ".\n";
				fw.write(msg);
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setColumnWidth(JTable table, int[] sizeArray) {
		for (int i = 0; i < sizeArray.length; i++) {
			table.getColumnModel().getColumn(i).setMaxWidth(sizeArray[i]);
			table.getColumnModel().getColumn(i).setMinWidth(sizeArray[i]);
		}
	}

	private void updateProductsTable() {
		buildProductsTable();
		loadProductsTableData();
		drawProductsTable();
	}



	/**
	 * ######################
	 * ### HELPER CLASSES ###
	 * ######################
	 * */
	class SellerProductsButtonRenderer extends JButton implements TableCellRenderer {

		public SellerProductsButtonRenderer() {
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

	class SellerProductsButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public SellerProductsButtonEditor(JTextField textField, Controller controller) {
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
				if (column == 6 && value == 15) {
					JOptionPane.showMessageDialog(null, "Reach amount limit: 15!");
				} else {
					this.controller.updateProductsAmountsToDB(productName, value, column);
					this.controller.updateViewSeller();
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

		@Override
		protected void fireEditingCanceled() {
			super.fireEditingCanceled();
		}
	}
}

