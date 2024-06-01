package gr.uth.cardshop.forms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationForm {

    public ValidationForm(){
    }

    public boolean isValidFullName(final String name) {
        Pattern pattern;
        Matcher matcher;
        final String checkName = "^(?=.{1,20}$)[A-Za-z]+(?:\\s[A-Za-z]+)*$";

        pattern = Pattern.compile(checkName);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public boolean isValidEmail(final String email) {
        Pattern pattern;
        Matcher matcher;
        final String checkEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        pattern = Pattern.compile(checkEmail);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public boolean isValidPhone(final String phone) {
        Pattern pattern;
        Matcher matcher;
        final String checkPhone = "^\\d{10}$";

        pattern = Pattern.compile(checkPhone);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    public boolean isValidAddress(final String address) {
        Pattern pattern;
        Matcher matcher;
        final String checkAddress = "^[a-zA-Z0-9 ]{1,40}$";

        pattern = Pattern.compile(checkAddress);
        matcher = pattern.matcher(address);
        return matcher.matches();
    }
    public boolean isValidPostalCode(final String postalCode) {
        Pattern pattern;
        Matcher matcher;
        final String checkPostalCode = "^\\d{5}$";

        pattern = Pattern.compile(checkPostalCode);
        matcher = pattern.matcher(postalCode);
        return matcher.matches();
    }
    public boolean isValidCountry(final String country) {
        Pattern pattern;
        Matcher matcher;
        final String checkCountry = "^[a-zA-Z]{1,40}$";

        pattern = Pattern.compile(checkCountry);
        matcher = pattern.matcher(country);
        return matcher.matches();
    }
    public boolean isValidCity(final String city) {
        Pattern pattern;
        Matcher matcher;
        final String checkCity = "^[a-zA-Z]{1,40}$";

        pattern = Pattern.compile(checkCity);
        matcher = pattern.matcher(city);
        return matcher.matches();
    }
}
