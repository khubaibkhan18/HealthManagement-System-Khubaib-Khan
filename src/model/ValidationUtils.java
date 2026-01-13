package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationUtils {
    
    // Validate date format
    public static boolean isValidDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    // Validate NHS number (10 digits)
    public static boolean isValidNhsNumber(String nhsNumber) {
        if (nhsNumber == null || nhsNumber.trim().isEmpty()) {
            return false;
        }
        // Remove spaces
        nhsNumber = nhsNumber.replaceAll("\\s+", "");
        if (nhsNumber.length() != 10) {
            return false;
        }
        try {
            Long.parseLong(nhsNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Validate UK phone number (starts with 07, 11 digits)
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Remove spaces, dashes, parentheses
        phone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return phone.matches("07\\d{9}");
    }
    
    // Validate email format
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    // Validate postcode 
    public static boolean isValidPostcode(String postcode) {
        if (postcode == null) return false;
        return postcode.matches("^[A-Z]{1,2}[0-9][A-Z0-9]?\\s?[0-9][A-Z]{2}$");
    }
    
    // Validate age 
    public static boolean isValidNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}