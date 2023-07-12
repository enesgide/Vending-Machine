package vendingmachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelTest {

	@Test
	public void testModelConstruction() {
		Model model = new Model(new JDBC(), "cards.json");
		assertNotNull(model);
	}

	@Test 
	public void testJDBC() {
		Model model = new Model(new JDBC(), "card.json");
		JDBC jdbc = new JDBC();
		model.setJDBC(jdbc);
		assertEquals(model.getJDBC(), jdbc);
	}

	@Test
	public void testJSONpath() {
		Model model = new Model(new JDBC(), "card.json");
		String path = "cards_record.json";
		model.setJSONpath(path);
		assertEquals(model.getJSONpath(), path);
	}

	@Test 
	public void testCurrentUser() {
		Model model = new Model(new JDBC(), "card.json");
		User user = new User();
		model.setCurrentUser(user);
		assertEquals(model.getCurrentUser(), user);
	}

	@Test
	public void testTotalPrice() {
		Model model = new Model(new JDBC(), "card.json");
		Double tp = 0.0;
		model.setTotalPrice(tp);
		assertEquals(model.getTotalPrice(), tp);
	}

	@Test
	public void testCurrentPrice() {
		Model model = new Model(new JDBC(), "card.json");
		Double cp = 0.0;
		model.setCurrentPrice(cp);
		assertEquals(model.getCurrentPrice(), cp);
	}

	@Test
	public void testRecentProducts() {
		Model model = new Model(new JDBC(), "card.json");
		HashMap<Product, Integer> recent = new HashMap<Product, Integer>();
		model.setRecentProducts(recent);
		assertEquals(model.getRecentProducts(), recent);
	}

	@Test
	public void testGroupedProducts() {
		Model model = new Model(new JDBC(), "card.json");
		HashMap<Product, Integer> grouped = new HashMap<Product, Integer>();
		model.setGroupedProducts(grouped);
		assertEquals(model.getGroupedProducts(), grouped);
	}

	@Test
	public void testSelectedProducts() {
		Model model = new Model(new JDBC(), "card.json");
		HashMap<Product, Integer> selected = new HashMap<Product, Integer>();
		model.setSelectedProducts(selected);
		assertEquals(model.getSelectedProducts(), selected);
	}

	@Test
	public void testCashMap() {
		Model model = new Model(new JDBC(), "card.json");
		HashMap<Cash, Integer> cp = new HashMap<Cash, Integer>();
		model.setCashMap(cp);
		assertEquals(model.getCashMap(), cp);
	}

	@Test
	public void testCardInfoMap() {
		Model model = new Model(new JDBC(), "card.json");
		HashMap<String, String> cip = new HashMap<String, String>();
		model.setCardInfoMap(cip);
		assertEquals(model.getCardInfoMap(), cip);
	}

	@Test
	public void testMaintainDB() {
		JDBC jdbc = new JDBC();
		jdbc.initDB();
		Model model = new Model(jdbc, "cards.json");

		Card card = new Card("cardName", "cardNumber");
		Cash cash = new Cash("cashName", 0.0, 0);
		Product product = new Product(0, ProductType.CANDY, "productName", 0.0, 0, 0);
		ArrayList<Product> recentProducts = new ArrayList<Product>();
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		User user = new User("userName", "userPassword", recentProducts, UserType.CASHIER, "Ruth");

		// Delete
		model.deleteCardInDB(null);
		model.deleteCashInDB(null);
		model.deleteUserInDB(null);
		model.deleteProductInDB(null);
		assertNotNull(model);
		// Get
		assertEquals(model.getCardFromDB("Ruth").getName(), jdbc.getCard("Ruth").getName());
		assertEquals(model.getCashFromDB("$100").getName(), jdbc.getCash("$100").getName());
		assertEquals(model.getProductFromDB(101).getName(), jdbc.getProduct(101).getName());
		assertEquals(model.getProductFromDB("Juice").getName(), jdbc.getProduct("Juice").getName());
		assertEquals(model.getProductsAllFromDB().size(), jdbc.getProductsAll().size());
		assertEquals(model.getUserAllFromDB().size(), jdbc.getUserAll().size());
		assertEquals(model.getUserFromDB("dahao").getPassword(), jdbc.getUser("dahao").getPassword());
		// Insert
		model.insertCardToDB(card);
		model.insertCashToDB(cash);
		model.insertUserToDB(user);
		model.insertProductToDB(product);
		assertNotNull(model);
		// Update
		model.updateCardToDB(card);
		model.updateCashToDB(cash);
		model.updateUserToDB(user);
		model.updateProductToDB(product);
		model.updateGlobalRecentToDB(recentProducts);
		assertNotNull(model);
		// Check
		assertEquals(model.ifHasCardInDB("Ruth"), jdbc.ifHashCard("Ruth"));
		assertEquals(model.ifHasUserInDB("dahao"), jdbc.ifHasUser("dahao"));
		assertEquals(model.ifMatchUserInDB("dahao", "123"), jdbc.ifMatchUser("dahao", "123"));
	}





}
