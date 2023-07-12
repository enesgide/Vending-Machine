package vendingmachine;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class JDBC {

	private String dbPath;
	private Connection dbConnection;

	public JDBC() {
		this.dbPath = "VM.db"; // auto-default database path
		this.dbConnection = null;
		tryConnectToDB();
	}

	public JDBC(String dbPath) {
		this.dbPath = dbPath;
		this.dbConnection = null;
		tryConnectToDB();
	}

	/**
	 * ======================
	 * ### INITIALISATION ###
	 * ======================
	 * */
	public void initDB() {

		// Remove old database file
		File dbFile = new File(this.dbPath);
		dbFile.delete();

		// Create a new database file
		tryConnectToDB();

		// Create tables
		createTableCard();
		createTableCash();
		createTableProducts();
		createTableGlobalRecentProducts();
		createTableUser();

		// Create and insert Card
		insertCard(new Card("Ruth", "55134"));

		// Create and insert Cash
		insertCash(new Cash("$100", 100.0, 0));
		insertCash(new Cash("$50", 50.0, 0));
		insertCash(new Cash("$20", 20.0, 0));
		insertCash(new Cash("$10", 10.0, 10));
		insertCash(new Cash("$5", 5.0, 10));
		insertCash(new Cash("$2", 2.0, 100));
		insertCash(new Cash("$1", 1.0, 100));
		insertCash(new Cash("¢50", 0.5, 100));
		insertCash(new Cash("¢20", 0.2, 100));
		insertCash(new Cash("¢10", 0.1, 100));
		insertCash(new Cash("¢5", 0.05, 100));
		insertCash(new Cash("¢2", 0.02, 100));
		insertCash(new Cash("¢1", 0.01, 100));

		// Create Products
		Product mineralWater = new Product(101, ProductType.DRINK, "Mineral Water", 3.0, 7, 0);
		Product sprite = new Product(102, ProductType.DRINK, "Sprite", 2.0, 7, 0);
		Product cocaCola = new Product(103, ProductType.DRINK, "Coca Cola", 2.0, 7, 0);
		Product pepsi = new Product(104, ProductType.DRINK, "Pepsi", 2.0, 7, 0);
		Product juice = new Product(105, ProductType.DRINK, "Juice", 3.5, 7, 0);

		Product mars = new Product(201, ProductType.CHOCOLATE, "Mars", 1.0, 7, 0);
		Product mm = new Product(202, ProductType.CHOCOLATE, "M&M", 2.0, 7, 0);
		Product bounty = new Product(203, ProductType.CHOCOLATE, "Bounty", 2.0, 7, 0);
		Product snickers = new Product(204, ProductType.CHOCOLATE, "Snickers", 3.0, 7, 0);

		Product smiths = new Product(301, ProductType.CHIP, "Smiths", 2.0, 7, 0);
		Product pringles = new Product(302, ProductType.CHIP, "Pringles", 2.5, 7, 0);
		Product kettle = new Product(303, ProductType.CHIP, "Kettle", 2.0, 7, 0);
		Product thins = new Product(304, ProductType.CHIP, "Thins", 3.0, 7, 0);

		Product mentos = new Product(401, ProductType.CANDY, "Mentos", 1.0, 7, 0);
		Product sourpatch = new Product(402, ProductType.CANDY, "Sour Patch", 1.0, 7, 0);
		Product skittles = new Product(403, ProductType.CANDY, "Skittles", 1.0, 7, 0);

		// Insert Products
		insertProduct(mineralWater);
		insertProduct(sprite);
		insertProduct(cocaCola);
		insertProduct(pepsi);
		insertProduct(juice);

		insertProduct(mars);
		insertProduct(mm);
		insertProduct(bounty);
		insertProduct(snickers);

		insertProduct(smiths);
		insertProduct(pringles);
		insertProduct(kettle);
		insertProduct(thins);

		insertProduct(mentos);
		insertProduct(sourpatch);
		insertProduct(skittles);


		// Init global recent products
		ArrayList<Product> globalRecent = new ArrayList<Product>();
		globalRecent.add(mineralWater.duplicate());
		globalRecent.add(cocaCola.duplicate());
		globalRecent.add(mars.duplicate());
		globalRecent.add(smiths.duplicate());
		globalRecent.add(mentos.duplicate());

		// Insert global recent products
		insertRecentAll(globalRecent);

		// Create and insert sample users
		ArrayList<Product> dahaoRecent = new ArrayList<Product>();
		dahaoRecent.add(cocaCola.duplicate());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		User dahao = new User("dahao", "123", dahaoRecent, UserType.NORMAL, "Ruth");
		insertUser(dahao);

		ArrayList<Product> sellerRecent = new ArrayList<Product>();
		sellerRecent.add(new Product());
		sellerRecent.add(new Product());
		sellerRecent.add(new Product());
		sellerRecent.add(new Product());
		sellerRecent.add(new Product());
		User seller = new User("Seller-01", "123", sellerRecent, UserType.SELLER, null);
		insertUser(seller);

		ArrayList<Product> cashierRecent = new ArrayList<Product>();
		cashierRecent.add(new Product());
		cashierRecent.add(new Product());
		cashierRecent.add(new Product());
		cashierRecent.add(new Product());
		cashierRecent.add(new Product());
		User cashier = new User("Cashier-01", "123", cashierRecent, UserType.CASHIER, null);
		insertUser(cashier);

		// Create and insert a Owner
		ArrayList<Product> ownerRecent = new ArrayList<Product>();
		ownerRecent.add(new Product());
		ownerRecent.add(new Product());
		ownerRecent.add(new Product());
		ownerRecent.add(new Product());
		ownerRecent.add(new Product());
		User owner = new User("Owner", "123", ownerRecent, UserType.OWNER, null);
		insertUser(owner);
	}

	/**
	 * ==============
	 * ### CREATE ###
	 * ==============
	 * */
	public void createTableCard() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: createTableCard connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS CARD" +
							"(" + 
								"NAME varchar(255)," +
								"NUMBER varchar(255)" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: createTableCard closed");
			} catch (SQLException e) {
				System.out.println("From createTableCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void createTableCash() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: createTableCash connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS CASH" +
							"(" + 
								"NAME varchar(255)," +
								"VALUE DOUBLE," + 
								"AMOUNT INT" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableCash");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: createTableCash closed");
			} catch (SQLException e) {
				System.out.println("From createTableCash");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void createTableGlobalRecentProducts() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: createTableGlobalRecentProducts connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS RECENT" +
							"(" + 
								"PRODUCT_1_ID INT," + 
								"PRODUCT_2_ID INT," + 
								"PRODUCT_3_ID INT," + 
								"PRODUCT_4_ID INT," + 
								"PRODUCT_5_ID INT" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableGlobalRecentProducts");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: createTableGlobalRecentProducts closed");
			} catch (SQLException e) {
			System.out.println("From createTableGlobalRecentProducts");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void createTableProducts() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: createTableProducts connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
							"(" + 
								"ID INT," + 
								"NAME varchar(255)," +
								"TYPE varchar(255)," + 
								"PRICE DOUBLE," + 
								"AMOUNT INT," + 
								"TOTALSOLD INT" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableProducts");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: createTableProducts closed");
			} catch (SQLException e) {
			System.out.println("From createTableProducts");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void createTableUser() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: createTableUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS USER" +
							"(" + 
								"NAME varchar(255)," +
								"PASSWORD varchar(255)," + 
								"RECENT_PRODUCT_1_ID INT," + 
								"RECENT_PRODUCT_2_ID INT," + 
								"RECENT_PRODUCT_3_ID INT," + 
								"RECENT_PRODUCT_4_ID INT," + 
								"RECENT_PRODUCT_5_ID INT," + 
								"TYPE varchar(255)," + 
								"CARD_NAME varchar(255)" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: createTableUser closed");
			} catch (SQLException e) {
				System.out.println("From createTableUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * ==============
	 * ### DELETE ###
	 * ==============
	 * */
	public void deleteCard(String name) {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: deleteCard connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "DELETE FROM CARD WHERE NAME='" + name + "';";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From deleteCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: deleteCard closed");
			} catch (SQLException e) {
				System.out.println("From deleteCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}

	}

	public void deleteCard(Card card) {
		deleteCard(card.getName());
	}

	public void deleteCash(String name) {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: deleteCash connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "DELETE FROM CASH WHERE NAME='" + name + "';";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From deleteCash");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: deleteCash closed");
			} catch (SQLException e) {
				System.out.println("From deleteCash");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void deleteCash(Cash cash) {
		deleteCash(cash.getName());
	}

	public void deleteProduct(String name) {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: deleteProduct connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "DELETE FROM PRODUCT WHERE NAME='" + name + "';";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From deleteProduct");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: deleteProduct closed");
			} catch (SQLException e) {
				System.out.println("From deleteProduct");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void deleteProduct(Product product) {
		deleteProduct(product.getName());
	}
	
	public void deleteUser(String name) {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: deleteUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "DELETE FROM USER WHERE NAME='" + name + "';";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From deleteUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: deleteUser closed");
			} catch (SQLException e) {
				System.out.println("From deleteUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void deleteUser(User user) {
		deleteUser(user.getName());
	}


	/**
	 * ===========
	 * ### GET ###
	 * ===========
	 * */

	public Card getCard(String name) {
		Card card = new Card();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getCard connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM CARD WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Retrieve values back 
				String card_name = resultSet.getString("NAME");
				String card_number = resultSet.getString("NUMBER");
				// Set value to the product to return
				card.setName(card_name);
				card.setNumber(card_number);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getCard closed");
			} catch (SQLException e) {
				System.out.println("From getCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return card;
	}
	
	public Cash getCash(String name) {
		Cash cash = new Cash();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getCash connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM CASH WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Retrieve values back 
				String cash_name = resultSet.getString("NAME");
				Double cash_value = resultSet.getDouble("VALUE");
				Integer cash_amount = resultSet.getInt("AMOUNT");
				// Set value to the product to return
				cash.setName(cash_name);
				cash.setValue(cash_value);
				cash.setAmount(cash_amount);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getCash closed");
			} catch (SQLException e) {
				System.out.println("From getCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return cash;
	}

	public ArrayList<Cash> getCashAll() {
		ArrayList<Cash> cashMap = new ArrayList<Cash>();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getCashAll connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM CASH;";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String cashName = resultSet.getString("NAME");
				Double cashValue = resultSet.getDouble("VALUE");
				Integer cashAmount = resultSet.getInt("AMOUNT");
				cashMap.add(new Cash(cashName, cashValue, cashAmount));
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getCashAll");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getCashAll connected");
			} catch (SQLException e) {
				System.out.println("From getCashAll");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return cashMap;
	}

	public Product getProduct(String name) {
		Product product = new Product();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getProduct connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM PRODUCT WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Retrieve values back 
				Integer product_id = resultSet.getInt("ID");
				String product_name = resultSet.getString("NAME");
				String product_type = resultSet.getString("TYPE");
				Double product_price = resultSet.getDouble("PRICE");
				Integer product_amount = resultSet.getInt("AMOUNT");
				Integer product_totalSold = resultSet.getInt("TOTALSOLD");
				// Set value to the product to return
				product.setId(product_id);
				product.setName(product_name);
				if (product_type.equals("DRINK")) product.setType(ProductType.DRINK);
				else if (product_type.equals("CHOCOLATE")) product.setType(ProductType.CHOCOLATE);
				else if (product_type.equals("CHIP")) product.setType(ProductType.CHIP);
				else if (product_type.equals("CANDY")) product.setType(ProductType.CANDY);
				product.setPrice(product_price);
				product.setAmount(product_amount);
				product.setTotalSold(product_totalSold);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getProduct");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getProduct closed");
			} catch (SQLException e) {
				System.out.println("From getProduct");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return product;
	}

	public Product getProduct(Integer id) {
		Product product = new Product();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getProduct connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM PRODUCT WHERE ID=" + id + ";";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Retrieve values back 
				Integer product_id = resultSet.getInt("ID");
				String product_name = resultSet.getString("NAME");
				String product_type = resultSet.getString("TYPE");
				Double product_price = resultSet.getDouble("PRICE");
				Integer product_amount = resultSet.getInt("AMOUNT");
				Integer product_totalSold = resultSet.getInt("TOTALSOLD");
				// Set value to the product to return
				product.setId(product_id);
				product.setName(product_name);
				if (product_type.equals("DRINK")) product.setType(ProductType.DRINK);
				else if (product_type.equals("CHOCOLATE")) product.setType(ProductType.CHOCOLATE);
				else if (product_type.equals("CHIP")) product.setType(ProductType.CHIP);
				else if (product_type.equals("CANDY")) product.setType(ProductType.CANDY);
				product.setPrice(product_price);
				product.setAmount(product_amount);
				product.setTotalSold(product_totalSold);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getProduct");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getProduct closed");
			} catch (SQLException e) {
				System.out.println("From getProduct");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return product;
	}

	public ArrayList<Product> getProductsAll() {
		ArrayList<Product> allProducts = new ArrayList<Product>();
		for (Product p: getProductsByType(ProductType.DRINK)) {
			allProducts.add(p);
		}
		for (Product p: getProductsByType(ProductType.CHOCOLATE)) {
			allProducts.add(p);
		}
		for (Product p: getProductsByType(ProductType.CHIP)) {
			allProducts.add(p);
		}
		for (Product p: getProductsByType(ProductType.CANDY)) {
			allProducts.add(p);
		}
		return allProducts;
	}

	public ArrayList<Product> getProductsByType(ProductType ptype) {
		// Convert product type into string
		String type = "";
		if (ptype.equals(ProductType.DRINK)) type = "DRINK";
		else if (ptype.equals(ProductType.CHOCOLATE)) type = "CHOCOLATE";
		else if (ptype.equals(ProductType.CHIP)) type = "CHIP";
		else if (ptype.equals(ProductType.CANDY)) type = "CANDY";
		// Get all the products with the same type
		ArrayList<Product> products = new ArrayList<Product>();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getProductsByType connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM PRODUCT WHERE TYPE='" + type + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Integer product_id = resultSet.getInt("ID");
				Product product = getProduct(product_id);
				products.add(product);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getProductsByType");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getProductsByType closed");
			} catch (SQLException e) {
				System.out.println("From getProductsByType");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return products;
	}

	public ArrayList<Product> getRecent() {
		ArrayList<Product> recent = new ArrayList<Product>();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getRecent connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM RECENT;";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				for (int index = 1; index < 6; index++) {
					Integer product_id = resultSet.getInt("PRODUCT_" + index + "_ID");
					Product product = getProduct(product_id);
					recent.add(product);
				}
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getRecent");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getRecent closed");
			} catch (SQLException e) {
				System.out.println("From getRecent");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return recent;
	}
	
	public User getUser(String name) {
		User user = new User();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM USER WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Retrieve values back 
				String user_name = resultSet.getString("NAME");
				String user_password = resultSet.getString("PASSWORD");
				Integer user_product_1_id = resultSet.getInt("RECENT_PRODUCT_1_ID");
				Product user_product_1 = getProduct(user_product_1_id);
				Integer user_product_2_id = resultSet.getInt("RECENT_PRODUCT_2_ID");
				Product user_product_2 = getProduct(user_product_2_id);
				Integer user_product_3_id = resultSet.getInt("RECENT_PRODUCT_3_ID");
				Product user_product_3 = getProduct(user_product_3_id);
				Integer user_product_4_id = resultSet.getInt("RECENT_PRODUCT_4_ID");
				Product user_product_4 = getProduct(user_product_4_id);
				Integer user_product_5_id = resultSet.getInt("RECENT_PRODUCT_5_ID");
				Product user_product_5 = getProduct(user_product_5_id);
				String user_type = resultSet.getString("TYPE");
				if (user_type.equals("NORMAL")) user.setType(UserType.NORMAL);
				else if (user_type.equals("CASHIER")) user.setType(UserType.CASHIER);
				else if (user_type.equals("SELLER")) user.setType(UserType.SELLER);
				else if (user_type.equals("OWNER")) user.setType(UserType.OWNER);
				String user_card = resultSet.getString("CARD_NAME");
				// Set value to the product to return
				user.setName(user_name);
				user.setPassword(user_password);
				user.setRecentProduct(0, user_product_1);
				user.setRecentProduct(1, user_product_2);
				user.setRecentProduct(2, user_product_3);
				user.setRecentProduct(3, user_product_4);
				user.setRecentProduct(4, user_product_5);
				if (user_card.equals("NULL")) user.setCardName(null);
				else user.setCardName(user_card);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getUser closed");
			} catch (SQLException e) {
				System.out.println("From getUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return user;
	}

	public ArrayList<User> getUserAll() {
		ArrayList<String> userNameList = new ArrayList<String>();
		ArrayList<User> userList = new ArrayList<User>();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: getUserAll connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM USER;";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String userName = resultSet.getString("NAME");
				userNameList.add(userName);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getUserAll");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: getUserAll closed");
			} catch (SQLException e) {
				System.out.println("From getUserAll");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		for (String userName: userNameList) {
			userList.add(getUser(userName));
		}
		return userList;
	}


	/**
	 * =============
	 * ### CHECK ###
	 * =============
	 * */
	public boolean ifHashCard(String name) {
		boolean return_value = true;
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: ifHashCard connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM CARD WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			if (!resultSet.isBeforeFirst()) {
				return_value = false;
			} else {
				return_value = true;
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From ifHasCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: ifHashCard closed");
			} catch (SQLException e) {
				System.out.println("From ifHasCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return return_value;
	}

	public boolean ifHasUser(String name) {
		boolean return_value = true;
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: ifHasUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM USER WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			if (!resultSet.isBeforeFirst()) {
				return_value = false;
			} else {
				return_value = true;
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From ifHasUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: ifHasUser closed");
			} catch (SQLException e) {
				System.out.println("From ifHasUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return return_value;
	}

	public boolean ifMatchUser(String name, String password) {
		boolean return_value = false;
		if (ifHasUser(name)) {
			User user = getUser(name);
			if (user.getPassword().equals(password)) {
				return_value = true;
			}
		}
		return return_value;
	}


	/**
	 * ==============
	 * ### INSERT ###
	 * ==============
	 * */
	public void insertCard(Card card) {
		String name = card.getName();
		String number = card.getName();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: insertCard connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO CARD " +
							"(NAME, NUMBER) " +
							"VALUES ('" + name + "', '" + number + "');";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From insertCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: insertCard closed");
			} catch (SQLException e) {
				System.out.println("From insertCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void insertCash(Cash cash) {
		String name = cash.getName();
		Double value = cash.getValue();
		Integer amount = cash.getAmount();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: insertCash connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO CASH " +
							"(NAME, VALUE, AMOUNT) " +
							"VALUES ('" + name + "', " + value + ", " + amount + ");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From insertCash");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: insertCash closed");
			} catch (SQLException e) {
				System.out.println("From insertCash");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void insertProduct(Product product) {
		Integer id = product.getId();
		String typeString = product.getTypeString();
		String name = product.getName();
		Double price = product.getPrice();
		Integer amount = product.getAmount();
		Integer totalSold = product.getTotalSold();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: insertProduct connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO PRODUCT " +
							"(ID, TYPE, NAME, PRICE, AMOUNT, TOTALSOLD) " +
							"VALUES (" + id + ", '" + typeString + "', '" + name + "', " + price + ", " + amount + ", " + totalSold  + ");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From insertProduct");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: insertProduct closed");
			} catch (SQLException e) {
				System.out.println("From insertProduct");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void insertRecentAll(ArrayList<Product> recentProducts) {
		Integer id_1 = recentProducts.get(0).getId();
		Integer id_2 = recentProducts.get(1).getId();
		Integer id_3 = recentProducts.get(2).getId();
		Integer id_4 = recentProducts.get(3).getId();
		Integer id_5 = recentProducts.get(4).getId();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: insertRecentAll connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO RECENT " +
							"(PRODUCT_1_ID, PRODUCT_2_ID, PRODUCT_3_ID, PRODUCT_4_ID, PRODUCT_5_ID) " +
							"VALUES (" + id_1 + ", " + id_2 + ", " + id_3 + ", " + id_4 + ", " + id_5 + ");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From insertRecentAll");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: insertRecentAll closed");
			} catch (SQLException e) {
				System.out.println("From insertRecentAll");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void insertUser(User user) {
		String name = user.getName();
		String password = user.getPassword();
		ArrayList<Product> products = user.getRecentProducts();
		String cardName = user.getCardName();
		if (cardName == null) cardName = "NULL";
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: insertUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO USER " +
							"(NAME, PASSWORD, RECENT_PRODUCT_1_ID, RECENT_PRODUCT_2_ID, RECENT_PRODUCT_3_ID, RECENT_PRODUCT_4_ID, RECENT_PRODUCT_5_ID, TYPE, CARD_NAME) " +
							"VALUES ('" + 
										name + "', '" + 
										password + "', " + 
										products.get(0).getId() + ", " + 
										products.get(1).getId() + ", " + 
										products.get(2).getId() + ", " + 
										products.get(3).getId() + ", " + 
										products.get(4).getId() + ", '" +
										user.getTypeString() + "', '" + 
										cardName + 
									"');";
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			System.out.println("From insertUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: insertUser closed");
			} catch (SQLException e) {
				System.out.println("From insertUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * ==============
	 * ### UPDATE ###
	 * ==============
	 * */
	public void updateCard(Card card) {
		deleteCard(card.getName());
		insertCard(card);
	}

	public void updateCash(Cash cash) {
		deleteCash(cash.getName());
		insertCash(cash);
	}

	public void updateProduct(Product product) {
		deleteProduct(product.getName());
		insertProduct(product);
	}

	public void updateUser(User user) {
		deleteUser(user.getName());
		insertUser(user);
	}

	public void updateGlobalRecent(ArrayList<Product> recentProducts) {
		Integer id_1 = recentProducts.get(0).getId();
		Integer id_2 = recentProducts.get(1).getId();
		Integer id_3 = recentProducts.get(2).getId();
		Integer id_4 = recentProducts.get(3).getId();
		Integer id_5 = recentProducts.get(4).getId();

		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: updateGlobalRecent connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "UPDATE RECENT SET " + 
				"PRODUCT_1_ID=" + id_1 + ", " + 
				"PRODUCT_2_ID=" + id_2 + ", " + 
				"PRODUCT_3_ID=" + id_3 + ", " + 
				"PRODUCT_4_ID=" + id_4 + ", " + 
				"PRODUCT_5_ID=" + id_5 + 
				";";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From updateGlobalRecent");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: updateGlobalRecent closed");
			} catch (SQLException e) {
				System.out.println("From updateGlobalRecent");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}



	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	/**
	 * Try to connect to the database
	 * A new database will be created if the database specified by the path doesn't exist
	 * */
	private void tryConnectToDB() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
// System.out.println("JDBC: tryConnectToDB connected");
		} catch (Exception e) {
			System.out.println("From tryConnectToDB");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}finally {
			try {
				this.dbConnection.close();
// System.out.println("JDBC: tryConnectToDB closed");
			} catch (SQLException e) {
				System.out.println("From tryConnectToDB");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}
}
