package vendingmachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void test1() {
        Product product = new Product(1,ProductType.CANDY,"Candy",20.0,30,20);
        assertEquals(product.getName(),"Candy");
        assertEquals(product.getPrice(),20.0);
        assertEquals(product.getAmount(),30);
        assertEquals(product.getTotalSold(),20);
        assertEquals(product.getType(),ProductType.CANDY);
    }


    @Test
    public void test2() {
        Product product = new Product();
        product.setId(2);
        product.setAmount(30);
        product.setName("cho");
        product.setPrice(15.0);
        product.setType(ProductType.CHIP);
        Product duplicate = product.duplicate();
        assertEquals(product.getAmount(),duplicate.getAmount());
        assertEquals(product.getName(),duplicate.getName());
        assertEquals(product.getPrice(),duplicate.getPrice());
        assertEquals(product.getId(),duplicate.getId());
        assertEquals(product.getType(),duplicate.getType());
    }


    @Test
    public void test3() {
        Product product = new Product();
        product.setType(ProductType.CHIP);
        assertEquals(product.getTypeString(),"CHIP");
        product.setType(ProductType.DRINK);
        assertEquals(product.getTypeString(),"DRINK");
        product.setType(ProductType.CHOCOLATE);
        assertEquals(product.getTypeString(),"CHOCOLATE");
    }
}
