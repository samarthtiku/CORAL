package com.coral.utils

object ValidationUtils {
    // Email regex pattern
    private val EMAIL_PATTERN = Regex(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && EMAIL_PATTERN.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidInput(input: String): Boolean {
        return input.isNotBlank()
    }

    fun isValidNumber(input: String): Boolean {
        return try {
            input.toDouble() >= 0
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}