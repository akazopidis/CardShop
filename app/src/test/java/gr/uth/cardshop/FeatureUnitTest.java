package gr.uth.cardshop;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.uth.cardshop.domain.Feature;

public class FeatureUnitTest {
    @Test
    public void testConstructor() {
        Feature feature = new Feature();
        assertNotNull(feature);
    }

    @Test
    public void testGetSetDescription() {
        Feature feature = new Feature();
        feature.setDescription("Test description");
        assertEquals("Test description", feature.getDescription());
    }

    @Test
    public void testGetSetImgUrl() {
        Feature feature = new Feature();
        feature.setImg_url("https://example.com/test.jpg");
        assertEquals("https://example.com/test.jpg", feature.getImg_url());
    }

    @Test
    public void testGetSetName() {
        Feature feature = new Feature();
        feature.setName("Test name");
        assertEquals("Test name", feature.getName());
    }

    @Test
    public void testGetSetPrice() {
        Feature feature = new Feature();
        feature.setPrice(9.99);
        assertEquals(9.99, feature.getPrice(), 0.001);
    }

    @Test
    public void testGetSetRarity() {
        Feature feature = new Feature();
        feature.setRarity("Common");
        assertEquals("Common", feature.getRarity());
    }

    @Test
    public void testGetSetQuantity() {
        Feature feature = new Feature();
        feature.setQuantity(5);
        assertEquals(5, feature.getQuantity());
    }

    @Test
    public void testGetSetDocId() {
        Feature feature = new Feature();
        feature.setDocId("5");
        assertEquals("5", feature.getDocId());
    }

    @Test
    public void testGetSetProductId() {
        Feature feature = new Feature();
        feature.setProductId(1);
        assertEquals(1, feature.getProductId());
    }
}
