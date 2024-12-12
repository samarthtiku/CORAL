package com.coral.utils

import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {
    @Test
    fun testValidEmails() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"))
        assertTrue(ValidationUtils.isValidEmail("user.name@domain.co.uk"))
        assertTrue(ValidationUtils.isValidEmail("user+label@domain.com"))
    }

    @Test
    fun testInvalidEmails() {
        assertFalse(ValidationUtils.isValidEmail(""))
        assertFalse(ValidationUtils.isValidEmail("invalid.email"))
        assertFalse(ValidationUtils.isValidEmail("@domain.com"))
        assertFalse(ValidationUtils.isValidEmail("user@"))
    }
}