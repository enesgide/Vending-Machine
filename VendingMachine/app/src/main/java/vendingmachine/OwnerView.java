package vendingmachine;

import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.ArrayList;
import java.util.Comparator;

public class OwnerView {

	private Timer timer;

	private Model model;
	private Controller controller;

	private JFrame jframe;
	private JPanel jpanel;

	private JButton userButton;
	private JButton purchaseButton;
	private JButton cashierPageButton;
	private JButton sellerPageButton;

	private JLabel usersLabel;
	private JTable  usersTable;
	private JScrollPane usersScrollPane;

	private JButton addButton;
	private JButton deleteButton;

	private JButton confirmButton;


	// DATA
	
	private final int DELAY = 120000;

	private final int[] WINDOW_SIZE = {600, 750};
	private final int[] USER_BUTTON_BP = {16, 16, 100, 36};
	private final int[] PURCHASE_BUTTON_BP = {116, 16, 100, 36};
	private final int[] CASH_BUTTON_BP = {216, 16, 100, 36};
	private final int[] PRODUCT_BUTTON_BP = {316, 16, 100, 36};

	private final int[] USERS_LABEL_BP = {18, 60, 300, 32};
	private final int[] USERS_SCROLL_PANE_BP = {18, 100, 564, 300};

	private final int[] ADD_BUTTON_BP = {495, 60, 90, 30};
	private final int[] DELETE_BUTTON_BP = {405, 60, 90, 30};

	private final int[] CONFIRM_BUTTON_BP = {500, 400, 90, 30};


	public OwnerView() {
		this.timer = null;

		this.model = null;
		this.controller = null;

		this.jframe = new JFrame("Owner Mode");
        this.jpanel = new JPanel();

		this.userButton = new JButton();
		this.purchaseButton = new JButton();
		this.cashierPageButton = new JButton();
		this.sellerPageButton = new JButton();

		this.usersLabel = new JLabel();
		this.usersTable = new JTable();
		this.usersScrollPane = new JScrollPane();

		this.addButton = new JButton();
		this.deleteButton = new JButton();
		this.confirmButton = new JButton();

	}

