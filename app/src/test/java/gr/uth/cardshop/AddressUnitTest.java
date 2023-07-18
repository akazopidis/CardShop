package gr.uth.cardshop;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.uth.cardshop.domain.Address;

public class AddressUnitTest {

    @Test
    public void testConstructor() {
        Address address = new Address();
        assertNotNull(address);
    }

    @Test
    public void testGetSetAddress() {
        Address address = new Address();
        address.setAddress("Papavasileiou 366");
        assertEquals("Papavasileiou 366", address.getAddress());
    }

    @Test
    public void testGetSetSelected() {
        Address address = new Address();
        address.setSelected(true);
        assertTrue(address.isSelected());
    }

    @Test
    public void testGetSetDocId() {
        Address address = new Address();
        address.setDocId("12");
        assertEquals("12", address.getDocId());
    }
}