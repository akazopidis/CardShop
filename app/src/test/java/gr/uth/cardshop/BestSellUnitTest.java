package gr.uth.cardshop;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.uth.cardshop.domain.BestSell;

public class BestSellUnitTest {

    @Test
    public void testConstructor() {
        BestSell bestSell = new BestSell();
        assertNotNull(bestSell);
    }

    @Test
    public void testGetSetDescription() {
        BestSell bestSell = new BestSell();
        bestSell.setDescription("This is a description.");
        assertEquals("This is a description.", bestSell.getDescription());
    }

    @Test
    public void testGetSetName() {
        BestSell bestSell = new BestSell();
        bestSell.setName("Card Name");
        assertEquals("Card Name", bestSell.getName());
    }

    @Test
    public void testGetSetImgUrl() {
        BestSell bestSell = new BestSell();
        bestSell.setImg_url("https://example.com/card.jpg");
        assertEquals("https://example.com/card.jpg", bestSell.getImg_url());
    }

    @Test
    public void testGetSetPrice() {
        BestSell bestSell = new BestSell();
        bestSell.setPrice(4.99);
        assertEquals(4.99, bestSell.getPrice(), 0.0);
    }

    @Test
    public void testGetSetRarity() {
        BestSell bestSell = new BestSell();
        bestSell.setRarity("Rare");
        assertEquals("Rare", bestSell.getRarity());
    }

    @Test
    public void testGetSetQuantity() {
        BestSell bestSell = new BestSell();
        bestSell.setQuantity(5);
        assertEquals(5, bestSell.getQuantity());
    }

    @Test
    public void testGetSetProductId() {
        BestSell bestSell = new BestSell();
        bestSell.setProductId(13);
        assertEquals(13, bestSell.getProductId());
    }

    @Test
    public void testGetSetDocId() {
        BestSell bestSell = new BestSell();
        bestSell.setDocId("15");
        assertEquals("15", bestSell.getDocId());
    }
}
