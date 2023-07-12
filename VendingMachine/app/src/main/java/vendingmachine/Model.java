package vendingmachine;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Model {

	// Run time data
	private JDBC jdbc;
	private String JSONpath;

	private User currentUser;
	private Double totalPrice;
	private Double currentPrice;

	private HashMap<Product, Integer> recentProducts;
	private HashMap<Product, Integer> groupedProducts;
	private HashMap<Product, Integer> selectedProducts;

	private HashMap<Cash, Integer> cashMap;

	private HashMap<String, String> cardInfoMap;


	public Model(JDBC jdbc, String JSONpath) {

		this.jdbc = jdbc;
		this.JSONpath = JSONpath;

		this.currentUser = new User();
		this.totalPrice = 0.0;
		this.currentPrice = 0.0;

		// Recent Products
		this.recentProducts = new HashMap<Product, Integer>();
		for (Product p: getRecentProductsFromDB()) {
			this.recentProducts.put(p, 0);
		}
		// Grouped Products
		this.groupedProducts = new HashMap<Product, Integer>();
		for (Product p: getProductsByTypeFromDB(ProductType.DRINK)) {
			Product newProduct = p.duplicate();
			this.groupedProducts.put(newProduct, 0);
		}
		// Selected Products
		this.selectedProducts = new HashMap<Product, Integer>();
		
		// CashMap
		this.cashMap = new HashMap<Cash, Integer>();
		for (Cash c: getCashAllFromDB()) {
			this.cashMap.put(c, 0);
		}
		
		// Card Info
		setCardInfoMapFromPath(JSONpath);
	}

	/**
	 * ###########################
	 * ### Maintain Attributes ###
	 * ###########################
	 * */

	public JDBC getJDBC() {
		return this.jdbc;
	}

	public void setJDBC(JDBC jdbc) {
		this.jdbc = jdbc;
	}

	public String getJSONpath() {
		return this.JSONpath;
	}

	public void setJSONpath(String JSONpath) {
		this.JSONpath = JSONpath;
	}

	public User getCurrentUser() {
		return this.currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public Double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getCurrentPrice() {
		return this.currentPrice;
	}

	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public HashMap<Product, Integer> getRecentProducts() {
		return this.recentProducts;
	}

	public void setRecentProducts(HashMap<Product, Integer> recentProducts) {
		this.recentProducts = recentProducts;
	}

	public HashMap<Product, Integer> getGroupedProducts() {
		return this.groupedProducts;
	}

	public void setGroupedProducts(HashMap<Product, Integer> groupedProducts) {
		this.groupedProducts = groupedProducts;
	}

	public HashMap<Product, Integer> getSelectedProducts() {
		return this.selectedProducts;
	}

	public void setSelectedProducts(HashMap<Product, Integer> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public HashMap<Cash, Integer> getCashMap() {
		return this.cashMap;
	}

	public void setCashMap(HashMap<Cash, Integer> cashMap) {
		this.cashMap = cashMap;
	}

	public HashMap<String, String> getCardInfoMap() {
		return this.cardInfoMap;
	}

	public void setCardInfoMap(HashMap<String, String> cardInfoMap) {
		this.cardInfoMap = cardInfoMap;
	}

	public void setCardInfoMapFromPath(String JSONpath) {
		this.cardInfoMap = new HashMap<String, String>();
 		JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader(JSONpath));
            JSONArray jsonArray = (JSONArray) object;
            for (Object cardObj: jsonArray) {
                JSONObject cardDetails = (JSONObject) cardObj;
                String name = (String) cardDetails.get("name");
                String number = (String) cardDetails.get("number");
				this.cardInfoMap.put(name, number);
            }
        } catch (Exception e) {
            System.out.println("Card reader error");
        }
	}




	/**
	 * #########################
	 * ### Maintain Database ###
	 * #########################
	 * */

	public void deleteCardInDB(String name) {
		this.jdbc.deleteCard(name);
	}

	public void deleteCashInDB(String name) {
		this.jdbc.deleteCash(name);
	}

	public void deleteProductInDB(String name) {
		this.jdbc.deleteProduct(name);;
	}

	public void deleteUserInDB(String name) {
		this.jdbc.deleteUser(name);;
	}

	public Card getCardFromDB(String name) {
		return this.jdbc.getCard(name);
	}
	
	public Cash getCashFromDB(String name) {
		return this.jdbc.getCash(name);
	}

	public ArrayList<Cash> getCashAllFromDB() {
		return this.jdbc.getCashAll();
	}

	public Product getProductFromDB(String name) {
		return this.jdbc.getProduct(name);
	}

	public Product getProductFromDB(Integer id) {
		return this.jdbc.getProduct(id);
	}

	public ArrayList<Product> getProductsAllFromDB() {
		return this.jdbc.getProductsAll();
	}

	public ArrayList<Product> getProductsByTypeFromDB(ProductType ptype) {
		return this.jdbc.getProductsByType(ptype);
	}

	public ArrayList<Product> getRecentProductsFromDB() {
		return this.jdbc.getRecent();
	}

	public ArrayList<User> getUserAllFromDB() {
		return this.jdbc.getUserAll();
	}

	public User getUserFromDB(String name) {
		return this.jdbc.getUser(name);
	}

	public void insertCardToDB(Card card) {
		this.jdbc.insertCard(card);
	}

	public void insertCashToDB(Cash cash) {
		this.jdbc.insertCash(cash);
	}

	public void insertProductToDB(Product product) {
		this.jdbc.insertProduct(product);
	}

	public void insertUserToDB(User user) {
		this.jdbc.insertUser(user);
	}

	public void updateCardToDB(Card card) {
		this.jdbc.updateCard(card);
	}

	public void updateCashToDB(Cash cash) {
		this.jdbc.updateCash(cash);
	}

	public void updateProductToDB(Product product) {
		this.jdbc.updateProduct(product);
	}

	public void updateUserToDB(User user) {
		this.jdbc.updateUser(user);
	}

	public void updateGlobalRecentToDB(ArrayList<Product> recentProducts) {
		this.jdbc.updateGlobalRecent(recentProducts);
	}



	/**
	 * #####################
	 * ### Other Methods ###
	 * #####################
	 * */

	public Boolean ifHasCardInDB(String name) {
		return this.jdbc.ifHashCard(name);
	}

	public Boolean ifHasUserInDB(String userName) {
		return this.jdbc.ifHasUser(userName);
	}

	public Boolean ifMatchUserInDB(String userName, String password) {
		return this.jdbc.ifMatchUser(userName, password);
	}
}
