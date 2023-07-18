package gr.uth.cardshop;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.uth.cardshop.domain.Items;

public class ItemsUnitTest {

    @Test
    public void testConstructor() {
        Items item = new Items();
        assertNotNull(item);
    }

    @Test
    public void testGetSetDescription() {
        Items item = new Items();
        item.setDescription("Test item description");
        assertEquals("Test item description", item.getDescription());
    }

    @Test
    public void testGetSetImgUrl() {
        Items item = new Items();
        item.setImg_url("http://test.com/image.jpg");
        assertEquals("http://test.com/image.jpg", item.getImg_url());
    }

    @Test
    public void testGetSetName() {
        Items item = new Items();
        item.setName("Test item name");
        assertEquals("Test item name", item.getName());
    }

    @Test
    public void testGetSetType() {
        Items item = new Items();
        item.setType("trap");
        assertEquals("trap", item.getType());
    }

    @Test
    public void testGetSetPrice() {
        Items item = new Items();
        item.setPrice(9.99);
        assertEquals(9.99, item.getPrice(),0.001);
    }

    @Test
    public void testGetSetDocId() {
        Items item = new Items();
        item.setDocId("6");
        assertEquals("6", item.getDocId());
    }

    @Test
    public void testGetSetRarity() {
        Items item = new Items();
        item.setRarity("common");
        assertEquals("common", item.getRarity());
    }

    @Test
    public void testGetSetQuantity() {
        Items item = new Items();
        item.setQuantity(10);
        assertEquals(10, item.getQuantity());
    }

    @Test
    public void testGetSetProductId() {
        Items item = new Items();
        item.setProductId(27);
        assertEquals(27, item.getProductId());
    }

    @Test
    public void testGetSetCurrentQuantity() {
        Items item = new Items();
        item.setCurrentQuantity(5);
        assertEquals(5, item.getCurrentQuantity());
    }

    @Test
    public void testGetDocRef() {
        Items item = new Items();
        item.setDocRef("EySfcrkO0YNyoPWlNl8w");
        assertEquals("EySfcrkO0YNyoPWlNl8w", item.getDocRef());
    }
}
