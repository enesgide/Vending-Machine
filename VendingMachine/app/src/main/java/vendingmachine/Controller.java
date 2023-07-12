package vendingmachine;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.*;
import java.time.format.*;

public class Controller {

	private Model model;
	private DefaultPageView defaultPageView;

	private CardPayView cardPayView;
	private CashPayView cashPayView;
	private CashierView cashierView;
	private LoginView loginView;
	private OwnerView ownerView;
	private RegisterView registerView;
	private SellerView sellerView;

	public Controller() {
		this.model = null;
		this.defaultPageView = null;

		this.cardPayView = null;
		this.cashPayView = null;
		this.cashPayView = null;
		this.loginView = null;
		this.registerView = null;
		this.sellerView = null;
	}

	public Controller(Model model, DefaultPageView defaultPageView) {
		this.model = model;
		this.defaultPageView = defaultPageView;

		this.cardPayView = null;
		this.cashPayView = null;
		this.loginView = null;
		this.registerView = null;
		this.sellerView = null;
	}

	public void launchWindow() {
		this.defaultPageView.launchWindow();
	}

	public void setCardPayView(CardPayView cardPayView) {
		this.cardPayView = cardPayView;
	}

	public void setCashPayView(CashPayView cashPayView) {
		this.cashPayView = cashPayView;
	}

	public void setCashierView(CashierView cashierView) {
		this.cashierView = cashierView;
	}

	public void setDefaultPageView(DefaultPageView defaultPageView) {
		this.defaultPageView = defaultPageView;
	}