	public void setController(Controller controller) {
		this.controller = controller;
		this.controller.setOwnerView(this);
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

		// JButton for sellerView
		this.sellerPageButton.setText("Products");
		this.sellerPageButton.setBounds(
				PRODUCT_BUTTON_BP[0],
				PRODUCT_BUTTON_BP[1],
				PRODUCT_BUTTON_BP[2],
				PRODUCT_BUTTON_BP[3]
			);
		this.sellerPageButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				launchSellerViewWindow();
			}
		});
		this.jpanel.add(this.sellerPageButton);

		// JButton for cashierView
		this.cashierPageButton.setText("Cashes");
		this.cashierPageButton.setBounds(
				CASH_BUTTON_BP[0],
				CASH_BUTTON_BP[1],
				CASH_BUTTON_BP[2],
				CASH_BUTTON_BP[3]
			);
		this.cashierPageButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				timer.restart();
				launchCashierViewWindow();
			}
		});
		this.jpanel.add(this.cashierPageButton);


		/**
		 * ===================
		 * ### Users Table ###
		 * ===================
		 * */
		// JLabel for user table
		this.usersLabel.setText("Maintain Users: ");
		this.usersLabel.setBounds(
				USERS_LABEL_BP[0],
				USERS_LABEL_BP[1],
				USERS_LABEL_BP[2],
				USERS_LABEL_BP[3]
			);
		this.jpanel.add(this.usersLabel);

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
				launchTryToAddUserWindow();
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
				launchTryToDeleteUserWindow();
			}
		});
		this.jpanel.add(this.deleteButton);

		// JTable for user table
		updateUserTable();	


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
				launchTryToConfirmWindow();	
			}
		});
		this.jpanel.add(this.confirmButton);


		// Prodce a report upon logged in
		obtainReports();

		
		// Show window
		this.jframe.setVisible(true);
	}

	public void updateView() {
		this.timer.restart();
		updateUserTable();
	}



	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */
	public void buildUsersTable() {
		String[] userTableColumnNames = {"User Type", "Name", "Password", "Card Name"};
		Object[][] userData = new Object[this.model.getUserAllFromDB().size()][userTableColumnNames.length];
		this.usersTable = new JTable(userData, userTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return userData[row][column];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 1) return false;
				else return true;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				userData[row][column] = value;
			}
		};
	}

	private void launchCashierViewWindow() {
		timer.stop();
		jframe.dispose();
		controller.setOwnerView(null);
		CashierView cashierView = new CashierView();
		cashierView.setModel(model);
		cashierView.setController(controller);
		controller.setCashierView(cashierView);
		cashierView.launchWindow();
	}

	private void launchDefaultPageViewWindow() {
		timer.stop();
		jframe.dispose();
		controller.setOwnerView(null);
		DefaultPageView defaultPageView = new DefaultPageView();
		defaultPageView.setModel(model);
		defaultPageView.setController(controller);
		controller.setDefaultPageView(defaultPageView);
		defaultPageView.launchWindow();
	}

	private void launchSellerViewWindow() {
		timer.stop();
		jframe.dispose();
		controller.setOwnerView(null);
		SellerView sellerView = new SellerView();
		sellerView.setModel(model);
		sellerView.setController(controller);
		controller.setSellerView(sellerView);
		sellerView.launchWindow();
	}


	private void loadUsersTableData() {
		ArrayList<User> userListInDB = this.model.getUserAllFromDB();
		ArrayList<String> userNameList = new ArrayList<String>();
		for (User u: userListInDB) {
			userNameList.add(u.getName());
		}
		userNameList.sort(Comparator.naturalOrder());
		for (int i = 0; i < userNameList.size(); i++) {
			// Maintain order
			User u = new User();
			for (User u0: userListInDB) {
				if (u0 == null || u0.getName() == null) continue;
				if (u0.getName().equals(userNameList.get(i))) {
					u = u0;
					break;
				}
			}
			// Set table data
			this.usersTable.setValueAt(u.getTypeString(), i, 0);
			this.usersTable.setValueAt(u.getName(), i, 1);
			this.usersTable.setValueAt(u.getPassword(), i, 2);
			this.usersTable.setValueAt(u.getCardName(), i, 3);
		}
	}

	private void drawUserstable() {
		this.jpanel.remove(this.usersScrollPane);
		this.usersScrollPane = new JScrollPane(this.usersTable);
		this.usersScrollPane.setBounds(
				USERS_SCROLL_PANE_BP[0],
				USERS_SCROLL_PANE_BP[1],
				USERS_SCROLL_PANE_BP[2],
				USERS_SCROLL_PANE_BP[3]
			);
		this.usersScrollPane.repaint();
		this.usersScrollPane.setVisible(true);
		this.jpanel.add(this.usersScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}

	public void launchTryToAddUserWindow() {
		String userDataString = JOptionPane.showInputDialog("Enter user data in the following format: \n" + 
					"<TYPE>;<NAME>;<PASSWORD>"
				);
		if (userDataString == null) return;
		String[] userData = userDataString.split(";");
		if (userData.length != 3) {
			JOptionPane.showMessageDialog(null,"Wrong format!");
			return;
		}

		UserType type = null;
		if (userData[0].toUpperCase().equals("NORMAL"))type = UserType.NORMAL;
		if (userData[0].toUpperCase().equals("SELLER")) type = UserType.SELLER; 
		if (userData[0].toUpperCase().equals("CASHIER")) type = UserType.CASHIER; 
		if (userData[0].toUpperCase().equals("OWNER")) type = UserType.OWNER; 
		if (type == null) {
			JOptionPane.showMessageDialog(null,"Wrong TYPE format!");
			return;
		}
		String name = userData[1];
		if (this.controller.ifHasUserInDB(name)) {
			JOptionPane.showMessageDialog(null,"Already have a user with the same name.");
			return;
		}

		ArrayList<Product> recentProducts = new ArrayList<Product>();
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		this.controller.insertUserToDB(new User(name, userData[2], recentProducts, type, null));
		JOptionPane.showMessageDialog(null,"User added successfully!");
		updateView();
	}

	public void launchTryToDeleteUserWindow() {
		String userName = JOptionPane.showInputDialog(null, "Enter the name of the user to delete: ");
		if (this.controller.ifHasUserInDB(userName)) {
			if (userName.equals("Owner")) {
				JOptionPane.showMessageDialog(null, "Owner cannot be deleted!");
			} else {
				this.controller.deleteUserInDB(userName);
				JOptionPane.showMessageDialog(null, userName + " deleted successfully.");
				this.controller.updateViewOwner();
			}
		} else if (userName == null) {
			return;
		} else {
			JOptionPane.showMessageDialog(null, "No such user!");
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

	private void launchTryToConfirmWindow() {
		for (int row = 0; row < this.model.getUserAllFromDB().size(); row++) {
			// Get new data
			String newTypeString = (String)this.usersTable.getValueAt(row, 0);
			newTypeString = newTypeString.toUpperCase();
			UserType newType = null;
			String newPassword = (String)this.usersTable.getValueAt(row, 2);
			String newCardName = (String)this.usersTable.getValueAt(row, 3);
			// Get data from database
			String userName = (String)this.usersTable.getValueAt(row, 1);
			User user = this.model.getUserFromDB(userName);
			// Cannot give seller, cashier and owner cards
			if (!user.getType().equals(UserType.NORMAL)) newCardName = null;

			// Set User Type
			if (newTypeString.equals("NORMAL")) newType = UserType.NORMAL;
			else if (newTypeString.equals("SELLER")) newType = UserType.SELLER;
			else if (newTypeString.equals("CASHIER")) newType = UserType.CASHIER;
			else if (newTypeString.equals("OWNER")) newType = UserType.OWNER;
			else {
				JOptionPane.showMessageDialog(null, "Invalid user type!");
				return;
			}
			user.setType(newType);

			// Set Password
			user.setPassword(newPassword);

			// Set Card Name
			if (newCardName == null || controller.ifHasCardGlobal(newCardName) || newCardName.equals("")) {
				user.setCardName(newCardName);
			} else {
				JOptionPane.showMessageDialog(null, "Invalid Card Name!");
				return;
			}

			// Update data to database
			this.controller.updateUserToDB(user);
		}
		JOptionPane.showMessageDialog(null, "Updated Successfully!");
	}

	private void obtainReports() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter ldtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String reportTail = ldtf.format(ldt);
		try {
			File current = new File("./Reports/Users/UserReport" + reportTail + ".txt");
			FileWriter fw = new FileWriter(current);
			fw.write("From Owner: " + this.model.getCurrentUser().getName() + "\n");
			for (User u: this.model.getUserAllFromDB()) {
				if (u.getName() == null) continue;
				String msg = "";
				msg += "Name: " + u.getName() + "; ";
				msg += "Role: " + u.getTypeString() + ".\n";
				fw.write(msg);
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			File outputFile = new File("./Reports/Transactions/Cancelled/CancelledTransactions" + reportTail + ".txt");
			File inputFile = new File("CancelledTransactions");
			outputFile.createNewFile();
			inputFile.createNewFile();
			copyFileUsingStream(inputFile, outputFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUserTable() {
		buildUsersTable();
		loadUsersTableData();
		drawUserstable();
	}

	private void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
}

