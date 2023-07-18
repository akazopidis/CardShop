package gr.uth.cardshop;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.uth.cardshop.domain.Category;

public class CategoryUnitTest {

    @Test
    public void testConstructor() {
        Category category = new Category();
        assertNotNull(category);
    }

    @Test
    public void testGetSetType() {
        Category category = new Category();
        category.setType("Spell");
        assertEquals("Spell", category.getType());
    }

    @Test
    public void testGetSetImgUrl() {
        Category category = new Category();
        category.setImg_url("http://test.com/test.jpg");
        assertEquals("http://test.com/test.jpg", category.getImg_url());
    }
}
