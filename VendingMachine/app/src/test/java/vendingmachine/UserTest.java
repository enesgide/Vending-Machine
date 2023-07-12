package vendingmachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class UserTest {

	@Test
	public void testUserConstruction() {
		User user1 = new User();
		assertNotNull(user1);
		User user2 = new User("test", "123", new ArrayList<Product>(), null, null);
		assertNotNull(user2);	
	}

	@Test
	public void testName() {
		User user = new User();
		user.setName("test");
		assertEquals(user.getName(), "test");
		user.setName("test2");
		assertEquals(user.getName(), "test2");
	}

	@Test
	public void testPassword() {
		User user = new User();
		user.setPassword("pwd");
		assertEquals(user.getPassword(), "pwd");
		user.setPassword("123");
		assertEquals(user.getPassword(), "123");
	}

	@Test
	public void testRecentProducts() {
		User user = new User();
		ArrayList<Product> recent = new ArrayList<Product>();
		user.setRecentProduct(recent);
		assertEquals(user.getRecentProducts(), recent);
		Product product = new Product()	;
		recent.add(product);
		user.setRecentProduct(recent);
		user.setRecentProduct(0, product);
		assertEquals(user.getRecentProducts(), recent);
	}

	@Test
	public void testType() {
		User user = new User();
		user.setType(UserType.CASHIER);
		assertEquals(user.getType(), UserType.CASHIER);
		user.setType(UserType.NORMAL);
		assertEquals(user.getType(), UserType.NORMAL);
	}

    @Test
    public void testTypeString() {
		User user = new User();
        user.setType(UserType.NORMAL);
        assertEquals(user.getTypeString(), "NORMAL");
        user.setType(UserType.OWNER);
        assertEquals(user.getTypeString(), "OWNER");
        user.setType(UserType.SELLER);
        assertEquals(user.getTypeString(), "SELLER");
		user.setType(UserType.CASHIER);
		assertEquals(user.getTypeString(), "CASHIER");
    }

	@Test
	public void testCardName() {
		User user = new User();
		user.setCardName("123");
		assertEquals(user.getCardName(), "123");
		user.setCardName("asd");
		assertEquals(user.getCardName(), "asd");
	}

    @Test
    public void testToString() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1,ProductType.CANDY,"Candy",20.0,30,20));
        products.add(new Product(2,ProductType.CHOCOLATE,"CHOCOLATE",20.0,30,25));
        products.add(new Product(3,ProductType.CHIP,"CHIP",22.0,30,30));
        products.add(new Product(4,ProductType.DRINK,"DRINK",23.0,30,40));
        products.add(new Product(5,ProductType.DRINK,"tt",12.0,30,40));

        User user = new User("tom","123456", products, UserType.CASHIER, "ABC");
        String expected = "Name: tom\n" +
                "Password: 123456\nRencet Products: Candy, CHOCOLATE, CHIP, DRINK, tt, Type: CASHIER, CardName: ABC";
        assertEquals(user.toString(), expected);
		
		User user2 = new User("tom","123456", products, UserType.CASHIER, null);
		String expected2 = "Name: tom\n" +
                "Password: 123456\nRencet Products: Candy, CHOCOLATE, CHIP, DRINK, tt, Type: CASHIER, CardName: NULL";
        assertEquals(user2.toString(), expected2);
    }

}
