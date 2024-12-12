package com.coral.utils

import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {
    @Test
    fun testValidEmails() {
        assertTrue("Should accept standard email",
            ValidationUtils.isValidEmail("test@example.com"))
        assertTrue("Should accept email with dots",
            ValidationUtils.isValidEmail("user.name@domain.co.uk"))
        assertTrue("Should accept email with plus",
            ValidationUtils.isValidEmail("user+label@domain.com"))
        assertTrue("Should accept email with numbers",
            ValidationUtils.isValidEmail("user123@domain.com"))
        assertTrue("Should accept email with underscores",
            ValidationUtils.isValidEmail("user_name@domain.com"))
    }

    @Test
    fun testInvalidEmails() {
        assertFalse("Should reject empty email",
            ValidationUtils.isValidEmail(""))
        assertFalse("Should reject blank email",
            ValidationUtils.isValidEmail("   "))
        assertFalse("Should reject email without @",
            ValidationUtils.isValidEmail("invalid.email"))
        assertFalse("Should reject email without local part",
            ValidationUtils.isValidEmail("@domain.com"))
        assertFalse("Should reject email without domain",
            ValidationUtils.isValidEmail("user@"))
        assertFalse("Should reject email with spaces",
            ValidationUtils.isValidEmail("user name@domain.com"))
        assertFalse("Should reject invalid domain",
            ValidationUtils.isValidEmail("user@domain"))
    }

    @Test
    fun testValidPasswords() {
        assertTrue("Should accept 6 character password",
            ValidationUtils.isValidPassword("123456"))
        assertTrue("Should accept longer passwords",
            ValidationUtils.isValidPassword("longpassword123"))
        assertTrue("Should accept passwords with special characters",
            ValidationUtils.isValidPassword("Pass@123"))
    }

    @Test
    fun testInvalidPasswords() {
        assertFalse("Should reject empty password",
            ValidationUtils.isValidPassword(""))
        assertFalse("Should reject short password",
            ValidationUtils.isValidPassword("12345"))
        assertFalse("Should reject blank password",
            ValidationUtils.isValidPassword("     "))
    }

    @Test
    fun testValidInput() {
        assertTrue("Should accept non-empty input",
            ValidationUtils.isValidInput("test"))
        assertTrue("Should accept numbers",
            ValidationUtils.isValidInput("123"))
        assertTrue("Should accept special characters",
            ValidationUtils.isValidInput("@#$"))
    }

    @Test
    fun testInvalidInput() {
        assertFalse("Should reject empty input",
            ValidationUtils.isValidInput(""))
        assertFalse("Should reject blank input",
            ValidationUtils.isValidInput("   "))
    }

    @Test
    fun testValidNumbers() {
        assertTrue("Should accept zero",
            ValidationUtils.isValidNumber("0"))
        assertTrue("Should accept positive integers",
            ValidationUtils.isValidNumber("123"))
        assertTrue("Should accept decimal numbers",
            ValidationUtils.isValidNumber("123.45"))
        assertTrue("Should accept scientific notation",
            ValidationUtils.isValidNumber("1.23e2"))
    }

    @Test
    fun testInvalidNumbers() {
        assertFalse("Should reject empty string",
            ValidationUtils.isValidNumber(""))
        assertFalse("Should reject non-numeric input",
            ValidationUtils.isValidNumber("abc"))
        assertFalse("Should reject special characters",
            ValidationUtils.isValidNumber("12.34.56"))
        assertFalse("Should reject multiple decimal points",
            ValidationUtils.isValidNumber("12..34"))
    }
}