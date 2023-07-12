package vendingmachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class JDBCTest {

    @Test
    public void testDelete() {
        JDBC jdbc = new JDBC();
        jdbc.initDB();

		jdbc.deleteCard("Ruth");
		assertNull(jdbc.getCard("Ruth").getName());
		Card card = new Card("Ruth", "55134");
		jdbc.insertCard(card);
		assertNotNull(jdbc.getCard("Ruth").getName());
		jdbc.deleteCard(jdbc.getCard("Ruth"));
		assertNull(jdbc.getCard("Ruth").getName());

		jdbc.deleteCash("$100");
		assertNull(jdbc.getCash("$100").getValue());
		Cash cash = new Cash("$100", 100.0, 0);
		jdbc.insertCash(cash);
		jdbc.deleteCash(jdbc.getCash("$100"));
		assertNull(jdbc.getCash("$100").getName());

		jdbc.deleteProduct("Juice");
		assertNull(jdbc.getProduct("Juice").getName());
		assertNull(jdbc.getProduct(105).getName());
		Product juice = new Product(105, ProductType.DRINK, "Juice", 3.5, 7, 0);
		jdbc.insertProduct(juice);
		jdbc.deleteProduct(juice);
		assertNull(jdbc.getProduct(105).getName());

		jdbc.deleteUser("dahao");
		assertNull(jdbc.getUser("dahao").getName());
		ArrayList<Product> dahaoRecent = new ArrayList<Product>();
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		User dahao = new User("dahao", "123", dahaoRecent, UserType.NORMAL, null);
		jdbc.insertUser(dahao);
		jdbc.deleteUser(dahao);
		assertNull(jdbc.getUser("dahao").getName());
    }

	@Test
	public void testGet() {
        JDBC jdbc = new JDBC();
        jdbc.initDB();
		assertEquals(jdbc.getCard("Ruth").getName(), "Ruth");
		assertEquals(jdbc.getCash("$100").getValue(), 100.0);
		//assertEquals(jdbc.getCashAll().size(), 13);
		assertEquals(jdbc.getProduct(101).getId(), 101);
		assertEquals(jdbc.getProduct("Juice").getName(), "Juice");
		assertEquals(jdbc.getProduct("Mars").getName(), "Mars");
		assertEquals(jdbc.getProduct("Thins").getName(), "Thins");
		assertEquals(jdbc.getProduct("Mentos").getName(), "Mentos");
		assertNotNull(jdbc.getProductsAll());
		//assertEquals(jdbc.getUserAll().size(), 4);
	}
}
