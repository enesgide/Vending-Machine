package vendingmachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CashTest {

    @Test
    public void test1() {
        Cash cash = new Cash("ABC",10.0,20);
        assertEquals(cash.getName(), "ABC");
        assertEquals(cash.getValue(), 10.0);
        assertEquals(cash.getAmount(), 20);
    }


    @Test
    public void test2() {
        Cash cash = new Cash();
        cash.setName("Ten");
        cash.setAmount(30);
        cash.setValue(20.0);
        assertEquals(cash.getName(), "Ten");
        assertEquals(cash.getValue(), 20.0);
        assertEquals(cash.getAmount(), 30);
    }


    @Test
    public void test3() {
        Cash cash = new Cash("ABC",10.0,20);
        Cash duplicate = cash.duplicate();
        assertEquals(cash.getName(), duplicate.getName());
        assertEquals(cash.getValue(), duplicate.getValue());
        assertEquals(cash.getAmount(), duplicate.getAmount());
    }
}