	public void setLoginView(LoginView loginView) {
		this.loginView = loginView;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setOwnerView(OwnerView ownerView) {
		this.ownerView = ownerView;
	}

	public void setRegisterView(RegisterView registerView) {
		this.registerView = registerView;
	}

	public void setSellerView(SellerView sellerView) {
		this.sellerView = sellerView;
	}



	/**
	 * ###############
	 * ### Process ###
	 * ###############
	 * */
	public void changeGroup(ProductType type) {
		// retrieve data from model
		HashMap<Product, Integer> groupedProducts = new HashMap<Product, Integer>();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();
		// Get groupedProducts data from database
		for (Product p: this.model.getProductsByTypeFromDB(type)) {
			if (p == null) continue;
			groupedProducts.put(p.duplicate(), 0);
		}
		// Update selectedProducts to new groupedProducts
		for (Product gp: groupedProducts.keySet()) {
			for (Product sp: selectedProducts.keySet()) {
				if (sp == null || gp == null || sp.getName() == null || gp.getName() == null) continue;
				if (gp.getName().equals(sp.getName())) {
					groupedProducts.put(gp, selectedProducts.get(sp));
				}
			}
		}
		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
	}

	public int confirmPay(Integer paymentMethod) {
		if (paymentMethod.equals(0)) {
			updateCashInDBAfterPay();	
		}
		updateRecentAfterPay();
		updateSelectedProductsAmountsToDB();
		return 0;
	}

	public void deleteProductInDB(String name) {
		this.model.deleteProductInDB(name);
	}

	public void deleteUserInDB(String name) {
		this.model.deleteUserInDB(name);
	}

	public UserType getCurrentUserType() {
		if (this.ifLoggedIn()) {
			if (this.model.getCurrentUser().getType().equals(UserType.NORMAL)) return UserType.NORMAL;
			else if (this.model.getCurrentUser().getType().equals(UserType.SELLER)) return UserType.SELLER;
			else if (this.model.getCurrentUser().getType().equals(UserType.CASHIER)) return UserType.CASHIER;
			else if (this.model.getCurrentUser().getType().equals(UserType.OWNER)) return UserType.OWNER;
		}
		return null;
	}

	public void insertProductToDB(Product product) {
		this.model.insertProductToDB(product);
	}

	public void register(String userName, String password) {
		ArrayList<Product> recentProducts = new ArrayList<Product>();
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		User user = new User(userName, password, recentProducts, UserType.NORMAL, null);
		this.model.insertUserToDB(user);
	}

	public void insertUserToDB(User user) {
		this.model.insertUserToDB(user);
	}

	public void resetCashPayData() {
		this.model.setCurrentPrice(0.0);
		HashMap<Cash, Integer> cashMap = new HashMap<Cash, Integer>();
		for (Cash c: this.model.getCashAllFromDB()) {
			cashMap.put(c, 0);
		}
		this.model.setCashMap(cashMap);
	}

	public void produceReportCancel(String cancelledReason) {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter ldtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String time = ldtf.format(ldt);
		try {
			File current = new File("CancelledTransactions");
			FileWriter fw = new FileWriter(current, true);
			String msg = "";
			msg += "User: " + this.model.getCurrentUser().getName() + "; ";
			msg += "Time: " + time + ";\n";
			msg += "Reason: " + cancelledReason + ";\n\n";
			fw.write(msg);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void produceReportSuccessful(Integer paymentMethod) {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter ldtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String time = ldtf.format(ldt);
		try {
			File current = new File("SuccessfulTransactions");
			FileWriter fw = new FileWriter(current, true);
			String msg = "";
			msg += "User: " + this.model.getCurrentUser().getName() + "; ";
			msg += "Time: " + time + ";\n";
			msg += "Product: ";
			for (Product p: this.model.getSelectedProducts().keySet()) {
				msg += p.getName() + "; ";
			}
			msg += "\n";
			if (paymentMethod.equals(0)) {
				msg += "Payment Method: Cash; ";
				msg += "Amount paid: " + this.model.getCurrentPrice();
				msg += "; Change :" + (this.model.getCurrentPrice() - this.model.getTotalPrice()) + ";";
			} else {
				msg += "Payment Method: Card;";
			}
			msg += "\n\n";
			fw.write(msg);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void restart() {
		Model model = new Model(this.model.getJDBC(), this.model.getJSONpath());
		DefaultPageView defaultPageView = new DefaultPageView();
		Controller controller = new Controller(model, defaultPageView);
		defaultPageView.setController(controller);
		defaultPageView.setModel(model);
		controller.launchWindow();
	}

	public void setCurrentUser(String userName) {
		User user = this.model.getUserFromDB(userName);
		this.model.setCurrentUser(user);
	}



	/**
	 * ##############
	 * ### UPDATE ###
	 * ##############
	 * */

	public void updateAfterLogin() {
		// Update recent products from database to model
		HashMap<Product, Integer> recentProducts = new HashMap<Product, Integer>();
		for (Product p: this.model.getCurrentUser().getRecentProducts()) {
			if (p == null) continue;
			recentProducts.put(p.duplicate(), 0);
		}
		for (Product rp: recentProducts.keySet()) {
			for (Product sp: this.model.getSelectedProducts().keySet()) {
				if (rp == null || sp == null || rp.getName() == null || sp.getName() == null) continue;
				if (rp.getName().equals(sp.getName())) {
					recentProducts.put(rp, this.model.getSelectedProducts().get(sp));
				}
			}
		}
		this.model.setRecentProducts(recentProducts);
	}

	public void updateCashAmount(String cashName, Integer value, Integer column) {
		Double currentPrice = this.model.getCurrentPrice();
		HashMap<Cash, Integer> cashMap = this.model.getCashMap();
		Integer newAmount = value;
		if (column == 1) {
			if (newAmount > 0) {
				newAmount--;
			}
		} else {
			newAmount++;
		}
		for (Cash c: cashMap.keySet()) {
			if (c == null || c.getName() == null) continue;
			if (c.getName().equals(cashName)) {
				currentPrice -= value * c.getValue();
				cashMap.put(c, newAmount);
				currentPrice += newAmount * c.getValue();
			}
		}
		this.model.setCurrentPrice(currentPrice);
		this.model.setCashMap(cashMap);
	}

	public void updateCashAmountToDB(String cashName, Integer value, Integer column) {
		Integer newAmount = value;
		if (column == 1) {
			if (newAmount > 0) {
				newAmount--;
			}
		} else {
			newAmount++;
		}
		Cash cash = this.model.getCashFromDB(cashName);
		cash.setAmount(newAmount);
		this.model.updateCashToDB(cash);
	}

	public void updateCardInDB(String name, String number) {
		Card newCard = new Card(name, number);
		this.model.updateCardToDB(newCard);
	}

	/**
	 * Note: Assume there is enough cash provided and change to provide.
	 * */
	public void updateCashInDBAfterPay() {
		Double change = this.model.getCurrentPrice() - this.model.getTotalPrice();
		ArrayList<Cash> cashMapInDB = new ArrayList<Cash>();
		for (Cash c: this.model.getCashAllFromDB()) cashMapInDB.add(c);
		HashMap<Cash, Integer> cashMap = this.model.getCashMap();
		// Add cash given into database
		for (Cash cInDB: cashMapInDB) {
			for (Cash c: cashMap.keySet()) {
				if (c.getName().equals(cInDB.getName())) {
					cInDB.setAmount(cInDB.getAmount() + cashMap.get(c));
				}
			}
		}
		String[] cashNameList = {
			"$100", "$50", "$20", "$10", 
			"$5", "$2", "$1", "¢50", "¢20",
			"¢10", "¢5", "¢2", "¢1"
		};
		int[] changeAmountLeft = new int[cashNameList.length];
		int index = 0;
		for (Cash c: cashMapInDB) {
			for (String c0: cashNameList) {
				if (c.getName().equals(c0)) {
					Double curretCashValue = c.getValue();
					Integer curretCashAmountInDB = c.getAmount();
					Integer curretCashAmountNeeded = 0;
					while (change >= curretCashValue && curretCashAmountInDB > 0) {
						change -= curretCashValue;
						curretCashAmountNeeded++;
						curretCashAmountInDB--;
					}
					changeAmountLeft[index] = curretCashAmountInDB;
					index++;
				}
			}
		}
		for (int i = 0; i < cashNameList.length; i++) {
			for (Cash c: cashMapInDB) {
				if (c.getName().equals(cashNameList[i])) {
					c.setAmount(changeAmountLeft[i]);
				}
			}
		}
		for (Cash c: cashMapInDB) {
			this.model.updateCashToDB(c);
		}
	}

	public void updateGroupedAmount(String productName, Integer value, Integer column) {
		// Calcualte and update price and amount
		Double totalPrice = this.model.getTotalPrice();
		Double price = this.model.getProductFromDB(productName).getPrice();
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalPrice -= price;
			}
		} else {
			newAmount++;
			totalPrice += price;
		}
		this.model.setTotalPrice(totalPrice);

		// Retrieve data from model
		HashMap<Product, Integer> groupedProducts = this.model.getGroupedProducts();
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();
		
		// Update grouped products
		Product productCounter = null;
		for (Product gp: groupedProducts.keySet()) {
			if (gp == null || gp.getName() == null) continue;
			if (gp.getName().equals(productName)) {
				groupedProducts.put(gp, newAmount);
				productCounter = gp;
			}
		}
		// Update recent products
		for (Product rp: recentProducts.keySet()) {
			if (rp == null || rp.getName() == null) continue;
			if (rp.getName().equals(productName)) {
				recentProducts.put(rp, newAmount);
			}
		}
		// Update selected products
		Boolean inSelected = false;
		Product toRemove = null;
		for (Product sp: selectedProducts.keySet()) {
			if (sp == null || sp.getName() == null) continue;
			if (sp.getName().equals(productName)) {
				inSelected = true;
				selectedProducts.put(sp, newAmount);
				if (newAmount == 0) {
					toRemove = sp;
				}
			}
		}
		if (toRemove != null) {
			selectedProducts.remove(toRemove);
		}
		if (inSelected == false && newAmount > 0) {
			Product newProduct = productCounter.duplicate();
			selectedProducts.put(newProduct, newAmount);
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
		this.model.setRecentProducts(recentProducts);
		this.model.setSelectedProducts(selectedProducts);
	}


	public void updateRecentAfterPay() {
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();
		int selectedProductsLength = selectedProducts.size();

		// Update Global
		ArrayList<Product> globalRecent = this.model.getRecentProductsFromDB();
		for (int j = 0; j < selectedProductsLength; j++) {
			// Check duplicate
			Product newProduct = (Product)(selectedProducts.keySet().toArray())[j];
			boolean hasProduct = false;
			for (Product p: globalRecent) {
				if (p.getName().equals(newProduct.getName())) hasProduct = true;
			}
			if (hasProduct) continue;
			// No duplicate
			for (int i = 0; i < 4; i++) {
				globalRecent.set(4-i, globalRecent.get(3-i));
			}
			globalRecent.set(0, newProduct);
		}
		for (Product sp: selectedProducts.keySet()) {
			for (Product rp: globalRecent) {
				if (sp == null || rp == null || sp.getName() == null || rp.getName() == null) continue;
				if (rp.getName().equals(sp.getName())) {
					rp.setAmount(rp.getAmount() - selectedProducts.get(sp));
				}
			}
		}
		this.model.updateGlobalRecentToDB(globalRecent);

		if (this.model.getCurrentUser().getName() == null) return;

		// Update User
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		ArrayList<Product> userRecent = new ArrayList<Product>();
		for (Product p: recentProducts.keySet()) userRecent.add(p.duplicate());
		for (int j = 0; j < selectedProductsLength; j++) {
			// Check duplicate
			Product newProduct = (Product)(selectedProducts.keySet().toArray())[j];
			boolean hasProduct = false;
			for (Product p: userRecent) {
				if (p == null || p.getName() == null) continue;
				if (p.getName().equals(newProduct.getName())) hasProduct = true;
			}
			if (hasProduct) continue;
			// No duplicate
			for (int i = 0; i < 4; i++) {
				userRecent.set(4-i, userRecent.get(3-i));
			}
			userRecent.set(0, newProduct);
		}
		for (Product sp: selectedProducts.keySet()) {
			for (Product rp: userRecent) {
				if (sp == null || rp == null || sp.getName() == null || rp.getName() == null) continue;
				if (rp.getName().equals(sp.getName())) {
					rp.setAmount(rp.getAmount() - selectedProducts.get(sp));
				}
			}
		}
		User user = this.model.getCurrentUser();
		user.setRecentProduct(userRecent);
		this.model.updateUserToDB(user);
	}

	public void updateProductsAmountsToDB(String productName, Integer value, Integer column) {
		Product newProduct = this.model.getProductFromDB(productName);
		Integer newAmount = value;
		if (column == 4){
			if (newAmount > 0) {
				newAmount--;
			}
		} else {
			newAmount++;
		}
		newProduct.setAmount(newAmount);
		this.model.updateProductToDB(newProduct);
	}

	public void updateRecentAmount(String productName, Integer value, Integer column) {
		// Calcualte and update price and amount
		Double totalPrice = this.model.getTotalPrice();
		Double price = this.model.getProductFromDB(productName).getPrice();
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalPrice -= price;
			}
		} else {
			newAmount++;
			totalPrice += price;
		}
		this.model.setTotalPrice(totalPrice);
		
		// Retrieve data from model
		HashMap<Product, Integer> groupedProducts = this.model.getGroupedProducts();
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();

		// Update recent products
		Product productCounter = null;
		for (Product rp: recentProducts.keySet()) {
			if (rp == null || rp.getName() == null) continue;
			if (rp.getName().equals(productName)) {
				recentProducts.put(rp, newAmount);
				productCounter = rp;
			}
		}
		// Update grouped prouducts
		for (Product gp: groupedProducts.keySet()) {
			if (gp == null || gp.getName() == null) continue;
			if (gp.getName().equals(productName)) {
				groupedProducts.put(gp, newAmount);
			}
		}
		// Update selected products
		Boolean inSelected = false;
		Product toRemove = null;
		for (Product sp: selectedProducts.keySet()) {
			if (sp == null || sp.getName() == null) continue;
			if (sp.getName().equals(productName)) {
				inSelected = true;
				selectedProducts.put(sp, newAmount);
				if (newAmount == 0) {
					toRemove = sp;
				}
			}
		}
		if (toRemove != null) {
			selectedProducts.remove(toRemove);
		}
		if (inSelected == false && newAmount > 0) {
			Product newProduct = productCounter.duplicate();
			selectedProducts.put(newProduct, newAmount);
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
		this.model.setRecentProducts(recentProducts);
		this.model.setSelectedProducts(selectedProducts);
	}


	public void updateSelectedAmount(String productName, Integer value, Integer column) {
		// Calcualte and update price and amount
		Double totalPrice = this.model.getTotalPrice();
		Double price = this.model.getProductFromDB(productName).getPrice();
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalPrice -= price;
			}
		} else {
			newAmount++;
			totalPrice += price;
		}
		this.model.setTotalPrice(totalPrice);

		// Retrieve data from model
		HashMap<Product, Integer> groupedProducts = this.model.getGroupedProducts();
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();

		// Update selected products
		Product toRemove = null;	
		for (Product sp: selectedProducts.keySet()) {
			if (sp == null || sp.getName() == null) continue;
			if (sp.getName().equals(productName)) {
				selectedProducts.put(sp, newAmount);
				if (newAmount == 0) {
					toRemove = sp;	
				}
			}
		}
		if (toRemove != null) {
			selectedProducts.remove(toRemove);
		}
		// Update recent products
		for (Product rp: recentProducts.keySet()) {
			if (rp == null || rp.getName() == null) continue;
			if (rp.getName().equals(productName)) {
				recentProducts.put(rp, newAmount);
			}
		}
		// Update grouped products
		for (Product gp: groupedProducts.keySet())	{
			if (gp == null || gp.getName() == null) continue;
			if (gp.getName().equals(productName)) {
				groupedProducts.put(gp, newAmount);
			}
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
		this.model.setRecentProducts(recentProducts);
		this.model.setSelectedProducts(selectedProducts);
	}

	public void updateSelectedProductsAmountsToDB() {
		for (Product p: this.model.getSelectedProducts().keySet()) {
			Product pInDB = this.model.getProductFromDB(p.getName());
			pInDB.setAmount(pInDB.getAmount() - this.model.getSelectedProducts().get(p));
			pInDB.setTotalSold(pInDB.getTotalSold() + this.model.getSelectedProducts().get(p));
			this.model.updateProductToDB(pInDB);
		}
	}

	public void updateUserCardInfo(String name) {
		User user = this.model.getCurrentUser();
		user.setCardName(name);
		this.model.updateUserToDB(user);
	}

	public void updateUserToDB(User user) {
		this.model.updateUserToDB(user);
	}

	public void updateViewDefaultPage() {
		this.defaultPageView.updateView();
	}

	public void updateViewCashPay() {
		this.cashPayView.updateView();
	}

	public void updateViewCashier() {
		this.cashierView.updateView();
	}

	public void updateViewSeller() {
		this.sellerView.updateView();
	}

	public void updateViewOwner() {
		this.ownerView.updateView();
	}



	/**
	 * #############
	 * ### Check ###
	 * #############
	 * */
	public Boolean ifCurrentUserHasCard() {
		if (this.model.getCurrentUser().getName() == null) return false;
		if (this.model.getCurrentUser().getCardName() == null) return false;
		return true;
	}

	public Boolean ifCurrentUserHasCard(String name) {
		if (this.model.getCurrentUser().getName() == null) return false;
		if (this.model.getCurrentUser().getCardName() == null) return false;
		if (this.model.getCurrentUser().getCardName().equals(name)) return true;
		return false;
	}

	public Boolean ifGaveEnoughMoney() {
		if (this.model.getCurrentPrice() - this.model.getTotalPrice() < 0) return false;
		return true;
	}

	public Boolean ifHasCardGlobal(String name) {
		for (String cardName: this.model.getCardInfoMap().keySet()) {
			if (cardName.equals(name)) return true;
		}
		return false;
	}

	public Boolean ifHasEnoughChange() {
		Double change = this.model.getCurrentPrice() - this.model.getTotalPrice();
		ArrayList<Cash> cashMapInDB = new ArrayList<Cash>();
		for (Cash c: this.model.getCashAllFromDB()) cashMapInDB.add(c);
		HashMap<Cash, Integer> cashMap = this.model.getCashMap();
		for (Cash cInDB: cashMapInDB) {
			for (Cash c: cashMap.keySet()) {
				if (c.getName().equals(cInDB.getName())) {
					cInDB.setAmount(cInDB.getAmount() + cashMap.get(c));
				}
			}
		}
		String[] cashNameList = {
			"$100", "$50", "$20", "$10", 
			"$5", "$2", "$1", "¢50", "¢20",
			"¢10", "¢5", "¢2", "¢1"
		};
		int[] changeAmountLeft = new int[cashNameList.length];
		for (Cash c: cashMapInDB) {
			int index = 0;
			for (String c0: cashNameList) {
				if (c.getName().equals(c0)) {
					Double curretCashValue = c.getValue();
					Integer curretCashAmountInDB = c.getAmount();
					Integer curretCashAmountNeeded = 0;
					while (change > curretCashValue && curretCashAmountNeeded <= curretCashAmountInDB && curretCashAmountInDB > 0) {
						change -= curretCashValue;
						curretCashAmountNeeded++;
						curretCashAmountInDB--;
					}
					changeAmountLeft[index] = curretCashAmountInDB;
					index++;
				}
			}
		}
		if (change > 0.01) return false;
		return true;
	}

	public Boolean ifHasEnoughProducts(String productName, Integer value, Integer column) {
		if (column.equals(4)) return true;
		for (Product p: this.model.getProductsAllFromDB()) {
			if (p.getName().equals(productName)) {
				if (p.getAmount() <= value) return false;
			}
		}
		return true;
	}

	public Boolean ifHasProductInDB(String productName) {
		Product product = this.model.getProductFromDB(productName);
		if (product.getId() != null) return true;
		return false;
	}

	public Boolean ifHasProductInDB(Integer id) {
		Product product = this.model.getProductFromDB(id);
		if (product.getName() != null) return true;
		return false;
	}

	public Boolean ifHasUserInDB(String userName) {
		return this.model.ifHasUserInDB(userName);
	}

	public Boolean ifLoggedIn() {
		if (this.model.getCurrentUser().getName() == null) return false;
		return true;
	}

	public Boolean ifMatchCardGlobal(String name, String number) {
		for (String cardName: this.model.getCardInfoMap().keySet()) {
			if (cardName.equals(name)) {
				if (number.equals(this.model.getCardInfoMap().get(cardName))) return true;
				return false;
			}
		}
		return false;
	}

	public Boolean ifMatchUserInDB(String userName, String password) {
		return this.model.ifMatchUserInDB(userName, password);
	}
}
