package gr.uth.cardshop;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationFormUnitTest {

    @Test
    public void testValidFullName() {
        ValidationForm vf = new ValidationForm();
        assertTrue(vf.isValidFullName("John Doe"));
        assertFalse(vf.isValidFullName("J0hn D03"));
    }

    @Test
    public void testValidEmail() {
        ValidationForm vf = new ValidationForm();
        assertTrue(vf.isValidEmail("johndoe@example.com"));
        assertFalse(vf.isValidEmail("johndoe@.com"));
    }

    @Test
    public void testValidPassword() {
        ValidationForm vf = new ValidationForm();
        assertTrue(vf.isValidPassword("Password123@"));
        assertFalse(vf.isValidPassword("password"));
    }

    @Test
    public void testValidPhone() {
        ValidationForm vf = new ValidationForm();
        assertTrue(vf.isValidPhone("1234567890"));
        assertFalse(vf.isValidPhone("123456789"));
    }

    @Test
    public void testValidAddress() {
        ValidationForm vf = new ValidationForm();
        assertTrue(vf.isValidAddress("123 Main St"));
        assertFalse(vf.isValidAddress("123 Main St #1"));
    }

    @Test
    public void testValidPostalCode() {
        ValidationForm vf = new ValidationForm();
        assertTrue(vf.isValidPostalCode("12345"));
        assertFalse(vf.isValidPostalCode("123456"));
    }

    @Test
    public void testValidCountry() {
        ValidationForm vf = new ValidationForm();
        assertTrue(vf.isValidCountry("USA"));
        assertFalse(vf.isValidCountry("United States"));
    }

    @Test
    public void testValidCity() {
        ValidationForm vf = new ValidationForm();
        assertTrue(vf.isValidCity("Larissa"));
        assertFalse(vf.isValidCity("Larissa 13"));
    }

}
