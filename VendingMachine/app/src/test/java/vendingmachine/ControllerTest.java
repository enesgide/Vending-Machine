package vendingmachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

public class ControllerTest {

	@Test
	public void testAttributes() {
		Controller controller = new Controller();
		Model model = new Model(new JDBC(), "cards.json");

		DefaultPageView defaultPageView = new DefaultPageView();
		LoginView loginView = new LoginView(model, controller, null, null);
		RegisterView registerView = new RegisterView(model, controller, null, null, null);
		CardPayView cardPayView = new CardPayView(model, controller, null, null);
		CashPayView cashPayView = new CashPayView(model, controller, null, null);
		CashierView cashierView = new CashierView();
		SellerView sellerView = new SellerView();
		OwnerView ownerView = new OwnerView();

		controller.setModel(model);
		controller.setDefaultPageView(defaultPageView);
		controller.setLoginView(loginView);
		controller.setRegisterView(registerView);
		controller.setCardPayView(cardPayView);
		controller.setCashPayView(cashPayView);
		controller.setCashierView(cashierView);
		controller.setSellerView(sellerView);
		controller.setOwnerView(ownerView);

		assertNotNull(controller);
	}

	@Test
	public void test1() {
		
		/**
		 * The path to database and card info file.
		 * */
		String dbPath = "VM.db";
		String JSONpath = "credit_cards.json";

		// Create the database handler
		JDBC jdbc = new JDBC(dbPath);

		/**
		 * Comment to stop init the database when starting the app.
		 * */
		jdbc.initDB();

		/**
		* Create the model, for handling run time data 
		* and interacting with the database, by calling jdbc methods
		 * */
		Model model = new Model(jdbc, JSONpath);

		// Start the applicatoin with the DefaultPageView
		DefaultPageView defaultPageView = new DefaultPageView();

		// Init the controller
		Controller controller = new Controller(model, defaultPageView);

		// Set up the DefaultPageView
		defaultPageView.setController(controller);
		defaultPageView.setModel(model);

		controller.changeGroup(ProductType.CANDY);
		//controller.confirmPay(0);
		assertNotNull(controller);


		controller.setCurrentUser("Seller-01");
		assertEquals(controller.getCurrentUserType(), UserType.SELLER);
		controller.setCurrentUser("Cashier-01");
		assertEquals(controller.getCurrentUserType(), UserType.CASHIER);
		controller.setCurrentUser("Owner");
		assertEquals(controller.getCurrentUserType(), UserType.OWNER);
		controller.setCurrentUser("dahao");
		assertEquals(model.getCurrentUser().getName(), "dahao");
		assertEquals(controller.getCurrentUserType(), UserType.NORMAL);

		controller.insertProductToDB(model.getProductFromDB(101));
		controller.resetCashPayData();

		controller.register("123", "123");
		controller.produceReportCancel(null);
		controller.produceReportSuccessful(0);
		controller.produceReportSuccessful(1);
		controller.insertUserToDB(model.getUserFromDB("dahao"));
	
		assertNotNull(controller);
	}

	@Test
	public void testCheck() {
		/**
		 * The path to database and card info file.
		 * */
		String dbPath = "VM.db";
		String JSONpath = "credit_cards.json";

		// Create the database handler
		JDBC jdbc = new JDBC(dbPath);

		/**
		 * Comment to stop init the database when starting the app.
		 * */
		jdbc.initDB();

		/**
		* Create the model, for handling run time data 
		* and interacting with the database, by calling jdbc methods
		 * */
		Model model = new Model(jdbc, JSONpath);

		// Start the applicatoin with the DefaultPageView
		DefaultPageView defaultPageView = new DefaultPageView();

		// Init the controller
		Controller controller = new Controller(model, defaultPageView);

		controller.setCurrentUser("dahao");

		assertEquals(controller.ifLoggedIn(), true);
		assertEquals(controller.ifGaveEnoughMoney(), true);
		assertEquals(controller.ifHasEnoughChange(), true);
		assertEquals(controller.ifCurrentUserHasCard(), true);
		assertEquals(controller.ifCurrentUserHasCard("Ruth"), true);
		assertEquals(controller.ifHasCardGlobal("Ruth"), true);
		assertEquals(controller.ifHasProductInDB(101), true);
		assertEquals(controller.ifHasProductInDB("Juice"), true);
		assertEquals(controller.ifHasUserInDB("dahao"), true);
		assertEquals(controller.ifMatchCardGlobal("Ruth", "55134"), true);
		assertEquals(controller.ifHasEnoughProducts("Juice", 1, 5), true);
		assertEquals(controller.ifMatchUserInDB("dahao", "123"), true);

	}

