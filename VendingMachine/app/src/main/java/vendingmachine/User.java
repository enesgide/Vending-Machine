package vendingmachine;

import java.util.ArrayList;

public class User {

	private String name;
	private String password;
	private ArrayList<Product> recentProducts;
	private UserType type;
	private String cardName;

	public User() {
		this.name = null;
		this.password = null;
		this.recentProducts = new ArrayList<Product>();
		this.recentProducts.add(null);
		this.recentProducts.add(null);
		this.recentProducts.add(null);
		this.recentProducts.add(null);
		this.recentProducts.add(null);
		this.type = null;
		this.cardName = null;
	}

	public User(String name, String password, ArrayList<Product> recentProducts, UserType type, String cardName) {
		this.name = name;
		this.password = password;
		this.recentProducts = recentProducts;
		this.type = type;
		this.cardName = cardName;
	}

	public String getCardName() {
		return this.cardName;
	}

	public String getName() {
		return this.name;
	}

	public String getPassword() {
		return this.password;
	}

	public ArrayList<Product> getRecentProducts() {
		return this.recentProducts;
	}

	public UserType getType() {
		return this.type;
	}

	public String getTypeString() {
		if (this.type.equals(UserType.NORMAL)) return "NORMAL";
		else if (this.type.equals(UserType.CASHIER)) return "CASHIER";
		else if (this.type.equals(UserType.SELLER)) return "SELLER";
		else if (this.type.equals(UserType.OWNER)) return "OWNER";
		else return null;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRecentProduct(ArrayList<Product> recentProducts) {
		this.recentProducts = recentProducts;
	}

	public void setRecentProduct(Integer index, Product product) {
		index = index % 5;
		this.recentProducts.add(index, product);
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String toString() {
		String output = "";
		output += "Name: " + this.name + "\n";
		output += "Password: " + this.password + "\n";
		output += "Rencet Products: " + this.recentProducts.get(0).getName() + ", ";
		output += this.recentProducts.get(1).getName() + ", ";
		output += this.recentProducts.get(2).getName() + ", ";
		output += this.recentProducts.get(3).getName() + ", ";
		output += this.recentProducts.get(4).getName() + ", ";
		output += "Type: " + getTypeString() + ", ";
		output += "CardName: " + (this.cardName == null ? "NULL" : this.cardName);
		return output;
	}
}
