package gr.uth.cardshop;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.uth.cardshop.domain.Orders;

public class OrdersUnitTest {

    @Test
    public void testConstructor() {
        Orders order = new Orders();
        assertNotNull(order);
    }

    @Test
    public void testGetSetName() {
        Orders order = new Orders();
        order.setName("Test order name");
        assertEquals("Test order name", order.getName());
    }

    @Test
    public void testGetSetAddress() {
        Orders order = new Orders();
        order.setAddress("Papavasileiou 366");
        assertEquals("Papavasileiou 366", order.getAddress());
    }

    @Test
    public void testGetSetImgUrl() {
        Orders order = new Orders();
        order.setImg_url("http://test.com/image.jpg");
        assertEquals("http://test.com/image.jpg", order.getImg_url());
    }

    @Test
    public void testGetSetfName() {
        Orders order = new Orders();
        order.setfName("Zane Jordan");
        assertEquals("Zane Jordan", order.getfName());
    }

    @Test
    public void testGetSetEmail() {
        Orders order = new Orders();
        order.setEmail("zane@example.com");
        assertEquals("zane@example.com", order.getEmail());
    }

    @Test
    public void testGetSetAmount() {
        Orders order = new Orders();
        order.setAmount(9.99);
        assertEquals(9.99, order.getAmount(), 0.001);
    }

    @Test
    public void testGetSetRarity() {
        Orders order = new Orders();
        order.setRarity("Super Rare");
        assertEquals("Super Rare", order.getRarity());
    }

    @Test
    public void testGetSetQuantity() {
        Orders order = new Orders();
        order.setQuantity(10);
        assertEquals(10, order.getQuantity());
    }

    @Test
    public void testGetSetDocId() {
        Orders order = new Orders();
        order.setDocId("23");
        assertEquals("23", order.getDocId());
    }

}