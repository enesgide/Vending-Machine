package vendingmachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

	@Test
	public void testCardConstruction() {
		Card card = new Card();
		assertNotNull(card);
		Card card2 = new Card("Ruth", "55134");
		assertNotNull(card2);
	}

	@Test
	public void testName() {
		Card card = new Card();
		card.setName("asd");
		assertEquals(card.getName(), "asd");
		card.setName("123");
		assertEquals(card.getName(), "123");
	}

	@Test
	public void testNumber() {
		Card card = new Card();
		card.setNumber("asd");
		assertEquals(card.getNumber(), "asd");
		card.setNumber("123");
		assertEquals(card.getNumber(), "123");
	}




}