	@Test
	public void testDelete() {
		/**
		 * The path to database and card info file.
		 * */
		String dbPath = "VM.db";
		String JSONpath = "credit_cards.json";

		// Create the database handler
		JDBC jdbc = new JDBC(dbPath);

		/**
		 * Comment to stop init the database when starting the app.
		 * */
		jdbc.initDB();

		/**
		* Create the model, for handling run time data 
		* and interacting with the database, by calling jdbc methods
		 * */
		Model model = new Model(jdbc, JSONpath);

		// Start the applicatoin with the DefaultPageView
		DefaultPageView defaultPageView = new DefaultPageView();

		// Init the controller
		Controller controller = new Controller(model, defaultPageView);

		// Set up the DefaultPageView
		defaultPageView.setController(controller);
		defaultPageView.setModel(model);

		controller.deleteProductInDB("Juice");
		assertNull(jdbc.getProduct("Juice").getName());
		controller.deleteUserInDB("dahao");
		assertNull(jdbc.getUser("dahao").getName());
	}

	@Test
	public void testUpdates() {
		/**
		 * The path to database and card info file.
		 * */
		String dbPath = "VM.db";
		String JSONpath = "credit_cards.json";

		// Create the database handler
		JDBC jdbc = new JDBC(dbPath);

		/**
		 * Comment to stop init the database when starting the app.
		 * */
		jdbc.initDB();

		/**
		* Create the model, for handling run time data 
		* and interacting with the database, by calling jdbc methods
		 * */
		Model model = new Model(jdbc, JSONpath);

		// Start the applicatoin with the DefaultPageView
		DefaultPageView defaultPageView = new DefaultPageView();

		HashMap<Product, Integer> selected = new HashMap<Product, Integer>();
		selected.put(jdbc.getProduct("Juice"), 1);
		selected.put(jdbc.getProduct("Mars"), 2);
		model.setSelectedProducts(selected);

		// Init the controller
		Controller controller = new Controller(model, defaultPageView);
		controller.restart();
		LoginView loginView = new LoginView(model, controller, null, null);
		RegisterView registerView = new RegisterView(model, controller, null, null, null);
		CardPayView cardPayView = new CardPayView(model, controller, null, null);
		CashPayView cashPayView = new CashPayView(model, controller, null, null);
		CashierView cashierView = new CashierView();
		SellerView sellerView = new SellerView();
		OwnerView ownerView = new OwnerView();

		controller.setLoginView(loginView);
		controller.setRegisterView(registerView);
		controller.setCardPayView(cardPayView);
		controller.setCashPayView(cashPayView);
		controller.setCashierView(cashierView);
		controller.setSellerView(sellerView);
		controller.setOwnerView(ownerView);

		controller.setCurrentUser("dahao");

		controller.updateAfterLogin();
		controller.updateCashAmount("$100", 1, 1);
		controller.updateCashAmountToDB("$100", 2, 1);
		controller.updateCardInDB("Ruth", "55134");
		controller.updateGroupedAmount("Juice", 1, 4);
		controller.updateGroupedAmount("Juice", 1, 6);
		controller.updateRecentAmount("Juice", 3, 6);
		controller.updateRecentAmount("Juice", 3, 4);
		controller.updateSelectedAmount("Juice", 3, 6);
		controller.updateSelectedAmount("Juice", 3, 4);
		controller.updateProductsAmountsToDB("Juice", 1, 4);
		controller.updateProductsAmountsToDB("Juice", 1, 6);
		controller.updateSelectedProductsAmountsToDB();
		controller.updateCardInDB("Ruth", "55134");
		controller.updateUserToDB(jdbc.getUser("dahao"));
		controller.updateUserCardInfo("Ruth");
		controller.updateRecentAfterPay();

		assertNotNull(controller);
	}





}
